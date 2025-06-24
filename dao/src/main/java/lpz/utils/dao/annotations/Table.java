package lpz.utils.dao.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define metadata for a database table. This annotation can be applied
 * to classes to specify details such as the table's name and schema in the database.
 * It helps in mapping a class to a corresponding table in the database schema.
 *
 * @since 1.0.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * Specifies the name of the table. Can be used to explicitly define the table name
     * in the database schema.
     *
     * @return the name of the table.
     * @since 1.0.0
     */
    String name() default "";

    /**
     * Specifies the schema of the table in the database. This can be used to define
     * the schema name explicitly, allowing the separation of database objects into
     * distinct namespaces.
     *
     * @return the name of the schema.
     * @since 1.0.0
     */
    String schema() default "public";
}
