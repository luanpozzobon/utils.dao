package lpz.utils.dao;

import java.util.List;

public interface WhereBuilder<T extends IOperation> {
    T equal(final Object value);
    T notEqual(final Object value);
    T greaterThan(final Object value);
    T lessThan(final Object value);
    T greaterThanOrEqual(final Object value);
    T lessThanOrEqual(final Object value);
    T like(final Object value);
    T ilike(final Object value);
    T notLike(final Object value);
    T in(final List<?> values);
}
