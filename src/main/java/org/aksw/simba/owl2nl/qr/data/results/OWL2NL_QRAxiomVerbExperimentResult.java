package org.aksw.simba.owl2nl.qr.data.results;

/**
 * Created by felix on 04.04.2016.
 */
public class OWL2NL_QRAxiomVerbExperimentResult extends OWL2NL_QRExperimentResult {
    private int adequacy;
    private int fluency;

    public OWL2NL_QRAxiomVerbExperimentResult(int id, int adequacy, int fluency) {
        super(id);
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
