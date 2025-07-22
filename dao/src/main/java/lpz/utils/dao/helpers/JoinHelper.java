package lpz.utils.dao.helpers;

import lpz.utils.dao.annotations.Join;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public abstract class JoinHelper {

    public static Class<?> getJoinClass(Field field) {
        Class<?> joinClass;
        if (!field.getAnnotation(Join.class).clazz().equals(void.class)) {
            joinClass = field.getAnnotation(Join.class).clazz();
        } else if (Collection.class.isAssignableFrom(field.getType())) {
            joinClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        } else {
            joinClass = field.getType();
        }

        return joinClass;
    }
}
