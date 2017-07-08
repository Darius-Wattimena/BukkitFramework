package nl.antimeta.bukkit.framework.test;

import nl.antimeta.bukkit.framework.database.annotation.Entity;
import nl.antimeta.bukkit.framework.database.annotation.Field;
import nl.antimeta.bukkit.framework.database.model.BaseEntity;
import nl.antimeta.bukkit.framework.database.model.FieldType;

import java.sql.ResultSet;
import java.util.List;

@Entity(tableName = "test_entity")
public class TestEntity extends BaseEntity<TestEntity> {

    @Field(fieldName = "id", fieldType = FieldType.Integer, primary = true)
    private Integer id;

    @Field(fieldName = "name", fieldType = FieldType.Varchar)
    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<TestEntity> buildResultSet(ResultSet resultSet) {
        return null;
    }
}