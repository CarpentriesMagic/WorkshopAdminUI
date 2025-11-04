package uk.ac.ncl.dwa.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serial;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.dwa.database.DBHandler;

public class Workshops extends ArrayList<Workshop> {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Serial
    private static final long serialVersionUID = 1L;

    // TODO	There should be a better way of doing this, but in the meantime ..
    Hashtable<String, Integer> id_index = new Hashtable<>();

    public Workshops() {
        super();
        loadFromDatabase();
    }

    /**
     * Find workshop using its ID
     *
     * @param slug the ID of the workshop
     * @return the workshop with the provided ID
     */
    public Workshop returnWorkshop(String slug) {
        return get(id_index.get(slug));
    }

    /**
     * The number of attributes (columns) in the Workshop class.
     *
     * @return an integer containing the number attributes in the workshop class to be used as columns in a table
     */
    public int getColumnCount() {
        return Workshop.getColumnCount();
    }

    /**
     * Get the names of the attributes in the Workshop class to be used column names in a table
     *
     * @return an array of string containing the names of the columns
     */
    public String[] getColumnNames() {
        return Workshop.getColumnNames();
    }

    /**
     * Get the names of all the workshops
     *
     * @return an array of string containing the names of all the workshops
     */
    public String[] getWorkshopNames() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("All");
        this.forEach((ws) -> {
            names.add(ws.getSlug());
        });
        String[] ret = names.toArray(new String[0]);
        logger.trace("Workshop names: {}", ret.length);
        return ret;
    }

    /**
     * Find the name of a workshop given its ID
     *
     * @param id a string containing the ID of the workshop
     * @return the name of the workshop given the ID
     */
    public String getWorkshopName(String id) {
        return get(id_index.get(id)).getSlug();
    }

    /**
     * A list of all the workshop IDs
     *
     * @return an array of string containing all the workshop IDs
     */
    public String[] getSlugs() {
        ArrayList<String> ids = new ArrayList<String>();
        ids.add("All");
        this.forEach((ws) -> {
            ids.add(ws.getSlug());
        });
        String[] ret = ids.toArray(new String[0]);
        logger.trace("Workshop slugs: " + ret.length);
        return ret;
    }

    /**
     * Check whether a workshop exists for the given ID
     *
     * @param slug a string containing the ID of the workshop
     * @return true if a workshop with the given ID was found, otherwise false
     */
    public boolean exists(String slug) {
        for (Workshop workshop : this) {
            if (workshop.getSlug().equals(slug))
                return true;
        }
        return false;
    }

    /**
     * Override the add method in order to update a hashtable that contains the
     * workshopID as the key and the index of the workshop in the array as the value
     *
     * @param workshop element whose presence in this collection is to be ensured
     * @return true if the workshop was successfully added
     */
    @Override
    public boolean add(Workshop workshop) {
        logger.trace("Add workshop: {}", workshop.getTitle());
        boolean ret = super.add(workshop);
        id_index.put(workshop.getTitle(), this.size() - 1);
        return ret;
    }

    public void loadFromDatabase() {

        loadFromDatabase("2020", "3000");
    }

    public void loadFromDatabase(String startdate, String enddate) {
        String[] columnNames = Workshop.dbColumnNames;
        String where = "";
        if (!startdate.trim().isEmpty()) {
            where = "slug >= '" + startdate + "'";
        }
        if (!enddate.trim().isEmpty()) {
            where += "and slug <= '" + enddate + "'";
        }
        clear();
        List<Object> workshops = DBHandler.getInstance().select(
                "workshops", columnNames, where, "slug");
        for (Object o : workshops) {
            HashMap<String,Object> workshopObject = (HashMap<String, Object>) o;
            Workshop workshop = new Workshop((String)workshopObject.get(columnNames[0]),
                    (String)workshopObject.get(columnNames[1]),
                    (String)workshopObject.get(columnNames[2]),
                    (String)workshopObject.get(columnNames[3]),
                    (String)workshopObject.get(columnNames[4]),
                    (String)workshopObject.get(columnNames[5]),
                    (String)workshopObject.get(columnNames[6]),
                    (String)workshopObject.get(columnNames[7]),
                    (String)workshopObject.get(columnNames[8]),
                    ((workshopObject.get(columnNames[9])).equals("1")),
                    ((workshopObject.get(columnNames[10])).equals("1")),
                    (String)workshopObject.get(columnNames[11]),
                    (String)workshopObject.get(columnNames[12]),
                    (String)workshopObject.get(columnNames[13]),
                    (String)workshopObject.get(columnNames[14]),
                    (String)workshopObject.get(columnNames[15]),
                    (String)workshopObject.get(columnNames[16]),
                    (String)workshopObject.get(columnNames[17]),
                    (String)workshopObject.get(columnNames[18]),
                    (String)workshopObject.get(columnNames[19]),
                    ((workshopObject.get(columnNames[20])).equals("1"))
                    );
            workshop.setStatus('s');
            add(workshop);
            logger.info("Load workshop: {}",workshop.getSlug());
        }
    }

    public boolean updateWorkshop(Workshop workshop) {

        return DBHandler.getInstance().update(
                "workshops",
                Workshop.dbColumnNames,
                workshop.asArray(),
                new String[]{"slug=\"" + workshop.getKey() + "\""});
    }

    public boolean insertWorkshop(Workshop workshop) {
        if (DBHandler.getInstance().insert("workshops",
                workshop.asArray())) {
            workshop.setStatus('s');
            return true;
        }
        return false;
    }


    public boolean importWorkshopFromCSV(String tsvFile) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(tsvFile));
        logger.info("Importing workshop from file {}", tsvFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split("\t");
            logger.info("values: {}", values.length);
            if (values.length == 21) {
                Workshop workshop = new Workshop(values[0], values[1], values[2], values[3], values[4], values[5],
                        values[6], values[7], values[8], values[9].equals("1"), values[10].equals("1"), values[11],
                        values[12], values[13], values[14], values[15], values[16], values[17], values[18], values[19],
                        values[20].equals("1"));
                if (insertWorkshop(workshop)) {
                    logger.info("Imported workshop: {}", workshop.getSlug());
                } else {
                    logger.error("Failed to import workshop: {}", workshop.getSlug());
                    return false;
                }
            }
        }
        return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Workshop remove(int index) {
        Workshop workshop = get(index);
        super.remove(index);
        if (workshop.getStatus() == 's') {
            logger.debug("Removing workshop for key={}", workshop.getSlug());
            if (!DBHandler.getInstance().delete("workshops", new String[]{"slug"},
                    new String[]{workshop.getSlug()}))
                workshop = null;
        }
        return workshop;
    }
}