package uk.ac.ncl.dwa.database;

import java.util.List;

public abstract class DBHandler {
    protected static DBHandler instance = null;
    public static DBHandler getInstance() {
        return instance;
    }

    public abstract List<Object> select(String tableName, String[] columns, String where);
    public abstract List<Object> query(String sql, String[] columns);
    public abstract List<Object> create(String tableName, DBColumnDesc[] columns);
    public abstract List<Object> insert(String tableName, String[] columns);
    public abstract boolean update(String tableName, String[] columns, String[] values, String[] where);
    public abstract boolean delete(String tableName, String column, String[] where);
}
