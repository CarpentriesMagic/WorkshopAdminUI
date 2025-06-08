package uk.ac.ncl.dwa.model;

import uk.ac.ncl.dwa.database.DBController;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.database.DBHandlerMysql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Settings extends HashMap<String, Object> {
    String connectionString;

    public Settings(String connectionString) {
        super();
        this.connectionString = connectionString;
    }

    public void loadFromDatabase() {
        List<Object> settings = DBHandler.getInstance().select("settings", new String[]{"keyvalue", "value"}, new String[]{});
        for (Object o : settings) {
            Setting setting = (Setting) o;
            put(setting.getKeyValue(), setting.getValue());
        }
    }
}
