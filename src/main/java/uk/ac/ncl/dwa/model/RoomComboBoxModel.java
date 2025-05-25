package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.Serial;
import java.util.ArrayList;

public class RoomComboBoxModel extends DefaultComboBoxModel<String> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private int selected = 0;
    ArrayList<String> rooms;

    public RoomComboBoxModel(ArrayList<String> rooms) {
        this.rooms = rooms;
        logger.info("Loading {} rooms", rooms.size());
    }

    @Override
    public String getSelectedItem() {
        logger.info("Selected item: {}", selected);
        if (selected > -1) {
            return rooms.get(selected);
        } else {
            selected = 0;
            return " ";
        }
    }

    @Override
    public void addElement(String room) {
        rooms.add((String) room);
    }

    @Override
    public int getSize() {
        return rooms.size();
    }

    @Override
    public String getElementAt(int index) {
        selected = index;
        return rooms.get(index);
    }
}
