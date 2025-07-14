package lpz.utils.dao.postgresql;

import lpz.utils.dao.Result;
import lpz.utils.dao.SelectBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SelectIntegrationTest {
    private static final String FIELD1 = "field1";
    private static final Integer FIELD2 = 1;
    private static final Long FIELD3 = 1L;
    private static final Float FIELD4 = 1.0F;
    private static final Double FIELD5 = 1.0D;
    private static final Boolean FIELD6 = true;
    private static final BigDecimal FIELD7 = new BigDecimal(10);
    private static final Byte FIELD8 = 1;
    private static final LocalDate FIELD9 = LocalDate.now();

    private static Connection connection;

    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15.13")
                    .withDatabaseName("mvs-test")
                    .withUsername("test-user")
                    .withPassword("test-password")
                    .withInitScript("schema.sql");

    private static UUID id = UUID.randomUUID();

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();

        System.setProperty("db.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("db.username", postgreSQLContainer.getUsername());
        System.setProperty("db.password", postgreSQLContainer.getPassword());

        String jdbcUrl = System.getProperty("db.url");
        String username = System.getProperty("db.username");
        String password = System.getProperty("db.password");

        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            for (int i = 0; i < 100; i++) {
                final UUID id = UUID.randomUUID();
                final TestEntity e = new TestEntity(id, FIELD1, FIELD2, FIELD3, FIELD4, FIELD5, FIELD6, FIELD7, FIELD8, FIELD9);

                new CRUDBuilderFactory(connection).insert(TestEntity.class)
                        .execute(e);
            }

            final String sql = "INSERT INTO test (id) VALUES (?)";
            final java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Test
    void shouldSelectById() {
        SelectBuilder<TestEntity> select = new Select<>(TestEntity.class, connection)
                .where("id").equal(id);

        Result<TestEntity> result = assertDoesNotThrow(select::execute);

        assertNotNull(result);
        assertEquals(1, result.lines());
        assertEquals(id, result.entities().getFirst().getId());
    }

    @Test
    void shouldSelect50Entities() {
        Select<TestEntity> select = new Select<>(TestEntity.class, connection);

        Result<TestEntity> result = assertDoesNotThrow(select::execute);

        assertNotNull(result);
        assertEquals(50, result.lines());
    }

    @Test
    void shouldSelectWithLimit() {
        Select<TestEntity> select = new Select<>(TestEntity.class, connection)
                .limit(20);

        Result<TestEntity> result = assertDoesNotThrow(select::execute);

        assertNotNull(result);
        assertEquals(20, result.lines());
    }

    @Test
    void shouldSelectLimitless() {
        Select<TestEntity> select = new Select<>(TestEntity.class, connection)
                .limitless();

        Result<TestEntity> result = assertDoesNotThrow(select::execute);

        assertNotNull(result);
        assertEquals(101, result.lines());
    }

    @Test
    void shouldSelectWithOffset() {
        Select<TestEntity> select = new Select<>(TestEntity.class, connection)
                .offset(20);

        Result<TestEntity> result = assertDoesNotThrow(select::execute);

        assertNotNull(result);
        assertEquals(50, result.lines());
    }

    @Test
    void shouldSelectLimitlessWithOffset() {
        Select<TestEntity> select = new Select<>(TestEntity.class, connection)
                .limitless()
                .offset(20);

        Result<TestEntity> result = assertDoesNotThrow(select::execute);

        assertNotNull(result);
        assertEquals(81, result.lines());
    }

    @Test
    void shouldSelectWithPageAndPageSize() {
        Select<TestEntity> select = new Select<>(TestEntity.class, connection)
                .page(2, 20);

        Result<TestEntity> result = assertDoesNotThrow(select::execute);

        assertNotNull(result);
        assertEquals(20, result.lines());
    }

    @Test
    void shouldSelectWithPageAndDefaultPageSize() {
        Select<TestEntity> select = new Select<>(TestEntity.class, connection)
                .page(2);

        Result<TestEntity> result = assertDoesNotThrow(select::execute);

        assertNotNull(result);
        assertEquals(50, result.lines());
    }
}
