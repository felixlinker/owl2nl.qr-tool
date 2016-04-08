package org.aksw.simba.owl2nl.qr.data.ontoelements;

/**
 * Class for ontology triples stored in the db
 */
public class OWL2NL_QRTriple extends OWL2NL_QROntoElement {
    /**
     * Triple value
     */
    private String triple;

    /**
     * Triple verbalization
     */
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
