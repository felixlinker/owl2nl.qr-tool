package org.aksw.simba.owl2nl.qr.data.results;

/**
 * Experiment result for resource verbalization
 */
public class OWL2NL_QRResourceVerbExperimentResult extends OWL2NL_QRExperimentResult {
    /**
     * user rating for adequacy - can be -1 if not set
     */
    private int adequacy;

    /**
     * user rating for fluency
     */
    private int fluency;

    /**
     * user rating for completeness - can be -1 if not set
     */
    private int completeness;

    /**
     * Constructor for user group amateur result
     * @param baseResult
     * @param fluency
     */
    public OWL2NL_QRResourceVerbExperimentResult(OWL2NL_QRExperimentResultBase baseResult, int fluency) {
        super(baseResult);
        initResult(-1, fluency, -1);
    }

    /**
     * Constructor for user group expert result
     * @param id
     * @param adequacy
     * @param fluency
     * @param completeness
     */
    public OWL2NL_QRResourceVerbExperimentResult(OWL2NL_QRExperimentResultBase baseResult, int adequacy, int fluency, int completeness) {
        super(baseResult);
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
