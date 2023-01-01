package com.jonathandevinesoftware.decisionlogger.gui.decision;

import com.jonathandevinesoftware.decisionlogger.core.ApplicationConstants;
import com.jonathandevinesoftware.decisionlogger.gui.common.Mode;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.MainMenuController;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;
import com.jonathandevinesoftware.decisionlogger.model.Decision;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Tag;

import javax.swing.*;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

public class DecisionEditorController {

    private DecisionEditorForm form;

    private boolean openMainMenuOnClose = true;

    private Decision decision;

    private Mode mode;

    public DecisionEditorController(Decision decision) {
        this.decision = decision;

        //check if decision already exists
        Decision dbDecision = null;
        try {
            dbDecision = DecisionDAO.getInstance().loadDecision(decision.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dbDecision != null) {
            mode = Mode.EDIT;
        } else {
            mode = Mode.NEW;
        }

        form = new DecisionEditorForm(decision, mode);
        form.setSaveCallback(this::onSave);
        form.setCancelCallback(this::onCancel);
        form.setDiscardCallback(this::onDiscard);
    }

    public void setOpenMainMenuOnClose(boolean openMainMenuOnClose) {
        this.openMainMenuOnClose = openMainMenuOnClose;
    }

    public void onSave(DecisionPanel.ViewModel viewModel) {

        boolean isNewDecision = mode == Mode.NEW;
        Decision decisionToPersist;
        //is this a save or update?
        if(!isNewDecision) {
            decisionToPersist = new Decision(decision.getId());
        } else {
            decisionToPersist = new Decision(UUID.randomUUID());
        }

        decisionToPersist.setDecisionText(viewModel.getDecision());
        decisionToPersist.setTimestamp(viewModel.getTimestamp());
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

        String action = mode == Mode.NEW ? "Discard" : "Delete";

        if (mode == Mode.NEW && !form.changesMade()) {
            closeForm();
            return;
        }

        if(mode == Mode.NEW) {
            GuiUtils.ConfirmDialogueWithAction(form, "Discard changes?", "Confirm", () -> {
               closeForm();
            });
        }

        //treat as delete in edit mode
        if(mode == Mode.EDIT) {
            GuiUtils.ConfirmDialogueWithAction(form, "Delete decision?", "Confirm", () -> {
                try {
                    DecisionDAO.getInstance().deleteDecision(decision.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(form, "Could not delete! " + e.getMessage());
                    return;
                }
                closeForm();
            });
        }
    }

    private void onDiscard() {
        if(form.changesMade()) {
            GuiUtils.ConfirmDialogueWithAction(form, "Discard changes?", "Confirm", () -> {
                closeForm();
            });
        } else {
            closeForm();
        }
    }

    private void closeForm() {
        form.dispose();
        form = null;
        if(openMainMenuOnClose) {
            MainMenuController.getInstance().displayMainMenu();
        }
    }
}
