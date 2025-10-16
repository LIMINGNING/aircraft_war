package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
                // 启动简单模式
                SimpleGame game = new SimpleGame();
                Main.cardPanel.add(game, "simple");
                Main.cardLayout.show(Main.cardPanel, "simple");
                game.action();
            }
        });
        middleModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 启动中等模式
                MiddleGame game = new MiddleGame();
                Main.cardPanel.add(game, "middle");
                Main.cardLayout.show(Main.cardPanel, "middle");
                game.action();
            }
        });
        hardModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 启动困难模式
                HardGame game = new HardGame();
                Main.cardPanel.add(game, "hard");
                Main.cardLayout.show(Main.cardPanel, "hard");
                game.action();
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
