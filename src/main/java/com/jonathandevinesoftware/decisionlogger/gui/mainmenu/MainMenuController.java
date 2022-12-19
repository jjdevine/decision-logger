package com.jonathandevinesoftware.decisionlogger.gui.mainmenu;

import com.jonathandevinesoftware.decisionlogger.gui.newdecision.DecisionEditorController;
import com.jonathandevinesoftware.decisionlogger.gui.searchdecisions.SearchDecisionController;

public class MainMenuController {

    private MainMenuForm mainMenu;

    public static void main(String[] args) {
        new MainMenuController();
    }

    public MainMenuController() {
        displayMainMenu();
    }

    public void onSearchMeetings() {

    }

    public void onSearchDecisions() {
        closeMainMenu();
        new SearchDecisionController();
    }

    public void onNewAdHocDecision() {
        closeMainMenu();
        new DecisionEditorController(null);
    }

    public void onNewMeeting() {

    }

    public void displayMainMenu() {
        closeMainMenu();
        mainMenu = new MainMenuForm();
        mainMenu.setNewAdHocDecisionCallback(this::onNewAdHocDecision);
        mainMenu.setNewMeetingCallback(this::onNewMeeting);
        mainMenu.setSearchDecisionsCallback(this::onSearchDecisions);
        mainMenu.setSearchMeetingsCallback(this::onNewMeeting);
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
