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
     *
     * @param field
     *
     * @since 1.0.0
     */
    WhereBuilder<UpdateBuilder<T>> where(final String field);

    /**
     *
     * @param field
     *
     *
     * @since 2.0.0
     */
    WhereBuilder<UpdateBuilder<T>> and(final String field);

    /**
     *
     * @param field
     *
     *
     * @since 2.0.0
     */
    WhereBuilder<UpdateBuilder<T>> or(final String field);

    Result<T> execute(final T entity) throws SQLException;

    Result<T> execute(final List<T> entities) throws SQLException;
}
