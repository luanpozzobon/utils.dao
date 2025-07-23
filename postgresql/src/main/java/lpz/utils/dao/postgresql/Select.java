package lpz.utils.dao.postgresql;

import lpz.utils.dao.Result;
import lpz.utils.dao.SelectBuilder;
import lpz.utils.dao.WhereBuilder;
import lpz.utils.dao.annotations.Join;
import lpz.utils.dao.annotations.JoinOn;
import lpz.utils.dao.enums.WhereAppender;
import lpz.utils.dao.helpers.Helper;
import lpz.utils.dao.helpers.JoinHelper;
import lpz.utils.dao.helpers.SQLExecutor;
import lpz.utils.dao.helpers.SQLStatement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    private final StringBuilder from;

    protected Integer limit = null;
    protected Integer offset = null;
    protected List<Class<?>> joinClasses = new ArrayList<>();

    protected Select(final Class<T> clazz,
                     final Connection connection) {
        super();
        this.clazz = clazz;
        this.connection = connection;
        this.tableName = Helper.getTableName(this.clazz);

        this.sql = new StringBuilder("SELECT ");
        this.from = new StringBuilder("FROM ");
        this.init();
    }

    private void init() {
        this.fields(this.clazz);
        this.from();
    }

    private void fields(Class<?> clazz) {
        final String tableName = Helper.getTableName(clazz);
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class))
                .forEach(field -> {
                    String fieldName = Helper.getFieldName(field);

                    this.sql.append(tableName).append(".");
                    this.sql.append(fieldName);
                    this.sql.append(" AS \"").append(Helper.getQualifiedName(clazz, field)).append("\", ");
                });
    }

    private void from() {
        this.from.append(this.tableName).append(" ");
    }

    public WhereBuilder<SelectBuilder<T>> where(final String field) {
        return new Where<>(WhereAppender.WHERE, field, this);
    }

    public WhereBuilder<SelectBuilder<T>> and(final String field) {
        return new Where<>(WhereAppender.AND, field, this);
    }

    public WhereBuilder<SelectBuilder<T>> or(final String field) {
        return new Where<>(WhereAppender.OR, field, this);
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

    private void join(final String join, final Field[] fields) {
        Arrays.stream(fields)
                .forEach(field -> {
                    this.from.append(join);

                    Class<?> joinClass = JoinHelper.getJoinClass(field);

                    this.fields(joinClass);

                    final String tableName = Helper.getTableName(joinClass);
                    this.from.append(tableName).append(" ");

                    JoinOn[] joinOn = field.getAnnotation(Join.class).on();
                    for (JoinOn on : joinOn) {
                        this.from.append("ON ").append(this.tableName).append(".");
                        this.from.append(on.localField()).append(" = ");
                        this.from.append(tableName).append(".");
                        this.from.append(on.foreignField()).append(" AND ");
                    }

                    this.joinClasses.add(joinClass);
                    Helper.replaceFromLast("AND ", "", this.from);
                });

    }

    private void join(final String join) {
        Field[] fields = Arrays.stream(this.clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Join.class))
                .toArray(Field[]::new);

        this.join(join, fields);
    }

    private void join(final String join, final String field) {
        Field[] fields = Arrays.stream(this.clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Join.class)
                        && (Helper.getFieldName(f).equals(field)
                        || f.getName().equals(field))
                ).toArray(Field[]::new);

        this.join(join, fields);
    }

    @Override
    public Select<T> leftJoin() {
        final String join = "LEFT JOIN ";

        this.join(join);
        return this;
    }

    @Override
    public Select<T> leftJoin(String field) {
        final String join = "LEFT JOIN ";

        this.join(join, field);
        return this;
    }

    @Override
    public Select<T> innerJoin() {
        final String join = "INNER JOIN ";

        this.join(join);
        return this;
    }

    @Override
    public Select<T> innerJoin(String field) {
        final String join = "INNER JOIN ";

        this.join(join, field);
        return this;
    }

    private PreparedStatement prepareStatement() throws SQLException {
        Helper.replaceFromLast(",", " ", this.sql);
        this.sql.append(this.from).append(" ");
        if (this.where != null) this.sql.append(this.where).append(" ");

        if (this.limit == null) this.limit = DEFAULT_LIMIT;
        if (this.limit > NO_LIMIT) this.sql.append("LIMIT ").append(this.limit).append(" ");

        if (this.offset != null) this.sql.append("OFFSET ").append(this.offset).append(" ");

        return SQLStatement.prepare(this.connection, (this.sql), this.values);
    }

    public Result<T> execute() throws SQLException {
        try (PreparedStatement preparedStatement = this.prepareStatement()) {
            return SQLExecutor.executeQuery(preparedStatement, this.clazz, this.joinClasses, true);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("You must have a public 'No-Args' constructor declared in your entity");
        } catch (InstantiationException e) {
            throw new RuntimeException("Your entity must be instantiable");
        }
    }

    protected String getSQL() {
        return this.sql.toString();
    }
}