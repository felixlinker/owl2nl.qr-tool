package org.aksw.simba.owl2nl.qr.data;

import org.aksw.simba.qr.datatypes.UserId;

public class OWL2NL_QRUser extends UserId {
    private int numberOfAnswers;
    private boolean isExpert;
    private boolean isExpertSet = false;

    public OWL2NL_QRUser(int id, int numberOfAnswers) {
        super(id);
        this.numberOfAnswers = numberOfAnswers;
    }

    public int getNumberOfAnswers() {
        return numberOfAnswers;
    }

    public void setExpert(boolean isExpert) {
        this.isExpertSet = true;
        this.isExpert = isExpert;
    }

    public boolean isExpert() {
        return isExpert;
    }

    public boolean isExpertSet() {
        return isExpertSet;
    }
}
