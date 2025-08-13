package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        setLayout(new MigLayout("", "[50%][50%]", "[fill][fill]"));

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
        JButton btn_save = new JButton("Save");
        JButton btn_add = new JButton("Add");
        JButton btn_del = new JButton("Delete");
        JButton btn_import = new JButton("Import");
        JButton btn_reload = new JButton("Refresh");
        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        btn_import.addActionListener(this);
        btn_reload.addActionListener(this);
        buttonPanel.add(btn_save);
        buttonPanel.add(btn_add);
        buttonPanel.add(btn_del);
        buttonPanel.add(btn_import);
        buttonPanel.add(btn_reload);

        // Add panel and scroll pane to the frame
        this.add(buttonPanel, "wrap");
        this.add(splitPane, "span, grow, push, wrap");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info(e.getActionCommand());
        Workshops workshops = workshopTable.getModel().getWorkshops();
        switch (e.getActionCommand()) {
            case "Save" -> {
                workshops.forEach(workshop -> {
                    logger.info(workshop.getSlug());
                    if (workshop.getSlug() == null || workshop.getSlug().equals("")) {
                        JOptionPane.showMessageDialog(this, "Record with empty slug could not be saved. Fix and save again. Other records should all be saved.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        switch (workshop.getStatus()) {
                            case 'u':
                                if (!workshops.updateWorkshop(workshop)) {
                                    JOptionPane.showMessageDialog(this, "Something went wrong. The workshop couldn't be saved.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                break;
                            case 'n':
                                if (!workshops.insertWorkshop(workshop)) {
                                    JOptionPane.showMessageDialog(this, "This is a duplicate entry. Please correct before trying to save again.", "Error", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    workshop.setStatus('s');
                                    workshop.setKey(workshop.getSlug());
                                }
                        }
                    }
                });
            }
            case "Add" -> {
                logger.info("Adding new workshop number {}",workshops.size());
                workshops.add(workshops.size(), new Workshop());
            }
            case "Delete" -> {
                int row = workshopTable.getSelectedRow();
                if (row != -1) {
                    if (workshops.remove(row) == null) {
                        JOptionPane.showMessageDialog(this, "The workshop could not be removed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No row selected");
                }
            }
            case "Import" -> {
                workshopTable.getModel().getWorkshops().importWorkshopFromCSV("workshops.tsv");
                workshopTable.getModel().getWorkshops().clear();
                workshopTable.getModel().getWorkshops().loadFromDatabase();

            }
            case "Refresh" -> {
                workshopTable.loadCarpentries();
                workshopTable.loadCurricula();
                workshopTable.loadRooms();
                workshopTable.loadFlavours();
                workshopTable.loadSchedules();
            }
            default -> logger.debug("Unknown action command: " + e.getActionCommand());
        }
        workshopTable.repaint();
    }
}
