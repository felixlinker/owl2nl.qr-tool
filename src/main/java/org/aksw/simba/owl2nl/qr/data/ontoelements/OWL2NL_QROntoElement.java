package org.aksw.simba.owl2nl.qr.data.ontoelements;

/**
 * Created by felix on 05.04.2016.
 */
public abstract class OWL2NL_QROntoElement {
    private int id;
    public OWL2NL_QROntoElement(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
