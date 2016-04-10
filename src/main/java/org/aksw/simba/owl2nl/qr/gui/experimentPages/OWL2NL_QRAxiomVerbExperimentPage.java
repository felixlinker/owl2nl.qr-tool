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

        // ToDo: check language
        container.addElement(new Paragraph("Below you can see an axiom from an owl ontology and its verbalization."));
        container.addElement(new Paragraph("Please rate the verbalization according to how adequately it is describing the axiom and how fluent the language is."));

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

        Div bodyDiv = new Div();
        bodyDiv.addAttribute("class", "panel-body");
        Paragraph axiomParagraph = new Paragraph();
        axiomParagraph.addElement(new Text("Axiom: "));
        axiomParagraph.addElement(new BoldText(experiment.getAxiom()));
        bodyDiv.addElement(axiomParagraph);

        bodyDiv.addElement(new Paragraph("Verbalization: "+experiment.getVerbalization()));

        bodyDiv.addElement(generateStarRatingTable(guiHelper.STAR_RATINGS));

        experimentDiv.addElement(bodyDiv);
        return experimentDiv;
    }
}
