package ind.jsa.crib.ds.api;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
}
