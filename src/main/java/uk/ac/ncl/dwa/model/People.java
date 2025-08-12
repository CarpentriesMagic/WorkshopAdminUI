package uk.ac.ncl.dwa.model;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class People extends ArrayList<Person> {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(People.class);

    public People() {
        super();
        loadFromDatabase();
    }

    public void loadFromDatabase() {
        String[] columnNames = Person.dbColumnNames;
        List<Object> people = DBHandler.getInstance().select("people", columnNames, "");
        for (Object object : people) {
            HashMap<String, Object> peopleMap = (HashMap<String, Object>) object;
            Person person = new Person(
                    (String) peopleMap.get(columnNames[0]),
                    (String) peopleMap.get(columnNames[1]),
                    (String) peopleMap.get(columnNames[2]),
                    (String) peopleMap.get(columnNames[3]),
                    (String) peopleMap.get(columnNames[4]),
                    (String) peopleMap.get(columnNames[5]),
                    's'
            );
            add(person);
        }
    }

    public int getColumnCount() {
        return Person.columnNames.length;
    }


    public boolean insertPerson(Person person) {
        logger.debug("Inserting person person id {}", person.getPerson_id());
        String[] values = {person.getPerson_id(), person.getTitle(),
                person.getFirstname(), person.getLastname(),
                person.getCertified(), person.getEmail()};
        if (DBHandler.getInstance().insert("people", values)) {
            person.setStatus('s');
            return true;
        } else return false;
    }

    public void updatePerson(Person person) {
        logger.debug("Updating person {}", person.getPerson_id());
        String[] values = {person.getPerson_id(), person.getTitle(),
                person.getFirstname(), person.getLastname(),
                person.getCertified(), person.getEmail()};
        DBHandler.getInstance().update("people", Person.dbColumnNames, values,
                new String[]{"person_id='" + person.getKey() + "'"});
    }

    /**
     * Add a new setting (also add to insertedRows array for later saving to database)
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, Person element) {
        super.add(index, element);
        System.out.println(element.toString());
    }

    @Override
    public Person remove(int index) {
        Person person = get(index);
        logger.debug("Removing person for key={}",person.getPerson_id());
        DBHandler.getInstance().delete("people",
                new String[]{"person_id"},
                new String[]{person.getKey()});
        super.remove(index);
        return person;
    }

    @Override
    public boolean remove(Object o) {
        String key = (String)o;
        DBHandler.getInstance().delete("people", new String[]{"person_id"},
                new String[]{key});
        super.remove(key);
        return true;
    }

    public String[] getColumnNames() {
        return Person.columnNames;
    }

    /**
     * Select specific people from the database
     *
     * @param certvalue 1 for instructors, 2 for helpers, 0 for everybody else
     * @return
     */
    public static String[] selectedList(int certvalue) {
        ArrayList<String> instructors = new ArrayList<>();
            String where;
            String sql = switch (certvalue) {
                //INSTRUCTORS
                case 1 -> where =  "certified = '" + certvalue + "'";
                // HELPERS
                case 2 -> where = "certified > " + 0;
                // EVERYONE
                default -> where = "";
            };

            List<Object> people = DBHandler.getInstance().select(
                    "people", new String[]{"person_id", "title", "firstname", "lastname"}, where);
            people.forEach(p -> {
                HashMap<String, Object> personMap = (HashMap<String, Object>) p;
                logger.info("{} {}", personMap.get("person_id"), personMap.get("lastname"));
                instructors.add(personMap.get("person_id") + "," +
                        personMap.get("title") + " " +
                        personMap.get("firstname") + " " +
                        personMap.get("lastname"));
            });
        return instructors.toArray(new String[instructors.size()]);
    }



}
