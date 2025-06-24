package lpz.utils.dao.postgresql;

import lpz.utils.dao.*;
import lpz.utils.dao.exceptions.MissingAnnotationException;

import java.util.Objects;

public class CRUDBuilderFactory implements ICRUDBuilderFactory {
    private final java.sql.Connection connection;

    public CRUDBuilderFactory(java.sql.Connection connection) throws NullPointerException {
        this.connection = Objects.requireNonNull(connection, "The connection cannot be null.");
    }

    private static <T> void validate(Class<T> clazz) throws NullPointerException, MissingAnnotationException {
        Objects.requireNonNull(clazz, "The class parameter cannot be null.");

        if (!clazz.isAnnotationPresent(lpz.utils.dao.annotations.Table.class))
            throw new MissingAnnotationException("The class must be annotated with @Table.");
    }

    @Override
    public <T> InsertBuilder<T> insert(Class<T> clazz) throws NullPointerException, MissingAnnotationException {
        validate(clazz);

        return new Insert<>(clazz, this.connection);
    }

    @Override
    public <T> SelectBuilder<T> select(Class<T> clazz) throws NullPointerException, MissingAnnotationException {
        validate(clazz);

        return new Select<>(clazz, this.connection);
    }

    @Override
    public <T> UpdateBuilder<T> update(Class<T> clazz) throws NullPointerException, MissingAnnotationException {
        validate(clazz);

        return new Update<>(clazz, this.connection);
    }

    @Override
    public <T> DeleteBuilder<T> delete(Class<T> clazz) throws NullPointerException, MissingAnnotationException {
        validate(clazz);

        return new Delete<>(clazz, this.connection);
    }
}