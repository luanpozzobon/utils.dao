package lpz.utils.dao;

import java.util.List;

/**
 * Builder interface for constructing SQL WHERE clause conditions.
 * Provides methods to create various comparison operations in SQL queries.
 *
 * @param <T> the type of operation that implements IOperation interface
 * @since 1.0.0
 */
public interface WhereBuilder<T extends IOperation> {
    /**
     * Creates an equality condition (column = value).
     *
     * @param value the value to compare against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T equal(final Object value);

    /**
     * Creates an inequality condition (column != value).
     *
     * @param value the value to compare against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T notEqual(final Object value);

    /**
     * Creates a greater than condition (column > value).
     *
     * @param value the value to compare against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T greaterThan(final Object value);

    /**
     * Creates a less than condition (column < value).
     *
     * @param value the value to compare against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T lessThan(final Object value);

    /**
     * Creates a greater than or equal condition (column >= value).
     *
     * @param value the value to compare against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T greaterThanOrEqual(final Object value);

    /**
     * Creates a less than or equal condition (column <= value).
     *
     * @param value the value to compare against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T lessThanOrEqual(final Object value);

    /**
     * Creates a LIKE condition for pattern matching (column LIKE value).
     *
     * @param value the pattern to match against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T like(final Object value);

    /**
     * Creates a case-insensitive LIKE condition (column ILIKE value).
     *
     * @param value the pattern to match against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T ilike(final Object value);

    /**
     * Creates a NOT LIKE condition (column NOT LIKE value).
     *
     * @param value the pattern to not match against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T notLike(final Object value);

    /**
     * Creates an IN condition (column IN (values)).
     *
     * @param values the list of values to match against
     * @return the operation instance for method chaining
     * @since 1.0.0
     */
    T in(final List<?> values);
}
