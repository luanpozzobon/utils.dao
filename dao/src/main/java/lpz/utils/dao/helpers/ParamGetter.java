package lpz.utils.dao.helpers;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public abstract class ParamGetter {
    protected static String getString(ResultSet resultSet, String fieldName) throws SQLException {
        return resultSet.getString(fieldName);
    }

    protected static UUID getUUID(ResultSet resultSet, String fieldName) throws SQLException {
        String id = resultSet.getString(fieldName);
        return id != null ? UUID.fromString(id) : null;
    }

    protected static Integer getInteger(ResultSet resultSet, String fieldName) throws SQLException {
        return resultSet.getInt(fieldName);
    }

    protected static Long getLong(ResultSet resultSet, String fieldName) throws SQLException {
        return resultSet.getLong(fieldName);
    }

    protected static Float getFloat(ResultSet resultSet, String fieldName) throws SQLException {
        return resultSet.getFloat(fieldName);
    }

    protected static Double getDouble(ResultSet resultSet, String fieldName) throws SQLException {
        return resultSet.getDouble(fieldName);
    }

    protected static Boolean getBoolean(ResultSet resultSet, String fieldName) throws SQLException {
        return resultSet.getBoolean(fieldName);
    }

    protected static BigDecimal getBigDecimal(ResultSet resultSet, String fieldName) throws SQLException {
        return resultSet.getBigDecimal(fieldName);
    }

    protected static Byte getByte(ResultSet resultSet, String fieldName) throws SQLException {
        return resultSet.getByte(fieldName);
    }

    protected static LocalDate getLocalDate(ResultSet resultSet, String fieldName) throws SQLException {
        java.sql.Date date = resultSet.getDate(fieldName);
        return date != null ? date.toLocalDate() : null;
    }

    protected static Object getDefault(ResultSet resultSet, String fieldName) throws SQLException {
        return resultSet.getObject(fieldName);
    }
}
