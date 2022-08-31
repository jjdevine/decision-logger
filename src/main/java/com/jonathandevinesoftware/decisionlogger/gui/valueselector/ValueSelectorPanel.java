package com.jonathandevinesoftware.decisionlogger.gui.valueselector;

import com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory.ComponentFactory;

import javax.swing.*;
import java.awt.*;

public class ValueSelectorPanel extends JPanel {

    private JTextField tfAddValue;
    private JButton bAdd;
    private JButton bRemove;
    private JList<String> lSuggestions;
    private JList<String> lSelections;
    private static final int HEIGHT = 360;

    public ValueSelectorPanel(int width, String type) {
        setBorder(ComponentFactory.createDefaultBorder());
        setPreferredSize(new Dimension(width, HEIGHT));

        add(ComponentFactory.createJLabel("Add " + type));
        tfAddValue = ComponentFactory.createJTextField();
        tfAddValue.setPreferredSize(new Dimension(width/2, 50));
        add(tfAddValue);
    }
}
