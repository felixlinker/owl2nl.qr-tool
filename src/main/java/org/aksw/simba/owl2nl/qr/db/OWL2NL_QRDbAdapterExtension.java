package org.aksw.simba.owl2nl.qr.db;

import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRUser;
import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRUserRowMapper;
import org.aksw.simba.qr.db.DbAdapter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OWL2NL_QRDbAdapterExtension {

    private static final String UPDATE_EXPERT_USER = "UPDATE Users SET isExpert=? WHERE id=?;";
    private static final String INSERT_USER = "INSERT INTO Users(username) VALUES (?)";
    private static final String SELECT_NUMBER_OF_ANSWERS = "SELECT COUNT(axiomId) FROM AxiomExperiments WHERE userId=?;";
    private static final String SELECT_USER = "SELECT id, isExpert FROM Users WHERE id=?;";
    private static final String SELECT_USER_IS_EXPERT = "SELECT isExpert FROM USERS WHERE id=?;";

    private static final IntegerRowMapper INT_ROW_MAPPER = new IntegerRowMapper();
    private static final OWL2NL_QRUserRowMapper USER_ROW_MAPPER = new OWL2NL_QRUserRowMapper();
    private static final RowMapper<Object> USER_EXPERT_ROW_MAPPER = new RowMapper<Object>() {
        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            if (rs.getString("isExpert") != null) {
                return new Object();
            }

            return null;
        }
    };

    public static int addUser(String loginName, DbAdapter db) {
        db.getJdbcTemplate().update(INSERT_USER, new Object[] { loginName });
        return db.getUserId(loginName);
    }

    public static OWL2NL_QRUser getUser(int userId, DbAdapter db) {

        OWL2NL_QRUser user = null;
        List<OWL2NL_QRUser> users = db.getJdbcTemplate().query(SELECT_USER, new Object[] { userId }, USER_ROW_MAPPER);
        if (!users.isEmpty()) {
            user = users.get(0);

            List<Integer> numberOfAnswers = db.getJdbcTemplate().query(SELECT_NUMBER_OF_ANSWERS, new Object[] { userId }, INT_ROW_MAPPER);
            if (!numberOfAnswers.isEmpty()) {
                int count = 0;
                for (Integer number: numberOfAnswers) {
                    count += number;
                }
                user.setNumberOfAnswers(count);
            }
        }

        // ToDo: return null if there is no user?
        return user;
    }

    public static boolean isExpertUnknown(int userId, DbAdapter db) {
        return db.getJdbcTemplate().query(SELECT_USER_IS_EXPERT, new Object[] { userId }, USER_EXPERT_ROW_MAPPER).isEmpty();
    }

    public static void setUserIsExpert(boolean isExpert, int userId, DbAdapter db) {
        db.getJdbcTemplate().update(UPDATE_EXPERT_USER, new Object[] { isExpert ? 1 : 0, userId });
    }
}
