package org.aksw.simba.owl2nl.qr.descriptions;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRClassVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResult;
import org.aksw.simba.owl2nl.qr.db.consumer.OWL2NL_QRAxiomVerbResultConsumer;
import org.aksw.simba.owl2nl.qr.db.supplier.OWL2NL_QRAxiomVerbSetupSupplier;
import org.aksw.simba.owl2nl.qr.db.supplier.OWL2NL_QRClassVerbSetupSupplier;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRAxiomVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRClassVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.parser.OWL2NL_QRAxiomVerbResultParser;
import org.aksw.simba.qr.datatypes.SimpleExperimentDescription;

/**
 * Created by felix on 11.04.2016.
 */
public class OWL2NL_QRClassVerbExperimentDescr extends SimpleExperimentDescription<OWL2NL_QRClassVerbExperimentSetup, OWL2NL_QRExperimentResult> {
    public OWL2NL_QRClassVerbExperimentDescr() {
        super("OWL2NL_QRClassVerb", new OWL2NL_QRClassVerbSetupSupplier(), new OWL2NL_QRClassVerbGuiHelper(), new OWL2NL_QRAxiomVerbResultParser(), new OWL2NL_QRAxiomVerbResultConsumer());
    }
}
