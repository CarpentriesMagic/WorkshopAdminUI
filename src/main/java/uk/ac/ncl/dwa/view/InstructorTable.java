package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.InstructorTableModel;
import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

public class InstructorTable extends JTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    Globals globals = Globals.getInstance();

    public InstructorTable() {
        super();
        InstructorTableModel instructorTableModel = new InstructorTableModel();
        setModel(instructorTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //  set column widths
        getColumnModel().getColumn(0).setPreferredWidth(130);
        getColumnModel().getColumn(1).setPreferredWidth(200);
    }
}
