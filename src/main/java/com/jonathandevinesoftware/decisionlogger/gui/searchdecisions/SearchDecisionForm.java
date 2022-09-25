package com.jonathandevinesoftware.decisionlogger.gui.searchdecisions;

import com.jonathandevinesoftware.decisionlogger.gui.common.SearchResultJPanel;
import com.jonathandevinesoftware.decisionlogger.gui.factory.BaseForm;
import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.MainMenuController;
import com.jonathandevinesoftware.decisionlogger.gui.newdecision.PersonDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.newdecision.TagDataSource;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.valueselector.ValueSelectorPanel;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SearchDecisionForm extends BaseForm {

    private ValueSelectorPanel vsDecisionMakers;

    private ValueSelectorPanel vsTags;

    private JButton bSearch, bCollapse;

    private static Dimension dimExpandedFilters = new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 390);
    private static Dimension dimCollapsedFilters = new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 40);
    private static Dimension dimExpandedScrollPane = new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 790);
    private static Dimension dimCollapsedScrollPane = new Dimension(GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH, 440);
    private boolean filtersExpanded = true;
    private JPanel panelFilters;
    private SearchResultJPanel panelSearchResults;

    private int searchResultsDisplayed = 0;

    private JScrollPane jspSearchResults;

    public SearchDecisionForm() {
        super("Search Decisions");
    }

    @Override
    protected void init() {
        setPreferredSize(new Dimension(GuiConstants.DEFAULT_FORM_WIDTH, 960));
        JPanel headerPanel = ComponentFactory.createHeaderPanel(
                "Search Decisions",
                new Dimension(
                        GuiConstants.DEFAULT_FULL_COMPONENT_WIDTH,
                        GuiConstants.DEFAULT_HEADER_HEIGHT));
        add(headerPanel);

        add(ComponentFactory.createJLabel("Filters",16));

        panelFilters = ComponentFactory.createJPanel();
        panelFilters.setPreferredSize(dimExpandedFilters);
        add(panelFilters);

        vsDecisionMakers = new ValueSelectorPanel(
                GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH,
                "Decision Maker",
                PersonDataSource.getInstance());
        panelFilters.add(vsDecisionMakers);

        vsTags = new ValueSelectorPanel(
                GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH,
                "Tag",
                TagDataSource.getInstance());
        panelFilters.add(vsTags);

        Dimension dimButton = new Dimension(GuiConstants.DEFAULT_HALF_COMPONENT_WIDTH, 30);

        bSearch = ComponentFactory.createJButton("Search", dimButton, this::onSearchClick);
        bCollapse = ComponentFactory.createJButton("Collapse", dimButton, this::toggleCollapseExpand);
        panelFilters.add(bSearch);
        panelFilters.add(bCollapse);

        panelSearchResults = ComponentFactory.getSearchResultJPanel();
        jspSearchResults = ComponentFactory.createJScrollPane(panelSearchResults);
        jspSearchResults.setPreferredSize(dimCollapsedScrollPane);

        add(jspSearchResults);
    }

    private Optional<BiConsumer<java.util.List<UUID>, java.util.List<UUID>>> searchCallback
            = Optional.empty();

    public void setSearchCallback(BiConsumer<java.util.List<UUID>, java.util.List<UUID>> callback) {
        searchCallback = Optional.of(callback);
    }

    private void onSearchClick() {
        java.util.List<UUID> decisionMakerIds =
                vsDecisionMakers.getSelectedValues().stream()
                        .map(ReferenceData::getId)
                        .collect(Collectors.toList());

        java.util.List<UUID> tagIds =
                vsTags.getSelectedValues().stream()
                        .map(ReferenceData::getId)
                        .collect(Collectors.toList());

        searchCallback.ifPresent(c -> c.accept(decisionMakerIds, tagIds));
    }

    @Override
    public void closeOperation() {
        dispose();
        MainMenuController.getInstance().displayMainMenu();
        System.exit(0);
    }

    private void toggleCollapseExpand() {
        if(filtersExpanded) {
            panelFilters.remove(vsDecisionMakers);
            panelFilters.remove(vsTags);
            panelFilters.setPreferredSize(dimCollapsedFilters);
            bCollapse.setText("Expand");
            jspSearchResults.setPreferredSize(dimExpandedScrollPane);
            filtersExpanded = false;
        } else {
            panelFilters.remove(bSearch);
            panelFilters.remove(bCollapse);
            panelFilters.add(vsDecisionMakers);
            panelFilters.add(vsTags);
            panelFilters.add(bSearch);
            panelFilters.add(bCollapse);
            bCollapse.setText("Collapse");
            panelFilters.setPreferredSize(dimExpandedFilters);
            jspSearchResults.setPreferredSize(dimCollapsedScrollPane);
            filtersExpanded = true;
        }
        revalidate();
    }

    public void setSearchResults(java.util.List<SearchDecisionResultViewModel> viewModels) {
        panelSearchResults.clear();
        for(SearchDecisionResultViewModel viewModel: viewModels) {
            panelSearchResults.addSearchResult(
                    SearchDecisionResultPanel.buildSearchDecisionResultPanel(
                            viewModel,
                            null,
                            null));
        }
        revalidate();
    }

    public void clearSearchResults() {
        //TODO - implement this and put search results in a scrollpane
    }

    @Override
    protected LayoutManager setupLayout() {
        FlowLayout layout = new FlowLayout();
        layout.setHgap(1);
        layout.setVgap(1);
        return layout;
    }
}

