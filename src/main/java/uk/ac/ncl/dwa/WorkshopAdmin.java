package uk.ac.ncl.dwa;

import org.mariadb.jdbc.Connection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Scanner;

import static uk.ac.ncl.dwa.InsertFunctions.insertWorkshop;

public class WorkshopAdmin {
    static String dbServer = "192.168.0.153";
    static int dbPort = 3306;
    static String dbName = "workshopadmin";
    static String dbUser = "jannetta";
    static String dbPass = "j0nn3tt0";

    static String connectionString =String.format("jdbc:mariadb://%s:%d/%s?user=%s&password=%s",
    dbServer, dbPort, dbName, dbUser, dbPass);

    public WorkshopAdmin() {

    }

    private static int menu() {
        System.out.println("Welcome to the Desperado Workshop Admin");
        System.out.println(" 1. Display workshops               7. Insert a new workshop");
        System.out.println(" 2. Display people                  8. Insert a new person");
        System.out.println(" 3. Display Carpentry code");
        System.out.println(" 4. Display curricula");
        System.out.println(" 5. Display curricula flavours");
        System.out.println(" 6. Display rooms");
        System.out.println("99. Exit");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select an option: ");
        return scanner.nextInt();
    }


     private static void selected(String table, int[] columns) {

        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(connectionString);
            String sql = "SELECT * FROM " + table;
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            String val;
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                for (int i = 0; i < columns.length; i++) {
                    val = resultSet.getString(columns[i]);
                    result.append(" - ").append(val);
                }
                System.out.println(result);
                val = "";
                result = new StringBuilder();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press any Enter to continue...");
        scanner.nextLine();
    }

    public void noGUI() {
        int selected = menu();
        while (selected != 99) {

            switch (selected) {
                case 1:
                    selected("workshops", new int[]{1, 2});
                    break;
                case 2:
                    selected("people", new int[]{1, 2, 3, 4, 6});
                    break;
                case 6:
                    selected("room", new int[]{1, 2, 3, 4, 5});
                    break;
                case 7:
                    insertWorkshop(connectionString);
                    break;
            }
            selected = menu();
        }
    }

    public void runGUI(){
        JFrame frame = new JFrame("Desperado Workshop Admin");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("JFrame By Example");
        JButton button = new JButton();
        button.setText("Button");
        panel.add(label);
        panel.add(button);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}

