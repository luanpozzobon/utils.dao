package lpz.utils.dao.helpers;

import lpz.utils.dao.helpers.consumer.SQLBiConsumer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class SQLStatement {
    private static final Map<Class<?>, SQLBiConsumer<PreparedStatement, Param>> TYPES = Map.of(
            String.class, ParamSetter::setString,
            UUID.class, ParamSetter::setUUID,
            Integer.class, ParamSetter::setInteger,
            Long.class, ParamSetter::setLong,
            Float.class, ParamSetter::setFloat,
            Double.class, ParamSetter::setDouble,
            Boolean.class, ParamSetter::setBoolean,
            BigDecimal.class, ParamSetter::setBigDecimal,
            Byte.class, ParamSetter::setByte,
            LocalDate.class, ParamSetter::setLocalDate
    );

    public static PreparedStatement prepare(final Connection connection,
                                            final StringBuilder sql,
                                            final List<Object> parameters) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

        int index = 0;
        for (Object p : parameters) {
            if (p == null) {
                ParamSetter.setNull(preparedStatement, new Param(++index, p));
                continue;
            }

            TYPES.getOrDefault(
                    p.getClass(),
                    ParamSetter::setDefault
            ).accept(preparedStatement, new Param(++index, p));
        }

        return preparedStatement;
    }
}
