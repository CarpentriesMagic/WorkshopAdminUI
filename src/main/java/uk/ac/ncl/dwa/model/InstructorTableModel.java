package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;

public class InstructorTableModel extends AbstractTableModel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    Instructors instructors = Globals.getInstance().getInstructors();


    public InstructorTableModel() {
        super();
        logger.trace("Create RoomTableModel");
        instructors.loadFromDatabase(Globals.getInstance().getConnectionString());
        setInstructors(instructors);
    }

    private void setInstructors(Instructors instructors) {
        this.instructors = instructors;
    }

    public int getColumnCount() {
        if (instructors == null)
            return 0;
        else
            return instructors.getColumnCount();
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Instructor instructor = instructors.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> instructor.getPerson_id();
            case 1 -> instructor.getSlug();
            default -> null;
        };

    }

    public int getRowCount() {
        if (instructors == null)
            return 0;
        else
            return instructors.size();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // Allow editing for all cells
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (instructors == null) {
            return;
        }

        Instructor instructor = instructors.get(row);
        Globals.getInstance().setDirty(true);
        logger.info("Setting dirty to " + Globals.getInstance().getDirty());
        if (instructor.getPerson_id().isBlank()) {
            Globals.getInstance().getInsertedRows("instructors").add(row);
        } else {
            logger.info("Add row " + row + " to dirty rows");
            Globals.getInstance().getEditedRows("rooms").add(row);
        }
        switch (col) {
            case 0:
                instructor.setPerson_id((String) value);
                break;
            case 1:
                instructor.setSlug((String) value);
                break;
            default:
                break;
        }
        // Notify the table that the data has changed
        fireTableCellUpdated(row, col);
    }

    @Override
    public String getColumnName(int col) {

        return instructors.getColumnNames()[col];
    }

}
