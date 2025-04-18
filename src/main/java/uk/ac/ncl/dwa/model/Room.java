package uk.ac.ncl.dwa.model;

public class Room {

    private String room_id;
    private String description;
    private String longitude;
    private String latitude;
    private String what_three_words;

    public Room(String room_id, String description, String longitude, String latitude, String what_three_words) {
        this.room_id = room_id;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.what_three_words = what_three_words;
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
