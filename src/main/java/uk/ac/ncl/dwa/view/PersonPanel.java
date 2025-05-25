package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.Person;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(PersonPanel.class);
    PersonTable personTable = new PersonTable();

    public PersonPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(personTable);
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
                boolean success2 = true;
                if (!globals.getEditedRows("people").isEmpty()) {
                    logger.info("There are people to insert");
                    success2 = globals.getPeople().insertPerson();
                    if (!success2) {
                        JOptionPane.showMessageDialog(this, "Error updating people");
                        throw new RuntimeException("Error updating people from database");
                    } else {
                        logger.debug("Clear dirty flag");
                        logger.debug("People inserted");
                        globals.getInsertedRows("people").clear();
                    }
                }
            }
            case "Add" -> {
                // Add action
                logger.info("Adding a new Person");
                Globals.getInstance().getPeople().add(new Person());
                logger.debug("Setting dirty to true");
                globals.setDirty(true);
                globals.getInsertedRows("people").add(globals.getPeople().size() - 1);
                personTable.repaint();
            }
            case "Delete" -> {
                // Delete action
                int row = personTable.getSelectedRow();
                if (row != -1) {
                    String person_id = Globals.getInstance().getPeople().get(row).getPerson_id();
                    String slug = Globals.getInstance().getPeople().get(row).getTitle();
                    if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?") == JOptionPane.YES_OPTION) {
                        logger.info("Deleting selected person");
                        if (globals.getPeople().deletePerson(person_id, slug)) {
                            globals.getPeople().remove(row);
                            globals.getInsertedRows("people").remove(row);
                            globals.getEditedRows("people").remove(row);
                            personTable.repaint();
                        } else {
                            JOptionPane.showMessageDialog(this, "The person could not be deleted");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No row selected");
                }
            }
        }
    }

}
