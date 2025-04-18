package uk.ac.ncl.dwa.model;

public class RoomListItem {

    String room_id;
    String description;

    public RoomListItem(String room_id, String description) {
        this.room_id = room_id;
        this.description = description;
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
    @Override
    public String toString() {
        return "RoomList [room_id=" + room_id + ", description=" + description + "]";
    }
}
