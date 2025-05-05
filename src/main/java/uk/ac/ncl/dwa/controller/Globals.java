package uk.ac.ncl.dwa.controller;

import uk.ac.ncl.dwa.model.Rooms;
import uk.ac.ncl.dwa.model.Workshops;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a singleton and if you don't like singletons you can f*&£ off.
 * It is the only way to make things work reliably and without confusing
 * complexity.
 */
public class Globals {
    static Globals globals;
    Boolean dirty = false;
    Workshops workshops = new Workshops();
    Rooms rooms = new Rooms();
    String connectionString;
    Set<Integer> dirtyRows = new HashSet<>();
    Set<Integer> insertedRows = new HashSet<>();
    /**
     * Dummy contructor to prevent instantiation.
     */
    private Globals() {
    }

    public static Globals getInstance() {
        if (globals == null) {
            globals = new Globals();
        }
        return globals;
    }


    public Boolean getDirty() {
        return dirty;
    }

    public void setDirty(Boolean dirty) {
        this.dirty = dirty;
    }

    public Set<Integer> getDirtyRows() {
        return dirtyRows;
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

    public Set<Integer> getInsertedRows() {
        return insertedRows;
    }

    public Rooms getRooms() {
        return rooms;
    }
}
