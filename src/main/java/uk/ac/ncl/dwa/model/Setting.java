package uk.ac.ncl.dwa.model;

public class Setting {
    private String keyValue;
    private String value;
    public static final String[] dbColumnNames = {"keyValue", "value"};
    public static final String[] columnNames = {"keyValue", "value"};

    public Setting(String keyValue, String value) {
        this.keyValue = keyValue;
        this.value = value;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
