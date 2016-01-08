package ind.jsa.crib.ds.internal.sql;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ind.jsa.crib.ds.api.DataSetQuery;

/**
 * Postgres DataSet implementation.
 */
public class PostgresDataSet extends AbstractSqlDataSet {

	public PostgresDataSet(String entity, String domain, NamedParameterJdbcTemplate dbclient) {
		super(entity, domain, dbclient);
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
        String idPropName = getIdPropertyName();
        if (getSequence() != null && !StringUtils.isEmpty(idPropName)) {
            // select pubportal.template_seq.nextval as id from dual
            StringBuilder sql = new StringBuilder(INIT_SQL_SIZE);
            sql.append("select nextval('");
            if (getDomain() != null) {
                sql.append(getDomain()).append('.');
            }
            sql.append(getSequence()).append("') as ").append(idPropName);

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
