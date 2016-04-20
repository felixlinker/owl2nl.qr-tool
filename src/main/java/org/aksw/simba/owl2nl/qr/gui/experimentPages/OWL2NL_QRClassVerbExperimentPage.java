package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRClassVerbExperimentSetup;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRInstance;
import org.aksw.simba.owl2nl.qr.data.ontoelements.OWL2NL_QRTriple;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRClassVerbGuiHelper;
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

        container.addElement(new Paragraph("In this experiment, you will see an axiom and/or it's verbalization. Below there are five instances of the class described by the axiom. Four of them wrong and one correct."));
        container.addElement(new Paragraph("Please select the correct instance."));
        container.addElement(new Paragraph("The instances are described by triples. If you're not an expert you'll only see some \"facts\" about the instance."));
        container.addElement(new Paragraph("Below the instance selection you see some triples describing overall information that might be necessary in order to decide which instance is the correct one."));

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
        if (experiment.isPerformedByExpert()) {
            bodyDiv.addElement(new Paragraph(experiment.getAxiom()));
        }
        bodyDiv.addElement(new Paragraph(experiment.getVerbalization()));

        // Show all instances
        Div instancesDiv = new Div();
        instancesDiv.addElement(new BoldText("Instances"));
        LinkedList<OWL2NL_QRInstance> instances = experiment.getInstances();
        Collections.shuffle(instances);
        for (OWL2NL_QRInstance instance: instances) {
            Div instanceDiv = new Div();
            instanceDiv.addElement(new Paragraph(new BoldText(instance.getName())));

            Table tripleTable = new Table();
            if (experiment.isPerformedByExpert()) {
                TableRow headCells = new TableRow();
                headCells.addCell(new BoldText("Triple"));
                headCells.addCell(new BoldText("Verbalization"));
                tripleTable.addRow(headCells);
            }

            for (OWL2NL_QRTriple triple: instance.getTriples()) {
                TableRow cells = new TableRow();
                if (experiment.isPerformedByExpert()) {
                    cells.addCell(new Paragraph(triple.getTriple()));
                }
                cells.addCell(new Paragraph(triple.getVerbalization()));

                tripleTable.addRow(cells);
            }
            instanceDiv.addElement(tripleTable);
            instancesDiv.addElement(instanceDiv);
        }
        bodyDiv.addElement(instancesDiv);

        // Add user selection
        bodyDiv.addElement(generateRadioButtonList(guiHelper.INSTANCE_TO_RADIO_MAPPER.map(instances)));

        // Add overhead instances
        Div overheadDiv = new Div();
        overheadDiv.addElement(new Paragraph("Below you see some necessary information for above instances."));
        LinkedList<OWL2NL_QRTriple> overheadTriples = experiment.getOverheadTriples();
        Table overheadTable = new Table();
        if (experiment.isPerformedByExpert()) {
            TableRow head = new TableRow();
            head.addCell(new Paragraph("Triple"));
            head.addCell(new Paragraph("Verbalization"));
            overheadTable.addRow(head);
        }

        for (OWL2NL_QRTriple triple: overheadTriples) {
            TableRow cells = new TableRow();
            if (experiment.isPerformedByExpert()) {
                cells.addCell(new Paragraph(triple.getTriple()));
            }
            cells.addCell(new Paragraph(triple.getVerbalization()));
            overheadTable.addRow(cells);
        }
        overheadDiv.addElement(overheadTable);
        bodyDiv.addElement(overheadDiv);

        experimentDiv.addElement(bodyDiv);
        return experimentDiv;
    }
}
