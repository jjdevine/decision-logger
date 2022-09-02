package com.jonathandevinesoftware.decisionlogger.gui.utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class GuiUtils {

    public static void addActionListenerToAll(ActionListener listener, JButton... buttons) {
        Arrays.stream(buttons).forEach(b -> b.addActionListener(listener));
    }

    public static void addTextFieldChangeHandler(JTextField textField, JTextFieldChangeHandler handler) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handler.handleChange(textField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handler.handleChange(textField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handler.handleChange(textField);
            }
        });
    }

    public static void setJListValues(JList jList, List<String> values ) {

        DefaultListModel newDLM = new DefaultListModel();
        newDLM.addAll(values);
        jList.setModel(newDLM);
    }
}
