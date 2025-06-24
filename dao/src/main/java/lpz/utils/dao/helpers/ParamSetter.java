package lpz.utils.dao.helpers;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public abstract class ParamSetter {
    protected static void setString(PreparedStatement statement, Param param) throws SQLException {
        statement.setString(param.index(), (String) param.value());
    }

    protected static void setUUID(PreparedStatement statement, Param param) throws SQLException {
        statement.setString(param.index(), ((UUID) param.value()).toString());
    }

    protected static void setInteger(PreparedStatement statement, Param param) throws SQLException {
        statement.setInt(param.index(), (Integer) param.value());
    }

    protected static void setLong(PreparedStatement statement, Param param) throws SQLException {
        statement.setLong(param.index(), (Long) param.value());
    }

    protected static void setFloat(PreparedStatement statement, Param param) throws SQLException {
        statement.setFloat(param.index(), (Float) param.value());
    }

    protected static void setDouble(PreparedStatement statement, Param param) throws SQLException {
        statement.setDouble(param.index(), (Double) param.value());
    }

    protected static void setBoolean(PreparedStatement statement, Param param) throws SQLException {
        statement.setBoolean(param.index(), (Boolean) param.value());
    }

    protected static void setBigDecimal(PreparedStatement statement, Param param) throws SQLException {
        statement.setBigDecimal(param.index(), (BigDecimal) param.value());
    }

    protected static void setByte(PreparedStatement statement, Param param) throws SQLException {
        statement.setByte(param.index(), (Byte) param.value());
    }

    protected static void setLocalDate(PreparedStatement statement, Param param) throws SQLException {
        statement.setDate(param.index(), java.sql.Date.valueOf(((LocalDate) param.value())));
    }

    protected static void setDefault(PreparedStatement statement, Param param) throws SQLException {
        statement.setObject(param.index(), param.value());
    }

}
