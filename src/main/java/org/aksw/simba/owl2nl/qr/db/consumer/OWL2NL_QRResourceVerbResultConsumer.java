package org.aksw.simba.owl2nl.qr.db.consumer;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResult;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRResourceVerbExperimentResult;
import org.aksw.simba.qr.datatypes.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by felix on 05.04.2016.
 */
public class OWL2NL_QRResourceVerbResultConsumer extends OWL2NL_QRResultConsumer {
    private static final String STORE_QUERY_AMATEUR = "INSERT INTO ResourceExperiments (userId, resourceId, fluency) VALUES (?,?,?);";
    private static final String STORE_QUERY_EXPERT = "INSERT INTO ResourceExperiments (userId, resourceId, adequacy, fluency, completeness) VALUES (?,?,?,?,?);";
    private static final String CHECK_PRESENCE_QUERY = "SELECT R.userId, R.resourceId FROM ResourceExperiments AS R WHERE R.userId=? AND R.resourceId=?;";

    @Override
    public boolean storeExperimentResult(JdbcTemplate jdbcTemplate, OWL2NL_QRExperimentResult result, User user) {

        if (!(result instanceof OWL2NL_QRResourceVerbExperimentResult)) {
            LOGGER.info("The result was meta settings only.");
            return true;
        }
        OWL2NL_QRResourceVerbExperimentResult resResult = (OWL2NL_QRResourceVerbExperimentResult)result;

        List<Object> queryResult = jdbcTemplate.query(CHECK_PRESENCE_QUERY, new Object[] { user.getId(), resResult.getExperimentSetupId() }, OBJECT_ROW_MAPPER);
        if (!queryResult.isEmpty()) {
            LOGGER.info("The result is already present inside the database. The new one will be ignored.");
            return true;
        }

        String storeQuery;
        Object[] args;
        if (resResult.getAdequacy() == -1 && resResult.getCompleteness() == -1) {
            // result must be from amateur
            storeQuery = STORE_QUERY_AMATEUR;
            args = new Object[] { user.getId(), resResult.getExperimentSetupId(), resResult.getFluency() };
        } else {
            // result must be from expert
            storeQuery = STORE_QUERY_EXPERT;
            args = new Object[] { user.getId(), resResult.getExperimentSetupId(), resResult.getAdequacy(), resResult.getFluency(), resResult.getCompleteness() };
        }

        try {
            if (jdbcTemplate.update(storeQuery, args) == 0) {
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
