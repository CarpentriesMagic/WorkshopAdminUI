package uk.ac.ncl.dwa.model;

import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Statement;
import uk.ac.ncl.dwa.controller.Globals;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertFunctions {
    private static Globals globals = Globals.getInstance();

    public static void insertWorkshop() {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(globals.getConnectionString());
            Statement stmt = connection.createStatement();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press any Enter to continue...");
        scanner.nextLine();
    }
}
