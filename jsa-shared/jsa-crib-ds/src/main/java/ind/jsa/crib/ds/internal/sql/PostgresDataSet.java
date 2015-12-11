package ind.jsa.crib.ds.internal.sql;

import java.util.List;

import ind.jsa.crib.ds.api.DataSetQuery;

/**
 * Postgres DataSet implementation.
 */
public class PostgresDataSet extends AbstractSqlDataSet {

	public PostgresDataSet(String entity, String domain) {
		super(entity, domain);
	}
	
    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.sql.SqlDataSet#getDefaultNewKeyCommand()
     */
    @Override
    public SqlCommand getDefaultNewKeyCommand() {
        SqlCommand cmd = null;

        // attempt to create an oracle sequence command by default if no user-specified
        // command is given and at least one key and a sequence name have been specified
        List<String> keyIds = getIdPropertyNames();
        if (getSequence() != null && keyIds != null && keyIds.size() > 0) {
            String defaultKeyName = keyIds.iterator().next(); // grab first by default

            // select pubportal.template_seq.nextval as id from dual
            StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);
            sql.append("select nextval('");
            if (getDomain() != null) {
                sql.append(getDomain()).append('.');
            }
            sql.append(getSequence()).append("') as ").append(defaultKeyName);

            cmd = new SqlCommand(sql.toString());
        }

        return cmd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jsa.crib.ds.impl.sql.SqlDataSet#formulateRowLimitedQuery(com.jsa.crib.ds.impl
     * .sql.SqlCommand, com.jsa.crib.ds.api.DataSetQuery)
     */
    @Override
    protected String formulateRowLimitedQuery(SqlCommand cmd, DataSetQuery query) {
        StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);

        sql.append("select ");
        addDisplayableFields(query, sql);
        sql.append(" from ");

        appendCoreQuery(cmd, query, sql);

        sql.append(" limit ").append(query.getMaxRows()).append(" offset ").append(query.getStartRow() - 1);

        return sql.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jsa.crib.ds.impl.sql.SqlDataSet#formulateRetrieveCountCommand(com.jsa.crib.
     * ds.impl.sql.SqlCommand, com.jsa.crib.ds.api.DataSetQuery)
     */
    @Override
    protected String formulateRetrieveCountCommand(SqlCommand cmd, DataSetQuery query) {
        StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);

        sql.append("select count(*) from (");
        sql.append("select 1 from ");
        addInnerQueryClause(cmd, query, sql);
        sql.append(") as rowcount");

        return sql.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.sql.SqlDataSet#formulateDateTruncFunctionPrefix()
     */
    @Override
    protected String formulateDateTruncFunctionPrefix() {
        return "date_trunc('day',";
    }
}
