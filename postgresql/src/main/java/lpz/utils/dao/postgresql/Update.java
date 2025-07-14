package lpz.utils.dao.postgresql;

import lpz.utils.dao.Result;
import lpz.utils.dao.UpdateBuilder;
import lpz.utils.dao.WhereBuilder;
import lpz.utils.dao.helpers.Helper;
import lpz.utils.dao.helpers.SQLExecutor;
import lpz.utils.dao.helpers.SQLStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public final class Update<T> extends Operation implements UpdateBuilder<T> {
    private static final Logger logger = Logger.getGlobal();

    private final Class<T> clazz;
    private final Connection connection;

    private final StringBuilder sql;

    protected Update(final Class<T> clazz,
                     final Connection connection) {
        super();
        this.clazz = clazz;
        this.connection = connection;
        this.sql = new StringBuilder("UPDATE ");

        this.init();
    }

    private void init() {
        String tableName = Helper.getTableName(this.clazz);
        this.sql.append(tableName).append(" SET ");
    }

    @Override
    public WhereBuilder<UpdateBuilder<T>> where(final String field) {
        return new Where<>(field, this);
    }

    private void where(final T entity) {
        Arrays.stream(this.clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                        && field.getAnnotation(lpz.utils.dao.annotations.Field.class).primaryKey()
                        && field.trySetAccessible())
                .forEach(field -> {
                    final String fieldName = Helper.getFieldName(field);
                    try {
                        this.where(fieldName).equal(field.get(entity));
                    } catch (IllegalAccessException e) {
                        logger.severe(e.getMessage());
                    }
                });
    }

    private PreparedStatement prepareStatement() throws SQLException {
        return SQLStatement.prepare(this.connection, this.sql.append(this.where), this.values);
    }

    private void values(final T entity) {
        List<Object> temp = null;
        if (!this.values.isEmpty()) {
            temp = List.copyOf(this.values);
            this.values.clear();
        }

        Arrays.stream(this.clazz.getDeclaredFields())
                .filter(field -> {
                    try {
                        return field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                                && field.getAnnotation(lpz.utils.dao.annotations.Field.class).updatable()
                                && !field.getAnnotation(lpz.utils.dao.annotations.Field.class).primaryKey()
                                && field.trySetAccessible()
                                && field.get(entity) != null;
                    } catch (IllegalAccessException e) {
                        logger.severe(e.getMessage());
                        return false;
                    }
                })
                .forEach(field -> {
                    this.sql.append(Helper.getFieldName(field)).append(" = ");
                    lpz.utils.dao.postgresql.helper.Helper.addParam(this.sql, field.getType());
                    this.sql.append(", ");

                    try {
                        this.values.add(field.get(entity));
                    } catch (IllegalAccessException e) {
                        logger.severe(e.getMessage());
                    }
                });

        Helper.replaceFromLast(",", " ", this.sql);

        if (temp != null && !temp.isEmpty())
            this.values.addAll(temp);
    }

    @Override
    public Result<T> execute(final T entity) throws SQLException {
        Objects.requireNonNull(entity, "The updatable entity must not be null.");

        this.values(entity);
        if (this.where == null) {
            this.where(entity);
        }

        try (PreparedStatement preparedStatement = this.prepareStatement()) {
            return SQLExecutor.executeUpdate(preparedStatement, this.clazz);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("You must have a 'No-Args' constructor declared in your entity");
        } catch (InstantiationException e) {
            throw new RuntimeException("Your entity must be instantiable");
        }
    }

    @Override
    public Result<T> execute(List<T> entities) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected String getSQL() {
        return this.sql.toString();
    }
}
