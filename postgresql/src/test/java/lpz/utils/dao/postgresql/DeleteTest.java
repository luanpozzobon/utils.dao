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
}
