package org.aksw.simba.owl2nl.qr.data.rowMapper;

import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OWL2NL_QRUserRowMapper implements RowMapper<OWL2NL_QRUser> {
    @Override
    public OWL2NL_QRUser mapRow(ResultSet resultSet, int i) throws SQLException {
        OWL2NL_QRUser user = new OWL2NL_QRUser(resultSet.getInt("id"), resultSet.getBoolean("isExpert"));

        return user;
    }
}
