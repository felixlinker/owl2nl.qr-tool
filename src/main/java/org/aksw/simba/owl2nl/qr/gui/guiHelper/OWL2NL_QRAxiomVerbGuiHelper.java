package org.aksw.simba.owl2nl.qr.gui.guiHelper;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRAxiomVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.experimentPages.OWL2NL_QRAxiomVerbExperimentPage;
import org.aksw.simba.owl2nl.qr.gui.experimentPages.OWL2NL_QRExperimentSelectionPage;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRStarRatingHelper;
import org.aksw.simba.qr.gui.Page;

public class OWL2NL_QRAxiomVerbGuiHelper extends OWL2NL_QRGuiHelper<OWL2NL_QRAxiomVerbExperimentSetup> {
    public static final String ADEQUACY_RATING_NAME = "adequacy:";
    public static final String ADEQUACY_RATING_KEY = "adequacy";
    public static final String FLUENCY_RATING_NAME = "fluency:";
    public static final String FLUENCY_RATING_KEY = "fluency";
    public static final String EXPERIMENT_IDENTIFIER_VALUE = "OWL2NL_QRAxiomVerb";
    public static final String EXPERIMENT_IDENTIFIER_NAME = "Axiom verbalization (for experienced users only)";

    public static final OWL2NL_QRStarRatingHelper[] STAR_RATINGS = { new OWL2NL_QRStarRatingHelper(ADEQUACY_RATING_KEY, ADEQUACY_RATING_NAME), new OWL2NL_QRStarRatingHelper(FLUENCY_RATING_KEY, FLUENCY_RATING_NAME)};

    public OWL2NL_QRAxiomVerbGuiHelper() {
        super(OWL2NL_QRAxiomVerbExperimentSetup.class);
    }

    @Override
    public Page getExperimentPageFinal(OWL2NL_QRAxiomVerbExperimentSetup experimentSetup) {
        return new OWL2NL_QRAxiomVerbExperimentPage(this, experimentSetup);
    }

    @Override
    public Page getFinishPage() {
//        return new OWL2NL_QRAxiomVerbExperimentPage(this, null);
        return new OWL2NL_QRExperimentSelectionPage(true);
    }

    @Override
    public String getExperimentIdentifierValue() {
        return EXPERIMENT_IDENTIFIER_VALUE;
    }
}
