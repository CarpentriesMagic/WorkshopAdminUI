package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.Workshop;
import uk.ac.ncl.dwa.model.Workshops;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WorkshopPanel extends JPanel implements java.awt.event.ActionListener {
    Logger logger = LoggerFactory.getLogger(WorkshopPanel.class);
    WorkshopTable workshopTable = new WorkshopTable();

    public WorkshopPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
        JButton btn_save = new JButton("Save");
        JButton btn_add = new JButton("Add");
        JButton btn_del = new JButton("Delete");
        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        panel.add(btn_save);
        panel.add(btn_add);
        panel.add(btn_del);

        // Add panel and scroll pane to the frame
        this.add(panel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        Globals globals = Globals.getInstance();
        if (e.getActionCommand().equals("Save")) {
            // Save action
            logger.info("Saving changes");
            boolean success1 = true;
            boolean success2 = true;
            if (!globals.getDirtyRows().isEmpty()) {
                success1 = globals.getWorkshops().updateWorkshops();
            }
            if (!globals.getInsertedRows().isEmpty()) {
                success2 = globals.getWorkshops().insertWorkshops();
            }
            if (success1 && success2) {
                globals.setDirty(false);
            }
            if (success1) {
                globals.getDirtyRows().clear();
            }
            if (success2) {
                globals.getInsertedRows().clear();
            }
        } else if (e.getActionCommand().equals("Add")) {
            // Add action
            logger.info("Adding new workshop");
            Globals.getInstance().getWorkshops().add(new Workshop());
            logger.debug("Setting dirty to true");
            globals.setDirty(true);
            globals.getInsertedRows().add(globals.getWorkshops().size() - 1);
            workshopTable.repaint();
        } else if (e.getActionCommand().equals("Delete")) {
            // Delete action
            logger.info("Deleting selected workshop");
            int row = workshopTable.getSelectedRow();
            if (row != -1) {
                String slug = Globals.getInstance().getWorkshops().get(row).getSlug();
                globals.getWorkshops().remove(row);
                globals.getInsertedRows().remove(row);
                globals.getDirtyRows().remove(row);
                globals.getWorkshops().deleteWorkshop(slug);
                workshopTable.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "No row selected");
            }
        } else {
            logger.debug("Unknown action command: " + e.getActionCommand());
        }
    }
}
