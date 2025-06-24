package lpz.utils.dao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define metadata for a field in a class. This annotation can be used to
 * specify various constraints and properties associated with a field, such as whether
 * it is a primary key, unique key, or its behavior during insert and update operations.
 *
 * @since 1.0.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
    /**
     * Specifies the name of the field. Can be used to explicitly define the column name
     * corresponding to the field in the database.
     *
     * @return the name of the field or column.
     * @since 1.0.0
     */
    String name() default "";

    /**
     * Indicates whether the field is a primary key in the associated database table.
     *
     * @return true if the field is a primary key; false otherwise
     * @since 1.0.0
     */
    boolean primaryKey() default false;

    /**
     * Indicates whether the field is a unique key in the associated database table.
     *
     * @return true if the field is defined as a unique key; false otherwise
     * @since 1.0.0
     */
    boolean uniqueKey() default false;

    /**
     * Determines whether the field can be included in SQL INSERT statements.
     *
     * @return true if the field is insertable; false otherwise. Defaults to true.
     * @since 1.0.0
     */
    boolean insertable() default true;

    /**
     * Determines whether the field can be included in SQL UPDATE statements.
     *
     * @return true if the field is updatable; false otherwise. Defaults to true.
     * @since 1.0.0
     */
    boolean updatable() default true;

    /**
     * Specifies the length constraint for the field. This can be used to define
     * the maximum allowed length for the field, such as in database schema definitions.
     *
     * @return the maximum allowed length for the field, defaulting to 0 if not specified
     * @since 1.0.0
     */
    int length() default 0;

    /**
     * Indicates whether the field can contain null values.
     *
     * @return true if the field allows null values; false otherwise. Defaults to true.
     * @since 1.0.0
     */
    boolean nullable() default true;
}