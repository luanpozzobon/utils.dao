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
    private static final int MIN_LIMIT = 1;
    private static final int NO_LIMIT = 0;
    private static final int DEFAULT_LIMIT = 50;

    private static final int MIN_OFFSET = 0;

    private final Connection connection;
    private final Class<T> clazz;
    private final String tableName;

    private final StringBuilder sql;

    protected Integer limit = null;
    protected Integer offset = null;

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

    public Select<T> limit(final int limit) {
        this.limit = Math.max(MIN_LIMIT, limit);

        return this;
    }

    public Select<T> limitless() {
        this.limit = NO_LIMIT;

        return this;
    }

    public Select<T> offset(final int offset) {
        this.offset = Math.max(MIN_OFFSET, offset);

        return this;
    }

    public Select<T> page(final int page, final int pageSize) {
        this.limit = pageSize;
        this.offset = Math.max(MIN_OFFSET, (page - 1) * this.limit);

        return this;
    }

    public Select<T> page(final int page) {
        if (this.limit == null) this.limit = DEFAULT_LIMIT;

        this.offset = Math.max(MIN_OFFSET, (page - 1) * this.limit);

        return this;
    }

    private PreparedStatement prepareStatement() throws SQLException {
        if (this.where != null) this.sql.append(this.where).append(" ");

        if (this.limit == null) this.limit = DEFAULT_LIMIT;
        if (this.limit > NO_LIMIT) this.sql.append("LIMIT ").append(this.limit).append(" ");

        if (this.offset != null) this.sql.append("OFFSET ").append(this.offset).append(" ");

        return SQLStatement.prepare(this.connection, (this.sql), this.values);
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

    protected String getSQL() {
        return this.sql.toString();
    }
}