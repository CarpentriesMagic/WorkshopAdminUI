package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.Settings;
import uk.ac.ncl.dwa.model.Workshop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkshopPanel extends JPanel implements ActionListener {
    private final Logger logger = LoggerFactory.getLogger(WorkshopPanel.class);
    private JTextArea workshopEntryTextArea = new JTextArea("", 640, 480);
    private Settings settings;
    private WorkshopTable workshopTable;

    public WorkshopPanel(Settings settings) {
        super();
        this.settings = settings;
        workshopTable = new WorkshopTable(workshopEntryTextArea, settings);
        setLayout(new MigLayout("","[50%][50%]","[fill][fill]"));

        JPanel workshopTablePanel = new JPanel(new MigLayout("fill", "[]", "[fill]"));
        JPanel workshopEntryPanel = new JPanel(new MigLayout("fill", "[]", "[]"));
        workshopEntryTextArea.setEditable(false);
        JScrollPane workshopTableScrollPane = new JScrollPane(workshopTable);
        JScrollPane workshopEntryScrollPane = new JScrollPane(workshopEntryTextArea);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, workshopTablePanel, workshopEntryPanel);
        splitPane.setResizeWeight(0.5); // Initial divider position at 50%
        splitPane.setContinuousLayout(true); // Smooth dragging
        splitPane.setLeftComponent(workshopTableScrollPane);
        splitPane.setRightComponent(workshopEntryScrollPane);
        workshopTable.setFillsViewportHeight(true);

        // Add components to the frame
        JPanel buttonPanel = new JPanel();
        JButton btn_save = new JButton("Save Workshops");
        JButton btn_add = new JButton("Add Workshop");
        JButton btn_del = new JButton("Delete Workshop");
        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        buttonPanel.add(btn_save);
        buttonPanel.add(btn_add);
        buttonPanel.add(btn_del);

        // Add panel and scroll pane to the frame
        this.add(buttonPanel, "wrap");
        this.add(splitPane, "span, grow, push, wrap");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        Globals globals = Globals.getInstance();
        if (e.getActionCommand().equals("Save Workshops")) {
            // Save action
            logger.info("Saving changes");
            boolean success1 = true;
            boolean success2 = true;
            if (!globals.getEditedRows("workshops").isEmpty()) {
                success1 = globals.getWorkshops().updateWorkshops();
                if (!success1) {
                    JOptionPane.showMessageDialog(this, "Error updating workshops");
                }
            }
            if (!globals.getInsertedRows("workshops").isEmpty()) {
                success2 = globals.getWorkshops().insertWorkshops();
            }
            if (success1 && success2) {
                globals.setDirty(false);
            }
            if (success1) {
                globals.getEditedRows("workshops").clear();
            }
            if (success2) {
                globals.getInsertedRows("workshops").clear();
            }
        } else if (e.getActionCommand().equals("Add Workshop")) {
            // Add action
            logger.info("Adding new workshop");
            Globals.getInstance().getWorkshops().add(new Workshop());
            logger.debug("Setting dirty to true");
            globals.setDirty(true);
            globals.getInsertedRows("workshops").add(globals.getWorkshops().size() - 1);
            workshopTable.repaint();
        } else if (e.getActionCommand().equals("Delete Workshop")) {
            // Delete action
            logger.info("Deleting selected workshop");
            int row = workshopTable.getSelectedRow();
            if (row != -1) {
                String slug = Globals.getInstance().getWorkshops().get(row).getSlug();
                globals.getWorkshops().deleteWorkshop(slug);
                globals.getWorkshops().remove(row);
                globals.getInsertedRows("workshops").remove(row);
                globals.getEditedRows("workshops").remove(row);
                workshopTable.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "No row selected");
            }
        } else {
            logger.debug("Unknown action command: " + e.getActionCommand());
        }
    }
}
