package org.aksw.simba.owl2nl.qr.tools;

import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataEvaluater {

    private static final IntegerRowMapper INTEGER_ROW_MAPPER = new IntegerRowMapper();

    private static final String QUERY_COUNT_USERS = "SELECT count(*) FROM Users;";
    private static final String QUERY_COUNT_USERS_EXPERT = "SELECT count(*) FROM Users WHERE isExpert=1;";
    private static final String QUERY_COUNT_USERS_USER = "SELECT count(*) FROM Users WHERE isExpert=0;";

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = DataInserter.getJdbcTemplate();

        List<Integer> results = jdbcTemplate.query(QUERY_COUNT_USERS, INTEGER_ROW_MAPPER);
        int usersCount = 0;
        if (!results.isEmpty()) {
            usersCount = results.get(0);
        }
        System.out.println(usersCount + " Users have participated in this experiment.");

        results = jdbcTemplate.query(QUERY_COUNT_USERS_EXPERT, INTEGER_ROW_MAPPER);
        int usersCountExpert = 0;
        if (!results.isEmpty()) {
            usersCountExpert = results.get(0);
        }
        System.out.println(usersCountExpert + " of them were experts.");

        results = jdbcTemplate.query(QUERY_COUNT_USERS_USER, INTEGER_ROW_MAPPER);
        int usersCountUser = 0;
        if (!results.isEmpty()) {
            usersCountUser = results.get(0);
        }
        System.out.println(usersCountUser + " of them were normal users.");

        System.out.println("That means that " + (usersCount - usersCountExpert - usersCountUser) + " didn't start the evaluation at all.");

        System.out.println();

        evaluateAxiomExperiments(jdbcTemplate);
        evaluateResourceExperiments(jdbcTemplate);
        evaluateClassExperiments(jdbcTemplate);
    }

    private static final RowMapper<Integer> FluencyRowMapper = (rs, rowNum) -> rs.getInt("fluency");
    private static final RowMapper<Integer> AdequacyRowMapper = (rs, rowNum) -> rs.getInt("adequacy");
    private static final RowMapper<Integer> CompletenessRowMapper = (rs, rowNum) -> rs.getInt("completeness");

    // Axiom queries
    private static final String QUERY_AXIOM_RATINGS = "SELECT fluency, adequacy FROM AxiomExperiments WHERE adequacy > 0 AND fluency > 0;";
//    private static final String QUERY_FLUENCY_RATINGS_AXIOM = "SELECT fluency FROM AxiomExperiments;";
//    private static final String QUERY_ADEQUACY_RATINGS_AXIOM = "SELECT adequacy FROM AxiomExperiments;";

    public static void evaluateAxiomExperiments(JdbcTemplate jdbcTemplate) {
        List<Integer> queryResults;

        queryResults = jdbcTemplate.query(QUERY_AXIOM_RATINGS, FluencyRowMapper);
        int[] fluencyCounts = applyStarRatings(new int[5], queryResults);
        queryResults = jdbcTemplate.query(QUERY_AXIOM_RATINGS, AdequacyRowMapper);
        int[] adequacyCounts = applyStarRatings(new int[5], queryResults);

        System.out.println("Evaluation of axiom experiments:");
        System.out.println("Adequacy ratings from 1 to 5 stars: " + Arrays.toString(adequacyCounts) + ", total: " + countArray(adequacyCounts));
        System.out.println("Fluency ratings from 1 to 5 stars: " + Arrays.toString(fluencyCounts) + ", total: " + countArray(fluencyCounts));
        System.out.println();
    }

    // Resource queries
    private static final String QUERY_RESOURCE_RATINGS_EXPERTS = "SELECT adequacy, completeness, fluency FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE adequacy is NOT NULL AND adequacy > 0 AND completeness IS NOT NULL AND completeness > 0 AND fluency > 0 AND isExpert=1;";
    private static final String QUERY_RESOURCE_RATINGS_USERS = "SELECT adequacy, completeness, fluency FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE fluency > 0 AND isExpert=0;";
