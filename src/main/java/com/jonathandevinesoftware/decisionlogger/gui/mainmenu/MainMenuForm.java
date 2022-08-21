package com.jonathandevinesoftware.decisionlogger.gui.mainmenu;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;
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
            listener.ifPresent(MainFormListener::onSearchMeetings);

        } else if(e.getSource().equals(bSearchDecisions)) {
            listener.ifPresent(MainFormListener::onSearchDecisions);

        } else if(e.getSource().equals(bAdHocDecision)) {
            listener.ifPresent(MainFormListener::onNewAdHocDecision);

        } else if(e.getSource().equals(bCreateMeeting)) {
            listener.ifPresent(MainFormListener::onNewMeeting);
        }
    }

    public interface MainFormListener {

        void onSearchMeetings();
        void onSearchDecisions();
        void onNewAdHocDecision();
        void onNewMeeting();
    }

    private Optional<MainFormListener> listener = Optional.empty();

    public void setListener(MainFormListener listener) {
        this.listener = Optional.of(listener);
    }
}
