package org.aksw.simba.owl2nl.qr.db.consumer;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRClassVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResult;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRResourceVerbExperimentResult;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.ExperimentResultConsumer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by felix on 05.04.2016.
 */
public class OWL2NL_QRClassVerbResultConsumer extends OWL2NL_QRResultConsumer {
    private static final String CHECK_PRESENCE_QUERY = "SELECT userId, axiomId FROM ClassExperiments WHERE userId=? AND axiomId=?;";
    private static final String STORE_QUERY = "INSERT INTO ClassExperiments (userId, axiomId, usersChoice) VALUES (?,?,?);";

    @Override
    public boolean storeExperimentResult(JdbcTemplate jdbcTemplate, OWL2NL_QRExperimentResult result, User user) {
        super.storeExperimentResult(jdbcTemplate, result, user);

        if (!(result instanceof OWL2NL_QRClassVerbExperimentResult)) {
            LOGGER.info("The result was user group setting only.");
            return true;
        }
        OWL2NL_QRClassVerbExperimentResult classResult = (OWL2NL_QRClassVerbExperimentResult)result;

        List<Object> queryResult = jdbcTemplate.query(CHECK_PRESENCE_QUERY, new Object[] { user.getId(), classResult.getExperimentSetupId() }, OBJECT_ROW_MAPPER);
        if (!queryResult.isEmpty()) {
            LOGGER.info("The result is already present inside the database. The new one will be ignored.");
            return true;
        }

        try {
            if (jdbcTemplate.update(STORE_QUERY, new Object[] { user.getId(), classResult.getExperimentSetupId(), classResult.getChosenTriple() }) == 0) {
                LOGGER.error("Creation of experiment result didn't changed a single row ( TODO ). Return false.");
                return false;
            }
        } catch (DataAccessException e) {
            LOGGER.error("Creation of experiment result didn't changed a single row ( TODO ). Return false.");
            return false;
        }

        return true;
    }
}
