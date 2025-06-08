package uk.ac.ncl.dwa.database;

import java.util.List;

public abstract class DBHandler {
    protected static DBHandler instance = null;
    public static DBHandler getInstance() {
        return instance;
    }

    public abstract List<Object> select(String tableName, String[] columns, String[] where);
    public abstract List<Object> create(String tableName, DBColumnDesc[] columns);
    public abstract List<Object> insert();
    public abstract List<Object> update();
}
