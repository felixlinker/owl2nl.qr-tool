package org.aksw.simba.owl2nl.qr.data.experiments;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by felix on 05.04.2016.
 */
public class OWL2NL_QRResourceVerbExperimentRowMapper implements RowMapper<OWL2NL_QRExperimentSetup> {
    @Override
    public OWL2NL_QRExperimentSetup mapRow(ResultSet resultSet, int i) throws SQLException {
        return new OWL2NL_QRResourceVerbExperimentSetup(resultSet.getInt("id"), resultSet.getString("verbalization"));
    }
}
