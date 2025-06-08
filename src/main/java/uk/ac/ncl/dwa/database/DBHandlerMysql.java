package uk.ac.ncl.dwa.database;

import uk.ac.ncl.dwa.model.Setting;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DBHandlerMysql extends DBHandler {
    String connectionString;

    public DBHandlerMysql(String connectionString) {
        this.connectionString = connectionString;
        instance = this;
    }

    @Override
    public List<Object> select(String tableName, String[] columns, String[] where) {
        Connection connection;
        String sql = String.format("SELECT %s FROM %s", String.join(",", Arrays.asList(columns)), tableName);
        List<Object> settings = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(connectionString);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Setting setting = new Setting(resultSet.getString("keyvalue"), resultSet.getString("value"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return settings;
    }

    @Override
    public List<Object> create(String tableName, DBColumnDesc[] columns) {
        return List.of();
    }

    @Override
    public List<Object> insert() {
        return List.of();
    }

    @Override
    public List<Object> update() {
        return List.of();
    }
}
