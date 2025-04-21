import org.apache.commons.cli.*;
import org.slf4j.Logger;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.view.WorkshopAdmin;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    static String dbServer = "localhost";
    static int dbPort = 3306;
    static String dbName = "workshopadmin";
    static String dbUser = "workshopadmin";
    static String dbPass = "w0rksh0p";

    static {
        // must set before the Logger
        // loads logging.properties from the classpath
        String sourceFilename ="logging.properties";
        System.setProperty("java.util.logging.config.file", sourceFilename);

    }
    static Logger logger = LoggerFactory.getLogger(Main.class);
    static Globals globals = Globals.getInstance();
    static String connectionString =String.format("jdbc:mariadb://%s:%d/%s?user=%s&password=%s",
            dbServer, dbPort, dbName, dbUser, dbPass);

    public static void main(String[] args) {
        // Create properties file
        globals.setDirty(false);
        logger.info("Set dirty to " + globals.getDirty());
        globals.setConnectionString(connectionString);
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
