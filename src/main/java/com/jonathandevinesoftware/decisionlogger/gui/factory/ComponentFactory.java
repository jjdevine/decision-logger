package com.jonathandevinesoftware.decisionlogger.gui.factory;

import javax.swing.*;
import java.awt.*;

public class ComponentFactory {

    public static JButton createJButton(String title, Dimension d) {
        JButton button = new JButton(title);
        button.setPreferredSize(d);
        return button;
    }
}
