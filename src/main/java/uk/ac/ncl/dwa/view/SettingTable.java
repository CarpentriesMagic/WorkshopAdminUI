package uk.ac.ncl.dwa.view;

import uk.ac.ncl.dwa.model.SettingTableModel;

import javax.swing.*;

public class SettingTable extends JTable {
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
