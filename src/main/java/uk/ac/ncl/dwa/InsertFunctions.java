package uk.ac.ncl.dwa;

import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertFunctions {

    public static void insertWorkshop(String connectionString) {
        Connection connection = null;




        try {
            connection = (Connection) DriverManager.getConnection(connectionString);
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
