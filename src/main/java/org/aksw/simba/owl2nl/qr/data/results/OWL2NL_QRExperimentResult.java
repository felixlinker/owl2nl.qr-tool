package org.aksw.simba.owl2nl.qr.data.results;

import org.aksw.simba.qr.datatypes.ExperimentResult;

/**
 * Base class for all experiment result
 */
public abstract class OWL2NL_QRExperimentResult implements ExperimentResult {
    private OWL2NL_QRExperimentResultBase baseResult;

    public OWL2NL_QRExperimentResult(OWL2NL_QRExperimentResultBase baseResult) {
        this.baseResult = baseResult;
    }

    @Override
    public int getExperimentSetupId() {
        return baseResult.getExperimentSetupId();
    }

    public boolean isExpert() {
        return baseResult.isExpert();
    }

    public boolean isExpertSet() {
        return baseResult.isExpertSet();
    }
}
