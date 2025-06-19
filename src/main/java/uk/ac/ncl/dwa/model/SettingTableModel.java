package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;

public class SettingTableModel  extends AbstractTableModel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    Settings settings = Globals.getInstance().getSettings();

    public SettingTableModel() {
        super();
        settings.loadFromDatabase();
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
            return settings.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] keys = settings.keySet().toArray(new String[0]);
        return switch (columnIndex) {
            case 0 -> keys[rowIndex];
            case 1 -> settings.get(keys[rowIndex]);
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // Allow editing for all cells
        return true;
    }
}
