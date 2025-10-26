package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.model.Settings;
import uk.ac.ncl.dwa.model.Workshop;
import uk.ac.ncl.dwa.model.Workshops;
import java.awt.Desktop;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import static uk.ac.ncl.dwa.controller.github.GitHubActions.*;

public class WorkshopPanel extends JPanel implements ActionListener, HyperlinkListener {
    private final Logger logger = LoggerFactory.getLogger(WorkshopPanel.class);
    private JTextPane workshopEntryTextArea = new JTextPane();
    private Settings settings;
    private WorkshopTable workshopTable;
    private URL url;
    JScrollPane workshopTableScrollPane;
    JScrollPane workshopEntryScrollPane;
    public WorkshopPanel(Settings settings) {
        super();
        workshopEntryTextArea.setContentType("text/html");
        workshopEntryTextArea.addHyperlinkListener(this);
        this.settings = settings;
        workshopTable = new WorkshopTable(workshopEntryTextArea, settings);
        setLayout(new MigLayout("", "[50%][50%]", "[fill][fill]"));

        JPanel buttonPanel = new JPanel(new MigLayout("", "[50%][50%]", "[fill][fill]"));
        JPanel leftButtonPanel = new JPanel();
        JPanel rightButtonPanel = new JPanel();
        JPanel workshopTablePanel = new JPanel(new MigLayout("fill", "[]", "[fill]"));
        JPanel workshopEntryPanel = new JPanel(new MigLayout("fill", "[]", "[]"));
        workshopEntryTextArea.setEditable(false);
        workshopTableScrollPane = new JScrollPane(workshopTable);
        workshopEntryScrollPane = new JScrollPane(workshopEntryTextArea);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, workshopTablePanel, workshopEntryPanel);
        splitPane.setResizeWeight(0.5); // Initial divider position at 50%
        splitPane.setContinuousLayout(true); // Smooth dragging
        splitPane.setLeftComponent(workshopTableScrollPane);
        splitPane.setRightComponent(workshopEntryScrollPane);
        workshopTable.setFillsViewportHeight(true);

        // Add components to the frame
        JButton btn_save = new JButton("Save");
        JButton btn_add = new JButton("Add");
        JButton btn_del = new JButton("Delete");
        JButton btn_import = new JButton("Import");
        JButton btn_reload = new JButton("Refresh");
        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        btn_import.addActionListener(this);        btn_reload.addActionListener(this);
        btn_save.setToolTipText("Save modifications made to workshops");
        btn_add.setToolTipText("Add a new workshop");
        btn_del.setToolTipText("Delete a selected workshop");
        btn_import.setToolTipText("Import workshop");
        btn_reload.setToolTipText("Reload any changes made to helpers, instructors or rooms");
        buttonPanel.add(leftButtonPanel, "cell 0 0,grow");
        buttonPanel.add(rightButtonPanel, "cell 1 0,grow");
        leftButtonPanel.add(btn_save);
        leftButtonPanel.add(btn_add);
        leftButtonPanel.add(btn_del);
        leftButtonPanel.add(btn_import);
        leftButtonPanel.add(btn_reload);

        JButton btn_gen = new JButton("Generate");
        JButton btn_del2 = new JButton("Delete GitHub Repository");
        JButton btn_clone = new JButton("Clone");
        JButton btn_events = new JButton("Events");
        btn_gen.addActionListener(this);
        btn_del2.addActionListener(this);
        btn_clone.addActionListener(this);
        btn_events.addActionListener(this);
        btn_gen.setToolTipText("Create a new repository using the Carpentries template");
        btn_del.setToolTipText("Delete the selected repository in GitHub");
        btn_clone.setToolTipText("Clone the selected repository in to your local drive, update it and push it back to GitHub");
        btn_events.setToolTipText("Create a CSV file of the selected workshopTable for inclusion in the team website");
        rightButtonPanel.add(btn_gen);
        rightButtonPanel.add(btn_del2);
        rightButtonPanel.add(btn_clone);
        rightButtonPanel.add(btn_events);


