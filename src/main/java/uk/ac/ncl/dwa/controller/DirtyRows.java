package uk.ac.ncl.dwa.controller;

import java.util.HashSet;
import java.util.Set;

/**
 * A class to hold two sets of rows, one for edited rows and one for inserted rows.
 */
public class DirtyRows {
    private Set<Integer> editedRows;
    private Set<Integer> insertedRows;

    /**
     * Constructor to initialize the dirty rows and inserted rows sets.
     */
    public DirtyRows() {
        editedRows = new HashSet<>();
        insertedRows = new HashSet<>();
    }

    /**
     * Gets the edited rows.
     *
     * @return the set of edited rows
     */
    public Set<Integer> getEditedRows() {
        return editedRows;
    }

    /**
     * Sets the edited rows.
     *
     * @param editedRows the set of dirty rows
     */
    public void setEditedRows(Set<Integer> editedRows) {
        this.editedRows = editedRows;
    }

    /**
     * Gets the inserted rows.
     *
     * @return the set of inserted rows
     */
    public Set<Integer> getInsertedRows() {
        return insertedRows;
    }
    /**
     * Sets the inserted rows.
     *
     * @param insertedRows the set of inserted rows
     */
    public void setInsertedRows(Set<Integer> insertedRows) {
        this.insertedRows = insertedRows;
    }
}
