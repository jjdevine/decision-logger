package com.jonathandevinesoftware.decisionlogger.gui.meeting;

import com.jonathandevinesoftware.decisionlogger.gui.common.Mode;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.MainMenuController;
import com.jonathandevinesoftware.decisionlogger.model.DecisionDAO;
import com.jonathandevinesoftware.decisionlogger.model.Meeting;
import com.jonathandevinesoftware.decisionlogger.model.MeetingDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.PersonDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.TagDAO;

import javax.swing.*;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

public class MeetingEditorController {

    private MeetingEditorForm form;
    private boolean openMainMenuOnClose = true;

    public MeetingEditorController() {
        Meeting meeting = new Meeting(UUID.randomUUID());
        form = new MeetingEditorForm(meeting, Mode.NEW);
        form.setOnCancelDeleteCallback(this::onCancelDelete);
        form.setOnFinishCallback(this::onFinish);
    }

    public MeetingEditorController(Meeting meeting) {
        form = new MeetingEditorForm(meeting, Mode.EDIT);
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
        try {
            Meeting meeting = MeetingDAO.getInstance().loadMeeting(meetingViewModel.getMeetingId());
            if(meeting != null) {
               // MeetingDAO.getInstance().deleteMeeting(meeting.getId());
                //TODO - need a way to close those form without making database changes
                //TODO - same for decisions?
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeForm();
        //TODO delete meeting if exists
    }

    public void setOpenMainMenuOnClose(boolean openMainMenuOnClose) {
        this.openMainMenuOnClose = openMainMenuOnClose;
    }

    private void closeForm() {
        form.dispose();
        form = null;
        if(openMainMenuOnClose) {
            MainMenuController.getInstance().displayMainMenu();
        }
    }

}
