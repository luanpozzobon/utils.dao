package lpz.utils.dao.postgresql.helper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class Helper {
    private Helper() { }
    private static final Map<Class<?>, Consumer<StringBuilder>> TYPE = Map.of(
            String.class, ParamAdder::addString,
            UUID.class, ParamAdder::addUUID,
            Integer.class, ParamAdder::addInteger,
            Long.class, ParamAdder::addLong,
            Float.class, ParamAdder::addFloat,
            Double.class, ParamAdder::addDouble,
            Boolean.class, ParamAdder::addBoolean,
            BigDecimal.class, ParamAdder::addBigDecimal,
            Byte.class, ParamAdder::addByte,
            LocalDate.class, ParamAdder::addLocalDate
    );

    public static void addParam(StringBuilder sql,
                                final Class<?> type) {
        TYPE.getOrDefault(type, ParamAdder::addDefault).accept(sql);
    }
}
