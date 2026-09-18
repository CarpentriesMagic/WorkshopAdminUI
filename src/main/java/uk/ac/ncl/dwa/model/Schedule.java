package uk.ac.ncl.dwa.model;

/**
 * A schedule is the abbreviation used to indicate which HTML schedule should be included in the workshop website
 */
public class Schedule {
    String schedule_id;

    /**
     * Constructor with a specified schedule ID
     * @param schedule_id
     */
    public Schedule(String schedule_id) {
        this.schedule_id = schedule_id;

    }

    /**
     * No parameter constructor
     */
    public Schedule() {
        schedule_id = "";
    }

    /**
     * Return the schedule ID
     * @return schedule_id
     */
    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }
}
