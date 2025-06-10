package uk.ac.ncl.dwa.database;

/**
 * @author Jannetta Steyn
 *
 */
public class DBColumnDesc {
    public enum ColumnType {
        Text,
        Integer,
        Float,
    };

    public ColumnType columnType;
    public String columnName;
    public int textLength;

    private DBColumnDesc(ColumnType columnType, String columnName, int textLength) {
        this.columnType = columnType;
        this.columnName = columnName;
        this.textLength = textLength;
    }

    public static DBColumnDesc createTextColumn(String columnName, int textLength) {
        return new DBColumnDesc(ColumnType.Text, columnName, textLength);
    }

    public static DBColumnDesc createIntegerColumn(String columnName) {
        return new DBColumnDesc(ColumnType.Integer, columnName, 0);
    }
}
