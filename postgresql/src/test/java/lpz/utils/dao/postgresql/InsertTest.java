package lpz.utils.dao.postgresql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsertTest {
    private static final String BASE_SQL = "INSERT INTO public.test (id, field1, field2, field3, field4, field5, " +
            "field6, field7, field8, field9) ";

    @Test
    void shouldAssembleBaseSQL() {
        final Insert<TestEntity> insert = new Insert<>(TestEntity.class, null);

        assertNotNull(insert);
        assertNotNull(insert.getSQL());

        assertEquals(BASE_SQL, insert.getSQL());
        assertFalse(insert.returning);
    }

    @Test
    void shouldAssembleSQLWithReturning() {
        final Insert<TestEntity> insert = new Insert<>(TestEntity.class, null)
                .returning();

        assertEquals(BASE_SQL, insert.getSQL());

        assertTrue(insert.returning);
    }
}
