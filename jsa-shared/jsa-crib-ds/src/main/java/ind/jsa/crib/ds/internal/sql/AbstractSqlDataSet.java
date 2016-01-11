package ind.jsa.crib.ds.internal.sql;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.DataSetQuery.FilterExpression;
import ind.jsa.crib.ds.api.DataSetQuery.FilterOperator;
import ind.jsa.crib.ds.api.DataSetQuery.SortDirection;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetProperty;
import ind.jsa.crib.ds.api.IDataSetResultHandler;
import ind.jsa.crib.ds.internal.AbstractDataSet;
import ind.jsa.crib.ds.internal.DataSetItem;
import ind.jsa.crib.ds.internal.DataSetMetaData;
import ind.jsa.crib.ds.internal.DataSetProperty;
import ind.jsa.crib.ds.internal.IdListDataSetResultHandler;
import ind.jsa.crib.ds.internal.ListDataSetResultHandler;
import ind.jsa.crib.ds.internal.PropertyNameUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import ind.jsa.crib.ds.internal.sql.SqlCommand;
import ind.jsa.crib.ds.internal.type.plugins.StdTypeManagerPlugin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

/**
 * Base SQL implementation of the DataSet interface.
 * 
 */
public abstract class AbstractSqlDataSet extends AbstractDataSet {

	private static final String DEFAULT_ID_REGEX = "^(_?[iI][dD])$";
	private static final String DEFAULT_IDREF_REGEX = "^\\w(_[iI][dD])$";
	
    /**
     * Alias of wrapped inner query.
     */
    protected static final String INNER_QUERY_ALIAS = "q";

    /**
     * Max number of in clause expressions before looping.
     */
    protected static final int MAX_IN_CLAUSE_ITEMS = 1000;

    /**
     * The default wildcard character to user for LIKE expressions.
     */
    protected static final char DEFAULT_WILDCARD_CHAR = '%';

    /**
     * The default string buffer size for generated SQL statements.
     */
    protected static final int INIT_SQL_SIZE = 500;

    /**
     * The default array buffer size for lists of fields/properties.
     */
    protected static final int INIT_FLD_COUNT = 50;

    private String sequence;
    private NamedParameterJdbcTemplate dbclient;
    private SqlCommand retrieveCmd;
    private SqlCommand createCmd;
    private SqlCommand updateCmd;
    private SqlCommand deleteCmd;
    private SqlCommand newKeyCmd;
    private String idNameRegex = DEFAULT_ID_REGEX;
    private String idRefNameRegex = DEFAULT_IDREF_REGEX;
    private Pattern idNamePattern;
    private Pattern idRefNamePattern;
    
	public AbstractSqlDataSet(String entity, String domain, NamedParameterJdbcTemplate dbclient) {
		super(entity, domain);
		
        this.dbclient = dbclient;
		
		idNamePattern = Pattern.compile(idNameRegex);
		idRefNamePattern = Pattern.compile(idRefNameRegex);
	}

    /**
     * If specified, a sequence is used to identify an Oracle sequence which will be used to generate a new id
     * when creating records.
     * 
     * @param sequence The sequence name
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * Get the sequence name.
     * 
     * @return The sequence name
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Specify the command that will be used to retrieve data from the dataset.
     * 
     * @param cmd The retrieval command.
     */
    public void setRetrieveCommand(SqlCommand cmd) {
        this.retrieveCmd = cmd;
    }

    /**
     * Specify the command that will be used to create records in the dataset.
     * 
     * @param cmd The create command
     */
    public void setCreateCommand(SqlCommand cmd) {
        this.createCmd = cmd;
    }

    /**
     * Specify the command that will be used to update records in the dataset.
     * 
     * @param cmd The update command
     */
    public void setUpdateCommand(SqlCommand cmd) {
        this.updateCmd = cmd;
    }

    /**
     * Specify the command that will be used to delete records from the dataset.
     * 
     * @param cmd The delete command
     */
    public void setDeleteCommand(SqlCommand cmd) {
        this.deleteCmd = cmd;
    }

    /**
     * Specify the command that will be used to create generate a new set of primary keys for the dataset.
     * 
     * @param cmd The command to set
     */
    public void setNewKeyCmd(SqlCommand cmd) {
        this.newKeyCmd = cmd;
    }

    /**
     * Set the regular expression for identifying id property names.
     * 
     * @param idNameRegex A regular expression
     */
    public void setIdNameRegex(String idNameRegex) {
		this.idNameRegex = idNameRegex;
	}

    /**
     * Set the regular expression for identifying id reference property names.
     * 
     * @param idNameRegex A regular expression
     */
	public void setIdRefNameRegex(String idRefNameRegex) {
		this.idRefNameRegex = idRefNameRegex;
	}

