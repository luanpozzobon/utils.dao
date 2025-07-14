package lpz.utils.dao.postgresql;

import lpz.utils.dao.InsertBuilder;
import lpz.utils.dao.Result;
import lpz.utils.dao.helpers.Helper;
import lpz.utils.dao.helpers.SQLExecutor;
import lpz.utils.dao.helpers.SQLStatement;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public final class Insert<T> extends Operation implements InsertBuilder<T> {
    private static final Logger logger = Logger.getGlobal();

    private final Connection connection;
    private final Class<T> clazz;

    private final StringBuilder sql;

    protected boolean returning;

    protected Insert(final Class<T> clazz,
                     final Connection connection) {
        super();
        this.clazz = clazz;
        this.connection = connection;
        this.sql = new StringBuilder("INSERT INTO ");
        this.returning = false;

        this.init();
    }

    private void init() {
        this.into();
        this.fields();
    }

    private void into() {
        String tableName = Helper.getTableName(this.clazz);

        this.sql.append(tableName).append(" (");
    }

    private void fields() {
        Arrays.stream(this.clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                        && field.getAnnotation(lpz.utils.dao.annotations.Field.class).insertable())
                .forEach(field -> {
                    String fieldName = Helper.getFieldName(field);
                    this.sql.append(fieldName).append(", ");
                });

        sql.replace(sql.length() - 2, sql.length(), ") ");
    }

    private void values(final T entity) {
        List<Field> fields = Arrays.stream(this.clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                        && field.getAnnotation(lpz.utils.dao.annotations.Field.class).insertable()
                        && field.trySetAccessible())
                .toList();

        for (var field : fields) {
            lpz.utils.dao.postgresql.helper.Helper.addParam(this.sql, field.getType());
            this.sql.append(", ");
            try {
                this.values.add(field.get(entity));
            } catch (IllegalAccessException e) {
                logger.severe(e.getMessage());
            }
        }

        Helper.replaceFromLast(",", ") ", this.sql);
    }

    public Insert<T> returning() {
        this.returning = true;
        return this;
    }

    private PreparedStatement prepareStatement() throws SQLException {
        return SQLStatement.prepare(this.connection, this.sql, this.values);
    }

    @Override
    public Result<T> execute(final T entity) throws NullPointerException, SQLException {
        Objects.requireNonNull(entity, "The insertable entity must not be null.");

        this.sql.append("VALUES (");
        this.values(entity);

        if (this.returning)
            this.sql.append(" RETURNING *");

        try (PreparedStatement preparedStatement = this.prepareStatement()) {
            if (!this.returning) {
                return SQLExecutor.executeUpdate(preparedStatement, this.clazz);
            } else {
                return SQLExecutor.executeQuery(preparedStatement, this.clazz);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("You must have a 'No-Args' constructor declared in your entity");
        } catch (InstantiationException e) {
            throw new RuntimeException("Your entity must be instantiable");
        }
    }

    @Override
    public Result<T> execute(final List<T> entities) throws NullPointerException, SQLException {
        Objects.requireNonNull(entities, "The list of insertable entities must not be null.");

        this.sql.append("VALUES (");
        for (var entity : entities) {
            Objects.requireNonNull(entity, "The insertable entity must not be null.");

            this.values(entity);
            this.sql.append(", (");
        }
        Helper.replaceFromLast(", (", " ", this.sql);

        try (PreparedStatement preparedStatement = this.prepareStatement()) {
            if (this.sql.indexOf("RETURNING") == -1) {
                return SQLExecutor.executeUpdate(preparedStatement, this.clazz);
            } else {
                return SQLExecutor.executeQuery(preparedStatement, this.clazz);
            }
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
