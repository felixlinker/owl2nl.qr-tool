package org.aksw.simba.owl2nl.qr.parser;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResult;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResultBase;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRUserGroupGuiHelper;
import org.aksw.simba.qr.ExperimentResultParser;
import org.aksw.simba.qr.datatypes.ExperimentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class OWL2NL_QRResultParser implements ExperimentResultParser {

    protected static final Logger LOGGER = LoggerFactory.getLogger(OWL2NL_QRResultParser.class);

    protected ExperimentResult logIdParsingError() {
        LOGGER.error("Couldn't parse the ID of the experiment. Returning null.");
        return null;
    }

    protected ExperimentResult logParsingError() {
        LOGGER.error("Couldn't parse experiment result. Returning null.");
        return null;
    }

    protected OWL2NL_QRExperimentResultBase getBaseResult(HttpServletRequest request) throws NumberFormatException {
        String tmpString = request.getParameter(OWL2NL_QRGuiHelper.EXPERIMENT_ID_KEY);
        int experimentId = Integer.parseInt(tmpString);
        OWL2NL_QRExperimentResultBase baseResult = new OWL2NL_QRExperimentResultBase(experimentId);

        tmpString = request.getParameter(OWL2NL_QRUserGroupGuiHelper.RADIO_KEY);
        if (tmpString != null) {
            baseResult.setExpert(Boolean.parseBoolean(tmpString));
        }

        return baseResult;
    }

}
