package org.aksw.simba.owl2nl.qr.tools;

import au.com.bytecode.opencsv.CSVReader;
import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.aksw.simba.webelements.LineBreak;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class DataInserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInserter.class);

    private static final String PROPERTIES_FILE_NAME = "OWL2NL_QR.properties";
    private static final String DRIVER_CLASS_NAME_PROPERTY_KEY = "org.aksw.simba.owl2nl.qr.db.driverClassName";
    private static final String DB_FILE_PROPERTY_KEY = "org.aksw.simba.owl2nl.qr.db.file";
    private static final String DB_URL_FIRST_PART = "jdbc:hsqldb:file:";
    private static final String CSV_DELIMITER = ";";

    public static void main(String[] args) {
        JdbcTemplate jdbctemplate = getJdbcTemplate();
        if (jdbctemplate == null) {
            return;
        }
        DataInserter inserter = new DataInserter(jdbctemplate);
//        inserter.runAxioms(new File("C:/Git/owl2nl.qr-tool/data/axioms.csv"));
        inserter.runResources(new File("C:/Git/owl2nl.qr-tool/data/resources.csv"));
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

    /*** AXIOM FUNCTIONS ***/

    private static final String INSERT_AXIOM = "INSERT INTO Axioms (axiom, verbalization) VALUES (?,?);";

    public void runAxioms(File file) {
        Collection<Axiom> axioms;
        try {
            axioms = loadDataAxiom(file);
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found: " + file.getPath());
            return;
        }

        for (Axiom axiom: axioms) {
            jdbctemplate.update(INSERT_AXIOM, new Object[] { axiom.axiom, axiom.verbalization });
        }
    }

    public Collection<Axiom> loadDataAxiom(File file) throws FileNotFoundException {
        BufferedReader fileReader;
        try {
             fileReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new LinkedList<>();
        }

        LinkedList<Axiom> axioms = new LinkedList<>();
        try {
            String line = fileReader.readLine();
            while (line != null) {
                String[] cells = line.split(CSV_DELIMITER);
                if (cells.length != 2) {
                    line = fileReader.readLine();
                    continue;
                }

                axioms.add(new Axiom(cells[0], cells[1]));
                line = fileReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return axioms;
    }

    private class Axiom {

        public String axiom;
        public String verbalization;
        public Axiom(String axiom, String verbalization) {
            this.axiom = axiom;
            this.verbalization = verbalization;
        }
    }

    /*** RESOURCE FUNCTIONS ***/

    private static final String INSERT_RESOURCE = "INSERT INTO Resources (resource, verbalization) VALUES (?,?);";
    private static final String INSERT_TRIPLE_TO_RESOURCE = "INSERT INTO Triples (aggregatesResource, triple, verbalization) VALUES (?,?,?);";
    private static final String SELECT_RESOURCE_ID = "SELECT id FROM Resources where resource=?;";

    public void runResources(File file) {
        Collection<Resource> resources;
        try {
            resources = loadDataResource(file);
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found: " + file.getPath());
            return;
        }

        for (Resource resource: resources) {
            try {
                String res = resource.resource;

                if (jdbctemplate.update(INSERT_RESOURCE, new Object[] { res, resource.verbalization }) == 0) {
                    throw new SQLException("Couldn't update db - resource");
                }

                List<Integer> primaryKeyResult = jdbctemplate.query(SELECT_RESOURCE_ID, new Object[] { res }, new IntegerRowMapper());

                if (primaryKeyResult.isEmpty()) {
                    throw new SQLException("Couldn't get primary key for resource: " + res);
                }

                int pk = primaryKeyResult.get(0);

                for (Triple t: resource.cluster) {
                    if (jdbctemplate.update(INSERT_TRIPLE_TO_RESOURCE, new Object[] { pk, t.triple, t.verbalization }) == 0) {
                        throw new SQLException("Couldn't update db - triple");
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private Collection<Resource> loadDataResource(File file) throws FileNotFoundException {
        BufferedReader fileReader = new BufferedReader(new FileReader(file));

        LinkedList<Resource> resources = new LinkedList<>();
        try {
            String line = fileReader.readLine();
            while (line != null) {
                String[] cells = line.split(CSV_DELIMITER);
                if (cells.length < 4) {
                    line = fileReader.readLine();
                    continue;
                }

                LinkedList<Triple> cluster = new LinkedList<>();
                for (int i = 2; i + 1 < cells.length; i += 2) {
                    cluster.add(new Triple(cells[i], cells[i + 1]));
                }

                resources.add(new Resource(cells[0], cells[1], cluster));

                line = fileReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resources;
    }

    private class Resource {
        public String resource;
        public String verbalization;
        public Collection<Triple> cluster;
        public Resource(String resource, String verbalization, Collection<Triple> cluster) {
            this.resource = resource;
            this.verbalization = verbalization;
            this.cluster = cluster;
        }
    }

    private class Triple {
        public String triple;
        public String verbalization;
        public Triple(String triple, String verbalization) {
            this.triple = triple;
            this.verbalization = verbalization;
        }
    }
}
