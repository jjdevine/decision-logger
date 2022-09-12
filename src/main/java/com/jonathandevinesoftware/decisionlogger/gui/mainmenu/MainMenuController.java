package com.jonathandevinesoftware.decisionlogger.gui.mainmenu;

import com.jonathandevinesoftware.decisionlogger.gui.newdecision.NewDecisionController;

public class MainMenuController implements MainMenuForm.MainFormListener {

    private MainMenuForm mainMenu;

    public static void main(String[] args) {
        new MainMenuController();
    }

    public MainMenuController() {
        displayMainMenu();
    }

    @Override
    public void onSearchMeetings() {

    }

    @Override
    public void onSearchDecisions() {

    }

    @Override
    public void onNewAdHocDecision() {
        closeMainMenu();
        new NewDecisionController();
    }

    @Override
    public void onNewMeeting() {

    }

    public void displayMainMenu() {
        closeMainMenu();
        mainMenu = new MainMenuForm();
        mainMenu.setListener(this);
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
