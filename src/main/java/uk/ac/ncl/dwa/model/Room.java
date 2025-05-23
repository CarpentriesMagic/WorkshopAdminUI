package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Room {

    private String room_id;
    private String description;
    private String longitude;
    private String latitude;
    private String what_three_words;
    private static final String[] columnNames = {"Room ID", "Description", "Longitude", "Latitude", "What3Words"};
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Room(String room_id, String description, String longitude, String latitude, String what_three_words) {
        this.room_id = room_id;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.what_three_words = what_three_words;
    }

    public Room() {
        this.room_id = "Room ID";
        this.description = "";
        this.longitude = "";
        this.latitude = "";
        this.what_three_words = "";
    }

    public static String[] getColumnNames() {
        return columnNames;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getWhat_three_words() {
        return what_three_words;
    }

    public void setWhat_three_words(String what_three_words) {
        this.what_three_words = what_three_words;
    }
}
