package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;

public class Note {
    private String note;
    private String noteID;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String[] columnNames = {"notes", "notes_id"};
    public static final String[] dbColumnNames = {"notes", "notes_id"};

    public Note(String note, String noteID) {
        this.note = note;
        this.noteID = noteID;
    }

    public Note() {

    }

    public String getNote() {
        return note;
    }

    public String getNote_id() {
        return noteID;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public boolean updateNote() {

        if (DBHandler.getInstance().update(
                "notes",
                Note.dbColumnNames,
                asArray(),
                new String[]{"notes_id=\"" + getNote_id() + "\""})) {
            logger.info("Updated note: {}", getNote());
            return true;
        } else {
            logger.info("Failed to update note: {}", getNote());
            return false;
        }
    }

    public boolean insertNote() {
        if (DBHandler.getInstance().insert("notes",
                asArray())) {
                logger.info("Inserted note: {}", getNote());
                return true;
        }
        return false;
    }

    public String[] asArray() {
        return new String[]{note, noteID};
    }

    public void loadNote() {
        String noteContent = DBHandler.getInstance().selectString("notes", "notes", "notes_id=\""
                + getNote_id() + "\"");
        if (noteContent != null) {
            setNote(noteContent);
        }
    }
}
