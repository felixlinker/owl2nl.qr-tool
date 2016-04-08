package org.aksw.simba.owl2nl.qr.gui.webElementsHelper;

/**
 * Helper class to implement star rating elements
 */
public class OWL2NL_QRStarRatingHelper {
    private String key;
    private String name;

    public OWL2NL_QRStarRatingHelper(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * @return Key as hidden value for star rating
     */
    public String getKey() {
        return key;
    }

    /**
     * @return Displayname for rating
     */
    public String getName() {
        return name;
    }
}
