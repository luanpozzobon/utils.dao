package lpz.utils.dao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation used to define join relationships between entities in a database context.
 * This annotation is applied to fields that represent joined entities and specifies
 * the target class and the join conditions.
 *
 * @since 2.0.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {
    /**
     * Specifies the target class to join with. If not specified, defaults to {@code void.class}.
     *
     * @return the {@code Class} object representing the target entity to join with
     * @since 2.0.0
     */
    Class<?> clazz() default void.class;

    /**
     * Defines the join conditions between the source and target entities.
     * Each {@code JoinOn} element represents a single join condition.
     *
     * @return an array of JoinOn conditions that specify how entities should be joined
     * @since 2.0.0
     * @see lpz.utils.dao.annotations.JoinOn
     */
    JoinOn[] on();
}
