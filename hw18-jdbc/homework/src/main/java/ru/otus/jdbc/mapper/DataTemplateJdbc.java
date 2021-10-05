package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntityClassMetaData<T> entityClassMetaData, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                T t = entityClassMetaData.getConstructor().newInstance();
                Map<String, Method> methodsByFieldName = getSetOrGetMethods(t, "set");

                methodsByFieldName.get(entityClassMetaData.getIdField().getName()).invoke(t, id);

                if (rs.next()) {
                    for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                        methodsByFieldName.get(field.getName()).invoke(t, rs.getObject(field.getName()));
                    }
                    return t;
                }

                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            T t;
            List<T> clientList = new ArrayList<>();

            try {
                while (rs.next()) {
                    t = entityClassMetaData.getConstructor().newInstance();

                    Map<String, Method> methodsByFieldName = getSetOrGetMethods(t, "set");

                    try {
                        for (Field field : entityClassMetaData.getAllFields()) {
                            methodsByFieldName.get(field.getName()).invoke(t, rs.getObject(field.getName()));
                        }
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }

                    clientList.add(t);
                }

                return clientList;

            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> params = new ArrayList<>();

        try {
            Map<String, Method> methodsByFieldName = getSetOrGetMethods(client, "get");

            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                params.add(methodsByFieldName.get(field.getName()).invoke(client));
            }

            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> params = new ArrayList<>();

        try {
            Map<String, Method> methodsByFieldName = getSetOrGetMethods(client, "get");

            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                    params.add(methodsByFieldName.get(field.getName()).invoke(client));
            }

            params.add(methodsByFieldName.get(entityClassMetaData.getIdField().getName()).invoke(client));

            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                    params
            );

        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Map<String, Method> getSetOrGetMethods(T t, String pattern) {
        return Arrays.stream(t.getClass().getDeclaredMethods())
                .filter(s -> s.getName().startsWith(pattern))
                .collect(Collectors.toUnmodifiableMap(s -> s.getName().substring(3).toLowerCase(),
                        Function.identity()));
    }

}
