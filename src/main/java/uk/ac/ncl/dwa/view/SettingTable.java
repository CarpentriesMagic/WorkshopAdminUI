package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.SettingTableModel;

import javax.swing.*;
import java.io.Serial;

public class SettingTable extends JTable {
    @Serial
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(RoomTable.class);
    private final SettingTableModel settingTableModel = new SettingTableModel();

    public SettingTable() {
        super();
        setModel(settingTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getColumnModel().getColumn(0).setPreferredWidth(200);
        getColumnModel().getColumn(1).setPreferredWidth(400);
        setRowHeight(20);
    }

    @Override
    public SettingTableModel getModel() {
        return settingTableModel;
    }

}
