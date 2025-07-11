package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.Settings;
import uk.ac.ncl.dwa.model.Workshop;
import uk.ac.ncl.dwa.model.Workshops;

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
        JButton btn_save = new JButton("Save ");
        JButton btn_add = new JButton("Add");
        JButton btn_del = new JButton("Delete");
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
        Workshops workshops = workshopTable.getModel().getWorkshops();
        switch (e.getActionCommand()) {
            case "Save" -> {
                workshops.forEach((workshop) -> {
                    switch (workshop.getStatus()) {
                        case 'u':
                            if (workshops.updateWorkshop(workshop)) {
                                workshop.setKey(workshop.getSlug());
                            } else {
                                JOptionPane.showMessageDialog(this, "This is a duplicate entry. Please correct before trying to save again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 'n':
                            workshop.setStatus('s');
                            if (workshops.insertWorkshop(workshop)) {
                                workshop.setKey(workshop.getSlug());
                            } else {
                                JOptionPane.showMessageDialog(this, "This is a duplicate entry. Please correct before trying to save again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                    }
                });

            }
            case "Add" -> {
                workshops.add(workshops.size(), new Workshop());
            }
            case "Delete" -> {
                int row = workshopTable.getSelectedRow();
                if (row != 1) {
                    if (workshops.remove(row) == null) {
                        JOptionPane.showMessageDialog(this, "Record could not be deleted. It may not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        logger.debug("Deleted workshop at row: " + row);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No row selected");
                }
            }
            default -> logger.debug("Unknown action command: " + e.getActionCommand());
        }
    }
}
