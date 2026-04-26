package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.SchedulesTableModel;
import javax.swing.*;
import java.io.Serial;

public class SchedulesPanel extends JTable {
    @Serial
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(SchedulesTable.class);
    private final SchedulesTableModel schedulesTableModel = new SchedulesTableModel();

    public SchedulesPanel() {
        super();
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setModel(schedulesTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getColumnModel().getColumn(0).setPreferredWidth(30);

        setRowHeight(20);
    }

    @Override
    public SchedulesTableModel getModel() {
        return schedulesTableModel;
    }
}
