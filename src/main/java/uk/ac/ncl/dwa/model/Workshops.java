package uk.ac.ncl.dwa.model;

import java.io.Serial;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.database.DBHandler;

public class Workshops extends ArrayList<Workshop> {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Serial
    private static final long serialVersionUID = 1L;

    // TODO	There should be a better way of doing this, but in the meantime ..
    Hashtable<String, Integer> id_index = new Hashtable<>();

    /**
     * Find workshop using its ID
     *
     * @param slug the ID of the workshop
     * @return the workshop with the provided ID
     */
    public Workshop returnWorkshop(String slug) {
        return get(id_index.get(slug));
    }

    /**
     * The number of attributes (columns) in the Workshop class.
     *
     * @return an integer containing the number attributes in the workshop class to be used as columns in a table
     */
    public int getColumnCount() {
        return Workshop.getColumnCount();
    }

    /**
     * Get the names of the attributes in the Workshop class to be used column names in a table
     *
     * @return an array of string containing the names of the columns
     */
    public String[] getColumnNames() {
        return Workshop.getColumnNames();
    }

    /**
     * Get the names of all the workshops
     *
     * @return an array of string containing the names of all the workshops
     */
    public String[] getWorkshopNames() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("All");
        this.forEach((ws) -> {
            names.add(ws.getSlug());
        });
        String[] ret = names.toArray(new String[0]);
        logger.trace("Workshop names: {}", ret.length);
        return ret;
    }

    /**
     * Find the name of a workshop given its ID
     *
     * @param id a string containing the ID of the workshop
     * @return the name of the workshop given the ID
     */
    public String getWorkshopName(String id) {
        return get(id_index.get(id)).getSlug();
    }

    /**
     * A list of all the workshop IDs
     *
     * @return an array of string containing all the workshop IDs
     */
    public String[] getSlugs() {
        ArrayList<String> ids = new ArrayList<String>();
        ids.add("All");
        this.forEach((ws) -> {
            ids.add(ws.getSlug());
        });
        String[] ret = ids.toArray(new String[0]);
        logger.trace("Workshop slugs: " + ret.length);
        return ret;
    }

    /**
     * Check whether a workshop exists for the given ID
     *
     * @param slug a string containing the ID of the workshop
     * @return true if a workshop with the given ID was found, otherwise false
     */
    public boolean exists(String slug) {
        for (Workshop workshop : this) {
            if (workshop.getSlug().equals(slug))
                return true;
        }
        return false;
    }

    /**
     * Override the add method in order to update a hashtable that contains the
     * workshopID as the key and the index of the workshop in the array as the value
     *
     * @param workshop element whose presence in this collection is to be ensured
     * @return true if the workshop was successfully added
     */
    @Override
    public boolean add(Workshop workshop) {
        logger.trace("Add workshop: {}", workshop.getTitle());
        boolean ret = super.add(workshop);
        id_index.put(workshop.getTitle(), this.size() - 1);
        return ret;
    }

    public void loadFromDatabase() {
        String[] columnNames = Workshop.dbColumnNames;
        List<Object> settings = DBHandler.getInstance().select("workshops", columnNames, "");
        for (Object o : settings) {
            HashMap<String,Object> settingObject = (HashMap<String, Object>) o;
            Workshop workshop = new Workshop((String)settingObject.get(columnNames[0]),
                    (String)settingObject.get(columnNames[1]),
                    (String)settingObject.get(columnNames[2]),
                    (String)settingObject.get(columnNames[3]),
                    (String)settingObject.get(columnNames[4]),
                    (String)settingObject.get(columnNames[5]),
                    (String)settingObject.get(columnNames[6]),
                    (String)settingObject.get(columnNames[7]),
                    (String)settingObject.get(columnNames[8]),
                    (((String) settingObject.get(columnNames[9])).equals("1")),
                    (((String) settingObject.get(columnNames[10])).equals("1")),
                    (String)settingObject.get(columnNames[11]),
                    (String)settingObject.get(columnNames[12]),
                    (String)settingObject.get(columnNames[13]),
                    (String)settingObject.get(columnNames[14]),
                    (String)settingObject.get(columnNames[15]),
                    (String)settingObject.get(columnNames[16]),
                    (String)settingObject.get(columnNames[17]),
                    (String)settingObject.get(columnNames[18]),
                    (String)settingObject.get(columnNames[19])
                    );
            workshop.setStatus('s');
            add(workshop);
            logger.info("Load workshop: {}",workshop.getSlug());
        }
    }

    public boolean updateWorkshop(Workshop workshop) {

        return DBHandler.getInstance().update(
                "workshops",
                Workshop.dbColumnNames,
                workshop.asArray(),
                new String[]{"slug=" + workshop.getKey()});
    }

    public boolean insertWorkshop(Workshop workshop) {
        if (DBHandler.getInstance().insert("workshops",
                Workshop.dbColumnNames)) {
            workshop.setStatus('s');
            return true;
        }
        return false;
    }

    public void deleteWorkshop(String key) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "DELETE FROM workshops WHERE slug = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, key);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting workshop from database", e);
        }
    }

    public ArrayList<String> loadSchedules() {
        Connection connection = null;
        ArrayList<String> ret = new ArrayList<>();
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "SELECT schedule_id FROM schedules";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ret.add(resultSet.getString("schedule_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public ArrayList<String> loadCurricula() {
        Connection connection = null;
        ArrayList<String> ret = new ArrayList<>();
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "SELECT curriculum_code FROM curriculum";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ret.add(resultSet.getString("curriculum_code"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}