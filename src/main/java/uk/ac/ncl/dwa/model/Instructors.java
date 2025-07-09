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

    public void loadFromDatabase() {
        String[] columnNames = {"slug", "person_id", "firstname", "lastname"};
        List<Object> instructors = DBHandler.getInstance().query(
                "SELECT i.slug, i.person_id, firstname, lastname " +
                        "FROM instructors as i " +
                        "JOIN people as p on p.person_id=i.person_id " +
                        "ORDER BY i.slug ",
                new String[]{"slug", "person_id", "firstname", "lastname"});
        for (Object o : instructors) {
            HashMap<String, Object> settingObject = (HashMap<String, Object>) o;
            Instructor instructor = new Instructor((String) settingObject.get(columnNames[0]),
                    (String) settingObject.get(columnNames[1]),
                    (String) settingObject.get(columnNames[2]) + " " +
                            (String) settingObject.get(columnNames[3]),
                    's'
            );
            add(instructor);
            logger.info("Load instructor {}", instructor.getName());
        }
    }

    public int getColumnCount() {
        return Instructor.getColumnNames().length;
    }

    public boolean insertInstructor(Instructor instructor) {
        if (DBHandler.getInstance().insert("instructors",
                new String[]{instructor.getPerson_id(),
                        instructor.getSlug()})) {
            instructor.setStatus('s');
            return true;
        } else {
            instructor.setStatus('n');
            return false;
        }
    }

    public void updateInstructor(Instructor instructor) {
        DBHandler.getInstance().update("instructors", new String[]{"person_id", "slug"},
                new String[]{instructor.getPerson_id(), instructor.getSlug()},
                new String[]{"person_id='" + instructor.getKey_person_id() + "' AND slug='" + instructor.getKey_slug() + "'"});
    }

    public String[] getColumnNames() {
        return Instructor.getColumnNames();
    }

    /**
     * Add a new setting (also add to insertedRows array for later saving to database)
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, Instructor element) {
        super.add(index, element);
    }

    @Override
    public Instructor remove(int index) {
        Instructor instructor = get(index);
        logger.debug("Removing instructor={}", instructor.getPerson_id());
        if (DBHandler.getInstance().delete("instructors", new String[]{"person_id", "slug"},
                new String[]{instructor.getPerson_id(), instructor.getSlug()})) {
            super.remove(index);
            return instructor;
        } else return null;
    }

    @Override
    public boolean remove(Object o) {
        Instructor instructor = (Instructor) o;
        DBHandler.getInstance().delete("instructors", new String[]{"person_id", "slug"},
                new String[]{instructor.getPerson_id(), instructor.getSlug()});
        super.remove(instructor);
        return true;
    }
}