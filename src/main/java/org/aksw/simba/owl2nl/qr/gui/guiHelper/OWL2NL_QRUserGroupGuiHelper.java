package org.aksw.simba.owl2nl.qr.gui.guiHelper;

import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRRadioButtonHelper;

import java.util.LinkedList;
import java.util.List;

public class OWL2NL_QRUserGroupGuiHelper {

    public static final String RADIO_KEY = "usergroup";
    public static final String EXPERT_RADIO_NAME = "Expert";
    public static final String EXPERT_RADIO_VALUE = "true";
    public static final String AMATEUR_RADIO_NAME = "Amateur";
    public static final String AMATEUR_RADIO_VALUE = "false";

    public List<OWL2NL_QRRadioButtonHelper> getRadioButtons() {
        LinkedList<OWL2NL_QRRadioButtonHelper> list = new LinkedList<>();
        list.add(new OWL2NL_QRRadioButtonHelper(EXPERT_RADIO_VALUE, RADIO_KEY, EXPERT_RADIO_VALUE));
        list.add(new OWL2NL_QRRadioButtonHelper(AMATEUR_RADIO_VALUE, RADIO_KEY, AMATEUR_RADIO_VALUE));

        return list;
    }
}
