package uk.ac.ncl.dwa.model;

import org.mariadb.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Settings extends HashMap<String, Object> {

    public Settings() {
        super();
    }

    public void loadFromDatabase(String connectionString) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(connectionString);
            String sql = "SELECT keyvalue, value FROM settings";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                put(resultSet.getString("keyvalue"), resultSet.getString("value"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
