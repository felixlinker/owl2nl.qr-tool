package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRResourceVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRResourceVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRStarRatingHelper;
import org.aksw.simba.webelements.Div;
import org.aksw.simba.webelements.HtmlContainer;
import org.aksw.simba.webelements.Paragraph;
import org.aksw.simba.webelements.*;
import org.aksw.simba.webelements.Heading.HeadingOrder;

public class OWL2NL_QRResourceVerbExperimentPage extends OWL2NL_QRExperimentPage<OWL2NL_QRResourceVerbExperimentSetup> {

    private OWL2NL_QRResourceVerbGuiHelper guiHelper;

    public OWL2NL_QRResourceVerbExperimentPage(OWL2NL_QRResourceVerbGuiHelper guiHelper, OWL2NL_QRResourceVerbExperimentSetup experiment) {
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

        container.addElement(new Paragraph("In this experiment you have to rate the verbalization of a resource. If you're not an expert, you'll just be able to rate the fluency of the language."));
        container.addElement(new Paragraph("If however you are an expert you'll see the triples creating the resource. Please rate how complete and adequate the resources verbalization is, too."));

        return container;
    }

    @Override
    Div generateExperimentDiv(OWL2NL_QRResourceVerbExperimentSetup experiment) {
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
        bodyDiv.addElement(new Paragraph(experiment.getResourceVerbalization()));
        OWL2NL_QRStarRatingHelper[] starRatingHelpers;
        if (experiment.isPerformedByExpert()) {
            bodyDiv.addElement(generateUnorderedList(guiHelper.TRIPLE_STRING_LIST_CONVERTER.map(experiment.getTriples())));
            starRatingHelpers = guiHelper.STAR_RATINGS_EXPERT;
        } else {
            starRatingHelpers = guiHelper.STAR_RATINGS_AMATEUR;
        }
        bodyDiv.addElement(generateStarRatingTable(starRatingHelpers));

        experimentDiv.addElement(bodyDiv);
        return experimentDiv;
    }
}
