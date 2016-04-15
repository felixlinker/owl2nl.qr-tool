package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.ListConverter;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRClassVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRClassVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRRadioButtonHelper;
import org.aksw.simba.webelements.Div;
import org.aksw.simba.webelements.HtmlContainer;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRStarRatingHelper;
import org.aksw.simba.webelements.*;
import org.aksw.simba.webelements.Heading.HeadingOrder;

import java.util.Collections;
import java.util.LinkedList;

public class OWL2NL_QRClassVerbExperimentPage extends OWL2NL_QRExperimentPage<OWL2NL_QRClassVerbExperimentSetup> {

    private OWL2NL_QRClassVerbGuiHelper guiHelper;

    public OWL2NL_QRClassVerbExperimentPage(OWL2NL_QRClassVerbGuiHelper guiHelper, OWL2NL_QRClassVerbExperimentSetup experiment) {
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

        container.addElement(new Paragraph("In this experiment, you will see an axiom and it's verbalization. Below there are five instances of the axiom. Four of them wrong and one correct."));
        container.addElement(new Paragraph("Please select the correct instance."));

        return container;
    }

    @Override
    Div generateExperimentDiv(OWL2NL_QRClassVerbExperimentSetup experiment) {
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
        bodyDiv.addElement(new Paragraph(experiment.getAxiom()));
        bodyDiv.addElement(new Paragraph("Instances:"));

        LinkedList<OWL2NL_QRTriple> instances = experiment.getInstances();
        Collections.shuffle(instances);
        bodyDiv.addElement(generateRadioButtonList(guiHelper.TRIPLE_TO_RADIO_MAPPER.map(instances)));

        // ToDo: enable options for showing verbalizations

        experimentDiv.addElement(bodyDiv);
        return experimentDiv;
    }
}
