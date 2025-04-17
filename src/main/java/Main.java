import org.apache.commons.cli.*;
import uk.ac.ncl.dwa.WorkshopAdmin;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("g", "gui", false, "Run GUI");
        WorkshopAdmin workshopAdmin = new WorkshopAdmin();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
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
