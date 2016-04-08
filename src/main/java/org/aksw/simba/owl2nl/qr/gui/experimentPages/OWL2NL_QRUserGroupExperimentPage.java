package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRUserGroupExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRUserGroupGuiHelper;
import org.aksw.simba.webelements.Div;
import org.aksw.simba.webelements.HtmlContainer;
import org.aksw.simba.webelements.*;

/**
 * Created by felix on 07.04.2016.
 */
public class OWL2NL_QRUserGroupExperimentPage extends OWL2NL_QRExperimentPage<OWL2NL_QRUserGroupExperimentSetup> {

    private OWL2NL_QRUserGroupGuiHelper guiHelper;

    public OWL2NL_QRUserGroupExperimentPage(OWL2NL_QRUserGroupGuiHelper guiHelper, OWL2NL_QRUserGroupExperimentSetup experimentSetup) {
        super(guiHelper, experimentSetup);
        this.guiHelper = guiHelper;
    }

    @Override
    HtmlContainer getInstructions() {
        HtmlContainer container = new HtmlContainer();

        Div headerDiv = new Div();
        headerDiv.addAttribute("class", "page-header");
        headerDiv.addElement(new Heading(new Text("Instructions"), Heading.HeadingOrder.H1));
        container.addElement(headerDiv);

        // ToDo: check language
        container.addElement(new Paragraph("In order to start the experiments please choose your user group."));
        container.addElement(new Paragraph("If you're familiar with semantic web language you can choose 'expert'."));
        container.addElement(new Paragraph("If you're not familiar with semantic web language please choose 'amateur'."));

        StringBuilder builder = new StringBuilder();
        builder.append("For getting a chance to win one of the amazon vouchers, you will have to submit the answers of ");
        builder.append(NUMBER_OF_ANSWERS_NEEDED_FOR_KEY_WORD);
        builder.append(" different pages (during a single session). After the submission of the tenth page, a solution word is displayed, that you can send to roeder@informatik.uni-leipzig.de if you want to take part in the lottery.");
        container.addElement(new Paragraph(builder.toString()));

        return container;
    }

    @Override
    Div generateExperimentDiv(OWL2NL_QRUserGroupExperimentSetup experimentSetup) {
        Div experimentDiv = new Div();
        experimentDiv.addAttribute("class", "panel panel-default experiment_div");

        Div titleDiv = new Div();
        titleDiv.addAttribute("class", "panel-heading");
        Heading heading = new Heading(new Text(""), Heading.HeadingOrder.H3);
        heading.addAttribute("class", "panel-title");
        titleDiv.addElement(heading);
        experimentDiv.addElement(titleDiv);

        experimentDiv.addElement(generateRadioButtonList(guiHelper.getRadioButtons()));

        Div bodyDiv = new Div();
        bodyDiv.addAttribute("class", "panel-body");

        experimentDiv.addElement(bodyDiv);
        return experimentDiv;
    }
}
