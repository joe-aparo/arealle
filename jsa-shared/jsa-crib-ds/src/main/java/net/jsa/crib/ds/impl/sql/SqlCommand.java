package net.jsa.crib.ds.impl.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates a SQL command string and any parameters inferred by presence of "<param_name>:" tokens within
 * the SQL.
 */
public class SqlCommand {

    private static final int INIT_PNAME_SIZE = 20;

    private String sql;
    private List<String> params;

    /**
     * Constructor sets required fields based on a sql statement.
     * 
     * @param sql A SQL statement
     */
    public SqlCommand(String sql) {
        this.sql = sql;
        this.params = parseParams(sql);
    }

    /**
     * Get the SQL statement.
     * 
     * @return A SQL statement
     */
    public String getSql() {
        return sql;
    }

    /**
     * Get a list of parameter names identified within the SQL command. A parameter is recognized by a name
     * followed by a colon (:), e.g. :ID
     * 
     * @return A list of parameter names
     */
    public List<String> getParams() {
        return params;
    }

    /**
     * Parse out a parameter statement looking for parameters.
     * 
     * @param sqlStr The sql string to parse
     * @return A list of identified parameter names
     */
    private List<String> parseParams(String sqlStr) {
        int pos = 0;
        int start = 0;
        char chr;
        List<String> parms = new ArrayList<String>();
        StringBuilder pname = new StringBuilder(INIT_PNAME_SIZE);

        while (pos < sqlStr.length()) {
            pos = sqlStr.indexOf(':', start);
            if (pos == -1) {
                break;
            }

            pos++; // skip delimiter
            if (pos >= sqlStr.length()) {
                break; // syntax error - bail
            }

            // clear buffer
            int len = pname.length();
            if (len > 0) {
                pname.delete(0, len);
            }

            while (true) {
                chr = sqlStr.charAt(pos++);
                if (!isValidIdentifierChar(chr)) {
                    break;
                }

                pname.append(chr);

                if (pos >= (sqlStr.length())) {
                    break;
                } else {
                    start = pos;
                }
            }

            if (pname.length() > 0) {
                parms.add(pname.toString());
            }
        }

        return parms;
    }

    /**
     * Indicates whether a character is valid for use in a parameter identifier.
     * 
     * @param chr The char to check
     * @return Validity indicator
     */
    private boolean isValidIdentifierChar(char chr) {
        return Character.isLetterOrDigit(chr) || chr == '_';
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SQL Statement: ").append(sql).append(" Params: ").append(params);

        return sb.toString();
    }
}
