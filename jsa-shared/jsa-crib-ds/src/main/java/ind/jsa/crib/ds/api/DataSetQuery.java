package ind.jsa.crib.ds.api;

import net.jsa.common.logging.LogUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import ind.jsa.crib.ds.internal.type.utils.std.ToIntUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Represents a logical query against a dataset. A query consists of "parameters", "filters", "sorts",
 * "fields", and a "logical page".
 * 
 * Parameters are values considered required by the underlying retrieval implementation to obtain the core
 * dataset, whereas filters are considered optional values used to further narrow the core dataset.
 * 
 * Fields are a subselection of properties desired from the core dataset. If none are specified, all fields
 * are assumed.
 * 
 * The logical page is indicated by a starting row and maximum number of rows to fetch from the given starting
 * point.
 * 
 * An entire query, or any of its underlying facets may be represented in condensed string form. All such
 * strings use a simple, consistent notation.
 * 
 * An entire query, or any of its facets, may be set by using such strings. A query can also produce such
 * strings. These features support such things as the storing of queries, or the inclusion of a query in a
 * hyperlink.
 * <p/>
 * Currently, for queries, the '~' charater is used for the expression assignment delimiter, while the '^'
 * charater is used as the expression delimiter
 * <p/>
 * The facet identifiers recognized for a Query are:
 * <p/>
 * sr - start row; single value string<br>
 * mxr - maximum rows; single value string<br>
 * fld - fields to select; multi-instance value string with simple string values, e.g.
 * ACCOUNT_NUMBER|YEAR|QUARTER<br>
 * prm - required parameter values; multi-instance value string with value parts, e.g. quarters:1|years:2000<br>
 * flt - optional filters; multi-instance value string with value parts, e.g. PRODUCT:=:|IDNO:=:<br>
 * srt - sort behavior; multi-instance value string with value parts, e.g.
 * ACCOUNT_NUMBER:asc|YEAR:asc|QUARTER:asc<br>
 * <p/>
 * 
 * An example of a full query string might be:
 * fld~ORG_NAME|DEFAULT_CURRENCY_CODE|CHANNEL^prm~STATUS:A^flt~CHANNEL:lk:Right^srt~ORG_NAME:a
 * 
 * Here we are asking for a result containing the name, currency, and channel for selected records. The query
 * supplies a status parameter known to be required by the data set. Items are to be filtered for records
 * where the channel contains the term "Right" and results are sorted by the org name, ascending.
 */
public class DataSetQuery {

    private Logger log = LogUtils.getLogger();

    /**
     * Used to qualify upper bound values in parameter maps.
     */
    public static final String UPPERBOUND_NAME_SUFFIX = "_UB";
    
    /**
     * Used to delimit the parts of a value in a query string.
     */
    public static final String VALUE_PART_DELIMITER = ":";
    
    /**
     * Used to delimit the values in a quiery string.
     */
    public static final String VALUE_INSTANCE_DELIMITER = "|";
    
    /**
     * The number of parts in a filter value.
     */
    public static final int FILTER_PART_COUNT = 3;

    /**
     * The number of parts in a parameter value.
     */
    public static final int PARAM_PART_COUNT = 2;
    
    private int startRow = 0;
    private int maxRows = 0;
    private Set<String> fields = new LinkedHashSet<String>();
    private Map<String, Object> params = new LinkedHashMap<String, Object>();
    private Map<String, FilterExpression> filters = new LinkedHashMap<String, FilterExpression>();
    private Map<String, SortDirection> sorts = new LinkedHashMap<String, SortDirection>();

    /**
     * No-arg constructor.
     */
    public DataSetQuery() {
    }

    /**
     * Construct a new query by copying a provided query.
     * 
     * @param query The query to copy
     */
    public DataSetQuery(DataSetQuery query) {
        this.startRow = query.getStartRow();
        this.maxRows = query.getMaxRows();
        this.fields = new LinkedHashSet<String>(query.getFields());
        this.params = new LinkedHashMap<String, Object>(query.getParams());
        this.filters = new LinkedHashMap<String, FilterExpression>(query.getFilters());
        this.sorts = new LinkedHashMap<String, SortDirection>(query.getSorts());
    }

    /**
     * Get the starting row of the first item to retrieve from the dataset.
     * 
     * @return A starting row index
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * Set the 1-based starting index of the first item to retrieve from the dataset.
     * 
     * @param row A starting row
     */
    public void setStartRow(int row) {
        this.startRow = row;
    }

