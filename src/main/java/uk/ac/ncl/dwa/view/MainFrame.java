package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

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
        setSize(2048, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(this);
        setVisible(true);

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (!globals.getDirty()) {
            logger.debug("Window closing");
            dispose();
        } else {
            logger.debug("Window closing cancelled");

            var yesOrNo = JOptionPane.showConfirmDialog(this, "Would you like to save your changes?");
            if (yesOrNo == 0) {
                logger.info("Saving changes");
                globals.getWorkshops().updateWorkshops(globals.getConnectionString());
            }
            if (yesOrNo == 1) {
                logger.info("Exiting without saving");
            }
            if (yesOrNo == 2) {
                JOptionPane.showMessageDialog(null, "You chose to cancel!");
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
