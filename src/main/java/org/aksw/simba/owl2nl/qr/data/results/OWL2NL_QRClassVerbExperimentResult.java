package org.aksw.simba.owl2nl.qr.data.results;

import org.aksw.simba.qr.datatypes.ExperimentResult;

/**
 * Experiment result for class verbalization experiment
 */
public class OWL2NL_QRClassVerbExperimentResult extends OWL2NL_QRExperimentResult {
    /**
     * Triple chosen by user to be a correct instance of the associated axiom
     */
    private int chosenTriple;

    public OWL2NL_QRClassVerbExperimentResult(OWL2NL_QRExperimentResultBase baseResult, int chosenTriple) {
        super(baseResult);
        this.chosenTriple = chosenTriple;
    }

    public int getChosenTriple() {
        return chosenTriple;
    }
}
