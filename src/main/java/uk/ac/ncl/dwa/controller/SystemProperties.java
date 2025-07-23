package uk.ac.ncl.dwa.controller;

import java.util.HashMap;

/**
 * This is a singleton
 */
public class SystemProperties {

    static SystemProperties sysProperties = null;
    private static final HashMap<String, String> hsh_properties = new HashMap<String, String>();
    
    private SystemProperties() {
        hsh_properties.put("dbServer","localhost");
        hsh_properties.put("dbPort","3306");
        hsh_properties.put("dbName","workshopadmin");
        hsh_properties.put("dbUser","workshopadmin");
        hsh_properties.put("dbPass","w0rksh0p");
        hsh_properties.put("connectionString","jdbc:sqlite:emptydatabase.sqlite");
        hsh_properties.put("dbType","sqlite");

    }

    public static SystemProperties getInstance() {
        if (sysProperties == null) {
            sysProperties = new SystemProperties();
        }
        return sysProperties;
    }

    public HashMap<String, String> getProperties() {
        return hsh_properties;
    }
}
