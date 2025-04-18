package uk.ac.ncl.dwa.model;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import org.mariadb.jdbc.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Workshops extends ArrayList<Workshop> {
    Logger logger = LoggerFactory.getLogger(getClass());

    Workshops workshops = this;
    private static final long serialVersionUID = 1L;

    // TODO	There should be a better way of doing this, but in the meantime ..
    Hashtable<String, Integer> id_index = new Hashtable<>();

    /**
     * Find workshop using its ID
     * @param slug the ID of the workshop
     * @return the workshop with the provided ID
     */
    public Workshop returnWorkshop(String slug) {
        return workshops.get(id_index.get(slug));
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
        logger.trace("Workshop names: " + ret.length);
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
        for (int i = 0; i < this.size(); i++) {
            if (get(i).getSlug().equals(slug))
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

    public static void selected(String table, int[] columns, String connectionString) {

        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(connectionString);
            String sql = "SELECT * FROM " + table;
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            String val;
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                for (int i = 0; i < columns.length; i++) {
                    val = resultSet.getString(columns[i]);
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
                        LoggerFactory.getLogger(Workshop.class),
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
                        resultSet.getString("carpentry_code"),
                        resultSet.getString("curriculum_code"),
                        resultSet.getString("flavour_id"),
                        resultSet.getString("schedule"),
                        resultSet.getString("inc_lesson_site"),
                        resultSet.getString("pre_survey"),
                        resultSet.getString("post_survey"),
                        resultSet.getString("eventbrite")

                        );
                this.add(workshop);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error loading workshops from database", e);
        }
    }

}