package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.model.InstructorTableModel;
import uk.ac.ncl.dwa.model.People;
import uk.ac.ncl.dwa.model.RoomTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class InstructorTable extends JTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final InstructorTableModel instructorTableModel = new InstructorTableModel();

    public InstructorTable() {
        super();
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setModel(instructorTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getColumnModel().getColumn(0).setPreferredWidth(130);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(200);

        loadInstructors();
        loadWorkshopSlugs();
        setRowHeight(20);
    }

    public void loadWorkshopSlugs() {
        /*
         * ComboBox for selecting workshop slug
         */
        String[] workshopList = DBHandler.getInstance().selectStringArray("workshops", "slug", "", "slug");
        TableColumn workshop = this.getColumnModel().getColumn(0);
        JComboBox<String> workshopComboBox = new JComboBox<>(workshopList);
        workshop.setCellEditor(new DefaultCellEditor(workshopComboBox));
    }

    public void loadInstructors() {
        logger.info("Loading instructors");
        /*
         * ComboBox for selecting instructor
         */
        String[] instructors = People.selectedList(1);
        TableColumn instructorColumn = this.getColumnModel().getColumn(1);
        JComboBox<String> instructorComboBox = new JComboBox<>(instructors);
        instructorColumn.setCellEditor(new DefaultCellEditor(instructorComboBox));
    }

    @Override
    public InstructorTableModel getModel() {
        return instructorTableModel;
    }
}
