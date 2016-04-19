package org.aksw.simba.owl2nl.qr.tools;

import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRObjectRowMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public class DataInserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInserter.class);

    private static final String PROPERTIES_FILE_NAME = "OWL2NL_QR.properties";
    private static final String DRIVER_CLASS_NAME_PROPERTY_KEY = "org.aksw.simba.owl2nl.qr.db.driverClassName";
    private static final String DB_FILE_PROPERTY_KEY = "org.aksw.simba.owl2nl.qr.db.file";
    private static final String DB_URL_FIRST_PART = "jdbc:hsqldb:file:";

    private static final OWL2NL_QRObjectRowMapper OBJECT_ROW_MAPPER = new OWL2NL_QRObjectRowMapper();

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
            if (jdbctemplate.query(SELECT_AXIOM_ID, new Object[]{axiom.axiom}, OBJECT_ROW_MAPPER).isEmpty()) {
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

                if ((jdbctemplate.query(SELECT_RESOURCE_ID, new Object[] { res }, OBJECT_ROW_MAPPER).isEmpty())) {
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
                    if (jdbctemplate.query(SELECT_TRIPLE_ID, new Object[] { t.triple }, OBJECT_ROW_MAPPER).isEmpty()) {
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
    private static final String UPDATE_INSTANCE = "INSERT INTO Instances (instanceOf, name, correctInstance) VALUES (?,?,?);";
    private static final String SELECT_INSTANCE = "SELECT id FROM Instances WHERE name=? AND instanceOf=?;";
    private static final String UPDATE_INSTANCE_TRIPLE = "INSERT INTO InstanceTriples (tripleOf, triple, verbalization) VALUES (?,?,?);";
    private static final String SELECT_INSTANCE_TRIPLE = "SELECT id FROM InstanceTriples WHERE triple=? AND tripleOf=?;";
    private static final String UPDATE_OVERHEAD_TRIPLE = "INSERT INTO InstanceTriples (overheadTripleOf, triple, verbalization) VALUES (?,?,?);";
    private static final String SELECT_OVERHEAD_TRIPLE = "SELECT id FROM InstanceTriples WHERE overheadTripleOf=? AND triple=?;";

    public void runClasses(File file) {
        LinkedList<ClassVerbExp> experiments;
        try {
            experiments = loadClasses(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        for (ClassVerbExp experiment: experiments) {
            List<Integer> pks = jdbctemplate.query(SELECT_AXIOM_ID, new Object[] { experiment.axiom }, new IntegerRowMapper());
            if (pks.isEmpty()) {
                LOGGER.error("Couldn't find pk for axiom: " + experiment.axiom);
                continue;
            }
            int pk = pks.get(0);

            for (Triple t: experiment.overheadTriples) {
                if ((jdbctemplate.query(SELECT_OVERHEAD_TRIPLE, new Object[] { pk, t.triple }, OBJECT_ROW_MAPPER)).isEmpty()) {
                    if (jdbctemplate.update(UPDATE_OVERHEAD_TRIPLE, new Object[] { pk, t.triple, t.verbalization }) == 0) {
                        LOGGER.error("Couldn't insert overhead triple for axiom: " + pk);
                    }
                }
            }

            for (Instance instance: experiment.instances) {
                if ((jdbctemplate.query(SELECT_INSTANCE, new Object[] { instance.name, pk }, OBJECT_ROW_MAPPER)).isEmpty()) {
                    if (jdbctemplate.update(UPDATE_INSTANCE, new Object[] { pk, instance.name, instance.isCorrect }) == 0) {
                        LOGGER.error("Couldn't insert instance: " + instance.name);
                        continue;
                    }
                }

                List<Integer> pksInstances = jdbctemplate.query(SELECT_INSTANCE, new Object[] { instance.name, pk }, new IntegerRowMapper());
                if (pksInstances.isEmpty()) {
                    LOGGER.error("Couldn't get pk from instance: " + instance.name);
                    continue;
                }
                int pkInstance = pksInstances.get(0);

                for (Triple t: instance.triples) {
                    if ((jdbctemplate.query(SELECT_INSTANCE_TRIPLE, new Object[] { t.triple, pkInstance }, OBJECT_ROW_MAPPER)).isEmpty()) {
                        if (jdbctemplate.update(UPDATE_INSTANCE_TRIPLE, new Object[] { pkInstance, t.triple, t.verbalization }) == 0) {
                            LOGGER.error("Couldn't insert triple for " + pkInstance + " triple: " + t.triple);
                        }
                    }
                }
            }
        }
    }

    private LinkedList<ClassVerbExp> loadClasses(File file) throws FileNotFoundException {
        CsvParser experiments = new CsvParser("C:/Git/owl2nl.qr-tool/data/instances.csv");
        HashMap<String, ClassVerbExp> classExperiments = new HashMap<>();
        LinkedList<ClassVerbExp> result = new LinkedList<>();

        for (LinkedList<String> row: experiments.getRows()) {
            if (row.size() < 5) {
                continue;
            }

            Iterator<String> iterator = row.listIterator();
            String axiom = iterator.next();
            String type = iterator.next();
            boolean isCorrect = iterator.next().equals("WAHR");

            ClassVerbExp exp = classExperiments.get(axiom);
            if (exp == null) {
                exp = new ClassVerbExp(axiom);
                classExperiments.put(axiom, exp);
                result.add(exp);
            }

            if (type.equals("overhead")) {
                while (iterator.hasNext()) {
                    String triple = iterator.next();
                    if (!iterator.hasNext()) { break; }
                    String verb = iterator.next();
                    exp.addOverheadTriple(new Triple(triple, verb));
                }
            } else {
                Instance instance = new Instance(type, isCorrect);
                while (iterator.hasNext()) {
                    String triple = iterator.next();
                    if (!iterator.hasNext()) { break; }
                    String verb = iterator.next();
                    instance.addTriple(new Triple(triple, verb));
                }
                exp.addInstance(instance);
            }
        }

        return result;
    }

    private class ClassVerbExp {
        public String axiom;
        public LinkedList<Instance> instances = new LinkedList<>();
        public LinkedList<Triple> overheadTriples = new LinkedList<>();

        public ClassVerbExp(String axiom) {
            this.axiom = axiom;
        }

        public void addInstance(Instance instance) {
            this.instances.add(instance);
        }

        public void addOverheadTriple(Triple triple) {
            this.overheadTriples.add(triple);
        }
    }

    private class Instance {
        public String name;
        public LinkedList<Triple> triples = new LinkedList<>();
        public boolean isCorrect;

        public Instance(String name, boolean isCorrect) {
            this.name = name;
            this.triples = triples;
            this.isCorrect = isCorrect;
        }

        public void addTriple(Triple t) {
            this.triples.add(t);
        }
    }
}
