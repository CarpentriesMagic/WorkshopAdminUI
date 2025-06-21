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

public class Instructors  extends ArrayList<Instructor> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Instructors() {
        super();
        loadFromDatabase();
    }

    public List<Object> loadFromDatabase() {
        String[] columnNames = {"person_id", "slug", "firstname", "lastname"};
        List<Object> instructors = DBHandler.getInstance().query(
                "select i.person_id, slug, firstname, lastname from instructors as i join people as p on p.person_id=i.person_id",
                new String[]{"person_id", "slug", "firstname", "lastname"});
        for (Object o : instructors) {
            HashMap<String,Object> settingObject = (HashMap<String, Object>) o;
            Instructor instructor = new Instructor((String)settingObject.get(columnNames[0]),
                    (String)settingObject.get(columnNames[1]),
                    (String)settingObject.get(columnNames[2]) + " " +
                            (String)settingObject.get(columnNames[3]),
                   's'
            );
            add(instructor);
            logger.info("Load instructor {}", instructor.getName());
        }
        return instructors;
    }



    public int getColumnCount() {
        return Instructor.getColumnNames().length;
    }

    public boolean insertInstructors() {
        Connection connection = null;
        AtomicBoolean success = new AtomicBoolean(false);
        logger.info("Inserting {} rows", Globals.getInstance().getEditedRows("instructors").size());
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "INSERT instructors VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            Globals.getInstance().getInsertedRows("instructors").forEach(row -> {
            //for (int row : Globals.getInstance().getInsertedRows("instructors")) {
                Instructor instructor = this.get(row);
                logger.info("Inserting instructor " + instructor.getPerson_id());
                try {
                    statement.setString(1, instructor.getPerson_id());
                    statement.setString(2, instructor.getSlug());

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
            throw new RuntimeException("Error updating instructors in database", e);
        }
        return success.get();
    }

    public boolean updateInstructors() {
    Connection connection = null;
        AtomicBoolean success = new AtomicBoolean(true);
        logger.info("Updating {} rows", Globals.getInstance().getEditedRows("instructors").size());
        Globals.getInstance().getEditedRows("instructors").forEach(row -> {
            logger.info("Saving row: {}", row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "UPDATE instructors SET slug = ?, person_id = ? WHERE person_id = ? and slug = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

//            for (int row : Globals.getInstance().getEditedRows("instructors")) {
            Globals.getInstance().getEditedRows("instructors").forEach(row ->{
                logger.info("Updating row: {}", row);
                Instructor instructor = this.get(row);
                logger.info("Updating instructors {}", instructor.getPerson_id());
                try {
                    statement.setString(1, instructor.getSlug());
                    statement.setString(2, instructor.getPerson_id());
                    statement.setString(3, instructor.getKey_person_id());
                    statement.setString(4, instructor.getKey_slug());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    logger.error("The record could not be updated");
                    success.set(false);
//                    throw new RuntimeException(e);
                }
            });
            connection.close();
        } catch (SQLException e) {
            success.set(false);
        }
        return success.get();
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
