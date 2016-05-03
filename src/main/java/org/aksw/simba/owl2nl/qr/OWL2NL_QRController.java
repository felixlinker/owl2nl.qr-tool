package org.aksw.simba.owl2nl.qr;

import org.aksw.simba.owl2nl.qr.data.OWL2NL_QRUser;
import org.aksw.simba.owl2nl.qr.db.OWL2NL_QRDbAdapterExtension;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRGuiHelper;
import org.aksw.simba.owl2nl.qr.gui.guiHelper.OWL2NL_QRUserGroupGuiHelper;
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

    public OWL2NL_QRController() {
        this.setDefaultGuiHelper(new OWL2NL_QRUserGroupGuiHelper());
    }

    @Override
    protected User identifyUser(HttpServletRequest request, GuiHelper<ExperimentSetup> guiHelper) {

        HttpSession session = request.getSession();
        DbAdapter db = getDbAdapter();

        int userId = db.getUserId(session.getId());
        if (userId == DbAdapter.ID_NOT_FOUND) {
            LOGGER.info("Identified a new user session ({}). Adding it to the database.", session.getId());

            // Add the user
            OWL2NL_QRDbAdapterExtension.addUser(session.getId(), db);

            // Send login page
            return null;

        } else {

            // Parse user group selection
            String userGroupParameter = request.getParameter(OWL2NL_QRUserGroupGuiHelper.RADIO_KEY);
            if (userGroupParameter != null) {
                boolean isExpert = Boolean.parseBoolean(userGroupParameter);
                OWL2NL_QRDbAdapterExtension.setUserIsExpert(isExpert, userId, db);

            } else if (OWL2NL_QRDbAdapterExtension.isExpertUnknown(userId, db)) {
                return null;
            }

            OWL2NL_QRUser user = OWL2NL_QRDbAdapterExtension.getUser(userId, db);
            LOGGER.info("user #" + userId + " has already " + user.getNumberOfAnswers() + " answers.");
            return user;
        }
    }

    @Override
    protected ExperimentDescription<?, ?> identifyExperiment(HttpServletRequest request, GuiHelper<ExperimentSetup> guiHelper) {
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
