package com.jonathandevinesoftware.decisionlogger.gui.factory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public abstract class BaseForm extends JFrame {

    public BaseForm(String title) {
        super(title);

        getContentPane().setLayout(new FlowLayout());

        init();
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension formSize = getSize();

        setLocation(
                (screenSize.width-formSize.width)/2,
                (screenSize.height-formSize.height)/2);	//centre form

        setVisible(true);//display screen
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeOperation();
            }
        });
    }

    protected abstract void init();

    protected void add(Component... components) {
        Arrays.stream(components).forEach(this::add);
    }

    public abstract void closeOperation();
}
