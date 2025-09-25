package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings extends ArrayList<Setting> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static String[] columnNames = {"keyValue", "value"};
    public static String[] dbColumnNames = {"keyValue", "value"};
    public boolean isDirty = false;

    public Settings() {
        super();
        loadFromDatabase();
    }

    public int getColumnCount() {
        return dbColumnNames.length;
    }

    public void loadFromDatabase() {
        String[] columnNames = Setting.columnNames;
        List<Object> settings = DBHandler.getInstance().select("settings", columnNames, "", "");
        for (Object o : settings) {
            HashMap<String,Object> settingObject = (HashMap<String, Object>) o;
            Setting setting = new Setting((String)settingObject.get(columnNames[0]),
                    (String)settingObject.get(columnNames[1]),
                    's');
            add(setting);
            logger.info("key {}, value {}",setting.getKey(), setting.getValue());
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
            settings.put(setting.getKey(), setting.getValue());
        }
        return settings;
    }

    public boolean insertSetting(Setting setting) {
        if (DBHandler.getInstance().insert("settings",
                new String[]{setting.getKey(), setting.getValue()})) {
            setting.setStatus('s');
            return true;
        } else return false;

    }

    public void updateSettings(Setting setting) {
        DBHandler.getInstance().update("settings", new String[]{"value"},
                new String[]{setting.getValue()}, new String[]{"keyvalue='" + setting.getOriginal_keyValue() + "'"});
    }


    /**
     * Add a new setting (also add to insertedRows array for later saving to database)
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, Setting element) {
        super.add(index, element);
        System.out.println(element.toString());
    }

    @Override
    public Setting remove(int index) {
        Setting setting = get(index);
        logger.debug("Removing setting for key={}",setting.getKey());
        DBHandler.getInstance().delete("settings", new String[]{"keyvalue"},
                new String[]{setting.getKey()});
        super.remove(index);
        return setting;
    }

    @Override
    public boolean remove(Object o) {
        String key = (String)o;
        DBHandler.getInstance().delete("settings",
                new String[]{"keyvalue"},
                new String[]{key});
        super.remove(key);
        return true;
    }

    public List<Object> selectSetting(String key) {
        return DBHandler.getInstance().select("settings",new String[]{"keyValue"}, key, "");
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }
}
