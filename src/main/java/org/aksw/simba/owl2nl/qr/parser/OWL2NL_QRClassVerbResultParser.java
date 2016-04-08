package org.aksw.simba.owl2nl.qr.parser;

import org.aksw.simba.owl2nl.qr.data.results.OWL2NL_QRClassVerbExperimentResult;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRClassVerbGuiHelper;
import org.aksw.simba.qr.datatypes.ExperimentResult;

import javax.servlet.http.HttpServletRequest;

public class OWL2NL_QRClassVerbResultParser extends OWL2NL_QRResultParser {

    @Override
    public ExperimentResult getExperimentResult(HttpServletRequest request) {
        int experimentId, chosenId;
        try {
            experimentId = super.parseExperimentId(request);
        } catch (NumberFormatException e) {
            return logIdParsingError();
        }

        String tempString = request.getParameter(OWL2NL_QRClassVerbGuiHelper.CHOSEN_TRIPLE_KEY);
        try {
            chosenId = Integer.parseInt(tempString);
        } catch (NumberFormatException e) {
            return logParsingError();
        }

        LOGGER.info("Parsed a result for class verbalization experiment #{} (chosenId={}).", experimentId, chosenId);
        return new OWL2NL_QRClassVerbExperimentResult(experimentId, chosenId);
    }
}

