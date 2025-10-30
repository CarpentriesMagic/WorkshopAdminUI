package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.Instructor;
import uk.ac.ncl.dwa.model.Instructors;
import javax.swing.*;
import static uk.ac.ncl.dwa.view.FocusUtils.lastRow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(InstructorPanel.class);
    InstructorTable instructorTable = new InstructorTable();
    JScrollPane scrollPane = new JScrollPane(instructorTable);

    public InstructorPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
        JButton btn_refresh = new JButton("Refresh");
        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        btn_refresh.addActionListener(this);
        panel.add(btn_save);
        panel.add(btn_add);
        panel.add(btn_del);
        panel.add(btn_refresh);

        // Add panel and scroll pane to the frame
        this.add(panel);
        this.add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug(e.getActionCommand());
        Instructors instructors = instructorTable.getModel().getInstructors();
        switch (e.getActionCommand()) {
            case "Save" -> {
                instructors.forEach(instructor -> {
                    switch (instructor.getStatus()) {
                        case 'n':
                            instructor.setStatus('s');
                            if (instructors.insertInstructor(instructor)) {
                                instructor.setKey_person_id(instructor.getPerson_id());
                                instructor.setKey_slug(instructor.getSlug());
                            } else {
                                JOptionPane.showMessageDialog(this, "This is a duplicate entry. Please correct before trying to save again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 'u':
                            instructors.updateInstructor(instructor);
                            break;
                    }
                    if (instructor.getStatus() == 'n') {
                        instructor.setStatus('s');
                        instructor.setKey_person_id(instructor.getPerson_id());
                    }

                });
                repaint();
            }
            case "Add" -> {
                // Add action
                instructors.add(instructors.size(), new Instructor());
                lastRow(instructorTable, scrollPane);
            }
            case "Delete" -> {
                // Delete action
                int row = instructorTable.getSelectedRow();
                if (row != -1) {
                    instructors.remove(row);
                } else {
                    JOptionPane.showMessageDialog(this, "No row selected");
                }

            }
            case "Refresh" -> {
                instructorTable.loadInstructors();
                instructorTable.loadWorkshopSlugs();
            }
        }
        
        instructorTable.revalidate();
        instructorTable.repaint();
    }

}
