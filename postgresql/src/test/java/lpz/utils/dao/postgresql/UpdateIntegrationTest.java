package lpz.utils.dao.postgresql;

import lpz.utils.dao.Result;
import lpz.utils.dao.UpdateBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateIntegrationTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String FIELD1 = "field1";

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
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @BeforeEach
    void setUp() {
        try {
            final String sql = "DELETE FROM test";
            final java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();

            final TestEntity e = new TestEntity();
            e.setId(ID);

            new CRUDBuilderFactory(connection).insert(TestEntity.class)
                    .execute(e);
        } catch (Exception e) {
            System.err.println("Error deleting data from database: " + e.getMessage());
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
    void shouldUpdateEntity() throws SQLException {
        final TestEntity dbEntity = new CRUDBuilderFactory(connection).select(TestEntity.class)
                .where("id").equal(ID)
                .execute().entities().getFirst();
        dbEntity.setField1(FIELD1);

        final Update<TestEntity> update = new Update<>(TestEntity.class, connection);

        Result<TestEntity> result = assertDoesNotThrow(() -> update.execute(dbEntity));

        assertNotNull(result);
        assertEquals(1, result.lines());

        final TestEntity updated = new CRUDBuilderFactory(connection).select(TestEntity.class)
                .where("id").equal(ID)
                .execute().entities().getFirst();

        assertEquals(FIELD1, updated.getField1());
    }

    @Test
    void shouldUpdateWithWhere() throws SQLException {
        final TestEntity dbEntity = new CRUDBuilderFactory(connection).select(TestEntity.class)
                .where("id").equal(ID)
                .execute().entities().getFirst();
        dbEntity.setField1(FIELD1);

        final UpdateBuilder<TestEntity> update = new Update<>(TestEntity.class, connection)
                .where("id").equal(ID);

        Result<TestEntity> result = assertDoesNotThrow(() -> update.execute(dbEntity));

        assertNotNull(result);
        assertEquals(1, result.lines());

        final TestEntity updated = new CRUDBuilderFactory(connection).select(TestEntity.class)
                .where("id").equal(ID)
                .execute().entities().getFirst();

        assertEquals(FIELD1, updated.getField1());
    }
}
