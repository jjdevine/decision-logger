package com.jonathandevinesoftware.decisionlogger.gui.mainmenu.factory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ComponentFactory {

    public static Font getHeaderFont(int size) {
       return new Font("Comic Sans MS", Font.BOLD, size);
    }

    public static JButton createJButton(String title, Dimension d) {
        JButton button = new JButton(title);
        button.setPreferredSize(d);
        return button;
    }

    public static JPanel createHeaderPanel(String header) {
        JPanel panel = createJPanel();
        panel.setBorder(new LineBorder(Color.BLACK));

        JLabel label = createJLabel(header);
        panel.add(label);
        return panel;
    }

    public static JLabel createJLabel(String text) {
        return new JLabel (text);
    }

    public static JPanel createJPanel() {
        return new JPanel();
    }

}
