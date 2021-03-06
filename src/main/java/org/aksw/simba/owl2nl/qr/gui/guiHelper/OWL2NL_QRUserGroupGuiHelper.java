package org.aksw.simba.owl2nl.qr.gui.guiHelper;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRRadioButtonHelper;
import org.aksw.simba.qr.gui.Page;

import java.util.LinkedList;
import java.util.List;

public class OWL2NL_QRUserGroupGuiHelper extends OWL2NL_QRGuiHelper<OWL2NL_QRExperimentSetup> {

    public OWL2NL_QRUserGroupGuiHelper() {
        super(OWL2NL_QRExperimentSetup.class);
    }

    public static final String RADIO_KEY = "usergroup";
    public static final String EXPERT_RADIO_NAME = "Expert";
    public static final String EXPERT_RADIO_VALUE = "true";
    public static final String AMATEUR_RADIO_NAME = "Non-expert";
    public static final String AMATEUR_RADIO_VALUE = "false";

    public List<OWL2NL_QRRadioButtonHelper> getRadioButtons() { // ToDo: is this needed?
        LinkedList<OWL2NL_QRRadioButtonHelper> list = new LinkedList<>();
        list.add(new OWL2NL_QRRadioButtonHelper(EXPERT_RADIO_VALUE, RADIO_KEY, EXPERT_RADIO_VALUE));
        list.add(new OWL2NL_QRRadioButtonHelper(AMATEUR_RADIO_VALUE, RADIO_KEY, AMATEUR_RADIO_VALUE));

        return list;
    }

    @Override
    public Page getExperimentPageFinal(OWL2NL_QRExperimentSetup experimentSetup) {
        return null;
    }

    @Override
    public String getExperimentIdentifierValue() {
        return "";
    }
}
