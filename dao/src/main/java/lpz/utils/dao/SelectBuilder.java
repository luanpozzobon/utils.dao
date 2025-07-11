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
    WhereBuilder<SelectBuilder<T>> where(final String field);

    Result<T> execute() throws SQLException;

    /**
     *
     * @since 1.1.0
     */
    SelectBuilder<T> limit(int limit);

    /**
     *
     * @since 1.1.0
     */
    SelectBuilder<T> limitless();

    /**
     *
     * @since 1.1.0
     */
    SelectBuilder<T> offset(int offset);

    /**
     *
     * @since 1.1.0
     */
    SelectBuilder<T> page(int page, int pageSize);

    /**
     *
     * @since 1.1.0
     */
    SelectBuilder<T> page(int page);
}
