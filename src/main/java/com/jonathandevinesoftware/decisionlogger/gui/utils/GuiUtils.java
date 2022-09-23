package com.jonathandevinesoftware.decisionlogger.gui.utils;

import com.jonathandevinesoftware.decisionlogger.persistence.referencedata.ReferenceData;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
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

    public static void addJListDoubleClickHandler(JList jList, JListDoubleClickHandler handler) {

        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    handler.handleDoubleClick(jList);
                }
            }
        });
    }

    public static DateTimeFormatter getDefaultDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }
}
