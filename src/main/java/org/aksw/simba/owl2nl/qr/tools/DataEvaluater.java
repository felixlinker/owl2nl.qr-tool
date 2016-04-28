package org.aksw.simba.owl2nl.qr.tools;

import org.aksw.simba.db.mapper.IntegerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by felix on 28.04.2016.
 */
public class DataEvaluater {

    // Axiom queries
    private static final String QUERY_SUM_ADEQUACY_AXIOM = "SELECT SUM(adequacy) FROM AxiomExperiments;";
    private static final String QUERY_SUM_FLUENCY_AXIOM = "SELECT SUM(fluency) FROM AxiomExperiments;";
    private static final String QUERY_AXIOM_COUNT = "SELECT COUNT(*) FROM AxiomExperiments;";

    // Resource queries
    private static final String QUERY_SUM_ADEQUACY_RESOURCE = "SELECT SUM(adequacy) FROM ResourceExperiments;";
    private static final String QUERY_SUM_COMPLETENESS_RESOURCE = "SELECT SUM(completeness) FROM ResourceExperiments;";
    private static final String QUERY_SUM_FLUENCY_RESOURCE_USER = "SELECT SUM(fluency) FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=0;";
    private static final String QUERY_SUM_FLUENCY_RESOURCE_EXPERT = "SELECT SUM(fluency) FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=1;";
    private static final String QUERY_RESOURCE_COUNT_USER = "SELECT COUNT(*) FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=0;";
    private static final String QUERY_RESOURCE_COUNT_EXPERT = "SELECT COUNT(*) FROM ResourceExperiments as R JOIN Users as U ON R.userId=U.id WHERE isExpert=1;";

    // Class queries
    private static final String QUERY_COUNT_CORRECT_CLASS_USER = "SELECT COUNT(*) FROM ClassExperiments as E JOIN Instances as I ON E.usersChoice=I.id JOIN Users as U ON E.userId=U.id WHERE correctInstance=1 AND U.isExpert=0;";
    private static final String QUERY_COUNT_CORRECT_CLASS_EXPERT = "SELECT COUNT(*) FROM ClassExperiments as E JOIN Instances as I ON E.usersChoice=I.id JOIN Users as U ON E.userId=U.id WHERE correctInstance=1 AND U.isExpert=1";
    private static final String QUERY_CLASS_COUNT_USER = "SELECT COUNT(*) FROM ClassExperiments AS E JOIN Users as U ON E.userId=U.id WHERE U.isExpert=0;";
    private static final String QUERY_CLASS_COUNT_EXPERT = "SELECT COUNT(*) FROM ClassExperiments AS E JOIN Users as U ON E.userId=U.id WHERE U.isExpert=1;";

    private static final IntegerRowMapper INTEGER_ROW_MAPPER = new IntegerRowMapper();

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = DataInserter.getJdbcTemplate();

        evaluateAxiomExperiments(jdbcTemplate);
        evaluateResourceExperiments(jdbcTemplate);

    }

    public static void evaluateAxiomExperiments(JdbcTemplate jdbcTemplate) {
        List<Integer> queryResults;

        queryResults = jdbcTemplate.query(QUERY_AXIOM_COUNT, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int axiomCount = queryResults.get(0);

        queryResults = jdbcTemplate.query(QUERY_SUM_ADEQUACY_AXIOM, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        double meanAdequacyAxiom = queryResults.get(0) / axiomCount;

        queryResults = jdbcTemplate.query(QUERY_SUM_FLUENCY_AXIOM, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        double meanFluencyAxiom = queryResults.get(0) / axiomCount;

        System.out.println("Evaluation of adequacy experiments:");
        System.out.println("Mean adequacy is: " + meanAdequacyAxiom);
        System.out.println("Mean fluency is: " + meanFluencyAxiom);
    }

    public static void evaluateResourceExperiments(JdbcTemplate jdbcTemplate) {
        List<Integer> queryResults;

        queryResults = jdbcTemplate.query(QUERY_RESOURCE_COUNT_EXPERT, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int resourceCountExpert = queryResults.get(0);

        queryResults = jdbcTemplate.query(QUERY_RESOURCE_COUNT_USER, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int resourceCountUser = queryResults.get(0);
        int resourceCount = resourceCountExpert + resourceCountUser;

        queryResults = jdbcTemplate.query(QUERY_SUM_ADEQUACY_RESOURCE, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        double meanAdequacy = queryResults.get(0) / resourceCountExpert;

        queryResults = jdbcTemplate.query(QUERY_SUM_COMPLETENESS_RESOURCE, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        double meanCompleteness = queryResults.get(0) / resourceCountExpert;

        queryResults = jdbcTemplate.query(QUERY_SUM_FLUENCY_RESOURCE_EXPERT, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int sumFluencyExpert = queryResults.get(0);

        queryResults = jdbcTemplate.query(QUERY_SUM_FLUENCY_RESOURCE_USER, INTEGER_ROW_MAPPER);
        if (queryResults.isEmpty()) {
            return;
        }
        int sumFluencyUser = queryResults.get(0);

        double meanFluency = (sumFluencyExpert + sumFluencyUser) / resourceCount;
        double meanFluencyExpert = sumFluencyExpert / resourceCountExpert;
        double meanFluencyUser = sumFluencyUser / resourceCountUser;

        System.out.println("Evaluation of resource experiments:");
        System.out.println("Experts voted on fluency, adequacy and completeness.");
        System.out.println("Mean expert fluency rating: " + meanFluencyExpert);
        System.out.println("Mean expert adequacy rating: " + meanAdequacy);
        System.out.println("Mean expert completeness rating: " + meanCompleteness);
        System.out.println("Users voted only on fluency.");
        System.out.println("Mean user fluency rating: " + meanFluencyUser);
        System.out.println("Since both experts and users voted on fluency, we can combine their rating:");
        System.out.println("Mean user rating: " + meanFluency);
    }

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

        double percentageCorrectExpert = (sumCorrectInstanceExpert / classCountExpert) * 100;
        double percentageCorrectUser = (sumCorrectInstanceUser / classCountUser) * 100;
        double percentageCorrect = ((sumCorrectInstanceExpert + sumCorrectInstanceUser) / (classCountExpert + classCountUser)) * 100;

        System.out.println("Evaluation of class experiments:");
        System.out.println("Percentage of correctly guessed instances by experts: " + percentageCorrectExpert);
        System.out.println("Percentage of correctly guessed instances by users: " + percentageCorrectUser);
        System.out.println("Percentage of correctly guessed instances overall: " + percentageCorrect);
    }
}
