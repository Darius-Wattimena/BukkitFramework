package nl.antimeta.bukkit.framework.database.model;

import nl.antimeta.bukkit.framework.database.Dao;
import nl.antimeta.bukkit.framework.util.LogUtil;

import java.lang.reflect.Field;

public class FieldConfig<T> {
    private String fieldName;
    private boolean primary;
    private FieldType fieldType;
    private int size;
    private int digitSize;
    private Field field;
    private boolean foreign;
    private boolean foreignAutoLoad;
    private boolean foreignAutoSave;
    private Class<?> foreignClass;
    private Dao<?> foreignDao;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDigitSize() {
        return digitSize;
    }

    public void setDigitSize(int digitSize) {
        this.digitSize = digitSize;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getFieldValue(T entity) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(entity);
    }

    public void setFieldValue(T entity, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(entity, value);
    }

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }

    public Class<?> getForeignClass() {
        return foreignClass;
    }

    public void setForeignClass(Class<?> foreignClass) {
        this.foreignClass = foreignClass;
    }

    public Dao<?> getForeignDao() {
        return foreignDao;
    }

    public void setForeignDao(Dao<?> foreignDao) {
        this.foreignDao = foreignDao;
    }

    public boolean isForeignAutoLoad() {
        return foreignAutoLoad;
    }

    public void setForeignAutoLoad(boolean foreignAutoLoad) {
        this.foreignAutoLoad = foreignAutoLoad;
    }

    public boolean isForeignAutoSave() {
        return foreignAutoSave;
    }

    public void setForeignAutoSave(boolean foreignAutoSave) {
        this.foreignAutoSave = foreignAutoSave;
    }
}
