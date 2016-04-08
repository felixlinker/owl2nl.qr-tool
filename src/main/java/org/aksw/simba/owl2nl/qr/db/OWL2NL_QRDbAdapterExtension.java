package org.aksw.simba.owl2nl.qr.db;

import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRUser;
import org.aksw.simba.owl2nl.qr.data.rowMapper.OWL2NL_QRUserRowMapper;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.DbAdapter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class OWL2NL_QRDbAdapterExtension {

    private static final String INSERT_USER = "INSERT INTO Users(username) VALUES (?)";
    private static final String SELECT_NUMBER_OF_ANSWERS = "SELECT COUNT(expId) FROM AxiomExperiments WHERE userId=?"; // ToDo: calculate sum
    private static final String SELECT_USER = "SELECT id, isExpert FROM Users WHERE id=?;";

    private static final IntegerRowMapper INT_ROW_MAPPER = new IntegerRowMapper();
    private static final OWL2NL_QRUserRowMapper USER_ROW_MAPPER = new OWL2NL_QRUserRowMapper();

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
                user.setNumberOfAnswers(numberOfAnswers.get(0));
            }
        }

        // ToDo: return null if there is no user?
        return user;
    }

    public static void setUserIsExpert(JdbcTemplate jdbcTemplate, User user, boolean isExpert) {
        // ToDo
    }
}
