package com.jonathandevinesoftware.decisionlogger.gui.newdecision;

public class DecisionMakerDAO {

    private static DecisionMakerDAO instance;

    public static DecisionMakerDAO getInstance() {
        if (instance == null) {
            instance = new DecisionMakerDAO();
        }
        return instance;
    }

    public boolean addDecisionMaker(String name) {

    }
}
