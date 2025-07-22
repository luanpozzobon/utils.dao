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
     *
     * @param field
     *
     *
     * @since 1.0.0
     */
    WhereBuilder<DeleteBuilder<T>> where(final String field);

    /**
     *
     * @param field
     *
     *
     * @since 2.0.0
     */
    WhereBuilder<DeleteBuilder<T>> and(final String field);

    /**
     *
     * @param field
     *
     *
     * @since 2.0.0
     */
    WhereBuilder<DeleteBuilder<T>> or(final String field);

    Result<T> execute() throws NullPointerException, SQLException;

    Result<T> execute(T entity) throws NullPointerException, SQLException;
}
