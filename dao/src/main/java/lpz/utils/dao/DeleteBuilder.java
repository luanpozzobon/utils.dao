package lpz.utils.dao;

import java.sql.SQLException;

/**
 * DeleteBuilder defines the contract for constructing database delete operations.
 * It serves as a type-safe builder for creating and customizing SQL delete statements
 * for a specified entity class.
 *
 * @param <T> the type of the entity class for which the delete builder is to be used
 *
 * @since 1.0.0
 */
public interface DeleteBuilder<T> extends IOperation {

    /**
     * Initiates a {@code WHERE} clause in the SQL query for the specified field.
     *
     * @param field the database column name to apply the WHERE condition
     * @return a WhereBuilder instance for constructing the WHERE condition
     * @since 1.0.0
     */
    WhereBuilder<DeleteBuilder<T>> where(final String field);

    /**
     * Adds an {@code AND} condition to the existing {@code WHERE} clause for the specified field.
     *
     * @param field the database column name to apply the AND condition
     * @return a WhereBuilder instance for constructing the AND condition
     * @since 2.0.0
     */
    WhereBuilder<DeleteBuilder<T>> and(final String field);

    /**
     * Adds an {@code OR} condition to the existing {@code WHERE} clause for the specified field.
     *
     * @param field the database column name to apply the OR condition
     * @return a WhereBuilder instance for constructing the OR condition
     * @since 2.0.0
     */
    WhereBuilder<DeleteBuilder<T>> or(final String field);

    /**
     * Executes the constructed DELETE query.
     *
     * @return Result object containing the query results
     * @throws NullPointerException if required query parameters are null
     * @throws SQLException         if a database access error occurs
     * @since 1.0.0
     */
    Result<T> execute() throws NullPointerException, SQLException;

    /**
     * Executes the constructed DELETE query using the provided entity.
     *
     * @param entity the entity to be used for the delete operation
     * @return Result object containing the query results
     * @throws NullPointerException if the entity or required query parameters are null
     * @throws SQLException         if a database access error occurs
     * @since 1.0.0
     */
    Result<T> execute(T entity) throws NullPointerException, SQLException;
}
