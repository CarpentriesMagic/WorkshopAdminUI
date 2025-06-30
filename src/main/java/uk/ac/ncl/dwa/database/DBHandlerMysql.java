package uk.ac.ncl.dwa.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DBHandlerMysql extends DBHandler {
    String connectionString;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DBHandlerMysql(String connectionString) {
        this.connectionString = connectionString;
        instance = this;
    }

    @Override
    public List<Object> select(String tableName, String[] columns, String[] where) {
        Connection connection;
        String sql = String.format("SELECT %s FROM %s", String.join(",", Arrays.asList(columns)), tableName);
        List<Object> returnList = new ArrayList<>();
        try {
            logger.info("Connection string: {}", connectionString);
            connection = DriverManager.getConnection(connectionString);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                HashMap<String, Object> recordSet = new HashMap<>();
                for (String column : columns) {
                    recordSet.put(column, resultSet.getString(column));
                }
                returnList.add(recordSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return returnList;
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
