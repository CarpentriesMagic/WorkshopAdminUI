package uk.ac.ncl.dwa.view;

import org.mariadb.jdbc.Connection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Scanner;

import static uk.ac.ncl.dwa.model.InsertFunctions.insertWorkshop;
import static uk.ac.ncl.dwa.model.Workshops.selected;

public class WorkshopAdmin {
    String connectionString;

    public WorkshopAdmin(String connectionString) {
        this.connectionString = connectionString;
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



    public void noGUI() {
        int itemSelected = menu();
        while (itemSelected != 99) {

            switch (itemSelected) {
                case 1:
                    selected("workshops", new int[]{1, 2}, connectionString);
                    break;
                case 2:
                    selected("people", new int[]{1, 2, 3, 4, 6}, connectionString);
                    break;
                case 6:
                    selected("Room", new int[]{1, 2, 3, 4, 5}, connectionString);
                    break;
                case 7:
                    insertWorkshop(connectionString);
                    break;
            }
            itemSelected = menu();
        }
    }

    public void runGUI(){
        new MainFrame(connectionString);
    }


}

