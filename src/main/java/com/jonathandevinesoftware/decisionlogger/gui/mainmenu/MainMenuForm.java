package com.jonathandevinesoftware.decisionlogger.gui.mainmenu;

import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class MainMenuForm extends BaseForm implements ActionListener {

    private JButton bSearchMeetings, bSearchDecisions, bAdHocDecision, bCreateMeeting;

    public MainMenuForm() {
        super("Decision Logger Menu");
    }

    @Override
    protected void init() {
        Dimension dimButton = new Dimension(200,200);
        bSearchMeetings = ComponentFactory.createJButton("Search Meetings", dimButton);
        bSearchDecisions = ComponentFactory.createJButton("Search Decisions", dimButton);
        bAdHocDecision = ComponentFactory.createJButton("New Ad Hoc Decision", dimButton);
        bCreateMeeting = ComponentFactory.createJButton("Create Meeting", dimButton);

        GuiUtils.addActionListenerToAll(this,
                bSearchMeetings,
                bSearchDecisions,
                bAdHocDecision,
                bCreateMeeting);

        add(bSearchMeetings, bSearchDecisions, bAdHocDecision, bCreateMeeting);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(bSearchMeetings)) {
            searchMeetingsCallback.ifPresent(Runnable::run);

        } else if(e.getSource().equals(bSearchDecisions)) {
            searchDecisionsCallback.ifPresent(Runnable::run);

        } else if(e.getSource().equals(bAdHocDecision)) {
            newAdHocDecisionCallback.ifPresent(Runnable::run);

        } else if(e.getSource().equals(bCreateMeeting)) {
            newMeetingCallback.ifPresent(Runnable::run);
        }
    }

    private Optional<Runnable> searchMeetingsCallback = Optional.empty();
    private Optional<Runnable> searchDecisionsCallback = Optional.empty();
    private Optional<Runnable> newAdHocDecisionCallback = Optional.empty();
    private Optional<Runnable> newMeetingCallback = Optional.empty();

    public void setSearchMeetingsCallback(Runnable searchMeetingsCallback) {
        this.searchMeetingsCallback = Optional.of(searchMeetingsCallback);
    }

    public void setSearchDecisionsCallback(Runnable searchDecisionsCallback) {
        this.searchDecisionsCallback = Optional.of(searchDecisionsCallback);
    }

    public void setNewAdHocDecisionCallback(Runnable newAdHocDecisionCallback) {
        this.newAdHocDecisionCallback = Optional.of(newAdHocDecisionCallback);
    }

    public void setNewMeetingCallback(Runnable newMeetingCallback) {
        this.newMeetingCallback = Optional.of(newMeetingCallback);
    }

    @Override
    public void closeOperation() {
        dispose();
        System.exit(0);
    }
}
