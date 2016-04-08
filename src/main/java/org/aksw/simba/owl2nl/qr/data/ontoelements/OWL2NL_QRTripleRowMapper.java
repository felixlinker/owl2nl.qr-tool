package org.aksw.simba.owl2nl.qr.data.ontoelements;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Simple row mapper to create a OWL2NL_QRTriple from a sql query result
 */
public class OWL2NL_QRTripleRowMapper implements RowMapper<OWL2NL_QRTriple> {
    @Override
    public OWL2NL_QRTriple mapRow(ResultSet resultSet, int i) throws SQLException {
        return new OWL2NL_QRTriple(resultSet.getInt("id"), resultSet.getString("triple"), resultSet.getString("verbalization"));
    }
}
