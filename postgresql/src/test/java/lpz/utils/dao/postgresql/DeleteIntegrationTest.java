package lpz.utils.dao.postgresql;

import lpz.utils.dao.DeleteBuilder;
import lpz.utils.dao.Result;
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

class DeleteIntegrationTest {
    private static final UUID ID = UUID.randomUUID();

    private static Connection connection;

    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15.13")
                    .withDatabaseName("mvs-test")
                    .withUsername("test-user")
                    .withPassword("test-password")
                    .withInitScript("schema.sql");

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
    void shouldDelete() throws SQLException {
        TestEntity dbEntity = new CRUDBuilderFactory(connection).select(TestEntity.class)
                .where("id").equal(ID)
                .execute().entities().getFirst();

        Delete<TestEntity> delete = new Delete<>(TestEntity.class, connection);

        Result<TestEntity> result = assertDoesNotThrow(() -> delete.execute(dbEntity));

        assertNotNull(result);
        assertEquals(1, result.lines());

        Result<TestEntity> selected = new CRUDBuilderFactory(connection).select(TestEntity.class)
                .where("id").equal(ID)
                .execute();

        assertEquals(0, selected.lines());
    }

    @Test
    void shouldDeleteWithWhere() throws SQLException {
        DeleteBuilder<TestEntity> delete = new Delete<>(TestEntity.class, connection)
                .where("id").equal(ID);

        Result<TestEntity> result = assertDoesNotThrow(() -> delete.execute());

        assertNotNull(result);
        assertEquals(1, result.lines());

        Result<TestEntity> selected = new CRUDBuilderFactory(connection).select(TestEntity.class)
                .where("id").equal(ID)
                .execute();

        assertEquals(0, selected.lines());
    }
}
