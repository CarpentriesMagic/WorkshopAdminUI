package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;

public class GitHubRepoTableModel  extends AbstractTableModel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Workshops workshops = new Workshops();

    @Override
    public int getColumnCount() {
        if (workshops == null)
            return 0;
        else
            return workshops.getColumnCount();
    }

    @Override
    public int getRowCount() {
        if (workshops == null)
            return 0;
        else
            return workshops.size();
    }
    public Object getValueAt(int row, int col) {
        Workshop workshop = workshops.get(row);
        return switch (col) {
            case 0 -> workshop.getSlug();
            case 1 -> workshop.getTitle();
            case 2 -> workshop.getHumandate();
            case 3 -> workshop.getHumantime();
            case 4 -> workshop.getStartdate();
            case 5 -> workshop.getEnddate();
            case 6 -> workshop.getRoom_id();
            case 7 -> workshop.getLanguage();
            case 8 -> workshop.getCountry();
            case 9 -> workshop.isOnline();
            case 10 -> workshop.isPilot();
            case 11 -> workshop.getInc_lesson_site();
            case 12 -> workshop.getPre_survey();
            case 13 -> workshop.getPost_survey();
            case 14 -> workshop.getCarpentry_code();
            case 15 -> workshop.getCurriculum_code();
            case 16 -> workshop.getFlavour_id();
            case 17 -> workshop.getEventbrite();
            case 18 -> workshop.getSchedule();
            case 19 -> workshop.getInternal_id();
            default -> null;
        };

    }

}
