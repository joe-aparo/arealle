package net.jsa.crib.ds.impl.sql;


import net.jsa.crib.ds.api.DataSetQuery;
import net.jsa.crib.ds.api.FilterExpression;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetItem;
import net.jsa.crib.ds.api.IDataSetResultHandler;
import net.jsa.crib.ds.api.DataSetQuery.SortDirection;
import net.jsa.crib.ds.api.FilterExpression.FilterOperator;
import net.jsa.crib.ds.impl.AbstractDataSet;
import net.jsa.crib.ds.impl.DataSetItem;
import net.jsa.crib.ds.impl.DataSetProperty;
import net.jsa.crib.ds.impl.IdListDataSetResultHandler;
import net.jsa.crib.ds.impl.ListDataSetResultHandler;

import org.apache.commons.lang3.StringUtils;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Base SQL implementation of the DataSet interface.
 * 
 */
public abstract class SqlDataSet extends AbstractDataSet {

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

    private String qualifier;
    private String table;
    private String sequence;
    private NamedParameterJdbcTemplate dbclient;
    private SqlCommand retrieveCmd;
    private SqlCommand createCmd;
    private SqlCommand updateCmd;
    private SqlCommand deleteCmd;
    private SqlCommand newKeyCmd;

    /*
     * Spring injection / initialization methods follow
     */

    /**
     * Set the underlying database table for the dataSet. Specifying a table allows much of the default CRUD
     * behavior of a SQLDataSet to be realized. Without it, the developer must explicitly define all CRUD
     * behavior. A dataset may contain a mix of having a table specified to obtain default behavior, combined
     * with overrides for specific CRUD operations.
     * 
     * @param table The name of the table
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * Get the name of the underlying database table.
     * 
     * @return The table name.
     */
    public String getTable() {
        return table;
    }

