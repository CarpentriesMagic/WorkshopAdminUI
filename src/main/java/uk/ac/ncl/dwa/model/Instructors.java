package uk.ac.ncl.dwa.model;

import org.mariadb.jdbc.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import java.io.Serial;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Instructors  extends ArrayList<Instructor> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public void loadFromDatabase(String connectionString) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(connectionString);
            String sql = "SELECT * FROM instructors";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            this.clear(); // Clear existing data
            while (resultSet.next()) {
                Instructor instructor = new Instructor(
                        resultSet.getString("person_id"),
                        resultSet.getString("slug")
                );
                instructor.setInserted(true);
                this.add(instructor);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error loading workshops from database", e);
        }
    }

    public int getColumnCount() {
        return Instructor.getColumnNames().length;
    }

    public boolean insertInstructors() {
        Connection connection = null;
        boolean success = false;
        logger.info("Updating " + Globals.getInstance().getEditedRows("instructors").size() + " rows");
        Globals.getInstance().getInsertedRows("instructors").forEach(row -> {
            logger.info("Saving row: " + row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "INSERT instructors VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            //Globals.getInstance().getInsertedRows().forEach(row -> {
            for (int row : Globals.getInstance().getInsertedRows("instructors")) {
                Instructor instructor = this.get(row);
                logger.info("Inserting instructor " + instructor.getPerson_id());
                try {
                    statement.setString(1, instructor.getPerson_id());
                    statement.setString(2, instructor.getSlug());

                    statement.executeUpdate();
                    success = true;
                } catch (SQLException e) {
                    success = false;
                    throw new RuntimeException(e);
                }
            }
            success = true;
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating instructors in database", e);
        }
        return success;
    }

    public boolean updateInstructors() {
    Connection connection = null;
        boolean success = false;
        logger.info("Updating {} rows", Globals.getInstance().getEditedRows("instructors").size());
        Globals.getInstance().getEditedRows("instructors").forEach(row -> {
            logger.info("Saving row: {}", row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "UPDATE instructor SET person_id = ?, slug = ? WHERE person_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            for (int row : Globals.getInstance().getEditedRows("instructors")) {
                logger.info("Updating row: {}", row);
                Instructor instructor = this.get(row);
                logger.info("Updating instructors {}", instructor.getPerson_id());
                try {
                    statement.setString(1, instructor.getPerson_id());
                    statement.setString(2, instructor.getSlug());
                    statement.executeUpdate();
                    success = true;
                } catch (SQLException e) {
                    success = false;
                    throw new RuntimeException(e);
                }
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating instructors in database", e);
        }
        return success;
    }


    public String[] getColumnNames() {
        return Instructor.getColumnNames();
    }

    public void deleteInstructor(String personId, String slug) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "DELETE FROM instructors WHERE person_id = ? and slug = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, personId);
            statement.setString(2, slug);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting instructor/workshop from database", e);
        }
    }
}
