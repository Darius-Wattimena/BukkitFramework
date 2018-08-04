package nl.antimeta.bukkit.framework.database.model;

public enum FieldType {
    Boolean(1),
    Date,
    DateTime,
    Time,
    Char(255),
    Varchar(255),
    Text,
    SmallInt(5),
    Integer(10),
    BigInt(19),
    Decimal(38, 5);

    private int size = 0;
    private int digitsSize = 0;
    private boolean needSize = false;
    private boolean needDigitsSize = false;

    FieldType() {

    }

    FieldType(int size) {
        this.size = size;
        this.needSize = true;
    }

    FieldType(int size, int digitsSize) {
        this.size = size;
        this.digitsSize = digitsSize;
        this.needSize = true;
        this.needDigitsSize = true;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isSizeNeeded() {
        return needSize;
    }

    public boolean isDigitSizeNeeded() {
        return needDigitsSize;
    }

    public int getDigitsSize() {
        return digitsSize;
    }

    public void setDigitsSize(int digitsSize) {
        this.digitsSize = digitsSize;
    }
}
