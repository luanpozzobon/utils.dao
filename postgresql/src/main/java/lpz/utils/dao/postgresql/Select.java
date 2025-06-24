package lpz.utils.dao.postgresql;

import lpz.utils.dao.Result;
import lpz.utils.dao.SelectBuilder;
import lpz.utils.dao.WhereBuilder;
import lpz.utils.dao.helpers.Helper;
import lpz.utils.dao.helpers.SQLExecutor;
import lpz.utils.dao.helpers.SQLStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public final class Select<T> extends Operation implements SelectBuilder<T> {
    private final Connection connection;
    private final Class<T> clazz;
    private final String tableName;

    private final StringBuilder sql;

    protected Select(final Class<T> clazz,
                     final Connection connection) {
        super();
        this.clazz = clazz;
        this.connection = connection;
        this.tableName = Helper.getTableName(this.clazz);

        this.sql = new StringBuilder("SELECT ");
        this.init();
    }

    private void init() {
        this.fields();
        this.from();
    }

    private void fields() {
        Arrays.stream(this.clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class))
                .forEach(field -> {
                    String fieldName = Helper.getFieldName(field);

                    this.sql.append(this.tableName).append(".");
                    this.sql.append(fieldName).append(", ");
                });

        Helper.replaceFromLast(",", " ", this.sql);
    }

    private void from() {
        this.sql.append("FROM ").append(this.tableName).append(" ");
    }

    public WhereBuilder<SelectBuilder<T>> where(final String field) {
        return new Where<>(field, this);
    }

    private PreparedStatement prepareStatement() throws SQLException {
        return SQLStatement.prepare(this.connection, (this.sql.append(this.where.toString())), this.values);
    }

    public Result<T> execute() throws SQLException {
        try (PreparedStatement preparedStatement = this.prepareStatement()) {
            return SQLExecutor.executeQuery(preparedStatement, this.clazz);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("You must have a 'No-Args' constructor declared in your entity");
        } catch (InstantiationException e) {
            throw new RuntimeException("Your entity must be instantiable");
        }
    }
}