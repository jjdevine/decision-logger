package com.jonathandevinesoftware.decisionlogger.gui.valueselector;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiUtils;
import com.jonathandevinesoftware.decisionlogger.gui.utils.JTextFieldChangeHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ValueSelectorPanel extends JPanel implements JTextFieldChangeHandler {

    private JTextField tfAddValue;
    private JPanel valueInputPanel;
    private JPanel bAddPanel;
    private JButton bAdd;
    private JButton bRemove;

    private JScrollPane jspSuggestions;
    private JScrollPane jspSelections;
    private JList<String> lSuggestions;
    private JList<String> lSelections;

    private ValueDataSource dataSource;
    private static final int HEIGHT = 360;

    public ValueSelectorPanel(int width, String type, ValueDataSource dataSource) {

        this.dataSource = dataSource;
        GuiConstants.DEBUG = false;

        setBorder(ComponentFactory.createDefaultBorder());
        setPreferredSize(new Dimension(width, HEIGHT));

        int valueInputPanelWidth = width/2 + 70;
        int bAddPanelWidth = width/2 - 90;
        int bAddWidth = bAddPanelWidth - 10;
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
        bAddPanel.setPreferredSize(new Dimension(bAddPanelWidth, firstRowHeight));

        bAdd = ComponentFactory.createJButton(
                "Add",
                new Dimension(bAddWidth, 30));

        bAddPanel.add(ComponentFactory.createDummyJPanel(bAddPanelWidth-10, 15));
        bAddPanel.add(bAdd);

        add(bAddPanel);

        add(ComponentFactory.createJLabel("Suggestions"));

        lSuggestions = ComponentFactory.createJList();
        jspSuggestions = ComponentFactory.createJScrollPane(lSuggestions);
        jspSuggestions.setPreferredSize(new Dimension(width-24 , 100));
        add(jspSuggestions);



    }

    @Override
    public void handleChange(JTextField changedTextField) {
        System.out.println(dataSource.searchValues(changedTextField.getText()));

        GuiUtils.setJListValues(lSuggestions, dataSource.searchValues(changedTextField.getText()));
    }
}
