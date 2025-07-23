# utils.dao

A Java library, designed to simplify your database CRUD, using a Fluent API!

[![Static Badge](https://img.shields.io/badge/license-MIT-white?style=for-the-badge&labelColor=black)](./LICENSE)

---

This is a library to abstract common database queries and operations. The main goal is to ease the devlopment, allowing the focus to remain on the business rules, and not on the data access.

![Library Comparation](./assets/compare.jpeg)

---

## Features

- **Fluent API**: Build the queries elegantly and simple.
- **Object Relational Mapping (ORM)**:  Map your entities to your database using the annotations `@Table` and `@Field`.
- **CRUD**: Build your CRUD operations easily.
- **Pagination**: Supports pagination through `LIMIT` and `OFFSET`.
- **JOIN**: Suppports JOIN operations using `LEFT` and `INNER`.

## How to Use

This section will show how to use the library, including small snippets of code for each situation.

### Entities
If no value is specified within the annotation, a default naming convention will be adopted.

```java
@Table()
public class MyEntity { // table name = public.my_entity
    @Field(primaryKey = true)
    private int id; // field name = id

    @Field
    private String myFirstField; // field name = my_field_name

    @Field(name = "other_entity_id")
    private int entityId; // field name = other_entity_id
}
```

```java
@Table(name = "my_second_entity")
public class Entity { // table name = public.my_second_entity
    @Field(primaryKey = true)
    private int id;

    @Join(
        on = @JoinOn(localField = "id", foreignField = "other_entity_id")
    ) // Relation: 1:N
    private List<MyEntity> myEntityList; // join public.my_entity ON other_entity_id = public.my_second_entity.id

    // You can also specify the class to JOIN
    @Join(
        clazz = MyEntity.class,
        on = @JoinOn(localField = "id", foreignField = "other_entity_id")
    ) // Relation: N:1
    private OtherEntity otherEntity; // join public.my_entity ON other_entity_id = public.my_second_entity.id
}
```

### Insert
```java
Result<Entity> result = 
    new CRUDBuilderFactory(connection).insert(Entity.class)
        .returning()
        .execute(entity);
```

### Select
```java
Result<Entity> result = 
    new CRUDBuilderFactory(connection).select(Entity.class)
        .where("id").equals(1)
        .and("my_entity.id").in(List.of(1, 2, 3))
        .leftJoin()
        .page(2);
```

### Update
```java
Result<Entity> result =
    new CRUDBuilderFactory(connection).update(Entity.class)
        .execute(entity);
```

### Delete
```java
Result<Entity> result =
    new CRUDBuilderFactory(connection).delete(Entity.class)
        .execute(entity);
```

## Technologies

- [Java 21](https://www.java.com/en/)
- [Maven](https://maven.apache.org) (Build and Dependency Management)
- [SonarQube](https://www.sonarsource.com/products/sonarqube/)

## Databases

Currently the library is designed to support the following databases:

- [PostgreSQL](https://www.postgresql.org)

## Quality Assurance

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=luanpozzobon_utils.dao&metric=bugs)](https://sonarcloud.io/summary/new_code?id=luanpozzobon_utils.dao)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=luanpozzobon_utils.dao&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=luanpozzobon_utils.dao)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=luanpozzobon_utils.dao&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=luanpozzobon_utils.dao)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=luanpozzobon_utils.dao&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=luanpozzobon_utils.dao)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=luanpozzobon_utils.dao&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=luanpozzobon_utils.dao)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=luanpozzobon_utils.dao&metric=coverage)](https://sonarcloud.io/summary/new_code?id=luanpozzobon_utils.dao)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=luanpozzobon_utils.dao&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=luanpozzobon_utils.dao)

## Author

<table>
    <tr>
        <td align="center">
            <a href="http://github.com/luanpozzobon">
            <img src="https://avatars.githubusercontent.com/u/108753073?v=4" width="100px;" alt="GitHub photo of Luan Pozzobon"/><br>
            <sub>
                <b>luanpozzobon</b>
            </sub>
            </a>
        </td>
        <td align="center">
            <a href="https://www.linkedin.com/in/luanpozzobon/">
            <img src="https://media.licdn.com/dms/image/v2/D4D03AQFW0wMXnNIOZw/profile-displayphoto-shrink_800_800/profile-displayphoto-shrink_800_800/0/1711368855052?e=1756339200&v=beta&t=ECNa-G2AvvuhpHO1o4CVmZXcS7oykelAzm0lGHexS1g" width="100px;" alt="LinkedIn photo of Luan Pozzobon"/><br>
            <sub>
                <b>LinkedIn: Luan Pozzobon</b>
            </sub>
            </a>
        </td>
    </tr>
</table>