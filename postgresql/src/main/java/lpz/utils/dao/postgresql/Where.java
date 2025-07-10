package lpz.utils.dao.postgresql;

import lpz.utils.dao.IOperation;
import lpz.utils.dao.WhereBuilder;
import lpz.utils.dao.helpers.Helper;

import java.util.List;
import java.util.UUID;

public class Where<T extends IOperation> implements WhereBuilder<T> {
    private final String field;
    private final T operation;
    private final StringBuilder clause;

    protected Where(final String field,
                    final T operation) {
        this.field = field;
        this.operation = operation;
        this.clause = new StringBuilder();
    }

    public T equal(final Object value) {
        this.clause.append(field).append(" = ");
        lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
        this.clause.append(" ");

        this.operation.where(clause.toString(), value);

        return operation;
    }

    public T notEqual(final Object value) {
        this.clause.append(field).append(" != ");
        lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
        this.clause.append(" ");

        this.operation.where(clause.toString(), value);

        return operation;
    }

    public T greaterThan(final Object value) {
        this.clause.append(field).append(" > ");
        lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
        this.clause.append(" ");

        this.operation.where(clause.toString(), value);

        return operation;
    }

    public T lessThan(final Object value) {
        this.clause.append(field).append(" < ");
        lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
        this.clause.append(" ");

        this.operation.where(clause.toString(), value);

        return operation;
    }

    public T greaterThanOrEqual(final Object value) {
        this.clause.append(field).append(" >= ");
        lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
        this.clause.append(" ");

        this.operation.where(clause.toString(), value);

        return operation;
    }

    public T lessThanOrEqual(final Object value) {
        this.clause.append(field).append(" <= ");
        lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
        this.clause.append(" ");

        this.operation.where(clause.toString(), value);

        return operation;
    }

    public T like(final Object value) {
        this.clause.append(field).append(" LIKE '%' || ");
        lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
        this.clause.append(" || '%' ");

        this.operation.where(clause.toString(), value);

        return operation;
    }

    public T ilike(final Object value) {
        this.clause.append(field).append(" ILIKE '%' || ");
        lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
        this.clause.append(" || '%' ");

        this.operation.where(clause.toString(), value);

        return operation;
    }

    public T notLike(final Object value) {
        this.clause.append(field).append(" NOT LIKE '%' || ");
        lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
        this.clause.append(" || '%' ");

        this.operation.where(clause.toString(), value);

        return operation;
    }

    public T in(final List<?> values) {
        this.clause.append(this.field).append(" IN (");
        values.forEach(value -> {
            lpz.utils.dao.postgresql.helper.Helper.addParam(this.clause, value.getClass());
            this.clause.append(", ");
        });

        Helper.replaceFromLast(",", ")", this.clause);
        this.operation.where(clause.toString(), values.toArray());

        return operation;
    }
}
