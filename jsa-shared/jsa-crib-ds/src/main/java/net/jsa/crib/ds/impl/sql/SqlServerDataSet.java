package net.jsa.crib.ds.impl.sql;

import net.jsa.crib.ds.api.DataSetQuery;

/**
 * SQL Server DataSet implementation.
 * 
 */
public class SqlServerDataSet extends SqlDataSet {

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.sql.SqlDataSet#getDefaultNewKeyCommand()
     */
    @Override
    protected SqlCommand getDefaultNewKeyCommand() {
        return null;
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

        sql.append("select * from (select Row_Number() over (order by (select 1)) "
                + "as row_num, w.* from (select top 99999999 ");

        addDisplayableFields(query, sql);
        sql.append(" from ");

        appendCoreQuery(cmd, query, sql);

        sql.append(") as w ) x");

        int startRow = query.getStartRow();
        int endRow = startRow + query.getMaxRows() - 1;

        sql.append(" where row_num >= ").append(startRow).append(" and row_num <= ").append(endRow);

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
        sql.append("select * from ");
        addInnerQueryClause(cmd, query, sql);
        sql.append(") as rowct");

        return sql.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.sql.SqlDataSet#formulateDateTruncFunctionPrefix()
     */
    @Override
    protected String formulateDateTruncFunctionPrefix() {
        return "dateadd(DD, datediff(DD,0,";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.sql.SqlDataSet#formulateDateTruncFunctionSuffix()
     */
    @Override
    protected String formulateDateTruncFunctionSuffix() {
        // TODO Auto-generated method stub
        return "), 0)";
    }
}
