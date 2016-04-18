package org.aksw.simba.owl2nl.qr.data.experiments;

import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRInstance;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;

import java.util.LinkedList;
import java.util.List;

/**
 * Experiment setup for Class Verbalization experiment
 */
public class OWL2NL_QRClassVerbExperimentSetup extends OWL2NL_QRAxiomVerbExperimentSetup {
    /**
     * Instances of the verbalized axiom
     */
    private LinkedList<OWL2NL_QRTriple> overheadTriples = new LinkedList<>();
    private LinkedList<OWL2NL_QRInstance> instances = new LinkedList<>();

    public OWL2NL_QRClassVerbExperimentSetup(int id, String axiom, String verbalization) {
        super(id, axiom, verbalization);
    }

    public void addInstances(List<OWL2NL_QRInstance> instances) {
        this.instances.addAll(instances);
    }

    public LinkedList<OWL2NL_QRInstance> getInstances() {
        return (LinkedList<OWL2NL_QRInstance>)instances.clone();
    }

    public void addOverheadeTriples(List<OWL2NL_QRTriple> triples) {
        this.overheadTriples.addAll(triples);
    }

    public LinkedList<OWL2NL_QRTriple> getOverheadTriples() {
        return (LinkedList<OWL2NL_QRTriple>)this.overheadTriples.clone();
    }
}
