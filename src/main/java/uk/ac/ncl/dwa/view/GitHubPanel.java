package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static uk.ac.ncl.dwa.controller.github.GitHubActions.*;

public class GitHubPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(getClass().getName());
    JTextArea textArea;
    JComboBox<String> workshopComboBox;
    String[] workshopList;

    public GitHubPanel() {
        super();
        workshopList = DBHandler.getInstance().selectStringArray("workshops", "slug", "");
        workshopComboBox = new JComboBox<>(workshopList);
        setLayout(new MigLayout("", "[50%][50%]", "[fill][fill]"));
        textArea = new JTextArea("");
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
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
        btn_gen.addActionListener(this);
        btn_del.addActionListener(this);
        btn_clone.addActionListener(this);
        buttonPanel.add(workshopComboBox);
        buttonPanel.add(btn_gen);
        buttonPanel.add(btn_del);
        buttonPanel.add(btn_clone);
        this.add(buttonPanel, "wrap");
        this.add(scrollPane, "span, grow, push, wrap");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String organisation = DBHandler.getInstance().selectString("settings",
                "value", "keyValue=\"organisation\"");
        if (workshopComboBox.getSelectedItem() != null) {
            switch (e.getActionCommand()) {
                case "Generate" -> {
                    int ret = createFromTemplate(organisation, workshopComboBox.getSelectedItem().toString());
                    switch (ret) {
                        case 0 -> {
                            JOptionPane.showMessageDialog(this,
                                    "Repository created successfully.", "SUCCESS",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        case 1 -> {
                            JOptionPane.showMessageDialog(this, "Failed to create repository.",
                                    "FAIL", JOptionPane.ERROR_MESSAGE);
                        }
                        case 2 -> {
                            JOptionPane.showMessageDialog(this, "An unknown error occured.",
                                    "FAIL: ", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                case "Delete" -> {
                    int ret = deleteRepository(organisation, workshopComboBox.getSelectedItem().toString());
                    switch (ret) {
                        case 0 -> {
                            JOptionPane.showMessageDialog(this,
                                    "Repository deleted successfully.", "SUCCESS",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        case 1 -> {
                            JOptionPane.showMessageDialog(this, "Failed to delete repository.",
                                    "FAIL", JOptionPane.ERROR_MESSAGE);
                        }
                        case 2 -> {
                            JOptionPane.showMessageDialog(this,
                                    "IOException error. Please check log for more information","FAIL",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                case "Clone" -> {
                    String ret = cloneRepository(organisation, workshopComboBox.getSelectedItem()
                            .toString(), organisation);
                    switch (ret) {

                        case "0" -> {
                            JOptionPane.showMessageDialog(this, "A clone of this repository already exist",
                                    "Clone exists", JOptionPane.ERROR_MESSAGE);

                        }
                        case "1" -> {
                            JOptionPane.showMessageDialog(this, "File index.md was not found", "Cloning failed: ", JOptionPane.ERROR_MESSAGE);
                        }
                        default -> {
                            textArea.setText(ret);
                        }
                    }
                }
            }
        } else {
            JOptionPane.showConfirmDialog(this, "Please select a workshop first\n");

        }
    }

}
