package lpz.utils.dao.enums;

/**
 * Enum representing SQL query clause keywords used for constructing WHERE conditions
 * in database queries. This enum provides standardized SQL keywords for building
 * query conditions with proper syntax.
 *
 * @since 2.0.0
 */

public enum WhereAppender {
    /**
     * Represents the initial {@code WHERE} clause in an SQL query to start conditions
     *
     * @since 2.0.0
     */
    WHERE("WHERE"),
    /**
     * Represents the {@code AND} operator to combine multiple conditions with logical {@code AND}
     *
     * @since 2.0.0
     */
    AND("AND"),
    /**
     * Represents the {@code OR} operator to combine multiple conditions with logical {@code OR}
     *
     * @since 2.0.0
     */
    OR("OR");

    private final String value;

    /**
     * Returns the SQL keyword associated with this enum constant.
     *
     * @return the SQL keyword string value
     * @since 2.0.0
     */
    public String getValue() {
        return value;
    }

    /**
     * Constructs a WhereAppender enum constant with the specified SQL keyword.
     *
     * @param value the SQL keyword string value
     * @since 2.0.0
     */
    WhereAppender(String value) {
        this.value = value;
    }
}
