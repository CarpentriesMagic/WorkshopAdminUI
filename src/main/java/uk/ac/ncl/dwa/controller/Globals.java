package uk.ac.ncl.dwa.controller;

import uk.ac.ncl.dwa.model.*;
import java.util.Hashtable;
import java.util.Set;

/**
 * This is a singleton and if you don't like singletons you can f*&£ off.
 * It is the only way to make things work reliably and without confusing
 * complexity.
 */
public class Globals {
    public static Globals globals;
    private Boolean dirty = false;
    private Workshops workshops = new Workshops();
    private Rooms rooms = new Rooms();
    private Instructors instructors = new Instructors();
    private Helpers helpers = new Helpers();
    private People people = new People();
    private String connectionString;
    private Hashtable<String, DirtyRows> dirtyRows = new Hashtable<>();

    /**
     * Dummy contructor to prevent instantiation.
     */
    private Globals() {
        //rooms.loadFromDatabase(getInstance().getConnectionString());
    }

    /**
     * Returns the single instance of the Globals class.
     *
     * @return the single instance of Globals
     */
    public static Globals getInstance() {
        if (globals == null) {
            globals = new Globals();
            globals.dirtyRows.put("workshops", new DirtyRows());
            globals.dirtyRows.put("rooms", new DirtyRows());
            globals.dirtyRows.put("instructors", new DirtyRows());
            globals.dirtyRows.put("helpers", new DirtyRows());
            globals.dirtyRows.put("people", new DirtyRows());
        }
        return globals;
    }


    public Boolean getDirty() {
        return dirty;
    }

    public void setDirty(Boolean dirty) {
        this.dirty = dirty;
    }

    public Set<Integer> getEditedRows(String key) {
        return dirtyRows.get(key).getEditedRows();
    }

    public Workshops getWorkshops() {
        return workshops;
    }

    public void setWorkshops(Workshops workshops) {
        this.workshops = workshops;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public Set<Integer> getInsertedRows(String key) {
        return dirtyRows.get(key).getInsertedRows();
    }

    public Rooms getRooms() {
        return rooms;
    }

    public Instructors getInstructors() {
        return instructors;
    }

    public Helpers getHelpers() {
        return helpers;
    }

    public People getPeople() {
        return people;
    }
}
