package uk.ac.ncl.dwa.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.SpecificQueriesHelper;
import uk.ac.ncl.dwa.model.People;
import uk.ac.ncl.dwa.model.Person;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(PersonPanel.class);
    JTextArea textArea = new JTextArea();
    PersonTable personTable = new PersonTable(textArea);
    JTextField tf_startdate = new JTextField(30);

    public PersonPanel() {
        setLayout(new MigLayout("", "[30%][70%]", "[fill][fill]"));

        // Add components to the frame
        JPanel personTablePanel = new JPanel(new MigLayout("fill", "[]", "[fill]"));
        JPanel personTextFieldPanel = new JPanel(new MigLayout("fill", "[]", "[fill]"));
        textArea.setEditable(false);
        JScrollPane personTableScollPane = new JScrollPane(personTable);
        JScrollPane personTextFieldScrollPane = new JScrollPane(textArea);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, personTablePanel, personTextFieldPanel);
        splitPane.setResizeWeight(0.3); // Initial divider position at 50%
        splitPane.setContinuousLayout(true); // Smooth dragging
        splitPane.setLeftComponent(personTableScollPane);
        splitPane.setRightComponent(personTextFieldScrollPane);
        personTable.setFillsViewportHeight(true);
        JPanel buttonPanel = new JPanel();
        JButton btn_save = new JButton("Save");
        JButton btn_add = new JButton("Add");
        JButton btn_del = new JButton("Delete");
        JLabel lbl_startdate = new JLabel("Start Date");
        JButton btn_calcTotals = new JButton("Calculate Totals");
        btn_calcTotals.addActionListener(this);
        btn_calcTotals.setToolTipText("Calculate the number of times this person acted as a \nhelper and an instructor, start from the slug (date) specified.");
        btn_save.addActionListener(this);
        btn_add.addActionListener(this);
        btn_del.addActionListener(this);
        buttonPanel.add(btn_save);
        buttonPanel.add(btn_add);
        buttonPanel.add(btn_del);
        buttonPanel.add(lbl_startdate);
        buttonPanel.add(tf_startdate);
        buttonPanel.add(btn_calcTotals);

        // Add panel and scroll pane to the frame
        this.add(buttonPanel, "span, grow, wrap");
        this.add(splitPane, "span, grow, push, wrap");
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
            case "Calculate Totals" -> {
                if (tf_startdate.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter start date");
                } else {
                    String results = "Instructors:\n";
                    results += SpecificQueriesHelper.getAllStatus("instructors", tf_startdate.getText(), "");
                    results += "Helpers:\n";
                    results += SpecificQueriesHelper.getAllStatus("helpers", tf_startdate.getText(), "");
                    textArea.setText(results);
                }
            }
        }
    }

}
