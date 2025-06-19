package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBController;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.database.DBHandlerMysql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Settings extends HashMap<String, Object> {
    String connectionString;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static String[] columnNames = {"keyValue", "value"};
    public static String[] dbColumnNames = {"keyValue", "value"};

    public Settings(String connectionString) {
        super();
        this.connectionString = connectionString;
    }

    public int getColumnCount() {
        return dbColumnNames.length;
    }

    public void loadFromDatabase() {
        String[] columnNames = Setting.columnNames;
        List<Object> settings = DBHandler.getInstance().select("settings", columnNames, new String[]{});
        for (Object o : settings) {
            HashMap<String,Object> settingObject = (HashMap<String, Object>) o;
            Setting setting = new Setting((String)settingObject.get(columnNames[0]), (String)settingObject.get(columnNames[1]));
            put(setting.getKeyValue(), setting.getValue());
            logger.info("key {}, value {}",setting.getKeyValue(), setting.getValue());
        }
    }
}
