package org.aksw.simba.owl2nl.qr.db.consumer;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResult;
import org.aksw.simba.owl2nl.qr.db.OWL2NL_QRDbAdapterExtension;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.ExperimentResultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class OWL2NL_QRResultConsumer<T extends OWL2NL_QRExperimentResult> implements ExperimentResultConsumer<T> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(OWL2NL_QRResultConsumer.class);

    protected static final ObjectRowMapper OBJECT_ROW_MAPPER = new ObjectRowMapper();

    @Override
    public boolean storeExperimentResult(JdbcTemplate jdbcTemplate, T experimentResult, User user) {
        if (experimentResult.isExpertSet()) {
            OWL2NL_QRDbAdapterExtension.setUserIsExpert(jdbcTemplate, user, experimentResult.isExpert());
        }

        return true;
    }

    protected static class ObjectRowMapper implements RowMapper<Object> {
        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Object();
        }
    }
}
