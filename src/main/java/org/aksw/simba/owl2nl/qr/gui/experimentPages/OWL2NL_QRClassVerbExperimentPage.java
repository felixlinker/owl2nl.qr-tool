package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRClassVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRInstance;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRAxiomVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRClassVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRPageElements;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRTable;
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
        addInstructionsParagraph(container);

        return container;
    }

    public static void addInstructionsParagraph(HtmlContainer container) {
        container.addElement(new Paragraph("In this experiment, you will see an OWL axiom(in Manchester OWL syntax) and it's verbalization. Below there are five instances of the class described by the axiom. Four of them wrong and one correct."));
        container.addElement(new Paragraph("Please select the correct instance."));
        container.addElement(new Paragraph("The instances are described by triples. If you're not an expert, you'll only see some 'facts' about the instance."));
        container.addElement(new Paragraph("First you see some triples describing overall information that might be necessary to decide which instance is the correct one."));
        container.addElement(new Paragraph("For example, you might see an axiom like: 'Every pupil is a person that goes to school.' This axiom describes the class 'pupil'. Below there might be an instance 'Anna' with additional information, e.g. that 'Anna is a person.' and that 'Anna goes to school.'. In this case, 'Anna' would be the correct instance of the class 'pupil'."));
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
        if (experiment.isPerformedByExpert()) {
            Paragraph axiomParagraph = new Paragraph();
            axiomParagraph.addElement(new Text("Axiom: "));
            axiomParagraph.addElement(new BoldText("Axiom: " + experiment.getAxiom()));
            bodyDiv.addElement(axiomParagraph);
        }
        bodyDiv.addElement(new Paragraph("Verbalization: " + experiment.getVerbalization()));

        // Add overhead instances
        Div overheadDiv = new Div();
        overheadDiv.addElement(new Paragraph("Below you see some necessary information for the instances."));
        LinkedList<OWL2NL_QRTriple> overheadTriples = experiment.getOverheadTriples();
        OWL2NL_QRTable overheadTable = new OWL2NL_QRTable();
        if (experiment.isPerformedByExpert()) {
            overheadTable.addRowLight(new BoldText("Triple"), new BoldText("Verbalization"));
        }

        for (OWL2NL_QRTriple triple: overheadTriples) {
            if (experiment.isPerformedByExpert()) {
                overheadTable.addRow(triple.getTriple(), triple.getVerbalization());
            } else {
                overheadTable.addRow(triple.getVerbalization());
            }
        }
        overheadDiv.addElement(overheadTable.getTable());
        bodyDiv.addElement(overheadDiv);

        // Show all instances
        Div instancesDiv = new Div();
        instancesDiv.addElement(new Paragraph());
        instancesDiv.addElement(new Text("Below you see all instances. Please choose the instance that is of the class described by the axiom."));
        LinkedList<OWL2NL_QRInstance> instances = experiment.getInstances();
        Collections.shuffle(instances);
        for (OWL2NL_QRInstance instance: instances) {
            Div instanceDiv = new Div();
            instanceDiv.addElement(new Paragraph());
            instanceDiv.addElement(new Paragraph(new BoldText(instance.getName())));

            OWL2NL_QRTable tripleTable = new OWL2NL_QRTable();
            if (experiment.isPerformedByExpert()) {
                tripleTable.addRowLight(new BoldText("Triple"), new BoldText("Verbalization"));
            }

            for (OWL2NL_QRTriple triple: instance.getTriples()) {
                if (experiment.isPerformedByExpert()) {
                    tripleTable.addRow(triple.getTriple(), triple.getVerbalization());
                } else {
                    tripleTable.addRow(triple.getVerbalization());
                }
            }

            instanceDiv.addElement(tripleTable.getTable());
            instancesDiv.addElement(instanceDiv);
        }
        bodyDiv.addElement(instancesDiv);

        // Add user selection
        bodyDiv.addElement(OWL2NL_QRPageElements.generateRadioButtonList(guiHelper.INSTANCE_TO_RADIO_MAPPER.map(instances)));

        experimentDiv.addElement(bodyDiv);
        return experimentDiv;
    }

    @Override
    HtmlContainer getNextExperimentContainer() {
        HtmlContainer container = new HtmlContainer();
        container.addElement(new Paragraph("Thank you for your effort so far! You will now do a different experiment. Note that this experiment will only be shown when you are an expert."));
        OWL2NL_QRAxiomVerbExperimentPage.addInstructionsParagraph(container);
        return container;
    }

    @Override
    String getNextExperimentType() {
        return OWL2NL_QRAxiomVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE; // ToDo: check for non-experts
    }
}