    /**
     * Get the maximum number of rows to retrieve from the starting point.
     * 
     * @return A max row count
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * Set the maximum number of rows to retrieve from the starting point.
     * 
     * @param maxRows A maximum row count
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    /**
     * Specify the name of a field to retrieve by the query.
     * 
     * @param fieldName A property field name
     */
    public void putField(String fieldName) {
        fields.add(fieldName);
    }

    /**
     * Set the fields to retrieve.
     * 
     * @param fields A collection of field names
     */
    public void setFields(Set<String> fields) {
        clearFields();
        for (String fld : fields) {
            fields.add(fld);
        }
    }

    /**
     * Set the fields to retrieve.
     * 
     * @param fieldStrs A collection of field names
     */
    public void setFields(List<String> fieldStrs) {
        clearFields();
        for (String fld : fieldStrs) {
            fields.add(fld);
        }
    }

    /**
     * Set the fields to retrieve.
     * 
     * @param fieldString A delimited string of field names
     */
    public void setFields(String fieldString) {
        setFields(parseObjectList(fieldString));
    }

    /**
     * Get the list of fields to retrieve.
     * 
     * @return A field list
     */
    public List<String> getFieldStrings() {
        List<String> strs = new ArrayList<String>(fields.size());
        for (String fld : fields) {
            strs.add(fld);
        }

        return strs;
    }

    /**
     * Get the list of fields to retrieve as a delimited string of names.
     * 
     * @return A delimited field list string
     */
    public String getFieldsString() {
        return StringUtils.join(getFields(), VALUE_INSTANCE_DELIMITER);
    }

    /**
     * Get the fields to retrieve.
     * 
     * @return A collection of field names
     */
    public Set<String> getFields() {
        return fields;
    }

    /**
     * Clear the fields to retrieve.
     */
    public void clearFields() {
        fields.clear();
    }

    /**
     * Add a named parameter value.
     * 
     * @param fieldName The name of the parameter
     * @param value The value of the parameter
     */
    public void putParam(String fieldName, Object value) {
        params.put(fieldName, value);
    }

    /**
     * Add a named parameter value.
     * 
     * @param paramStr A delimited string parameter value
     */
    public void putParam(String paramStr) {
        String[] parts = StringUtils.split(paramStr, VALUE_PART_DELIMITER);
        if (parts.length == PARAM_PART_COUNT) {
            putParam(parts[0], parts[1]);
        }
    }

    /**
     * Set the collection of named parameter values.
     * 
     * @param params A collection of named parameter values
     */
    public void setParams(Map<String, Object> params) {
        clearParams();
        for (Entry<String, Object> e : params.entrySet()) {
            putParam(e.getKey(), e.getValue());
        }
    }

    /**
     * Set the collection of named parameter values.
     * 
     * @param paramStrs A list of delimited string parameter values 
     */
    public void setParams(List<String> paramStrs) {
        clearParams();
        for (String paramStr : paramStrs) {
            putParam(paramStr);
        }
    }

    /**
     * Set the collection of named parameter values.
     * 
     * @param paramsStr A delimited string of parameters and values
     */
    public void setParams(String paramsStr) {
        setParams(parseObjectList(paramsStr));
    }

    /**
     * Get the collection of named selection criteria.
     * 
     * @return A collection of named parameter values
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * Get the currently set parameters and values as a collection of delimited strings.
     * 
     * @return A collection of delimited parameter string values
     */
    public List<String> getParamStrings() {
        List<String> paramStrs = new ArrayList<String>();

        for (String fld : params.keySet()) {
            paramStrs.add(fld + VALUE_PART_DELIMITER + params.get(fld));
        }

        return paramStrs;
    }

    /**
     * Get the currently set parameters as a delimited string.
     * 
     * @return A delimited string of parameter value strings
     */
    public String getParametersString() {
        return StringUtils.join(getParamStrings(), VALUE_INSTANCE_DELIMITER);
    }

    /**
     * Clear the selection criteria.
     */
    public void clearParams() {
        params.clear();
    }

    /**
     * Set a filter on a given field.
     * 
     * @param fieldName The name of the filter field
     * @param operator The operator to apply
     * @param value The value to apply
     */
    public void putFilter(String fieldName, FilterOperator operator, Object value) {
        putFilter(fieldName, operator, value, null);
    }

