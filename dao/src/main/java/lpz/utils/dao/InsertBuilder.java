package lpz.utils.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * InsertBuilder defines the contract for constructing database insert operations.
 * It serves as a type-safe builder for creating and customizing SQL insert statements
 * for a specified entity class.
 *
 * @param <T> the type of the entity class for which the insert builder is to be used
 * @since 1.0.0
 */
public interface InsertBuilder<T> extends IOperation {

    /**
     * Configures the insert operation to return the generated keys or inserted entity after execution.
     *
     * @return the current InsertBuilder instance for method chaining
     * @since 1.0.0
     */
    InsertBuilder<T> returning();

    /**
     * Executes the insert operation for a single entity.
     *
     * @param entity the entity to be inserted into the database
     * @return Result containing the inserted entity and any generated keys
     * @throws NullPointerException if the provided entity is null
     * @throws SQLException         if a database access error occurs
     * @since 1.0.0
     */
    Result<T> execute(final T entity) throws NullPointerException, SQLException;

    /**
     * Executes the insert operation for multiple entities in batch.
     *
     * @param entities the list of entities to be inserted into the database
     * @return Result containing the inserted entities and any generated keys
     * @throws NullPointerException if the provided entities list is null
     * @throws SQLException         if a database access error occurs
     * @since 1.0.0
     */
    Result<T> execute(final List<T> entities) throws NullPointerException, SQLException;
}
