package org.aksw.simba.owl2nl.qr.gui.guiHelper;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRUserGroupExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.experimentPages.OWL2NL_QRUserGroupExperimentPage;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRRadioButtonHelper;
import org.aksw.simba.qr.gui.Page;

import java.util.LinkedList;
import java.util.List;

public class OWL2NL_QRUserGroupGuiHelper extends OWL2NL_QRGuiHelper<OWL2NL_QRUserGroupExperimentSetup> {

    public static final String RADIO_KEY = "radiokey";
    public static final String EXPERT_RADIO_VALUE = "expert";
    public static final String AMATEUR_RADIO_VALUE = "amateur";

    public List<OWL2NL_QRRadioButtonHelper> getRadioButtons() {
        LinkedList<OWL2NL_QRRadioButtonHelper> list = new LinkedList<>();
        list.add(new OWL2NL_QRRadioButtonHelper(EXPERT_RADIO_VALUE, RADIO_KEY, EXPERT_RADIO_VALUE));
        list.add(new OWL2NL_QRRadioButtonHelper(AMATEUR_RADIO_VALUE, RADIO_KEY, AMATEUR_RADIO_VALUE));

        return list;
    }

    @Override
    public Page getExperimentPage(OWL2NL_QRUserGroupExperimentSetup experimentSetup) {
        return new OWL2NL_QRUserGroupExperimentPage(this, experimentSetup);
    }

    @Override
    public Page getFinishPage() {
        return new OWL2NL_QRUserGroupExperimentPage(this, null);
    }
}
