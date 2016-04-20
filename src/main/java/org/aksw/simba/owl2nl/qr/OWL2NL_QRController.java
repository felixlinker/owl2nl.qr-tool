package org.aksw.simba.owl2nl.qr;

import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRUser;
import org.aksw.simba.owl2nl.qr.db.OWL2NL_QRDbAdapterExtension;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRExperimentSelectionGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.qr.Controller;
import org.aksw.simba.qr.datatypes.ExperimentDescription;
import org.aksw.simba.qr.datatypes.ExperimentSetup;
import org.aksw.simba.qr.datatypes.User;
import org.aksw.simba.qr.db.DbAdapter;
import org.aksw.simba.qr.gui.GuiHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OWL2NL_QRController extends Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(OWL2NL_QRController.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected User identifyUser(HttpServletRequest request, GuiHelper<ExperimentSetup> guiHelper) {
        // Force login page upon experiment selection
        if (request.getParameter(OWL2NL_QRGuiHelper.EXPERIMENT_SELECTION_BUTTON_KEY) != null) {
            this.setDefaultGuiHelper(new OWL2NL_QRExperimentSelectionGuiHelper());
            return null;
        }

        HttpSession session = request.getSession();
        DbAdapter db = getDbAdapter();
        int userId = db.getUserId(session.getId());
        if (userId == DbAdapter.ID_NOT_FOUND) {
            LOGGER.info("Identified a new user session ({}). Adding it to the database.", session.getId());
            userId = OWL2NL_QRDbAdapterExtension.addUser(session.getId(), db);
            return new OWL2NL_QRUser(userId);
        } else {
            OWL2NL_QRUser user = OWL2NL_QRDbAdapterExtension.getUser(userId, db);
            LOGGER.info("user #" + userId + " has already " + user.getNumberOfAnswers() + " answers.");
            return user;
        }
    }

    @Override
    protected ExperimentDescription<?, ?> identifyExperiment(HttpServletRequest request,
            GuiHelper<ExperimentSetup> guiHelper) {
        ExperimentDescription<?, ?> expDesc = super.identifyExperiment(request, guiHelper);
        if (expDesc == null) {
            List<ExperimentDescription<?, ?>> experimentDescriptions = getExperimentDescriptions();
            expDesc = experimentDescriptions.get(0);
        }

        return expDesc;
    }

    @Override
    protected boolean shouldContainExperimentResult(HttpServletRequest request, GuiHelper<ExperimentSetup> guiHelper) {
        return request.getParameter(OWL2NL_QRGuiHelper.SUBMIT_BUTTON_KEY) != null;
    }
}
