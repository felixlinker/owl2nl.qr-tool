package org.aksw.simba.owl2nl.qr.data;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OWL2NL_QRUserRowMapper implements RowMapper<OWL2NL_QRUser> {
    @Override
    public OWL2NL_QRUser mapRow(ResultSet resultSet, int i) throws SQLException {
        OWL2NL_QRUser user = new OWL2NL_QRUser(resultSet.getInt("id"));
        // ToDo: what happens if value is not null? Will string be empty, etc.?
        if (resultSet.getString("isExpert") != null) {
            user.setExpert(resultSet.getBoolean("isExpert"));
        }

        return user;
    }
}
