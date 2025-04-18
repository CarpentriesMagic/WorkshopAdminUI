package uk.ac.ncl.dwa.model;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkshopTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    Workshops workshops = new Workshops();
    private Logger logger = LoggerFactory.getLogger(getClass());

    public WorkshopTableModel(String connectionString) {
        super();
        logger.trace("Create WorkshopTableModel");
        workshops.loadFromDatabase(connectionString);

        //WorkshopTableModel tableModel = new WorkshopTableModel(connectionString);
        setWorkshops(workshops);
    }

    public int getColumnCount() {
        if (workshops == null)
            return 0;
        else
            return workshops.getColumnCount();
    }

    public int getRowCount() {
        if (workshops == null)
            return 0;
        else
            return workshops.size();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (workshops == null) {
            return;
        }

        Workshop workshop = workshops.get(row);
        switch (col) {
            case 0:
                workshop.setSlug((String) value);
                break;
            case 1:
                workshop.setTitle((String) value);
                break;
            case 2:
                workshop.setHumandate((String) value);
                break;
            case 3:
                workshop.setHumandate((String) value);
                break;
            case 4:
                workshop.setStartdate((String) value);
                break;
            case 5:
                workshop.setEnddate((String) value);
                break;
        }

        // Notify the table that the data has changed
        fireTableCellUpdated(row, col);
    }

    public Object getValueAt(int row, int col) {
        Workshop workshop = workshops.get(row);
        switch (col) {
            case 0:
                return workshop.getSlug();
            case 1:
                return workshop.getTitle();
            case 2:
                return workshop.getHumandate();
            case 3:
                return workshop.getHumandate();
            case 4:
                return workshop.getStartdate();
            case 5:
                return workshop.getEnddate();
            case 6:
                return workshop.getRoom_id();
            case 7:
                return workshop.getLanguage();
            case 8:
                return workshop.getCountry();
            case 9:
                return workshop.isOnline();
            case 10:
                return workshop.isPilot();
            case 11:
                return workshop.getInc_lesson_site();
            case 12:
                return workshop.getPre_survey();
            case 13:
                return workshop.getPost_survey();
            case 14:
                return workshop.getCarpentry_code();
            case 15:
                return workshop.getCurriculum_code();
            case 16:
                return workshop.getFlavour_id();
            case 17:
                return workshop.getEventbrite();
            case 18:
                return workshop.getSchedule();

        }
        return null;

    }

    @Override
    public String getColumnName(int col) {
        return workshops.getColumnNames()[col];
    }

    public Workshops getWorkshops() {
        return workshops;
    }

    public void setWorkshops(Workshops workshops) {
        logger.trace("Set workshops object");
        this.workshops = workshops;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // Allow editing for all cells
        return true;
    }



}