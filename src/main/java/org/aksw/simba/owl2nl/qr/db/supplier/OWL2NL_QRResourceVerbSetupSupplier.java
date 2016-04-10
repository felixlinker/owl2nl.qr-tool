package org.aksw.simba.owl2nl.qr.db.supplier;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRResourceVerbExperimentRowMapper;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRResourceVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;
import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRTripleRowMapper;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.qr.datatypes.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLDataException;
import java.util.List;

public class OWL2NL_QRResourceVerbSetupSupplier extends OWL2NL_QRSetupSupplier {
    private static final String STARTED_EXPERIMENTS_QUERY = "SELECT resourceId FROM ResourceExperiments WHERE resourceId NOT IN (SELECT resourceId FROM ResourceExperiments WHERE userId=?);";
    private static final String NOT_STARTED_EXPERIMENTS_QUERY = "SELECT id FROM Resources WHERE id NOT IN (SELECT DISTINCT resourceId FROM ResourceExperiments);";
    private static final String RESOURCE_QUERY = "SELECT id, verbalization FROM Resources WHERE id=?;";
    private static final String TRIPLE_QUERY = "SELECT Triples.id, Triples.triple, Triples.verbalization FROM Triples JOIN Resources ON Triples.aggregatesResource=Resources.id WHERE Resources.id=?;";

    public OWL2NL_QRResourceVerbSetupSupplier() {
        super(new OWL2NL_QRResourceVerbExperimentRowMapper());
    }

    @Override
    String getExperimentsSetupQuery(int setupId) {
        return OWL2NL_QRSimpleFormatter.compose(RESOURCE_QUERY, setupId);
    }

    @Override
    OWL2NL_QRExperimentSetup finishExperimentSetup(JdbcTemplate jdbcTemplate, OWL2NL_QRExperimentSetup experimentSetup) throws ClassCastException, SQLDataException, DataAccessException {
        if (!(experimentSetup instanceof OWL2NL_QRResourceVerbExperimentSetup)) {
            throw new ClassCastException();
        }

        List<OWL2NL_QRTriple> triples = jdbcTemplate.query(TRIPLE_QUERY, new Object[] { experimentSetup.getId() }, new OWL2NL_QRTripleRowMapper());
        if (triples.isEmpty()) {
            throw new SQLDataException();
        }

        ((OWL2NL_QRResourceVerbExperimentSetup)experimentSetup).addTriples(triples);
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
