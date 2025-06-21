package uk.ac.ncl.dwa.tests;

import org.junit.jupiter.api.*;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.database.DBHandlerMysql;
import uk.ac.ncl.dwa.model.Setting;
import uk.ac.ncl.dwa.model.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static uk.ac.ncl.dwa.controller.Utilities.createPropertiesFile;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SettingsTest {
    Settings settings;
    private Properties properties;
    static String dbServer;
    static int dbPort;
    static String dbName;
    static String dbUser;
    static String dbPass;
    static String conn;
    static String connectionString;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        DBHandler dbHandler;

        properties = createPropertiesFile();
        dbServer = properties.getProperty("dbServer");
        dbPort = Integer.parseInt(properties.getProperty("dbPort"));
        dbName = properties.getProperty("dbName");
        dbUser = properties.getProperty("dbUser");
        dbPass = properties.getProperty("dbPass");
        conn = properties.getProperty("connectionString");
        connectionString = String.format(conn,
                dbServer, dbPort, dbName, dbUser, dbPass);
        if (properties.getProperty("dbType").equals("mysql")) {
            dbHandler = new DBHandlerMysql(connectionString);
            System.out.println("Setting up MySQL");
        } else {
            System.out.println("Database server not specified in properties file");
            System.exit(-1);
        }
        settings = new Settings();
    }

    @org.junit.jupiter.api.Test
    void getColumnCount() {
        assertEquals(2, settings.getColumnCount());
    }

    @org.junit.jupiter.api.Test
    void getColumnName() {
        assertEquals("value", settings.getColumnName(1));
    }

    @org.junit.jupiter.api.Test
    void getColumnNames() {
        assertEquals("keyValue", settings.getColumnNames()[0]);
        assertEquals("value", settings.getColumnNames()[1]);
    }

    @Order (2)
    @org.junit.jupiter.api.Test
    void deleteSetting() {
        settings.remove("testkey");
        List<Object> returnSetting = settings.selectSetting("value='testValue'");
        assertEquals(0, returnSetting.size());
    }

    @Order (1)
    @org.junit.jupiter.api.Test
    void insertSetting() {
        settings.insertSetting(new Setting("testkey","testValue",'s'));
        List<Object> returnSetting = settings.selectSetting("value='testValue'");
        assertEquals(1, returnSetting.size());
        settings.remove("testkey");
        returnSetting = settings.selectSetting("value='testValue'");
        assertEquals(0, returnSetting.size());
    }

    @org.junit.jupiter.api.Test
    void updateSettings() {
        // Insert a setting
        settings.insertSetting(new Setting("testkey","testValue", 's'));
        // Find the inserted record
        List<Object> returnSetting = settings.selectSetting("value='testValue'");
        // Check that only one record with that key was found
        assertEquals(1, returnSetting.size());
        // Update the value of that setting
        settings.updateSettings("testkey", "newvalue");
        settings.remove("testkey");
        returnSetting = settings.selectSetting("value='newvalue'");
        assertEquals(0, returnSetting.size());   }

}