package org.aksw.simba.owl2nl.qr.db.consumer;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRClassVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.ExperimentResultConsumer;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by felix on 05.04.2016.
 */
public class OWL2NL_QRClassVerbResultConsumer
    extends OWL2NL_QRResultConsumer
        implements ExperimentResultConsumer<OWL2NL_QRClassVerbExperimentResult> {
    private static final String CHECK_PRESENCE_QUERY = "SELECT userId, axiomId FROM ClassExperiments WHERE userId=? AND axiomId=?;";
    private static final String STORE_QUERY = "INSERT INTO ClassExperiments (userId, axiomId, usersChoice) VALUES (?,?,?);";

    @Override
    public boolean storeExperimentResult(JdbcTemplate jdbcTemplate, OWL2NL_QRClassVerbExperimentResult result, User user) {
        String checkPresence = OWL2NL_QRSimpleFormatter.compose(CHECK_PRESENCE_QUERY, user.getId(), result.getExperimentSetupId());
        String storeQuery = OWL2NL_QRSimpleFormatter.compose(STORE_QUERY, user.getId(), result.getExperimentSetupId(), result.getChosenTriple());
        return super.storeResults(jdbcTemplate, checkPresence, storeQuery);
    }
}
