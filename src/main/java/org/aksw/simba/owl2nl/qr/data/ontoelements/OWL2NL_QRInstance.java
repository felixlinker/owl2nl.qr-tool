package org.aksw.simba.owl2nl.qr.data.ontoelements;

import java.util.LinkedList;

/**
 * Created by felix on 18.04.2016.
 */
public class OWL2NL_QRInstance extends OWL2NL_QROntoElement {
    private String name;
    private LinkedList<OWL2NL_QRTriple> triples = new LinkedList<>();

    public OWL2NL_QRInstance(int id, String name) {
        super(id);
        this.name = name;
    }

    public void addTriples(LinkedList<OWL2NL_QRTriple> triples) {
        triples.addAll(triples);
    }

    public LinkedList<OWL2NL_QRTriple> getTriples() {
        return (LinkedList<OWL2NL_QRTriple>)triples.clone();
    }

    public String getName() {
        return name;
    }
}