        // Add panel and scroll pane to the frame
        this.add(buttonPanel, "wrap");
        this.add(splitPane, "span, grow, push, wrap");
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (Desktop.isDesktopSupported()) { // Check if Desktop API is supported on the current platform
                Desktop desktop = Desktop.getDesktop();
                logger.info("Open URL {}", event.getURL());
                try {
                    desktop.browse(event.getURL().toURI()); // Open the URI in the default browser
                } catch (IOException | URISyntaxException e) {
                    System.err.println("Error opening link: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Desktop API is not supported on this platform.");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info(e.getActionCommand());
        Workshops workshops = workshopTable.getModel().getWorkshops();
        String organisation = DBHandler.getInstance().selectString("settings",
                "value", "keyValue=\"organisation\"");
        int[] selectedRows = workshopTable.getSelectedRows();

        //if (selectedRows != null) {
            switch (e.getActionCommand()) {
                case "Generate" -> {
                    if  (selectedRows != null) {
                        int rowIndex = selectedRows[0];
                        String ret = createFromTemplate(organisation,
                                (String) workshopTable.getValueAt(rowIndex, 0));
                        switch (ret) {
                            case "0" -> {
                                JOptionPane.showMessageDialog(this,
                                        "Repository created successfully.", "SUCCESS",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                            case "1" -> {
                                JOptionPane.showMessageDialog(this, "Failed to create repository.",
                                        "FAIL", JOptionPane.ERROR_MESSAGE);
                            }
                            case "2" -> {
                                JOptionPane.showMessageDialog(this, "An unknown error occured.",
                                        "FAIL: ", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");
                    }
                }
                case "Delete GitHub Repository" -> {
                    if (selectedRows != null) {
                        int rowIndex = selectedRows[0];
                        String ret = deleteRepository(organisation, (String) workshopTable.getValueAt(rowIndex, 0));
                        switch (ret) {
                            case "0" -> {
                                JOptionPane.showMessageDialog(this,
                                        "Repository deleted successfully.", "SUCCESS",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                            case "1" -> {
                                JOptionPane.showMessageDialog(this, "Failed to delete repository.",
                                        "FAIL", JOptionPane.ERROR_MESSAGE);
                            }
                            case "2" -> {
                                JOptionPane.showMessageDialog(this,
                                        "IOException error. Please check log for more information", "FAIL",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");
                    }
                }
                case "Clone" -> {
                    if (selectedRows != null) {
                        for (int rowIndex : selectedRows) {
                            String ret = cloneRepository(organisation,
                                    (String) workshopTable.getValueAt(rowIndex, 0), organisation);
                            switch (ret) {

                                case "1" -> {
                                    JOptionPane.showMessageDialog(this, "A clone of this repository already exist",
                                            "Clone exists", JOptionPane.ERROR_MESSAGE);
                                }
                                case "2" -> {
                                    JOptionPane.showMessageDialog(this, "File index.md was not found", "Cloning failed: ", JOptionPane.ERROR_MESSAGE);
                                }
                                case "3" -> {
                                    JOptionPane.showMessageDialog(this, "Could not copy schedule file.");
                                }
                                case "0" -> {
                                    //textArea.setText(ret);
                                }
                            }
                        }
                    } else {
                        JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");
                    }
                }
                case "Events" -> {
                    if (selectedRows != null) {
                        try {
                            PrintWriter writer = new PrintWriter(new FileWriter("events.csv", true));
                            for (int rowIndex : selectedRows) {
                                String date = (String) workshopTable.getValueAt(rowIndex, 2);
                                String title = (String) workshopTable.getValueAt(rowIndex, 1);
                                String location = (String) workshopTable.getValueAt(rowIndex, 6);
                                String file = (String) workshopTable.getValueAt(rowIndex, 18);
                                Scanner sc = new Scanner(new File("descriptions/" + file + ".txt"));
                                String description = "";
                                while (sc.hasNextLine()) {
                                    description += sc.nextLine().trim();
                                }
                                String slug = (String) workshopTable.getValueAt(rowIndex, 0);
                                writer.println(date + ", Training, " + title + ", " + "\"" + description + "\", " + location
                                        + ", not yet available, " + "https://nclrse-training.github.io/" + slug + "/");
                            }
                            writer.flush();
                            writer.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");
                    }
                }
                case "Delete" -> {
                    if (selectedRows != null) {
                        int row = workshopTable.getSelectedRow();
                        if (row != -1) {
                            logger.info("Delete row {}", row);
                            if (workshops.remove(row) == null) {
                                JOptionPane.showMessageDialog(this, "The workshop could not be removed.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "No row selected");
                        }
                    } else {
                        JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");
                    }
                }
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
                default -> {
                    JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");
                    //logger.debug("Unknown action command: " + e.getActionCommand());
                }
            }
        // Ensure the table scrolls to and focuses on the last row
        SwingUtilities.invokeLater(() -> {
            int lastRow = workshopTable.getRowCount() - 1;
            if (lastRow >= 0) {
                workshopTable.setRowSelectionInterval(lastRow, lastRow);
                workshopTable.scrollRectToVisible(workshopTable.getCellRect(lastRow, 0, true));
                workshopTable.requestFocusInWindow();
            }

            // Make sure the scrollbars are scrolled all the way to the bottom
            JScrollBar vertical = workshopTableScrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
        workshopTable.revalidate();
        workshopTable.repaint();
        //}
    }
}

