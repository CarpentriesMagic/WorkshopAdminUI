package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelperPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(HelperPanel.class);
    HelperTable helperTable = new HelperTable();

    public HelperPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(helperTable);
        // Create scroll bars
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        //  enable horizontal scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        //  enable vertical scrolling
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the frame
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
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
        this.add(panel);
        this.add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        Globals globals = Globals.getInstance();
        switch (e.getActionCommand()) {
            case "Save" -> {
                // Save action
                logger.info("Saving changes");
                boolean inserted;
                boolean updated;
                if (!globals.getEditedRows("helpers").isEmpty()) {
                    logger.info("There are helpers to update");
                    updated = globals.getHelpers().updateHelpers();
                    if (!updated) {
                        JOptionPane.showMessageDialog(this, "Error updating helpers");
//                        throw new RuntimeException("Error updating helpers from database");
                    } else {
                        logger.debug("Clear dirty flag");
                        globals.setDirty(false);
                    }
                }
                if (!globals.getInsertedRows("helpers").isEmpty()) {
                    logger.info("There are helpers to insert");
                    inserted = globals.getHelpers().insertHelpers();
                    if (!inserted) {
                        JOptionPane.showMessageDialog(this, "Error inserting helpers");
                    } else {
                        logger.debug("Helpers inserted");
                        globals.getInsertedRows("helpers").clear();
                    }
                }
            }
            case "Add" -> {
                // Add action
                logger.info("Adding new Helper");
                Globals.getInstance().getHelpers().add(new Helper());
                logger.debug("Setting dirty to true");
                globals.setDirty(true);
                globals.getInsertedRows("helpers").add(globals.getHelpers().size() - 1);
                helperTable.repaint();
            }
            case "Delete" -> {
                // Delete action
                int row = helperTable.getSelectedRow();
                if (row != -1) {
                    String person_id = Globals.getInstance().getHelpers().get(row).getPerson_id();
                    String slug = Globals.getInstance().getHelpers().get(row).getSlug();
                    if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?") == JOptionPane.YES_OPTION) {
                        globals.getHelpers().remove(row);
                        logger.info("Deleting selected helper");
                        globals.getInsertedRows("helpers").remove(row);
                        globals.getEditedRows("helpers").remove(row);
                        globals.getHelpers().deleteHelper(person_id, slug);
                        helperTable.repaint();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No row selected");
                }
            }
        }
    }

}
