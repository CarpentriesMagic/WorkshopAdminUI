package uk.ac.ncl.dwa.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import uk.ac.ncl.dwa.database.SpecificQueriesHelper;
import uk.ac.ncl.dwa.model.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.HashMap;

public class WorkshopTable extends JTable implements ListSelectionListener {
    WorkshopTableModel workshopTableModel;
    Logger logger = LoggerFactory.getLogger(WorkshopTable.class);
    ArrayList<String> rooms;
    JTextPane textArea;
    Settings settingsObject;

    public WorkshopTable(JTextPane textArea, Settings settings) {
        super();
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        loadRooms();
        loadCarpentries();
        loadCurricula();
        loadFlavours();
        loadSchedules();
        selectPilot();
        selectOnline();
        selectRegistered();
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
        String[] flavour = DBHandler.getInstance().selectStringArray("flavour", "flavour_id", "");
        TableColumn flavourColumn = this.getColumnModel().getColumn(16);
        JComboBox<String> flavourComboBox = new JComboBox<>(flavour);
        flavourColumn.setCellEditor(new DefaultCellEditor(flavourComboBox));
    }

    public void selectPilot() {
        String[] pilot = new String[]{"false", "true"};
        TableColumn pilotColumn = this.getColumnModel().getColumn(10);
        JComboBox<String> pilotComboBox = new JComboBox<>(pilot);
        pilotColumn.setCellEditor(new DefaultCellEditor(pilotComboBox));
    }

    public void selectOnline() {
        String[] online = new String[]{"false", "true"};
        TableColumn onlineColumn = this.getColumnModel().getColumn(9);
        JComboBox<String> onlineComboBox = new JComboBox<>(online);
        onlineColumn.setCellEditor(new DefaultCellEditor(onlineComboBox));
    }

    public void selectRegistered() {
        String[] registered = new String[]{"false", "true"};
        TableColumn registeredColumn = this.getColumnModel().getColumn(20);
        JComboBox<String> registeredComboBox = new JComboBox<>(registered);
        registeredColumn.setCellEditor(new DefaultCellEditor(registeredComboBox));
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

    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        textArea.setText("");
        textArea.setText(getRecordAsString(getSelectedRow()));

    }

    private String makeLink(String title, String url) {
        StringBuilder builder = new StringBuilder();

        builder.append(title)
                .append(":\t\t")
                .append("<a href=\"").append(url)
                .append("\">")
                .append(url)
                .append("</a><br/>");
        return builder.toString();
    }

    private String makeGitHubLink(String organisation, String slug) {
        StringBuilder builder = new StringBuilder();
        builder.append("Website Repository link:\t")
                .append("<a href=\"")
                .append("https://")
                .append("github.com/")
                .append(organisation)
                .append("/")
                .append(slug)
                .append("\">")
                .append("Repository Link")
                .append("</a><br/>");

        return builder.toString();

    }

    private String makeWebsiteLink(String organisation, String slug) {
        StringBuilder builder = new StringBuilder();
        builder.append("Website link:\t")
                .append("<a href=\"")
                .append("https://")
                .append(organisation)
                .append(".github.io/")
                .append(slug)
                .append("\">")
                .append("Workshop Website")
                .append("</a><br/>");

        return builder.toString();
    }

    /**
     * Retrieve the workshop record as text for display
     * THIS SHOULD PROBABLY NOT BE IN THIS CLASS (TO BE THOUGHT ABOUT)
     * @param row
     * @return
     */
    public String getRecordAsString(int row) {
        HashMap<String, String> settings = this.settingsObject.getHashMap();
        String collabdoc = settings.get("collabdoc");
        String organisation = settings.get("organisation");
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
        String pretix_admin = (settings.get("pretix_admin") != null?
                "<a href=\"" + settings.get("pretix_admin").replace("<slug>", slug) + "\">" + settings.get("pretix_admin").replace("<slug>", slug) + "</a>":"");
        String pretix_shop = (settings.get("pretix_shop".toLowerCase()) != null?
                "<a href=\"" + settings.get("pretix_shop").replace("<slug>", slug) + "\">" + settings.get("pretix_shop").replace("<slug>", slug) + "</a>":"");
        String internal_id = (workshopTableModel.getValueAt(getSelectedRow(), 19) != null)?workshopTableModel.getValueAt(getSelectedRow(), 19).toString():"";
        collabdoc = collabdoc.replace("<slug>", slug);
        internaldoc = internaldoc.replace("<internal_id>", internal_id);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Slug:\t\t").append(slug).append("<br/>");
        stringBuilder.append(makeLink("Internal ID", internaldoc));
        stringBuilder.append("Title:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 1).toString()).append("<br/>");
        stringBuilder.append("Human date:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 2).toString()).append("<br/>");
        stringBuilder.append("Human time:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 3).toString()).append("<br/>");
        stringBuilder.append("Start date:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 4).toString()).append("<br/>");
        stringBuilder.append("End date:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 5).toString()).append("<br/>");
        stringBuilder.append("Room:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 6).toString()).append("<br/>");
        stringBuilder.append("Language:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 7).toString()).append("<br/>");
        stringBuilder.append("Country:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 8).toString()).append("<br/>");
        stringBuilder.append("Online:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 9).toString()).append("<br/>");
        stringBuilder.append("Pilot:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 10).toString()).append("<br/>");
        stringBuilder.append("Lesson site:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 11).toString()).append("<br/>");
        stringBuilder.append(makeLink("Pre workshop", pre));
        stringBuilder.append(makeLink("Post workshop", post));
        stringBuilder.append("Carpentry:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 14).toString()).append("<br/>");
        stringBuilder.append("Curriculum:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 15).toString()).append("<br/>");
        stringBuilder.append("Flavour:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 16).toString()).append("<br/>");
        stringBuilder.append("EventBrite:\t").append(workshopTableModel.getValueAt(getSelectedRow(), 17).toString()).append("<br/>");
        stringBuilder.append("Schedule:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 18).toString()).append("<br/>");
        stringBuilder.append("Internal ID:\t").append(workshopTableModel.getValueAt(getSelectedRow(), 19).toString()).append("<br/>");
        stringBuilder.append("Registered:\t\t").append(workshopTableModel.getValueAt(getSelectedRow(), 20).toString()).append("<br/>");
        stringBuilder.append("Pretix admin:\t\t").append(pretix_admin).append("<br/>");
        stringBuilder.append("Pretix shop:\t\t").append(pretix_shop).append("<br/>");
        stringBuilder.append(makeLink("Collaborative Doc", collabdoc));
        stringBuilder.append(makeWebsiteLink(organisation, slug));
        stringBuilder.append(makeGitHubLink(organisation, slug));

        stringBuilder.append("<b>Staff:</b><br>")
                .append(SpecificQueriesHelper.getStaff(slug));
        return stringBuilder.toString();
    }

    public WorkshopTableModel getModel() {
        return workshopTableModel;
    }
}