    /**
     * If specified, the qualifier will be prepended to database table names when auto-generating dynamic CRUD
     * sql.
     * 
     * @param qualifier The qualifier string.
     */
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * Return the table name qualifier.
     * 
     * @return The table name qualifier
     */
    public String getQualifier() {
        return qualifier;
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
     * Establish the client database connection object. This implementation relies specifically on Spring's
     * NamedParameterJdbcTemplate to invoke JDBC sql commands.
     * 
     * @param client The database connection client
     */
    public void setDbClient(NamedParameterJdbcTemplate client) {
        this.dbclient = client;
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

    /*
     * Public DataSet interface methods follow
     */

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

    /**
     * Retrieve an item from the dataset.
     * 
     * @param keyMap The key to use for retrieval.
     * @return The retrieved item
     */
    public IDataSetItem retrieve(Map<String, Object> keyMap) {
        SqlCommand cmd = retrieveCmd != null ? retrieveCmd : getDefaultRetrieveCommand();

        if (cmd == null) {
            return null;
        }

        // Filter the dataset with a query initialized with the given keys
        DataSetQuery query = new DataSetQuery();

        // first store parameters in query and remove them from input map
        for (String p : cmd.getParams()) {
            if (keyMap.containsKey(p)) {
                query.putParam(p, keyMap.get(p));
                keyMap.remove(p);
            }
        }

        // store remaining entries in map as filters
        for (Map.Entry<String, Object> keyItem : keyMap.entrySet()) {
            query.putFilter(keyItem.getKey(), FilterOperator.EQUAL, keyItem.getValue());
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
    public DataSetItem create(Map<String, Object> values) {
        SqlCommand cmd = createCmd != null ? createCmd : getDefaultCreateCommand(values);

        if (cmd == null) {
            return null;
        }

        // convert values to map types specified by dataset
        Map<String, Object> convertedValues = convertToNativeValues(values);

        // attempt to seed the values with a set of newly
        // generated keys
        Set<String> keyIds = getKeys();
        if (keyIds != null) {
            Map<String, Object> newKeys = getNewKeyValues(convertedValues);
            if (newKeys != null) {
                for (Entry<String, Object> e : newKeys.entrySet()) {
                    if (convertedValues.get(e.getKey()) == null) {
                        convertedValues.put(e.getKey(), e.getValue());
                    }
                }
            }
        }

        // invoke the insert
        dbclient.update(cmd.getSql(), convertedValues);

        return new DataSetItem(this, convertedValues);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.copyright.ds.DataSet#update(java.util.Map)
     */
    @Override
    public DataSetItem update(Map<String, Object> values) {

        // convert values to map types specified by dataset
        Map<String, Object> convertedValues = convertToNativeValues(values);

        SqlCommand cmd = updateCmd != null ? updateCmd : getDefaultUpdateCommand(convertedValues, null);

        if (cmd != null) {
            dbclient.update(cmd.getSql(), convertedValues);
        }

        return new DataSetItem(this, values);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.copyright.ds.DataSet#update(java.util.Map)
     */
    @Override
    public void update(DataSetQuery query, Map<String, Object> values) {
        Set<String> keyIds = getKeys();

        // this call works for single id datasets only
        if (keyIds.size() != 1) {
            return;
        }

        String idProp = keyIds.iterator().next();

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

        // we're only interested in the key field
        idQuery.setFields(keyIds);

        // fetch the id list
        IdListDataSetResultHandler handler = new IdListDataSetResultHandler(this, query, idProp);
        retrieve(idQuery, handler);

        // convert values to map types specified by dataset
        Map<String, Object> convertedValues = convertToNativeValues(values);

        // set up an update command based on a single filter for ids
        idQuery.clearFilters();
        idQuery.putFilter(idProp, FilterOperator.ONE_OF, handler.getIds());
        SqlCommand cmd = updateCmd != null ? updateCmd : getDefaultUpdateCommand(convertedValues,
                idQuery.getFilters());

        // update items matching the retrieved set of ids, breaking the id list into chunks
        // if its size exceeds the specified maximum
        if (cmd != null) {
            if (handler.getIds().size() <= MAX_IN_CLAUSE_ITEMS) {
                // add id list to original values
                convertedValues.put(idProp, convertToNativeValuesList(idProp, handler.getIds()));
                dbclient.update(cmd.getSql(), convertedValues);
            } else {
                int idx = 0; // current index
                int ct = handler.getIds().size(); // size of id list
                List<Object> ids = new ArrayList<Object>(MAX_IN_CLAUSE_ITEMS); // id buffer
                convertedValues.put(idProp, ids); // add id buffer to original values

                // break id list into separate updates
                while (idx < ct) {
                    ids.add(handler.getIds().get(idx++));

                    // invoke update on max size boundary, or when at and of id list
                    if (idx >= MAX_IN_CLAUSE_ITEMS && (idx % MAX_IN_CLAUSE_ITEMS == 0 || idx == ct)) {
                        dbclient.update(cmd.getSql(), convertedValues);
                        ids.clear(); // reset buffer
                    }
                }
            }
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

        if (table != null) {
            StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);

            sql.append("select * from ");
            if (qualifier != null) {
                sql.append(qualifier).append('.');
            }
            sql.append(table);

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

        if (table != null && fields.size() > 0) {
            // statement preamble
            sql.append("insert into ");
            if (qualifier != null) {
                sql.append(qualifier).append('.');
            }
            sql.append(table).append('(');

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
            Set<String> keyIds = getKeys();
            if (keyIds != null && keyIds.size() > 0) {
                for (String k : keyIds) {
                    if (!fields.contains(k)) {
                        if (ct++ > 0) {
                            sql.append(',');
                        }
                        sql.append(k);
                    }
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
            if (keyIds != null && keyIds.size() > 0) {
                for (String k : keyIds) {
                    if (!fields.contains(k)) {
                        if (ct++ > 0) {
                            sql.append(',');
                        }
                        sql.append(':').append(k);
                    }
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
        if (table == null) {
            return null;
        }

        SqlCommand cmd = null;
        Set<String> fields = values.keySet();
        Set<String> keyIds = getKeys();

        if (fields.size() > 0 && keyIds.size() > 0) {
            StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);

            // preamble
            sql.append("update ");
            if (qualifier != null) {
                sql.append(qualifier).append('.');
            }
            sql.append(table).append(" set ");

            // set up the column=:value portion of the statement
            int ct = 0;
            for (String fld : fields) {
                if (!keyIds.contains(fld) && isWritableProperty(fld)) { // exclude keys
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
            } else if (keyIds.size() > 0) { // Otherwise attempt to use keys
                sql.append(" where ");
                int count = 0;
                for (String k : keyIds) {
                    if (count++ > 0) {
                        sql.append(" AND ");
                    }
                    sql.append(k).append("=:").append(k);
                }
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

        Set<String> keyIds = getKeys();
        if (table != null && keyIds != null) {
            // preamble
            StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);
            sql.append("delete from ");
            if (qualifier != null) {
                sql.append(qualifier).append('.');
            }

            // where clause
            sql.append(table).append(" where ");
            int count = 0;
            for (String k : keyIds) { // account for multiple keys
                if (count++ > 0) {
                    sql.append(" AND ");
                }
                sql.append(k).append("=:").append(k);
            }

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
            for (String propname : getReadablePropertyNames()) {
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
    protected void appendFilterWhereClause(Map<String, FilterExpression> filters, StringBuilder sql,
            boolean isInner) {
        int ct = 0;
        for (FilterExpression filter : filters.values()) {
            // If we can't read a property, we can't filter on it
            if (!isReadableProperty(filter.getOperand())) {
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
                boolean stringField = false;
                DataSetProperty prop = getProperty(filter.getOperand());
                if (prop != null && "java.lang.String".equals(prop.getClassName())) {
                    stringField = true;
                }

                // determine if upper case treament should be ued
                boolean useCaseInsensitiveSearch = !getCaseSensitiveSearch()
                        && !getKeys().contains(filter.getOperand());

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
                DataSetProperty prop = getProperty(sort.getKey());
                boolean stringField = false;
                if (prop != null && "java.lang.String".equals(prop.getClassName())) {
                    stringField = true;
                }

                // determine if upper case treament should be ued
                boolean useCaseInsensitiveSearch = !getCaseSensitiveSearch()
                        && !getKeys().contains(sort.getKey());

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
    protected Map<String, Object> getNewKeyValues(Map<String, Object> params) {
        Map<String, Object> keyValues = new HashMap<String, Object>();

        Set<String> keyIds = getKeys();
        if (keyIds != null && keyIds.size() > 0) {
            SqlCommand keyCmd = newKeyCmd != null ? newKeyCmd : getDefaultNewKeyCommand();
            if (keyCmd != null) {
                SingleRowHandler handler = new SingleRowHandler(this, keyIds);
                dbclient.query(keyCmd.getSql(), params, handler);

                DataSetItem item = handler.getItem();
                if (item != null) {
                    for (String k : keyIds) {
                        keyValues.put(k, item.get(k));
                    }
                }
            } else if (getKeyGenerator() != null) {
                for (String k : keyIds) {
                    keyValues.put(k, getKeyGenerator().generateKeyValue(getName(), k));
                }
            }
        }

        return keyValues;
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

        // Fetch query parameters as native values
        Map<String, Object> qparams = convertToNativeValues(query.getParameters());

        char wc = getWildCardCharacter();

        // Place query parameters into output collection
        for (Map.Entry<String, Object> qparam : qparams.entrySet()) {
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
                
                if (!getCaseSensitiveSearch()) {
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
     * Load the writable properties based on the table currently associated with the dataset.
     */
    @Override
    protected void establishWritableProperties() {
        // Writable properties are all those in the table, if specified
        if (table != null) {
            SqlCommand cmd = getDefaultRetrieveCommand();
            String sql = cmd.getSql() + " where 1<>1"; // special case - not interested in data
            List<DataSetProperty> props = new ArrayList<DataSetProperty>();
            MetaDataPropertyLoader loader = new MetaDataPropertyLoader(this, props);
            dbclient.query(sql, getDefaultCommandParameters(cmd), loader);
            for (DataSetProperty p : props) {
                addProperty(p);
                addWritableProperty(p.getName());
            }
        }
    }

    /**
     * Initialize the readable properties for the DataSet. Readable properties are those corresponding to the
     * retrieve command if specified. Otherwise they are considered to be the same as the currently set
     * writable properties.
     */
    @Override
    protected void establishReadableProperties() {
        if (retrieveCmd != null) {
            // set up metadata row handler
            List<DataSetProperty> props = new ArrayList<DataSetProperty>();
            MetaDataPropertyLoader loader = new MetaDataPropertyLoader(this, props);

            // fetch metadata for command
            dbclient.query(retrieveCmd.getSql(), getDefaultCommandParameters(retrieveCmd), loader);

            // record properties
            for (DataSetProperty p : props) {
                if (!getPropertiesByName().containsKey(p.getName())) {
                    addProperty(p);
                }

                addReadableProperty(p.getName());
            }
        } else {
            // Assume same as writable properties and that the call
            // to establishWritableProperties preceded this call.
            for (String propName : getPropertiesByName().keySet()) {
                addReadableProperty(propName);
            }
        }
    }

    /**
     * Initialize the filterable properties for the DataSet. Unless specifically configured, filterable
     * properties are assumed to be the same as the currently set readable properties.
     */
    @Override
    protected void establishFilterableProperties() {
        if (getFilterProperties() != null) { // configured
            String[] names = getFilterProperties().split(",");
            for (String propname : names) {
                addFilterableProperty(propname.trim());
            }
        } else {
            // Assume same as writable properties and that the call
            // to establishWritableProperties preceded this call.
            for (String propName : getPropertiesByName().keySet()) {
                if (isReadableProperty(propName)) {
                    addFilterableProperty(propName);
                }
            }
        }
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
                for (String name : this.dataSet.getReadablePropertyNames()) {
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

            handler.processRow(item);
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
        private Set<String> fields;

        /**
         * Handler constructor.
         * 
         * @param dataSet The dataset to process
         * @param fields The fields to process
         */
        public SingleRowHandler(IDataSet dataSet, Set<String> fields) {
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

    /**
     * Class for loading a result set's metadata into a DataSetProperty collection.
     * 
     */
    public static class MetaDataPropertyLoader implements ResultSetExtractor<Object> {

        private IDataSet dataSet;
        private List<DataSetProperty> properties;

        /**
         * Construct loader.
         * 
         * @param dataSet The dataset to use
         * @param props The property collection to load into
         */
        public MetaDataPropertyLoader(IDataSet dataSet, List<DataSetProperty> props) {
            this.dataSet = dataSet;
            this.properties = props;
        }

        @Override
        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

            try {
                ResultSetMetaData metaData = rs.getMetaData();
                int ct = metaData.getColumnCount();

                for (int i = 1; i <= ct; i++) {
                    DataSetProperty prop = new DataSetProperty(dataSet);
                    prop.setName(metaData.getColumnLabel(i).toUpperCase()); // upper consistency
                    prop.setSize(metaData.getColumnDisplaySize(i));
                    prop.setScale(metaData.getPrecision(i));
                    prop.setClassName(metaData.getColumnClassName(i));
                    prop.setVariant(metaData.getColumnTypeName(i));

                    properties.add(prop);
                }
            } catch (Exception ex) {
                throw new RuntimeException("Error fetching metadata", ex);
            }

            return null;
        }
    }
}
