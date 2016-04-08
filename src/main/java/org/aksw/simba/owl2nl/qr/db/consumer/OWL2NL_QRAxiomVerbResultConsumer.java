package org.aksw.simba.owl2nl.qr.db.consumer;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRAxiomVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.ExperimentResultConsumer;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by felix on 05.04.2016.
 */
public class OWL2NL_QRAxiomVerbResultConsumer extends OWL2NL_QRResultConsumer implements ExperimentResultConsumer<OWL2NL_QRAxiomVerbExperimentResult> {

    private static final String STORE_QUERY = "INSERT INTO AxiomExperiments (userId, axiomId, adequacy, fluency) VALUES (?,?,?,?);";
    private static final String CHECK_PRESENCE_QUERY = "SELECT A.userId, A.axiomId FROM AxiomExperiments WHERE A.userId=? AND A.axiomId=?;";

    @Override
    public boolean storeExperimentResult(JdbcTemplate jdbcTemplate, OWL2NL_QRAxiomVerbExperimentResult result, User user) {
        String checkPresenceQuery = OWL2NL_QRSimpleFormatter.compose(CHECK_PRESENCE_QUERY, user.getId(), result.getExperimentSetupId());
        String storeQuery = OWL2NL_QRSimpleFormatter.compose(STORE_QUERY, user.getId(), result.getExperimentSetupId(), result.getAdequacy(), result.getFluency());
        return super.storeResults(jdbcTemplate, checkPresenceQuery, storeQuery);
    }
}
