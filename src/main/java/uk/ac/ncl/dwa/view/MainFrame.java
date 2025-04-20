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
import java.util.ArrayList;

public class MainFrame extends JFrame implements ActionListener, WindowListener {
    Logger logger = LoggerFactory.getLogger(MainFrame.class);
    Globals globals = Globals.getInstance();

    public MainFrame() {
        this.setTitle("Desperado Workshop Admin");
        this.setLayout(new BorderLayout());

        // Create WorkshopTableModel
        MainTable mainTable = new MainTable();

        JScrollPane scrollPane = new JScrollPane(mainTable);
        // Create scroll bars
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        //  enable horizontal scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        //  enable vertical scrolling
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the frame
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Workshops");
        JButton button = new JButton();
        button.setText("Save");
        button.addActionListener(this);
        panel.add(label);
        panel.add(button);

        // Add panel and scroll pane to the frame
        this.add(panel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        // Frame settings
        setSize(2048, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(this);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        if (e.getActionCommand().equals("Save")) {
            // Save action
            logger.debug("Save button clicked");
            // Implement save functionality here
        } else {
            logger.debug("Unknown action command: " + e.getActionCommand());
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        logger.info("Is dirty: " + globals.getDirty());
        if (!globals.getDirty()) {
            logger.debug("Window closing");
            dispose();
        } else {
            logger.debug("Window closing cancelled");

            var yesOrNo = JOptionPane.showConfirmDialog(this, "Would you like to save your changes?");
            if (yesOrNo == 0) {
                logger.info("Saving changes");
                globals.getDirtyRows().forEach(row -> {
                    //globals.getWorkshops().get(row).save(globals.getConnectionString());
                    globals.getWorkshops().updateWorkshops(globals.getConnectionString());
                });
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
}
