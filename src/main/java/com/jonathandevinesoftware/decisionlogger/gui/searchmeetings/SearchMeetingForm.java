package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import com.jonathandevinesoftware.decisionlogger.gui.decision.PersonDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.decision.TagDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;

import javax.swing.*;
import java.awt.*;

public class SearchMeetingForm extends BaseForm {
    
    private static final int FORM_WIDTH = 1500;
    private static final int FULL_COMPONENT_WIDTH = 1470;

    private static final int HALF_COMPONENT_WIDTH = 720;

    private static final int ONE_THIRD_COMPONENT_WIDTH = 470;

    private ValueSelectorPanel vsDecisionMakers;

    private ValueSelectorPanel vsTags;

    private ValueSelectorPanel vsAttendees;

    private JPanel panelFilters;
    private JButton bSearch, bCollapse;


    private static Dimension dimExpandedFilters = new Dimension(FULL_COMPONENT_WIDTH, 390);
    private static Dimension dimCollapsedFilters = new Dimension(FULL_COMPONENT_WIDTH, 40);
    private static Dimension dimExpandedScrollPane = new Dimension(FULL_COMPONENT_WIDTH, 790);
    private static Dimension dimCollapsedScrollPane = new Dimension(FULL_COMPONENT_WIDTH, 440);

    public SearchMeetingForm(String title) {
        super(title);
    }

    @Override
    protected void init() {
        setPreferredSize(new Dimension((int) (FORM_WIDTH), 960));
        JPanel headerPanel = ComponentFactory.createHeaderPanel(
                "Search Meetings",
                new Dimension(
                        (int) (FULL_COMPONENT_WIDTH),
                        GuiConstants.DEFAULT_HEADER_HEIGHT));
        add(headerPanel);

        add(ComponentFactory.createJLabel("Filters",16));

        panelFilters = ComponentFactory.createJPanel();
        panelFilters.setPreferredSize(dimExpandedFilters);
        add(panelFilters);

        vsTags = new ValueSelectorPanel(
                ONE_THIRD_COMPONENT_WIDTH,
                "Attendee",
                PersonDataSource.getInstance());
        panelFilters.add(vsTags);

        vsTags = new ValueSelectorPanel(
                ONE_THIRD_COMPONENT_WIDTH,
                "Tag",
                TagDataSource.getInstance());
        panelFilters.add(vsTags);

        vsDecisionMakers = new ValueSelectorPanel(
                ONE_THIRD_COMPONENT_WIDTH,
                "Decision Maker",
                PersonDataSource.getInstance());
        panelFilters.add(vsDecisionMakers);

        Dimension dimButton = new Dimension(HALF_COMPONENT_WIDTH, 30);

        bSearch = ComponentFactory.createJButton("Search", dimButton, this::onSearchClick);
        bCollapse = ComponentFactory.createJButton("Collapse", dimButton, this::toggleCollapseExpand);
        panelFilters.add(bSearch);
        panelFilters.add(bCollapse);
    }

    private void toggleCollapseExpand() {
    }

    private void onSearchClick() {
    }

    @Override
    public void closeOperation() {
        System.exit(0);
    }
}
