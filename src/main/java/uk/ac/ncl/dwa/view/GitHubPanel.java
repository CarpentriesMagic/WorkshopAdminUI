package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

import static uk.ac.ncl.dwa.controller.github.GitHubActions.*;

public class GitHubPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(getClass().getName());
    //    JTextArea textArea;
    GitHubRepoTable workshops = new GitHubRepoTable();
    String[] workshopList;

    public GitHubPanel() {
        super();
        workshopList = DBHandler.getInstance().selectStringArray("workshops",
                "slug", "");
        setLayout(new MigLayout("", "[50%][50%]", "[fill][fill]"));
        JScrollPane scrollPane = new JScrollPane(workshops);
        // Create scroll bars
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        //  enable horizontal scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        //  enable vertical scrolling
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the frame
        JPanel buttonPanel = new JPanel();
        JButton btn_gen = new JButton("Generate");
        JButton btn_del = new JButton("Delete");
        JButton btn_clone = new JButton("Clone");
        JButton btn_events = new JButton("Events");
        btn_gen.addActionListener(this);
        btn_del.addActionListener(this);
        btn_clone.addActionListener(this);
        btn_events.addActionListener(this);
        buttonPanel.add(btn_gen);
        buttonPanel.add(btn_del);
        buttonPanel.add(btn_clone);
        buttonPanel.add(btn_events);
        this.add(buttonPanel, "wrap");
        this.add(scrollPane, "span, grow, push, wrap");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String organisation = DBHandler.getInstance().selectString("settings",
                "value", "keyValue=\"organisation\"");
        int[] selectedRows = workshops.getSelectedRows();

        if (selectedRows != null) {
            switch (e.getActionCommand()) {
                case "Generate" -> {
                    int rowIndex = selectedRows[0];
                    String ret = createFromTemplate(organisation,
                            (String)workshops.getValueAt(rowIndex, 0));
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
                }
                case "Delete" -> {
                    int rowIndex = selectedRows[0];
                    String ret = deleteRepository(organisation, (String) workshops.getValueAt(rowIndex, 0));
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
                }
                case "Clone" -> {
                    for (int rowIndex : selectedRows) {
                        String ret = cloneRepository(organisation,
                                (String) workshops.getValueAt(rowIndex, 0), organisation);
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
                }
                case "Events" -> {
                    try {
                        PrintWriter writer = new PrintWriter(new FileWriter("events.csv", true));
                        for (int rowIndex : selectedRows) {
                            String date = (String) workshops.getValueAt(rowIndex, 2);
                            String title = (String) workshops.getValueAt(rowIndex, 1);
                            String location = (String) workshops.getValueAt(rowIndex, 6);
                            String file = (String) workshops.getValueAt(rowIndex, 18);
                            Scanner sc = new Scanner(new File("descriptions/" + file + ".txt"));
                            String description = "";
                            while (sc.hasNextLine()) {
                                description += sc.nextLine().trim();
                            }
                            String slug = (String) workshops.getValueAt(rowIndex, 0);
                            writer.println(date + ", Training, " + title + ", " + "\"" + description + "\", " + location
                                    + ", not yet available, " + "https://nclrse-training.github.io/" + slug + "/");
                        }
                        writer.flush();
                        writer.close();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (UnsupportedEncodingException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        } else {
            JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");

        }
    }
}
