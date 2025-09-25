package uk.ac.ncl.dwa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;

public class Rooms extends ArrayList<Room> {
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(getClass());
    Rooms rooms = this;


    public Rooms() {
        super();
        loadFromDatabase();
    }


    public void loadFromDatabase() {
        String[] columnNames = Room.dbColumnNames;
        List<Object> rooms = DBHandler.getInstance().select("room",
                columnNames, "", "");
        for (Object o : rooms) {
            HashMap<String,Object> settingObject = (HashMap<String, Object>) o;
            Room room = new Room((String)settingObject.get(columnNames[0]),
                    (String)settingObject.get(columnNames[1]),
                    (String)settingObject.get(columnNames[2]),
                    (String)settingObject.get(columnNames[3]),
                    (String)settingObject.get(columnNames[4]),
                    's'
            );
            add(room);
            logger.info("Load room {}",room.getRoom_id());
        }
    }

    public ArrayList<String> loadRoomList(String connectionString) {
        List<Object> rooms = DBHandler.getInstance().select("room", new String[]{"room_id"}, "", "");
        ArrayList<String> result = new ArrayList<>();
        for (Object o : rooms) {
            HashMap<String,Object> room = (HashMap<String, Object>) o;
            result.add(room.get("room_id").toString());
        }
        return result;
    }

    public int getColumnCount() {
        return Room.getColumnNames().length;
    }

    public String[] getColumnNames() {
        return Room.getColumnNames();
    }

    public void insertRoom(Room room) {
        logger.debug("Inserting room room_id {}", room.getRoom_id());
        String[] values = {room.getRoom_id(), room.getDescription(),
        room.getLongitude(), room.getLatitude(),
        room.getWhat_three_words()};
        DBHandler.getInstance().insert("room", values);
    }

    public void updateRoom(Room room) {
        logger.debug("Updating rooms {}", room.getRoom_id());
        String[] columns = {"room_id", "description", "longitude", "latitude", "what_three_words"};
        String[] values = {room.getRoom_id(), room.getDescription(),
        room.getLongitude(), room.getLatitude(),
        room.getWhat_three_words()};
        DBHandler.getInstance().update("room", columns, values,
                new String[]{"room_id='" + room.getKey() + "'"});

    }

    @Override
    public Room remove(int index) {
        Room room = get(index);
        logger.debug("Removing room_id={}",room.getRoom_id());
        if (DBHandler.getInstance().delete("room", new String[]{"room_id"},
                new String[]{room.getRoom_id()})) {
            super.remove(index);
            return room;
        } else return null;
    }

    @Override
    public boolean remove(Object o) {
        Room room = (Room)o;
        DBHandler.getInstance().delete("room", new String[]{"room_id"},
                new String[]{room.getRoom_id()});
        super.remove(room);
        return true;
    }


}
