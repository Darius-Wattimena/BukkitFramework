package nl.antimeta.bukkit.framework.database.model;

import java.lang.reflect.Method;

public class FieldConfig {
    private String fieldName;
    private boolean primary;
    private FieldType fieldType;
    private int size;
    private int digitSize;
    private Method get;
    private Method set;

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

    public Method getGet() {
        return get;
    }

    public void setGet(Method get) {
        this.get = get;
    }

    public Method getSet() {
        return set;
    }

    public void setSet(Method set) {
        this.set = set;
    }
}
