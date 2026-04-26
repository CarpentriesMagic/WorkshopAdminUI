package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;


public class SchedulesTableModel extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Schedules schedules = new Schedules();

    public SchedulesTableModel() {
        super();
    }

    public int getColumnCount() {
        if (schedules == null)
            return 0;
        else
            return schedules.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Schedule schedule = schedules.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> schedule.getSchedule_id();
            default -> null;
        };

    }

    public int getRowCount() {
        if (schedules == null)
            return 0;
        else
            return schedules.size();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (schedules == null) {
            return;
        }

        Schedule schedule = schedules.get(row);
        if (col == 0) {
            schedule.getSchedule_id();
            //if (schedule.getStatus() != 'n') schedule.setStatus('u');
        }
        // Notify the table that the data has changed
        fireTableCellUpdated(row, col);
    }

    @Override
    public String getColumnName(int col) {
        return schedules.getColumnNames()[col];
    }

    public String[] getColumnNames() {
        return schedules.getColumnNames();
    }

    public void setSchedules(Schedules schedules) {
        this.schedules = schedules;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // Allow editing for all cells
        return true;
    }

    public Schedules getSchedules() {
        return schedules;
    }
}
