package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRResourceVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRClassVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRResourceVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRPageElements;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRStarRatingHelper;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRTable;
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
        addInstructionsParagraphs(container);

        return container;
    }

    @Override
    public void addInstructionsParagraphs(HtmlContainer container) {
        addInstructionsParagraphsStatic(container);
    }

    public void addInstructionsParagraphsStatic(HtmlContainer container) {
        container.addElement(new Paragraph("In this experiment, you have to rate the verbalization of a resource. If you're not an expert, please rate how fluent the language is."));
        Paragraph p = new Paragraph(new BoldText("Fluency"));
        p.addElement(new Text(" means the readability and understandability of the language of the verbalization. Does the verbalization sound odd? Is it's phrasing difficult or easy to understand?"));
        container.addElement(p);
        container.addElement(new Paragraph("If however you are an expert you'll see the triples creating the resource. Please rate on completeness and adequacy, too."));
        p = new Paragraph(new BoldText("Completeness"));
        p.addElement(new Text(" means whether all triples making up the resource have been verbalized."));
        container.addElement(p);
        p = new Paragraph(new BoldText("Adequacy"));
        p.addElement(new BoldText(" means the 'correctness' of the verbalization. Is the verbalization consistent with the triples, i.e. are all covered triples verbalized correctly? Is the resource verbalization consistent with the resource itself?"));
        container.addElement(p);
        container.addElement(new Paragraph());
        container.addElement(new Paragraph("For example have a look at this verbalisation: 'Volkswagen is a car manufacturers of germany. Volkswagen's internationally is and its industry is Automotive industry.'"));
        container.addElement(new Paragraph("It was created using following triples:"));
        container.addElement(new Paragraph("dbr:Volkswagen @dbo:internationally \"1\"^^http://www.w3.org/2001/XMLSchema#boolean"));
        container.addElement(new Paragraph("dbr:Volkswagen @dbo:industry dbr:Automotive_industry"));
        container.addElement(new Paragraph("dbr:Volkswagen @dbo:product dbr:Car"));
        container.addElement(new Paragraph("dbr:Volkswagen @rdf:type http://dbpedia.org/class/yago/CarManufacturersOfGermany"));
        container.addElement(new Paragraph());
        container.addElement(new Paragraph("This verbalization's fluency rating would be low as the second sentence sounds odd. Also, it's completeness rating would be low since one triple is missing. The adequacy rating would be low as well since the verbalization of the first triple doesn't really reflect it's meaning."));
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

        OWL2NL_QRTable tripleTable = new OWL2NL_QRTable();
        for (OWL2NL_QRTriple triple: triples) {
            tripleTable.addRow(triple.getTriple(), triple.getVerbalization());
        }
        container.addElement(tripleTable.getTable());

        return container;
    }

    @Override
    HtmlContainer getNextExperimentContainer() {
        HtmlContainer container = new HtmlContainer();
        container.addElement(new Paragraph("Thank you for your effort so far! You will now do a different experiment."));
        OWL2NL_QRClassVerbExperimentPage.addInstructionsParagraphsStatic(container);
        return container;
    }

    @Override
    String getNextExperimentType() {
        return OWL2NL_QRClassVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE;
    }
}
