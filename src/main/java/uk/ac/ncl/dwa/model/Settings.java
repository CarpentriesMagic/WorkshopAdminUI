package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.database.DBHandlerMysql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings extends ArrayList<Setting> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static String[] columnNames = {"keyValue", "value"};
    public static String[] dbColumnNames = {"keyValue", "value"};
    private ArrayList<Integer> insertedRows = new ArrayList<>();
    private ArrayList<Integer> updatedRows = new ArrayList<>();

    public Settings() {
        super();
        loadFromDatabase();
    }

    public int getColumnCount() {
        return dbColumnNames.length;
    }

    public void loadFromDatabase() {
        String[] columnNames = Setting.columnNames;
        List<Object> settings = DBHandler.getInstance().select("settings", columnNames, "");
        for (Object o : settings) {
            HashMap<String,Object> settingObject = (HashMap<String, Object>) o;
            Setting setting = new Setting((String)settingObject.get(columnNames[0]), (String)settingObject.get(columnNames[1]));
            add(setting);
            logger.info("key {}, value {}",setting.getKeyValue(), setting.getValue());
        }
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public HashMap<String, String> getHashMap() {
        HashMap<String, String> settings = new HashMap<>();
        for (Setting setting : this) {
            settings.put(setting.getKeyValue(), setting.getValue());
        }
        return settings;
    }

    public void deleteSetting(String key) {
        DBHandler.getInstance().deleteOne("settings", "keyvalue", key);

    }

    public void insertSetting(Setting setting) {
        DBHandler.getInstance().insert("settings",
                new String[]{setting.getKeyValue(), setting.getValue()});
    }

    public boolean updateSettings(String key, String newValue) {
        logger.debug("Updating settings for key={} to value={}", key, newValue);
        DBHandler.getInstance().update("settings", new String[]{"value"},
                new String[]{newValue}, new String[]{"keyvalue='" + key + "'"});
        return true;
    }

    public ArrayList<Integer> getInsertedRows() {
        return insertedRows;
    }

    public ArrayList<Integer> getUpdatedRows() {
        return updatedRows;
    }

    @Override
    public boolean add(Setting element) {
        if (super.add(element)) {
            insertedRows.add(this.size() - 1);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update an existing record
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return
     */
    @Override
    public Setting set(int index, Setting element) {
        Setting oldrecord = super.set(index, element);
        updatedRows.add(index);
        return oldrecord;
    }

    public List<Object> selectSetting(String key) {
        return DBHandler.getInstance().select("settings",new String[]{"keyValue"}, key);

    }

    public boolean isDirty() {
        return !updatedRows.isEmpty() || !insertedRows.isEmpty();
    }
}
