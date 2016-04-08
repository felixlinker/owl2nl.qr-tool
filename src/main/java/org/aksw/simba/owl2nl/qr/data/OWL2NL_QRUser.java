package org.aksw.simba.owl2nl.qr.data;

import org.aksw.simba.qr.datatypes.UserId;

public class OWL2NL_QRUser extends UserId {
    private int numberOfAnswers = 0;
    private boolean isExpert = false;
    private boolean isExpertSet = false;

    public OWL2NL_QRUser(int id) {
        super(id);
    }

    public void setNumberOfAnswers(int numberOfAnswers) {
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
