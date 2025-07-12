package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.table.AbstractTableModel;
import java.io.Serial;

public class HelperTableModel extends AbstractTableModel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    Helpers helpers = new Helpers();

    public HelperTableModel() {
        super();
    }

    public int getColumnCount() {
        if (helpers == null)
            return 0;
        else
            return helpers.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Helper helper = helpers.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> helper.getSlug();
            case 1 -> helper.getPerson_id();
            case 2 -> helper.getName();
            default -> null;
        };

    }

    public int getRowCount() {
        if (helpers == null)
            return 0;
        else
            return helpers.size();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (helpers.get(row).getStatus() == 's') return false;
        else return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (helpers == null) {
            return;
        }

        Helper helper = helpers.get(row);
        System.out.println("Value: " + value);
        switch (col) {
            case 0 -> helper.setSlug((String) value);
            case 1 -> {
                String id = ((String) value).split(",")[0];
                String name = ((String) value).split(",")[1];
                helper.setPerson_id(id);
                helper.setName(name.trim());
            }
//            case 2 -> helper.setName(name);
        }
        // Notify the table that the data has changed
        fireTableCellUpdated(row, col);
        fireTableCellUpdated(row, col + 1);
    }

    @Override
    public String getColumnName(int col) {
        return helpers.getColumnNames()[col];
    }

    public Helpers getHelpers() {
        return helpers;
    }

}
