package org.aksw.simba.owl2nl.qr.tools;

import au.com.bytecode.opencsv.CSVReader;
import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataInserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInserter.class);

    private static final String PROPERTIES_FILE_NAME = "OWL2NL_QR.properties";
    private static final String DRIVER_CLASS_NAME_PROPERTY_KEY = "org.aksw.simba.owl2nl.qr.db.driverClassName";
    private static final String DB_FILE_PROPERTY_KEY = "org.aksw.simba.owl2nl.qr.db.file";
    private static final String DB_URL_FIRST_PART = "jdbc:hsqldb:file:";
    private static final String SELECT_EXPERIMENT = "SELECT id FROM Experiments WHERE axiom=? AND verbalization=?";
    private static final String INSERT_EXPERIMENT = "INSERT INTO Experiments(category, axiom, verbalization) VALUES(?,?,?)";

    public static void main(String[] args) {
        JdbcTemplate jdbctemplate = getJdbcTemplate();
        if (jdbctemplate == null) {
            return;
        }
        DataInserter inserter = new DataInserter(jdbctemplate);
        inserter.run(new File("axiomConversionResults_koala.csv"), 0);
        inserter.run(new File("axiomConversionResults_travel.csv"), 0);
    }

    protected static JdbcTemplate getJdbcTemplate() {
        Properties properties = new Properties();
        InputStream is = DataInserter.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (is == null) {
            LOGGER.error("Couldn't load properties file. Returning null.");
            return null;
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            LOGGER.error("Couldn't load properties file. Returning null.", e);
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
        String driverClassName = properties.getProperty(DRIVER_CLASS_NAME_PROPERTY_KEY);
        if (driverClassName == null) {
            LOGGER.error("Couldn't load driver class name from properties. Returning null.");
            return null;
        }
        String dbFile = properties.getProperty(DB_FILE_PROPERTY_KEY);
        if (dbFile == null) {
            LOGGER.error("Couldn't load database file name from properties. Returning null.");
            return null;
        }

        DriverManagerDataSource dbManager = new DriverManagerDataSource(DB_URL_FIRST_PART + dbFile);
        dbManager.setDriverClassName(driverClassName);
        return new JdbcTemplate(dbManager);
    }

    private JdbcTemplate jdbctemplate;

    public DataInserter(JdbcTemplate jdbctemplate) {
        this.jdbctemplate = jdbctemplate;
    }

    public void run(File file, int category) {
        String[][] data = loadData(file);
        if (data != null) {
            insertData(category, data);
        }
    }

    private String[][] loadData(File file) {
        FileReader fReader = null;
        CSVReader reader = null;
        List<String[]> lines = new ArrayList<String[]>();
        try {
            fReader = new FileReader(file);
            reader = new CSVReader(fReader);
            lines = reader.readAll();
        } catch (IOException e) {
            LOGGER.error("Couldn't read data.", e);
            return null;
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(fReader);
        }
        return lines.toArray(new String[lines.size()][]);
    }

    /**
     * data[n][0] = axiom
     * 
     * data[n][1] = verbalization
     * 
     * @param category
     * @param data
     */
    private void insertData(Integer category, String[][] data) {
        List<Object[]> batchArgs = new ArrayList<Object[]>(data.length);
        List<Integer> expIds;
        IntegerRowMapper rowMapper = new IntegerRowMapper();
        Object selectArgs[] = new Object[2];
        for (int i = 0; i < data.length; ++i) {
            selectArgs[0] = data[i][0];
            selectArgs[1] = data[i][1];
            expIds = jdbctemplate.query(SELECT_EXPERIMENT, selectArgs, rowMapper);
            if (expIds.size() == 0) {
                batchArgs.add(new Object[] { category, data[i][0], data[i][1] });
            }
        }
        if (batchArgs.size() > 0) {
            jdbctemplate.batchUpdate(INSERT_EXPERIMENT, batchArgs);
        }
        LOGGER.info("Inserted {} new experiments.", batchArgs.size());
    }
}
