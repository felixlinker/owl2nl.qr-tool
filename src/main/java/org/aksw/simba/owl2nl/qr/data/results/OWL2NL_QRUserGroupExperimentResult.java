package org.aksw.simba.owl2nl.qr.data.results;

/**
 * Created by felix on 07.04.2016.
 */
public class OWL2NL_QRUserGroupExperimentResult extends OWL2NL_QRExperimentResult {
    private boolean isExpert;
    public OWL2NL_QRUserGroupExperimentResult(boolean isExpert) {
        super(-1);
        this.isExpert = isExpert;
    }

    public boolean isExpert() {
        return isExpert;
    }
}
