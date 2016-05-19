package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRUserGroupGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRPageElements;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRRadioButtonHelper;
import org.aksw.simba.webelements.BoldText;
import org.aksw.simba.webelements.Div;
import org.aksw.simba.webelements.HtmlContainer;
import org.aksw.simba.webelements.Paragraph;

import java.util.LinkedList;

public class OWL2NL_QRLoginPage extends OWL2NL_QRPage {

    public OWL2NL_QRLoginPage() {
        super(new OWL2NL_QRUserGroupGuiHelper());
    }

    @Override
    HtmlContainer createContent() {
        HtmlContainer loginContainer = new HtmlContainer();

        loginContainer.addElement(OWL2NL_QRPageElements.createHeadDiv("Instructions"));
        loginContainer.addElement(new Paragraph("Before you start with the evaluation please tell us how familiar you are with RDF and OWL concepts."));
        loginContainer.addElement(new Paragraph("If you're not familiar with OWL and RDF concepts, please select '" + OWL2NL_QRUserGroupGuiHelper.AMATEUR_RADIO_NAME + "' as user group. If you are familiar with those concepts, you can select '" + OWL2NL_QRUserGroupGuiHelper.EXPERT_RADIO_NAME + "' as user group."));
        loginContainer.addElement(new Paragraph("The experiments will take about 15 minutes. Users of user group '" + OWL2NL_QRUserGroupGuiHelper.AMATEUR_RADIO_NAME + "' will need less time."));
        loginContainer.addElement(new Paragraph(OWL2NL_QRExperimentPage.getWinText()));

        loginContainer.addElement(createUserGroupSelection());
        this.addHiddenValue(OWL2NL_QRGuiHelper.EXPERIMENT_IDENTIFIER_KEY, guiHelper.getExperimentIdentifierValue());

        loginContainer.addElement(OWL2NL_QRPageElements.createSubmitButton());

        return loginContainer;
    }

    protected HtmlContainer createUserGroupSelection() {
        HtmlContainer container = new HtmlContainer();

        Div messageDiv = new Div();
        messageDiv.addElement(new Paragraph(new BoldText("Please select a user group:")));

        LinkedList<OWL2NL_QRRadioButtonHelper> buttonHelpers = new LinkedList<>();
        buttonHelpers.add(new OWL2NL_QRRadioButtonHelper(OWL2NL_QRUserGroupGuiHelper.EXPERT_RADIO_NAME, OWL2NL_QRUserGroupGuiHelper.RADIO_KEY, OWL2NL_QRUserGroupGuiHelper.EXPERT_RADIO_VALUE));
        buttonHelpers.add(new OWL2NL_QRRadioButtonHelper(OWL2NL_QRUserGroupGuiHelper.AMATEUR_RADIO_NAME, OWL2NL_QRUserGroupGuiHelper.RADIO_KEY, OWL2NL_QRUserGroupGuiHelper.AMATEUR_RADIO_NAME));

        messageDiv.addElement(OWL2NL_QRPageElements.generateRadioButtonList(buttonHelpers));

        container.addElement(messageDiv);
        return container;
    }
}
