package com.jonathandevinesoftware.decisionlogger.gui.meeting;

import com.jonathandevinesoftware.decisionlogger.model.Meeting;

import java.util.UUID;

public class MeetingEditorController {

    private MeetingEditorForm form;

    public MeetingEditorController() {
        Meeting meeting = new Meeting(UUID.randomUUID());
        form = new MeetingEditorForm("New Meeting", meeting);
    }

}
