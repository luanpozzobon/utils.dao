package lpz.utils.dao.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation used to specify join conditions between entities in a database context.
 * This annotation defines the mapping between local and foreign fields when performing
 * joins between database tables or entities.
 *
 * @since 2.0.0
 * @see lpz.utils.dao.annotations.Join
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinOn {
    /**
     * Specifies the field name in the local (source) entity that will be used for joining.
     *
     * @return the name of the field in the local entity
     * @since 2.0.0
     */
    String localField();

    /**
     * Specifies the field name in the foreign (target) entity that will be used for joining.
     *
     * @return the name of the field in the foreign entity
     * @since 2.0.0
     */
    String foreignField();
}
