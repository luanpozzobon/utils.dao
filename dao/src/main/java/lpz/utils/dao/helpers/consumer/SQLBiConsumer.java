package lpz.utils.dao.helpers.consumer;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLBiConsumer<T, U> {
    void accept(T t, U u) throws SQLException;
}
