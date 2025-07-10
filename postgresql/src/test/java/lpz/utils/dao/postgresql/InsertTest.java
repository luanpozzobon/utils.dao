package lpz.utils.dao.postgresql;

import lpz.utils.dao.InsertBuilder;
import lpz.utils.dao.Result;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class InsertTest {
    private static final UUID ID = UUID.randomUUID();
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
    void shouldInsertEntity() throws SQLException {
        TestEntity entity = new TestEntity(ID, FIELD1, FIELD2, FIELD3, FIELD4, FIELD5, FIELD6, FIELD7, FIELD8, FIELD9);
        InsertBuilder<TestEntity> insert = new CRUDBuilderFactory(connection)
                .insert(TestEntity.class)
                .returning();

        Result<TestEntity> result = assertDoesNotThrow(() -> insert.execute(entity));

        assertNotNull(result);
        assertEquals(1, result.lines());

        TestEntity savedEntity = result.entities().getFirst();

        assertNotNull(savedEntity);
        assertEquals(ID, savedEntity.getId());
        assertEquals(FIELD1, savedEntity.getField1());
        assertEquals(FIELD2, savedEntity.getField2());
        assertEquals(FIELD3, savedEntity.getField3());
        assertEquals(FIELD4, savedEntity.getField4());
        assertEquals(FIELD5, savedEntity.getField5());
        assertEquals(FIELD6, savedEntity.getField6());
        assertEquals(FIELD7, savedEntity.getField7());
        assertEquals(FIELD8, savedEntity.getField8());
        assertEquals(FIELD9, savedEntity.getField9());
    }
}
