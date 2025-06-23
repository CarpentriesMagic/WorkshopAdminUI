package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;

public class PersonTableModel extends AbstractTableModel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    People people = new People();

    public PersonTableModel () {
        super();
    }

    public int getColumnCount() {
        if (people == null)
            return 0;
        else
            return people.getColumnCount();
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Person person = people.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> person.getPerson_id();
            case 1 -> person.getTitle();
            case 2 -> person.getFirstname();
            case 3 -> person.getLastname();
            case 4 -> person.getCertified();
            case 5 -> person.getEmail();
            default -> null;
        };

    }

    public int getRowCount() {
        if (people == null)
            return 0;
        else
            return people.size();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // Allow editing for all cells
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (people == null) {
            return;
        }

        Person person = people.get(row);

        switch (col) {
            case 0:
                person.setPerson_id((String) value);
                if (person.getStatus() == 's') person.setStatus('u');
                break;
            case 1:
                person.setTitle((String) value);
                if (person.getStatus() == 's') person.setStatus('u');
                break;
            case 2:
                person.setFirstname((String) value);
                if (person.getStatus() == 's') person.setStatus('u');
                break;
            case 3:
                person.setLastname((String) value);
                if (person.getStatus() == 's') person.setStatus('u');
                break;
            case 4:
                person.setCertified((String) value);
                if (person.getStatus() == 's') person.setStatus('u');
                break;
            case 5:
                person.setEmail((String) value);
                if (person.getStatus() == 's') person.setStatus('u');
                break;
            default:
                break;
        }
        // Notify the table that the data has changed
        fireTableCellUpdated(row, col);
    }

    @Override
    public String getColumnName(int col) {

        return people.getColumnNames()[col];
    }

    public People getPeople() {
        return people;
    }

}
