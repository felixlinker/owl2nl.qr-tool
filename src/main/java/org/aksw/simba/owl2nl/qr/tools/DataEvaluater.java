package org.aksw.simba.owl2nl.qr.tools;

import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by felix on 28.04.2016.
 */
public class DataEvaluater {

    private static final IntegerRowMapper INTEGER_ROW_MAPPER = new IntegerRowMapper();


    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = DataInserter.getJdbcTemplate();

        evaluateAxiomExperiments(jdbcTemplate);
        evaluateResourceExperiments(jdbcTemplate);
        evaluateClassExperiments(jdbcTemplate);
    }

    // Axiom queries
    private static final String QUERY_FLUENCY_RATINGS_AXIOM = "SELECT adequacy FROM AxiomExperiments;";
    private static final String QUERY_ADEQUACY_RATINGS_AXIOM = "SELECT adequacy FROM AxiomExperiments;";
    private static final String QUERY_COUNT_AXIOM_EXPERIMENTS = "SELECT COUNT(*) FROM AxiomExperiments;";
    private static final String QUERY_COUNT_AXIOMS = "SELECT COUNT(*) FROM Axioms;";

    public static void evaluateAxiomExperiments(JdbcTemplate jdbcTemplate) {
        List<Integer> queryResults;

        queryResults = jdbcTemplate.query(QUERY_FLUENCY_RATINGS_AXIOM, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }

        double meanFluencyRating = calculateMean(queryResults);
        double stdDeviationFluencyRating = calculateStandardDeviation(queryResults, meanFluencyRating);

        queryResults = jdbcTemplate.query(QUERY_ADEQUACY_RATINGS_AXIOM, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        double meanAdequacyRating = calculateMean(queryResults);
        double stdDeviationAdequacyRating = calculateStandardDeviation(queryResults, meanAdequacyRating);

        queryResults = jdbcTemplate.query(QUERY_COUNT_AXIOMS, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int countAxioms = queryResults.get(0);

        queryResults = jdbcTemplate.query(QUERY_COUNT_AXIOM_EXPERIMENTS, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int countAxiomExeriments = queryResults.get(0);
        double meanAxiomAnswer =(double)countAxiomExeriments / (double)countAxioms;

        System.out.println("Evaluation of adequacy experiments:");
        System.out.println("Mean adequacy is: " + meanAdequacyRating + " with a standard deviation of: " + stdDeviationAdequacyRating);
        System.out.println("Mean fluency is: " + meanFluencyRating + " with a standard deviation of: " + stdDeviationFluencyRating);
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

        queryResults = jdbcTemplate.query(QUERY_ADEQUACY_RATINGS_RESOURCE, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        double meanAdequacy = calculateMean(queryResults);
        double stdDeviationAdequacy = calculateStandardDeviation(queryResults, meanAdequacy);

        queryResults = jdbcTemplate.query(QUERY_COMPLETENESS_RATINGS_RESOURCE, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        double meanCompleteness = calculateMean(queryResults);
        double stdDeviationCompleteness = calculateStandardDeviation(queryResults, meanCompleteness);

        List<Integer> fluencyRatingsExpert = jdbcTemplate.query(QUERY_FLUENCY_RATINGS_RESOURCE_EXPERT, INTEGER_ROW_MAPPER);
        if (fluencyRatingsExpert.isEmpty()) {
            return;
        }
        double meanFluencyExpert = calculateMean(fluencyRatingsExpert);
        double stdDeviationFluencyExpert = calculateStandardDeviation(fluencyRatingsExpert, meanFluencyExpert);

        List<Integer> fluencyRatingsUser = jdbcTemplate.query(QUERY_FLUENCY_RATINGS_RESOURCE_USER, INTEGER_ROW_MAPPER);
        if (fluencyRatingsUser.isEmpty()) {
            return;
        }
        double meanFluencyUser = calculateMean(fluencyRatingsUser);
        double stdDeviationFluencyUser = calculateStandardDeviation(fluencyRatingsUser, meanFluencyUser);

        List<Integer> fluencyRatings = fluencyRatingsExpert;
        fluencyRatings.addAll(fluencyRatingsUser);
        double meanFluency = calculateMean(fluencyRatings);
        double stdDeviationFluency = calculateStandardDeviation(fluencyRatings, meanFluency);

        queryResults = jdbcTemplate.query(QUERY_COUNT_RESOURCES, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int countResources = queryResults.get(0);

        queryResults = jdbcTemplate.query(QUERY_COUNT_USER_RESOURCE_EXPERIMENTS, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int countUserResourceExperiments = queryResults.get(0);

        queryResults = jdbcTemplate.query(QUERY_COUNT_EXPERT_RESOURCE_EXPERIMENTS, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int countExpertResourceExperiments = queryResults.get(0);

        double resourceTestedByExpert = (double)countExpertResourceExperiments / (double)countResources;
        double resourceTestedByUser = (double)countUserResourceExperiments / (double)countResources;
        double resourceTestedByAny = (double)(countExpertResourceExperiments + countUserResourceExperiments) / (double)countResources;


        System.out.println("Evaluation of resource experiments:");
        System.out.println("Experts voted on fluency, adequacy and completeness.");
        System.out.println("Mean expert fluency rating: " + meanFluencyExpert + " with a standard deviation of: " + stdDeviationFluencyExpert);
        System.out.println("Mean expert adequacy rating: " + meanAdequacy + " with a standard deviation of: " + stdDeviationAdequacy);
        System.out.println("Mean expert completeness rating: " + meanCompleteness + " with a standard deviation of: " + stdDeviationCompleteness);
        System.out.println("Users voted only on fluency.");
        System.out.println("Mean user fluency rating: " + meanFluencyUser + " with a standard deviation of: " + stdDeviationFluencyUser);
        System.out.println("Since both experts and users voted on fluency, we can combine their rating:");
        System.out.println("Mean fluency rating: " + meanFluency + " with a standard deviation of: " + stdDeviationFluency);
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
        if (queryResults.isEmpty()) {
            return;
        }
        int classCountExpert = queryResults.get(0);

        queryResults = jdbcTemplate.query(QUERY_CLASS_COUNT_USER, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int classCountUser = queryResults.get(0);

        queryResults = jdbcTemplate.query(QUERY_COUNT_CORRECT_CLASS_EXPERT, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int sumCorrectInstanceExpert = queryResults.get(0);

        queryResults = jdbcTemplate.query(QUERY_COUNT_CORRECT_CLASS_USER, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int sumCorrectInstanceUser = queryResults.get(0);

        double percentageCorrectExpert = ((double)sumCorrectInstanceExpert / (double)classCountExpert) * 100;
        double percentageCorrectUser = ((double)sumCorrectInstanceUser / (double)classCountUser) * 100;
        double percentageCorrect = ((double)(sumCorrectInstanceExpert + sumCorrectInstanceUser) / (double)(classCountExpert + classCountUser)) * 100;

        queryResults = jdbcTemplate.query(QUERY_COUNT_CLASSES, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int countClasses = queryResults.get(0);

        double answeredByExpert = (double)classCountExpert / (double)countClasses;
        double answeredByUser = (double)classCountUser / (double)countClasses;
        double answeredByAny = (double)(classCountExpert + classCountUser) / (double)countClasses;

        System.out.println("Evaluation of class experiments:");
        System.out.println("Percentage of correctly guessed instances by experts: " + percentageCorrectExpert);
        System.out.println("Percentage of correctly guessed instances by users: " + percentageCorrectUser);
        System.out.println("Percentage of correctly guessed instances overall: " + percentageCorrect);
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
