package org.aksw.simba.owl2nl.qr.gui.guiHelper;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRAxiomVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.experimentPages.OWL2NL_QRAxiomVerbExperimentPage;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRStarRatingHelper;
import org.aksw.simba.qr.gui.GuiHelper;
import org.aksw.simba.qr.gui.Page;

import java.util.LinkedList;
import java.util.List;

public class OWL2NL_QRAxiomVerbGuiHelper extends OWL2NL_QRGuiHelper<OWL2NL_QRAxiomVerbExperimentSetup> {
    public static final String ADEQUACY_RATING_NAME = "adequacy:";
    public static final String ADEQUACY_RATING_KEY = "adequacy";
    public static final String FLUENCY_RATING_NAME = "fluency:";
    public static final String FLUENCY_RATING_KEY = "fluency";
    public static final String EXPERIMENT_IDENTIFIER_KEY = "OWL2NL_QRAxiomVerb";

    public static final OWL2NL_QRStarRatingHelper[] STAR_RATINGS = { new OWL2NL_QRStarRatingHelper(ADEQUACY_RATING_KEY, ADEQUACY_RATING_NAME), new OWL2NL_QRStarRatingHelper(FLUENCY_RATING_KEY, FLUENCY_RATING_NAME)};

    @Override
    public Page getExperimentPage(OWL2NL_QRAxiomVerbExperimentSetup experimentSetup) {
        return new OWL2NL_QRAxiomVerbExperimentPage(this, experimentSetup);
    }

    @Override
    public Page getFinishPage() {
        return new OWL2NL_QRAxiomVerbExperimentPage(this, null);
    }

    @Override
    public String getStringValue(StringValue key) {
        switch (key) {
            case EXPERIMENT_IDENTIFIER_KEY: {
                return EXPERIMENT_IDENTIFIER_KEY;
            }
            default: {
                return super.getStringValue(key);
            }
        }
    }
}
