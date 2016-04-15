package org.aksw.simba.owl2nl.qr.tools;

import au.com.bytecode.opencsv.CSVReader;
import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRObjectRowMapper;
import org.aksw.simba.webelements.LineBreak;
import org.aksw.simba.webelements.Link;
import org.apache.commons.io.IOUtils;
import org.apache.commons.pool.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DataInserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInserter.class);

    private static final String PROPERTIES_FILE_NAME = "OWL2NL_QR.properties";
    private static final String DRIVER_CLASS_NAME_PROPERTY_KEY = "org.aksw.simba.owl2nl.qr.db.driverClassName";
    private static final String DB_FILE_PROPERTY_KEY = "org.aksw.simba.owl2nl.qr.db.file";
    private static final String DB_URL_FIRST_PART = "jdbc:hsqldb:file:";

    public static void main(String[] args) {
        JdbcTemplate jdbctemplate = getJdbcTemplate();
        if (jdbctemplate == null) {
            return;
        }
        DataInserter inserter = new DataInserter(jdbctemplate);
        inserter.runAxioms(new File("C:/Git/owl2nl.qr-tool/data/axioms.csv"));
        inserter.runResources(new File("C:/Git/owl2nl.qr-tool/data/resources.csv"));
        inserter.runClasses(new File("C:/Git/owl2nl.qr-tool/data/instances.csv"));
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
            if (jdbctemplate.query(SELECT_AXIOM_ID, new Object[]{axiom.axiom}, new OWL2NL_QRObjectRowMapper()).isEmpty()) {
                jdbctemplate.update(INSERT_AXIOM, new Object[] { axiom.axiom, axiom.verbalization });
            }
        }
    }

    public Collection<Axiom> loadDataAxiom(File file) throws FileNotFoundException {
        LinkedList<LinkedList<String>> cells = new CsvParser(file).getRows();

        LinkedList<Axiom> axioms = new LinkedList<>();
        for (LinkedList<String> row: cells) {
            if (row.size() < 2) {
                LOGGER.error("Error: malformed row for axiom experiments");
                continue;
            }

            axioms.add(new Axiom(row.get(0), row.get(1)));
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
    private static final String SELECT_RESOURCE_ID = "SELECT id FROM Resources WHERE resource=?;";
    private static final String SELECT_TRIPLE_ID = "SELECT id FROM Triples WHERE triple=?;";

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

                if ((jdbctemplate.query(SELECT_RESOURCE_ID, new Object[] { res }, new OWL2NL_QRObjectRowMapper()).isEmpty())) {
                    if (jdbctemplate.update(INSERT_RESOURCE, new Object[] { res, resource.verbalization }) == 0) {
                        throw new SQLException("Couldn't update db - resource");
                    }
                }

                List<Integer> primaryKeyResult = jdbctemplate.query(SELECT_RESOURCE_ID, new Object[] { res }, new IntegerRowMapper());

                if (primaryKeyResult.isEmpty()) {
                    throw new SQLException("Couldn't get primary key for resource: " + res);
                }

                int pk = primaryKeyResult.get(0);

                for (Triple t: resource.cluster) {
                    if (jdbctemplate.query(SELECT_TRIPLE_ID, new Object[] { t.triple }, new OWL2NL_QRObjectRowMapper()).isEmpty()) {
                        if (jdbctemplate.update(INSERT_TRIPLE_TO_RESOURCE, new Object[] { pk, t.triple, t.verbalization }) == 0) {
                            throw new SQLException("Couldn't update db - triple");
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private Collection<Resource> loadDataResource(File file) throws FileNotFoundException {
        LinkedList<LinkedList<String>> cells = new CsvParser(file).getRows();


        LinkedList<Resource> resources = new LinkedList<>();
        for (LinkedList<String> row: cells) {
            if (row.size() < 4) {
                LOGGER.error("Error: malformed row for resource experiments");
                continue;
            }

            Iterator<String> listIterator = row.iterator();
            String resource = listIterator.next();
            String veralization = listIterator.next();

            LinkedList<Triple> cluster = new LinkedList<>();
            while (listIterator.hasNext()) {
                String triple = listIterator.next();
                if (listIterator.hasNext()) {
                    String tripleVerb = listIterator.next();
                    cluster.add(new Triple(triple, tripleVerb));
                }
            }

            resources.add(new Resource(resource, veralization, cluster));
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

    /*** CLASS FUNCTIONS ***/

    private static final String SELECT_AXIOM_ID = "SELECT id FROM Axioms WHERE axiom=?;";
    private static final String UPDATE_INSTANCE = "INSERT INTO Instances (instanceOf, triple, verbalization, correctInstance) VALUES (?,?,?,?);";
    private static final String SELECT_INSTANCE = "SELECT id FROM Instances WHERE triple=? AND instanceOf=?;";

    public void runClasses(File file) {
        HashMap<String, LinkedList<Instance>> classes;
        try {
            classes = loadClasses(file);
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found: " + file.getPath());
            return;
        }

        Set<Map.Entry<String, LinkedList<Instance>>> entrySet = classes.entrySet();
        for (Map.Entry<String, LinkedList<Instance>> entry: entrySet) {
            String axiom = entry.getKey();
            LinkedList<Instance> instances = entry.getValue();

            List<Integer> pks = jdbctemplate.query(SELECT_AXIOM_ID, new Object[] { axiom }, new IntegerRowMapper());
            if (pks.isEmpty()) {
                LOGGER.error("Error: couldn't find pk to axiom: " + axiom);
                continue;
            }

            int pk = pks.get(0);
            for (Instance instace: instances) {
                if (jdbctemplate.query(SELECT_INSTANCE, new Object[] { instace.instance, pk }, new OWL2NL_QRObjectRowMapper()).isEmpty()) {
                    if ((jdbctemplate.update(UPDATE_INSTANCE, new Object[] { pk, instace.instance, instace.verbalization, instace.isCorrect ? 1 : 0 })) == 0) {
                        LOGGER.error("Error: Couldn't insert instace: " + instace.instance);
                    }
                }
            }
        }
    }

    private HashMap<String, LinkedList<Instance>> loadClasses(File file) throws FileNotFoundException {
        LinkedList<LinkedList<String>> cells = new CsvParser(file).getRows();

        HashMap<String, LinkedList<Instance>> classes = new HashMap<>();
        for (LinkedList<String> row: cells) {
            if (row.size() < 11) {
                LOGGER.error("Error: malformed row for class experiments");
                continue;
            }

            Iterator<String> listIerator = row.listIterator();
            String axiom = listIerator.next();

            LinkedList<Instance> instances = new LinkedList<>();
            for (int i = 0; i < 4; i++) {
                instances.add(new Instance(listIerator.next(), listIerator.next(), false));
            }
            instances.add((new Instance(listIerator.next(), listIerator.next(), true)));

            classes.put(axiom, instances);
        }

        return classes;
    }

    private class Instance {
        public String instance;
        public String verbalization;
        public boolean isCorrect;

        public Instance(String instance, String verbalization, boolean isCorrect) {
            this.instance = instance;
            this.verbalization = verbalization;
            this.isCorrect = isCorrect;
        }
    }
}
