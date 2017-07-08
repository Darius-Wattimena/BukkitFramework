package nl.antimeta.bukkit.framework.database.model;

import java.lang.reflect.Field;

public class FieldConfig<T> {
    private String fieldName;
    private boolean primary;
    private FieldType fieldType;
    private int size;
    private int digitSize;
    private Field field;

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
}
