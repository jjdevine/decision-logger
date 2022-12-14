package com.jonathandevinesoftware.decisionlogger.gui.valueselector;

import com.jonathandevinesoftware.decisionlogger.gui.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;
import com.jonathandevinesoftware.decisionlogger.gui.utils.JListDoubleClickHandler;
import com.jonathandevinesoftware.decisionlogger.gui.utils.JTextFieldChangeHandler;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ValueSelectorPanel extends JPanel implements JTextFieldChangeHandler, ActionListener, JListDoubleClickHandler {

    private JTextField tfAddValue;
    private JPanel valueInputPanel;
    private JPanel bAddPanel;

    private JPanel removePanel;
    private JButton bAdd;
    private JButton bRemove;

    private JButton bRemoveAll;
    private JScrollPane jspSuggestions;
    private JScrollPane jspSelections;
    private JList<String> lSuggestions;
    private JList<String> lSelections;
    private java.util.List<ReferenceData> selectedItems;

    private ReferenceDataSource dataSource;
    private static final int HEIGHT = 340;

    public ValueSelectorPanel(int width, String type, ReferenceDataSource dataSource) {

        this.dataSource = dataSource;

        setBorder(ComponentFactory.createDefaultBorder());
        setPreferredSize(new Dimension(width, HEIGHT));

        int valueInputPanelWidth = width/2 + 70;
        int panelAddWidth = width/2 - 90;
        int bWidth = panelAddWidth - 15;
        int firstRowHeight = 60;
        int removePanelWidth = bWidth + 4;

        valueInputPanel = ComponentFactory.createJPanel();
        valueInputPanel.setPreferredSize(new Dimension(valueInputPanelWidth,firstRowHeight));

        tfAddValue = ComponentFactory.createJTextField();
        tfAddValue.setPreferredSize(new Dimension(valueInputPanelWidth - 10, 30));
        GuiUtils.addTextFieldChangeHandler(tfAddValue, this);

        valueInputPanel.add(ComponentFactory.createJLabel("Add " + type));
        valueInputPanel.add(tfAddValue);

        add(valueInputPanel);

        bAddPanel = ComponentFactory.createJPanel();
        bAddPanel.setPreferredSize(new Dimension(panelAddWidth, firstRowHeight));

        bAdd = ComponentFactory.createJButton(
                "Add",
                new Dimension(bWidth, 30));
        bAdd.addActionListener(this);

        bAddPanel.add(ComponentFactory.createDummyJPanel(panelAddWidth-10, 15));
        bAddPanel.add(bAdd);

        add(bAddPanel);

        add(ComponentFactory.createJLabel("Suggestions"));

        lSuggestions = ComponentFactory.createJList();
        GuiUtils.addJListDoubleClickHandler(lSuggestions, this);
        jspSuggestions = ComponentFactory.createJScrollPane(lSuggestions);
        jspSuggestions.setPreferredSize(new Dimension(width-24 , 100));
        add(jspSuggestions);

        add(ComponentFactory.createJLabel("Selected " + type + "(s)"));

        JPanel panelSelections = ComponentFactory.createJPanel();
        panelSelections.setPreferredSize(new Dimension(width-2, 120));
        panelSelections.setLayout(new FlowLayout(FlowLayout.CENTER, 20,5));
        add(panelSelections);

        lSelections = ComponentFactory.createJList();
        jspSelections = ComponentFactory.createJScrollPane(lSelections);
        jspSelections.setPreferredSize(new Dimension(width-179 , 100));

        selectedItems = new ArrayList<>();

        removePanel = ComponentFactory.createJPanelWithMargin(3,1);
        removePanel.setPreferredSize(new Dimension(removePanelWidth, 100));

        bRemove = ComponentFactory.createJButton(
                "Remove",
                new Dimension(bWidth, 45));
        bRemove.addActionListener(this);

        bRemoveAll = ComponentFactory.createJButton(
                "Remove All",
                new Dimension(bWidth, 45));
        bRemoveAll.addActionListener(this);

        removePanel.add(bRemove);
        removePanel.add(bRemoveAll);

        panelSelections.add(jspSelections);
        panelSelections.add(removePanel);

        init();
    }

    private void init() {
        handleChange(tfAddValue);  //to trigger a search with no query
    }

    @Override
    public void handleChange(JTextField changedTextField) {
        refreshSuggestions();
    }

    private void refreshSuggestions() {
        java.util.List<String> values = dataSource.searchValues(tfAddValue.getText())
                .stream()
                .map(p -> p.getValue())
                .filter(s -> !selectedItems.stream().map(ReferenceData::getValue).collect(Collectors.toList()).contains(s)) //where item is already selected
                .sorted()
                .collect(Collectors.toList());

        GuiUtils.setJListValues(lSuggestions, values);
    }

    public void setSelectedValue(UUID valueId) {

        Optional<? extends ReferenceData> selectedValue = dataSource.getById(valueId);
        if(selectedValue.isPresent()) {
            selectedItems.add(selectedValue.get());
            refreshSelectedItemView();
            refreshSuggestions();
        } else {
            System.out.println("Could not find reference data for " + valueId);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == bAdd) {
            processSelection(tfAddValue.getText());
            tfAddValue.setText("");
        } else if(e.getSource() == bRemove) {
            String selectedValue = lSelections.getSelectedValue();
            if(selectedValue != null) {
                selectedItems.removeIf(si -> si.getValue().equals(selectedValue));
                refreshSuggestions();
                refreshSelectedItemView();
            }
        } else if(e.getSource() == bRemoveAll) {
            selectedItems.clear();
            refreshSuggestions();
            refreshSelectedItemView();
        }
    }

    public java.util.List<ReferenceData> getSelectedValues() {
        return selectedItems;
    }

    private void refreshSelectedItemView() {

        System.out.println(selectedItems);

        GuiUtils.setJListValues(lSelections,
                selectedItems.stream().map(ReferenceData::getValue).collect(Collectors.toList()));
    }

    @Override
    public void handleDoubleClick(JList clickedList) {
        if(clickedList == lSuggestions) {
            String selection = lSuggestions.getSelectedValue();
            if(selection != null) {
                processSelection(selection);
                refreshSuggestions();
            }
        }
    }

    private void processSelection(String selection) {
        selection = selection.trim();
        if(selection.length() > 0) {

            //does value already exist?
            ReferenceData referenceData = dataSource.getExactValue(selection);
            if(referenceData == null) {
                referenceData = dataSource.constructInstance(UUID.randomUUID(), selection);
                dataSource.addValue(referenceData);
            }
            selectedItems.add(referenceData);
            refreshSelectedItemView();
        }
    }
}
