package org.aksw.simba.owl2nl.qr.descriptions;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRResourceVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResult;
import org.aksw.simba.owl2nl.qr.db.consumer.OWL2NL_QRResourceVerbResultConsumer;
import org.aksw.simba.owl2nl.qr.db.supplier.OWL2NL_QRResourceVerbSetupSupplier;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRResourceVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.parser.OWL2NL_QRResourceVerbResultParser;
import org.aksw.simba.qr.datatypes.SimpleExperimentDescription;

public class OWL2NL_QRResourceVerbExperimentDescr extends SimpleExperimentDescription<OWL2NL_QRResourceVerbExperimentSetup, OWL2NL_QRExperimentResult> {
    public OWL2NL_QRResourceVerbExperimentDescr() {
        super("OWL2NL_QRResourceVerb", new OWL2NL_QRResourceVerbSetupSupplier(), new OWL2NL_QRResourceVerbGuiHelper(), new OWL2NL_QRResourceVerbResultParser(), new OWL2NL_QRResourceVerbResultConsumer());
    }
}
