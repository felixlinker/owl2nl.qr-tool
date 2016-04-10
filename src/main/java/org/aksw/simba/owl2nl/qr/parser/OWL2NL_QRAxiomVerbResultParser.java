package org.aksw.simba.owl2nl.qr.parser;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRAxiomVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResult;
import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRExperimentResultBase;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRAxiomVerbGuiHelper;
import org.aksw.simba.qr.datatypes.ExperimentResult;

import javax.servlet.http.HttpServletRequest;

public class OWL2NL_QRAxiomVerbResultParser extends OWL2NL_QRResultParser {

    @Override
    public ExperimentResult getExperimentResult(HttpServletRequest request) {
        OWL2NL_QRExperimentResultBase baseResult = super.getBaseResult(request);
        if (baseResult.isBaseResultOnly()) {
            return new OWL2NL_QRExperimentResult(baseResult);
        }

        int adequacy, fluency;

        String tempString = request.getParameter(OWL2NL_QRAxiomVerbGuiHelper.ADEQUACY_RATING_KEY);
        try {
            adequacy = Integer.parseInt(tempString);
        } catch (NumberFormatException e) {
            return super.logParsingError();
        }
        tempString = request.getParameter(OWL2NL_QRAxiomVerbGuiHelper.FLUENCY_RATING_KEY);
        try {
            fluency = Integer.parseInt(tempString);
        } catch (NumberFormatException e) {
            return super.logParsingError();
        }

        LOGGER.info("Parsed a result for axiom verbalization experiment #{} (adequacy={}, fluency={}).", baseResult.getExperimentSetupId(), adequacy, fluency);
        return new OWL2NL_QRAxiomVerbExperimentResult(baseResult, adequacy, fluency);
    }
}
