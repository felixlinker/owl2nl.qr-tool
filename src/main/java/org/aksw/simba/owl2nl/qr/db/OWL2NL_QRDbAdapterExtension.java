package org.aksw.simba.owl2nl.qr.db;

import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRUser;
import org.aksw.simba.qr.db.DbAdapter;

import java.util.List;

public class OWL2NL_QRDbAdapterExtension {

    private static final String INSERT_USER = "INSERT INTO Users(username) VALUES (?)";
    private static final String SELECT_NUMBER_OF_ANSWERS = "SELECT COUNT(expId) FROM AxiomExperiments WHERE userId=?"; // ToDo: calculate sum
    private static final String SELECT_USER = "SELECT id, isExpert FROM Users WHERE id=?;";

    private IntegerRowMapper intRowMapper = new IntegerRowMapper();

    public int addUser(String loginname, DbAdapter db) {
        db.getJdbcTemplate().update(INSERT_USER, new Object[] { loginname });
        return db.getUserId(loginname);
    }

    // ToDo - get user properly
    /*public OWL2NL_QRUser getUser(int userId, DbAdapter db) {
        int numberOfAnswers = getNumberOfAnswers(userId, db);

        List<OWL2NL_QRUser>
    }*/

    public int getNumberOfAnswers(int userId, DbAdapter db) {
        List<Integer> counts = db.getJdbcTemplate().query(SELECT_NUMBER_OF_ANSWERS, new Object[] { userId }, intRowMapper);
        if (counts.size() > 0) {
            return counts.get(0);
        } else {
            return 0;
        }
    }
}
