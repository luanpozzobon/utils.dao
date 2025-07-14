package lpz.utils.dao.postgresql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WhereTest {
    private static final UUID id = UUID.randomUUID();

    private Where<Select<TestEntity>> where;

    @BeforeEach
    void setUp() {
        this.where = new Where<>("id", new Select<>(TestEntity.class, null));
    }

    @Test
    void equal() {
        final String expected = "WHERE id = ?::uuid ";

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.equal(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }

    @Test
    void notEqual() {
        final String expected = "WHERE id != ?::uuid ";

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.notEqual(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }

    @Test
    void greaterThan() {
        final String expected = "WHERE id > ?::uuid ";

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.greaterThan(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }

    @Test
    void lessThan() {
        final String expected = "WHERE id < ?::uuid ";

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.lessThan(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }

    @Test
    void greaterThanOrEqual() {
        final String expected = "WHERE id >= ?::uuid ";

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.greaterThanOrEqual(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }

    @Test
    void lessThanOrEqual() {
        final String expected = "WHERE id <= ?::uuid ";

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.lessThanOrEqual(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }

    @Test
    void like() {
        final String expected = "WHERE id LIKE '%' || ?::uuid || '%' ";

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.like(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }

    @Test
    void ilike() {
        final String expected = "WHERE id ILIKE '%' || ?::uuid || '%' ";

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.ilike(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }

    @Test
    void notLike() {
        final String expected = "WHERE id NOT LIKE '%' || ?::uuid || '%' ";

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.notLike(id));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(1, op.values.size());
        assertEquals(id, op.values.getFirst());
    }

    @Test
    void in() {
        final String expected = "WHERE id IN (?::uuid, ?::uuid, ?::uuid) ";
        final List<UUID> ids = List.of(id, id, id);

        Select<TestEntity> op = assertDoesNotThrow(() -> this.where.in(ids));

        assertNotNull(op);
        assertEquals(expected, op.where.toString());
        assertEquals(3, op.values.size());
        assertEquals(id, op.values.getFirst());
    }
}
