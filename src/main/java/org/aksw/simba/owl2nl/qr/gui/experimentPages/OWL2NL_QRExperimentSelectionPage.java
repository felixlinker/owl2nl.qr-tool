package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.webelements.Div;
import org.aksw.simba.webelements.HtmlContainer;

/**
 * Created by felix on 20.04.2016.
 */
public class OWL2NL_QRExperimentSelectionPage extends OWL2NL_QRExperimentPage<OWL2NL_QRExperimentSetup> {

    public OWL2NL_QRExperimentSelectionPage() {
        super(null, null);
    }

    @Override
    protected HtmlContainer createContent() {
        return super.createContent();
    }

    @Override
    Div generateExperimentDiv(OWL2NL_QRExperimentSetup experimentSetup) {
        return null;
    }

    @Override
    HtmlContainer getInstructions() {
        return null;
    }
}
