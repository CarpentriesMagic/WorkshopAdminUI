package uk.ac.ncl.dwa.view;

import javax.swing.*;

public class MainTabbedPane extends JTabbedPane {
    public MainTabbedPane() {
        addTab("Workshops", new WorkshopPanel());
        addTab("Rooms", new RoomPanel());
        addTab("Instructors", new InstructorPanel());
    }
}
