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
        logger.trace("Create InstructorTableModel");
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
            case 0 -> instructor.getSlug();
            case 1 -> instructor.getPerson_id();
            case 2 -> instructor.getName();
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
        if (col < 2)
        return true;
        else return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (instructors == null) {
            return;
        }
        Instructor instructor = instructors.get(row);
        Globals.getInstance().setDirty(true);
        logger.info("Setting dirty to {}", Globals.getInstance().getDirty());
        if (instructor.getPerson_id().isBlank()) {
            Globals.getInstance().getInsertedRows("instructors").add(row);
        } else {
            logger.info("Add row {} to dirty rows", row);
            Globals.getInstance().getEditedRows("instructors").add(row);
        }
        logger.info("Value {}", ((String) value));
        switch (col) {
            case 0 -> instructor.setSlug((String) value);
            case 1 -> {
                String id = ((String) value).split(",")[0];
                String name = ((String) value).split(",")[1];
                instructor.setPerson_id(id);
                instructor.setName(name.trim());
            }
            case 2 -> {
                String id = ((String) value).split(",")[0];
                String name = ((String) value).split(",")[1];

                instructor.setName(name.trim());
            }
        }
        // Notify the table that the data has changed
        fireTableCellUpdated(row, col);
        fireTableCellUpdated(row, col + 1);
    }

    @Override
    public String getColumnName(int col) {
        return instructors.getColumnNames()[col];
    }

}
