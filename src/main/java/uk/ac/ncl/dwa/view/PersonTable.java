package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.PersonTableModel;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

public class PersonTable extends JTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    Globals globals = Globals.getInstance();

    public PersonTable() {
        super();
        PersonTableModel personTableModel = new PersonTableModel();
        setModel(personTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //  set column widths
        getColumnModel().getColumn(0).setPreferredWidth(80);
        getColumnModel().getColumn(1).setPreferredWidth(80);
        getColumnModel().getColumn(2).setPreferredWidth(130);
        getColumnModel().getColumn(3).setPreferredWidth(130);
        getColumnModel().getColumn(4).setPreferredWidth(80);
        getColumnModel().getColumn(5).setPreferredWidth(250);

        setRowHeight(20);
    }
}
