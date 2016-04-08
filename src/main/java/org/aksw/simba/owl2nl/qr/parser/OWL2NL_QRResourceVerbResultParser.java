package org.aksw.simba.owl2nl.qr.parser;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRClassVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRResourceVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRClassVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRResourceVerbGuiHelper;
import org.aksw.simba.qr.datatypes.ExperimentResult;

import javax.servlet.http.HttpServletRequest;

public class OWL2NL_QRResourceVerbResultParser extends OWL2NL_QRResultParser {

    @Override
    public ExperimentResult getExperimentResult(HttpServletRequest request) {
        int experimentId, adequacy, fluency, completeness;
        try {
            experimentId = super.parseExperimentId(request);
        } catch (NumberFormatException e) {
            return logIdParsingError();
        }

        // ToDo: what happens if adequacy isn't set
        String tempString = request.getParameter(OWL2NL_QRResourceVerbGuiHelper.ADEQUACY_RATING_KEY);
        try {
            adequacy = Integer.parseInt(tempString);
        } catch (NumberFormatException e) {
            return logParsingError();
        }

        // ToDo: what happens if fluency isn't set
        tempString = request.getParameter(OWL2NL_QRResourceVerbGuiHelper.FLUENCY_RATING_KEY);
        try {
            fluency = Integer.parseInt(tempString);
        } catch (NumberFormatException e) {
            return logParsingError();
        }

        tempString = request.getParameter(OWL2NL_QRResourceVerbGuiHelper.COMPLETENSS_RATING_KEY);
        try {
            completeness = Integer.parseInt(tempString);
        } catch (NumberFormatException e) {
            return logParsingError();
        }

        LOGGER.info("Parsed a result for resource verbalization experiment #{} (adequacy={}, fluency={}, completeness={}).", experimentId, adequacy, fluency, completeness);
        return new OWL2NL_QRResourceVerbExperimentResult(experimentId, adequacy, fluency, completeness);
    }
}
