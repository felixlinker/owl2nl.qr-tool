package org.aksw.simba.owl2nl.qr.data.rowMapper;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRClassVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Simple row mapper to create OWL2NL_QRClassExperimentSetup from a sql query result
 */
public class OWL2NL_QRClassVerbExperimentRowMapper implements RowMapper<OWL2NL_QRExperimentSetup> {
    @Override
    public OWL2NL_QRExperimentSetup mapRow(ResultSet resultSet, int i) throws SQLException {
        return new OWL2NL_QRClassVerbExperimentSetup(resultSet.getInt("id"), resultSet.getString("axiom"), resultSet.getString("verbalization"));
    }
}
