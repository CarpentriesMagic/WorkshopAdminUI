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
import java.util.concurrent.atomic.AtomicBoolean;

public class Helpers extends ArrayList<Helper> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public void loadFromDatabase(String connectionString) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(connectionString);
            String sql = "SELECT slug, person_id FROM helpers order by slug";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            this.clear(); // Clear existing data
            while (resultSet.next()) {
                Helper helper = new Helper(
                        resultSet.getString("slug"),
                        resultSet.getString("person_id")
                );
                helper.setInserted(true);
                this.add(helper);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error loading helpers from database", e);
        }
    }

    public int getColumnCount() {
        return Helper.getColumnNames().length;
    }

    public boolean insertHelpers() {
        Connection connection = null;
        AtomicBoolean success = new AtomicBoolean(false);
        logger.info("Updating " + Globals.getInstance().getEditedRows("helpers").size() + " rows");
        Globals.getInstance().getInsertedRows("helpers").forEach(row -> {
            logger.info("Saving row: " + row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "INSERT helpers VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            Globals.getInstance().getInsertedRows("helpers").forEach(row -> {
            //for (int row : Globals.getInstance().getInsertedRows("helpers")) {
                Helper helper = this.get(row);
                logger.info("Inserting helper " + helper.getPerson_id());
                try {
                    statement.setString(1, helper.getPerson_id());
                    statement.setString(2, helper.getSlug());

                    statement.executeUpdate();
                    success.set(true);
                } catch (SQLException e) {
                    success.set(false);
                    throw new RuntimeException(e);
                }
            });
                    success.set(true);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating helpers in database", e);
        }
        return success.get();
    }

    public boolean updateHelpers() {
    Connection connection = null;
        AtomicBoolean success = new AtomicBoolean(true);
        logger.info("Updating {} rows", Globals.getInstance().getEditedRows("helpers").size());
        Globals.getInstance().getEditedRows("helpers").forEach(row -> {
            logger.info("Saving row: {}", row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "UPDATE helpers SET slug = ?, person_id = ? WHERE person_id = ? and slug = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            for (int row : Globals.getInstance().getEditedRows("helpers")) {
                logger.info("Updating row: {}", row);
                Helper helper = this.get(row);
                logger.info("Updating helpers {}", helper.getPerson_id());
                try {
                    statement.setString(1, helper.getSlug());
                    statement.setString(2, helper.getPerson_id());
                    statement.setString(3, helper.getKey_person_id());
                    statement.setString(4, helper.getKey_slug());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    logger.error("The record could not be updated");
                    success.set(false);
                }
            }
            connection.close();
        } catch (SQLException e) {
            success.set(false);
        }
        return success.get();
    }


    public String[] getColumnNames() {
        return Helper.getColumnNames();
    }

    public void deleteHelper(String personId, String slug) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "DELETE FROM helpers WHERE person_id = ? and slug = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, personId);
            statement.setString(2, slug);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting helper/workshop from database", e);
        }
    }
}
