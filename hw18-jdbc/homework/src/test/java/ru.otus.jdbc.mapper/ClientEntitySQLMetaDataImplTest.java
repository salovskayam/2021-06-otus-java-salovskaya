package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Client;


class ClientEntitySQLMetaDataImplTest {

    private EntityClassMetaData<Client> entityClassMetaData;
    private EntitySQLMetaData entitySQLMetaData;

    @BeforeEach
    void setUp() {
        entityClassMetaData = new EntityClassMetaDataImpl<>(Client.class);
        entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
    }

    @Test
    void getSelectAllSql() {
        Assertions.assertEquals("select id, name from client",
                entitySQLMetaData.getSelectAllSql());
    }

    @Test
    void getSelectByIdSql() {
        Assertions.assertEquals("select name from client where id = ?",
            entitySQLMetaData.getSelectByIdSql());
    }

    @Test
    void getInsertSql() {
        Assertions.assertEquals("insert into client(name) values (?)",
                entitySQLMetaData.getInsertSql());
    }

    @Test
    void getUpdateSql() {
        Assertions.assertEquals("update client set name = ? where id = ?",
                entitySQLMetaData.getUpdateSql());
    }
}