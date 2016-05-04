package org.aksw.simba.owl2nl.qr.gui.guiHelper;

import org.aksw.simba.owl2nl.qr.data.experiments.OWL2NL_QRExperimentSetup;
import org.aksw.simba.owl2nl.qr.gui.experimentPages.OWL2NL_QRLoginPage;
import org.aksw.simba.qr.datatypes.ExperimentSetup;
import org.aksw.simba.qr.gui.AbstractGuiHelper;
import org.aksw.simba.qr.gui.Page;

import java.util.List;

public abstract class OWL2NL_QRGuiHelper<T extends OWL2NL_QRExperimentSetup> extends AbstractGuiHelper<ExperimentSetup> {

    public static final int NUMBER_OF_ANSWERS_NEEDED_FOR_KEY_WORD = 10;

    private Class<T> type;

    public static final String APPLICATION_NAME = "Survey";
    public static final String LONG_APPLICATION_NAME = "";
    public static final String EXPERIMENT_ID_KEY = "experimentId";
    public static final String EXPERIMENT_IDENTIFIER_KEY = "experimentType";
    public static final String SUBMIT_BUTTON_KEY = "submit";
    public static final String SUBMIT_BUTTON_LABEL = "Continue";
    public static final String SUBMIT_BUTTON_ID = "submit";
    public static final String PATH = "survey";
    public static final String PATH_FOR_SUBMIT = "survey";

    public static final String RESULT_SAVED_SUCCESSFULLY_MSG = "Your input has been saved successfully.";
    public static final String RESULT_SAVED_ERROR_MSG = "Your rating couldn't be saved.";

    public static final String HEAD_TAGS = "<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\" />\n"
            + "<link href=\"css/tapioca-qr.css\" type=\"text/css\" rel=\"stylesheet\">\n"
            + "<link href=\"css/jquery-ui.min.css\" type=\"text/css\" rel=\"stylesheet\">\n"
            + "<link href=\"css/bootstrap.min.css\" type=\"text/css\" rel=\"stylesheet\">\n"
            + "<link href=\"css/bootstrap-theme.min.css\" type=\"text/css\" rel=\"stylesheet\">\n"
            + "<link href=\"css/star-rating.min.css\" type=\"text/css\" rel=\"stylesheet\">\n"
            + "<link href=\"css/table-styles.css\" type=\"text/css\" rel=\"stylesheet\">\n"
            + "<!--java script files are included at the end of the body -->\n";

    public OWL2NL_QRGuiHelper(Class<T> type) {
        this.type = type;
    }

    @Override
    public String getStringValue(StringValue key) {
        switch (key) {
            case EXPERIMENT_IDENTIFIER_KEY: {
                return EXPERIMENT_IDENTIFIER_KEY;
            }
            case APPLICATION_NAME: {
                return APPLICATION_NAME;
            }
            case LONG_APPLICATION_NAME: {
                return LONG_APPLICATION_NAME;
            }
            case HEAD_TAGS: {
                return HEAD_TAGS;
            }
            case PATH_FOR_SUBMIT: {
                return PATH_FOR_SUBMIT;
            }
            case RESULT_SAVED_SUCCESSFULLY_MSG: {
                return RESULT_SAVED_SUCCESSFULLY_MSG;
            }
            case RESULT_SAVED_ERROR_MSG: {
                return RESULT_SAVED_ERROR_MSG;
            }
            default: {
                return super.getStringValue(key);
            }
        }
    }

    @Override
    public Page getExperimentPage(ExperimentSetup experimentSetup) {
        if (type.isInstance(experimentSetup)) {
            return this.getExperimentPageFinal((T)experimentSetup);
        }
        return null;
    }

    @Override
    public Page getLoginPage() {
        return new OWL2NL_QRLoginPage();
    }

    @Override
    public Page getLoginPage(List<String> experimentNames) {
        return this.getLoginPage();
    }

    public static String mapExperimentType(String type) {
        switch (type) {
            case OWL2NL_QRAxiomVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE: return OWL2NL_QRAxiomVerbGuiHelper.EXPERIMENT_IDENTIFIER_NAME;
            case OWL2NL_QRClassVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE: return OWL2NL_QRClassVerbGuiHelper.EXPERIMENT_IDENTIFIER_NAME;
            case OWL2NL_QRResourceVerbGuiHelper.EXPERIMENT_IDENTIFIER_VALUE: return OWL2NL_QRResourceVerbGuiHelper.EXPERIMENT_IDENTIFIER_NAME;
            default: return type;
        }
    }

    public abstract Page getExperimentPageFinal(T t);

    public abstract String getExperimentIdentifierValue();
}
