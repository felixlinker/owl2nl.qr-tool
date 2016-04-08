package org.aksw.simba.owl2nl.qr.data.experiments;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OWL2NL_QRAxiomVerbExperimentRowMapper implements RowMapper<OWL2NL_QRExperimentSetup> {
    @Override
    public OWL2NL_QRExperimentSetup mapRow(ResultSet resultSet, int i) throws SQLException {
        return new OWL2NL_QRAxiomVerbExperimentSetup(resultSet.getInt("id"), resultSet.getString("axiom"), resultSet.getString("verbalization"));
    }
}