    /**
     * Set a filter on a given field.
     * 
     * @param flt A delimited filter value string
     */
    public void putFilter(String flt) {
        String[] parts = StringUtils.split(flt, VALUE_PART_DELIMITER);
        if (parts.length == FILTER_PART_COUNT) {
            FilterOperator op = FilterOperator.getByCode(parts[1]);
            if (op != null) {
                putFilter(parts[0], op, parts[2]);
            } else {
                log.warn("Invalid operator: " + parts[1]);
            }
        } else {
            log.warn("Invalid filter: " + flt);
        }
    }

    /**
     * Set a filter on a given field.
     * 
     * @param fieldName The name of the field to filter on
     * @param operator The operator to apply
     * @param value The value to apply 
     * @param upperValue The upperbound value to apply if filter operator is BETWEEN
     */
    public void putFilter(String fieldName, FilterOperator operator, Object value, Object upperValue) {
        filters.put(fieldName, new FilterExpression(fieldName, operator, value, upperValue));
    }

    /**
     * Set the specified collection of filters.
     * 
     * @param filters A collection of filters keyed by field name
     */
    public void setFilters(Map<String, FilterExpression> filters) {
        this.filters = filters;
    }

    /**
     * Set the current filters from a list of delimited filter strings.
     * 
     * @param filters A list of delimited filter strings
     */
    public void setFilters(List<String> filters) {
        clearFilters();
        for (String flt : filters) {
            putFilter(flt);
        }
    }

    /**
     * Set the current filters from a delimited string of delimited filter strings.
     * 
     * @param filtersString A delimited string of delimited filter strings
     */
    public void setFilters(String filtersString) {
        setFilters(parseObjectList(filtersString));
    }

    /**
     * Get the specified collection of filters.
     * 
     * @return A collection of filters mapped by field name
     */
    public Map<String, FilterExpression> getFilters() {
        return filters;
    }

    /**
     * Get the current filters as a collection of delimited filter strings.
     * 
     * @return A collection of delimited filter strings.
     */
    public List<String> getFilterStrings() {
        List<String> flts = new ArrayList<String>();
        for (FilterExpression expr : filters.values()) {
            flts.add(expr.getOperand() + VALUE_PART_DELIMITER + expr.getOperator().getCode()
                    + VALUE_PART_DELIMITER + expr.getValue());
        }

        return flts;
    }

    /**
     * Get the currently set filters as a delimited string of delimited filter strings.
     * 
     * @return A delimited string of delimited filter strings.
     */
    public String getFiltersString() {
        return StringUtils.join(getFilterStrings(), VALUE_INSTANCE_DELIMITER);
    }

    /**
     * Clear all filters.
     */
    public void clearFilters() {
        filters.clear();
    }

    /**
     * Clear a specific filter.
     * 
     * @param name A field name
     */
    public void clearFilter(String name) {
        filters.remove(name);
    }

    /**
     * Determine whether any filters are set on the query.
     * 
     * @return An indicator
     */
    public boolean hasFilters() {
        return filters.size() > 0;
    }

    /**
     * Specify a sort for a given field, and a logical direction.
     * 
     * @param fieldName A field name
     * @param dir A sort direction
     */
    public void putSort(String fieldName, SortDirection dir) {
        sorts.put(fieldName, dir);
    }

    /**
     * Specify a sort for a given field, and a logical direction, as a delimited sort string.
     * 
     * @param srt A delimited sort string
     */
    public void putSort(String srt) {
        String[] parts = StringUtils.split(srt, VALUE_PART_DELIMITER);
        if (parts.length == 2) {
            SortDirection dir = SortDirection.getByCode(parts[1]);
            if (dir != null) {
                putSort(parts[0], dir);
            } else {
                log.warn("Invalid direction:" + parts[1]);
            }
        } else {
            log.warn("Invalid sort: " + srt);
        }
    }

    /**
     * Specify a collection of fields to sort on as a collection sort directions mapped by field name.
     * 
     * @param sorts A collection sort directions mapped by field name
     */
    public void setSorts(Map<String, SortDirection> sorts) {
        clearSorts();
        for (Entry<String, SortDirection> e : sorts.entrySet()) {
            sorts.put(e.getKey(), e.getValue());
        }
    }

    /**
     * Specify the fields to sort on as a collection of delimited sort strings.
     * 
     * @param sorts A collection of delimited sort strings
     */
    public void setSorts(List<String> sorts) {
        clearSorts();
        for (String srt : sorts) {
            putSort(srt);
        }
    }

