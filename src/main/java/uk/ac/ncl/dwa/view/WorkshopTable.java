package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.*;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkshopTable extends JTable {
    WorkshopTableModel workshopTableModel;
    Logger logger = LoggerFactory.getLogger(WorkshopTable.class);
    Globals globals = Globals.getInstance();
    ArrayList<String> rooms;

    public WorkshopTable() {
        super();

        workshopTableModel = new WorkshopTableModel();
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

        setRowHeight(20);
        /*
         * ComboBox for selecting room
         */
        rooms = globals.getRooms().loadRoomList(globals.getConnectionString());
        TableColumn roomColumn = this.getColumnModel().getColumn(6);
        JComboBox<String> roomComboBox = new JComboBox<>();
        rooms.forEach(roomComboBox::addItem);
        roomColumn.setCellEditor(new DefaultCellEditor(roomComboBox));

        /*
         * ComboBox for selecting carpentries
         */
        String[] carpentries = {"incubator", "swc", "lc", "dc", "hpd", "cp"};
        TableColumn carpColumn = this.getColumnModel().getColumn(14);
        JComboBox<String> carpComboBox = new JComboBox<>(carpentries);
        carpColumn.setCellEditor(new DefaultCellEditor(carpComboBox));

        /*
         * ComboBox for selecting curriculum
         */
        ArrayList<String> curriculum = globals.getWorkshops().loadCurricula();
        TableColumn currColumn = this.getColumnModel().getColumn(15);
        JComboBox<String> currComboBox = new JComboBox<>(curriculum.toArray(new String[curriculum.size()]));
        currColumn.setCellEditor(new DefaultCellEditor(currComboBox));

        /*
         * ComboBox for selecting curriculum
         */
        String[] flavour = {"python", "r", "na"};
        TableColumn flavourColumn = this.getColumnModel().getColumn(16);
        JComboBox<String> flavourComboBox = new JComboBox<>(flavour);
        flavourColumn.setCellEditor(new DefaultCellEditor(flavourComboBox));

        /*
         * ComboBox for selecting schedule to include
         */
        ArrayList<String> schedules = globals.getWorkshops().loadSchedules();
        TableColumn scheduleColumn = this.getColumnModel().getColumn(18);
        JComboBox<String> scheduleComboBox = new JComboBox<String>(schedules.toArray(new String[schedules.size()]));
        scheduleColumn.setCellEditor(new DefaultCellEditor(scheduleComboBox));
    }
}
