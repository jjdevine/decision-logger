package com.jonathandevinesoftware.decisionlogger.gui.mainmenu;

import com.jonathandevinesoftware.decisionlogger.gui.decision.DecisionEditorController;
import com.jonathandevinesoftware.decisionlogger.gui.meeting.MeetingEditorController;
import com.jonathandevinesoftware.decisionlogger.gui.searchdecisions.SearchDecisionController;
import com.jonathandevinesoftware.decisionlogger.gui.searchmeetings.SearchMeetingController;
import com.jonathandevinesoftware.decisionlogger.model.Decision;

import java.util.UUID;

public class MainMenuController {

    private MainMenuForm mainMenu;

    public static void main(String[] args) {
        new MainMenuController();
    }

    public MainMenuController() {
        displayMainMenu();
    }

    public void onSearchMeetings() {
        closeMainMenu();
        new SearchMeetingController();
    }

    public void onSearchDecisions() {
        closeMainMenu();
        new SearchDecisionController();
    }

    public void onNewAdHocDecision() {
        closeMainMenu();
        Decision decision = new Decision(UUID.randomUUID());
        new DecisionEditorController(decision);
    }

    public void onNewMeeting() {
        closeMainMenu();
        new MeetingEditorController();
    }

    public void displayMainMenu() {
        closeMainMenu();
        mainMenu = new MainMenuForm();
        mainMenu.setNewAdHocDecisionCallback(this::onNewAdHocDecision);
        mainMenu.setNewMeetingCallback(this::onNewMeeting);
        mainMenu.setSearchDecisionsCallback(this::onSearchDecisions);
        mainMenu.setSearchMeetingsCallback(this::onSearchMeetings);
    }

    private void closeMainMenu() {
        if(mainMenu != null) {
            mainMenu.dispose();
            mainMenu = null;
        }
    }

    private static MainMenuController instance;

    public static MainMenuController getInstance() {
        if(instance == null) {
            instance = new MainMenuController();
        }
        return instance;
    }


}
