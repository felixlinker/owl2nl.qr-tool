package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.ListConverter;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.*;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRRadioButtonHelper;
import org.aksw.simba.webelements.Div;
import org.aksw.simba.webelements.HtmlContainer;
import org.aksw.simba.webelements.Paragraph;
import org.aksw.simba.webelements.Text;

import java.util.LinkedList;

public class OWL2NL_QRExperimentSelectionPage extends OWL2NL_QRExperimentPage<OWL2NL_QRExperimentSetup> {

    private boolean finishPage;

    public OWL2NL_QRExperimentSelectionPage(boolean finishPage) {
        super(new OWL2NL_QRExperimentSelectionGuiHelper(), null);
        this.finishPage = finishPage;
    }

    @Override
    protected HtmlContainer createContent() {
        HtmlContainer container = new HtmlContainer();

        if (finishPage) {
            Div alertDiv = new Div();
            alertDiv.addAttribute("class", "alert alert-warning");
            alertDiv.addAttribute("role", "alert");
            alertDiv.addElement(new Text("There are no more datasets for you. Thank you very much for all the work that you have done!"));
            container.addElement(alertDiv);
        }

        LinkedList<String> experimentTypes = new LinkedList<>();
        experimentTypes.add(OWL2NL_QRAxiomVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE);
        experimentTypes.add(OWL2NL_QRClassVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE);
        experimentTypes.add(OWL2NL_QRResourceVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE);

        addHiddenValue(OWL2NL_QRGuiHelper.EXPERIMENT_ID_KEY, Integer.toString(-1));

        ListConverter<String, OWL2NL_QRRadioButtonHelper> converter = str -> new OWL2NL_QRRadioButtonHelper(mapExperimentType(str), OWL2NL_QRGuiHelper.EXPERIMENT_IDENTIFIER_KEY, str);
        container.addElement(new Paragraph("You can select of the following experiments:"));
        container.addElement(generateRadioButtonList(converter.map(experimentTypes)));

        container.addElement(createSubmitButton());

        return container;
    }

    @Override
    Div generateExperimentDiv(OWL2NL_QRExperimentSetup experimentSetup) {
        return null;
    }

    @Override
    HtmlContainer getInstructions() {
        return null;
    }
}
