package uk.ac.ncl.dwa.model;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;
import uk.ac.ncl.dwa.database.DBHandler;

public class Rooms extends ArrayList<Room> {
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(getClass());
    Rooms rooms = this;


    public Rooms() {
        super();
        loadFromDatabase();
    }


    public List<Object> loadFromDatabase() {
        String[] columnNames = Room.dbColumnNames;
        List<Object> rooms = DBHandler.getInstance().select("room",
                columnNames, "");
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
        return rooms;
    }

    public ArrayList<String> loadRoomList(String connectionString) {
        List<Object> rooms = DBHandler.getInstance().select("room", new String[]{"room_id"}, "");
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

    public boolean updateRooms() {
        Connection connection = null;
        boolean success = false;
        logger.info("Updating " + Globals.getInstance().getEditedRows("rooms").size() + " rows");
        Globals.getInstance().getEditedRows("rooms").forEach(row -> {
            logger.info("Saving row: " + row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "UPDATE room SET description = ?, longitude = ?, latitude = ?, " +
                    "what_three_words = ? WHERE room_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            for (int row : Globals.getInstance().getEditedRows("rooms")) {
                logger.info("Updating row: " + row);
                Room room = this.get(row);
                logger.info("Updating rooms " + room.getRoom_id());
                try {
                    statement.setString(1, room.getDescription());
                    statement.setString(2, room.getLongitude());
                    statement.setString(3, room.getLatitude());
                    statement.setString(4, room.getWhat_three_words());
                    statement.setString(5, room.getRoom_id());

                    statement.executeUpdate();
                    success = true;
                } catch (SQLException e) {
                    success = false;
                    throw new RuntimeException(e);
                }
            }
            connection.close();
        } catch (SQLException e) {
            success = false;
            throw new RuntimeException("Error updating rooms in database", e);
        }
        return success;
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
                new String[]{"room_id='" + room.getRoom_id() + "'"});

    }


    @Override
    public Room remove(int index) {
        Room room = get(index);
        logger.debug("Removing room_id={}",room.getRoom_id());
        DBHandler.getInstance().delete("room", "room_id",
                new String[]{room.getRoom_id()});
        super.remove(index);
        return room;
    }

    @Override
    public boolean remove(Object o) {
        Room room = (Room)o;
        DBHandler.getInstance().delete("room", "room_id",
                new String[]{room.getRoom_id()});
        super.remove(room);
        return true;
    }

    public void deleteRoom(String key) {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "DELETE FROM room WHERE room_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, key);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting room from database", e);
        }
    }

}
