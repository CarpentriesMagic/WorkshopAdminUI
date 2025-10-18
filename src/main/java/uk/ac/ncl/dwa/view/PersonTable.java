package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.PersonTableModel;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import static uk.ac.ncl.dwa.database.SpecificQueriesHelper.getPersonHelperStatus;
import static uk.ac.ncl.dwa.database.SpecificQueriesHelper.getPersonInstructorStatus;

public class PersonTable extends JTable implements Serializable, ListSelectionListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PersonTableModel personTableModel = new PersonTableModel();
    private JTextArea textArea;

    public PersonTable(JTextArea textArea) {
        super();
        this.textArea = textArea;
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setModel(personTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getColumnModel().getColumn(0).setPreferredWidth(80);
        getColumnModel().getColumn(1).setPreferredWidth(80);
        getColumnModel().getColumn(2).setPreferredWidth(130);
        getColumnModel().getColumn(3).setPreferredWidth(130);
        getColumnModel().getColumn(4).setPreferredWidth(80);
        getColumnModel().getColumn(5).setPreferredWidth(250);
        setRowHeight(20);
    }

    @Override
    public PersonTableModel getModel() {
        return personTableModel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        LocalDate today = LocalDate.now();

        calcIndivTotals(today.toString().substring(0, 4) + "-09-01");
    }

    public void calcIndivTotals(String startslug) {
        textArea.setText("");
        if (getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row");
        } else {
            String person_id = personTableModel.getValueAt(getSelectedRow(), 0).toString();
            StringBuilder sb = new StringBuilder();
            sb.append("Acting as instructor: ").append(getPersonInstructorStatus(person_id, startslug, ""));
            sb.append("\n");
            sb.append("Acting as helper: ").append(getPersonHelperStatus(person_id, startslug, ""));
            sb.append("\n");
            textArea.setText(sb.toString());
        }
    }
}
