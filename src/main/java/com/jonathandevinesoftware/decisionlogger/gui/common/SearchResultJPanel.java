package com.jonathandevinesoftware.decisionlogger.gui.common;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchResultJPanel extends JPanel {

    private SpringLayout sLayout;
    private java.util.List<Component> displayedComponents = new ArrayList<>();
    
    private int panelHeight = 0;

    public SearchResultJPanel() {
        sLayout = new SpringLayout();
        setLayout(sLayout);
    }

    public void addSearchResult(Component comp) {
        int pad = 5;
        if(displayedComponents.size() == 0) {
            //first result
            sLayout.putConstraint(SpringLayout.NORTH, this, pad, SpringLayout.NORTH, comp);
        } else {
            Component previousComponent = displayedComponents.get(displayedComponents.size()-1);
            sLayout.putConstraint(SpringLayout.NORTH, comp, pad, SpringLayout.SOUTH, previousComponent);
        }
        add(comp);

        panelHeight += pad + comp.getPreferredSize().height;
        setPreferredSize(new Dimension(comp.getPreferredSize().width, panelHeight));
        displayedComponents.add(comp);
    }

    public void clear() {
        displayedComponents.forEach(this::remove);
        displayedComponents.clear();
        panelHeight = 0;
        repaint();
        revalidate();
    }
}
