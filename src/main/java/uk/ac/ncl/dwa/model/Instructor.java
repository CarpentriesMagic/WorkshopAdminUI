package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;

public class Instructor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    final Logger logger = LoggerFactory.getLogger(getClass());
    private String person_id;
    private String slug;
    private Boolean inserted = false;
    private static final String[] columnNames = {"Person_ID", "Slug"};

    public Instructor() {
        this.person_id = "Person ID";
        this.slug = "Slug";
    }

    public Instructor(String person_id, String slug) {
        this.person_id = person_id;
        this.slug = slug;
    }

    public static String[] getColumnNames() {
        return columnNames;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getInserted() {
        return inserted;
    }

    public void setInserted(Boolean inserted) {
        this.inserted = inserted;
    }
}
