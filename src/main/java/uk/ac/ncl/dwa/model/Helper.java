package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;

public class Helper implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    final Logger logger = LoggerFactory.getLogger(getClass());
    private String key_person_id;
    private String key_slug;
    private String person_id;
    private String slug;
    private String name;
    private Boolean inserted = false;
    private char status = 'n';
    public static final String[] columnNames = {"Slug", "Person_ID", "Name"};
    public static final String[] dbColumnNames = {"Slug", "Person_ID", "Name"};

    public Helper() {
        this.person_id = "Person ID";
        this.slug = "Slug";
        this.name = "Name";
    }

    public Helper(String slug, String person_id, String name, char status) {
        this.person_id = person_id;
        this.slug = slug;
        this.name = name;
        this.key_slug = slug;
        this.key_person_id = person_id;
        this.status = status;
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

    public String getKey_person_id() {
        return key_person_id;
    }

    public String getKey_slug() {
        return key_slug;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public void setKey_person_id(String key_person_id) {
        this.key_person_id = key_person_id;
    }

    public void setKey_slug(String key_slug) {
        this.key_slug = key_slug;
    }

    @Override
    public String toString() {
        return String.join(",", new String[]{slug, person_id, name});
    }
}
