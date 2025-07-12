package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.model.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkshopTable extends JTable implements ListSelectionListener {
    WorkshopTableModel workshopTableModel;
    Logger logger = LoggerFactory.getLogger(WorkshopTable.class);
    ArrayList<String> rooms;
    JTextArea textArea;
    Settings settingsObject;

    public WorkshopTable(JTextArea textArea, Settings settings) {
        super();
        this.settingsObject = settings;
        this.textArea = textArea;
        workshopTableModel = new WorkshopTableModel();
        setModel(workshopTableModel);
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
        /*
         * ComboBox for selecting room
         */
        String[] roomsList = DBHandler.getInstance().selectStringArray("room", "room_id", "");
        TableColumn roomColumn = this.getColumnModel().getColumn(6);
        JComboBox<String> roomComboBox = new JComboBox<>(roomsList);
        roomColumn.setCellEditor(new DefaultCellEditor(roomComboBox));

        /*
         * ComboBox for selecting carpentries
         */
        String[] carpentries = {"incubator", "swc", "lc", "dc", "hpd", "cp"};
        TableColumn carpColumn = this.getColumnModel().getColumn(14);
        JComboBox<String> carpComboBox = new JComboBox<>(carpentries);
        carpColumn.setCellEditor(new DefaultCellEditor(carpComboBox));

        /*
         * ComboBox for selecting curriculum
         */
        String[] curriculaList = DBHandler.getInstance().selectStringArray("curriculum", "curriculum_code", "");
        TableColumn currColumn = this.getColumnModel().getColumn(15);
        JComboBox<String> currComboBox = new JComboBox<>(curriculaList);
        currColumn.setCellEditor(new DefaultCellEditor(currComboBox));

        /*
         * ComboBox for selecting curriculum
         */
        String[] flavour = {"python", "r", "na"};
        TableColumn flavourColumn = this.getColumnModel().getColumn(16);
        JComboBox<String> flavourComboBox = new JComboBox<>(flavour);
        flavourColumn.setCellEditor(new DefaultCellEditor(flavourComboBox));

        /*
         * ComboBox for selecting schedule to include
         */
        String[] schedules = DBHandler.getInstance().selectStringArray("schedules", "schedule_id", "");
        TableColumn scheduleColumn = this.getColumnModel().getColumn(18);
        JComboBox<String> scheduleComboBox = new JComboBox<String>(schedules);
        scheduleColumn.setCellEditor(new DefaultCellEditor(scheduleComboBox));
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        textArea.setText(getRecordAsString(getSelectedRow()));

    }

    public String getRecordAsString(int row) {
        HashMap<String, String> settings = this.settingsObject.getHashMap();
        String collabdoc = settings.get("collabdoc");
        String internaldoc = (settings.get("internal_id") != null?
                settings.get("internal_id"):"");
        String slug = workshopTableModel.getValueAt(getSelectedRow(), 0).toString();
        String pre = workshopTableModel.getValueAt(getSelectedRow(), 12).toString();
        if (pre == null || pre.isEmpty()) {
            pre = settings.get("pre_survey") + slug;
        }
        String post = workshopTableModel.getValueAt(row, 13).toString();
        if (post == null || post.isEmpty()) {
            post = settings.get("post_survey") + slug;
        }
        String internal_id = (workshopTableModel.getValueAt(getSelectedRow(), 19) != null)?workshopTableModel.getValueAt(getSelectedRow(), 19).toString():"";
        collabdoc = collabdoc.replace("<slug>", slug);
        internaldoc = internaldoc.replace("<internal_id>", internal_id);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Slug:\t\t").append(slug).append("\n");
        stringBuilder.append("Internal ID:\t\t").append(internaldoc).append("\n");
        stringBuilder.append("Title:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 1).toString()).append("\n");
        stringBuilder.append("Human date:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 2).toString()).append("\n");
        stringBuilder.append("Human time:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 3).toString()).append("\n");
        stringBuilder.append("Start date:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 4).toString()).append("\n");
        stringBuilder.append("End date:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 5).toString()).append("\n");
        stringBuilder.append("Room:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 6).toString()).append("\n");
        stringBuilder.append("Language:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 7).toString()).append("\n");
        stringBuilder.append("Country:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 8).toString()).append("\n");
        stringBuilder.append("Online:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 9).toString()).append("\n");
        stringBuilder.append("Pilot:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 10).toString()).append("\n");
        stringBuilder.append("Lesson site:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 11).toString()).append("\n");
        stringBuilder.append("Pre workshop:\t").append(pre).append("\n");
        stringBuilder.append("Post workshop:\t").append(post).append("\n");
        stringBuilder.append("Carpentry:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 14).toString()).append("\n");
        stringBuilder.append("Curriculum:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 15).toString()).append("\n");
        stringBuilder.append("Flavour:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 16).toString()).append("\n");
        stringBuilder.append("EventBrite:\t").append(workshopTableModel.getValueAt(getSelectedRow(), 17).toString()).append("\n");
        stringBuilder.append("Schedule:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 18).toString()).append("\n");
        stringBuilder.append("Collaborative Doc:\t").append(collabdoc).append("\n");

        return stringBuilder.toString();
    }

    public WorkshopTableModel getModel() {
        return workshopTableModel;
    }
}
