package org.aksw.simba.owl2nl.qr.gui.guiHelper;

import org.aksw.simba.owl2nl.qr.data.ListConverter;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRClassVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;
import org.aksw.simba.owl2nl.qr.gui.experimentPages.OWL2NL_QRClassVerbExperimentPage;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRRadioButtonHelper;
import org.aksw.simba.qr.gui.Page;

public class OWL2NL_QRClassVerbGuiHelper extends OWL2NL_QRGuiHelper<OWL2NL_QRClassVerbExperimentSetup> {

    public static final String CHOSEN_TRIPLE_KEY = "chosenTriple";
    public static final String EXPERIMENT_IDENTIFIER_VALUE = "OWL2NL_QRClassVerb";
    public static final String EXPERIMENT_IDENTIFIER_NAME = "Class verbalization";

    public static final ListConverter<OWL2NL_QRTriple, OWL2NL_QRRadioButtonHelper> TRIPLE_TO_RADIO_MAPPER = triple ->  new OWL2NL_QRRadioButtonHelper(triple.getTriple(), CHOSEN_TRIPLE_KEY, String.valueOf(triple.getId()));

    @Override
    public Page getExperimentPage(OWL2NL_QRClassVerbExperimentSetup experimentSetup) {
        return new OWL2NL_QRClassVerbExperimentPage(this, experimentSetup);
    }

    @Override
    public Page getFinishPage() {
        return new OWL2NL_QRClassVerbExperimentPage(this, null);
    }

    @Override
    public String getExperimentIdentifierValue() {
        return EXPERIMENT_IDENTIFIER_VALUE;
    }
}
