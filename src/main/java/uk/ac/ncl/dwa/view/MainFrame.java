package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame implements ActionListener, WindowListener {
    Logger logger = LoggerFactory.getLogger(MainFrame.class);
    Globals globals = Globals.getInstance();

    public MainFrame() {
        this.setTitle("Desperado Workshop Admin");
        this.setLayout(new BorderLayout());
        add(new MainTabbedPane());

        // Frame settings
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(this);
        setVisible(true);

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (!globals.getDirty() ) {
            logger.debug("Window closing");
            dispose();
        } else {
            logger.debug("Window closing cancelled");

            var yesOrNo = JOptionPane.showConfirmDialog(null,
                    "Would you like to save your changes?", "Save", JOptionPane.YES_NO_OPTION);
            if (yesOrNo == 0) {
//                logger.info("Saving changes");
//                globals.getRooms().insertRooms();
//                globals.getRooms().updateRooms();
//                globals.getWorkshops().insertWorkshops();
//                globals.getWorkshops().updateWorkshops();
//                globals.getInstructors().insertInstructors();
//                globals.getInstructors().updateInstructors();
//                globals.getHelpers().insertHelpers();
//                globals.getHelpers().updateHelpers();

            }
            if (yesOrNo == 1) {
                logger.info("Exiting without saving");
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
