package org.aksw.simba.owl2nl.qr.data;

import org.aksw.simba.qr.datatypes.UserId;

public class OWL2NL_QRUser extends UserId {
    private int numberOfAnswers = 0;
    private boolean expert = false;
    private boolean expertSet = false;

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
        this.expertSet = true;
        this.expert = isExpert;
    }

    public boolean isExpert() {
        return expert;
    }

    public boolean isExpertSet() {
        return expertSet;
    }
}
