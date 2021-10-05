package ru.otus.jdbc.mapper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.crm.model.Client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


@Testcontainers
class DataTemplateJdbcTest {

    private HikariDataSource dataSourcePool;
    private EntityClassMetaData<Client> entityClassMetaDataClient;
    private EntitySQLMetaData entitySQLMetaDataClient;
    private DataTemplateJdbc<Client> dataTemplateJdbc;

    private static final Logger logger = LoggerFactory.getLogger(DataTemplateJdbcTest.class);

    // will be started before and stopped after each test method
    @Container
    private final PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("testDataBase")
            .withUsername("owner")
            .withPassword("secret")
            .withClasspathResourceMapping("00_createTables.sql", "/docker-entrypoint-initdb.d/00_createTables.sql", BindMode.READ_ONLY)
            .withClasspathResourceMapping("01_insertData.sql", "/docker-entrypoint-initdb.d/01_insertData.sql", BindMode.READ_ONLY);

    private Properties getConnectionProperties() {
        Properties props = new Properties();
        props.setProperty("user", postgresqlContainer.getUsername());
        props.setProperty("password", postgresqlContainer.getPassword());
        props.setProperty("ssl", "false");
        return props;
    }

    void makeConnectionPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgresqlContainer.getJdbcUrl());
        config.setConnectionTimeout(3000); //ms
        config.setIdleTimeout(60000); //ms
        config.setMaxLifetime(600000);//ms
        config.setAutoCommit(false);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setPoolName("DemoHiPool");
        config.setRegisterMbeans(true);

        config.setDataSourceProperties(getConnectionProperties());

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSourcePool = new HikariDataSource(config);
    }

    private Connection getConnection() throws SQLException {
        return dataSourcePool.getConnection();
    }

    @BeforeEach
    void setUp() {
        entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        dataTemplateJdbc = new DataTemplateJdbc<>(new DbExecutorImpl(),
                entityClassMetaDataClient, entitySQLMetaDataClient);
    }

    @Test
    void findByIdTest1() throws SQLException {
        makeConnectionPool();

        try (Connection connection = getConnection()) {
            Optional<Client> client = dataTemplateJdbc.findById(connection, 1L);

            Assertions.assertEquals("name1", client.get().getName());
        }

        dataSourcePool.close();
    }

    @Test
    void findByIdTest2() throws SQLException {
        makeConnectionPool();

        try (Connection connection = getConnection()) {
            Optional<Client> client = dataTemplateJdbc.findById(connection, 10L);

            Assertions.assertTrue(client.isEmpty());
        }

        dataSourcePool.close();
    }

    @Test
    void findAllTest() throws SQLException {
        makeConnectionPool();

        try (Connection connection = getConnection()) {
            List<Client> clients = dataTemplateJdbc.findAll(connection);
            logger.info("clients: {}", clients);

            List<Client> clientExpected = Arrays.asList(new Client(1L, "name1"), new Client(2L, "name2"));
            Assertions.assertEquals(clientExpected.get(0).getId(), clients.get(0).getId());
            Assertions.assertEquals(clientExpected.get(1).getName(), clients.get(1).getName());
        }

        dataSourcePool.close();
    }

    @Test
    void insertTest() throws SQLException {
        makeConnectionPool();

        try (Connection connection = getConnection()) {
            Long id = dataTemplateJdbc.insert(connection, new Client("name3"));

            Assertions.assertEquals(3L, id);
        }

        dataSourcePool.close();
    }

    @Test
    void updateTest1() throws SQLException {
        makeConnectionPool();

        try (Connection connection = getConnection()) {
            dataTemplateJdbc.update(connection, new Client(2L,"name4"));

            Optional<Client> client = dataTemplateJdbc.findById(connection, 2L);

            Assertions.assertEquals("name4", client.get().getName());
        }

        dataSourcePool.close();
    }

    @Test
    void updateTest2() throws SQLException {
        makeConnectionPool();

        try (Connection connection = getConnection()) {

            Assertions.assertThrows(DataTemplateException.class, () -> {
                    dataTemplateJdbc.update(connection, new Client(10L,"name4"));
            });
        }

        dataSourcePool.close();
    }
}