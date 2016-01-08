package ind.jsa.crib.ds.internal.sql;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ind.jsa.crib.ds.api.DataSetQuery;

/**
 * Oracle DataSEt implementation.
 * 
 */
public final class OracleDataSet extends AbstractSqlDataSet {

	public OracleDataSet(String domain, String entity, NamedParameterJdbcTemplate dbclient) {
		super(domain, entity, dbclient);
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
            sql.append("select ");
            if (getDomain() != null) {
                sql.append(getDomain()).append('.');
            }
            sql.append(getSequence()).append(".nextval as " + idPropName + " from dual");

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

        sql.append("select * from (select rownum as row_num, w.* from (select ");
        addDisplayableFields(query, sql);
        sql.append(" from ");

        appendCoreQuery(cmd, query, sql);

        sql.append(") w )");

        int startRow = query.getStartRow();
        int endRow = startRow + query.getMaxRows() - 1;

        sql.append(" where ").append("row_num between ").append(startRow).append(" and ").append(endRow);

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
        sql.append(")");

        return sql.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.sql.SqlDataSet#formulateDateTruncFunctionPrefix()
     */
    @Override
    protected String formulateDateTruncFunctionPrefix() {
        return "TRUNC(";
    }
}
