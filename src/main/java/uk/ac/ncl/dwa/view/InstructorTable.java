package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.model.InstructorTableModel;
import uk.ac.ncl.dwa.model.People;
import uk.ac.ncl.dwa.model.RoomTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.io.Serial;
import java.io.Serializable;

public class InstructorTable extends JTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final InstructorTableModel instructorTableModel = new InstructorTableModel();

    public InstructorTable() {
        super();
        setModel(instructorTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getColumnModel().getColumn(0).setPreferredWidth(130);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(200);

        /*
         * ComboBox for selecting slug
         */

        String[] workshops = Globals.getInstance().getWorkshops().getWorkshopNames();
        TableColumn roomColumn = this.getColumnModel().getColumn(0);
        JComboBox<String> roomComboBox = new JComboBox<>(workshops);
        roomColumn.setCellEditor(new DefaultCellEditor(roomComboBox));

        /*
         * ComboBox for selecting slug
         */
        String[] instructors = People.selectedList(1);
        TableColumn instructorColumn = this.getColumnModel().getColumn(1);
        JComboBox<String> instructorComboBox = new JComboBox<>(instructors);
        instructorColumn.setCellEditor(new DefaultCellEditor(instructorComboBox));

        setRowHeight(20);
    }

    @Override
    public InstructorTableModel getModel() {
        return instructorTableModel;
    }
}
