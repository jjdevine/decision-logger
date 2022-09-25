package com.jonathandevinesoftware.decisionlogger.gui.factory;

import com.jonathandevinesoftware.decisionlogger.core.Application;
import com.jonathandevinesoftware.decisionlogger.gui.common.SearchResultJPanel;
import com.jonathandevinesoftware.decisionlogger.gui.utils.GuiConstants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ComponentFactory {

    public static Font getHeaderFont(int size) {
       return new Font("Arial", Font.BOLD, size);
    }

    public static Font getStandardFont(int size) {
        return new Font("Arial", Font.BOLD, size);
    }

    public static Font getItalicFont(int size) {
        return new Font("Arial", Font.ITALIC, size);
    }

    public static JButton createJButton(String title, Dimension d) {
        JButton button = new JButton(title);
        button.setPreferredSize(d);
        return button;
    }

    public static JButton createJButton(String title, Dimension d, Runnable onClick) {
        JButton button = new JButton(title);
        button.setPreferredSize(d);
        if(onClick == null) {
            button.setEnabled(false);
        } else {
            button.addActionListener(e -> onClick.run());
        }
        return button;
    }

    public static JPanel createHeaderPanel(String header, Dimension size) {
        JPanel panel = createJPanel();
        panel.setPreferredSize(size);
        panel.setBorder(new LineBorder(Color.BLACK));

        JLabel label = createJLabel(header);
        label.setPreferredSize(new Dimension(size.width-10, size.height-10));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(getHeaderFont(16));
        panel.add(label);
        return panel;
    }

    public static Border createDefaultBorder() {
        return new LineBorder(Color.BLACK);
    }

    public static JLabel createJLabel(String text) {
        return new JLabel (text);
    }

    public static JLabel createJLabel(String text, int fontSize) {
        JLabel label = createJLabel(text);
        label.setFont(getStandardFont(fontSize));
        return label;
    }

    public static JPanel createJPanel() {
        JPanel panel = new JPanel();
        if(GuiConstants.DEBUG) {
            panel.setBorder(new LineBorder(Color.RED));
            Application.debug("border is red");
        }
        return panel;
    }

    public static JPanel createJPanelWithMargin(int verticalMargin, int horizontalMargin) {
        JPanel panel = createJPanel();
        FlowLayout layout = new FlowLayout();
        layout.setVgap(verticalMargin);
        layout.setHgap(horizontalMargin);
        panel.setLayout(layout);
        return panel;
    }

    public static JPanel createDummyJPanel(int width, int height) {
        JPanel panel = createJPanel();
        panel.setPreferredSize(new Dimension(width, height));
        return panel;
    }

    public static JTextArea createJTextArea() {
        JTextArea ta = new JTextArea();
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    public static JTextField createJTextField() {
        return new JTextField();
    }

    public static JScrollPane createJScrollPane(Component component) {
        JScrollPane jsp = new JScrollPane(component);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return jsp;
    }

    public static JList<String> createJList() {
        return new JList<>();
    }

    public static FlowLayout getFlowLayoutWithMargin(int horizontal, int vertical) {
        FlowLayout layout = new FlowLayout();
        layout.setHgap(horizontal);
        layout.setVgap(vertical);
        return layout;
    }

    public static SearchResultJPanel getSearchResultJPanel() {
        return new SearchResultJPanel();
    }

}

