package org.aksw.simba.owl2nl.qr.data.results;

/**
 * Created by felix on 08.04.2016.
 */
public class OWL2NL_QRExperimentResultBase {
    /**
     * id of the experiment in its sql table
     */
    private int experimentSetupId;

    /**
     * True if the experiment result set the user group
     */
    private boolean expertSet = false;

    /**
     * user group value
     */
    private boolean expert = false;

    public OWL2NL_QRExperimentResultBase(int id) {
        this.experimentSetupId = id;
    }

    public int getExperimentSetupId() {
        return experimentSetupId;
    }

    public void setExpert(boolean expert) {
        this.expertSet = true;
        this.expert = expert;
    }

    public boolean isExpert() {
        return expert;
    }

    public boolean isExpertSet() {
        return expertSet;
    }
}
