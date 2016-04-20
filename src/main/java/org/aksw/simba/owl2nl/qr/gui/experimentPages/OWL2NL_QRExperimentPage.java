package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.data.ListConverter;
import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.*;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRRadioButtonHelper;
import org.aksw.simba.owl2nl.qr.gui.webElementsHelper.OWL2NL_QRStarRatingHelper;
import org.aksw.simba.qr.gui.GuiHelper.StringValue;
import org.aksw.simba.qr.gui.Page;
import org.aksw.simba.webelements.*;
import org.aksw.simba.webelements.Heading.HeadingOrder;
import org.aksw.simba.webelements.InputElement.InputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class OWL2NL_QRExperimentPage<T extends OWL2NL_QRExperimentSetup> implements Page {

    protected static final Logger LOGGER = LoggerFactory.getLogger(OWL2NL_QRExperimentPage.class);

    public static final int NUMBER_OF_ANSWERS_NEEDED_FOR_KEY_WORD = 10;

    private static final String BOLD_START_TAG = "<b>";
    private static final String BOLD_END_TAG = "</b>";
    private static final String ITALIC_START_TAG = "<i>";
    private static final String ITALIC_END_TAG = "</i>";

    private T experiment;
    protected String message;
    protected String onload;
    private HashMap<String, String> hiddenValues = new HashMap<>();
    protected OWL2NL_QRGuiHelper guiHelper;

    public OWL2NL_QRExperimentPage(OWL2NL_QRGuiHelper guiHelper, T experiment) {
        this.guiHelper = guiHelper;
        this.experiment = experiment;
    }

    @Override
    public String toHtml() {
        HtmlPage page = new HtmlPage(guiHelper.getStringValue(StringValue.APPLICATION_NAME));
        String headTags = guiHelper.getStringValue(StringValue.HEAD_TAGS);
        if (headTags != null) {
            page.setHead(headTags);
        }
        Div mainDiv = new Div();
        mainDiv.addAttribute("class", "container theme-showcase");
        mainDiv.addAttribute("role", "main");

        Form form = createFormElement();

        form.addElement(createHeadDiv());
        form.addElement(createContent());
        InputElement input;
        for (String key : hiddenValues.keySet()) {
            input = new InputElement(key, InputType.Hidden);
            input.setValue(hiddenValues.get(key));
            input.setId(key);
            form.addElement(input);
        }

        mainDiv.addElement(form);
        mainDiv.addElement(createFooter());
        page.addElementToBody(mainDiv);

        page.addElementToBody(createJSInclusions());

        if (onload != null) {
            page.setOnload(onload);
        }
        return page.toHtml();
    }

    @Override
    public void addHiddenValue(String key, String value) {
        hiddenValues.put(key, value);
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    protected void addPageContent(HtmlContainer container) {
        if (!experiment.isExpertValueKnown()) {
            Div headerDiv = new Div();
            headerDiv.addAttribute("class", "page-header");
            headerDiv.addElement(new Heading(new Text("Instructions"), HeadingOrder.H1));
            container.addElement(headerDiv);
            container.addElement(new Paragraph("Before you start with the evaluation please tell us how familiar you are with RDF and OWL concepts."));
            container.addElement(new Paragraph("If you're not familiar with OWL and RDF concepts, please select 'Amateur' as user group. If you are familiar with those concepts, you can select 'Expert' as user group."));

            container.addElement(createUserGroupSelection());
            this.addHiddenValue(OWL2NL_QRGuiHelper.EXPERIMENT_ID_KEY, Integer.toString(-1));
            this.addHiddenValue(OWL2NL_QRGuiHelper.EXPERIMENT_IDENTIFIER_KEY, guiHelper.getExperimentIdentifierValue());
            return;
        }

        HtmlContainer intructions = getInstructions();
        intructions.addElement(new Paragraph(getWinText()));
        container.addElement(intructions);
        container.addElement(createMessageDiv());

        Div headerDiv = new Div();
        headerDiv.addAttribute("class", "page-header");
        headerDiv.addElement(new Heading(new Text("Task"), HeadingOrder.H1));
        container.addElement(headerDiv);

        Div experimentDiv = generateExperimentDiv(experiment);
        this.addHiddenValue(OWL2NL_QRGuiHelper.EXPERIMENT_ID_KEY, Integer.toString(experiment.getId()));
        this.addHiddenValue(OWL2NL_QRGuiHelper.EXPERIMENT_IDENTIFIER_KEY, guiHelper.getExperimentIdentifierValue());

        container.addElement(experimentDiv);
    }

    protected HtmlContainer createUserGroupSelection() {
        HtmlContainer container = new HtmlContainer();

        Div messageDiv = new Div();
        messageDiv.addElement(new Paragraph(new BoldText("Please select a user group:")));

        OWL2NL_QRUserGroupGuiHelper userGuiHelper = new OWL2NL_QRUserGroupGuiHelper();
        LinkedList<OWL2NL_QRRadioButtonHelper> buttonHelpers = new LinkedList<>();
        buttonHelpers.add(new OWL2NL_QRRadioButtonHelper(userGuiHelper.EXPERT_RADIO_NAME, userGuiHelper.RADIO_KEY, userGuiHelper.EXPERT_RADIO_VALUE));
        buttonHelpers.add(new OWL2NL_QRRadioButtonHelper(userGuiHelper.AMATEUR_RADIO_NAME, userGuiHelper.RADIO_KEY, userGuiHelper.AMATEUR_RADIO_NAME));

        messageDiv.addElement(generateRadioButtonList(buttonHelpers));

        container.addElement(messageDiv);
        return container;
    }

    protected HtmlContainer createContent() {
        HtmlContainer container = new HtmlContainer();
        if (experiment != null) {
            addPageContent(container);
            container.addElement(createSubmitButton());
        } else {
            Div alertDiv = new Div();
            alertDiv.addAttribute("class", "alert alert-warning");
            alertDiv.addAttribute("role", "alert");
            alertDiv.addElement(new Text("There are no more datasets for you. Thank you very much for all the work that you have done!"));
            container.addElement(alertDiv);

            LinkedList<String> experimentTypes = new LinkedList<>();
            experimentTypes.add(OWL2NL_QRAxiomVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE);
            experimentTypes.add(OWL2NL_QRClassVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE);
            experimentTypes.add(OWL2NL_QRResourceVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE);
            experimentTypes.remove(guiHelper.getExperimentIdentifierValue());

            // ToDo: map experiment identifiers to more fluent language

            addHiddenValue(guiHelper.EXPERIMENT_ID_KEY, Integer.toString(-1));

            ListConverter<String, OWL2NL_QRRadioButtonHelper> converter = a -> new OWL2NL_QRRadioButtonHelper(mapExperimentType(a), OWL2NL_QRGuiHelper.EXPERIMENT_IDENTIFIER_KEY, a);
            container.addElement(new Paragraph("If you haven't tried below experiments, you can try them now!"));
            container.addElement(generateRadioButtonList(converter.map(experimentTypes)));

            container.addElement(createSubmitButton());
        }
        return container;
    }

    protected WebElement createFooter() {
        Paragraph footerParagraph = new Paragraph();
        footerParagraph.addAttribute("class", "footer_paragraph");
        footerParagraph.addElement(new Text("This survey has been created by "));
        footerParagraph.addElement(new Link("Michael Röder", "http://www.aksw.org/MichaelRoeder.html"));
        footerParagraph.addElement(new Text(", a member of the Agile Knowledge Semantic Web (AKSW) research group ("));
        footerParagraph.addElement(new Link("http://www.aksw.org", "http://www.aksw.org"));
        footerParagraph.addElement(new Text(
                "). The answers are stored in an anonymized way. The cookie that comes with this page only contains a session id that is used to count the number of anwers the user has generated. "));
        footerParagraph.addElement(new Text("After answering " + NUMBER_OF_ANSWERS_NEEDED_FOR_KEY_WORD
                + " forms during a single session, a key word is shown with which the user can take part in the lottery of amazon vouchers. "));
        footerParagraph.addElement(new Text(
                "Regarding the lottery, any recourse to courts of law is excluded and the judges' decision is final."));
        return footerParagraph;
    }

    protected WebElement createJSInclusions() {
        HtmlContainer container = new HtmlContainer();
        JavaScriptFunction js = new JavaScriptFunction("");
        js.addAttribute("src", "js/jquery.min.js");
        container.addElement(js);
        js = new JavaScriptFunction("");
        js.addAttribute("src", "js/bootstrap.min.js");
        container.addElement(js);
        js = new JavaScriptFunction("");
        js.addAttribute("src", "js/jquery-ui.min.js");
        container.addElement(js);
        js = new JavaScriptFunction("");
        js.addAttribute("src", "js/star-rating.min.js");
        container.addElement(js);
        js = new JavaScriptFunction("");
        js.addAttribute("src", "js/owl2nl.qr-tool.js");
        container.addElement(js);
        return container;
    }

    public Form createFormElement() {
        Form form = new Form();
        form.addAttribute("action", OWL2NL_QRGuiHelper.PATH);
        form.addAttribute("onsubmit", "return checkSubmit();");
        form.addAttribute("method", "POST");
        return form;
    }

    // ToDo: Maybe add message during usergroup selection?
    protected Div createMessageDiv() {
        Div messagesDiv = new Div();
        if ((this.message != null) && (!this.message.isEmpty())) {
            if (this.message.equals(OWL2NL_QRGuiHelper.RESULT_SAVED_SUCCESSFULLY_MSG)) {
                Div messageDiv = new Div();
                messageDiv.addAttribute("class", "alert alert-info");
                messageDiv.addAttribute("role", "alert");
                messageDiv.addElement(new Paragraph(OWL2NL_QRGuiHelper.RESULT_SAVED_SUCCESSFULLY_MSG));
                messagesDiv.addElement(messageDiv);
            }
            if (this.message.equals(OWL2NL_QRGuiHelper.RESULT_SAVED_ERROR_MSG)) {
                Div messageDiv = new Div();
                messageDiv.addAttribute("class", "alert alert-danger");
                messageDiv.addAttribute("role", "alert");
                messageDiv.addElement(new Paragraph(OWL2NL_QRGuiHelper.RESULT_SAVED_ERROR_MSG));
                messagesDiv.addElement(messageDiv);
            }
            if (experiment.getUserAnswerCount() >= NUMBER_OF_ANSWERS_NEEDED_FOR_KEY_WORD) {
                Div messageDiv = new Div();
                messageDiv.addAttribute("class", "alert alert-success");
                messageDiv.addAttribute("role", "alert");
                Paragraph p = new Paragraph();
                p.addElement(new BoldText("Congratulations!"));
                // ToDo: change keyword or mail?
                p.addElement(new Text(
                        " You have answered enough questions to take part in the lottery. Just send the keyword \"banana\" to "));
                p.addElement(new Link("roeder@informatik.uni-leipzig.de", "mailto:roeder@informatik.uni-leipzig.de"));
                p.addElement(new Text("."));
                messageDiv.addElement(p);
                messageDiv.addElement(new Paragraph("Thank you very much for your effort!"));
                messagesDiv.addElement(messageDiv);
            }
        }
        return messagesDiv;
    }

    protected Div createHeadDiv() {
        Div headDiv = new Div();
        headDiv.addAttribute("class", "jumbotron");
        headDiv.addElement(new Heading(new Text("Survey"), HeadingOrder.H1));
        headDiv.addElement(new Paragraph(new Text("This is a survey for evaluating the calculation of RDF dataset similarity in terms of topical relatedness.")));
        return headDiv;
    }

    protected WebElement generateUnorderedList(Collection<String> items) throws IllegalArgumentException {
        if (items.size() == 0) {
            throw new IllegalArgumentException();
        }

        UnnumberedList list = new UnnumberedList();
        for (String item: items) {
            list.addElement(item);
        }

        return list;
    }

    protected WebElement generateRadioButtonList(Collection<OWL2NL_QRRadioButtonHelper> buttons) throws IllegalArgumentException {
        if (buttons.size() == 0) {
            throw new IllegalArgumentException();
        }

        HtmlContainer container = new HtmlContainer();
        Table table = new Table();
        InputElement input;
        Label label;
        for (OWL2NL_QRRadioButtonHelper button: buttons) {
            input = new InputElement(button.getKey(), InputType.Radiobutton);
            input.addAttribute("id", "radio_" + button.getValue());
            input.addAttribute("value", button.getValue());

            label = new Label(new Text(button.getName()));
            label.addAttribute("for", "radio_" + button.getValue());
            label.addAttribute("class", "rating_label");

            TableRow row = new TableRow();
            row.addCell(input);
            row.addCell(label);
            table.addRow(row);
        }

        container.addElement(table);
        return container;
    }

    protected WebElement generateStarRatingTable(OWL2NL_QRStarRatingHelper...ratings) throws IllegalArgumentException {
        if (ratings.length == 0) {
            throw new IllegalArgumentException();
        }

        Table table = new Table();
        for (OWL2NL_QRStarRatingHelper rating: ratings) {
            TableRow row = new TableRow();
            row.addCell(new TableCell(new Text(rating.getName())));
            row.addCell(new TableCell(generateStarRating(0, 5, 1, rating.getKey(), rating.getKey())));
            table.addRow(row);
        }

        return table;
    }

    private WebElement generateStarRating(int min, int max, int step, String name, String id) {
        InputElement input = new InputElement(name, InputType.Number);
        input.addAttribute("class", "star_rating");
        input.addAttribute("min", Integer.toString(min));
        input.addAttribute("value", Integer.toString(min));
        input.addAttribute("max", Integer.toString(max));
        input.addAttribute("step", Integer.toString(step));
        input.addAttribute("id", id);
        return input;
    }

    protected WebElement createSubmitButton() {
        InputElement input = new InputElement(OWL2NL_QRGuiHelper.SUBMIT_BUTTON_KEY, InputType.Submit);
        input.addAttribute("id", "submit");
        input.addAttribute("value", OWL2NL_QRGuiHelper.SUBMIT_BUTTON_LABEL);
        return new Paragraph(input);
    }

    protected String getWinText() {
        // ToDo: alter this text
        StringBuilder builder = new StringBuilder();
        builder.append("For getting a chance to win one of the amazon vouchers, you will have to submit the answers of ");
        builder.append(NUMBER_OF_ANSWERS_NEEDED_FOR_KEY_WORD);
        builder.append(" different pages (during a single session). After the submission of the tenth page, a solution word is displayed, that you can send to roeder@informatik.uni-leipzig.de if you want to take part in the lottery.");

        return builder.toString();
    }

    protected String mapExperimentType(String type) {
        switch (type) {
            case OWL2NL_QRAxiomVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE: return OWL2NL_QRAxiomVerbGuiHelper.EXPERIMENT_IDENTIFIER_NAME;
            case OWL2NL_QRClassVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE: return OWL2NL_QRClassVerbGuiHelper.EXPERIMENT_IDENTIFIER_NAME;
            case OWL2NL_QRResourceVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE: return OWL2NL_QRResourceVerbGuiHelper.EXPERIMENT_IDENTIFIER_NAME;
            default: return type;
        }
    }

    abstract HtmlContainer getInstructions();

    abstract Div generateExperimentDiv(T t);
}
