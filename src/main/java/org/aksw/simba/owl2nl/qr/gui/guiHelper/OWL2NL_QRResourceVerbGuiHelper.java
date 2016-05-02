package org.aksw.simba.owl2nl.qr.gui.guiHelper;

import org.aksw.simba.owl2nl.qr.data.ListConverter;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRResourceVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;
import org.aksw.simba.owl2nl.qr.gui.experimentPages.OWL2NL_QRExperimentSelectionPage;
import org.aksw.simba.owl2nl.qr.gui.experimentPages.OWL2NL_QRResourceVerbExperimentPage;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRStarRatingHelper;
import org.aksw.simba.qr.gui.Page;

public class OWL2NL_QRResourceVerbGuiHelper extends OWL2NL_QRGuiHelper<OWL2NL_QRResourceVerbExperimentSetup> {

    public static final String ADEQUACY_RATING_NAME = "Adequacy:";
    public static final String ADEQUACY_RATING_KEY = "adequacy";
    public static final String FLUENCY_RATING_NAME = "Fluency:";
    public static final String FLUENCY_RATING_KEY = "fluency";
    public static final String COMPLETENESS_RATING_NAME = "Completeness:";
    public static final String COMPLETENSS_RATING_KEY = "completeness";
    public static final String EXPERIMENT_IDENTIFIER_VALUE = "OWL2NL_QRResourceVerb";
    public static final String EXPERIMENT_IDENTIFIER_NAME = "Resource verbalization";

    public static final ListConverter<OWL2NL_QRTriple, String> TRIPLE_STRING_LIST_CONVERTER = a -> a.getTriple();
    public static final OWL2NL_QRStarRatingHelper[] STAR_RATINGS_EXPERT = { new OWL2NL_QRStarRatingHelper(ADEQUACY_RATING_KEY, ADEQUACY_RATING_NAME), new OWL2NL_QRStarRatingHelper(FLUENCY_RATING_KEY, FLUENCY_RATING_NAME), new OWL2NL_QRStarRatingHelper(COMPLETENSS_RATING_KEY, COMPLETENESS_RATING_NAME)};
    public static final OWL2NL_QRStarRatingHelper[] STAR_RATINGS_AMATEUR = { new OWL2NL_QRStarRatingHelper(FLUENCY_RATING_KEY, FLUENCY_RATING_NAME)};

    public OWL2NL_QRResourceVerbGuiHelper() {
        super(OWL2NL_QRResourceVerbExperimentSetup.class);
    }

    @Override
    public Page getExperimentPageFinal(OWL2NL_QRResourceVerbExperimentSetup experimentSetup) {
        return new OWL2NL_QRResourceVerbExperimentPage(this, experimentSetup);
    }

    @Override
    public Page getFinishPage() {
//        return new OWL2NL_QRResourceVerbExperimentPage(this, null);
        return new OWL2NL_QRExperimentSelectionPage(true);
    }

    @Override
    public String getExperimentIdentifierValue() {
        return EXPERIMENT_IDENTIFIER_VALUE;
    }
}
