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
    private Rooms rooms = Globals.getInstance().getRooms();

    public RoomTableModel() {
        super();
        logger.trace("Create RoomTableModel");
        rooms.loadFromDatabase();
        setRooms(rooms);
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
        Globals.getInstance().setDirty(true);
        logger.info("Setting dirty to " + Globals.getInstance().getDirty());
        if (room.getRoom_id().trim().isBlank()) {
            logger.info("Add row " + row + " to inserted rows");
            Globals.getInstance().getInsertedRows("rooms").add(row);
        } else {
            logger.info("Add row " + row + " to dirty rows");
            Globals.getInstance().getEditedRows("rooms").add(row);
        }
        switch (col) {
            case 0:
                room.setRoom_id((String) value);
                break;
            case 1:
                room.setDescription((String) value);
                break;
            case 2:
                room.setLongitude((String) value);
                break;
            case 3:
                room.setLatitude((String) value);
                break;
            case 4:
                room.setWhat_three_words((String) value);
                break;
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


}
