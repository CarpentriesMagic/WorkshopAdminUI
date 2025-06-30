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

public class People extends ArrayList<Person> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public People() {
        super();
        loadFromDatabase();
    }

    public void loadFromDatabase() {
        String[] columnNames = Person.dbColumnNames;
        List<Object> people = DBHandler.getInstance().select("people", columnNames, "");
        for (Object object : people) {
            HashMap<String, Object> peopleMap = (HashMap<String, Object>) object;
            Person person = new Person(
                    (String) peopleMap.get(columnNames[0]),
                    (String) peopleMap.get(columnNames[1]),
                    (String) peopleMap.get(columnNames[2]),
                    (String) peopleMap.get(columnNames[3]),
                    (String) peopleMap.get(columnNames[4]),
                    (String) peopleMap.get(columnNames[5]),
                    's'
            );
            add(person);
        }
    }

    public int getColumnCount() {
        return Person.columnNames.length;
    }


    public boolean insertPerson(Person person) {
        logger.debug("Inserting person person id {}", person.getPerson_id());
        String[] values = {person.getPerson_id(), person.getTitle(),
                person.getFirstname(), person.getLastname(),
                person.getCertified(), person.getEmail()};
        if (DBHandler.getInstance().insert("people", values)) {
            person.setStatus('s');
            return true;
        } else return false;
    }

    public void updatePerson(Person person) {
        logger.debug("Updating person {}", person.getPerson_id());
        String[] values = {person.getPerson_id(), person.getTitle(),
                person.getFirstname(), person.getLastname(),
                person.getCertified(), person.getEmail()};
        DBHandler.getInstance().update("people", Person.dbColumnNames, values,
                new String[]{"person_id='" + person.getKey() + "'"});
    }

    /**
     * Add a new setting (also add to insertedRows array for later saving to database)
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, Person element) {
        super.add(index, element);
        System.out.println(element.toString());
    }

    @Override
    public Person remove(int index) {
        Person person = get(index);
        logger.debug("Removing person for key={}",person.getPerson_id());
        DBHandler.getInstance().delete("people",
                new String[]{"person_id"},
                new String[]{person.getKey()});
        super.remove(index);
        return person;
    }

    @Override
    public boolean remove(Object o) {
        String key = (String)o;
        DBHandler.getInstance().delete("people", new String[]{"person_id"},
                new String[]{key});
        super.remove(key);
        return true;
    }

    public String[] getColumnNames() {
        return Person.columnNames;
    }

    public boolean deletePerson(String key) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "DELETE FROM people WHERE person_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, key);
            statement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            logger.debug("Error deleting person/workshop from database\n {}", e.getMessage());
            return false;
        }
    }

    /**
     * Select specific people from the database
     *
     * @param certvalue 1 for instructors, 2 for helpers, 0 for everybody else
     * @return
     */
    public static String[] selectedList(int certvalue) {
        Connection connection = null;
        ArrayList<String> instructors = new ArrayList<>();
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql;
            switch (certvalue) {
                //INSTRUCTORS
                case 1:
                    sql = "SELECT person_id, title, firstname, lastname FROM people WHERE certified = " + certvalue;
                    break;
                // HELPERS
                case 2:
                    sql = "SELECT person_id, title, firstname, lastname FROM people WHERE certified > " + 0;
                    break;
                // EVERYONE
                default:
                    sql = "SELECT person_id, title, firstname, lastname FROM people";
                    break;
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                instructors.add(resultSet.getString("person_id") + "," +
                        resultSet.getString("title") + " " +
                        resultSet.getString("firstname") + " " +
                        resultSet.getString("lastname"));
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting instructor/workshop from database", e);
        }
        return instructors.toArray(new String[instructors.size()]);
    }



}
