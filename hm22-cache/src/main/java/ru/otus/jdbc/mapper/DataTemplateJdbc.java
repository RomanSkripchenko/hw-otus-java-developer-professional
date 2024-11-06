package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String sql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.execQuery(connection, sql, List.of(id), resultSet -> {
            if (resultSet.next()) {
                return createEntity(resultSet);
            }
            return null;
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        String sql = entitySQLMetaData.getSelectAllSql();
        return dbExecutor.execQuery(connection, sql, List.of(), resultSet -> {
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(createEntity(resultSet));
            }
            return results;
        });
    }

    @Override
    public long insert(Connection connection, T client) {
        String sql = entitySQLMetaData.getInsertSql();
        return dbExecutor.execInsert(connection, sql, getFieldValues(client));
    }

    @Override
    public void update(Connection connection, T client) {
        String sql = entitySQLMetaData.getUpdateSql();
        dbExecutor.execUpdate(connection, sql, getFieldValues(client));
    }

    private T createEntity(ResultSet resultSet) {
        try {
            T instance = entityClassMetaData.getConstructor().newInstance();
            for (Field field : entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                field.set(instance, resultSet.getObject(field.getName()));
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create entity", e);
        }
    }

    private List<Object> getFieldValues(T client) {
        List<Object> values = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                values.add(field.get(client));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to get field values", e);
            }
        }
        Field idField = entityClassMetaData.getIdField();
        idField.setAccessible(true);
        try {
            values.add(idField.get(client));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get ID field value", e);
        }
        return values;
    }
}
