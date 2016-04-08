package org.aksw.simba.owl2nl.qr.gui.webElementsHelper;

/**
 * Created by felix on 07.04.2016.
 */
public class OWL2NL_QRRadioButtonHelper {
    private String name;
    private String key;
    private String value;
    public OWL2NL_QRRadioButtonHelper(String name, String key, String value) {
        this.name = name;
        this.key = key;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
