import org.apache.commons.cli.*;
import org.slf4j.Logger;
import uk.ac.ncl.dwa.view.WorkshopAdmin;

public class Main {
    static String dbServer = "192.168.0.228";
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
    static String connectionString =String.format("jdbc:mariadb://%s:%d/%s?user=%s&password=%s",
            dbServer, dbPort, dbName, dbUser, dbPass);
    static Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("g", "gui", false, "Run GUI");
        CommandLineParser parser = new DefaultParser();
        logger.trace("Running GUI");
        try {
            CommandLine cmd = parser.parse(options, args);
            WorkshopAdmin workshopAdmin = new WorkshopAdmin(connectionString);
            if (cmd.hasOption("g")) {
                workshopAdmin.runGUI();
            } else {
                workshopAdmin.noGUI();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
