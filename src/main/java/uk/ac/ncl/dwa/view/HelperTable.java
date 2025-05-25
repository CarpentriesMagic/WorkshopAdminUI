package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.HelperTableModel;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

public class HelperTable extends JTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    Globals globals = Globals.getInstance();

    public HelperTable() {
        super();
        HelperTableModel helperTableModel = new HelperTableModel();
        setModel(helperTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //  set column widths
        getColumnModel().getColumn(0).setPreferredWidth(200);
        getColumnModel().getColumn(1).setPreferredWidth(130);

        setRowHeight(20);
    }
}
