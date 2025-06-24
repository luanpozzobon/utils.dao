package lpz.utils.dao.helpers;

import lpz.utils.dao.Result;
import lpz.utils.dao.exceptions.EntityConstructorException;
import lpz.utils.dao.helpers.consumer.SQLResult;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public abstract class SQLExecutor {
    private static final Logger logger = Logger.getGlobal();

    private static final Map<Class<?>, SQLResult<?>> TYPES = Map.of(
            String.class, ParamGetter::getString,
            UUID.class, ParamGetter::getUUID,
            Integer.class, ParamGetter::getInteger,
            Long.class, ParamGetter::getLong,
            Float.class, ParamGetter::getFloat,
            Double.class, ParamGetter::getDouble,
            Boolean.class, ParamGetter::getBoolean,
            BigDecimal.class, ParamGetter::getBigDecimal,
            Byte.class, ParamGetter::getByte,
            LocalDate.class, ParamGetter::getLocalDate
    );

    private static <T> T createObject(ResultSet resultSet, Class<T> clazz) throws NoSuchMethodException, InstantiationException, SQLException {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(
                        field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                                 && field.trySetAccessible()
                ).toList();

        T entity;
        try {
            entity = clazz.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EntityConstructorException("Your entity doesn't have a Non-Args constructor, or it threw an exception!", e);
        }
        for (var field : fields) {
            String fieldName = Helper.getFieldName(field);
            Object value = TYPES.getOrDefault(
                    field.getType(),
                    ParamGetter::getDefault
            ).apply(resultSet, fieldName);

            try {
                field.set(entity, value);
            } catch (IllegalAccessException e) {
                logger.severe(e.getMessage());
            }
        }

        return entity;
    }

    public static <T> Result<T> executeQuery(PreparedStatement preparedStatement, Class<T> clazz) throws SQLException, NoSuchMethodException, InstantiationException {
        ResultSet rs = preparedStatement.executeQuery();

        List<T> entities = new ArrayList<>();
        while (rs.next()) {
            T entity = SQLExecutor.createObject(rs, clazz);
            entities.add(entity);
        }

        return Result.of(entities);
    }

    public static <T> Result<T> executeUpdate(PreparedStatement preparedStatement, Class<T> clazz) throws SQLException, NoSuchMethodException, InstantiationException {
        int lines =  preparedStatement.executeUpdate();

        return Result.of(lines);
    }
}
