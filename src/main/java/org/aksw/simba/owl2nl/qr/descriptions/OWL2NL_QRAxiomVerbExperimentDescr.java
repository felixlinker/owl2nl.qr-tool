package org.aksw.simba.owl2nl.qr.descriptions;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRAxiomVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResult;
import org.aksw.simba.owl2nl.qr.db.consumer.OWL2NL_QRAxiomVerbResultConsumer;
import org.aksw.simba.owl2nl.qr.db.supplier.OWL2NL_QRAxiomVerbSetupSupplier;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRAxiomVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.parser.OWL2NL_QRAxiomVerbResultParser;
import org.aksw.simba.qr.datatypes.SimpleExperimentDescription;

public class OWL2NL_QRAxiomVerbExperimentDescr extends SimpleExperimentDescription<OWL2NL_QRAxiomVerbExperimentSetup, OWL2NL_QRExperimentResult> {
    public OWL2NL_QRAxiomVerbExperimentDescr() {
        super("OWL2NL_QRAxiomVerb", new OWL2NL_QRAxiomVerbSetupSupplier(), new OWL2NL_QRAxiomVerbGuiHelper(), new OWL2NL_QRAxiomVerbResultParser(), new OWL2NL_QRAxiomVerbResultConsumer());
    }
}
