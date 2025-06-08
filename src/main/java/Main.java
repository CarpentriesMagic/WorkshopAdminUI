import org.apache.commons.cli.*;
import org.slf4j.Logger;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.database.DBHandlerMysql;
import uk.ac.ncl.dwa.view.WorkshopAdmin;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static uk.ac.ncl.dwa.controller.Utilities.createPropertiesFile;

public class Main {
    private static Properties properties;
    static String dbServer;
    static int dbPort;
    static String dbName;
    static String dbUser;
    static String dbPass;
    static String conn;

    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        String sourceFilename ="logging.properties";
        System.setProperty("java.util.logging.config.file", sourceFilename);

    }
    static Logger logger = LoggerFactory.getLogger(Main.class);
    static Globals globals;
    static String connectionString;

    public static void main(String[] args) {
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
        } else {
            logger.error("Database server not specified in properties file");
            System.exit(-1);
        }



        globals = Globals.getInstance();
        globals.setConnectionString(connectionString);
        // Create properties file
        globals.setDirty(false);
        logger.info("Set dirty to {}", globals.getDirty());
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
                if (cmd.hasOption("g")) {
                    logger.info("Running GUI");
                    workshopAdmin.runGUI();
                } else {
                    logger.info("Running TUI");
                    workshopAdmin.noGUI();
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
