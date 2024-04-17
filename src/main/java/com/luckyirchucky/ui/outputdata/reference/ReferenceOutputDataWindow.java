package com.luckyirchucky.ui.outputdata.reference;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ReferenceOutputDataWindow extends JFrame {

    private static Font font = new Font("System", Font.PLAIN, 12);

    public ReferenceOutputDataWindow(){
        JLabel title = new JLabel("Справка о программе");
        title.setFont(font);
        this.setTitle(title.getText());
        this.setMinimumSize(new Dimension(800, 50));
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 1", "[grow]", "[grow][]"));
        JLabel reference = new JLabel("Программное обеспечение для оценки состояния движущегося летательного аппарата в воздушном пространстве");
        panel.add(reference, "wrap");
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }
}
