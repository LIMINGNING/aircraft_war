package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static final CardLayout cardLayout = new CardLayout();
    public static final JPanel cardPanel = new JPanel(cardLayout);

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 提前预加载图片资源，避免首次点击模式时在EDT上卡顿
        try {
            Class.forName("edu.hitsz.application.ImageManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // 获得屏幕的分辨率，初始化 Frame
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            JFrame frame = new JFrame("Aircraft War");
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setResizable(false);
            //设置窗口的大小和位置,居中放置
            frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                    WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 使用卡片布局承载菜单与游戏界面
            frame.setContentPane(cardPanel);

            StartMenu start = new StartMenu();
            cardPanel.add(start.getMainPanel(), "menu");
            cardLayout.show(cardPanel, "menu");

            frame.setVisible(true);
        });
    }
}
