import org.apache.commons.cli.*;
import org.slf4j.Logger;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.database.DBHandlerMysql;
import uk.ac.ncl.dwa.database.DBHandlerSQLite;
import uk.ac.ncl.dwa.view.WorkshopAdmin;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static uk.ac.ncl.dwa.controller.Utilities.createPropertiesFile;

public class Main {
    static String dbServer;
    static int dbPort;
    static String dbName;
    static String dbUser;
    static String dbPass;
    static String dbType;
    static String conn;
    static String connectionString;

    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        String sourceFilename ="logging.properties";
        System.setProperty("java.util.logging.config.file", sourceFilename);

    }
    static Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String BACKING_STORE_AVAIL = "BackingStoreAvail";

    private static boolean backingStoreAvailable() {
        Preferences prefs = Preferences.userRoot().node("<temporary>");
        try {
            boolean oldValue = prefs.getBoolean(BACKING_STORE_AVAIL, false);
            prefs.putBoolean(BACKING_STORE_AVAIL, !oldValue);
            prefs.flush();
        } catch(BackingStoreException e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Backing store: " + backingStoreAvailable());
        System.setProperty("sun.awt.backingStore", "NotUseful");
        DBHandler dbHandler;

        Properties properties = createPropertiesFile();
        dbName = properties.getProperty("dbName");
        dbPass = properties.getProperty("dbPass");
        dbPort = Integer.parseInt(properties.getProperty("dbPort"));
        dbServer = properties.getProperty("dbServer");
        dbUser = properties.getProperty("dbUser");
        conn = properties.getProperty("connectionString");
        dbType = properties.getProperty("dbType");

        connectionString = String.format(conn, dbType,
                dbServer, dbPort, dbName, dbUser, dbPass);
        if (properties.getProperty("dbType").equals("mariadb") || properties.getProperty("dbType").equals("mysql")) {
            dbHandler = new DBHandlerMysql(connectionString);
        } else if (properties.getProperty("dbType").equals("sqlite")) {
            dbHandler = new DBHandlerSQLite(connectionString);
        } else {
            logger.error("Database server type is not specified in properties file");
            System.exit(-1);
        }

//        globals = Globals.getInstance();
//        globals.setConnectionString(connectionString);
        // Create properties file
//        globals.setDirty(false);
//        logger.info("Set dirty to {}", globals.getDirty());
        String sourceFilename = "logging.properties";
        if (!Files.exists(Paths.get(sourceFilename))) {
            System.out.println("Logging configuration file " + sourceFilename + " not found");
            System.exit(1);
        } else {
            logger.info("Logging model output to {}", sourceFilename);
            Options options = new Options();
            options.addOption("g", "gui", false, "Run GUI");
            CommandLineParser parser = new DefaultParser();
            try {
                CommandLine cmd = parser.parse(options, args);
                WorkshopAdmin workshopAdmin = new WorkshopAdmin();
                logger.info("Running GUI");
                workshopAdmin.runGUI();
                /**
                if (cmd.hasOption("g")) {
                    logger.info("Running GUI");
                    workshopAdmin.runGUI();
                } else {
                    logger.info("Running GUI");
                    workshopAdmin.runGUI();
                }
                 **/
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
