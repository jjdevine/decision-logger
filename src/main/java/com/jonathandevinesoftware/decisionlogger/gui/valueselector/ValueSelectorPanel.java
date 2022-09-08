package com.jonathandevinesoftware.decisionlogger.gui.valueselector;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;
import com.jonathandevinesoftware.decisionlogger.gui.utils.JTextFieldChangeHandler;
import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;

public class ValueSelectorPanel extends JPanel implements JTextFieldChangeHandler, ActionListener {

    private JTextField tfAddValue;
    private JPanel valueInputPanel;
    private JPanel bAddPanel;
    private JButton bAdd;
    private JButton bRemove;

    private JScrollPane jspSuggestions;
    private JScrollPane jspSelections;
    private JList<String> lSuggestions;
    private JList<String> lSelections;

    private ReferenceDataSource<Person> dataSource;
    private static final int HEIGHT = 360;

    public ValueSelectorPanel(int width, String type, ReferenceDataSource dataSource) {

        this.dataSource = dataSource;
        GuiConstants.DEBUG = false;

        setBorder(ComponentFactory.createDefaultBorder());
        setPreferredSize(new Dimension(width, HEIGHT));

        int valueInputPanelWidth = width/2 + 70;
        int panelAddWidth = width/2 - 90;
        int bWidth = panelAddWidth - 10;
        int firstRowHeight = 60;

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

        bRemove = ComponentFactory.createJButton(
                "Remove",
                new Dimension(bWidth, 100));
        bRemove.addActionListener(this);

        panelSelections.add(jspSelections);
        panelSelections.add(bRemove);

        init();
    }

    private void init() {
        handleChange(tfAddValue);  //to trigger a search with no query
    }

    @Override
    public void handleChange(JTextField changedTextField) {

        java.util.List<String> values = dataSource.searchValues(changedTextField.getText())
                .stream()
                .map(p -> p.getValue())
                .sorted()
                .collect(Collectors.toList());

        GuiUtils.setJListValues(lSuggestions, values);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == bAdd) {
            dataSource.
            tfAddValue.setText("");
        } else if(e.getSource() == bRemove) {

        }
    }
}
