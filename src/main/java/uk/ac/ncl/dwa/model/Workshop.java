package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Workshop {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String[] columnNames = {"slug", "title", "human date", "human time", "start date", "end date",
            "room_id", "lang", "cntry", "online", "pilot", "inc_lesson_site", "pre_survey", "post_survey",
            "carpentry_code", "curriculum_code", "flavour_id", "eventbrite", "schedule"};

    private String slug;
    private String title;
    private String humandate;
    private String humantime;
    private String startdate;
    private String enddate;
    private String room_id;
    private String language = "en";
    private String country = "gb";
    private boolean online = false;;
    private boolean pilot = false;
    private String inc_lesson_site = "";
    private String pre_survey = "";
    private String post_survey = "";
    private String carpentry_code = "";
    private String curriculum_code = "";
    private String flavour_id = "";
    private String eventbrite = "";
    private String schedule = "";
    private Boolean inserted = false;

    public Workshop() {
        this.logger = LoggerFactory.getLogger(getClass());
        this.slug = "";
        this.title = "";
        this.humandate = "";
        this.humantime = "";
        this.startdate = "";
        this.enddate = "";
        this.room_id = "";
        this.language = "en";
        this.country = "gb";
        this.online = false;
        this.pilot = false;
        this.inc_lesson_site = "";
        this.pre_survey = "";
        this.post_survey = "";
        this.carpentry_code = "";
        this.curriculum_code = "";
        this.flavour_id = "";
    }

    /**
     *
     * @param slug
     * @param title
     * @param humandate
     * @param humantime
     * @param startdate
     * @param enddate
     * @param room_id
     * @param language
     * @param country
     * @param online
     * @param pilot
     * @param inc_lesson_site
     * @param pre_survey
     * @param post_survey
     * @param carpentry_code
     * @param curriculum_code
     * @param flavour_id
     * @param eventbrite
     * @param schedule
     */
    public Workshop(String slug, String title, String humandate, String humantime,
                    String startdate, String enddate, String room_id, String language, String country,
                    boolean online, boolean pilot, String inc_lesson_site, String pre_survey,
                    String post_survey, String carpentry_code, String curriculum_code, String flavour_id,
                    String eventbrite, String schedule) {
        this.slug = slug;
        this.title = title;
        this.humandate = humandate;
        this.humantime = humantime;
        this.startdate = startdate;
        this.enddate = enddate;
        this.room_id = room_id;
        this.language = language;
        this.country = country;
        this.online = online;
        this.pilot = pilot;
        this.inc_lesson_site = inc_lesson_site;
        this.pre_survey = pre_survey;
        this.post_survey = post_survey;
        this.carpentry_code = carpentry_code;
        this.curriculum_code = curriculum_code;
        this.flavour_id = flavour_id;
        this.eventbrite = eventbrite;
        this.schedule = schedule;
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

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isPilot() {
        return pilot;
    }

    public void setPilot(boolean pilot) {
        this.pilot = pilot;
    }

    public String getInc_lesson_site() {
        return inc_lesson_site;
    }

    public void setInc_lesson_site(String inc_lesson_site) {
        this.inc_lesson_site = inc_lesson_site;
    }

    public String getPre_survey() {
        return pre_survey;
    }

    public void setPre_survey(String pre_survey) {
        this.pre_survey = pre_survey;
    }

    public String getPost_survey() {
        return post_survey;
    }

    public void setPost_survey(String post_survey) {
        this.post_survey = post_survey;
    }

    public String getCarpentry_code() {
        return carpentry_code;
    }

    public void setCarpentry_code(String carpentry_code) {
        this.carpentry_code = carpentry_code;
    }

    public String getCurriculum_code() {
        return curriculum_code;
    }

    public void setCurriculum_code(String curriculum_code) {
        this.curriculum_code = curriculum_code;
    }

    public String getFlavour_id() {
        return flavour_id;
    }

    public void setFlavour_id(String flavour_id) {
        this.flavour_id = flavour_id;
    }

    public String getEventbrite() {
        return eventbrite;
    }

    public void setEventbrite(String eventbrite) {
        this.eventbrite = eventbrite;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Boolean getInserted() {
        return inserted;
    }

    public void setInserted(Boolean inserted) {
        this.inserted = inserted;
    }

    public static String[] getColumnNames() {
        return columnNames;
    }

}
