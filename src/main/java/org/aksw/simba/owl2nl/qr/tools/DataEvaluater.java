package org.aksw.simba.owl2nl.qr.tools;

import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        printResultsToTSV("C:\\Users\\felix\\Desktop\\axioms.tsv", "C:\\Users\\felix\\Desktop\\resources_exp.tsv", "C:\\Users\\felix\\Desktop\\resources_user.tsv");
    }

    private static final String QUERY_AXIOM_RATINGS_DETAILED = "SELECT axiom, " +
            "verbalization, " +
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
            "FROM Axioms as A JOIN AxiomExperiments as E ON E.axiomId = A.id AND adequacy > 0 AND fluency > 0 GROUP BY axiom, verbalization;";

    private static class AxiomRating {
        String axiom;
        String verbalization;
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

        AxiomRating(String axiom, String verbalization) {
            this.axiom = axiom;
            this.verbalization = verbalization;
        }

        @Override
        public String toString() {
            String tsvLine = axiom.concat("\t").concat(verbalization);
            for (int rating: fluencyRatings) {
                tsvLine = tsvLine.concat("\t").concat(Integer.toString(rating));
            }

            for (int rating: adequacyRatings) {
                tsvLine = tsvLine.concat("\t").concat(Integer.toString(rating));
            }

            return tsvLine;
        }
    }

    private static final RowMapper<AxiomRating> AXIOM_RATING_ROW_MAPPER = (rs, rowNum) -> {
        AxiomRating rating = new AxiomRating(rs.getString("axiom"), rs.getString("verbalization"));
        for (int i = 1; i <= 5; i++) {
            rating.addFluencyRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Fluency")));
            rating.addAdequacyRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Adequacy")));
        }

        return rating;
    };

    private static final String QUERY_RESOURCE_RATINGS_DETAILED_EXPERT = "SELECT resource, " +
            "verbalization, " +
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
            "FROM Resources as R JOIN ResourceExperiments as E ON R.id = E.resourceId JOIN Users as U on E.userId = U.id WHERE U.isExpert = 1 AND completeness > 0 AND adequacy > 0 AND fluency > 0 GROUP BY resource, verbalization;";

    private static final String QUERY_RESOURCE_RATINGS_DETAILED_USER = "SELECT resource, " +
            "verbalization, " +
            "SUM( CASE WHEN fluency = 1 THEN 1 else 0 END) as star1Fluency, " +
            "SUM( CASE WHEN fluency = 2 THEN 1 ELSE 0 END) as star2Fluency, " +
            "SUM( CASE WHEN fluency = 3 THEN 1 ELSE 0 END) as star3Fluency, " +
            "SUM( CASE WHEN fluency = 4 THEN 1 ELSE 0 END) as star4Fluency, " +
            "SUM( CASE WHEN fluency = 5 THEN 1 ELSE 0 END) as star5Fluency " +
            "FROM Resources as R JOIN ResourceExperiments as E ON R.id = E.resourceId JOIN Users as U on E.userId = U.id WHERE U.isExpert = 0 AND fluency > 0 GROUP BY resource, verbalization;";

    private static class ResourceRatingUser {
        String resource;
        String verbalization;
        int[] fluencyRatings = new int[5];

        void addFluencyRating(int rating, int count) {
            addRating(fluencyRatings, rating, count);
        }

        void addRating(int[] ratings, int rating, int count) {
            if (rating <= 0 ||rating > 5) {
                return;
            }

            ratings[rating - 1] = count;
        }

        ResourceRatingUser(String resource, String verbalization) {
            this.resource = resource;
            this.verbalization = verbalization;
        }

        @Override
        public String toString() {
            String tsvLine = resource.concat("\t").concat(verbalization);
            for (int rating: fluencyRatings) {
                tsvLine = tsvLine.concat("\t").concat(Integer.toString(rating));
            }

            return tsvLine;
        }
    }

    private static class ResourceRatingExpert extends ResourceRatingUser {
        int[] completenessRatings = new int[5];
        int[] adequacyRatings = new int[5];

        void addCompletenessRating(int rating, int count) {
            addRating(completenessRatings, rating, count);
        }

        void addAdequacyRating(int rating, int count) {
            addRating(adequacyRatings, rating, count);
        }

        ResourceRatingExpert(String resource, String verbalization) {
            super(resource, verbalization);
        }

        @Override
        public String toString() {
            String tsvLine = super.toString();
            for (int rating: adequacyRatings) {
                tsvLine = tsvLine.concat("\t").concat(Integer.toString(rating));
            }

            for (int rating: completenessRatings) {
                tsvLine = tsvLine.concat("\t").concat(Integer.toString(rating));
            }

            return tsvLine;
        }
    }

    private static final RowMapper<ResourceRatingExpert> RESOURCE_RATING_EXPERT_ROW_MAPPER = (rs, rowNum) -> {
        ResourceRatingExpert rating = new ResourceRatingExpert(rs.getString("resource"), rs.getString("verbalization"));
        for (int i = 1; i <= 5; i++) {
            rating.addFluencyRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Fluency")));
            rating.addAdequacyRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Adequacy")));
            rating.addCompletenessRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Completeness")));
        }

        return rating;
    };

    private static final RowMapper<ResourceRatingUser> RESOURCE_RATING_USER_ROW_MAPPER = (rs, rowNum) -> {
        ResourceRatingExpert rating = new ResourceRatingExpert(rs.getString("resource"), rs.getString("verbalization"));
        for (int i = 1; i <= 5; i++) {
            rating.addFluencyRating(i, rs.getInt("star".concat(Integer.toString(i)).concat("Fluency")));
        }

        return rating;
    };

    private static void printResultsToTsv(String fileName, List<? extends Object> results) {
        try (BufferedWriter fw = new BufferedWriter(new FileWriter(fileName))) {
            results.stream().map(Object::toString)
                    .forEach(line -> {
                        try {
                            fw.write(line);
                            fw.newLine();
                        } catch (IOException e) {
                            System.out.println("Exception writing file ".concat(fileName));
                        }
                    });
        } catch (IOException e) {
            System.out.println("Exception writing file ".concat(fileName));
        }
    }

    public static void printResultsToTSV(String axiomFile, String resourceFileExp, String resourceFileUser) {
        JdbcTemplate jdbcTemplate = DataInserter.getJdbcTemplate();

        List<AxiomRating> axiomRatings = jdbcTemplate.query(QUERY_AXIOM_RATINGS_DETAILED, AXIOM_RATING_ROW_MAPPER);
        List<ResourceRatingExpert> resourceRatingExperts = jdbcTemplate.query(QUERY_RESOURCE_RATINGS_DETAILED_EXPERT, RESOURCE_RATING_EXPERT_ROW_MAPPER);
        List<ResourceRatingUser> resourceRatingUsers = jdbcTemplate.query(QUERY_RESOURCE_RATINGS_DETAILED_USER, RESOURCE_RATING_USER_ROW_MAPPER);

        printResultsToTsv(axiomFile, axiomRatings);
        printResultsToTsv(resourceFileExp, resourceRatingExperts);
        printResultsToTsv(resourceFileUser, resourceRatingUsers);
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