    /**
     * Specify the fields to sort on as a delimited string of delimited sort strings.
     * 
     * @param sortsString A delimited string of delimited sort strings
     */
    public void setSorts(String sortsString) {
        setSorts(parseObjectList(sortsString));
    }

    /**
     * Get the collection of properties to sort on.
     * 
     * @return A collection of sort directions keyed by field name
     */
    public Map<String, SortDirection> getSorts() {
        return sorts;
    }

    /**
     * Get the fields to sort on as a collection of delimited sort strings.
     * 
     * @return A collection of delimited sort strings
     */
    public List<String> getSortStrings() {
        List<String> sortsStrs = new ArrayList<String>();
        for (String fld : sorts.keySet()) {
            sortsStrs.add(fld + VALUE_PART_DELIMITER + sorts.get(fld).getCode());
        }

        return sortsStrs;
    }

    /**
     * Get the fields to sort on as a delimited string of delimited sort strings.
     * 
     * @return A delimited string of delimited sort strings
     */
    public String getSortsString() {
        return StringUtils.join(getSortStrings(), VALUE_INSTANCE_DELIMITER);
    }

    /**
     * Clear all sorts.
     */
    public void clearSorts() {
        sorts.clear();
    }

    /**
     * Convenience method indicating whether all fields are logically selected in the query.
     * 
     * @return An indicator
     */
    public boolean getStar() {
        return fields == null || fields.size() == 0;
    }

    /**
     * Get all specified criteria and optional filters as a single collection of name/value pairs.
     * 
     * @return A map of values keyed by field name
     */
    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<String, Object>();

        for (Entry<String, Object> e : params.entrySet()) {
            parameters.put(e.getKey(), e.getValue());
        }

        for (Entry<String, FilterExpression> e : filters.entrySet()) {
            FilterExpression exp = e.getValue();
            if (exp.getValue() != null) {
                parameters.put(e.getKey(), exp.getValue());
            }

            if (exp.getUpperBoundValue() != null) {
                parameters.put(e.getKey() + UPPERBOUND_NAME_SUFFIX, exp.getUpperBoundValue());
            }
        }

