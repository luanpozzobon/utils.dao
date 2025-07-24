package lpz.utils.dao.postgresql;

import lpz.utils.dao.SelectBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SelectTest {
    private static final String BASE_SQL =
            "SELECT public.test.id AS \"public.test.id\", public.test.field1 AS \"public.test.field1\", " +
                    "public.test.field2 AS \"public.test.field2\", public.test.field3 AS \"public.test.field3\", " +
                    "public.test.field4 AS \"public.test.field4\", public.test.field5 AS \"public.test.field5\", " +
                    "public.test.field6 AS \"public.test.field6\", public.test.field7 AS \"public.test.field7\", " +
                    "public.test.field8 AS \"public.test.field8\", public.test.field9 AS \"public.test.field9\", " +
                    "public.test.entity_id AS \"public.test.entity_id\", ";

    @Test
    void shouldAssembleBaseSQL() {
        final Select<TestEntity> select = new Select<>(TestEntity.class, null);

        assertNotNull(select);
        assertNotNull(select.getSQL());

        assertEquals(BASE_SQL, select.getSQL());
    }

    @Test
    void shouldAssembleSQLWithWhere() {
        final String expected = "WHERE id = ?::uuid ";

        final SelectBuilder<TestEntity> select = new Select<>(TestEntity.class, null)
                .where("id").equal(UUID.randomUUID());

        assertEquals(BASE_SQL, ((Select<TestEntity>) select).getSQL());

        assertNotNull(((Select<TestEntity>) select).where);
        assertEquals(expected, ((Select<TestEntity>) select).where.toString());
    }

    @Test
    void shouldAssembleSQLWithMultipleWhere() {
        final String expected = "WHERE id = ?::uuid AND field1 = ?::varchar ";

        final SelectBuilder<TestEntity> select = new Select<>(TestEntity.class, null)
                .where("id").equal(UUID.randomUUID())
                .where("field1").equal("test");

        assertEquals(BASE_SQL, ((Select<TestEntity>) select).getSQL());

        assertNotNull(((Select<TestEntity>) select).where);
        assertEquals(expected, ((Select<TestEntity>) select).where.toString());
    }

    @Test
    void shouldAssembleSQLWithWhereAnd() {
        final String expected = "WHERE id = ?::uuid AND field1 = ?::varchar ";

        final SelectBuilder<TestEntity> select = new Select<>(TestEntity.class, null)
                .where("id").equal(UUID.randomUUID())
                .and("field1").equal("test");

        assertEquals(BASE_SQL, ((Select<TestEntity>) select).getSQL());

        assertNotNull(((Select<TestEntity>) select).where);
        assertEquals(expected, ((Select<TestEntity>) select).where.toString());
    }

    @Test
    void shouldAssembleSQLWithWhereOr() {
        final String expected = "WHERE id = ?::uuid OR field1 = ?::varchar ";

        final SelectBuilder<TestEntity> select = new Select<>(TestEntity.class, null)
                .where("id").equal(UUID.randomUUID())
                .or("field1").equal("test");

        assertEquals(BASE_SQL, ((Select<TestEntity>) select).getSQL());

        assertNotNull(((Select<TestEntity>) select).where);
        assertEquals(expected, ((Select<TestEntity>) select).where.toString());
    }

    @Test
    void shouldAssembleSQLWithLimit() {
        final int limit = 20;

        final Select<TestEntity> select = new Select<>(TestEntity.class, null)
                .limit(limit);

        assertEquals(BASE_SQL, select.getSQL());

        assertNotNull(select.limit);
        assertEquals(limit, select.limit);
    }

    @Test
    void shouldAssembleSQLLimitless() {
        final int noLimit = 0;

        final Select<TestEntity> select = new Select<>(TestEntity.class, null)
                .limitless();

        assertEquals(BASE_SQL, select.getSQL());

        assertNotNull(select.limit);
        assertEquals(noLimit, select.limit);

        assertNull(select.offset);
    }

    @Test
    void shouldAssembleSQLWithOffset() {
        final int offset = 10;

        final Select<TestEntity> select = new Select<>(TestEntity.class, null)
                .offset(offset);

        assertEquals(BASE_SQL, select.getSQL());

        assertNotNull(select.offset);
        assertEquals(offset, select.offset);

        assertNull(select.limit);
    }

    @Test
    void shouldAssembleSQLWithPageAndPageSize() {
        final int page = 2;
        final int pageSize = 20;
        final int offset = (page - 1) * pageSize;

        final Select<TestEntity> select = new Select<>(TestEntity.class, null)
                .page(page, pageSize);

        assertEquals(BASE_SQL, select.getSQL());

        assertNotNull(select.offset);
        assertEquals(offset, select.offset);

        assertNotNull(select.limit);
        assertEquals(pageSize, select.limit);
    }

    @Test
    void shouldAssembleSQLWithPageAndDefaultPageSize() {
        final int page = 2;
        final int defaultLimit = 50;
        final int offset = (page - 1) * defaultLimit;

        final Select<TestEntity> select = new Select<>(TestEntity.class, null)
                .page(page);

        assertEquals(BASE_SQL, select.getSQL());

        assertNotNull(select.offset);
        assertEquals(offset, select.offset);

        assertNotNull(select.limit);
        assertEquals(defaultLimit, select.limit);
    }

    @Test
    void shouldAssembleSQLWithLimitAndPage() {
        final int limit = 20;
        final int page = 2;
        final int offset = (page - 1) * limit;

        final Select<TestEntity> select = new Select<>(TestEntity.class, null)
                .limit(limit)
                .page(page);

        assertEquals(BASE_SQL, select.getSQL());

        assertNotNull(select.offset);
        assertEquals(offset, select.offset);

        assertNotNull(select.limit);
        assertEquals(limit, select.limit);
    }
}
