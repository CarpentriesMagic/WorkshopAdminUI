package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;

public class Instructor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    final Logger logger = LoggerFactory.getLogger(getClass());
    private String key_person_id;
    private String key_slug;
    private String person_id;
    private String slug;
    private String name;
    private char status = 'n'; // u - updated, n - new, s - saved
    public static final String[] columnNames = {"Slug", "Person_ID", "Name"};
    public static final String[] dbColumnNames = {"Slug", "Person_ID", "Name"};

    public Instructor() {
        person_id = "Person ID";
        slug = "Slug";
        name = "Name";
    }

    public Instructor(String slug, String person_id, String name) {
        this.person_id = person_id;
        this.slug = slug;
        key_slug = slug;
        key_person_id = person_id;
        this.name = name;
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

    public String getKey_slug() {
        return key_slug;
    }

    public void setKey_slug(String key_slug) {
        this.key_slug = key_slug;
    }

    public String getKey_person_id() {
        return key_person_id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
