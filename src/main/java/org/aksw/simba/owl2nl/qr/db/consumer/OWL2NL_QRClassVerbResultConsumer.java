package org.aksw.simba.owl2nl.qr.db.consumer;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRClassVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.ExperimentResultConsumer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by felix on 05.04.2016.
 */
public class OWL2NL_QRClassVerbResultConsumer extends OWL2NL_QRResultConsumer<OWL2NL_QRClassVerbExperimentResult> {
    private static final String CHECK_PRESENCE_QUERY = "SELECT userId, axiomId FROM ClassExperiments WHERE userId=? AND axiomId=?;";
    private static final String STORE_QUERY = "INSERT INTO ClassExperiments (userId, axiomId, usersChoice) VALUES (?,?,?);";

    @Override
    public boolean storeExperimentResult(JdbcTemplate jdbcTemplate, OWL2NL_QRClassVerbExperimentResult result, User user) {
        super.storeExperimentResult(jdbcTemplate, result, user);

        List<Object> queryResult = jdbcTemplate.query(CHECK_PRESENCE_QUERY, new Object[] { user.getId(), result.getExperimentSetupId() }, OBJECT_ROW_MAPPER);
        if (!queryResult.isEmpty()) {
            LOGGER.info("The result is already present inside the database. The new one will be ignored.");
            return true;
        }

        try {
            if (jdbcTemplate.update(STORE_QUERY, new Object[] { user.getId(), result.getExperimentSetupId(), result.getChosenTriple() }) == 0) {
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
