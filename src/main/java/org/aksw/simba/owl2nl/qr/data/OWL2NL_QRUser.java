package org.aksw.simba.owl2nl.qr.data;

import org.aksw.simba.qr.datatypes.UserId;

public class OWL2NL_QRUser extends UserId {

    private int numberOfAnswers = 0;

    private boolean expert = false;

    public OWL2NL_QRUser(int id, boolean isExpert) {
        super(id);
        this.expert = isExpert;
    }

    public void setNumberOfAnswers(int numberOfAnswers) {
        this.numberOfAnswers = numberOfAnswers;
    }

    public int getNumberOfAnswers() {
        return numberOfAnswers;
    }

    public boolean isExpert() {
        return expert;
    }
}
