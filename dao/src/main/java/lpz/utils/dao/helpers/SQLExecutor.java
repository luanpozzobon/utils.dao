package lpz.utils.dao.helpers;

import lpz.utils.dao.Result;
import lpz.utils.dao.annotations.Join;
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

    private static <T> T createObject(final List<String> primaryKeys,
                                      final Map<List<Object>, T> cache,
                                      final ResultSet resultSet,
                                      final Class<T> clazz,
                                      final boolean useQualifiedName
    ) throws NoSuchMethodException, InstantiationException, SQLException {
        // TODO - Verify if entity exists in cache!
        List<Object> primaryKeyValues = new ArrayList<>();
        for (String primaryKey : primaryKeys) {

            Object value = ParamGetter.getDefault(resultSet, primaryKey);
            primaryKeyValues.add(value);
        }

        if (cache.containsKey(primaryKeyValues)) return cache.get(primaryKeyValues);

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
            if (useQualifiedName) {
                fieldName = Helper.getQualifiedName(clazz, field);
            }

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

        // TODO - Add entity to cache!
        cache.put(primaryKeyValues, entity);
        return entity;
    }

    private static void mapInnerValue(final ResultSet resultSet,
                                      final Object instance,
                                      final List<Field> innerFields) throws SQLException {
        for (Field innerField : innerFields) {
            String fieldName = Helper.getQualifiedName(instance.getClass(), innerField);

            Object value = TYPES.getOrDefault(
                    innerField.getType(),
                    ParamGetter::getDefault
            ).apply(resultSet, fieldName);

            try {
                innerField.set(instance, value);
            } catch (IllegalAccessException e) {
                logger.severe(e.getMessage());
            }
        }
    }

    private static <T> void mapSingleJoin(final Field joinField,
                                          final ResultSet resultSet,
                                          final T entity
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        Object instance = joinField.getType().getDeclaredConstructor().newInstance();

        final List<Field> innerFields = Arrays.stream(joinField.getType().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                        && field.trySetAccessible()
                ).toList();

        SQLExecutor.mapInnerValue(resultSet, instance, innerFields);

        try {
            joinField.set(entity, instance);
        } catch (IllegalAccessException e) {
            logger.severe(e.getMessage());
        }
    }

    private static <T> void mapMultiJoin(final Field joinField,
                                         final ResultSet resultSet,
                                         final T entity
    ) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, SQLException {
        Collection<Object> collection = (Collection<Object>) joinField.get(entity);
        if (collection == null) {
            collection = new LinkedHashSet<>();
        } else {
            collection = new LinkedHashSet<>(collection);
        }

        // TODO - Criar objeto, validar se objeto já existe!
        Class<?> joinClass = JoinHelper.getJoinClass(joinField);

        Object joinEntity = joinClass.getDeclaredConstructor().newInstance();
        final List<Field> innerFields = Arrays.stream(joinClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                        && field.trySetAccessible()
                ).toList();

        SQLExecutor.mapInnerValue(resultSet, joinEntity, innerFields);

        List<Object> primaryKeyValues = Arrays.stream(joinClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                        && field.getAnnotation(lpz.utils.dao.annotations.Field.class).primaryKey()
                        && field.trySetAccessible()
                ).map(field -> {
                    try {
                        return field.get(joinEntity);
                    } catch (IllegalAccessException e) {
                        logger.severe(e.getMessage());
                        return null;
                    }
                }).toList();

        if (primaryKeyValues.stream().anyMatch(Objects::isNull)) {
            return;
        }

        collection.add(joinEntity);
        try {
            Collection<Object> coll = switch (joinField.getType().getSimpleName()) {
                case "Set" -> collection;
                case "List" -> new ArrayList<>(collection);
                default ->
                        throw new IllegalArgumentException("Invalid join type: " + joinField.getType().getSimpleName());
            };
            joinField.set(entity, coll);
        } catch (IllegalAccessException e) {
            logger.severe(e.getMessage());
        }
    }

    private static <T> void mapJoin(final ResultSet resultSet,
                                    final T entity,
                                    final List<Class<?>> joinClasses
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        final List<Field> joinFields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Join.class)
                        && joinClasses.contains(JoinHelper.getJoinClass(field))
                        && field.trySetAccessible()
                ).toList();

        for (Field joinField : joinFields) {
            if (Collection.class.isAssignableFrom(joinField.getType())) {
                SQLExecutor.mapMultiJoin(joinField, resultSet, entity);
            } else {
                SQLExecutor.mapSingleJoin(joinField, resultSet, entity);
            }
        }
    }

    public static <T> Result<T> executeQuery(final PreparedStatement preparedStatement,
                                             final Class<T> clazz,
                                             final List<Class<?>> joinClasses,
                                             final boolean useQualifiedName
    ) throws SQLException, NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
        try (ResultSet rs = preparedStatement.executeQuery()) {
            final Map<List<Object>, T> cache = new HashMap<>();
            final List<String> primaryKeys = new ArrayList<>();
            // TODO - Map primary keys into primaryKeys
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(lpz.utils.dao.annotations.Field.class)
                            && field.getAnnotation(lpz.utils.dao.annotations.Field.class).primaryKey()
                    ).forEach(field -> {
                        String fieldName = Helper.getFieldName(field);
                        if (useQualifiedName) {
                            fieldName = Helper.getQualifiedName(clazz, field);
                        }

                        primaryKeys.add(fieldName);
                    });

            while (rs.next()) {
                T entity = SQLExecutor.createObject(primaryKeys, cache, rs, clazz, useQualifiedName);
                SQLExecutor.mapJoin(rs, entity, joinClasses);
            }

            List<T> entities = new ArrayList<>(cache.values());
            return Result.of(entities);
        }
    }

    public static <T> Result<T> executeUpdate(PreparedStatement preparedStatement, Class<T> clazz) throws SQLException, NoSuchMethodException, InstantiationException {
        int lines = preparedStatement.executeUpdate();

        return Result.of(lines);
    }
}
