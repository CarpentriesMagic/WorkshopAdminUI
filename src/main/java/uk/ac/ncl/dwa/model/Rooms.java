package uk.ac.ncl.dwa.model;

import java.sql.DriverManager;
import java.util.ArrayList;
import org.mariadb.jdbc.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rooms extends ArrayList<Room> {
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(getClass());
    Rooms rooms = this;

    public Rooms() {
        super();
    }

    public void loadFromDatabase(String connectionString) {
        try (Connection conn = (Connection) DriverManager.getConnection(connectionString)) {
            String sql = "SELECT * FROM room";
            var statement = conn.createStatement();
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Room room = new Room(resultSet.getString("room_id"), resultSet.getString("description"),
                        resultSet.getString("longitude"), resultSet.getString("latitude"),
                        resultSet.getString("what_three_words"));
                rooms.add(room);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public ArrayList<RoomListItem> loadRoomList(String connectionString) {
        ArrayList<RoomListItem> result = new ArrayList<>();
        try (Connection conn = (Connection) DriverManager.getConnection(connectionString)) {
            String sql = "SELECT * FROM room";
            var statement = conn.createStatement();
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                RoomListItem roomList = new RoomListItem(resultSet.getString("room_id"),
                        resultSet.getString("description"));
                result.add(roomList);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return result;
    }

}
