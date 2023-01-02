package com.jonathandevinesoftware.decisionlogger.gui.meeting;

import com.jonathandevinesoftware.decisionlogger.core.Application;
import com.jonathandevinesoftware.decisionlogger.gui.common.Mode;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.MainMenuController;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.model.Meeting;
import com.jonathandevinesoftware.decisionlogger.model.MeetingDAO;

import javax.swing.*;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

public class MeetingEditorController {

    private MeetingEditorForm form;
    private boolean openMainMenuOnClose = true;

    private Mode mode;

    public MeetingEditorController() {
        mode = Mode.NEW;
        Meeting meeting = new Meeting(UUID.randomUUID());
        form = new MeetingEditorForm(meeting, Mode.NEW);
        form.setOnDiscardCallback(this::onDiscard);
        form.setOnCancelDeleteCallback(this::onCancelDelete);
        form.setOnFinishCallback(this::onFinish);
    }

    public MeetingEditorController(Meeting meeting) {
        mode = Mode.EDIT;
        form = new MeetingEditorForm(meeting, Mode.EDIT);
        form.setOnDiscardCallback(this::onDiscard);
        form.setOnCancelDeleteCallback(this::onCancelDelete);
        form.setOnFinishCallback(this::onFinish);
    }

    private void onFinish(MeetingEditorForm.MeetingViewModel meetingViewModel) {
        System.out.println("Finish meeting");
        //save or update meeting
        System.out.println(meetingViewModel);
        if (!form.validateInput()) {
            return;
        }

        //convert to Meeting
        Meeting meeting = new Meeting(meetingViewModel.getMeetingId());
        meeting.setTimestamp(meetingViewModel.getMeetingDateTime());
        meeting.setTitle(meetingViewModel.getMeetingTitle());
        meeting.setTags(meetingViewModel.getTags().stream().map(t -> t.getId()).collect(Collectors.toList()));
        meeting.setAttendees(meetingViewModel.getAttendees().stream().map(p -> p.getId()).collect(Collectors.toList()));
        meeting.setDecisions(meetingViewModel.getDecisions().stream().map(d -> d.getId()).collect(Collectors.toList()));

        try {
            MeetingDAO.getInstance().saveOrUpdate(meeting);
            DecisionDAO.getInstance().deleteDecisionWithLinkedMeeting(meeting.getId());
            meetingViewModel.getDecisions().forEach(d -> {
                try {
                    DecisionDAO.getInstance().saveDecision(d);
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            form, "Unable to save decision linked to meeting - " + e.getMessage());
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(form, e.getMessage());
        }

        closeForm();
    }

    private void onCancelDelete(MeetingEditorForm.MeetingViewModel meetingViewModel) {

        if(mode == Mode.NEW) {
            onDiscard();
            return;
        }

        if(mode == Mode.EDIT) {   //delete meeting
            GuiUtils.ConfirmDialogueWithAction(form, "Really delete meeting?", "Confirm", () -> {
                try {
                    MeetingDAO.getInstance().deleteMeeting(meetingViewModel.getMeetingId());
                    closeForm();
                } catch (SQLException e) {
                    Application.log(e);
                    JOptionPane.showMessageDialog(form, "Could not delete: " + e.getMessage());
                }
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

    public void setOpenMainMenuOnClose(boolean openMainMenuOnClose) {
        this.openMainMenuOnClose = openMainMenuOnClose;
    }

    private void closeForm() {
        form.closeChildWindows();
        form.dispose();
        form = null;
        if(openMainMenuOnClose) {
            MainMenuController.getInstance().displayMainMenu();
        }
    }

}
