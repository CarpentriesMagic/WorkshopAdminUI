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

public class People extends ArrayList<Person> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void loadFromDatabase(String connectionString) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(connectionString);
            String sql = "SELECT person_id, title, firstname, lastname, certified, email" +
                    " FROM people order by lastname";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            this.clear(); // Clear existing data
            while (resultSet.next()) {
                Person person = new Person(
                        resultSet.getString("person_id"),
                        resultSet.getString("title"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("certified"),
                        resultSet.getString("email")
                );
                person.setInserted(true);
                this.add(person);
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error loading people from database", e);
        }
    }

    public int getColumnCount() {
        return Person.getColumnNames().length;
    }


    public boolean insertPerson() {
        Connection connection = null;
        AtomicBoolean success = new AtomicBoolean(false);
        logger.info("Inserting {} rows", Globals.getInstance().getEditedRows("people").size());
        Globals.getInstance().getInsertedRows("people").forEach(row -> {
            logger.info("Saving row: {}", row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "INSERT people VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            Globals.getInstance().getInsertedRows("people").forEach(row -> {
                //for (int row : Globals.getInstance().getInsertedRows("people")) {
                Person person = this.get(row);
                logger.info("Inserting person {}", person.getPerson_id());
                try {
                    statement.setString(1, person.getPerson_id());
                    statement.setString(2, person.getTitle());
                    statement.setString(3, person.getFirstname());
                    statement.setString(4, person.getLastname());
                    statement.setString(5, person.getCertified());
                    statement.setString(6, person.getEmail());

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
            throw new RuntimeException("Error updating people in database", e);
        }
        return success.get();
    }

    public boolean updatePerson() {
        Connection connection = null;
        boolean success = false;
        logger.info("Updating {} rows", Globals.getInstance().getEditedRows("people").size());
        Globals.getInstance().getEditedRows("people").forEach(row -> {
            logger.info("Saving row: {}", row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "UPDATE people SET title = ?, firstname = ?, lastname = ?, certified = ?," +
                    "email = ? WHERE person_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            for (int row : Globals.getInstance().getEditedRows("people")) {
                logger.info("Updating row: {}", row);
                Person person = this.get(row);
                logger.info("Updating people {}", person.getPerson_id());
                try {
                    statement.setString(1, person.getTitle());
                    statement.setString(2, person.getFirstname());
                    statement.setString(3, person.getLastname());
                    statement.setString(4, person.getCertified());
                    statement.setString(5, person.getEmail());
                    statement.setString(6, person.getKey());
                    statement.executeUpdate();
                    success = true;
                } catch (SQLException e) {
                    success = false;
                    throw new RuntimeException(e);
                }
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating people in database", e);
        }
        return success;
    }


    public String[] getColumnNames() {
        return Person.getColumnNames();
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
                case 1:sql = "SELECT person_id, firstname, lastname FROM people WHERE certified = " + certvalue;
                    break;
                // HELPERS
                case 2: sql = "SELECT person_id, firstname, lastname FROM people WHERE certified > " + 0;
                    break;
                // EVERYONE
                default:
                    sql = "SELECT person_id, firstname, lastname FROM people";
                    break;
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                instructors.add(resultSet.getString("person_id") + "," +
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
