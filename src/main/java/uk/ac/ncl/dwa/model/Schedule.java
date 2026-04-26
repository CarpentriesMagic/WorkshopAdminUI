package uk.ac.ncl.dwa.model;

public class Schedule {
    String schedule_id;

    public Schedule(String schedule_id) {
        this.schedule_id = schedule_id;

    }

    public Schedule() {
        schedule_id = "";
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }
}
