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
    People people = Globals.getInstance().getPeople();


    public PersonTableModel() {
        super();
        logger.trace("Create InstructorTableModel");
        people.loadFromDatabase(Globals.getInstance().getConnectionString());
        setPeople(people);
    }

    private void setPeople(People people) {
        this.people = people;
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
        Globals.getInstance().setDirty(true);
        logger.info("Setting dirty to " + Globals.getInstance().getDirty());
        if (person.getPerson_id().isBlank()) {
            Globals.getInstance().getInsertedRows("people").add(row);
        } else {
            logger.info("Add row " + row + " to dirty rows");
            Globals.getInstance().getEditedRows("people").add(row);
        }
        switch (col) {
            case 0:
                person.setPerson_id((String) value);
                break;
            case 1:
                person.setTitle((String) value);
                break;
            case 2:
                person.setFirstname((String) value);
                break;
            case 3:
                person.setLastname((String) value);
                break;
            case 4:
                person.setCertified((String) value);
                break;
            case 5:
                person.setEmail((String) value);
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

}
