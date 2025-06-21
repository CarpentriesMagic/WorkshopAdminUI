package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

import javax.naming.ldap.HasControls;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.util.HashMap;

public class SettingTableModel extends AbstractTableModel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Settings settings = new Settings();

    public SettingTableModel() {
        super();
//        settings.loadFromDatabase();
    }

    @Override
    public int getRowCount() {
        if (settings == null)
            return 0;
        else
            return settings.size();
    }

    @Override
    public int getColumnCount() {
        if (settings == null)
            return 0;
        else
            return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Setting setting = settings.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> setting.getKeyValue();
            case 1 -> setting.getValue();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (settings == null) {
            return;
        }
        Setting setting = settings.get(row);
        Globals.getInstance().setDirty(true);
        if (setting.getKeyValue().trim().isBlank()) {
            logger.info("Add row " + row + " to inserted settings");
            Globals.getInstance().getInsertedRows("settings").add(row);
        } else {
            logger.info("Add row " + row + " to dirty rows");
            Globals.getInstance().getEditedRows("settings").add(row);
        }
        switch (col) {
            case 0:
                logger.info("Setting key {}", (String) value);
                setting.setKeyValue((String) value);
                break;
            case 1:
                setting.setValue((String) value);
                break;
        }
//            fireTableCellUpdated(row, col);
        fireTableRowsUpdated(0, getRowCount() - 1);
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        // Allow editing for all cells
        return true;
    }

    @Override
    public String getColumnName(int col) {
        return settings.getColumnName(col);
    }

    public String[] getColumnNames() {
        return settings.getColumnNames();
    }

    public Settings getSettings() {
        return settings;
    }
}
