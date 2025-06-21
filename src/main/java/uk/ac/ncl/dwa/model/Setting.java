package uk.ac.ncl.dwa.model;

public class Setting {
    private String keyValue;
    private String value;
    private char status = 'n'; // u - updated, n - new, s - saved
    public static final String[] dbColumnNames = {"keyValue", "value"};
    public static final String[] columnNames = {"keyValue", "value"};

    public Setting(String keyValue, String value, char status) {
        this.keyValue = keyValue;
        this.value = value;
        this.status = status;
    }

    public Setting() {
        keyValue = "";
        value = "";
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

    public String toString() {
        return keyValue + "," + value;
    }

    /**
     * Return record status, u - update, n - new, s - saved
     * @return record status
     */
    public char getStatus() {
        return status;
    }

    /**
     * Set record status,
     * @param status u - update, n - new, s - saved
     */
    public void setStatus(char status) {
        this.status = status;
    }
}
