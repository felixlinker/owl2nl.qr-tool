package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRResourceVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRClassVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRResourceVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRPageElements;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRStarRatingHelper;
import org.aksw.simba.webelements.*;
import org.aksw.simba.webelements.Heading.HeadingOrder;

import java.util.Collection;

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
        addInstructionsParagraph(container);

        return container;
    }

    public static void addInstructionsParagraph(HtmlContainer container) {
        container.addElement(new Paragraph("In this experiment you have to rate the verbalization of a resource. If you're not an expert, you'll just be able to rate the fluency of the language."));
        container.addElement(new Paragraph("If however you are an expert you'll see the triples creating the resource. Please rate how complete and adequate the resources verbalization is, too."));
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
        bodyDiv.addElement(new Paragraph(new BoldText(experiment.getResourceVerbalization())));
        OWL2NL_QRStarRatingHelper[] starRatingHelpers;
        if (experiment.isPerformedByExpert()) {
            bodyDiv.addElement(generateTriplesParagraph(experiment.getTriples()));
            starRatingHelpers = guiHelper.STAR_RATINGS_EXPERT;
        } else {
            starRatingHelpers = guiHelper.STAR_RATINGS_AMATEUR;
        }
        bodyDiv.addElement(OWL2NL_QRPageElements.generateStarRatingTable(starRatingHelpers));

        experimentDiv.addElement(bodyDiv);
        return experimentDiv;
    }

    private WebElement generateTriplesParagraph(Collection<OWL2NL_QRTriple> triples) {
        HtmlContainer container = new HtmlContainer();

        Div explanationDiv = new Div();
        explanationDiv.addElement(new Paragraph("The table below shows all triples (incl. their verbalization) that were used to generate the summary shown above."));
        container.addElement(explanationDiv);

        Table tripleTable = new Table();
        for (OWL2NL_QRTriple triple: triples) {
            TableRow tableRow = new TableRow();
            tableRow.addCell(new Paragraph(triple.getTriple()));
            tableRow.addCell(new Paragraph(triple.getVerbalization()));
            tripleTable.addRow(tableRow);
        }
        container.addElement(tripleTable);

        return container;
    }

    @Override
    HtmlContainer getNextExperimentContainer() {
        HtmlContainer container = new HtmlContainer();
        container.addElement(new Paragraph("Thank you for your effort so far! You will now do a different experiment."));
        OWL2NL_QRClassVerbExperimentPage.addInstructionsParagraph(container);
        return container;
    }

    @Override
    String getNextExperimentType() {
        return OWL2NL_QRClassVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE;
    }
}
