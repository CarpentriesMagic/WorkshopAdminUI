package uk.ac.ncl.dwa.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.Workshop;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SpecificQueriesHelper {
    private static final Logger logger = LoggerFactory.getLogger(SpecificQueriesHelper.class);

    public static List getPeople(String repo, String who) {
        String[] columnNames = new String[]{"title", "firstname", "lastname", "email"};
        String sql = "SELECT " +
                "p.title as title, p.firstname as firstname, " +
                "p.lastname as lastname, " +
                "p.email as email " +
                "FROM " + who + " as h " +
                "JOIN people as p on p.person_id=h.person_id " +
                "WHERE h.slug=\"" + repo + "\" ";
        logger.info(sql);
        return DBHandler.getInstance().query(sql, columnNames);
    }

    /**
     * Query the database for all helpers and instructors
     * @param slug The identifier of the GitHub repository which is also the workshop slug
     * @return A string formatted as an HTML list
     */
    public static String getStaff(String slug) {
        String[] columnNames = new String[]{"title", "firstname", "lastname", "email"};
        List helpers = getPeople(slug, "helpers");
        StringBuilder sb_helper = new StringBuilder();
        for (Object object : helpers) {
            HashMap<String, Object> helperMap = (HashMap<String, Object>) object;
            String helper =
                    ((String) helperMap.get(columnNames[0]) + " " +
                            (String) helperMap.get(columnNames[1]) + " " +
                            (String) helperMap.get(columnNames[2])).trim() + ", " +
                            (String) helperMap.get(columnNames[3]);
            sb_helper.append("<li>").append(helper).append("</li>");
        }
        String helperlist = sb_helper.toString();
        helperlist = (!helperlist.isEmpty() ?helperlist.substring(0,helperlist.length()-1):"");
        List<Object> instructors = getPeople(slug, "instructors");
        StringBuilder sb_instructor = new StringBuilder();
        for (Object object : instructors) {
            HashMap<String, Object> instructorMap = (HashMap<String, Object>) object;
            String instructor =
                    ((String) instructorMap.get(columnNames[0]) + " " +
                            (String) instructorMap.get(columnNames[1]) + " " +
                            (String) instructorMap.get(columnNames[2])).trim() + ", " +
                            (String) instructorMap.get(columnNames[3]);
            sb_instructor.append("<li>").append(instructor).append("</li>");
        }
        String instructorlist = sb_instructor.toString();
        instructorlist = (!instructorlist.isEmpty()?instructorlist.substring(0,instructorlist.length()-1):"");
        return "<b>Helpers</b><br/><ol>" + helperlist + "</ol><br/><b>Instructors:</b><br/><ol>" + instructorlist +
                "</ol>";
    }

    public static String getHelpers(String repo) {
        String[] columnNames = new String[]{"title", "firstname", "lastname", "email"};
        List helpers = getPeople(repo, "helpers");
        StringBuilder sb_helper = new StringBuilder();
//        sb_helper.append("[");
        for (Object object : helpers) {
            HashMap<String, Object> helperMap = (HashMap<String, Object>) object;
            String helper =
                    ((String) helperMap.get(columnNames[0]) + " " +
                            (String) helperMap.get(columnNames[1]) + " " +
                            (String) helperMap.get(columnNames[2])).trim();
            sb_helper.append('"').append(helper).append('"').append(",");
        }
        String helperlist = sb_helper.toString();
        logger.info(helperlist);
        helperlist = "[" + (!helperlist.isEmpty() ?helperlist.substring(0,helperlist.length()-1):"") + "]";
        logger.info(helperlist);
        return helperlist;
    }

    public static String getInstructors(String repo) {
        String[] columnNames = new String[]{"title", "firstname", "lastname", "email"};
        List<Object> instructors = getPeople(repo, "instructors");
        StringBuilder sb_instructor = new StringBuilder();
//        sb_instructor.append("[");
        for (Object object : instructors) {
            HashMap<String, Object> instructorMap = (HashMap<String, Object>) object;
            String helper =
                    ((String) instructorMap.get(columnNames[0]) + " " +
                            (String) instructorMap.get(columnNames[1]) + " " +
                            (String) instructorMap.get(columnNames[2])).trim();
            sb_instructor.append('"').append(helper).append('"').append(",");
        }
        String instructorlist = sb_instructor.toString();
        instructorlist = "[" + (!instructorlist.isEmpty()?instructorlist.substring(0,instructorlist.length()-1):"") + "]";
        return instructorlist;
    }

    public static String getPersonInstructorStatus(String person_id, String start_date, String end_date) {
        String[] columnNames = new String[]{"count"};
        String sql = "SELECT count(*) as count from instructors as i where"
                + " i.slug > \"" + start_date + "\" and i.person_id = \"" + person_id + "\"";
        logger.info(sql);
        List<Object> count = DBHandler.getInstance().query(sql, columnNames);
        HashMap<String, Object> c = (HashMap<String, Object>) count.get(0);
        return (String) c.get("count");
    }

    public static String getPersonHelperStatus(String person_id, String start_date, String end_date) {
        String[] columnNames = new String[]{"count"};
        String sql = "SELECT count(*) as count from helpers as h where"
                + " h.slug > \"" + start_date + "\" and h.person_id = \"" + person_id + "\"";
        logger.info(sql);
        List<Object> count = DBHandler.getInstance().query(sql, columnNames);
        HashMap<String, Object> c = (HashMap<String, Object>) count.get(0);
        return (String) c.get("count");
    }

    public static String getAllStatus(String table, String start_date, String end_date) {
        String[] columnNames = new String[]{"count", "firstname", "lastname", "email"};
        String sql = "select count(*) as count, p.firstname as firstname, p.lastname as lastname, p.email from "
                + table + " as i " + "join people as p on i.person_id = p.person_id where i.slug > \"" +
                start_date +  "\" group by i.person_id order by p.firstname";
        logger.info(sql);
        List<Object> counts = DBHandler.getInstance().query(sql, columnNames);
        StringBuilder sb = new StringBuilder();
        counts.forEach(record -> {
            HashMap<String, Object> r = (HashMap<String, Object>) record;
            sb.append(r.get("count") + "\t" + r.get("firstname") + "\t" + r.get("lastname") + "\t" + r.get("email") +
                    "\n");
        });
        return sb.toString();

    }

}
