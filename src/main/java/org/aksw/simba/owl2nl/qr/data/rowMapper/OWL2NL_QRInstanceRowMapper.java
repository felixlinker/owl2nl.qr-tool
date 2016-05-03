package org.aksw.simba.owl2nl.qr.data.rowMapper;

import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRInstance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OWL2NL_QRInstanceRowMapper implements RowMapper<OWL2NL_QRInstance> {
    @Override
    public OWL2NL_QRInstance mapRow(ResultSet resultSet, int i) throws SQLException {
        return new OWL2NL_QRInstance(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
