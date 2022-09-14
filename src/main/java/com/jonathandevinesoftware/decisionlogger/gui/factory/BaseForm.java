package com.jonathandevinesoftware.decisionlogger.gui.factory;

import javax.swing.*;
import java.awt.*;
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
    }

    protected abstract void init();

    protected void add(Component... components) {
        Arrays.stream(components).forEach(this::add);
    }

}
