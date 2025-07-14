package lpz.utils.dao.postgresql;

import lpz.utils.dao.DeleteBuilder;
import lpz.utils.dao.Result;
import lpz.utils.dao.WhereBuilder;
import lpz.utils.dao.helpers.Helper;
import lpz.utils.dao.helpers.SQLExecutor;
import lpz.utils.dao.helpers.SQLStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public final class Delete<T> extends Operation implements DeleteBuilder<T> {
    private static final Logger logger = Logger.getGlobal();

    private final Class<T> clazz;
    private final Connection connection;

    private final StringBuilder sql;

    protected Delete(final Class<T> clazz,
                     final Connection connection) {
        super();
        this.clazz = clazz;
        this.connection = connection;
        this.sql = new StringBuilder("DELETE FROM ");

        this.init();
    }

    private void init() {
        String tableName = Helper.getTableName(this.clazz);
        this.sql.append(tableName).append(" ");
    }

    @Override
    public WhereBuilder<DeleteBuilder<T>> where(final String field) {
        return new Where<>(field, this);
    }

    private void where(final T entity) {
        Arrays.stream(this.clazz.getDeclaredFields())
                .filter(field -> {
                            try {
                                return field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                                        && field.getAnnotation(lpz.utils.dao.annotations.Field.class).primaryKey()
                                        && field.trySetAccessible()
                                        && field.get(entity) != null;
                            } catch (IllegalAccessException e) {
                                logger.severe(e.getMessage());
                                return false;
                            }
                        }
                )
                .forEach(field -> {
                    try {
                        this.where(Helper.getFieldName(field)).equal(field.get(entity));
                    } catch (IllegalAccessException e) {
                        logger.severe(e.getMessage());
                    }
                });
    }

    private PreparedStatement prepareStatement() throws SQLException {
        return SQLStatement.prepare(this.connection, this.sql.append(this.where), this.values);
    }

    @Override
    public Result<T> execute() throws NullPointerException, SQLException {
        if (this.where == null) {
            throw new RuntimeException("You must specify a where clause to execute a delete operation");
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
    public Result<T> execute(T entity) throws NullPointerException, SQLException {
        Objects.requireNonNull(entity, "The deletable entity must not be null.");

        this.where(entity);

        try (PreparedStatement preparedStatement = this.prepareStatement()) {
            return SQLExecutor.executeUpdate(preparedStatement, this.clazz);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("You must have a 'No-Args' constructor declared in your entity");
        } catch (InstantiationException e) {
            throw new RuntimeException("Your entity must be instantiable");
        }
    }

    protected String getSQL() {
        return this.sql.toString();
    }
}
