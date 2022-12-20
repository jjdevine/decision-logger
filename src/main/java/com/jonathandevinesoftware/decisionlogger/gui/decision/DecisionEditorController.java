package com.jonathandevinesoftware.decisionlogger.gui.decision;

import com.jonathandevinesoftware.decisionlogger.core.ApplicationConstants;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.MainMenuController;
import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

public class DecisionEditorController {

    private DecisionEditorForm form;

    private boolean openMainMenuOnClose = true;

    private Decision decision;

    public DecisionEditorController(Decision decision) {
        this.decision = decision;
        form = new DecisionEditorForm(decision);
        form.setSaveCallback(this::onSave);
        form.setCancelCallback(this::onCancel);
    }

    public void setOpenMainMenuOnClose(boolean openMainMenuOnClose) {
        this.openMainMenuOnClose = openMainMenuOnClose;
    }

    public void onSave(DecisionPanel.ViewModel viewModel) {

        boolean isNewDecision = decision == null;
        Decision decisionToPersist;
        //is this a save or update?
        if(!isNewDecision) {
            decisionToPersist = new Decision(decision.getId());
        } else {
            decisionToPersist = new Decision(UUID.randomUUID());
        }

        decisionToPersist.setDecisionText(viewModel.getDecision());
        decisionToPersist.setTimestamp(LocalDateTime.now());
        decisionToPersist.setDecisionMakers(
                viewModel.getDecisionMakers().stream().map(Person::getId).collect(Collectors.toList()));
        decisionToPersist.setTags(
                viewModel.getTags().stream().map(Tag::getId).collect(Collectors.toList()));

        try {
            System.out.println("saving...");
            DecisionDAO.getInstance().saveOrUpdateDecision(decisionToPersist);

            if(ApplicationConstants.DEBUG) {
                System.out.println("loading...");
                System.out.println(DecisionDAO.getInstance().loadDecision(decision.getId()));
            }

            JOptionPane.showMessageDialog(form,
                    "Decision saved successfully!",
                    "Decision Saved",
                    JOptionPane.INFORMATION_MESSAGE);
            closeForm();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCancel() {
        //if editing a decision, treat as a deletion
        if(decision != null) {
            try {
                DecisionDAO.getInstance().deleteDecision(decision.getId());
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(form, "Could not delete! " + e.getMessage());
                return;
            }
        }
        closeForm();
    }

    private void closeForm() {
        System.out.println("Closing form");
        form.dispose();
        form = null;
        if(openMainMenuOnClose) {
            MainMenuController.getInstance().displayMainMenu();
        }
    }
}
