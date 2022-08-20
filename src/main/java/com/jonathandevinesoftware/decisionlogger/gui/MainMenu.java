package com.jonathandevinesoftware.decisionlogger.gui;

import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends BaseForm {

    private static final int WIDTH = 450;
    private static final int HEIGHT = 460;

    private JButton bSearchMeetings, bSearchDecisions, bAdHocDecision, bCreateMeeting;

    public MainMenu() {
        super("Decision Logger Menu");
    }

    public static void main(String[] args) {
        new MainMenu();
    }

    @Override
    protected void init() {
        Dimension dimButton = new Dimension(200,200);
        bSearchMeetings = ComponentFactory.createJButton("Search Meetings", dimButton);
        bSearchDecisions = ComponentFactory.createJButton("Search Decisions", dimButton);
        bAdHocDecision = ComponentFactory.createJButton("New Ad Hoc Decision", dimButton);
        bCreateMeeting = ComponentFactory.createJButton("Create Meeting", dimButton);

        add(bSearchMeetings, bSearchDecisions, bAdHocDecision, bCreateMeeting);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public interface MainFormListener {

        void onSearchMeetings();
        void onSearchDecisions();
        void onNewAdHocDecision();
        void onNewMeeting();
    }

    public void addListener(MainFormListener listener) {
        //TODO this
    }
}
