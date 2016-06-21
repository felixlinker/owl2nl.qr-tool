package org.aksw.simba.owl2nl.qr.gui.experimentPages;

import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.qr.gui.GuiHelper;
import org.aksw.simba.qr.gui.Page;
import org.aksw.simba.webelements.*;

import java.util.HashMap;

public abstract class OWL2NL_QRPage implements Page {

    protected String onload;

    protected String message;

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    private HashMap<String, String> hiddenValues = new HashMap<>();

    @Override
    public void addHiddenValue(String key, String value) {
        hiddenValues.put(key, value);
    }

    protected OWL2NL_QRGuiHelper guiHelper;

    public OWL2NL_QRPage(OWL2NL_QRGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }

    @Override
    public String toHtml() {
        HtmlPage page = new HtmlPage(guiHelper.getStringValue(GuiHelper.StringValue.APPLICATION_NAME));
        String headTags = guiHelper.getStringValue(GuiHelper.StringValue.HEAD_TAGS);
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
            input = new InputElement(key, InputElement.InputType.Hidden);
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

    private Form createFormElement() {
        Form form = new Form();
        form.addAttribute("action", OWL2NL_QRGuiHelper.PATH);
        form.addAttribute("onsubmit", "return checkSubmit();");
        form.addAttribute("method", "POST");
        return form;
    }

    private Div createHeadDiv() {
        Div headDiv = new Div();
        headDiv.addAttribute("class", "jumbotron");
        headDiv.addElement(new Heading(new Text("Survey"), Heading.HeadingOrder.H1));
        headDiv.addElement(new Paragraph(new Text("This survey evaluates modern solutions for converting RDF and OWL to close-to-natural language.")));
        return headDiv;
    }

    private WebElement createFooter() {
        Paragraph footerParagraph = new Paragraph();
        footerParagraph.addAttribute("class", "footer_paragraph");
        footerParagraph.addElement(new Text("This survey has been created by "));
        footerParagraph.addElement(new Link("Axel Ngonga", "http://www.aksw.org/AxelNgonga.html"));
        footerParagraph.addElement(new Text(", a member of the Agile Knowledge Semantic Web (AKSW) research group ("));
        footerParagraph.addElement(new Link("http://www.aksw.org", "http://www.aksw.org"));
        footerParagraph.addElement(new Text(") in collaboration with Lorenz Bühmann, Felix Linker and Michael Röder. The answers are stored in an anonymized way. The cookie that comes with this page only contains a session id that is used to count the number of anwers the user has generated. "));
        footerParagraph.addElement(new Text(OWL2NL_QRExperimentPage.getWinText()));
        footerParagraph.addElement(new Text(" Regarding the lottery, any recourse to courts of law is excluded and the judges' decision is final."));
        return footerParagraph;
    }

    private WebElement createJSInclusions() {
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

    abstract HtmlContainer createContent();
}
