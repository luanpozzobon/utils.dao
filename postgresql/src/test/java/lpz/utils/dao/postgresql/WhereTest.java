package lpz.utils.dao.postgresql;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WhereTest {

    @Test
    void equal() {
        final UUID id = UUID.randomUUID();
        Where<Select<TestEntity>> where = new Where<>("id", new Select<>(TestEntity.class, null));

        final String expected = "WHERE id = ?::uuid ";

        Select<TestEntity> op = assertDoesNotThrow(() -> where.equal(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }
}
