package edu.hitsz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import edu.hitsz.application.Game;
import edu.hitsz.application.Main;

public class StartMenu {
    private JButton simpleModeButton;
    private JButton middleModeButton;
    private JButton hardModeButton;
    private JCheckBox musicCheckBox;
    private JPanel mainPanel;


    public StartMenu() {
        simpleModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.cardPanel.add(new Game());
                Main.cardLayout.last(Main.cardPanel);
            }
        });
        middleModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.cardPanel.add(new Game());
                Main.cardLayout.last(Main.cardPanel);
            }
        });
        hardModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.cardPanel.add(new Game());
                Main.cardLayout.last(Main.cardPanel);
            }
        });
        musicCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
