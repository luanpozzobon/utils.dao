package lpz.utils.dao.postgresql;

import lpz.utils.dao.IOperation;
import lpz.utils.dao.enums.WhereAppender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract sealed class Operation implements IOperation permits Delete, Insert, Select, Update {
    protected StringBuilder where;
    protected List<Object> values;

    protected Operation() {
        this.values = new ArrayList<>();
    }

    public Operation where(WhereAppender appender,
                           final String clause,
                           final Object... values) {
        if (this.where == null) {
            this.where = new StringBuilder(WhereAppender.WHERE.getValue()).append(" ");
        } else {
            if (WhereAppender.WHERE.equals(appender)) appender = WhereAppender.AND;

            this.where.append(appender.getValue()).append(" ");
        }
        where.append(clause);
        this.values.addAll(Arrays.asList(values));
        return this;
    }

}
