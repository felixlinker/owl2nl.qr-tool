package org.aksw.simba.owl2nl.qr.data.results;

/**
 * Created by felix on 04.04.2016.
 */
public class OWL2NL_QRResourceVerbExperimentResult extends OWL2NL_QRExperimentResult {
    private int adequacy;
    private int fluency;
    private int completeness;

    /**
     * Constructor for user group amateur result
     * @param id
     * @param fluency
     */
    public OWL2NL_QRResourceVerbExperimentResult(int id, int fluency) {
        super(id);
        initResult(-1, fluency, -1);
    }

    /**
     * Constructor for user group expert result
     * @param id
     * @param adequacy
     * @param fluency
     * @param completeness
     */
    public OWL2NL_QRResourceVerbExperimentResult(int id, int adequacy, int fluency, int completeness) {
        super(id);
        initResult(adequacy, fluency, completeness);
    }

    /**
     * Local constructor helper
     * @param adequacy
     * @param fluency
     * @param completeness
     */
    private void initResult(int adequacy, int fluency, int completeness) {
        this.adequacy = adequacy;
        this.fluency = fluency;
        this.completeness = completeness;
    }

    public int getAdequacy() {
        return adequacy;
    }

    public int getCompleteness() {
        return completeness;
    }

    public int getFluency() {
        return fluency;
    }
}
