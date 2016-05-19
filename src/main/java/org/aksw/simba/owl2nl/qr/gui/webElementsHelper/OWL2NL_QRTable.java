package org.aksw.simba.owl2nl.qr.gui.webElementsHelper;

import org.aksw.simba.webelements.Table;
import org.aksw.simba.webelements.TableRow;
import org.aksw.simba.webelements.Text;
import org.aksw.simba.webelements.WebElement;

import java.util.Arrays;

public class OWL2NL_QRTable {

    private static final String TABLE_CLASS = "table-owl";

    private Table table;

    private int rowIndex = 0;
    private static final String LIGHT_CLASS = "light-row";
    private static final String DARK_CLASS = "dark-row";
    private static final String[] ROW_CLASSES = new String[] { LIGHT_CLASS, DARK_CLASS };

    public OWL2NL_QRTable() {
        this.table = new Table();
        this.table.setClass(TABLE_CLASS);
    }

    public void addRow(String ...cells) {
        this.addRow(toWebElements(cells));
    }

    public void addRowLight(String ...cells) {
        this.addRowLight(toWebElements(cells));
    }

    public void addRow(WebElement ...elements) {
        this.createRow(this.getRowClass(), elements);
    }

    public void addRowLight(WebElement ...elements) {
        this.rowIndex = 0;
        this.createRow(this.LIGHT_CLASS, elements);
    }

    private static WebElement[] toWebElements(String ...texts) {
        WebElement[] elements = new WebElement[texts.length];
        for (int i = 0; i < texts.length; i++) {
            elements[i] = new Text(texts[i]);
        }

        return elements;
    }

    private void createRow(String clazz, WebElement ...elements) {
        TableRow row = new TableRow();
        row.setClass(clazz);
        for (WebElement element: elements) {
            row.addCell(element);
        }

        this.table.addRow(row);
    }

    private String getRowClass() {
        String clazz = ROW_CLASSES[this.rowIndex];
        this.rowIndex = ++this.rowIndex % 2;
        return clazz;
    }

    public Table getTable() {
        return this.table;
    }
}
