package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        Field idField = entityClassMetaData.getIdField();
        return "SELECT * FROM " + entityClassMetaData.getName() + " WHERE " + idField.getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        String fields = String.join(", ", entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).toArray(String[]::new));
        String placeholders = String.join(", ", Collections.nCopies(entityClassMetaData.getFieldsWithoutId().size(), "?"));
        return "INSERT INTO " + entityClassMetaData.getName() + " (" + fields + ") VALUES (" + placeholders + ")";
    }

    @Override
    public String getUpdateSql() {
        Field idField = entityClassMetaData.getIdField();
        String setClause = String.join(", ", entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?").toArray(String[]::new));
        return "UPDATE " + entityClassMetaData.getName() + " SET " + setClause + " WHERE " + idField.getName() + " = ?";
    }
}
