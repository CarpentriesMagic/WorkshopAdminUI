package uk.ac.ncl.dwa.model;

import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.database.DBHandler;

import java.io.Serial;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Helpers extends ArrayList<Helper> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Helpers() {
        super();
        loadFromDatabase();
    }

    public void loadFromDatabase() {
        String[] columnNames = new String[]{"slug", "person_id", "title",
                "firstname", "lastname"};
        String sql = "SELECT h.slug as slug, h.person_id as person_id, " +
                "p.title as title, p.firstname as firstname, " +
                "p.lastname as lastname " +
                "FROM helpers as h " +
                "JOIN people as p on p.person_id=h.person_id " +
                "ORDER BY h.slug ASC";
        logger.info(sql);
        List<Object> helpers = DBHandler.getInstance().query(sql, columnNames);
        for (Object object : helpers) {
            HashMap<String, Object> helperMap = (HashMap<String, Object>) object;
            Helper helper = new Helper(
                    (String) helperMap.get(columnNames[0]),
                    (String) helperMap.get(columnNames[1]),
                    ((String) helperMap.get(columnNames[2]) + " " +
                    (String) helperMap.get(columnNames[3]) + " " +
                    (String) helperMap.get(columnNames[4])).trim(),
                    's'
            );
            logger.info(helper.toString());
            add(helper);
        }
    }

    public int getColumnCount() {
        return Helper.columnNames.length;
    }

    public boolean insertHelpers(Helper helper) {
        logger.info("Inserting helper {}", helper.getName());
        String[] values = {helper.getPerson_id(), helper.getSlug()};
        if (DBHandler.getInstance().insert("helpers", values)) {
            helper.setStatus('s');
            return true;
        } else {
            helper.setStatus('n');
            return false;
        }
    }

    public String[] getColumnNames() {
        return Helper.columnNames;
    }

    @Override
    public Helper remove(int index) {
        Helper helper = get(index);
        super.remove(index);
        if (helper.getStatus() == 's') {
            logger.debug("Removing helper for key={}", helper.getPerson_id());
            if (!DBHandler.getInstance().delete("helpers", new String[]{"person_id", "slug"},
                    new String[]{helper.getPerson_id(), helper.getSlug()}))
                helper = null;
        }
        return helper;
    }

    @Override
    public boolean remove(Object o) {
        String key = (String)o;
        if (DBHandler.getInstance().delete("helpers", new String[]{"person_id"},
                new String[]{key})) {
            super.remove(key);
            return true;
        } else return false;
    }

}
