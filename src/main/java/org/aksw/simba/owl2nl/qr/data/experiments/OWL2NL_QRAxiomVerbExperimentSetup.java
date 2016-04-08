package org.aksw.simba.owl2nl.qr.data.experiments;

/**
 * Experiment setup for Axiom Verbalization experiment
 */
public class OWL2NL_QRAxiomVerbExperimentSetup extends OWL2NL_QRExperimentSetup {
    /**
     * Verbalized axiom
     */
    private String axiom;

    /**
     * Verbalization of the axiom
     */
    private String verbalization;

    public OWL2NL_QRAxiomVerbExperimentSetup(int id, String axiom, String verbalization) {
        super(id);
        this.axiom = axiom;
        this.verbalization = verbalization;
    }

    public String getAxiom() {
        return axiom;
    }

    public String getVerbalization() {
        return verbalization;
    }
}