package lpz.utils.dao.postgresql;

import lpz.utils.dao.UpdateBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTest {
    private static final String BASE_SQL = "UPDATE public.test SET ";

    @Test
    void shouldAssembleBaseSQL() {
        final Update<TestEntity> update = new Update<>(TestEntity.class, null);

        assertNotNull(update);
        assertNotNull(update.getSQL());

        assertEquals(BASE_SQL, update.getSQL());
    }

    @Test
    void shouldAssembleSQLWithWhere() {
        final UUID id = UUID.randomUUID();
        final String expected = "WHERE id = ?::uuid AND field1 = ?::varchar ";

        final UpdateBuilder<TestEntity> update = new Update<>(TestEntity.class, null)
                .where("id").equal(id)
                .where("field1").equal("field1");

        assertEquals(BASE_SQL, ((Update<TestEntity>) update).getSQL());

        assertNotNull(((Update<TestEntity>) update).where);
        assertEquals(expected, ((Update<TestEntity>) update).where.toString());
    }
}
