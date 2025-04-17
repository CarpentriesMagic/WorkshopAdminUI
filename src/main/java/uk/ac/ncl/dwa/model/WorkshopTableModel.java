package uk.ac.ncl.dwa.model;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkshopTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private Workshops workshops;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public WorkshopTableModel() {
        super();
        logger.trace("Create WorkshopTableModel");
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

}