package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.SchedulesTableModel;
import uk.ac.ncl.dwa.model.SettingTableModel;

import javax.swing.*;
import java.io.Serial;

public class SchedulesTable extends JTable {
    @Serial
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(SchedulesTable.class);
    private final SchedulesTableModel schedulesTablesModel = new SchedulesTableModel();

    public SchedulesTable() {
        super();
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setModel(schedulesTablesModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getColumnModel().getColumn(0).setPreferredWidth(200);
        setRowHeight(20);
    }

    @Override
    public SchedulesTableModel getModel() {
        return schedulesTablesModel;
    }

}
