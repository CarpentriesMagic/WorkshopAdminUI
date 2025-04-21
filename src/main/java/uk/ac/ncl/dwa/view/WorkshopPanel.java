package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WorkshopPanel extends JPanel implements java.awt.event.ActionListener {
    Logger logger = LoggerFactory.getLogger(WorkshopPanel.class);

    public WorkshopPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create WorkshopTableModel
        WorkshopTable workshopTable = new WorkshopTable();

        JScrollPane scrollPane = new JScrollPane(workshopTable);
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        if (e.getActionCommand().equals("Save")) {
            // Save action
            logger.debug("Save button clicked");
            logger.info("Saving changes");
            Globals globals = Globals.getInstance();
            globals.getWorkshops().updateWorkshops(globals.getConnectionString());
            globals.setDirty(false);
            logger.info("Set dirty to " + globals.getDirty());
        } else {
            logger.debug("Unknown action command: " + e.getActionCommand());
        }
    }
}
