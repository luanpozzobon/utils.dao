package lpz.utils.dao.helpers;

import lpz.utils.dao.annotations.Table;

import java.lang.reflect.Field;

public abstract class Helper {
    public static String normalizeSQLName(final String name) {
        return name.replaceAll("([A-Z])", "_$1").replaceFirst("^_", "").toLowerCase();
    }

    public static <T> String getTableName(final Class<T> clazz) {
        String schema = clazz.getAnnotation(Table.class).schema();
        String name = clazz.getAnnotation(Table.class).name();
        if (name.isBlank())
            name = normalizeSQLName(clazz.getSimpleName());

        return schema.isEmpty() ? name : schema + "." + name;
    }

    public static String getFieldName(final Field field) {
        String name = field.getAnnotation(lpz.utils.dao.annotations.Field.class).name();
        if (name.isBlank())
            name = normalizeSQLName(field.getName());

        return name;
    }

    public static void replaceFromLast(final String target, final String replacement, final StringBuilder sql) {
        sql.replace(sql.lastIndexOf(target), sql.length(), replacement);
    }

    public static <T> String getQualifiedName(final Class<T> clazz,
                                              final Field field) {
        return getTableName(clazz) + "." + getFieldName(field);
    }
}