package org.aksw.simba.owl2nl.qr.db.supplier;

import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRUser;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.qr.datatypes.ExperimentSetup;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.setupsupply.AbstractStartedExperimentsFirstSetupSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLDataException;
import java.util.List;

public abstract class OWL2NL_QRSetupSupplier extends AbstractStartedExperimentsFirstSetupSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(OWL2NL_QRSetupSupplier.class);

    /**
     * Row mapper to map the result sets from the experiment query
     */
    private RowMapper<OWL2NL_QRExperimentSetup> rowMapper;

    public OWL2NL_QRSetupSupplier(RowMapper<OWL2NL_QRExperimentSetup> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public ExperimentSetup getExperimentSetupForUser(JdbcTemplate jdbctemplate, User user) {
        ExperimentSetup setup = super.getExperimentSetupForUser(jdbctemplate, user);
        if (user instanceof OWL2NL_QRUser && setup instanceof OWL2NL_QRExperimentSetup) {
            OWL2NL_QRUser owl2NLQrUser = (OWL2NL_QRUser)user;
            OWL2NL_QRExperimentSetup owl2NlSetup = (OWL2NL_QRExperimentSetup) setup;

            if (owl2NLQrUser.isExpertSet()) {
                // We know whether he is an expert or not -> do experiments
                if (setup instanceof OWL2NL_QRExperimentSetup) {
                    owl2NlSetup.setUserAnswerCount(owl2NLQrUser.getNumberOfAnswers());
                    owl2NlSetup.setPerformedByExpert(((OWL2NL_QRUser) user).isExpert());
                }

                return owl2NlSetup;
            }
        }

        return setup;
    }

    /**
     * Builds experiment from database based on id
     * @param jdbcTemplate
     * @param setupId
     * @return
     */
    @Override
    public ExperimentSetup getExperimentSetup(JdbcTemplate jdbcTemplate, Integer setupId) {
        try {
            String experimentsSetupQuery = getExperimentsSetupQuery(setupId);
            List<OWL2NL_QRExperimentSetup> experimentSetups = jdbcTemplate.query(experimentsSetupQuery, rowMapper);
            if (experimentSetups.isEmpty()) {
                LOGGER.error(OWL2NL_QRSimpleFormatter.compose("Couldn't get setup for query (queryId=?). Returning null.", setupId));
                return null;
            }

            OWL2NL_QRExperimentSetup experimentSetup = experimentSetups.get(0);
            try {
                experimentSetup = finishExperimentSetup(jdbcTemplate, experimentSetup);
            } catch (ClassCastException e) {
                // ToDo: log error
            } catch (SQLDataException e) {
                // ToDo: log error
            }

            return experimentSetup;
        } catch (DataAccessException e) {
            // ToDo: log error
            return null;
        }
    }

    /**
     * Function needs to return a valid SQL statement to get an experiment out of the database
     * @param setupId setup id of the experiment
     * @return sql statement
     */
    abstract String getExperimentsSetupQuery(int setupId);

    /**
     * Will be called on every getExperimentSetup(...) unless you override this method. You can perform any operation you'd like to add needed information to the experiment.
     * @param jdbcTemplate
     * @param experimentSetup
     * @return
     * @throws ClassCastException Should be thrown if experimentSetup is of a wrong subclass of OWL2NL_QRExperimentSetup
     * @throws SQLDataException Should be thrown if there occurs any error during data retrieval for wrong data
     * @throws DataAccessException Should be thrown if there ocurs any error during querying the database
     */
    abstract OWL2NL_QRExperimentSetup finishExperimentSetup(JdbcTemplate jdbcTemplate, OWL2NL_QRExperimentSetup experimentSetup) throws ClassCastException, SQLDataException, DataAccessException;
}
