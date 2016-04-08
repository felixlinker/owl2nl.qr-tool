package org.aksw.simba.owl2nl.qr.data.experiments;

import org.aksw.simba.qr.datatypes.ExperimentSetup;

/**
 * Basic experiment setup for SemWeb2NL evaluation experiments
 */
public abstract class OWL2NL_QRExperimentSetup implements ExperimentSetup {
    /**
     * Experiment id in its base table
     */
    private int id;

    /**
     * Answer count of the current user - not the experiment
     */
    private int userAnswerCount;

    /**
     * Flag to symbolize whether this experiment is performed by an expert right now
     */
    private boolean performedByExpert = false;

    /**
     * Flag to symbolize whether the experiment must include a user group selection
     */
    private boolean expertValueKnown = false;

    public OWL2NL_QRExperimentSetup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getUserAnswerCount() {
        return userAnswerCount;
    }

    public void setUserAnswerCount(int answerCount) {
        this.userAnswerCount = answerCount;
    }

    public boolean isPerformedByExpert() {
        return performedByExpert;
    }

    public void setPerformedByExpert(boolean performedByExpert) {
        this.expertValueKnown = true;
        this.performedByExpert = performedByExpert;
    }

    public boolean isExpertValueKnown() {
        return expertValueKnown;
    }
}
