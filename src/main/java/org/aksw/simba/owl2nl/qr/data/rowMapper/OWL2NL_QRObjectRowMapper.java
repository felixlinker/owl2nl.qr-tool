package org.aksw.simba.owl2nl.qr.data.rowMapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OWL2NL_QRObjectRowMapper implements RowMapper<Object> {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Object();
    }
}
