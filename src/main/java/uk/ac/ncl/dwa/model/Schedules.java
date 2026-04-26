package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Schedules extends ArrayList<Schedule> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static String[] columnNames = {"schedule_id"};
    public static String[] dbColumnNames = {"schedule_id"};
    public boolean isDirty = false;

    public Schedules() {
        super();
        loadFromDatabase();
    }


    public int getColumnCount() {
        return dbColumnNames.length;
    }


    public String[] getColumnNames() {
        return columnNames;
    }

    public void loadFromDatabase() {
        String[] columnNames = Setting.columnNames;
        List<Object> schedules = DBHandler.getInstance().select("settings", columnNames, "", "", true);
        for (Object o : schedules) {
            HashMap<String,Object> settingObject = (HashMap<String, Object>) o;
            Schedule schedule = new Schedule((String)settingObject.get(columnNames[0]));
            add(schedule);
            logger.info("schedule id: {}",schedule.getSchedule_id());
        }
    }
}
