package org.aksw.simba.owl2nl.qr.db.consumer;

import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRSimpleFormatter;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRUserGroupExperimentResult;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.ExperimentResultConsumer;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by felix on 07.04.2016.
 */
public class OWL2NL_QRUserGroupResultConsumer extends OWL2NL_QRResultConsumer implements ExperimentResultConsumer<OWL2NL_QRUserGroupExperimentResult> {

    private static final String STORE_QUERY = "UPDATE Users SET isExpert=? WHERE userId=?;";
    private static final String CHECK_PRESENCE_QUERY = "SELECT isExpert FROM Users WHERE userId=? AND IS NOT NULL isExpert;";

    @Override
    public boolean storeExperimentResult(JdbcTemplate jdbcTemplate, OWL2NL_QRUserGroupExperimentResult result, User user) {
        String checkPresenceQuery = OWL2NL_QRSimpleFormatter.compose(CHECK_PRESENCE_QUERY, user.getId());
        String storeQuery = OWL2NL_QRSimpleFormatter.compose(STORE_QUERY, result.isExpert() ? 1 : 0, user.getId());
        return super.storeResults(jdbcTemplate, checkPresenceQuery, storeQuery);
    }
}
