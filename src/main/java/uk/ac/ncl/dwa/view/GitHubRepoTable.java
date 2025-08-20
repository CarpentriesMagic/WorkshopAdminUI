package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.model.GitHubRepoTableModel;
import uk.ac.ncl.dwa.model.Settings;
import uk.ac.ncl.dwa.model.WorkshopTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.HashMap;

public class GitHubRepoTable extends JTable implements ListSelectionListener {
    GitHubRepoTableModel gitHubRepoTableModel;
    Logger logger = LoggerFactory.getLogger(GitHubRepoTable.class);
    ArrayList<String> rooms;

    public GitHubRepoTable() {
        super();
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        gitHubRepoTableModel = new GitHubRepoTableModel();
        setModel(gitHubRepoTableModel);
        // Create JTable and set its model
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //  set column widths
        getColumnModel().getColumn(0).setPreferredWidth(130);
        getColumnModel().getColumn(1).setPreferredWidth(200);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        getColumnModel().getColumn(3).setPreferredWidth(100);
        getColumnModel().getColumn(4).setPreferredWidth(100);
        getColumnModel().getColumn(5).setPreferredWidth(100);
        getColumnModel().getColumn(6).setPreferredWidth(80);
        getColumnModel().getColumn(7).setPreferredWidth(30);
        getColumnModel().getColumn(8).setPreferredWidth(35);
        getColumnModel().getColumn(9).setPreferredWidth(40);
        getColumnModel().getColumn(10).setPreferredWidth(30);
        getColumnModel().getColumn(11).setPreferredWidth(180);
        getColumnModel().getColumn(12).setPreferredWidth(180);
        getColumnModel().getColumn(13).setPreferredWidth(180);

        setRowHeight(20);
        loadRooms();
        loadCarpentries();
        loadCurricula();
        loadFlavours();
        loadSchedules();
    }

    public void loadRooms() {
        /*
         * ComboBox for selecting room
         */
        String[] roomsList = DBHandler.getInstance().selectStringArray("room", "room_id", "");
        TableColumn roomColumn = this.getColumnModel().getColumn(6);
        JComboBox<String> roomComboBox = new JComboBox<>(roomsList);
        roomColumn.setCellEditor(new DefaultCellEditor(roomComboBox));
    }

    public void loadCarpentries() {
        /*
         * ComboBox for selecting carpentries
         */
//        String[] carpentries = {"incubator", "swc", "lc", "dc", "hpd", "cp"};
        String[] carpentries = DBHandler.getInstance().selectStringArray("carpentry", "carpentry_code", "");
        TableColumn carpColumn = this.getColumnModel().getColumn(14);
        JComboBox<String> carpComboBox = new JComboBox<>(carpentries);
        carpColumn.setCellEditor(new DefaultCellEditor(carpComboBox));
    }

    public void loadCurricula() {
        /*
         * ComboBox for selecting curriculum
         */
        String[] curriculaList = DBHandler.getInstance().selectStringArray("curriculum", "curriculum_code", "");
        TableColumn currColumn = this.getColumnModel().getColumn(15);
        JComboBox<String> currComboBox = new JComboBox<>(curriculaList);
        currColumn.setCellEditor(new DefaultCellEditor(currComboBox));
    }

    public void loadFlavours() {
        /*
         * ComboBox for selecting curriculum
         */
        String[] flavour = {"python", "r", "na"};
        TableColumn flavourColumn = this.getColumnModel().getColumn(16);
        JComboBox<String> flavourComboBox = new JComboBox<>(flavour);
        flavourColumn.setCellEditor(new DefaultCellEditor(flavourComboBox));
    }

    public void loadSchedules() {
        /*
         * ComboBox for selecting schedule to include
         */
        String[] schedules = DBHandler.getInstance().selectStringArray("schedules", "schedule_id", "");
        TableColumn scheduleColumn = this.getColumnModel().getColumn(18);
        JComboBox<String> scheduleComboBox = new JComboBox<String>(schedules);
        scheduleColumn.setCellEditor(new DefaultCellEditor(scheduleComboBox));
    }


    public GitHubRepoTableModel getModel() {
        return gitHubRepoTableModel;
    }
}
