package uk.ac.ncl.dwa.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DBHandlerMysql extends DBHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    String connectionString;

    public DBHandlerMysql(String connectionString) {
        this.connectionString = connectionString;
        instance = this;
    }

    @Override
    public List<Object> query(String sql, String[] columns) {
        Connection connection;
        List<Object> returnList = new ArrayList<>();
        try {
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
    public List<Object> select(String tableName, String[] columns, String where) {
        Connection connection;
        String sql;
        if (where.isBlank()) {
            sql = String.format("SELECT %s FROM %s", String.join(",", Arrays.asList(columns)), tableName);
        } else {
            sql = String.format("SELECT %s FROM %s WHERE %s", String.join(",", Arrays.asList(columns)), tableName, where);
            logger.info(sql);
        }
        List<Object> returnList = new ArrayList<>();
        try {
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
    public List<Object> insert(String tableName, String[] columns) {
        Connection connection;
        String values = String.join(",", Arrays.asList(columns));
        values = "'" + values.replaceAll(",","','") + "'";
        String sql = String.format("INSERT INTO %s VALUES (%s)", tableName, values);
        logger.info(String.format(sql));
        try {
            connection = DriverManager.getConnection(connectionString);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeQuery();
            connection.close();
            return List.of();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(String tableName, String[] columns,
                          String[] values, String[] where) {
        Connection connection;
        String[] setString;
        if (columns.length != values.length) {
            throw new RuntimeException("Columns and values arrays do not match!");
        } else {
            setString = new String[columns.length];
            for (int i=0; i<columns.length; i++) {
                setString[i] = String.format("%s='%s'",columns[i],values[i]);
            }

        }
        String setS = String.join(",", setString);
        logger.info(setS);
        String sql = String.format("UPDATE %s SET %s WHERE %s",
                tableName, String.join(",", setString), where[0]);
        logger.info(sql);
        try {
            connection = DriverManager.getConnection(connectionString);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeQuery();
            connection.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String tableName, String column, String[] where) {
        Connection connection;
        try {
            connection = (Connection) DriverManager.getConnection(connectionString);
            String sql = String.format("DELETE FROM %s " +
                    "WHERE %s=?",
                    tableName, column, where);
            logger.info(sql);
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i=0; i<where.length; i++) {
                statement.setString(1, where[i]);
            }
            statement.executeQuery();
            connection.close();
            return true;
        } catch (SQLException e) {
            logger.info("Error deleting workshop from database:\n{}", e);
            return false;
        }
    }
}
