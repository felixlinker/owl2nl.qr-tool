package org.aksw.simba.owl2nl.qr.data.ontoelements;

/**
 * Created by felix on 05.04.2016.
 */
public class OWL2NL_QRTriple extends OWL2NL_QROntoElement {
    private String triple;
    private String verbalization;

    public OWL2NL_QRTriple(int id, String triple, String verbalization) {
        super(id);
        this.triple = triple;
        this.verbalization = verbalization;
    }

    public String getTriple() {
        return triple;
    }

    public String getVerbalization() {
        return verbalization;
    }
}
