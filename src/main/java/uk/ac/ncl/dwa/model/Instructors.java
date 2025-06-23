package uk.ac.ncl.dwa.model;

import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.database.DBHandler;

import java.io.Serial;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
        String[] columnNames = {"slug", "person_id", "firstname", "lastname"};
        List<Object> instructors = DBHandler.getInstance().query(
                "SELECT i.slug, i.person_id, firstname, lastname " +
                        "FROM instructors as i " +
                        "JOIN people as p on p.person_id=i.person_id " +
                        "ORDER BY i.slug ",
                new String[]{ "slug", "person_id", "firstname", "lastname"});
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

    public boolean insertInstructor(Instructor  instructor) {
        if (DBHandler.getInstance().insert("instructors", new String[]{instructor.getPerson_id(),
        instructor.getSlug()})) {
            instructor.setStatus('s');
            return true;
        } else {
            instructor.setStatus('n');
            return false;
        }
    }

    public void updateInstructor(Instructor  instructor) {
        DBHandler.getInstance().update("instructors", new String[]{"person_id", "slug"},
                new String[]{instructor.getPerson_id(), instructor.getSlug()},
                new String[]{"person_id='" + instructor.getKey_person_id() + "' AND slug='" + instructor.getKey_slug() + "'"});
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

    /**
     * Add a new setting (also add to insertedRows array for later saving to database)
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, Instructor element) {
        super.add(index, element);
    }
}
