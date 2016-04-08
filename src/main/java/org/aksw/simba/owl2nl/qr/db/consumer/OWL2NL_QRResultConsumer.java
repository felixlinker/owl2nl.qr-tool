package org.aksw.simba.owl2nl.qr.db.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class OWL2NL_QRResultConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OWL2NL_QRResultConsumer.class);

    protected boolean storeResults(JdbcTemplate jdbcTemplate, String checkQuery, String storeQuery) {
        List<Object> queryResult = jdbcTemplate.query(checkQuery, new ObjectRowMapper());
        if (!queryResult.isEmpty()) {
            LOGGER.info("The result is already present inside the database. The new one will be ignored.");
            return true;
        }

        try {
            if (jdbcTemplate.update(storeQuery) == 0) {
                LOGGER.error("Creation of experiment result didn't changed a single row ( TODO ). Return false.");
                return false;
            }
        } catch (DataAccessException e) {
            LOGGER.error("Creation of experiment result didn't changed a single row ( TODO ). Return false.");
            return false;
        }

        return true;
    }

    protected class ObjectRowMapper implements RowMapper<Object> {
        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Object();
        }
    }
}
