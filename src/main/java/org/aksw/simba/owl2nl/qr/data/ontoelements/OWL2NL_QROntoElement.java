package org.aksw.simba.owl2nl.qr.data.ontoelements;

/**
 * Base class for all elements of an ontology that are stored in the evaluation db
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
