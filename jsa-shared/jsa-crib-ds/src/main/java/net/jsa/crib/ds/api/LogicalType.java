package net.jsa.crib.ds.api;

/**
 * Logical type enumeration.  All DataSet values are maintained
 * internally as having one of these pre-defined logical types.
 */
public enum LogicalType {
    /**
     * Character string value (String)
     */
    STRING,
    /**
     * Whole numeric value (Long)
     */
    INTEGER,
    /**
     * Fractional numeric value (Decimal)
     */
    DECIMAL,
    /**
     * Date/Time value (Calendar)
     */
    DATETIME,
    /**
     * Boolean value (Boolean)
     */
    BOOLEAN

}
