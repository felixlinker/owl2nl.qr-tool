package org.aksw.simba.owl2nl.qr.db.supplier;

import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRClassVerbExperimentRowMapper;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRClassVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;
import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRTripleRowMapper;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.qr.datatypes.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLDataException;
import java.util.List;

public class OWL2NL_QRClassVerbSetupSupplier extends OWL2NL_QRAxiomSetupSupplier {
    private static final String STARTED_EXPERIMENTS_QUERY = "SELECT DISTINCT axiomId FROM ClassExperiments WHERE axiomId NOT IN (SELECT DISTINCT axiomId FROM ClassExperiments WHERE userId=?);";
    private static final String NOT_STARTED_EXPERIMENTS_QUERY = "SELECT id FROM Axioms WHERE id NOT IN (SELECT DISTINCT axiomId FROM ClassExperiments);";
    private static final String INSTANCE_QUERY = "SELECT id, triple, verbalization FROM Instances WHERE instanceOf=?;";

    public OWL2NL_QRClassVerbSetupSupplier() {
        super(new OWL2NL_QRClassVerbExperimentRowMapper());
    }

    @Override
    String getExperimentsSetupQuery(int setupId) {
        return OWL2NL_QRSimpleFormatter.compose(AXIOM_QUERY, setupId);
    }

    @Override
    OWL2NL_QRExperimentSetup finishExperimentSetup(JdbcTemplate jdbcTemplate, OWL2NL_QRExperimentSetup experimentSetup) throws ClassCastException, SQLDataException, DataAccessException {
        if (!(experimentSetup instanceof OWL2NL_QRClassVerbExperimentSetup)) {
            throw new ClassCastException();
        }

        List<OWL2NL_QRTriple> instances = jdbcTemplate.query(INSTANCE_QUERY, new Object[] { experimentSetup.getId() }, new OWL2NL_QRTripleRowMapper());
        if (instances.isEmpty()) {
            throw new SQLDataException();
        }

        ((OWL2NL_QRClassVerbExperimentSetup)experimentSetup).addInstances(instances);
        return experimentSetup;
    }

    @Override
    protected String getQueryForAlreadyStartedExperiments(User user) {
        return OWL2NL_QRSimpleFormatter.compose(STARTED_EXPERIMENTS_QUERY, user.getId());
    }

    @Override
    protected String getQueryForNotStartedExperiments() {
        return NOT_STARTED_EXPERIMENTS_QUERY;
    }
}
