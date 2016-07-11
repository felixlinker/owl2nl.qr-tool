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
//        evaluateMain();
        printResultsToCSV();
    }

    private static final String QUERY_AXIOM_RATINGS_DETAILED = "SELECT axiom, " +
            "SUM( CASE WHEN adequacy = 1 THEN 1 ELSE 0 END) as star1Adequacy, " +
            "SUM( CASE WHEN adequacy = 2 THEN 1 ELSE 0 END) as star2Adequacy, " +
            "SUM( CASE WHEN adequacy = 3 THEN 1 ELSE 0 END) as star3Adequacy, " +
            "SUM( CASE WHEN adequacy = 4 THEN 1 ELSE 0 END) as star4Adequacy, " +
            "SUM( CASE WHEN adequacy = 5 THEN 1 ELSE 0 END) as star5Adequacy, " +
            "SUM( CASE WHEN fluency = 1 THEN 1 else 0 END) as star1Fluency, " +
            "SUM( CASE WHEN fluency = 2 THEN 1 ELSE 0 END) as star2Fluency, " +
            "SUM( CASE WHEN fluency = 3 THEN 1 ELSE 0 END) as star3Fluency, " +
            "SUM( CASE WHEN fluency = 4 THEN 1 ELSE 0 END) as star4Fluency, " +
            "SUM( CASE WHEN fluency = 5 THEN 1 ELSE 0 END) as star5Fluency " +
            "FROM Axioms as A JOIN AxiomExperiments as E ON E.axiomId = A.id GROUP BY axiom;";

    private static class AxiomRating {

        String axiom;
        int[] adequacyRatings = new int[5];
        int[] fluencyRatings = new int[5];
        void addAdequacyRating(int rating, int count) {
            addRating(adequacyRatings, rating, count);
        }

        void addFluencyRating(int rating, int count) {
            addRating(fluencyRatings, rating, count);
        }

        private void addRating(int[] ratings, int rating, int count) {
            if (rating <= 0 ||rating > 5) {
                return;
            }

            ratings[rating - 1] = count;
        }

        AxiomRating(String axiom) {
            this.axiom = axiom;
        }

        @Override
        public String toString() {
            return axiom.concat("\nAdequacy ratings: ")
                    .concat(Arrays.toString(adequacyRatings))
                    .concat("\nFluency ratings: ")
                    .concat(Arrays.toString(fluencyRatings ))
                    .concat("\n");
        }
    }

    private static final RowMapper<AxiomRating> AXIOM_RATING_ROW_MAPPER = (rs, rowNum) -> {
        AxiomRating rating = new AxiomRating(rs.getString("axiom"));
        for (int i = 1; i <= 5; i++) {
            rating.addFluencyRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Fluency")));
            rating.addAdequacyRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Adequacy")));
        }

        return rating;
    };

    private static final String QUERY_RESOURCE_RATINGS_DETAILED = "SELECT resource, " +
            "SUM( CASE WHEN adequacy = 1 THEN 1 ELSE 0 END) as star1Adequacy, " +
            "SUM( CASE WHEN adequacy = 2 THEN 1 ELSE 0 END) as star2Adequacy, " +
            "SUM( CASE WHEN adequacy = 3 THEN 1 ELSE 0 END) as star3Adequacy, " +
            "SUM( CASE WHEN adequacy = 4 THEN 1 ELSE 0 END) as star4Adequacy, " +
            "SUM( CASE WHEN adequacy = 5 THEN 1 ELSE 0 END) as star5Adequacy, " +
            "SUM( CASE WHEN fluency = 1 THEN 1 else 0 END) as star1Fluency, " +
            "SUM( CASE WHEN fluency = 2 THEN 1 ELSE 0 END) as star2Fluency, " +
            "SUM( CASE WHEN fluency = 3 THEN 1 ELSE 0 END) as star3Fluency, " +
            "SUM( CASE WHEN fluency = 4 THEN 1 ELSE 0 END) as star4Fluency, " +
            "SUM( CASE WHEN fluency = 5 THEN 1 ELSE 0 END) as star5Fluency, " +
            "SUM( CASE WHEN completeness = 1 THEN 1 else 0 END) as star1Completeness, " +
            "SUM( CASE WHEN completeness = 2 THEN 1 ELSE 0 END) as star2Completeness, " +
            "SUM( CASE WHEN completeness = 3 THEN 1 ELSE 0 END) as star3Completeness, " +
            "SUM( CASE WHEN completeness = 4 THEN 1 ELSE 0 END) as star4Completeness, " +
            "SUM( CASE WHEN completeness = 5 THEN 1 ELSE 0 END) as star5Completeness " +
            "FROM Resources as R JOIN ResourceExperiments as E ON R.id = E.resourceId JOIN Users as U on E.userId = U.id WHERE U.isExpert = 1 GROUP BY resource;";

    private static class ResourceRating {
        String resource;
        int[] completenessRatings = new int[5];
        int[] adequacyRatings = new int[5];
        int[] fluencyRatings = new int[5];

        void addCompletenessRating(int rating, int count) {
            addRating(completenessRatings, rating, count);
        }

        void addAdequacyRating(int rating, int count) {
            addRating(adequacyRatings, rating, count);
        }

        void addFluencyRating(int rating, int count) {
            addRating(fluencyRatings, rating, count);
        }

        private void addRating(int[] ratings, int rating, int count) {
            if (rating <= 0 ||rating > 5) {
                return;
            }

            ratings[rating - 1] = count;
        }

        ResourceRating(String resource) {
            this.resource = resource;
        }

        @Override
        public String toString() {
            return resource.concat("\nAdequacy ratings: ")
                    .concat(Arrays.toString(adequacyRatings))
                    .concat("\nFluency ratings: ")
                    .concat(Arrays.toString(fluencyRatings ))
                    .concat("\nCompleteness ratings: ")
                    .concat(Arrays.toString(completenessRatings))
                    .concat("\n");
        }
    }

    private static final RowMapper<ResourceRating> RESOURCE_RATING_ROW_MAPPER = (rs, rowNum) -> {
        ResourceRating rating = new ResourceRating(rs.getString("resource"));
        for (int i = 1; i <= 5; i++) {
            rating.addFluencyRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Fluency")));
            rating.addAdequacyRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Adequacy")));
            rating.addCompletenessRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Completeness")));
        }

        return rating;
    };

    public static void printResultsToCSV() {
        JdbcTemplate jdbcTemplate = DataInserter.getJdbcTemplate();

        List<AxiomRating> axiomRatings = jdbcTemplate.query(QUERY_AXIOM_RATINGS_DETAILED, AXIOM_RATING_ROW_MAPPER);
        List<ResourceRating> resourceRatings = jdbcTemplate.query(QUERY_RESOURCE_RATINGS_DETAILED, RESOURCE_RATING_ROW_MAPPER);
        axiomRatings.stream().forEach(System.out::println);
        resourceRatings.stream().forEach(System.out::println);
    }

    public static void evaluateMain() {
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
    private static final String QUERY_AXIOM_RATINGS = "SELECT fluency, adequacy FROM AxiomExperiments;";

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