        return parameters;
    }

    /**
     * Set this query's content from a given query string.
     * 
     * @param qryStr A query string compatible with the format produced by toString().
     */
    public void fromString(String qryStr) {
        MapStringifier ms = getMapStringifier();
        ListStringifier ls = getListStringifier();

        ms.parse(qryStr);

        setStartRow(ToIntUtils.str2Int(ms.get("sr", "0")));
        setMaxRows(ToIntUtils.str2Int(ms.get("mxr", "0")));
        ls.parse(ms.get("fld"));
        setFields(ls.getItems());
        ls.parse(ms.get("prm"));
        setParams(ls.getItems());
        ls.parse(ms.get("flt"));
        setFilters(ls.getItems());
        ls.parse(ms.get("srt"));
        setSorts(ls.getItems());
    }

    @Override
    /**
     * Gets this query's content as a string.
     * 
     * @return A query string compatible with the format expected by fromString().
     */
    public String toString() {
        MapStringifier ms = getMapStringifier();
        ListStringifier ls = getListStringifier();

        ms.put("sr", Integer.toString(getStartRow()));
        ms.put("mxr", Integer.toString(getMaxRows()));
        ls.setItems(getFieldStrings());
        ms.put("fld", ls.toString());
        ls.setItems(getParamStrings());
        ms.put("prm", ls.toString());
        ls.setItems(getFilterStrings());
        ms.put("flt", ls.toString());
        ls.setItems(getSortStrings());
        ms.put("srt", ls.toString());

        return ms.toString();
    }

    /**
     * Get a string representing the filters and parameters used in the query.
     * 
     * @return A string representation of the query
     */
    public String getCriteriaString() {
        return this.getParametersString() + VALUE_INSTANCE_DELIMITER + this.getFiltersString();
    }

    private MapStringifier getMapStringifier() {
        MapStringifier ms = new MapStringifier();
        ms.setItemSeparator("^");
        ms.setAssignmentSeparator("~");

        return ms;
    }

    private ListStringifier getListStringifier() {
        ListStringifier ls = new ListStringifier();
        ls.setItemSeparator(VALUE_INSTANCE_DELIMITER);

        return ls;
    }

    private List<String> parseObjectList(String objStr) {
        ListStringifier ls = getListStringifier();
        ls.parse(objStr);
        return ls.getItems();
    }

    /**
     * Create a copy of this query object.
     * 
     * @return A new query object representing a copy of this query
     */
    public DataSetQuery copy() {
        DataSetQuery q = new DataSetQuery();

        q.setStartRow(startRow);
        q.setMaxRows(maxRows);
        
        for (String f : fields) {
            q.putField(f);
        }

        for (Entry<String, Object> e : params.entrySet()) {
            q.putParam(e.getKey(), e.getValue());
        }

        for (Entry<String, FilterExpression> e : filters.entrySet()) {
            FilterExpression exp = e.getValue();
            q.putFilter(exp.getOperand(), exp.getOperator(), exp.getValue(), exp.getUpperBoundValue());
        }

        for (Entry<String, SortDirection> e : sorts.entrySet()) {
            q.putSort(e.getKey(), e.getValue());
        }

        return q;
    }

    /**
     * A combination of a property identifier (operand), an operator, and a value that together
     * represent a logical filter expression.
     * 
     */
    public class FilterExpression {

        private String operand;
        private FilterOperator operator;
        private Object value;
        private Object upperBoundValue;

        /**
         * No-arg constructor.
         */
        public FilterExpression() {
        }

        /**
         * This constructor will initialize the expression with the given components and will also
         * adjust or fill in the values for consistency.
         * 
         * @param operand The field name for the expression
         * @param operator The operator to apply
         * @param value The value to apply
         * @param upperBoundValue The upper bound value to apply if operator is BETWEEN
         */
        public FilterExpression(String operand, FilterOperator operator, Object value, Object upperBoundValue) {
            FilterOperator op = (operator == null) ? FilterOperator.EQUAL : operator;
            Object val = value;
            Object ubVal = upperBoundValue;

            boolean isCollection = value != null && value instanceof Collection;

            // Force operator to be IN type operator if filter value is a collection
            // and the given operator isn't compatible with a collection
            if (isCollection && op != FilterOperator.ONE_OF && op != FilterOperator.NOT_ONE_OF) {
                op = FilterOperator.ONE_OF;
            }

            String strVal = value != null ? value.toString() : "";
            String strUpperBoundVal = upperBoundValue != null ? upperBoundValue.toString() : "";

            // Force operator to <= or >= if only one side of range specified between
            if (op == FilterOperator.BETWEEN) {
                if (StringUtils.isEmpty(strVal) && !StringUtils.isEmpty(strUpperBoundVal)) {
                    op = FilterOperator.LESS_OR_EQUAL;
                    val = upperBoundValue;
                    ubVal = null;
                } else if (!StringUtils.isEmpty(strVal) && StringUtils.isEmpty(strUpperBoundVal)) {
                    op = FilterOperator.GREATER_OR_EQUAL;
                }
            }

            // Force operator to <> if either side of range not specified when not between
            if (op == FilterOperator.NOT_BETWEEN) {
                if (StringUtils.isEmpty(strVal) || StringUtils.isEmpty(strUpperBoundVal)) {
                    op = FilterOperator.NOT_EQUAL;
                }

                if (StringUtils.isEmpty(strVal) && !StringUtils.isEmpty(strUpperBoundVal)) {
                    val = upperBoundValue;
                    ubVal = null;
                }
            }

            // Assign checked inputs
            this.operand = operand;
            this.operator = op;
            this.value = val;
            this.upperBoundValue = ubVal;
        }

        /**
         * Get the logical operand identifier.
         * 
         * @return An operand string
         */
        public String getOperand() {
            return operand;
        }

        /**
         * Set the logical operand identifier.
         * 
         * @param operand The operand string
         */
        public void setOperand(String operand) {
            this.operand = operand;
        }

        /**
         * Get the logical operator.
         * 
         * @return The operator
         */
        public FilterOperator getOperator() {
            return operator;
        }

        /**
         * Set the logical operator.
         * 
         * @param operator The operator
         */
        public void setOperator(FilterOperator operator) {
            this.operator = operator;
        }

        /**
         * Get the expression value.
         * 
         * @return The value
         */
        public Object getValue() {
            return value;
        }

        /**
         * Set the expression value.
         * 
         * @param value The value
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * Set the upper bound value for range type operator.
         * 
         * @param upperBoundValue The upper bound value
         */
        public void setUpperBoundValue(Object upperBoundValue) {
            this.upperBoundValue = upperBoundValue;
        }

        /**
         * Get the upper bound value for range type operator.
         * 
         * @return The upper bound value
         */
        public Object getUpperBoundValue() {
            return upperBoundValue;
        }

        /**
         * Get a String representation of this filter expression.
         * 
         * @return A string
         */
        @Override
        public String toString() {
            return operand + " " + operator + " " + value;
        }

        /**
         * Determine whether the value for the expression is null, and empty string, or an empty
         * collection.
         * 
         * @return An indicator
         */
        public boolean isEmptyValue() {
            boolean isEmpty = value == null;

            if (!isEmpty) {
                if (value instanceof Collection) {
                    isEmpty = ((Collection<?>) value).size() == 0;
                } else {
                    isEmpty = StringUtils.isEmpty(value.toString());
                }
            }

            return isEmpty;
        }
    }
    
    /**
     * Represents a logical operator for a filter.
     */
    public enum FilterOperator {
        /**
         * Equals.
         */
        EQUAL("equals", "eq", "="),
        /**
         * Greater than.
         */
        GREATER("is greater than", "gt", ">"),
        /**
         * Greater than or equal.
         */
        GREATER_OR_EQUAL("is greater than or equal to", "ge", ">="),
        /**
         * Less than
         */
        LESS("is less than", "lt", "<"),
        /**
         * Less than or equal to.
         */
        LESS_OR_EQUAL("is less than or equal to", "le", "<="),
        /**
         * Not equal to.
         */
        NOT_EQUAL("does not equal", "ne", "<>"),
        /**
         * Contains substring.
         */
        CONTAINS("contains", "lk", "like"),
        /**
         * Is one of a collection of values.
         */
        ONE_OF("is one of", "in", "in"),
        /**
         * Is not one of a collection of values.
         */
        NOT_ONE_OF("is not one of", "nin", "not in"),
        /**
         * Is null.
         */
        NULL("is null", "nl", "is null"),
        /**
         * Is not null.
         */
        NOT_NULL("is not null", "nnl", "is not null"),
        /**
         * Is between (inclusive).
         */
        BETWEEN("is between", "bet", "between"),
        /**
         * Is not between (inclusive).
         */
        NOT_BETWEEN("is not between", "nbet", "not between");

        private static final Map<String, FilterOperator> OPS_BY_CODE = new HashMap<String, FilterOperator>();
        private static final Set<FilterOperator> SCALAR_OPS = new LinkedHashSet<FilterOperator>();
        private static final Set<FilterOperator> STRING_OPS = new LinkedHashSet<FilterOperator>();

        static {
            OPS_BY_CODE.put(EQUAL.getCode(), EQUAL);
            OPS_BY_CODE.put(GREATER.getCode(), GREATER);
            OPS_BY_CODE.put(GREATER_OR_EQUAL.getCode(), GREATER_OR_EQUAL);
            OPS_BY_CODE.put(LESS.getCode(), LESS);
            OPS_BY_CODE.put(LESS_OR_EQUAL.getCode(), LESS_OR_EQUAL);
            OPS_BY_CODE.put(NOT_EQUAL.getCode(), NOT_EQUAL);
            OPS_BY_CODE.put(CONTAINS.getCode(), CONTAINS);
            OPS_BY_CODE.put(ONE_OF.getCode(), ONE_OF);
            OPS_BY_CODE.put(NOT_ONE_OF.getCode(), NOT_ONE_OF);
            OPS_BY_CODE.put(NULL.getCode(), NULL);
            OPS_BY_CODE.put(NOT_NULL.getCode(), NOT_NULL);
            OPS_BY_CODE.put(BETWEEN.getCode(), BETWEEN);
            OPS_BY_CODE.put(NOT_BETWEEN.getCode(), NOT_BETWEEN);

            SCALAR_OPS.add(EQUAL);
            SCALAR_OPS.add(GREATER);
            SCALAR_OPS.add(GREATER_OR_EQUAL);
            SCALAR_OPS.add(LESS);
            SCALAR_OPS.add(LESS_OR_EQUAL);
            SCALAR_OPS.add(NOT_EQUAL);
            SCALAR_OPS.add(NULL);
            SCALAR_OPS.add(NOT_NULL);
            SCALAR_OPS.add(BETWEEN);
            SCALAR_OPS.add(NOT_BETWEEN);

            STRING_OPS.add(EQUAL);
            STRING_OPS.add(NOT_EQUAL);
            STRING_OPS.add(CONTAINS);
            STRING_OPS.add(NULL);
            STRING_OPS.add(NOT_NULL);
        }

        private String label;
        private String code;
        private String sqlOp;

        /**
         * Constructor.
         * 
         * @param label Displayable label
         * @param code Logical code
         * @param sqlOp SQL equivalent
         */
        FilterOperator(String label, String code, String sqlOp) {
            this.label = label;
            this.code = code;
            this.sqlOp = sqlOp;
        }

        /**
         * Get displayable label.
         * 
         * @return A string
         */
        public String getLabel() {
            return label;
        }

        /**
         * Get code.
         * 
         * @return A string
         */
        public String getCode() {
            return code;
        }

        /**
         * Get SQL equivalent.
         * 
         * @return A string
         */
        public String getSqlOp() {
            return sqlOp;
        }

        /**
         * Convert code to enumerated type.
         * 
         * @param code A code string
         * @return An enumerated type
         */
        public static FilterOperator getByCode(String code) {
            return OPS_BY_CODE.get(code);
        }

        /**
         * Get scalar operators.
         * 
         * @return A collection of operators
         */
        public static Set<FilterOperator> getScalarOperators() {
            return SCALAR_OPS;
        }

        /**
         * Get string operators.
         * 
         * @return A collection of operators
         */
        public static Set<FilterOperator> getStringOperators() {
            return STRING_OPS;
        }

        /**
         * Implement toString as label.
         * 
         * @return A string
         */
        @Override
        public String toString() {
            return label;
        }
    }

    /**
     * Represents a logical sort direction for sorted items in a dataset
     * 
     */
    public enum SortDirection {
        /**
         * Ascending
         */
        ASC("a", "asc"),
        /**
         * Descending
         */
        DESC("d", "desc");

        private String code;
        private String dir;

        private static final Map<String, SortDirection> DIRS_BY_CODE = new HashMap<String, SortDirection>();

        static {
            DIRS_BY_CODE.put(ASC.getCode(), ASC);
            DIRS_BY_CODE.put(DESC.getCode(), DESC);
        }

        /**
         * Constructor.
         * @param code Direction code
         * @param sqlDir SQL equivalent
         */
        SortDirection(String code, String sqlDir) {
            this.code = code;
            this.dir = sqlDir;
        }

        /**
         * Get current SQL direction.
         * @return SQL direction 
         */
        public String getDirection() {
            return dir;
        }

        /**
         * Get current direction code.
         * @return A direction code
         */
        public String getCode() {
            return code;
        }

        /**
         * Get an enumeration value by code.
         * @param code Direction code
         * @return SortDirection enumerated type
         */
        public static SortDirection getByCode(String code) {
            return DIRS_BY_CODE.get(code);
        }

        /**
         * Convert enumeration to a SQL equivalent.
         * 
         * @return A string
         */
        @Override
        public String toString() {
            return dir;
        }
    }

    /**
     * Class for parsing a string according to a set separator and storing
     * the result internally as a collection of strings.
     */
    public abstract class BaseStringifier {

        /**
         * Default separator character.
         */
        public static final String DEFAULT_ITEM_SEPARATOR = "^";

        private String itemSeparator = DEFAULT_ITEM_SEPARATOR;

        /**
         * Get current item separator.
         * 
         * @return Item separator string
         */
        public String getItemSeparator() {
            return itemSeparator;
        }

        /**
         * Set current item separator.
         * 
         * @param itemSeparator Item separator string
         */
        public void setItemSeparator(String itemSeparator) {
            this.itemSeparator = itemSeparator;
        }

        /**
         * Split the given string according to the current separator.
         * 
         * @param str The string to split
         * @return An array of strings
         */
        protected String[] split(String str) {
            return StringUtils.split(str, itemSeparator);
        }

        /**
         * Parse a given string according to the current separator.
         * @param val The string to parse
         */
        public abstract void parse(String val);

        /**
         * Convert the currently parsed collection of strings to a full string.
         * 
         * @return A string
         */
        @Override
        public abstract String toString();
    }

    /**
     * String-ifier implementation based on a list of strings. 
     */
    public class ListStringifier extends BaseStringifier {

        private List<String> items = new ArrayList<String>();

        /**
         * Store a string in the collection.
         * @param val A string to store
         */
        public void put(String val) {
            if (val != null & val.length() > 0) {
                items.add(val);
            }
        }

        /**
         * Store the given set of strings.
         * 
         * @param items The strings to store.
         */
        public void setItems(List<String> items) {
            this.items = copyList(items);
        }

        /**
         * Get the current collection of parsed string values.
         * 
         * @return A list of strings.
         */
        public List<String> getItems() {
            return copyList(items);
        }

        /**
         * Utility call for copying a list.
         * 
         * @param fromList The list to copy
         * @return A copy of the original list
         */
        private List<String> copyList(List<String> fromList) {
            List<String> copy = new ArrayList<String>();
            for (String s : fromList) {
                copy.add(s);
            }

            return copy;
        }

        /**
         * Parse a given string into a collection of strings.
         * 
         * @param val A string to parse
         */
        @Override
        public void parse(String val) {
            items.clear();
            String[] strs = split(val);
            for (String s : strs) {
                put(s);
            }
        }

        /**
         * Convert the currently parsed collection of strings to a full string.
         * 
         * @return A string
         */
        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            for (String item : items) {
                if (buf.length() > 0) {
                    buf.append(getItemSeparator());
                }

                buf.append(item);
            }

            return buf.toString();
        }
    }

    /**
     * String-ifier implementation based on a map of strings. 
     */
    public class MapStringifier extends BaseStringifier {

        /**
         * Default assignment separator.
         */
        public static final String DEFAULT_ASSIGNMENT_SEPARATOR = "~";

        private String assignmentSeparator = DEFAULT_ASSIGNMENT_SEPARATOR;
        private Map<String, String> items = new LinkedHashMap<String, String>();

        /**
         * Get current assignment separator.
         * 
         * @return A string
         */
        public String getAssignmentSeparator() {
            return assignmentSeparator;
        }

        /**
         * Set current assignment separator.
         * 
         * @param assignmentSeparator A string
         */
        public void setAssignmentSeparator(String assignmentSeparator) {
            this.assignmentSeparator = assignmentSeparator;
        }

        /**
         * Put a given value in the collection of values.
         * 
         * @param name The name of the value
         * @param value The value
         */
        public void put(String name, String value) {
            if (value != null && value.length() > 0) {
                items.put(name, value);
            }
        }

        /**
         * Get the specified named value, or blank if not found.
         * 
         * @param name The name of the value
         * @return The value, or blank if not found
         */
        public String get(String name) {
            return get(name, "");
        }

        /**
         * Get the specified named value, or a default if not found.
         *
         * @param name The name of the value
         * @param dft A default value if not found
         * @return The value, or the default if not found
         */
        public String get(String name, String dft) {
            return items.containsKey(name) ? items.get(name) : dft;
        }

        /**
         * Set the current collection of items.
         * 
         * @param items The set of named values to store
         */
        public void setItems(Map<String, String> items) {
            this.items = copyMap(items);
        }

        /**
         * Get the current collection of stored items.
         * @return A collection of name/value pairs
         */
        public Map<String, String> getItems() {
            return copyMap(items);
        }

        /**
         * Utility method for copying a map of values.
         * @param fromMap The map to copy
         * @return A copy of the original map of values.
         */
        private Map<String, String> copyMap(Map<String, String> fromMap) {
            Map<String, String> copy = new LinkedHashMap<String, String>();
            for (Entry<String, String> e : fromMap.entrySet()) {
                copy.put(e.getKey(), fromMap.get(e.getKey()));
            }

            return copy;

        }

        /**
         * Parse a given string into a map of named values.
         * 
         * @param val A string to parse
         */
        @Override
        public void parse(String val) {
            items.clear();

            if (val == null) {
                return;
            }

            String[] itemStrs = split(val);
            for (String item : itemStrs) {
                String[] parts = StringUtils.split(item, assignmentSeparator);
                if (parts.length == 2) {
                    items.put(parts[0], parts[1]);
                }
            }
        }

        /**
         * Convert the current collection of values into a delimited string.
         * 
         * @return A delimited value string
         */
        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            for (String k : items.keySet()) {
                if (buf.length() > 0) {
                    buf.append(getItemSeparator());
                }

                buf.append(k);
                buf.append(assignmentSeparator);
                buf.append(items.get(k));
            }

            return buf.toString();
        }
    }
}