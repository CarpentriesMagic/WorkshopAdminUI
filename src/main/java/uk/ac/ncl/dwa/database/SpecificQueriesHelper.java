package uk.ac.ncl.dwa.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.model.Workshop;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SpecificQueriesHelper {
    private static final Logger logger = LoggerFactory.getLogger(SpecificQueriesHelper.class);

    public static String getHelpers(String repo) {
        String[] columnNames = new String[]{"title", "firstname", "lastname"};
        String sql = "SELECT " +
                "p.title as title, p.firstname as firstname, " +
                "p.lastname as lastname " +
                "FROM helpers as h " +
                "JOIN people as p on p.person_id=h.person_id " +
                "WHERE h.slug=\"" + repo + "\" ";
        logger.info(sql);
        List<Object> helpers = DBHandler.getInstance().query(sql, columnNames);
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
        String[] columnNames = new String[]{"title", "firstname", "lastname"};
        String sql = "SELECT " +
                "p.title as title, p.firstname as firstname, " +
                "p.lastname as lastname " +
                "FROM instructors as h " +
                "JOIN people as p on p.person_id=h.person_id " +
                "WHERE h.slug=\"" + repo + "\" ";
        logger.info(sql);
        List<Object> instructors = DBHandler.getInstance().query(sql, columnNames);
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
                + " i.slug > \"2025-08\" and i.person_id = \"" + person_id + "\"";
        logger.info(sql);
        List<Object> count = DBHandler.getInstance().query(sql, columnNames);
        HashMap<String, Object> c = (HashMap<String, Object>) count.get(0);
        return (String) c.get("count");
    }

    public static String getPersonHelperStatus(String person_id, String start_date, String end_date) {
        String[] columnNames = new String[]{"count"};
        String sql = "SELECT count(*) as count from helpers as h where"
                + " h.slug > \"2025-08\" and h.person_id = \"" + person_id + "\"";
        logger.info(sql);
        List<Object> count = DBHandler.getInstance().query(sql, columnNames);
        HashMap<String, Object> c = (HashMap<String, Object>) count.get(0);
        return (String) c.get("count");
    }

}
