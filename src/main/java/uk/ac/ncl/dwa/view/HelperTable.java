package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.model.HelperTableModel;
import uk.ac.ncl.dwa.model.People;
import uk.ac.ncl.dwa.model.PersonTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class HelperTable extends JTable implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private HelperTableModel helperTableModel = new HelperTableModel();

    public HelperTable() {
        super();
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setModel(helperTableModel);
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getColumnModel().getColumn(0).setPreferredWidth(130);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(200);

        loadWorkshops();
        loadInstructors();
        setRowHeight(20);
    }

    public void loadWorkshops() {
        /*
         * ComboBox for selecting workshop slug
         */
        String[] workshopList = DBHandler.getInstance().selectStringArray("workshops", "slug", "");
        TableColumn workshop = this.getColumnModel().getColumn(0);
        JComboBox<String> workshopComboBox = new JComboBox<>(workshopList);
        workshop.setCellEditor(new DefaultCellEditor(workshopComboBox));


    }

    public void loadInstructors() {
        /*
         * ComboBox for selecting helpers
         * certvalue -1 = no cert
         * certvalue 0 = helpers
         * certvalue 1 = instructors
         */
        String[] instructors = People.selectedList(2);
        TableColumn instructorColumn = this.getColumnModel().getColumn(1);
        JComboBox<String> instructorComboBox = new JComboBox<>(instructors);
        instructorColumn.setCellEditor(new DefaultCellEditor(instructorComboBox));
    }

    @Override
    public HelperTableModel getModel() {
        return helperTableModel;
    }
}
