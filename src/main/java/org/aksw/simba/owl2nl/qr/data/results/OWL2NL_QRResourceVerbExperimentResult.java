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
     * @param baseResult Base result
     * @param fluency FLuency rating
     */
    public OWL2NL_QRResourceVerbExperimentResult(OWL2NL_QRExperimentResultBase baseResult, int fluency) {
        this(baseResult, -1, fluency, -1);
    }

    /**
     * Constructor for user group expert result
     * @param baseResult Base result
     * @param adequacy Adequacy rating
     * @param fluency Fluency rating
     * @param completeness Completeness rating
     */
    public OWL2NL_QRResourceVerbExperimentResult(OWL2NL_QRExperimentResultBase baseResult, int adequacy, int fluency, int completeness) {
        super(baseResult);
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
