package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.People;
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
        People people = personTable.getModel().getPeople();
        switch (e.getActionCommand()) {
            case "Save" -> {
                people.forEach(person -> {
                    switch (person.getStatus()) {
                        case 'n':
                            if (people.insertPerson(person)) {
                                person.setStatus('s');
                                person.setKey(person.getPerson_id());
                            }
                            break;
                        case 'u': people.updatePerson(person);
                            break;
                    }
                    personTable.repaint();
                });
            }
            case "Add" -> {
                logger.info("Adding new Person");
                people.add(people.size(), new Person());
                personTable.repaint();
            }
            case "Delete" -> {
                int row =  personTable.getSelectedRow();
                if (row != -1) {
                    people.remove(row);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a row");
                }
                personTable.repaint();
            }
        }
    }

}
