package org.aksw.simba.owl2nl.qr.db.supplier;

import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRUser;
import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRAxiomVerbExperimentRowMapper;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.qr.datatypes.ExperimentSetup;
import org.aksw.simba.qr.datatypes.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLDataException;

// ToDo: how to lock this for non-experts?
public class OWL2NL_QRAxiomVerbSetupSupplier extends OWL2NL_QRAxiomSetupSupplier {
    private static final String STARTED_EXPERIMENTS_QUERY = "SELECT DISTINCT axiomId FROM AxiomExperiments WHERE axiomId NOT IN (SELECT DISTINCT axiomId FROM AxiomExperiments WHERE userId=?);";
    private static final String NOT_STARTED_EXPERIMENTS_QUERY = "SELECT id FROM Axioms WHERE id NOT IN (SELECT DISTINCT axiomId FROM AxiomExperiments);";

    public OWL2NL_QRAxiomVerbSetupSupplier() {
        super(new OWL2NL_QRAxiomVerbExperimentRowMapper());
    }

    @Override
    public ExperimentSetup getExperimentSetupForUser(JdbcTemplate jdbctemplate, User user) {
        // excluse amateurs from this experiment
        if (user instanceof OWL2NL_QRUser) {
            if (!((OWL2NL_QRUser) user).isExpert() && ((OWL2NL_QRUser) user).isExpertSet()) {
                return null;
            }
        }

        return super.getExperimentSetupForUser(jdbctemplate, user);
    }

    @Override
    String getExperimentsSetupQuery(int setupId) {
        return OWL2NL_QRSimpleFormatter.compose(AXIOM_QUERY, setupId);
    }

    @Override
    OWL2NL_QRExperimentSetup finishExperimentSetup(JdbcTemplate jdbcTemplate, OWL2NL_QRExperimentSetup experimentSetup) throws ClassCastException, SQLDataException, DataAccessException {
        return experimentSetup;
    }

    @Override
    protected String getQueryForNotStartedExperiments() {
        return NOT_STARTED_EXPERIMENTS_QUERY;
    }

    @Override
    protected String getQueryForAlreadyStartedExperiments(User user) {
        return OWL2NL_QRSimpleFormatter.compose(STARTED_EXPERIMENTS_QUERY, user.getId());
    }
}
