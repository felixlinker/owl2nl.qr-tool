package org.aksw.simba.owl2nl.qr.db.consumer;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRResourceVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.ExperimentResultConsumer;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by felix on 05.04.2016.
 */
public class OWL2NL_QRResourceVerbResultConsumer
        extends OWL2NL_QRResultConsumer
        implements ExperimentResultConsumer<OWL2NL_QRResourceVerbExperimentResult> {
    private static final String STORE_QUERY_AMATEUR = "INSERT INTO ResourceExperiments (userId, resourceId, fluency) VALUES (?,?,?);";
    private static final String STORE_QUERY_EXPERT = "INSERT INTO ResourceExperiments (userId, resourceId, adequacy, fluency, completeness) VALUES (?,?,?,?,?);";
    private static final String CHECK_PRESENCE_QUERY = "SELECT R.userId, R.resourceId FROM ResourceExperiments AS R WHERE R.userId=? AND R.resourceId=?;";

    @Override
    public boolean storeExperimentResult(JdbcTemplate jdbcTemplate, OWL2NL_QRResourceVerbExperimentResult result, User user) {
        String checkPresenceQuery = OWL2NL_QRSimpleFormatter.compose(CHECK_PRESENCE_QUERY, user.getId(), result.getExperimentSetupId());
        String storeQuery;
        if (result.getAdequacy() == -1 && result.getCompleteness() == -1) {
            // result must be from amateur
            storeQuery = OWL2NL_QRSimpleFormatter.compose(STORE_QUERY_AMATEUR, user.getId(), result.getExperimentSetupId(), result.getFluency());
        } else {
            // result must be from expert
            storeQuery = OWL2NL_QRSimpleFormatter.compose(STORE_QUERY_EXPERT, user.getId(), result.getExperimentSetupId(), result.getAdequacy(), result.getFluency(), result.getCompleteness());
        }

        return super.storeResults(jdbcTemplate, checkPresenceQuery, storeQuery);
    }
}
