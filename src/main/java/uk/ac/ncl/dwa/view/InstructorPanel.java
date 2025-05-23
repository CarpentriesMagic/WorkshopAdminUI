package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.Instructor;
import uk.ac.ncl.dwa.model.Room;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(InstructorPanel.class);
    InstructorTable instructorTable = new InstructorTable();

    public InstructorPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(instructorTable);
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
                boolean success1 = true;
                boolean success2 = true;
                if (!globals.getEditedRows("instructors").isEmpty()) {
                    success1 = globals.getInstructors().updateInstructors();
                    if (!success1) {
                        JOptionPane.showMessageDialog(this, "Error updating instructors");
                        throw new RuntimeException("Error updating instructors from database");
                    }
                }
                if (!globals.getInsertedRows("instructors").isEmpty()) {
                    success2 = globals.getInstructors().insertInstructors();
                }
                if (success2) {
                    globals.setDirty(false);
                }
                globals.getEditedRows("instructors").clear();
                if (success2) {
                    globals.getInsertedRows("instructors").clear();
                }
            }
            case "Add" -> {
                // Add action
                logger.info("Adding new Instructor");
                Globals.getInstance().getInstructors().add(new Instructor());
                logger.debug("Setting dirty to true");
                globals.setDirty(true);
                globals.getInsertedRows("instructors").add(globals.getInstructors().size() - 1);
                instructorTable.repaint();
            }
            case "Delete" -> {
                // Delete action
                logger.info("Deleting selected instructor");
                int row = instructorTable.getSelectedRow();
                if (row != -1) {
                    String person_id = Globals.getInstance().getInstructors().get(row).getPerson_id();
                    String slug = Globals.getInstance().getInstructors().get(row).getSlug();
                    globals.getInstructors().remove(row);
                    globals.getInsertedRows("instructors").remove(row);
                    globals.getEditedRows("instructors").remove(row);
                    globals.getInstructors().deleteInstructor(person_id, slug);
                    instructorTable.repaint();
                    globals.setDirty(true);
                } else {
                    JOptionPane.showMessageDialog(this, "No row selected");
                }
            }
        }
    }

}
