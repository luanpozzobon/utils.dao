package lpz.utils.dao.helpers.consumer;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLResult<T> {
    T apply(ResultSet rs, String fieldName) throws SQLException;
}
