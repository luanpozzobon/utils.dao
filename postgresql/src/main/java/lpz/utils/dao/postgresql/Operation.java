package lpz.utils.dao.postgresql;

import lpz.utils.dao.IOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract sealed class Operation implements IOperation permits Delete, Insert, Select, Update {
    protected StringBuilder where;
    protected List<Object> values;

    protected Operation() {
        this.values = new ArrayList<>();
    }

    public Operation where(String clause, Object... values) {
        if (where == null) {
            where = new StringBuilder("WHERE ");
        }  else {
            where.append("AND ");
        }

        where.append(clause);
        this.values.addAll(Arrays.asList(values));
        return this;
    }

}