	/*
     * (non-Javadoc)
     * 
     * @see com.copyright.ds.DataSet#retrieve(com.copyright.ds.DataSetQuery,
     * com.copyright.ds.DataSetResultHandler)
     */
    @Override
    public void retrieve(DataSetQuery query, IDataSetResultHandler handler) {
        SqlCommand cmd = retrieveCmd != null ? retrieveCmd : getDefaultRetrieveCommand();

        if (cmd == null) {
            return;
        }

        if (query.getMaxRows() > 0 && query.getStartRow() == 0) {
            query.setStartRow(1);
        }

        try {
            Map<String, Object> params = getQueryRetrieveParameters(cmd, query);

            handler.processStart();
            fetchRows(cmd, query, handler, params);
            handler.processEnd();
        } catch (Exception ex) {
            throw new RuntimeException("Error executing query: ", ex);
        }
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#retrieve(java.util.Map)
     */
    @Override
    public IDataSetItem retrieve(Map<String, Object> params) {
        SqlCommand cmd = retrieveCmd != null ? retrieveCmd : getDefaultRetrieveCommand();

        if (cmd == null) {
            return null;
        }

        // Filter the dataset with a query initialized with the given keys
        DataSetQuery query = new DataSetQuery();

        // first store parameters in query and remove them from input map
        for (String p : cmd.getParams()) {
            if (params.containsKey(p)) {
                query.putParam(p, params.get(p));
            }
        }

        // Only return one row. If there are more that meet the criteria
        // they are ignored.
        query.setMaxRows(1);

        ListDataSetResultHandler handler = new ListDataSetResultHandler(this, query);

        // fetch the data and accumulate in the handler
        retrieve(query, handler);

        // return the first row
        return handler.getItems().size() > 0 ? handler.getItems().get(0) : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.copyright.ds.DataSet#create(java.util.Map)
     */
    @Override
    public IDataSetItem create(IDataSetItem item) {
        SqlCommand cmd = createCmd != null ? createCmd : getDefaultCreateCommand(item);

        if (cmd == null) {
            return null;
        }

        // attempt to seed the values with a set of newly
        // generated keys
        String idPropName = getIdPropertyName();
        if (!StringUtils.isEmpty(idPropName)) {
        	item.put(idPropName, getNewKeyValue(item));
        }

        // invoke the insert
        dbclient.update(cmd.getSql(), item);

        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.copyright.ds.DataSet#update(java.util.Map)
     */
    @Override
    public IDataSetItem update(IDataSetItem item) {

        SqlCommand cmd = updateCmd != null ? updateCmd : getDefaultUpdateCommand(item, null);

        if (cmd != null) {
            dbclient.update(cmd.getSql(), item);
        }

        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.copyright.ds.DataSet#update(java.util.Map)
     */
    @Override
    public void update(DataSetQuery query, IDataSetItem item) {
        String idPropName = getIdPropertyName();

        // this call works for single id datasets only
        if (StringUtils.isEmpty(idPropName)) {
            return;
        }
        
        // Retrieve the list of ids impacted by the update
        DataSetQuery idQuery = new DataSetQuery();

        // copy the original params to the new query object
        for (Entry<String, Object> e : query.getParams().entrySet()) {
            idQuery.putParam(e.getKey(), e.getValue());
        }

        // use filters provided by original query to select the ids
        for (FilterExpression expr : query.getFilters().values()) {
            idQuery.putFilter(expr.getOperand(), expr.getOperator(), expr.getValue(),
                    expr.getUpperBoundValue());
        }

        Set<String> fields = new HashSet<String>(1);
        fields.add(idPropName);
        // we're only interested in the key field
        idQuery.setFields(fields);

        // fetch the id list
        IdListDataSetResultHandler handler = new IdListDataSetResultHandler(this, query, idPropName);
        retrieve(idQuery, handler);

        List<Object> idList = handler.getIds();
        List<Object> updateIds = null;
        
        while (!CollectionUtils.isEmpty(idList)) {
        	
        	if (idList.size() <= MAX_IN_CLAUSE_ITEMS) {
        		updateIds = new ArrayList<Object>(idList);
        		idList = null;
        	} else {
        		updateIds = idList.subList(0, MAX_IN_CLAUSE_ITEMS);
        		idList = idList.subList(MAX_IN_CLAUSE_ITEMS, idList.size());
        	}
        	
	        // set up an update command based on a single filter for ids
	        idQuery.clearFilters();
	        idQuery.putFilter(idPropName, FilterOperator.ONE_OF, updateIds);
	        SqlCommand cmd = updateCmd != null ? updateCmd : 
	        	getDefaultUpdateCommand(item, idQuery.getFilters());
	
	        item.put(idPropName, updateIds);
	        dbclient.update(cmd.getSql(), item);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.copyright.ds.DataSet#delete(java.util.Map)
     */
    @Override
    public void delete(Map<String, Object> key) {
        SqlCommand cmd = deleteCmd != null ? deleteCmd : getDefaultDeleteCommand();

        if (cmd != null) {
            dbclient.update(cmd.getSql(), key);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.copyright.ds.DataSet#getItemCount(com.copyright.ds.DataSetQuery)
     */
    @Override
    public int getItemCount(DataSetQuery query) {
        SqlCommand cmd = retrieveCmd != null ? retrieveCmd : getDefaultRetrieveCommand();

        if (cmd == null) {
            return 0;
        }

        int rowCt = 0;

        try {
            rowCt = fetchRowCount(cmd, query, getQueryRetrieveParameters(cmd, query));
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving row count: ", ex);
        }

        return rowCt;
    }

    /**
     * Get the default retrieval statement.
     * 
     * @return The default retrieval statement
     */
    protected SqlCommand getDefaultRetrieveCommand() {
        SqlCommand cmd = null;

        if (getEntity() != null) {
            StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);

            sql.append("select * from ");
            if (getDomain() != null) {
                sql.append(getDomain()).append('.');
            }
            sql.append(getEntity());

            cmd = new SqlCommand(sql.toString());
        }

        return cmd;
    }

    /**
     * Creates a default insert command based on the currently specified core table and the give set of field
     * names. If keys are specified for the dataset, a check is made to ensure that these fields are included
     * when constructing the command.
     * 
     * @param values The values to consider
     * @return A SqlCommand, or null if no table is specified
     */
    protected SqlCommand getDefaultCreateCommand(Map<String, Object> values) {
        StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);

        // Extract writable field names from value map
        Set<String> fields = new LinkedHashSet<String>(values.size());
        for (String k : values.keySet()) {
            if (isWritableProperty(k)) {
                fields.add(k);
            }
        }

        if (getEntity() != null && fields.size() > 0) {
            // statement preamble
            sql.append("insert into ");
            if (getDomain() != null) {
                sql.append(getDomain()).append('.');
            }
            sql.append(getEntity()).append('(');

            // Construct the fields portion of the insert statement
            int ct = 0;
            for (String p : fields) {
                if (ct++ > 0) {
                    sql.append(',');
                }
                sql.append(p);
            }

            // Ensure to include keys in the statement if specified for the
            // dataset if not passed in as part of the given fields
            String idPropName = getIdPropertyName();
            if (!StringUtils.isEmpty(idPropName)) {
                 if (!fields.contains(idPropName)) {
                    if (ct++ > 0) {
                        sql.append(',');
                    }
                    sql.append(idPropName);
                 }
            }

            // Construct the values portion of the insert statement
            ct = 0;
            sql.append(") values (");
            for (String p : fields) {
                if (ct++ > 0) {
                    sql.append(',');
                }

                if (!isEmpty(values.get(p))) {
                    sql.append(treatParamExpr(":" + p));
                } else {
                    sql.append("null");
                }
            }

            // ensure to include value keys in the statement if
            // specified for the dataset and not passed in as
            // part of the given fields
            if (!StringUtils.isEmpty(idPropName)) {
                if (!fields.contains(idPropName)) {
                    if (ct++ > 0) {
                        sql.append(',');
                    }
                    sql.append(':').append(idPropName);
                }
            }

            sql.append(')');
        }

        return sql.length() > 0 ? new SqlCommand(sql.toString()) : null;
    }

    /**
     * Creates a default update command based on the currently specified core table and the give set of field
     * names. It is assumed that keys are specified for the dataset. These keys are used to build the where
     * clause of the statement. If no keys are specified, this method will return a null result.
     * 
     * @param values The values to consider
     * @param filters The filters to consider
     * @return A SqlCommand, or null if no table is specified
     */
    protected SqlCommand getDefaultUpdateCommand(Map<String, Object> values,
            Map<String, FilterExpression> filters) {
        if (getEntity() == null) {
            return null;
        }

        SqlCommand cmd = null;
        Set<String> fields = values.keySet();
        String idPropName = getIdPropertyName();

        if (!StringUtils.isEmpty(idPropName)) {
            StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);

            // preamble
            sql.append("update ");
            if (getDomain() != null) {
                sql.append(getDomain()).append('.');
            }
            sql.append(getEntity()).append(" set ");

            // set up the column=:value portion of the statement
            int ct = 0;
            for (String fld : fields) {
                if (!fld.equals(idPropName) && isWritableProperty(fld)) { // exclude keys
                    if (ct++ > 0) {
                        sql.append(',');
                    }
                    sql.append(fld);
                    if (!isEmpty(values.get(fld))) {
                        sql.append("=").append(treatParamExpr(":" + fld));
                    } else {
                        sql.append("=null");
                    }
                }
            }

            // If specified, use filters to select rows to update
            if (filters != null && filters.size() > 0) {
                appendFilterWhereClause(filters, sql, false);
            } else if (!StringUtils.isEmpty(idPropName)) { // Otherwise attempt to use keys
                sql.append(" where ");
                sql.append(idPropName).append("=:").append(idPropName);
            }

            cmd = new SqlCommand(sql.toString());
        }

        return cmd;
    }

    /**
     * Creates a default delete command based on the currently specified core table and the give set of field
     * names. It is assumed that keys are specified for the dataset. These keys are used to build the where
     * clause of the statement. If no keys are specified, this method will return a null result.
     * 
     * @return A SqlCommand, or null if no table is specified
     */
    protected SqlCommand getDefaultDeleteCommand() {
        SqlCommand cmd = null;

        String idPropName = getIdPropertyName();
        if (!StringUtils.isEmpty(idPropName)) {
            // preamble
            StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);
            sql.append("delete from ");
            if (getDomain() != null) {
                sql.append(getDomain()).append('.');
            }

            // where clause
            sql.append(getEntity()).append(" where ");
            sql.append(idPropName).append("=:").append(idPropName);

            cmd = new SqlCommand(sql.toString());
        }

        return cmd;
    }

    /**
     * Creates a default command for generating new keys for inserted records based on the currently specified
     * Oracle sequence name. Although specified keys are not used to generate the statement, keys must be
     * specified in order for this command to return a non-null result. It is presumed that if no keys are
     * specified, that there is no need to generate key values.
     * 
     * @return A SqlCommand, or null if no table is specified
     */
    protected abstract SqlCommand getDefaultNewKeyCommand();

    /**
     * Build the command that will be used to generate a count of records for the specified command and query.
     * 
     * @param cmd The core SQL command
     * @param query The quer to use to wrap the core command
     * 
     * @return A sql command
     */
    protected abstract String formulateRetrieveCountCommand(SqlCommand cmd, DataSetQuery query);

    /**
     * Convert the given sql retrieval command to one which is limited by the starting row and max row values
     * for the given query object.
     * 
     * @param cmd The command to limit
     * @param query The query defining the limit
     * 
     * @return The resulting sqlq
     */
    protected abstract String formulateRowLimitedQuery(SqlCommand cmd, DataSetQuery query);

    /**
     * Generate the necessary preamble for rounding a date value to a single day.
     * 
     * @return A string that will prefix the date column name to be rounded to a single day
     */
    protected abstract String formulateDateTruncFunctionPrefix();

    /**
     * Generate the necessary terminating suffix for rounding a date value to a single day.
     * 
     * @return A string that will suffix the date column name to be rounded to a single day
     */
    protected String formulateDateTruncFunctionSuffix() {
        return ")";
    }

    /**
     * Generate the core, wrapped SQL query.
     * 
     * @param cmd The primary SQL command to wrap
     * @param query The query that will given the wrapping around the primary SQL
     * @param sql The buffer to append results to
     */
    protected void appendCoreQuery(SqlCommand cmd, DataSetQuery query, StringBuilder sql) {
        addInnerQueryClause(cmd, query, sql);
        sql.append(" ");
        addOrderByClause(query, sql);
    }

    /**
     * Helper function for determining whether row limiters are specified in the given query.
     * 
     * @param query The query to consider
     * 
     * @return True/false indicator
     */
    protected boolean rowLimitersSpecified(DataSetQuery query) {
        return query.getStartRow() > 0 || (query.getMaxRows() > 0 && query.getMaxRows() < Integer.MAX_VALUE);
    }

    /**
     * Get a list of identifiers representing the properties to be displayed by the current dataset and query.
     * 
     * @param query The query to consider
     * 
     * @return A list of property identifiers, in the proper display order
     */
    protected List<String> getDisplayableFields(DataSetQuery query) {
        List<String> props = new ArrayList<String>(INIT_FLD_COUNT);

        if (query.getStar()) {
            for (String propname : getOrderedPropertyNames()) {
                props.add(propname);
            }
        } else {
            for (String f : query.getFields()) {
                props.add(f);
            }
        }

        return props;
    }

    /**
     * Append each displayable field to the query string.
     * 
     * @param query Query defining displayable fields
     * @param sql Result buffer
     */
    protected void addDisplayableFields(DataSetQuery query, StringBuilder sql) {
        List<String> flds = getDisplayableFields(query);
        int ct = 0;
        for (String f : flds) {
            if (ct > 0) {
                sql.append(", ");
            }

            sql.append(treatSelectedField(INNER_QUERY_ALIAS + "." + f));
            
            ct++;
        }
    }

    /**
     * Formulate the core query by combining the given retrieval command with the criteria indicated in the
     * given query.
     * 
     * @param cmd The retrieval command
     * @param query The query defining the criteria
     * @param sql Result buffer
     */
    protected void addInnerQueryClause(SqlCommand cmd, DataSetQuery query, StringBuilder sql) {
        // Wrap the command as an inner select
        sql.append("(").append(cmd.getSql()).append(") ").append(INNER_QUERY_ALIAS);
        appendFilterWhereClause(query.getFilters(), sql, true);
    }

    /**
     * This method appends a WHERE clause to the given sql buffer based on the specified set of filters.
     * 
     * @param filters The filters used to build up the WHERE clause
     * @param sql The sql buffer to append the clause to
     * @param isInner If the clause is being generated around a wrapped inner select, then prefix filtered
     *            fields with the inner query alias.
     */
    protected void appendFilterWhereClause(
    	Map<String, FilterExpression> filters, StringBuilder sql, boolean isInner) {
    	
        int ct = 0;
        for (FilterExpression filter : filters.values()) {
            // If we can't read a property, we can't filter on it
            if (!isFilterableProperty(filter.getOperand())) {
                continue;
            }

            FilterOperator op = filter.getOperator();

            // This is just used for an empty-value check
            boolean hasValue = filter.getValue() != null;
            if (hasValue && filter.getValue() instanceof Collection) {
                hasValue = ((Collection<?>) filter.getValue()).size() > 0;
            }

            // Is this a null/not null operator
            boolean isNullOperator = op == FilterOperator.NULL || op == FilterOperator.NOT_NULL;

            if (isNullOperator || hasValue) {
                // Are we specifying an IN clause
                boolean isInOperator = op == FilterOperator.ONE_OF || op == FilterOperator.NOT_ONE_OF;

                // Are we specifying a BETWEEN clause
                boolean isBetweenOperator = op == FilterOperator.BETWEEN || op == FilterOperator.NOT_BETWEEN;

                // Is the given value a date?
                boolean isDateVal = !isNullOperator && filter.getValue() instanceof Date;

                // First iteration is WHERE, else AND
                if (ct == 0) {
                    sql.append(" WHERE ");
                } else if (ct > 0) {
                    sql.append(" AND ");
                }

                // generally, dates are queried on granularity of 1 day
                if (!isNullOperator && isDateVal) {
                    sql.append(formulateDateTruncFunctionPrefix());
                }

                // determine if it's a string field
                boolean stringField =  isStringProperty(filter.getOperand());

                // determine if upper case treatment should be ued
                boolean useCaseInsensitiveSearch = isCaseInsensitiveSearch()
                        && isIdProperty(filter.getOperand());

                // Wrap string operand with case-insensitive uppercase treatment
                sql.append(stringField && useCaseInsensitiveSearch ? "UPPER(" : "");

                // append inner alias if wrapping an inner select
                if (isInner) {
                    sql.append(INNER_QUERY_ALIAS).append('.');
                }

                // Field name
                sql.append(filter.getOperand());

                // Finish wrapping string operand with case-insensitive uppercase treatment
                sql.append(stringField && useCaseInsensitiveSearch ? ")" : "");

                // End TRUNC(
                if (!isNullOperator && isDateVal) {
                    sql.append(formulateDateTruncFunctionSuffix());
                }

                // SQL operator
                sql.append(' ').append(op.getSqlOp()).append(' ');

                // Value
                if (!isNullOperator) {
                    if (isInOperator) {
                        sql.append("(");
                    }

                    sql.append(":").append(filter.getOperand());

                    if (isBetweenOperator) {
                        // Upper bound expression
                        sql.append(" AND :").append(filter.getOperand())
                                .append(DataSetQuery.UPPERBOUND_NAME_SUFFIX);
                    }

                    if (isInOperator) {
                        sql.append(")");
                    }
                }

                ct++;
            }
        }
    }

    /**
     * Append an order-by clause implied by the given query.
     * 
     * @param query The query defining the order
     * @param sql Result buffer
     */
    protected void addOrderByClause(DataSetQuery query, StringBuilder sql) {
        Map<String, SortDirection> sorts = query.getSorts();
        if (sorts.size() > 0) {
            sql.append(" order by ");
            int ct = 0;

            for (Map.Entry<String, SortDirection> sort : sorts.entrySet()) {
                if (ct > 0) {
                    sql.append(", ");
                }

                // Determine if it's a String property
                IDataSetProperty prop = getMetaData().getProperty(sort.getKey());
                boolean stringField = isStringProperty(prop.getName());

                // determine if upper case treament should be ued
                boolean useCaseInsensitiveSearch = isCaseInsensitiveSearch()
                        && !isIdProperty(sort.getKey());

                // Wrap string operand with case-insensitive uppercase treatment
                sql.append(stringField && useCaseInsensitiveSearch ? "UPPER(" : "");

                // Field name
                sql.append(sort.getKey());

                // Finish wrapping string operand with case-insensitive uppercase treatment
                sql.append(stringField && useCaseInsensitiveSearch ? ")" : "");

                sql.append(' ');
                sql.append(sort.getValue().getDirection()); // sort direction

                ct++;
            }
        }
    }

    /**
     * Append fields to select from the inner query based on the given query.
     * 
     * @param query The query defining the selected fields
     * @param sql Result buffer
     */
    protected void addSelectedFields(DataSetQuery query, StringBuilder sql) {
        int ct = 0;
        
        for (String fld : getDisplayableFields(query)) {
            if (ct > 0) {
                sql.append(", ");
            }

            sql.append(treatSelectedField(INNER_QUERY_ALIAS + "." + fld));
            
            ct++;
        }
    }

    /**
     * Indicates whether the given object is either a string, or a list of strings.
     * 
     * @param value The value to check
     * @return An indicator
     */
    protected boolean isStringOrStringList(Object value) {
        boolean isString = false;

        if (value instanceof String) {
            isString = true;
        } else if (value instanceof List) {
            Object firstItem = ((List<?>) value).get(0);
            if (firstItem instanceof String) {
                isString = true;
            }
        }

        return isString;
    }

    /**
     * Issue a command to fetch the number of rows that would be returned for a given sql command.
     * 
     * @param cmd The core select statement
     * @param query The query used to wrap the core statement
     * @param params Parameters used in the query
     * 
     * @return A row count
     */
    protected int fetchRowCount(SqlCommand cmd, DataSetQuery query, Map<String, Object> params) {
        String sql = formulateRetrieveCountCommand(cmd, query);
        Integer ct = dbclient.queryForObject(sql, params, Integer.class);
        return ct !=null ? ct.intValue() : 0;
    }

    /**
     * Fetch rows of data for the given command, query, and input parameters. Process the results according to
     * the given handler.
     * 
     * @param cmd The core select statement
     * @param query The query used to wrap the core statement
     * @param handler The result handler
     * @param params Parameters used in the query
     */
    protected void fetchRows(
    	SqlCommand cmd, DataSetQuery query, IDataSetResultHandler handler, Map<String, Object> params) {
    	
    	String sql = formulateRetrieveCommand(cmd, query);
    	
        dbclient.query(
        	sql, params,
        	new QueryRowHandler(this, query, handler));
    }

    /**
     * Issue a database call to retrieve a new set of keys for an entry in the dataset.
     * 
     * @param params The parameter values to consider
     * 
     * @return A collection of name/value pairs representing the new keys and their values.
     */
    protected Object getNewKeyValue(Map<String, Object> params) {
        Object keyValue = null;

        String idPropName = getIdPropertyName();
        if (!StringUtils.isEmpty(idPropName)) {
	        List<String> keyIds = new ArrayList<String>(1);
	        keyIds.add(idPropName);
        
            SqlCommand keyCmd = newKeyCmd != null ? newKeyCmd : getDefaultNewKeyCommand();
            if (keyCmd != null) {
                SingleRowHandler handler = new SingleRowHandler(this, keyIds);
                dbclient.query(keyCmd.getSql(), params, handler);

                DataSetItem item = handler.getItem();
                if (item != null) {
                	keyValue = item.get(idPropName);
                }
            } else if (getKeyGenerator() != null) {
            	keyValue = getKeyGenerator().generateKeyValue(this);
            }
        }

        return keyValue;
    }

    /**
     * For the given command, get a map of default values for the parameters associated with the command.
     * 
     * @param cmd The SQL command to parse.
     * @return The map of default parameters
     */
    protected Map<String, Object> getDefaultCommandParameters(SqlCommand cmd) {
        Map<String, Object> params = new HashMap<String, Object>();

        for (String pname : cmd.getParams()) {
            Object value = getDefaultParameterValues().get(pname);
            if (value == null) {
                value = "";
            }

            params.put(pname, value);
        }

        return params;
    }

    /**
     * Convert the specified query into a simple collection of name/value pairs.
     * 
     * Any values that are used in a "contains" filter will be wrapped with wildcard chars if not present.
     * 
     * String values will be converted to upper case if case insensitive searching is true for the dataSet.
     * 
     * Date values will be converted to a Date object without a time dimension (java.sql.Date).
     * 
     * @param cmd The core retrieval statement
     * @param query The query being applied
     * 
     * @return A map of values
     */
    protected Map<String, Object> getQueryRetrieveParameters(SqlCommand cmd, DataSetQuery query) {
        // start with defaults
        Map<String, Object> params = getDefaultCommandParameters(cmd);

        List<String> containsOperands = getOperandsWithContainsOperator(query);

        IDataSetItem item = new DataSetItem(this, query.getParameters());
        
        char wc = getWildCardCharacter();

        // Place query parameters into output collection
        for (Map.Entry<String, Object> qparam : item.entrySet()) {
            String key = qparam.getKey();
            Object value = qparam.getValue();

            // Skip null values
            if (value == null) {
                continue;
            }

            // Special treatment of strings. Check whether CONTAINS operator should be added,
            // and whether or not to convert string search criteria to upper case
            if (value instanceof String) {
                String str = ((String) value).trim();
                
                if (isCaseInsensitiveSearch()) {
                	value = str.toUpperCase();
                }

                if (containsOperands != null && containsOperands.contains(key)) {

                    if (str.indexOf(wc) == -1) {
                        value = wc + str + wc; // add wildcards
                    }
                }
            }

            params.put(key, value);
        }
        
        return params;

    }

    /**
     * If the query contains any filters that use the "contains" operator, return a list of the corresponding
     * operands (i.e. field names).
     * 
     * @param query the DataSetQuery object that has the filter list
     * 
     * @return a list of operands that are used in "contains" filters
     */
    protected List<String> getOperandsWithContainsOperator(DataSetQuery query) {
        List<String> operands = new ArrayList<String>();

        if (query.hasFilters()) {
            for (FilterExpression filter : query.getFilters().values()) {
                if (filter.getOperator() == FilterOperator.CONTAINS) {
                    operands.add(filter.getOperand());
                }
            }
        }

        return operands;
    }

    /**
     * Build the command that will be used to retrieve the records for the specified command and query.
     * 
     * @param cmd The core retrieval statement
     * @param query The query to use to wrap the core statement
     * 
     * @return A sql command
     */
    protected String formulateRetrieveCommand(SqlCommand cmd, DataSetQuery query) {
        StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);

        if (rowLimitersSpecified(query)) { // we're limiting number of rows
            return formulateRowLimitedQuery(cmd, query);
        /*
        } else if (query.getStar()) { // all fields (*)
            sql.append("select ").append(INNER_QUERY_ALIAS).append(".* from ");
            appendCoreQuery(cmd, query, sql);
        */
        } else { // specifically selected fields
            sql.append("select ");
            addSelectedFields(query, sql);
            sql.append(" from ");
            appendCoreQuery(cmd, query, sql);
        }

        return sql.toString();
    }

    /**
     * Utility method for determing whether an object is either null, or is a zero-length string value.
     * 
     * @param obj The object to check
     * @return An indicator
     */
    protected boolean isEmpty(Object obj) {
        return obj == null || StringUtils.isEmpty(obj.toString());
    }

    /**
     * Get the currently set wildcard character for LIKE statement.
     * 
     * @return The wildcard character
     */
    protected char getWildCardCharacter() {
        return DEFAULT_WILDCARD_CHAR;
    }

    /**
     * Method used to modify the expression used to represent a parameter.  The value passed in is in the form :{field_name}.
     * Under certain circumstances, it may be desirable to enhance the expression, such as with a storage-specific function, 
     * for example: my_func(:{field_name})
     * 
     * @param paramStr
     * @return
     */
    protected String treatParamExpr(String paramStr) {
    	return paramStr;
    }
    
    protected String treatSelectedField(String fldExpr) {
    	return fldExpr;
    }
    
    /*
     * Public inner support classes follow
     */

    /**
     * Class used to process rows accessed thru a DataSet.
     * 
     */
    public static class QueryRowHandler implements RowCallbackHandler {

        private IDataSetResultHandler handler;
        private List<String> fields;
        private IDataSet dataSet;

        /**
         * Construct row handler.
         * 
         * @param dataSet The dataset to use
         * @param query The query to use
         * @param handler The handler to use
         */
        public QueryRowHandler(IDataSet dataSet, DataSetQuery query, IDataSetResultHandler handler) {
            this.dataSet = dataSet;
            this.handler = handler;
            this.fields = new ArrayList<String>();

            if (query.getFields().size() > 0) { // subset of fields based on query
                for (String f : query.getFields()) {
                    this.fields.add(f);
                }
            } else {
                // all fields for the dataset
                for (String name : dataSet.getOrderedPropertyNames()) {
                    this.fields.add(name);
                }
            }
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            DataSetItem item = new DataSetItem(dataSet);

            for (String f : fields) {
                item.put(f, rs.getObject(f));
            }

            handler.processItem(item);
        }
    }

    /**
     * Class for processing the first result only, from items retrieved from a DataSet.
     * 
     */
    public static class SingleRowHandler implements RowCallbackHandler {

        private IDataSet dataSet;
        private DataSetItem item;
        private boolean gotFirstRow = false;
        private List<String> fields;

        /**
         * Handler constructor.
         * 
         * @param dataSet The dataset to process
         * @param fields The fields to process
         */
        public SingleRowHandler(IDataSet dataSet, List<String> fields) {
            this.dataSet = dataSet;
            this.item = new DataSetItem(this.dataSet);
            this.fields = fields;
        }

        public DataSetItem getItem() {
            return item;
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            if (gotFirstRow) {
                return;
            }

            gotFirstRow = true;

            for (String f : fields) {
                try {
                    item.put(f, rs.getObject(f));
                } catch (Exception ex) {
                    throw new RuntimeException("Error getting value for field: " + f);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.internal.AbstractDataSet#initMetaData()
     */
    @Override
    public IDataSetMetaData initMetaData() {
    	List<DataSetProperty> readableProperties = new ArrayList<DataSetProperty>(INIT_FLD_COUNT);
    	List<DataSetProperty> writableProperties = new ArrayList<DataSetProperty>(INIT_FLD_COUNT);
    	Map<String, DataSetProperty> propertySet = new LinkedHashMap<String, DataSetProperty>(INIT_FLD_COUNT);
    	
    	// If the caller has provided an explicit retrieval command, then use this command
    	// to represent the entire set of readable properties.
    	if (retrieveCmd != null) { 
     		dbclient.query(retrieveCmd.getSql(), 
     			getDefaultCommandParameters(retrieveCmd), 
     				new MetaDataPropertyLoader(readableProperties));
     		
     		for (DataSetProperty prop : readableProperties) {
     			// Generally, properties are writable, sortable, and filterable. But
     			// for SQL datasets, assume these values are false unless overridden further below
     			prop.setWritable(false);
     			prop.setFilterable(false);
     			prop.setSortable(false);
     			propertySet.put(prop.getName(), prop);    			
     		}
    	}
    	
    	// Record whether or not we obtained any readable properties
    	boolean haveReadableSet = readableProperties.size() > 0;

    	// Factor in write-ability of properties. All fields in the root table (entity) are
    	// considered writable. Properties not in the root table, i.e. from joined tables,
    	// are not considered writable.
    	if (!StringUtils.isEmpty(getEntity())) {
    		// We'll use the default retrieval command, which 
    		// is simply a "select * from table" command. We'll
    		// tack on criteria that will guarantee no hits because
    		// we're only interested in metadata.
			dbclient.query(getDefaultRetrieveCommand().getSql() + " where 1<>1", 
				new MetaDataPropertyLoader(writableProperties));
			
			// For all writable properties...
			for (DataSetProperty prop : writableProperties) {
				// If the caller has explicitly limited the set of available properties
				// with an explicit retrieve command...
				if (haveReadableSet) {
					DataSetProperty readableProp = propertySet.get(prop.getName());
					// If the property exists, this property should be considered writable
					if (readableProp != null) {
						prop = readableProp;
					} // otherwise, the caller has explicitly ecluded this property
				} else { // Writable and readable properties are the same
					propertySet.put(prop.getName(), prop);
				}
				
				// Record the fact that the property is writable
				prop.setWritable(true);
			}
    	}
    	
    	// Set other flags based on certain conventions and rules, and add all
    	// properties to the metadat object
    	DataSetMetaData metaData = new DataSetMetaData();   	
    	for (DataSetProperty prop : propertySet.values()) {
    		
    		// ID/ID Ref flags
    		if (idNamePattern.matcher(prop.getName()).matches()) {
    			prop.setId(true);
    		} else if (idRefNamePattern.matcher(prop.getName()).matches()) {
    			prop.setIdRef(true);    			
    		}
    		
    		// A property will be considered sortable and filterable if it is a simple type
    		long nature = getTypeManager().getTypeNature(prop.getType());
    		long simpleNature = StdTypeManagerPlugin.SIMPLE_NATURE;
    		long res = nature & simpleNature;
    		
    		if (res > 0) {
    			prop.setFilterable(true);
    			prop.setSortable(true);
    		}
    		
    		// Add the property to the metadata object
    		metaData.addProperty(prop);
    	}
    	        
    	return metaData;
    }
    
    /**
     * Class for loading a result set's metadata into a DataSetProperty collection.
     * 
     */
    public static class MetaDataPropertyLoader implements ResultSetExtractor<Object> {

        private List<DataSetProperty> properties;

        /**
         * Construct loader.
         * 
         * @param dataSet The dataset to use
         * @param props The property collection to load into
         */
        public MetaDataPropertyLoader(List<DataSetProperty> props) {
             this.properties = props;
        }

        @Override
        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

            try {
                ResultSetMetaData metaData = rs.getMetaData();
                int ct = metaData.getColumnCount();

                for (int i = 1; i <= ct; i++) {
                	String propName = PropertyNameUtils.normalizeName(metaData.getColumnLabel(i));
                	
                    DataSetProperty prop = new DataSetProperty(propName, metaData.getColumnClassName(i));
                    
                    prop.setVariant(metaData.getColumnTypeName(i));
                    prop.setSize(metaData.getColumnDisplaySize(i));

                    properties.add(prop);
                }
            } catch (Exception ex) {
                throw new RuntimeException("Error fetching metadata", ex);
            }

            return null;
        }
    }
}
