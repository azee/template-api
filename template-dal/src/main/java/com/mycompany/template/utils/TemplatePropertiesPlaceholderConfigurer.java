package com.mycompany.template.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 * Date: 7/1/13
 * Time: 5:47 PM
  */

public class TemplatePropertiesPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private Resource[] locations;

    private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

    private boolean ignoreResourceNotFound = false;

    private String fileEncoding;

    /**
     * Set a location of a properties file to be loaded.
     * <p>Can point to a classic properties file or to an XML file
     * that follows JDK 1.5's properties XML format.
     */
    public void setLocation(Resource location) {
        this.locations = new Resource[] {location};
    }

    /**
     * Set locations of properties files to be loaded.
     * <p>Can point to classic properties files or to XML files
     * that follow JDK 1.5's properties XML format.
     * <p>Note: Properties defined in later files will override
     * properties defined earlier files, in case of overlapping keys.
     * Hence, make sure that the most specific files are the last
     * ones in the given list of locations.
     */
    public void setLocations(Resource[] locations) {
        this.locations = locations;
    }

    /**
     * Set if failure to find the property resource should be ignored.
     * <p>"true" is appropriate if the properties file is completely optional.
     * Default is "false".
     */
    public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
        this.ignoreResourceNotFound = ignoreResourceNotFound;
    }

    /**
     * Set the encoding to use for parsing properties files.
     * <p>Default is none, using the <code>java.util.Properties</code>
     * default encoding.
     * <p>Only applies to classic properties files, not to XML files.
     * @see org.springframework.util.PropertiesPersister#load
     */
    public void setFileEncoding(String encoding) {
        this.fileEncoding = encoding;
    }

    /**
     * Load properties into the given instance.
     * @param props the Properties instance to load into
     * @throws java.io.IOException in case of I/O errors
     * @see #setLocations
     */
    protected void loadProperties(Properties props) throws IOException {
        if (locations != null) {
            boolean oneFound = false;
            for (Resource location : this.locations) {
                if (logger.isInfoEnabled()) {
                    logger.info("Loading properties file from " + location);
                }
                InputStream is = null;
                try {
                    logger.info("reading " + location.getFile().getAbsolutePath());
                    is = location.getInputStream();
                    String filename = location.getFilename();
                    if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
                        this.propertiesPersister.loadFromXml(props, is);
                    }
                    else {
                        if (this.fileEncoding != null) {
                            this.propertiesPersister.load(props, new InputStreamReader(is, this.fileEncoding));
                        }
                        else {
                            this.propertiesPersister.load(props, is);
                        }
                    }
                    oneFound = true;
                }

                catch (IOException ex) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
                    }
                }
                finally {
                    if (is != null) {
                        is.close();
                    }
                }

            }
            if (!oneFound) {
                throw new ExceptionInInitializerError("non of specified properties locations could be resolved");
            }
        }
    }

}

