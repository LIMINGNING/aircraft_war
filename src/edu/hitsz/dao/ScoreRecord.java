package edu.hitsz.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 得分记录实体类 (Value Object)
 * 用于存储玩家的得分信息
 * 
 * @author hitsz
 */
public class ScoreRecord {

    private String playerName;
    private int score;
    private LocalDateTime recordTime;

    /**
     * 构造函数 - 当前时间
     * 
     * @param playerName 玩家名称
     * @param score      得分
     */
    public ScoreRecord(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.recordTime = LocalDateTime.now();
    }

    /**
     * 构造函数 - 指定时间
     * 
     * @param playerName 玩家名称
     * @param score      得分
     * @param recordTime 记录时间
     */
    public ScoreRecord(String playerName, int score, LocalDateTime recordTime) {
        this.playerName = playerName;
        this.score = score;
        this.recordTime = recordTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }

    /**
     * 获取格式化的时间字符串
     * 
     * @return 格式化后的时间字符串
     */
    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return recordTime.format(formatter);
    }

    @Override
    public String toString() {
        return "ScoreRecord{" +
                "playerName='" + playerName + '\'' +
                ", score=" + score +
                ", recordTime=" + getFormattedTime() +
                '}';
    }

    /**
     * 将记录转换为文件存储格式
     * 
     * @return 用于文件存储的字符串
     */
    public String toFileString() {
        return playerName + "," + score + "," + recordTime.toString();
    }

    /**
     * 从文件字符串创建记录对象
     * 
     * @param line 文件中的一行数据
     * @return ScoreRecord对象
     */
    public static ScoreRecord fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid file format: " + line);
        }
        String playerName = parts[0];
        int score = Integer.parseInt(parts[1]);
        LocalDateTime recordTime = LocalDateTime.parse(parts[2]);
        return new ScoreRecord(playerName, score, recordTime);
    }
}