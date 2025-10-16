package edu.hitsz.application;

import edu.hitsz.dao.ScoreDao;
import edu.hitsz.dao.ScoreDaoImpl;
import edu.hitsz.dao.ScoreRecord;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 * 游戏结束界面
 * 显示得分排行榜，允许用户输入姓名并删除记录
 * 
 * @author hitsz
 */
public class GameOverScreen {
    
    private JPanel mainPanel;
    private JLabel scoreLabel;
    private JLabel difficultyLabel;
    private JTextField nameTextField;
    private JButton saveScoreButton;
    private JButton deleteRecordButton;
    private JButton returnToMenuButton;
    private JTable scoreTable;
    private JScrollPane scrollPane;
    
    private DefaultTableModel tableModel;
    private ScoreDao scoreDao;
    private int finalScore;
    private String difficulty;
    
    /**
     * 构造函数
     * @param finalScore 最终得分
     * @param difficulty 游戏难度
     * @param scoreFileName 得分文件名
     */
    public GameOverScreen(int finalScore, String difficulty, String scoreFileName) {
        this.finalScore = finalScore;
        this.difficulty = difficulty;
        this.scoreDao = new ScoreDaoImpl(scoreFileName);
        
        initComponents();
        setupListeners();
        loadScoreTable();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        // 更新界面文本
        difficultyLabel.setText("难度: " + difficulty);
        scoreLabel.setText("您的得分: " + finalScore);
        
        // 初始化表格
        String[] columnNames = {"排名", "玩家姓名", "得分", "记录时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        scoreTable.setModel(tableModel);
        
        // 设置列宽
        if (scoreTable.getColumnModel().getColumnCount() >= 4) {
            scoreTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // 排名
            scoreTable.getColumnModel().getColumn(1).setPreferredWidth(120); // 玩家姓名
            scoreTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // 得分
            scoreTable.getColumnModel().getColumn(3).setPreferredWidth(150); // 记录时间
        }
    }
    
    /**
     * 设置事件监听器
     */
    private void setupListeners() {
        // 姓名输入框焦点监听器
        nameTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameTextField.getText().equals("请输入您的姓名")) {
                    nameTextField.setText("");
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (nameTextField.getText().isEmpty()) {
                    nameTextField.setText("请输入您的姓名");
                }
            }
        });
        
        // 保存得分按钮
        saveScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveScore();
            }
        });
        
        // 删除记录按钮
        deleteRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRecord();
            }
        });
        
        // 姓名输入框回车事件
        nameTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveScore();
            }
        });
    }
    
    /**
     * 加载得分表格
     */
    private void loadScoreTable() {
        // 清空表格
        tableModel.setRowCount(0);
        
        // 获取所有得分记录
        List<ScoreRecord> allScores = scoreDao.getAllScores();
        
        // 添加数据到表格
        for (int i = 0; i < allScores.size() && i < 20; i++) { // 最多显示前20名
            ScoreRecord record = allScores.get(i);
            Object[] rowData = {
                i + 1,                           // 排名
                record.getPlayerName(),          // 玩家姓名
                record.getScore(),               // 得分
                record.getFormattedTime()        // 记录时间
            };
            tableModel.addRow(rowData);
        }
    }
    
    /**
     * 保存得分
     */
    private void saveScore() {
        String playerName = nameTextField.getText().trim();
        
        // 验证输入
        if (playerName.isEmpty() || playerName.equals("请输入您的姓名")) {
            JOptionPane.showMessageDialog(mainPanel, 
                "请输入有效的玩家姓名！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (playerName.length() > 15) {
            JOptionPane.showMessageDialog(mainPanel, 
                "玩家姓名不能超过15个字符！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 保存得分记录
        ScoreRecord record = new ScoreRecord(playerName, finalScore);
        scoreDao.updateScore(record);
        
        // 刷新表格
        loadScoreTable();
        
        // 清空输入框
        nameTextField.setText("请输入您的姓名");
        
        // 显示成功消息
        JOptionPane.showMessageDialog(mainPanel, 
            "得分已保存！", 
            "保存成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 删除选中的记录
     */
    private void deleteSelectedRecord() {
        int selectedRow = scoreTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainPanel, 
                "请先选择要删除的记录！", 
                "未选择记录", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 确认删除
        int confirm = JOptionPane.showConfirmDialog(mainPanel, 
            "确定要删除这条记录吗？", 
            "确认删除", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 删除记录
            boolean success = scoreDao.deleteScore(selectedRow);
            
            if (success) {
                // 刷新表格
                loadScoreTable();
                JOptionPane.showMessageDialog(mainPanel, 
                    "记录已删除！", 
                    "删除成功", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainPanel, 
                    "删除失败！", 
                    "删除错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 获取主面板
     * @return 主面板
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}