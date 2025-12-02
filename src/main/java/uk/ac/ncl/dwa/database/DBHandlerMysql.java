/**
 * Some notes stuff found and used:
 * https://stackoverflow.com/questions/71452373/java-sql-sqlexception-access-denied-for-user-rootlocalhost-using-password
 */
package uk.ac.ncl.dwa.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DBHandlerMysql extends DBHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    String connectionString;

    public DBHandlerMysql(String connectionString) {
        this.connectionString = connectionString;
        instance = this;
    }

    @Override
    public List<Object> query(String sql, String[] columns) {
        List<Object> returnList = new ArrayList<>();
        Connection connection;
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
    public List<Object> select(String tableName, String[] columns, String where, String orderBy, boolean ascending) {
        String sql;
        String orderstring = "";
        String order = ascending ? "ASC" : "DESC";
        if (!orderBy.isBlank()) {
            orderstring = " ORDER BY " + orderBy + " " + order;
        }
        if (where.isBlank()) {
            sql = String.format("SELECT %s FROM %s", String.join(",", Arrays.asList(columns)), tableName);
            logger.info("> {}", sql);
        } else {
            sql = String.format("SELECT %s FROM %s WHERE %s", String.join(",", Arrays.asList(columns)), tableName, where);
            logger.info(">> {}", sql);
        }
        sql += orderstring;
        return query(sql, columns);
    }

    @Override
    public List<Object> create(String tableName, DBColumnDesc[] columns) {
        return List.of();
    }

    @Override
    public boolean insert(String tableName, String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            columns[i] = "\"" + columns[i] + "\"";

        }
        String values = String.join(",", Arrays.asList(columns));
        String sql = String.format("INSERT INTO %s VALUES (%s)", tableName, values);
        return executeSQL(sql);
    }

    @Override
    public boolean update(String tableName, String[] columns,
                          String[] values, String[] where) {
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
        return executeSQL(sql);
    }

    private boolean executeSQL(String sql) {
        logger.info(sql);
        Connection connection;
        try {
            connection = DriverManager.getConnection(connectionString);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String tableName, String[] columns, String[] values) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(connectionString);
            String[] whereString = new String[columns.length];
            for (int i=0; i<columns.length; i++) {
                whereString[i] = columns[i] + "='" + values[i] + "'";
            }
            String sql = String.format("DELETE FROM %s " +
                            "WHERE " + String.join(" AND ",whereString),
                    tableName);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            logger.info("Error deleting workshop from database:\n{}", e);
            return false;
        }
    }

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

    @Override
    public String selectString(String tableName, String column, String where) {
        List<Object>  objectList = select(tableName, new String[]{column}, where, "", true);
        String ret;
        Object o = objectList.get(0);
        HashMap<String, Object> object = (HashMap<String, Object>) o;
        ret = object.get(column).toString();
        return ret;
    }

    @Override
    public String[] selectStringArray(String tableName, String column, String where, String orderby) {
        List<Object>  objectList = select(tableName, new String[]{column}, where, orderby, true);
        String[] ret = new String[objectList.size()];
        for (Object o: objectList) {
            HashMap<String, Object> object = (HashMap<String, Object>) o;
            ret[objectList.indexOf(o)] = object.get(column).toString();
        }
        return ret;
    }


    @Override
    public HashMap<String, Object> selectStringArray(String tableName, String[] columns, String where) {
        List<Object>  objectList = select(tableName, columns, where, "", true);
        Object o = objectList.get(0);

        return (HashMap<String, Object>) o;
    }


}
