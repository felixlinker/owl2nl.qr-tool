package org.aksw.simba.owl2nl.qr.data.results;

import org.aksw.simba.qr.datatypes.ExperimentResult;

/**
 * Created by felix on 05.04.2016.
 */
public abstract class OWL2NL_QRExperimentResult implements ExperimentResult {
    private int experimentSetupId;
    public OWL2NL_QRExperimentResult(int id) {
        this.experimentSetupId = id;
    }

    @Override
    public int getExperimentSetupId() {
        return experimentSetupId;
    }
}
