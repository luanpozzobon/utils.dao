package lpz.utils.dao;

import java.sql.SQLException;

/**
 * SelectBuilder defines the contract for constructing database select operations.
 * It serves as a type-safe builder for creating and customizing SQL select statements
 * for a specified entity class.
 *
 * @param <T> the type of the entity class for which the select builder is to be used
 *
 * @since 1.0.0
 */
public interface SelectBuilder<T> extends IOperation {
    /**
     * Initiates a {@code WHERE} clause in the SQL query for the specified field.
     *
     * @param field the database column name to apply the WHERE condition
     * @return a WhereBuilder instance for constructing the WHERE condition
     * @since 1.0.0
     */
    WhereBuilder<SelectBuilder<T>> where(final String field);

    /**
     * Adds an {@code AND} condition to the existing {@code WHERE} clause for the specified field.
     *
     * @param field the database column name to apply the AND condition
     * @return a WhereBuilder instance for constructing the AND condition
     * @since 2.0.0
     */
    WhereBuilder<SelectBuilder<T>> and(final String field);

    /**
     * Adds an {@code OR} condition to the existing {@code WHERE} clause for the specified field.
     *
     * @param field the database column name to apply the OR condition
     * @return a WhereBuilder instance for constructing the OR condition
     * @since 2.0.0
     */
    WhereBuilder<SelectBuilder<T>> or(final String field);

    /**
     * Executes the constructed SELECT query and returns the result.
     *
     * @return Result object containing the query results
     * @throws SQLException if a database access error occurs
     * @since 1.0.0
     */
    Result<T> execute() throws SQLException;

    /**
     * Sets the maximum number of records to be returned by the query.
     *
     * @param limit the maximum number of records to return
     * @return this SelectBuilder instance for method chaining
     * @since 1.1.0
     */
    SelectBuilder<T> limit(int limit);

    /**
     * Removes any previously set limit on the number of records to be returned.
     *
     * @return this SelectBuilder instance for method chaining
     * @since 1.1.0
     */
    SelectBuilder<T> limitless();

    /**
     * Sets the number of records to skip before starting to return records.
     *
     * @param offset the number of records to skip
     * @return this SelectBuilder instance for method chaining
     * @since 1.1.0
     */
    SelectBuilder<T> offset(int offset);

    /**
     * Sets up pagination with specified page number and page size.
     *
     * @param page the page number (1-based)
     * @param pageSize the number of records per page
     * @return this SelectBuilder instance for method chaining
     * @since 1.1.0
     */
    SelectBuilder<T> page(int page, int pageSize);

    /**
     * Sets up pagination with specified page number using default page size.
     *
     * @param page the page number (1-based)
     * @return this SelectBuilder instance for method chaining
     * @since 1.1.0
     */
    SelectBuilder<T> page(int page);

    /**
     * Adds a LEFT JOIN clause to the query for all join fields in the entity.
     *
     * @return this SelectBuilder instance for method chaining
     * @since 2.0.0
     */
    SelectBuilder<T> leftJoin();

    /**
     * Adds a LEFT JOIN clause to the query for the specified field.
     *
     * @param field the field name to join on
     * @return this SelectBuilder instance for method chaining
     * @since 2.0.0
     */
    SelectBuilder<T> leftJoin(String field);

    /**
     * Adds an INNER JOIN clause to the query for all join fields in the entity.
     *
     * @return this SelectBuilder instance for method chaining
     * @since 2.0.0
     */
    SelectBuilder<T> innerJoin();

    /**
     * Adds an INNER JOIN clause to the query for the specified field.
     *
     * @param field the field name to join on
     * @return this SelectBuilder instance for method chaining
     * @since 2.0.0
     */
    SelectBuilder<T> innerJoin(String field);
}
