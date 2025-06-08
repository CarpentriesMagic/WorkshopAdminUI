package uk.ac.ncl.dwa.database;

import java.sql.*;
import java.util.HashMap;

public class DBController {
    static private String connectionString;
    private static DBController dbController = null;

    private DBController() {}

    public static DBController getInstance(String connectionString) {
        DBController.connectionString = connectionString;
        if (dbController == null) {
            dbController = new DBController();
        }
        return dbController;
    }


}
