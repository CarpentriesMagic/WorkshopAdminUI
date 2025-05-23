package uk.ac.ncl.dwa.model;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mariadb.jdbc.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.controller.Globals;

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

    public boolean insertRooms() {
        Connection connection = null;
        boolean success = false;
        logger.info("Updating " + Globals.getInstance().getEditedRows("rooms").size() + " rows");
        Globals.getInstance().getInsertedRows("rooms").forEach(row -> {
            logger.info("Saving row: " + row);
        });
        try {
            connection = (Connection) DriverManager.getConnection(Globals.getInstance().getConnectionString());
            String sql = "INSERT room VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            //Globals.getInstance().getInsertedRows().forEach(row -> {
            for (int row : Globals.getInstance().getInsertedRows("rooms")) {
                Room room = this.get(row);
                logger.info("Inserting room " + room.getRoom_id());
                try {
                    statement.setString(1, room.getRoom_id());
                    statement.setString(2, room.getDescription());
                    statement.setString(3, room.getLongitude());
                    statement.setString(4, room.getLatitude());
                    statement.setString(5, room.getWhat_three_words());
                    statement.executeUpdate();
                    success = true;
                } catch (SQLException e) {
                    success = false;
                    throw new RuntimeException(e);
                }
            }
            success = true;
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating rooms in database", e);
        }
        return success;
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
