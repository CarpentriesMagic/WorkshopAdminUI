package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.RoomTableModel;

import javax.swing.*;
import java.io.Serial;

public class RoomTable extends JTable {
    @Serial
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(RoomTable.class);
    Globals globals = Globals.getInstance();

    public RoomTable() {
        super();
        RoomTableModel roomTableModel = new RoomTableModel();
        setModel(roomTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //  set column widths
        getColumnModel().getColumn(0).setPreferredWidth(130);
        getColumnModel().getColumn(1).setPreferredWidth(200);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        getColumnModel().getColumn(3).setPreferredWidth(100);
        getColumnModel().getColumn(4).setPreferredWidth(100);

        setRowHeight(20);

    }
}
