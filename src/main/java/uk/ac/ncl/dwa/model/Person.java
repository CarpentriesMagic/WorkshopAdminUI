package uk.ac.ncl.dwa.model;

public class Person {
    private String person_id;
    private String title;
    private String firstname;
    private String lastname;
    private String certified;
    private String email;
    private static final String[] columnNames = {"Person_id", "title", "first name", "last name", "Certified", "Email"};
    private Boolean inserted = false;


    public Person() {
        person_id = "Person ID";
        title = "title";
        firstname = "first name";
        lastname = "last name";
        certified = "0 = no, 1 = yes?";
        email = "email";
    }

    public Person(String person_id, String title, String firstname, String lastname, String certified, String email) {
        this.person_id = person_id;
        this.title = title;
        this.firstname = firstname;
        this.lastname = lastname;
        this.certified = certified;
        this.email = email;
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

    public static String[] getColumnNames() {
        return columnNames;
    }

    public static int getColumnCount() {
        return columnNames.length;
    }


}
