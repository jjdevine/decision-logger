package com.jonathandevinesoftware.decisionlogger.gui.utils;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GuiUtils {

    public static void addActionListenerToAll(ActionListener listener, JButton... buttons) {
        Arrays.stream(buttons).forEach(b -> b.addActionListener(listener));
    }
}
