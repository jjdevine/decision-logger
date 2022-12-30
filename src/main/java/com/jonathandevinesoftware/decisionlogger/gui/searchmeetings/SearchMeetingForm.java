package com.jonathandevinesoftware.decisionlogger.gui.searchmeetings;

import com.jonathandevinesoftware.decisionlogger.core.Application;
import com.jonathandevinesoftware.decisionlogger.core.ApplicationConstants;
import com.jonathandevinesoftware.decisionlogger.gui.common.SearchResultJPanel;
import com.jonathandevinesoftware.decisionlogger.gui.decision.PersonDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.decision.TagDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.meeting.MeetingEditorController;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;
import com.jonathandevinesoftware.decisionlogger.model.MeetingDAO;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SearchMeetingForm extends BaseForm {
    


    private ValueSelectorPanel vsDecisionMakers;

    private ValueSelectorPanel vsTags;

    private ValueSelectorPanel vsAttendees;

    private JPanel panelFilters;
    private JButton bSearch, bCollapse;

    private SearchResultJPanel panelSearchResults;

    private JScrollPane jspSearchResults;

    private final static Dimension dimExpandedFilters = new Dimension(SearchMeetingFormConstants.FULL_COMPONENT_WIDTH, 390);
    private final static Dimension dimCollapsedFilters = new Dimension(SearchMeetingFormConstants.FULL_COMPONENT_WIDTH, 40);
    private final static Dimension dimExpandedScrollPane = new Dimension(SearchMeetingFormConstants.FULL_COMPONENT_WIDTH, 780);
    private final static Dimension dimCollapsedScrollPane = new Dimension(SearchMeetingFormConstants.FULL_COMPONENT_WIDTH, 430);

    private boolean filtersExpanded = true;

    public SearchMeetingForm(String title) {
        super(title);
    }

    @Override
    protected void init() {
        setPreferredSize(new Dimension((int) (SearchMeetingFormConstants.FORM_WIDTH), 960));
        JPanel headerPanel = ComponentFactory.createHeaderPanel(
                "Search Meetings",
                new Dimension(
                        (int) (SearchMeetingFormConstants.FULL_COMPONENT_WIDTH),
                        GuiConstants.DEFAULT_HEADER_HEIGHT));
        add(headerPanel);

        add(ComponentFactory.createJLabel("Filters",16));

        panelFilters = ComponentFactory.createJPanel();
        panelFilters.setPreferredSize(dimExpandedFilters);
        add(panelFilters);

        vsAttendees = new ValueSelectorPanel(
                SearchMeetingFormConstants.ONE_THIRD_COMPONENT_WIDTH,
                "Attendee",
                PersonDataSource.getInstance());
        panelFilters.add(vsAttendees);

        vsTags = new ValueSelectorPanel(
                SearchMeetingFormConstants.ONE_THIRD_COMPONENT_WIDTH,
                "Tag",
                TagDataSource.getInstance());
        panelFilters.add(vsTags);

        vsDecisionMakers = new ValueSelectorPanel(
                SearchMeetingFormConstants.ONE_THIRD_COMPONENT_WIDTH,
                "Decision Maker",
                PersonDataSource.getInstance());
        panelFilters.add(vsDecisionMakers);

        Dimension dimButton = new Dimension(SearchMeetingFormConstants.HALF_COMPONENT_WIDTH, 30);

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

    public void setSearchResults(List<SearchMeetingResultViewModel> viewModels) {
        if(ApplicationConstants.DEBUG) {
            System.out.println("Search results: ");
            viewModels.forEach(System.out::println);
        }
        panelSearchResults.clear();
        for(SearchMeetingResultViewModel viewModel: viewModels) {
            panelSearchResults.addSearchResult(
                    SearchMeetingResultPanel.buildSearchDecisionResultPanel(viewModel, () -> {
                        try {
                            new MeetingEditorController(MeetingDAO.getInstance().loadMeeting(viewModel.getMeetingId())).setOpenMainMenuOnClose(false);
                        } catch (Exception ex) {
                            Application.log(ex);
                            JOptionPane.showMessageDialog(this, "Unable to open meeting: " + ex.getMessage());
                        }
                    })
            );
        }
        revalidate();
    }
}
