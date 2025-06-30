package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;

public class RoomTableModel extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Rooms rooms = new Rooms();

    public RoomTableModel() {
        super();
    }

    public int getColumnCount() {
        if (rooms == null)
            return 0;
        else
            return rooms.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Room room = rooms.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> room.getRoom_id();
            case 1 -> room.getDescription();
            case 2 -> room.getLongitude();
            case 3 -> room.getLatitude();
            case 4 -> room.getWhat_three_words();
            default -> null;
        };

    }

    public int getRowCount() {
        if (rooms == null)
            return 0;
        else
            return rooms.size();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (rooms == null) {
            return;
        }

        Room room = rooms.get(row);
        switch (col) {
            case 0:
                room.setRoom_id((String) value);
                if (room.getStatus() != 'n') room.setStatus('u');                break;
            case 1:
                room.setDescription((String) value);
                if (room.getStatus() != 'n') room.setStatus('u');                break;
            case 2:
                room.setLongitude((String) value);
                if (room.getStatus() != 'n') room.setStatus('u');                break;
            case 3:
                room.setLatitude((String) value);
                if (room.getStatus() != 'n') room.setStatus('u');                break;
            case 4:
                room.setWhat_three_words((String) value);
                if (room.getStatus() != 'n') room.setStatus('u');                break;
            default:
                break;
        }
        // Notify the table that the data has changed
        fireTableCellUpdated(row, col);
    }

    @Override
    public String getColumnName(int col) {
        return rooms.getColumnNames()[col];
    }

    public String[] getColumnNames() {
        return rooms.getColumnNames();
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // Allow editing for all cells
        return true;
    }

    public Rooms getRooms() {
        return rooms;
    }
}
