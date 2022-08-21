package com.jonathandevinesoftware.decisionlogger.gui.mainmenu;

import com.jonathandevinesoftware.decisionlogger.gui.newdecision.NewDecisionController;

public class MainMenuController implements MainMenuForm.MainFormListener {

    private MainMenuForm mainMenu;

    public static void main(String[] args) {
        new MainMenuController();
    }

    public MainMenuController() {
        mainMenu = new MainMenuForm();
        mainMenu.setListener(this);
    }

    @Override
    public void onSearchMeetings() {

    }

    @Override
    public void onSearchDecisions() {

    }

    @Override
    public void onNewAdHocDecision() {
        mainMenu.dispose();
        new NewDecisionController();
    }

    @Override
    public void onNewMeeting() {

    }
}
