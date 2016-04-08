package org.aksw.simba.owl2nl.qr.data.experiments;

import org.aksw.simba.qr.datatypes.ExperimentSetup;

public abstract class OWL2NL_QRExperimentSetup implements ExperimentSetup {
    private int id;
    private int userAnswerCount;
    private boolean performedByExpert;

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
        this.performedByExpert = performedByExpert;
    }
}
