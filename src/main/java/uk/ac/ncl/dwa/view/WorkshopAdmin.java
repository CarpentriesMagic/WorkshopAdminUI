package uk.ac.ncl.dwa.view;

import uk.ac.ncl.dwa.controller.Globals;

public class WorkshopAdmin {
    Globals globals = Globals.getInstance();

    public WorkshopAdmin() {
    }

    public void runGUI(){
        new MainFrame();
    }


}

