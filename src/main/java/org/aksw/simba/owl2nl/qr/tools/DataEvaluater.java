package org.aksw.simba.owl2nl.qr.tools;

import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.List;

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

    // Axiom queries
    private static final String QUERY_FLUENCY_RATINGS_AXIOM = "SELECT fluency FROM AxiomExperiments;";
    private static final String QUERY_ADEQUACY_RATINGS_AXIOM = "SELECT adequacy FROM AxiomExperiments;";
    private static final String QUERY_COUNT_AXIOM_EXPERIMENTS = "SELECT COUNT(*) FROM AxiomExperiments;";
    private static final String QUERY_COUNT_AXIOMS = "SELECT COUNT(*) FROM Axioms;";

    public static void evaluateAxiomExperiments(JdbcTemplate jdbcTemplate) {
        List<Integer> queryResults;

        queryResults = jdbcTemplate.query(QUERY_FLUENCY_RATINGS_AXIOM, INTEGER_ROW_MAPPER);
        double meanFluencyRating = Double.NaN;
        double stdDeviationFluencyRating = Double.NaN;
        if (!queryResults.isEmpty()) {
            meanFluencyRating = calculateMean(queryResults);
            stdDeviationFluencyRating = calculateStandardDeviation(queryResults, meanFluencyRating);
        }


        queryResults = jdbcTemplate.query(QUERY_ADEQUACY_RATINGS_AXIOM, INTEGER_ROW_MAPPER);
        double meanAdequacyRating = Double.NaN;
        double stdDeviationAdequacyRating = Double.NaN;
        if (!queryResults.isEmpty()) {
            meanAdequacyRating = calculateMean(queryResults);
            stdDeviationAdequacyRating = calculateStandardDeviation(queryResults, meanAdequacyRating);
        }

        queryResults = jdbcTemplate.query(QUERY_COUNT_AXIOMS, INTEGER_ROW_MAPPER);
        int countAxioms = 0;
        if (!queryResults.isEmpty()) {
            countAxioms = queryResults.get(0);
        }

        queryResults = jdbcTemplate.query(QUERY_COUNT_AXIOM_EXPERIMENTS, INTEGER_ROW_MAPPER);
        int countAxiomExperiments = 0;
        double meanAxiomAnswer = Double.NaN;
        if (!queryResults.isEmpty()) {
            countAxiomExperiments = queryResults.get(0);
            meanAxiomAnswer = (double) countAxiomExperiments / (countAxioms == 0 ? Double.NaN : (double) countAxioms);
        }

        System.out.println("Evaluation of adequacy experiments:");
        System.out.println("Mean adequacy is: " + meanAdequacyRating + " with a standard deviation of: " + stdDeviationAdequacyRating);
        System.out.println("Mean fluency is: " + meanFluencyRating + " with a standard deviation of: " + stdDeviationFluencyRating);
        System.out.println("There are " + countAxiomExperiments + " results.");
        System.out.println("Each axiom has been answered about " + meanAxiomAnswer + " times.");
        System.out.println();
    }

    // Resource queries
    private static final String QUERY_ADEQUACY_RATINGS_RESOURCE = "SELECT adequacy FROM ResourceExperiments WHERE adequacy IS NOT NULL;";
    private static final String QUERY_COMPLETENESS_RATINGS_RESOURCE = "SELECT completeness FROM ResourceExperiments WHERE completeness IS NOT NULL;";
    private static final String QUERY_FLUENCY_RATINGS_RESOURCE_USER = "SELECT fluency FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=0;";
    private static final String QUERY_FLUENCY_RATINGS_RESOURCE_EXPERT = "SELECT fluency FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=1;";

    private static final String QUERY_COUNT_RESOURCES = "SELECT COUNT(*) FROM Resources;";
    private static final String QUERY_COUNT_USER_RESOURCE_EXPERIMENTS = "SELECT COUNT(*) FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=0;";
    private static final String QUERY_COUNT_EXPERT_RESOURCE_EXPERIMENTS = "SELECT COUNT(*) FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=1;";

    public static void evaluateResourceExperiments(JdbcTemplate jdbcTemplate) {
        List<Integer> queryResults;


        double meanAdequacyExpert = Double.NaN;
        double stdDeviationAdequacyExpert = Double.NaN;
        queryResults = jdbcTemplate.query(QUERY_ADEQUACY_RATINGS_RESOURCE, INTEGER_ROW_MAPPER);
        if (!queryResults.isEmpty()) {
            meanAdequacyExpert = calculateMean(queryResults);
            stdDeviationAdequacyExpert = calculateStandardDeviation(queryResults, meanAdequacyExpert);
        }

        double meanCompletenessExpert = Double.NaN;
        double stdDeviationCompletenessExpert = Double.NaN;
        queryResults = jdbcTemplate.query(QUERY_COMPLETENESS_RATINGS_RESOURCE, INTEGER_ROW_MAPPER);
        if (!queryResults.isEmpty()) {
            meanCompletenessExpert = calculateMean(queryResults);
            stdDeviationCompletenessExpert = calculateStandardDeviation(queryResults, meanCompletenessExpert);
        }

        double meanFluencyExpert = Double.NaN;
        double stdDeviationFluencyExpert = Double.NaN;
        List<Integer> fluencyRatingsExpert = jdbcTemplate.query(QUERY_FLUENCY_RATINGS_RESOURCE_EXPERT, INTEGER_ROW_MAPPER);
        if (!fluencyRatingsExpert.isEmpty()) {
            meanFluencyExpert = calculateMean(fluencyRatingsExpert);
            stdDeviationFluencyExpert = calculateStandardDeviation(fluencyRatingsExpert, meanFluencyExpert);
        }

        double meanFluencyUser = Double.NaN;
        double stdDeviationFluencyUser = Double.NaN;
        List<Integer> fluencyRatingsUser = jdbcTemplate.query(QUERY_FLUENCY_RATINGS_RESOURCE_USER, INTEGER_ROW_MAPPER);
        if (!fluencyRatingsUser.isEmpty()) {
            meanFluencyUser = calculateMean(fluencyRatingsUser);
            stdDeviationFluencyUser = calculateStandardDeviation(fluencyRatingsUser, meanFluencyUser);
        }

        List<Integer> fluencyRatings = fluencyRatingsExpert;
        fluencyRatings.addAll(fluencyRatingsUser);
        double meanFluency = calculateMean(fluencyRatings);
        double stdDeviationFluency = calculateStandardDeviation(fluencyRatings, meanFluency);

        queryResults = jdbcTemplate.query(QUERY_COUNT_RESOURCES, INTEGER_ROW_MAPPER);
        int countResources = 0;
        if (!queryResults.isEmpty()) {
            countResources = queryResults.get(0);
        }

        queryResults = jdbcTemplate.query(QUERY_COUNT_USER_RESOURCE_EXPERIMENTS, INTEGER_ROW_MAPPER);
        int countUserResourceExperiments = 0;
        if (!queryResults.isEmpty()) {
            countUserResourceExperiments = queryResults.get(0);
        }

        queryResults = jdbcTemplate.query(QUERY_COUNT_EXPERT_RESOURCE_EXPERIMENTS, INTEGER_ROW_MAPPER);
        int countExpertResourceExperiments = 0;
        if (!queryResults.isEmpty()) {
            countExpertResourceExperiments = queryResults.get(0);
        }

        double resourceTestedByExpert = (double)countExpertResourceExperiments / (double)countResources;
        double resourceTestedByUser = (double)countUserResourceExperiments / (double)countResources;
        double resourceTestedByAny = (double)(countExpertResourceExperiments + countUserResourceExperiments) / (double)countResources;


        System.out.println("Evaluation of resource experiments:");
        System.out.println("Experts voted on fluency, adequacy and completeness.");
        System.out.println("Mean expert fluency rating: " + meanFluencyExpert + " with a standard deviation of: " + stdDeviationFluencyExpert);
        System.out.println("Mean expert adequacy rating: " + meanAdequacyExpert + " with a standard deviation of: " + stdDeviationAdequacyExpert);
        System.out.println("Mean expert completeness rating: " + meanCompletenessExpert + " with a standard deviation of: " + stdDeviationCompletenessExpert);

        System.out.println("Users voted only on fluency.");
        System.out.println("Mean user fluency rating: " + meanFluencyUser + " with a standard deviation of: " + stdDeviationFluencyUser);
        System.out.println("Since both experts and users voted on fluency, we can combine their rating:");
        System.out.println("Mean fluency rating: " + meanFluency + " with a standard deviation of: " + stdDeviationFluency);
        System.out.println("There are " + countExpertResourceExperiments + " results by experts.");
        System.out.println("There are " + countUserResourceExperiments + " results by users.");
        System.out.println("There are " + (countExpertResourceExperiments + countUserResourceExperiments) + " results in total.");
        System.out.println("Each resource has been tested by an user for " + resourceTestedByUser + " times");
        System.out.println("Each resource has been tested by an expert for " + resourceTestedByExpert + " times");
        System.out.println("Each resource has been tested by any for " + resourceTestedByAny + " times");
        System.out.println();
    }

    // Class queries
    private static final String QUERY_COUNT_CORRECT_CLASS_USER = "SELECT COUNT(*) FROM ClassExperiments as E JOIN Instances as I ON E.usersChoice=I.id JOIN Users as U ON E.userId=U.id WHERE correctInstance=1 AND U.isExpert=0;";
    private static final String QUERY_COUNT_CORRECT_CLASS_EXPERT = "SELECT COUNT(*) FROM ClassExperiments as E JOIN Instances as I ON E.usersChoice=I.id JOIN Users as U ON E.userId=U.id WHERE correctInstance=1 AND U.isExpert=1";
    private static final String QUERY_CLASS_COUNT_USER = "SELECT COUNT(*) FROM ClassExperiments AS E JOIN Users as U ON E.userId=U.id WHERE U.isExpert=0;";
    private static final String QUERY_CLASS_COUNT_EXPERT = "SELECT COUNT(*) FROM ClassExperiments AS E JOIN Users as U ON E.userId=U.id WHERE U.isExpert=1;";
    private static final String QUERY_COUNT_CLASSES = "SELECT COUNT(DISTINCT axiomId) FROM ClassExperiments";

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

        double percentageCorrectExpert = ((double) sumCorrectInstanceExpert / (classCountExpert == 0 ? Double.NaN : (double) classCountExpert)) * 100;
        double percentageCorrectUser = ((double) sumCorrectInstanceUser / (classCountUser == 0 ? Double.NaN : (double) classCountUser)) * 100;
        double percentageCorrect = ((double) (sumCorrectInstanceExpert + sumCorrectInstanceUser) / (classCountExpert + classCountUser == 0 ? Double.NaN : (double) (classCountExpert + classCountUser))) * 100;

        queryResults = jdbcTemplate.query(QUERY_COUNT_CLASSES, INTEGER_ROW_MAPPER);
        int countClasses = 0;
        if (!queryResults.isEmpty()) {
            countClasses = queryResults.get(0);
        }

        double countClassesDouble = countClasses == 0 ? Double.NaN : (double) countClasses;
        double answeredByExpert = (double) classCountExpert / countClassesDouble;
        double answeredByUser = (double) classCountUser / countClassesDouble;
        double answeredByAny = (double) (classCountExpert + classCountUser) / countClassesDouble;

        System.out.println("Evaluation of class experiments:");
        System.out.println("Percentage of correctly guessed instances by experts: " + percentageCorrectExpert);
        System.out.println("Percentage of correctly guessed instances by users: " + percentageCorrectUser);
        System.out.println("Percentage of correctly guessed instances overall: " + percentageCorrect);
        System.out.println("There are " + classCountExpert + " results by experts.");
        System.out.println("There are " + classCountUser + " results by users.");
        System.out.println("There are " + (classCountExpert + classCountUser) + " results in total.");
        System.out.println("Each class has been tested by an expert for " + answeredByExpert + " times");
        System.out.println("Each class has been tested by an user for " + answeredByUser + " times");
        System.out.println("Each class has been tested by any for " + answeredByAny + " times");
        System.out.println();
    }

    private static double calculateMean(Collection<Integer> values) {
        double size = values.size();
        return values.stream()
                .mapToDouble(value -> (double)value / size)
                .sum();
    }

    private static double calculateStandardDeviation(Collection<Integer> values, double mean) {
        double size = values.size();
        double sum = values.stream()
                .mapToDouble(value -> (value - mean)*(value - mean))
                .sum();

        return Math.sqrt(sum / size);
    }
}
