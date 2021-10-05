package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;


class ManagerEntitySQLMetaDataImplTest {

    private EntityClassMetaData<Manager> entityClassMetaData;
    private EntitySQLMetaData entitySQLMetaData;

    @BeforeEach
    void setUp() {
        entityClassMetaData = new EntityClassMetaDataImpl<>(Manager.class);
        entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
    }

    @Test
    void getSelectAllSql() {
        Assertions.assertEquals("select label, no, param1 from manager",
                entitySQLMetaData.getSelectAllSql());
    }

    @Test
    void getSelectByIdSql() {
        Assertions.assertEquals("select label, param1 from manager where no = ?",
            entitySQLMetaData.getSelectByIdSql());
    }

    @Test
    void getInsertSql() {
        Assertions.assertEquals("insert into manager(label, param1) values (?, ?)",
                entitySQLMetaData.getInsertSql());
    }

    @Test
    void getUpdateSql() {
        Assertions.assertEquals("update manager set label = ?, param1 = ? where no = ?",
                entitySQLMetaData.getUpdateSql());
    }
}