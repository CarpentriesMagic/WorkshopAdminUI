package uk.ac.ncl.dwa.view;

import uk.ac.ncl.dwa.model.Settings;

import javax.swing.*;

public class MainTabbedPane extends JTabbedPane {
    Settings settings = new Settings();

    public MainTabbedPane() {
        addTab("Workshops", new WorkshopPanel(settings));
        addTab("Rooms", new RoomPanel());
        addTab("Instructors", new InstructorPanel());
        addTab("Helpers", new HelperPanel());
        addTab("People", new PersonPanel());
        addTab("Settings", new SettingPanel());
    }
}
