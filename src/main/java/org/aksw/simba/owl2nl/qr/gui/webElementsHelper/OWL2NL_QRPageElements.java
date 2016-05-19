package org.aksw.simba.owl2nl.qr.gui.webElementsHelper;

import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.webelements.*;

import java.util.Collection;

/**
 * Holds some static methods for creating web elements.
 */
public class OWL2NL_QRPageElements {

    private static final String TABLE_ROW_CLASS = "tr-owl";

    public static WebElement generateUnorderedList(Collection<String> items) throws IllegalArgumentException {
        if (items.size() == 0) {
            throw new IllegalArgumentException();
        }

        UnnumberedList list = new UnnumberedList();
        for (String item: items) {
            list.addElement(item);
        }

        return list;
    }

    public static WebElement generateRadioButtonList(Collection<OWL2NL_QRRadioButtonHelper> buttons) throws IllegalArgumentException {
        if (buttons.size() == 0) {
            throw new IllegalArgumentException();
        }

        HtmlContainer container = new HtmlContainer();
        Table table = new Table();
        InputElement input;
        Label label;
        for (OWL2NL_QRRadioButtonHelper button: buttons) {
            input = new InputElement(button.getKey(), InputElement.InputType.Radiobutton);
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

    public static WebElement generateStarRatingTable(OWL2NL_QRStarRatingHelper...ratings) throws IllegalArgumentException {
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

    public static WebElement generateStarRating(int min, int max, int step, String name, String id) {
        InputElement input = new InputElement(name, InputElement.InputType.Number);
        input.addAttribute("class", "star_rating");
        input.addAttribute("min", Integer.toString(min));
        input.addAttribute("value", Integer.toString(min));
        input.addAttribute("max", Integer.toString(max));
        input.addAttribute("step", Integer.toString(step));
        input.addAttribute("id", id);
        return input;
    }

    public static WebElement createSubmitButton() {
        InputElement input = new InputElement(OWL2NL_QRGuiHelper.SUBMIT_BUTTON_KEY, InputElement.InputType.Submit);
        input.addAttribute("class", "submit-button");
        input.addAttribute("id", OWL2NL_QRGuiHelper.SUBMIT_BUTTON_ID);
        input.addAttribute("value", OWL2NL_QRGuiHelper.SUBMIT_BUTTON_LABEL);
        return new Paragraph(input);
    }

    public static Div createHeadDiv(String title) {
        Div headerDiv = new Div();
        headerDiv.addAttribute("class", "page-header");
        headerDiv.addElement(new Heading(new Text(title), Heading.HeadingOrder.H1));
        return headerDiv;
    }
}
