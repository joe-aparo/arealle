package ind.jsa.crib.ds.internal.sql;

import java.util.HashSet;
import java.util.Set;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSetProperty;


public class MySqlDataSet extends AbstractSqlDataSet {

	private static Set<String> GEOMETRY_TYPES = new HashSet<String>(10);
	
	static {
		GEOMETRY_TYPES.add("CURVE");
		GEOMETRY_TYPES.add("GEOMETRY");
		GEOMETRY_TYPES.add("GEOMETRYCOLLECTION");
		GEOMETRY_TYPES.add("LINE");
		GEOMETRY_TYPES.add("LINEARRING");
		GEOMETRY_TYPES.add("LINESTRING");
		GEOMETRY_TYPES.add("MULTICURVE");
		GEOMETRY_TYPES.add("MULTILINESTRING");
		GEOMETRY_TYPES.add("MULTIPOINT");
		GEOMETRY_TYPES.add("MULTIPOLYGON");
		GEOMETRY_TYPES.add("POINT");
		GEOMETRY_TYPES.add("POLYGON");
		GEOMETRY_TYPES.add("SURFACE");
	}
	
	public MySqlDataSet(String entity, String domain, NamedParameterJdbcTemplate dbclient) {
		super(entity, domain, dbclient);
	}
	
    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.sql.SqlDataSet#getDefaultNewKeyCommand()
     */
    @Override
    public SqlCommand getDefaultNewKeyCommand() {
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
        
        // select parcel_uid from splot.parcel limit 0,4 

        sql.append("select ");
        addDisplayableFields(query, sql);
        sql.append(" from ");

        appendCoreQuery(cmd, query, sql);

        sql.append(" limit ").append(query.getStartRow() - 1).append(",").append(query.getMaxRows());

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

        sql.append("select count(*) as rowcount from (");
        sql.append("select 1 from ");
        addInnerQueryClause(cmd, query, sql);
        sql.append(") a");

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

    @Override
    protected String treatParamExpr(String param) {
    	// param is assumed to start with a ':' character - standard for parameters
    	IDataSetProperty prop = getMetaData().getProperty(param.substring(1)); // skip ':'
    	
    	// Check for geometry type
    	if (GEOMETRY_TYPES.contains(prop.getVariant())) {
    		return "GeomFromWKB(" + param + ")";
    	} else {
    		return super.treatParamExpr(param);
    	}
    }
    
    protected String treatSelectedField(String fldExpr) {
    	// param is assumed to be of the form [alias.]fieldname - standard for field selection
    	int pos = fldExpr.indexOf('.');
    	
    	// Extract field name
    	String fldName = pos != -1 ? fldExpr.substring(pos+1) : fldExpr;

    	// Look up property
    	IDataSetProperty prop = getMetaData().getProperty(fldName);
    	
    	// Check for geometry type
    	if (GEOMETRY_TYPES.contains(prop.getVariant())) {
    		return "AsWKB(" + fldExpr + ") as " + fldName;
    	} else {
    		return super.treatSelectedField(fldExpr);
    	}
    }
}
