package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Setting {
    private String original_keyValue;
    private String key;
    private String value;
    private char status = 'n'; // u - updated, n - new, s - saved
    public static final String[] dbColumnNames = {"keyValue", "value"};
    public static final String[] columnNames = {"keyValue", "value"};
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Setting(String key, String value, char status) {
        this.key = key;
        this.value = value;
        this.status = status;
        this.original_keyValue = this.key;
    }

    public Setting() {
        key = "";
        value = "";
        status = 'n';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return key + "," + value;
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

    public String getOriginal_keyValue() {
        return original_keyValue;
    }

    public void setOriginal_keyValue(String original_keyValue) {
        this.original_keyValue = original_keyValue;
    }


}
