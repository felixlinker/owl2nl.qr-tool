package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRAxiomVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRAxiomVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.webelements.Div;
import org.aksw.simba.webelements.HtmlContainer;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRStarRatingHelper;
import org.aksw.simba.webelements.*;
import org.aksw.simba.webelements.Heading.HeadingOrder;

public class OWL2NL_QRAxiomVerbExperimentPage extends OWL2NL_QRExperimentPage<OWL2NL_QRAxiomVerbExperimentSetup> {

    private OWL2NL_QRAxiomVerbGuiHelper guiHelper;

    public OWL2NL_QRAxiomVerbExperimentPage(OWL2NL_QRAxiomVerbGuiHelper guiHelper, OWL2NL_QRAxiomVerbExperimentSetup experiment) {
        super(guiHelper,experiment);
        this.guiHelper = guiHelper;
    }

    @Override
    HtmlContainer getInstructions() {
        HtmlContainer container = new HtmlContainer();

        Div headerDiv = new Div();
        headerDiv.addAttribute("class", "page-header");
        headerDiv.addElement(new Heading(new Text("Instructions"), HeadingOrder.H1));
        container.addElement(headerDiv);

        // ToDo: add explanation
        container.addElement(new Paragraph("TODO"));

        StringBuilder builder = new StringBuilder();
        builder.append("For getting a chance to win one of the amazon vouchers, you will have to submit the answers of ");
        builder.append(NUMBER_OF_ANSWERS_NEEDED_FOR_KEY_WORD);
        builder.append(" different pages (during a single session). After the submission of the tenth page, a solution word is displayed, that you can send to roeder@informatik.uni-leipzig.de if you want to take part in the lottery.");
        container.addElement(new Paragraph(builder.toString()));

        return container;
    }

    @Override
    Div generateExperimentDiv(OWL2NL_QRAxiomVerbExperimentSetup experiment) {
        Div experimentDiv = new Div();
        experimentDiv.addAttribute("class", "panel panel-default experiment_div");

        Div titleDiv = new Div();
        titleDiv.addAttribute("class", "panel-heading");
        Heading heading = new Heading(new Text(""), HeadingOrder.H3);
        heading.addAttribute("class", "panel-title");
        titleDiv.addElement(heading);
        experimentDiv.addElement(titleDiv);

        experimentDiv.addElement(new Paragraph(bold(experiment.getAxiom())));
        experimentDiv.addElement(new Paragraph(experiment.getVerbalization()));

        experimentDiv.addElement(generateStarRatingTable(guiHelper.STAR_RATINGS));

        Div bodyDiv = new Div();
        bodyDiv.addAttribute("class", "panel-body");

        experimentDiv.addElement(bodyDiv);
        return experimentDiv;
    }
}
