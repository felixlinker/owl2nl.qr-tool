package org.aksw.simba.owl2nl.qr.data.results;

/**
 * Experiment result for Axiom verbalization
 */
public class OWL2NL_QRAxiomVerbExperimentResult extends OWL2NL_QRExperimentResult {

    /**
     * User rating for fluency
     */
    private int adequacy;

    /**
     * User rating for adequacy
     */
    private int fluency;

    public OWL2NL_QRAxiomVerbExperimentResult(OWL2NL_QRExperimentResultBase baseResult, int adequacy, int fluency) {
        super(baseResult);
        this.adequacy = adequacy;
        this.fluency = fluency;
    }

    public int getAdequacy() {
        return adequacy;
    }

    public int getFluency() {
        return fluency;
    }
}
