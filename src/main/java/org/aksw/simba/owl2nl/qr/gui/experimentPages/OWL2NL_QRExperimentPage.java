package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRPageElements;
import org.aksw.simba.webelements.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OWL2NL_QRExperimentPage<T extends OWL2NL_QRExperimentSetup> extends OWL2NL_QRPage {

    protected static final Logger LOGGER = LoggerFactory.getLogger(OWL2NL_QRExperimentPage.class);

    private T experiment;

    public OWL2NL_QRExperimentPage(OWL2NL_QRGuiHelper guiHelper, T experiment) {
        super(guiHelper);
        this.experiment = experiment;
    }

    protected void addPageContentExperiment(HtmlContainer container) {
        HtmlContainer instructions = getInstructions();
        instructions.addElement(new Paragraph(getWinText()));
        container.addElement(instructions);
        container.addElement(createMessageDiv());

        container.addElement(OWL2NL_QRPageElements.createHeadDiv("Task"));

        Div experimentDiv = generateExperimentDiv(experiment);
        this.addHiddenValue(OWL2NL_QRGuiHelper.EXPERIMENT_ID_KEY, Integer.toString(experiment.getId()));
        this.addHiddenValue(OWL2NL_QRGuiHelper.EXPERIMENT_IDENTIFIER_KEY, guiHelper.getExperimentIdentifierValue());

        container.addElement(experimentDiv);

        container.addElement(OWL2NL_QRPageElements.createSubmitButton());
    }

    protected void addPageContentNextExperiment(HtmlContainer container) {
        HtmlContainer nextExperimentContainer = getNextExperimentContainer();

        if (nextExperimentContainer == null) {
            addPageContentFinishPage(container);
            return;
        }

        container.addElement(getNextExperimentContainer());

        container.addElement(OWL2NL_QRPageElements.createSubmitButton());

        this.addHiddenValue(OWL2NL_QRGuiHelper.EXPERIMENT_IDENTIFIER_KEY, getNextExperimentType());
    }

    protected void addPageContentFinishPage(HtmlContainer container) {
        Div alertDiv = new Div();
        alertDiv.addAttribute("class", "alert alert-warning");
        alertDiv.addAttribute("role", "alert");
        alertDiv.addElement(new Text("There are no more datasets for you. Thank you very much for all the work that you have done!"));
        container.addElement(alertDiv);

        container.addElement(getWinMessage());
    }

    protected HtmlContainer createContent() {
        HtmlContainer container = new HtmlContainer();
        if (experiment != null) {
            addPageContentExperiment(container);
        } else { // experiment == null
            addPageContentNextExperiment(container);
        }

        return container;
    }

    protected Div createMessageDiv() {
        Div messagesDiv = new Div();
        if ((this.message != null) && (!this.message.isEmpty())) {
            /*if (this.message.equals(OWL2NL_QRGuiHelper.RESULT_SAVED_SUCCESSFULLY_MSG)) {
                Div messageDiv = new Div();
                messageDiv.addAttribute("class", "alert alert-info");
                messageDiv.addAttribute("role", "alert");
                messageDiv.addElement(new Paragraph(OWL2NL_QRGuiHelper.RESULT_SAVED_SUCCESSFULLY_MSG));
                messagesDiv.addElement(messageDiv);
            }*/

            if (this.message.equals(OWL2NL_QRGuiHelper.RESULT_SAVED_ERROR_MSG)) {
                Div messageDiv = new Div();
                messageDiv.addAttribute("class", "alert alert-danger");
                messageDiv.addAttribute("role", "alert");
                messageDiv.addElement(new Paragraph(OWL2NL_QRGuiHelper.RESULT_SAVED_ERROR_MSG));
                messagesDiv.addElement(messageDiv);
            }
            if (experiment.getUserAnswerCount() >= OWL2NL_QRGuiHelper.NUMBER_OF_ANSWERS_NEEDED_FOR_KEY_WORD) {
                messagesDiv.addElement(getWinMessage());
            }
        }
        return messagesDiv;
    }

    protected static WebElement getWinMessage() {
        Div messageDiv = new Div();
        messageDiv.addAttribute("class", "alert alert-success");
        messageDiv.addAttribute("role", "alert");
        Paragraph p = new Paragraph();
        p.addElement(new BoldText("Congratulations!"));
        p.addElement(new Text(" You have answered enough questions to take part in the lottery. Just send the keyword \"banana\" to "));
        p.addElement(new Link("buehmann@informatik.uni-leipzig.de", "mailto:buehmann@informatik.uni-leipzig.de"));
        p.addElement(new Text("."));
        messageDiv.addElement(p);
        messageDiv.addElement(new Paragraph("Thank you very much for your effort!"));

        return messageDiv;
    }

    protected static String getWinText() {
        StringBuilder builder = new StringBuilder();
        builder.append("For getting a chance to win one of the amazon vouchers, you will have to submit the answers of ");
        builder.append(OWL2NL_QRGuiHelper.NUMBER_OF_ANSWERS_NEEDED_FOR_KEY_WORD);
        builder.append(" different pages (during a single session). After the submission of the tenth page, you will see a message with further instructions for taking part in the lottery.");

        return builder.toString();
    }

    abstract String getNextExperimentType();

    abstract HtmlContainer getNextExperimentContainer();

    abstract HtmlContainer getInstructions();

    abstract Div generateExperimentDiv(T t);
}
