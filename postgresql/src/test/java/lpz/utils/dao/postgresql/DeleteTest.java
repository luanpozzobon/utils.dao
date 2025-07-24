package lpz.utils.dao.postgresql;

import lpz.utils.dao.DeleteBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeleteTest {
    private static final String BASE_SQL = "DELETE FROM public.test ";

    @Test
    void shouldAssembleBaseSQL() {
        final Delete<TestEntity> delete = new Delete<>(TestEntity.class, null);

        assertNotNull(delete);
        assertNotNull(delete.getSQL());

        assertEquals(BASE_SQL, delete.getSQL());
    }

    @Test
    void shouldAssembleSQLWithWhere() {
        final UUID id = UUID.randomUUID();
        final String expected = "WHERE id = ?::uuid ";

        final DeleteBuilder<TestEntity> delete = new Delete<>(TestEntity.class, null)
                .where("id").equal(id);

        assertEquals(BASE_SQL, ((Delete<TestEntity>) delete).getSQL());

        assertNotNull(((Delete<TestEntity>) delete).where);
        assertEquals(expected, ((Delete<TestEntity>) delete).where.toString());
    }

    @Test
    void shouldAssembleSQLWithMultipleWhere() {
        final String expected = "WHERE id = ?::uuid AND field1 = ?::varchar ";

        final DeleteBuilder<TestEntity> delete = new Delete<>(TestEntity.class, null)
                .where("id").equal(UUID.randomUUID())
                .where("field1").equal("test");

        assertEquals(BASE_SQL, ((Delete<TestEntity>) delete).getSQL());

        assertNotNull(((Delete<TestEntity>) delete).where);
        assertEquals(expected, ((Delete<TestEntity>) delete).where.toString());
    }

    @Test
    void shouldAssembleSQLWithWhereAnd() {
        final String expected = "WHERE id = ?::uuid AND field1 = ?::varchar ";

        final DeleteBuilder<TestEntity> delete = new Delete<>(TestEntity.class, null)
                .where("id").equal(UUID.randomUUID())
                .and("field1").equal("test");

        assertEquals(BASE_SQL, ((Delete<TestEntity>) delete).getSQL());

        assertNotNull(((Delete<TestEntity>) delete).where);
        assertEquals(expected, ((Delete<TestEntity>) delete).where.toString());
    }

    @Test
    void shouldAssembleSQLWithWhereOr() {
        final String expected = "WHERE id = ?::uuid OR field1 = ?::varchar ";

        final DeleteBuilder<TestEntity> delete = new Delete<>(TestEntity.class, null)
                .where("id").equal(UUID.randomUUID())
                .or("field1").equal("test");

        assertEquals(BASE_SQL, ((Delete<TestEntity>) delete).getSQL());

        assertNotNull(((Delete<TestEntity>) delete).where);
        assertEquals(expected, ((Delete<TestEntity>) delete).where.toString());
    }
}
