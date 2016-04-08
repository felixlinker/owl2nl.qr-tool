package org.aksw.simba.owl2nl.qr.data.results;

/**
 * Created by felix on 04.04.2016.
 */
public class OWL2NL_QRClassVerbExperimentResult extends OWL2NL_QRExperimentResult {
    private int chosenTriple;

    public OWL2NL_QRClassVerbExperimentResult(int id, int chosenTriple) {
        super(id);
        this.chosenTriple = chosenTriple;
    }

    public int getChosenTriple() {
        return chosenTriple;
    }
}