//    private static final String QUERY_ADEQUACY_RATINGS_RESOURCE = "SELECT adequacy FROM ResourceExperiments WHERE adequacy IS NOT NULL;";
//    private static final String QUERY_COMPLETENESS_RATINGS_RESOURCE = "SELECT completeness FROM ResourceExperiments WHERE completeness IS NOT NULL;";
//    private static final String QUERY_FLUENCY_RATINGS_RESOURCE_USER = "SELECT fluency FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=0;";
//    private static final String QUERY_FLUENCY_RATINGS_RESOURCE_EXPERT = "SELECT fluency FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=1;";

    public static void evaluateResourceExperiments(JdbcTemplate jdbcTemplate) {
        List<Integer> queryResults;

        queryResults = jdbcTemplate.query(QUERY_RESOURCE_RATINGS_USERS, FluencyRowMapper);
        int[] userFluencyCounts = applyStarRatings(new int[5], queryResults);
        queryResults = jdbcTemplate.query(QUERY_RESOURCE_RATINGS_EXPERTS, FluencyRowMapper);
        int[] expertFluencyCounts = applyStarRatings(new int[5], queryResults);
        queryResults = jdbcTemplate.query(QUERY_RESOURCE_RATINGS_EXPERTS, AdequacyRowMapper);
        int[] expertAdequacyCounts = applyStarRatings(new int[5], queryResults);
        queryResults = jdbcTemplate.query(QUERY_RESOURCE_RATINGS_EXPERTS, CompletenessRowMapper);
        int[] expertCompletenessCounts = applyStarRatings(new int[5], queryResults);

        System.out.println("Evaluation of resource experiments:");
        System.out.println("Expert fluency ratings from 1 to 5 stars: " + Arrays.toString(expertFluencyCounts) + ", total: " + countArray(expertFluencyCounts));
        System.out.println("Expert adequacy ratings from 1 to 5 stars: " + Arrays.toString(expertAdequacyCounts) + ", total: " + countArray(expertAdequacyCounts));
        System.out.println("Expert completeness ratings from 1 to 5 stars: " + Arrays.toString(expertCompletenessCounts) + ", total: " + countArray(expertCompletenessCounts));
        System.out.println("User fluency ratings from 1 to 5 stars: " + Arrays.toString(userFluencyCounts) + ", total: " + countArray(userFluencyCounts));
        System.out.println();
    }

    // Class queries
    private static final String QUERY_COUNT_CORRECT_CLASS_USER = "SELECT COUNT(*) FROM ClassExperiments as E JOIN Instances as I ON E.usersChoice=I.id JOIN Users as U ON E.userId=U.id WHERE correctInstance=1 AND U.isExpert=0;";
    private static final String QUERY_COUNT_CORRECT_CLASS_EXPERT = "SELECT COUNT(*) FROM ClassExperiments as E JOIN Instances as I ON E.usersChoice=I.id JOIN Users as U ON E.userId=U.id WHERE correctInstance=1 AND U.isExpert=1";
    private static final String QUERY_CLASS_COUNT_USER = "SELECT COUNT(*) FROM ClassExperiments AS E JOIN Users as U ON E.userId=U.id WHERE U.isExpert=0;";
    private static final String QUERY_CLASS_COUNT_EXPERT = "SELECT COUNT(*) FROM ClassExperiments AS E JOIN Users as U ON E.userId=U.id WHERE U.isExpert=1;";

    public static void evaluateClassExperiments(JdbcTemplate jdbcTemplate) {
        List<Integer> queryResults;

        queryResults = jdbcTemplate.query(QUERY_CLASS_COUNT_EXPERT, INTEGER_ROW_MAPPER);
        int classCountExpert = 0;
        if (!queryResults.isEmpty()) {
            classCountExpert = queryResults.get(0);
        }

        queryResults = jdbcTemplate.query(QUERY_CLASS_COUNT_USER, INTEGER_ROW_MAPPER);
        int classCountUser = 0;
        if (!queryResults.isEmpty()) {
            classCountUser = queryResults.get(0);
        }

        queryResults = jdbcTemplate.query(QUERY_COUNT_CORRECT_CLASS_EXPERT, INTEGER_ROW_MAPPER);
        int sumCorrectInstanceExpert = 0;
        if (!queryResults.isEmpty()) {
            sumCorrectInstanceExpert = queryResults.get(0);
        }

        queryResults = jdbcTemplate.query(QUERY_COUNT_CORRECT_CLASS_USER, INTEGER_ROW_MAPPER);
        int sumCorrectInstanceUser = 0;
        if (!queryResults.isEmpty()) {
            sumCorrectInstanceUser = queryResults.get(0);
        }

        System.out.println("Evaluation of class experiments:");
        System.out.println("Experts guessed " + sumCorrectInstanceExpert + " of " + classCountExpert);
        System.out.println("Experts guessed " + sumCorrectInstanceUser + " of " + classCountUser);
        System.out.println();
    }

    private static int countArray(int[] array) {
        return Arrays.stream(array).reduce(0, (a, b) -> a + b);
    }

    private static int[] applyStarRatings(int[] ratings, List<Integer> results) {
        results = results.stream().filter(result -> result > 0).collect(Collectors.toList());
        results.stream().forEach(result -> ratings[result - 1]++);
        return ratings;
    }
}
