package org.aksw.simba.owl2nl.qr.gui.guiHelper;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.experimentPages.OWL2NL_QRExperimentSelectionPage;
import org.aksw.simba.qr.gui.Page;

/**
 * Created by felix on 20.04.2016.
 */
public class OWL2NL_QRExperimentSelectionGuiHelper extends OWL2NL_QRGuiHelper<OWL2NL_QRExperimentSetup> {

    public OWL2NL_QRExperimentSelectionGuiHelper() {
        super(OWL2NL_QRExperimentSetup.class);
    }

    @Override
    public Page getExperimentPageFinal(OWL2NL_QRExperimentSetup experimentSetup) {
        return null;
    }

    @Override
    public String getExperimentIdentifierValue() {
        return "";
    }

    @Override
    public Page getLoginPage() {
        return new OWL2NL_QRExperimentSelectionPage(false);
    }
}
