package uk.ac.ncl.dwa.view;

import javax.swing.*;

public class MainTabbedPane extends JTabbedPane {
    public MainTabbedPane() {
        addTab("Workshops", new WorkshopPanel());
    }
}
