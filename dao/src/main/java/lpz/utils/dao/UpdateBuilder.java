package lpz.utils.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * UpdateBuilder defines the contract for constructing database update operations.
 * It serves as a type-safe builder for creating and customizing SQL update statements
 * for a specified entity class.
 *
 * @param <T> the type of the entity class for which the update builder is to be used
 *
 * @since 1.0.0
 */
public interface UpdateBuilder<T> extends IOperation {
    /**
     * Initiates a {@code WHERE} clause in the SQL query for the specified field.
     *
     * @param field the database column name to apply the WHERE condition
     * @return a WhereBuilder instance for constructing the WHERE condition
     * @since 1.0.0
     */
    WhereBuilder<UpdateBuilder<T>> where(final String field);

    /**
     * Adds an {@code AND} condition to the existing {@code WHERE} clause for the specified field.
     *
     * @param field the database column name to apply the AND condition
     * @return a WhereBuilder instance for constructing the AND condition
     * @since 2.0.0
     */
    WhereBuilder<UpdateBuilder<T>> and(final String field);

    /**
     * Adds an {@code OR} condition to the existing {@code WHERE} clause for the specified field.
     *
     * @param field the database column name to apply the OR condition
     * @return a WhereBuilder instance for constructing the OR condition
     * @since 2.0.0
     */
    WhereBuilder<UpdateBuilder<T>> or(final String field);

    /**
     * Executes the constructed UPDATE query for a single entity.
     *
     * @param entity the entity to be updated in the database
     * @return Result object containing the query results
     * @throws SQLException if a database access error occurs
     * @since 1.0.0
     */
    Result<T> execute(final T entity) throws SQLException;

    /**
     * Executes the constructed UPDATE query for multiple entities.
     *
     * @param entities the list of entities to be updated in the database
     * @return Result object containing the query results
     * @throws SQLException if a database access error occurs
     * @since 1.0.0
     */
    Result<T> execute(final List<T> entities) throws SQLException;
}
