package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import com.jonathandevinesoftware.decisionlogger.gui.common.SearchResultJPanel;
import com.jonathandevinesoftware.decisionlogger.gui.decision.PersonDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.decision.TagDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    private SearchResultJPanel panelSearchResults;

    private JScrollPane jspSearchResults;

    private final static Dimension dimExpandedFilters = new Dimension(FULL_COMPONENT_WIDTH, 390);
    private final static Dimension dimCollapsedFilters = new Dimension(FULL_COMPONENT_WIDTH, 40);
    private final static Dimension dimExpandedScrollPane = new Dimension(FULL_COMPONENT_WIDTH, 780);
    private final static Dimension dimCollapsedScrollPane = new Dimension(FULL_COMPONENT_WIDTH, 430);

    private boolean filtersExpanded = true;

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

        vsAttendees = new ValueSelectorPanel(
                ONE_THIRD_COMPONENT_WIDTH,
                "Attendee",
                PersonDataSource.getInstance());
        panelFilters.add(vsAttendees);

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

        panelSearchResults = ComponentFactory.getSearchResultJPanel();
        jspSearchResults = ComponentFactory.createJScrollPane(panelSearchResults);
        jspSearchResults.setPreferredSize(dimCollapsedScrollPane);

        add(jspSearchResults);
    }

    private void toggleCollapseExpand() {
        if(filtersExpanded) {
            panelFilters.remove(vsAttendees);
            panelFilters.remove(vsTags);
            panelFilters.remove(vsDecisionMakers);
            panelFilters.setPreferredSize(dimCollapsedFilters);
            bCollapse.setText("Expand");
            jspSearchResults.setPreferredSize(dimExpandedScrollPane);
            filtersExpanded = false;
        } else {
            panelFilters.remove(bSearch);
            panelFilters.remove(bCollapse);
            panelFilters.add(vsAttendees);
            panelFilters.add(vsTags);
            panelFilters.add(vsDecisionMakers);
            panelFilters.add(bSearch);
            panelFilters.add(bCollapse);
            bCollapse.setText("Collapse");
            panelFilters.setPreferredSize(dimExpandedFilters);
            jspSearchResults.setPreferredSize(dimCollapsedScrollPane);
            filtersExpanded = true;
        }
        revalidate();
    }

    private Optional<Consumer<SearchParameters>> searchCallback = Optional.empty();

    public void setSearchCallback(Consumer<SearchParameters> callback) {
        searchCallback = Optional.of(callback);
    }

    private void onSearchClick() {
        SearchParameters searchParameters = new SearchParameters();

        java.util.List<UUID> attendeeIds =
                vsAttendees.getSelectedValues().stream()
                        .map(ReferenceData::getId)
                        .collect(Collectors.toList());

        java.util.List<UUID> tagIds =
                vsTags.getSelectedValues().stream()
                        .map(ReferenceData::getId)
                        .collect(Collectors.toList());

        java.util.List<UUID> decisionMakerIds =
                vsDecisionMakers.getSelectedValues().stream()
                        .map(ReferenceData::getId)
                        .collect(Collectors.toList());

        searchParameters.setAttendeeIds(attendeeIds);
        searchParameters.setTagIds(tagIds);
        searchParameters.setDecisionMakerIds(decisionMakerIds);

        searchCallback.ifPresent(c -> c.accept(searchParameters));
    }

    @Override
    public void closeOperation() {
        System.exit(0);
    }
}
