package com.luckyirchucky.ui.outputdata.reference;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ReferenceOutputDataWindow extends JFrame {

    public ReferenceOutputDataWindow(){
        JLabel title = new JLabel("Справка о программе");
        Font font = new Font("System", Font.PLAIN, 12);
        title.setFont(font);
        this.setTitle(title.getText());
        this.setMinimumSize(new Dimension(800, 50));
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 1", "[grow]", "[grow][]"));
        JLabel reference = new JLabel("Программное обеспечение для оценки состояния движущегося летательного аппарата в воздушном пространстве");
        reference.setFont(font);
        panel.add(reference, "wrap");
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }
}
