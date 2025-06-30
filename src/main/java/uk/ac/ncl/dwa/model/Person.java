package uk.ac.ncl.dwa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Person {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String[] columnNames = {"Person_id", "title", "first name", "last name", "Certified", "Email"};
    public static final String[] dbColumnNames = {"person_id", "title", "firstname", "lastname", "Certified", "Email"};
    private String key; // person_id before any changes made to record - needed for updating
    private String person_id;
    private String title;
    private String firstname;
    private String lastname;
    private String certified;
    private String email;
    private Boolean inserted = false;
    private char status = 'n'; // u - updated, n - new, s - saved

    public Person() {
        person_id = "";
        title = "";
        firstname = "";
        lastname = "last name";
        certified = "0";
        email = "";
    }

    public Person(String person_id, String title, String firstname, String lastname, String certified, String email, char status) {
        this.key = person_id;
        this.person_id = person_id;
        this.title = title;
        this.firstname = firstname;
        this.lastname = lastname;
        this.certified = certified;
        this.email = email;
        this.status = status;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCertified() {
        return certified;
    }

    public void setCertified(String certified) {
        this.certified = certified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getInserted() {
        return inserted;
    }

    public void setInserted(Boolean inserted) {
        this.inserted = inserted;
    }

    public static int getColumnCount() {
        return columnNames.length;
    }

    public String getKey() {
        return this.key;
    }

    /**
     * Return record status, u - update, n - new, s - saved
     * @return record status
     */
    public char getStatus() {
        return status;
    }

    /**
     * Set record status,
     * @param status u - update, n - new, s - saved
     */
    public void setStatus(char status) {
        this.status = status;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
