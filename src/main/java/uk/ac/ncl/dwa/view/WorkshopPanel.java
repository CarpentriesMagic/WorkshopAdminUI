package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.HackmdCreateNote;
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
import java.util.Arrays;
import java.util.Scanner;
import static uk.ac.ncl.dwa.controller.github.GitHubActions.*;

public class WorkshopPanel extends JPanel implements ActionListener, HyperlinkListener {
    private final Logger logger = LoggerFactory.getLogger(WorkshopPanel.class);
    private URL url;
    private JTextPane workshopEntryTextArea = new JTextPane();
    private WorkshopTable workshopTable;
    private JScrollPane workshopTableScrollPane;
    private JScrollPane workshopEntryScrollPane;
    private JTextField tf_filterFrom = new JTextField("", 10);
    private JTextField tf_filterTo = new JTextField("", 10);

    public WorkshopPanel(Settings settings) {
        super();
        workshopEntryTextArea.setContentType("text/html");
        workshopEntryTextArea.addHyperlinkListener(this);
        workshopTable = new WorkshopTable(workshopEntryTextArea, settings);
        setLayout(new MigLayout("", "[50%][50%]", "[fill][fill]"));

        JPanel leftButtonPanel = new JPanel();
        JPanel rightButtonPanel = new JPanel();
        JPanel filterPanel = new JPanel();
        JPanel collabPanel = new JPanel();
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
        JButton btn_refresh = new JButton("Refresh");
        JButton btn_gen = new JButton("Generate");
        JButton btn_del2 = new JButton("Delete GitHub Repository");
        JButton btn_del3 = new JButton("Delete Local Clone");
        JButton btn_clone = new JButton("Clone");
        JButton btn_events = new JButton("Events");
        JButton btn_filter = new JButton("Filter");
        JButton btn_collab = new JButton("Collab Doc");

        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        btn_import.addActionListener(this);
        btn_refresh.addActionListener(this);
        btn_gen.addActionListener(this);
        btn_del2.addActionListener(this);
        btn_del3.addActionListener(this);
        btn_clone.addActionListener(this);
        btn_events.addActionListener(this);
        btn_filter.addActionListener(this);
        btn_collab.addActionListener(this);

        btn_save.setToolTipText("Save modifications made to workshops");
        btn_add.setToolTipText("Add a new workshop");
        btn_del.setToolTipText("Delete a selected workshop");
        btn_import.setToolTipText("Import workshop");
        btn_refresh.setToolTipText("Reload any changes made to helpers, instructors or rooms");
        btn_gen.setToolTipText("Create a new repository using the Carpentries template");
        btn_del2.setToolTipText("Delete the selected repository in GitHub");
        btn_del3.setToolTipText("Delete the local clone of the selected repository");
        btn_clone.setToolTipText("Clone the selected repository in to your local drive, update it and push it back to GitHub");
        btn_events.setToolTipText("Create a CSV file of the selected workshopTable for inclusion in the team website");
        btn_filter.setToolTipText("Filter the workshops to only display those between specified dates.");
        btn_collab.setToolTipText("Create collaborative HackMD document");

        leftButtonPanel.add(btn_save);
        leftButtonPanel.add(btn_add);
        leftButtonPanel.add(btn_del);
        leftButtonPanel.add(btn_import);
        leftButtonPanel.add(btn_refresh);
        rightButtonPanel.add(btn_del2);
        rightButtonPanel.add(btn_gen);
        rightButtonPanel.add(btn_del3);
        rightButtonPanel.add(btn_clone);

        filterPanel.add(new JLabel("From:"));
        filterPanel.add(tf_filterFrom);
        filterPanel.add(new JLabel("To:"));
        filterPanel.add(tf_filterTo);
        filterPanel.add(btn_filter);
        collabPanel.add(btn_collab);
        collabPanel.add(btn_events);

        // Add panel and scroll pane to the frame
        this.add(leftButtonPanel, "");
        this.add(rightButtonPanel, "wrap");
        this.add(filterPanel);
        this.add(collabPanel, "wrap");
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
                                JOptionPane.showMessageDialog(this, "An unknown error occurred.",
                                        "FAIL: ", JOptionPane.ERROR_MESSAGE);
                            }
                            case "3" -> {
                                JOptionPane.showMessageDialog(this, "The repository was created but we couldn't<br/> update the homepage link", "FAIL",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");
                    }
                }
                case "Delete Local Clone" -> {
                    if (selectedRows != null) {
                        int rowIndex = selectedRows[0];
                        String slug = (String) workshopTable.getValueAt(rowIndex, 0);
                        File file = FileUtils.getFile(slug);

                        try {
                            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to " +
                                    "delete the local clone of " + slug + "?\n", "Delete",
                                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                FileUtils.deleteDirectory(file);
                                JOptionPane.showMessageDialog(this, "Local clone of " + slug + " deleted or it didn't exist\n", "Delete", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(this, "Failed to delete local clone of " + slug + "\n", "Delete", JOptionPane.ERROR_MESSAGE);
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
                                    JOptionPane.showMessageDialog(this,
                                            "A clone of this repository already exist",
                                            "Clone exists", JOptionPane.ERROR_MESSAGE);
                                }
                                case "2" -> {
                                    JOptionPane.showMessageDialog(this,
                                            "File index.md was not found",
                                            "Cloning failed: ", JOptionPane.ERROR_MESSAGE);
                                }
                                case "3" -> {
                                    JOptionPane.showMessageDialog(this,
                                            "Could not copy schedule file.",
                                            "Error",  JOptionPane.ERROR_MESSAGE);
                                }
                                case "0" -> {
                                    //textArea.setText(ret);

                                    Object[] options = { "OK" };
                                    JOptionPane.showOptionDialog(
                                            null,
                                            "Workshops website appears to have been cloned successfully.",
                                            "Success",
                                            JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE,
                                            null,
                                            options,
                                            options[0]
                                    );
                                }
                            }
                        }
                    } else {
                        JOptionPane.showConfirmDialog(this,
                                "Please select a workshop first\n",
                                "Error",  JOptionPane.ERROR_MESSAGE);
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
                    lastRow(workshopTable, workshopTableScrollPane);
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
                case "Filter" -> {
                    String startdate = tf_filterFrom.getText();
                    String enddate = tf_filterTo.getText();
                    workshopTable.getModel().getWorkshops().loadFromDatabase(startdate, enddate);
                    workshopTable.getModel().fireTableDataChanged();
                }
                case "Collab Doc" -> {
                    if (selectedRows != null) {
                        int row = workshopTable.getSelectedRow();
                        if (row != -1) {
                            logger.info("Create HackMD for {} workshop with slug: {}", workshops.get(row).getSchedule(), workshops.get(row).getSlug());
                            int ret = HackmdCreateNote.createDoc(workshops.get(row).getSchedule(),
                                    workshops.get(row).getSlug());
                            switch (ret) {
                                case 0 -> {JOptionPane.showMessageDialog(this,
                                        "Document created successfully");}
                                case 1 -> {JOptionPane.showMessageDialog(this,
                                        "Missing HackMD API Token", "Error", JOptionPane.ERROR_MESSAGE);}
                                case 2 -> {JOptionPane.showMessageDialog(this,
                                        "❌ Failed to create note. See error log for more information",
                                        "Error", JOptionPane.ERROR_MESSAGE);}
                                case 3 -> {JOptionPane.showMessageDialog(this,
                                        "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);}
                                case 4 -> {JOptionPane.showMessageDialog(this,
                                        "The template file does not exist", "Error", JOptionPane.ERROR_MESSAGE);}
                            }
                        }
                    }
                }
                default -> {
                    JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");
                    //logger.debug("Unknown action command: " + e.getActionCommand());
                }
            }

        workshopTable.revalidate();
        workshopTable.repaint();
        //}
    }

    private void lastRow(JTable table, JScrollPane scrollPane) {
        // Ensure the table scrolls to and focuses on the last row
        SwingUtilities.invokeLater(() -> {
            int lastRow = table.getRowCount() - 1;
            if (lastRow >= 0) {
                table.setRowSelectionInterval(lastRow, lastRow);
                table.scrollRectToVisible(table.getCellRect(lastRow, 0, true));
                table.requestFocusInWindow();
            }

            // Make sure the scrollbars are scrolled all the way to the bottom
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
}

