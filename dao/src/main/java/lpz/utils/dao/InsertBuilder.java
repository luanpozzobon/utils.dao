package lpz.utils.dao;


import java.lang.reflect.InvocationTargetException;
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
    InsertBuilder<T> returning();

    Result<T> execute(final T entity) throws NullPointerException, SQLException;

    Result<T> execute(final List<T> entities) throws NullPointerException, SQLException;
}
