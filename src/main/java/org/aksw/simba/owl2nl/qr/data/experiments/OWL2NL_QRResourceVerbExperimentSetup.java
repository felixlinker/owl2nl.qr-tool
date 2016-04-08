package org.aksw.simba.owl2nl.qr.data.experiments;

import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;

import java.util.LinkedList;
import java.util.List;

public class OWL2NL_QRResourceVerbExperimentSetup extends OWL2NL_QRExperimentSetup {

    private String resourceVerbalization;
    private LinkedList<OWL2NL_QRTriple> triples = new LinkedList<>();

    public OWL2NL_QRResourceVerbExperimentSetup(int id, String resourceVerbalization) {
        super(id);
        this.resourceVerbalization = resourceVerbalization;
    }

    public String getResourceVerbalization() {
        return resourceVerbalization;
    }

    public void addTriples(List<OWL2NL_QRTriple> triples) {
        this.triples.addAll(triples);
    }

    public LinkedList<OWL2NL_QRTriple> getTriples() {
        return (LinkedList<OWL2NL_QRTriple>)triples.clone();
    }
}
