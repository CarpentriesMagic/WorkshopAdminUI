package uk.ac.ncl.dwa.model;

import java.io.Serial;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.mariadb.jdbc.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

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
    public String[] getWorkshopNames(String connectionString) {
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
        logger.trace("Add workshop: " + workshop.getTitle());
        boolean ret = super.add(workshop);
        id_index.put(workshop.getTitle(), this.size() - 1);
        return ret;
    }

    public static void selected(String table, int[] columns) {

        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "SELECT slug, title, humandate, humantime, startdate, starttime, room_id, language," +
                    "country, online, pilot, inc_lesson_site, pre_survey, post_survey, carpentry_code, flavour_id," +
                    "eventbrite, schedule FROM " + table;
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            String val;
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                for (int column : columns) {
                    val = resultSet.getString(column);
                    result.append(" - ").append(val);
                }
                System.out.println(result);
                val = "";
                result = new StringBuilder();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press any Enter to continue...");
        scanner.nextLine();
    }

    public void loadFromDatabase(String connectionString) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(connectionString);
            String sql = "SELECT * FROM workshops";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            this.clear(); // Clear existing data
            while (resultSet.next()) {
                Workshop workshop = new Workshop(
                        resultSet.getString("slug"),
                        resultSet.getString("title"),
                        resultSet.getString("humandate"),
                        resultSet.getString("humantime"),
                        resultSet.getString("startdate"),
                        resultSet.getString("enddate"),
                        resultSet.getString("room_id"),
                        resultSet.getString("language"),
                        resultSet.getString("country"),
                        resultSet.getBoolean("online"),
                        resultSet.getBoolean("pilot"),
                        resultSet.getString("inc_lesson_site"),
                        resultSet.getString("pre_survey"),
                        resultSet.getString("post_survey"),
                        resultSet.getString("carpentry_code"),
                        resultSet.getString("curriculum_code"),
                        resultSet.getString("flavour_id"),
                        resultSet.getString("eventbrite"),
                        resultSet.getString("schedule")

                );
                workshop.setInserted(true);
                this.add(workshop);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error loading workshops from database", e);
        }
    }

    public boolean updateWorkshops() {
        Connection connection = null;
        boolean success = false;
        logger.info("Updating {} rows", Globals.getInstance().getEditedRows("workshops").size());
        Globals.getInstance().getEditedRows("workshops").forEach(row -> {
            logger.info("Saving row: {}", row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "UPDATE workshops SET title = ?, humandate = ?, humantime = ?, startdate = ?, enddate = ?, " +
                    "room_id = ?, language = ?, country = ?, online = ?, pilot = ?, carpentry_code = ?, " +
                    "curriculum_code = ?, flavour_id = ?, schedule = ?, inc_lesson_site = ?, pre_survey = ?, " +
                    "post_survey = ?, eventbrite = ?, slug = ? WHERE slug = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            for (int row : Globals.getInstance().getEditedRows("workshops")) {
                logger.info("Updating row: {}", row);
                Workshop workshop = this.get(row);
                logger.info("Updating workshop {}", workshop.getSlug());
                try {
                    statement.setString(1, workshop.getTitle());
                    statement.setString(2, workshop.getHumandate());
                    statement.setString(3, workshop.getHumantime());
                    statement.setString(4, workshop.getStartdate());
                    statement.setString(5, workshop.getEnddate());
                    statement.setString(6, workshop.getRoom_id());
                    statement.setString(7, workshop.getLanguage());
                    statement.setString(8, workshop.getCountry());
                    statement.setBoolean(9, workshop.isOnline());
                    statement.setBoolean(10, workshop.isPilot());
                    statement.setString(11, workshop.getCarpentry_code());
                    statement.setString(12, workshop.getCurriculum_code());
                    statement.setString(13, workshop.getFlavour_id());
                    statement.setString(14, workshop.getSchedule());
                    statement.setString(15, workshop.getInc_lesson_site());
                    statement.setString(16, workshop.getPre_survey());
                    statement.setString(17, workshop.getPost_survey());
                    statement.setString(18, workshop.getEventbrite());
                    statement.setString(19, workshop.getSlug());
                    statement.setString(20, workshop.getKey());

                    statement.executeUpdate();
                    success = true;
                } catch (SQLException e) {
                    success = false;
                    //throw new RuntimeException(e);
                }
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating workshops in database", e);
        }
        return success;
    }


    public boolean insertWorkshops() {
        Connection connection = null;
        boolean success = false;
        logger.info("Inserting {} rows ", Globals.getInstance().getEditedRows("workshops").size());
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "INSERT workshops VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            //Globals.getInstance().getInsertedRows().forEach(row -> {
            for (int row : Globals.getInstance().getInsertedRows("workshops")) {
                Workshop workshop = this.get(row);
                logger.info("Inserting workshop {}", workshop.getSlug());
                try {
                    statement.setString(1, workshop.getSlug());
                    statement.setString(2, workshop.getTitle());
                    statement.setString(3, workshop.getHumandate());
                    statement.setString(4, workshop.getHumantime());
                    statement.setString(5, workshop.getStartdate());
                    statement.setString(6, workshop.getEnddate());
                    statement.setString(7, workshop.getRoom_id());
                    statement.setString(8, workshop.getLanguage());
                    statement.setString(9, workshop.getCountry());
                    statement.setBoolean(10, workshop.isOnline());
                    statement.setBoolean(11, workshop.isPilot());
                    statement.setString(12, workshop.getInc_lesson_site());
                    statement.setString(13, workshop.getPre_survey());
                    statement.setString(14, workshop.getPost_survey());
                    statement.setString(15, workshop.getCarpentry_code());
                    statement.setString(16, workshop.getCurriculum_code());
                    statement.setString(17, workshop.getFlavour_id());
                    statement.setString(18, workshop.getEventbrite());
                    statement.setString(19, workshop.getSchedule());


                    statement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            success = true;
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating workshops in database", e);
        }
        return success;
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