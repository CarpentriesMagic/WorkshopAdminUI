package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.InstructorTableModel;
import uk.ac.ncl.dwa.model.People;

import javax.swing.*;
import javax.swing.table.TableColumn;
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
        getColumnModel().getColumn(0).setPreferredWidth(200);
        getColumnModel().getColumn(1).setPreferredWidth(130);

        /*
         * ComboBox for selecting slug
         */
        String[] workshops = globals.getWorkshops().getWorkshopNames(globals.getConnectionString());
        TableColumn roomColumn = this.getColumnModel().getColumn(0);
        JComboBox<String> roomComboBox = new JComboBox<>(workshops);
        roomColumn.setCellEditor(new DefaultCellEditor(roomComboBox));

        /*
         * ComboBox for selecting slug
         */
        String[] instructors = People.listOfInstructors();
        TableColumn instructorColumn = this.getColumnModel().getColumn(1);
        JComboBox<String> instructorComboBox = new JComboBox<>(instructors);
        instructorColumn.setCellEditor(new DefaultCellEditor(instructorComboBox));

        setRowHeight(20);
    }
}
