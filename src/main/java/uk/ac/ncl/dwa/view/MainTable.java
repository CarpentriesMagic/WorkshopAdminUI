package uk.ac.ncl.dwa.view;

import uk.ac.ncl.dwa.model.RoomListItem;
import uk.ac.ncl.dwa.model.Rooms;
import uk.ac.ncl.dwa.model.WorkshopTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.ArrayList;

public class MainTable extends JTable {
    WorkshopTableModel tableModel;


    public MainTable(String connectionString) {
        super();
        tableModel= new WorkshopTableModel(connectionString);
        setModel(tableModel);
        // Create JTable and set its model
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //  set column widths
        getColumnModel().getColumn(0).setPreferredWidth(100);
        getColumnModel().getColumn(1).setPreferredWidth(200);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        getColumnModel().getColumn(3).setPreferredWidth(100);
        getColumnModel().getColumn(4).setPreferredWidth(100);
        getColumnModel().getColumn(5).setPreferredWidth(100);
        getColumnModel().getColumn(6).setPreferredWidth(100);
        getColumnModel().getColumn(7).setPreferredWidth(30);
        getColumnModel().getColumn(8).setPreferredWidth(35);
        getColumnModel().getColumn(9).setPreferredWidth(40);
        getColumnModel().getColumn(10).setPreferredWidth(30);

        Rooms rooms = new Rooms();
        ArrayList<RoomListItem> roomList = rooms.loadRoomList(connectionString);
        // Populate JComboBox with room descriptions
        JComboBox<String> roomComboBox = new JComboBox<>();
        for (RoomListItem room : roomList) {
            roomComboBox.addItem(room.getDescription());
        }
        // Set custom cell editor for the "room" column (index 6)
        TableColumn roomColumn = getColumnModel().getColumn(6);
        roomColumn.setCellEditor(new DefaultCellEditor(roomComboBox));

    }
}
