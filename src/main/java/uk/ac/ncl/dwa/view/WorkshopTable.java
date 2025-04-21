package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.RoomListItem;
import uk.ac.ncl.dwa.model.Rooms;
import uk.ac.ncl.dwa.model.WorkshopTableModel;
import javax.swing.*;
import java.util.List;

public class WorkshopTable extends JTable {
    WorkshopTableModel workshopTableModel;
    Logger logger = LoggerFactory.getLogger(WorkshopTable.class);
    Globals globals = Globals.getInstance();

    public WorkshopTable() {
        super();
        Rooms rooms = new Rooms();
        List<RoomListItem> roomList = rooms.loadRoomList(globals.getConnectionString());
        workshopTableModel= new WorkshopTableModel();
        setModel(workshopTableModel);

        // Create JTable and set its model
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //  set column widths
        getColumnModel().getColumn(0).setPreferredWidth(130);
        getColumnModel().getColumn(1).setPreferredWidth(200);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        getColumnModel().getColumn(3).setPreferredWidth(100);
        getColumnModel().getColumn(4).setPreferredWidth(100);
        getColumnModel().getColumn(5).setPreferredWidth(100);
        getColumnModel().getColumn(6).setPreferredWidth(80);
        getColumnModel().getColumn(7).setPreferredWidth(30);
        getColumnModel().getColumn(8).setPreferredWidth(35);
        getColumnModel().getColumn(9).setPreferredWidth(40);
        getColumnModel().getColumn(10).setPreferredWidth(30);
        getColumnModel().getColumn(11).setPreferredWidth(180);
        getColumnModel().getColumn(12).setPreferredWidth(180);
        getColumnModel().getColumn(13).setPreferredWidth(180);
    }
}
