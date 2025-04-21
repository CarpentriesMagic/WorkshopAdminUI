package uk.ac.ncl.dwa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

public class Utilities {

    private static final String APPLICATION_DIRECTORY = ""; //System.getProperty("user.home");
    private static final String PROPERTIES_FILEPATH = APPLICATION_DIRECTORY + "./workshopadmin.properties";
    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);
    private static final SystemProperties systemProperties = SystemProperties.getInstance();

    /**
     * Create a new properties file and set default properties
     */
    public static Properties createPropertiesFile() {
        Properties properties;
        File propertiesFile = new File(PROPERTIES_FILEPATH);
        try {
            if (!Files.exists(Paths.get(PROPERTIES_FILEPATH))) {
                System.out.println("Creating properties file: " + PROPERTIES_FILEPATH);
                logger.info("Creating properties file: {}", PROPERTIES_FILEPATH);
                properties = new Properties();
                OutputStream output = new FileOutputStream(propertiesFile);
                systemProperties.getProperties().forEach(properties::setProperty);
                properties.store(output, null);
                logger.trace("File {} created", propertiesFile.getAbsolutePath());
            } else {
                properties = Utilities.loadPropertiesFile();
                System.out.println("Read properties file: " + PROPERTIES_FILEPATH);
                logger.info("Read properties file: {}", PROPERTIES_FILEPATH);
                HashMap<String, String> propertiesMap = systemProperties.getProperties();
                propertiesMap.forEach((key, value) -> {
                    if (properties.getProperty(key) != null) {
                        logger.info("{} key found in properties file: {}", key, properties.getProperty(key));
                        if (System.getenv(key) != null) {
                            logger.info("Alternative value found in environment: {}", System.getenv(key));
                            properties.setProperty(key, System.getenv(key));
                        }
                    } else {
                        logger.warn("{} key not found, check environment", key);
                        if (System.getenv(key) != null) {
                            logger.info("Alternative value found in system properties: {}", System.getenv(key));
                            properties.setProperty(key, System.getenv(key));
                        } else {
                            logger.info("Alternative value not found in system properties, using default {}",
                                    systemProperties.getProperties().get(key));
                            properties.setProperty(key, systemProperties.getProperties().get(key));
                        }
                    }
                });

            }
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Load properties from the properties file
     */
    public static Properties loadPropertiesFile() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILEPATH)) {
            // load a properties file
            properties.load(input);
        } catch (IOException ex) {
            logger.error("Unable to load properties file: {}", PROPERTIES_FILEPATH, ex);
        }
        return properties;
    }
}