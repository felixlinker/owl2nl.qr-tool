package org.aksw.simba.owl2nl.qr.parser;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRClassVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResultBase;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRResourceVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRClassVerbGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRResourceVerbGuiHelper;
import org.aksw.simba.qr.datatypes.ExperimentResult;

import javax.servlet.http.HttpServletRequest;

public class OWL2NL_QRResourceVerbResultParser extends OWL2NL_QRResultParser {

    @Override
    public ExperimentResult getExperimentResult(HttpServletRequest request) {
        OWL2NL_QRExperimentResultBase baseResult = super.getBaseResult(request);

        int adequacy, fluency, completeness;

        String tempString = request.getParameter(OWL2NL_QRResourceVerbGuiHelper.ADEQUACY_RATING_KEY);
        if (tempString == null) {
            adequacy = -1;
        } else {
            try {
                adequacy = Integer.parseInt(tempString);
            } catch (NumberFormatException e) {
                return logParsingError();
            }
        }

        tempString = request.getParameter(OWL2NL_QRResourceVerbGuiHelper.FLUENCY_RATING_KEY);
        try {
            fluency = Integer.parseInt(tempString);
        } catch (NumberFormatException e) {
            return logParsingError();
        }

        tempString = request.getParameter(OWL2NL_QRResourceVerbGuiHelper.COMPLETENSS_RATING_KEY);
        if (tempString == null) {
            completeness = -1;
        } else {
            try {
                completeness = Integer.parseInt(tempString);
            } catch (NumberFormatException e) {
                return logParsingError();
            }
        }

        LOGGER.info("Parsed a result for resource verbalization experiment #{} (adequacy={}, fluency={}, completeness={}).", baseResult.getExperimentSetupId(), adequacy, fluency, completeness);
        return new OWL2NL_QRResourceVerbExperimentResult(baseResult, adequacy, fluency, completeness);
    }
}
