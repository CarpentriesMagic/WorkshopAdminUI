package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Workshop {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String[] columnNames = {"slug", "title", "human date", "human time", "start date", "end date"};

    private String slug;
    private String title;
    private String humandate;
    private String humantime;
    private String startdate;
    private String enddate;

    public Workshop(Logger logger, String slug, String title, String humandate, String humantime, String startdate, String enddate) {
        this.logger = logger;
        this.slug = slug;
        this.title = title;
        this.humandate = humandate;
        this.humantime = humantime;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHumandate() {
        return humandate;
    }

    public void setHumandate(String humandate) {
        this.humandate = humandate;
    }

    public String getHumantime() {
        return humantime;
    }

    public void setHumantime(String humantime) {
        this.humantime = humantime;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public static int getColumnCount() {
        return columnNames.length;
    }

    public static String[] getColumnNames() {
        return columnNames;
    }

}
