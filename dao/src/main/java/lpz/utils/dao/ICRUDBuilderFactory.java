package lpz.utils.dao;

import lpz.utils.dao.annotations.Table;
import lpz.utils.dao.exceptions.MissingAnnotationException;

/**
 * ICRUDBuilderFactory defines a contract for creating builder instances
 * that correspond to the CRUD (Create, Read, Update, Delete) operations.
 * It facilitates the construction and execution of database operations
 * with type-safe builders.
 *
 * @since 1.0.0
 */
public interface ICRUDBuilderFactory {
    /**
     * Creates an {@link InsertBuilder} instance for the specified class type.
     * The generated builder facilitates the construction of insert operations
     * for the provided entity class.
     *
     * @param <T> the type of the class for which the insert operation is being created
     * @param clazz the class type representing the entity for which the insert builder is to be created
     * @return an {@link InsertBuilder} instance to facilitate insert operations for the specified class type
     * @throws NullPointerException if the provided {@code clazz} is null
     * @throws MissingAnnotationException if the provided class type lacks the required annotation {@link Table}
     */
    <T> InsertBuilder<T> insert(Class<T> clazz) throws NullPointerException, MissingAnnotationException;

    /**
     * Creates a {@link SelectBuilder} instance for the specified class type.
     * The generated builder facilitates the construction of select operations
     * for the provided entity class.
     *
     * @param <T> the type of the class for which the select operation is being created
     * @param clazz the class type representing the entity for which the select builder is to be created
     * @return a {@link SelectBuilder} instance to facilitate select operations for the specified class type
     * @throws NullPointerException if the provided {@code clazz} is null
     * @throws MissingAnnotationException if the provided class type lacks the required annotation {@link Table}
     */
    <T> SelectBuilder<T> select(Class<T> clazz) throws NullPointerException, MissingAnnotationException;

    /**
     * Creates an {@link UpdateBuilder} instance for the specified class type.
     * The generated builder facilitates the construction of update operations
     * for the provided entity class.
     *
     * @param <T> the type of the class for which the update operation is being created
     * @param clazz the class type representing the entity for which the update builder is to be created
     * @return an {@link UpdateBuilder} instance to facilitate update operations for the specified class type
     * @throws NullPointerException if the provided {@code clazz} is null
     * @throws MissingAnnotationException if the provided class type lacks the required annotation {@link Table}
     */
    <T> UpdateBuilder<T> update(Class<T> clazz) throws NullPointerException, MissingAnnotationException;

    /**
     * Creates a {@link DeleteBuilder} instance for the specified class type.
     * The generated builder facilitates the construction of delete operations
     * for the provided entity class.
     *
     * @param <T> the type of the class for which the delete operation is being created
     * @param clazz the class type representing the entity for which the delete builder is to be created
     * @return a {@link DeleteBuilder} instance to facilitate delete operations for the specified class type
     * @throws NullPointerException if the provided {@code clazz} is null
     * @throws MissingAnnotationException if the provided class type lacks the required annotation {@link Table}
     */
    <T> DeleteBuilder<T> delete(Class<T> clazz) throws NullPointerException, MissingAnnotationException;
}
