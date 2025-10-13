package edu.hitsz.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 得分数据访问对象实现类 (DAO Implementation)
 * 使用文件系统进行数据持久化
 * 
 * @author hitsz
 */
public class ScoreDaoImpl implements ScoreDao {

    private static final String SCORE_FILE_PATH = "scores.txt";
    private List<ScoreRecord> scoreRecords;

    /**
     * 构造函数，初始化并从文件加载数据
     */
    public ScoreDaoImpl() {
        scoreRecords = new ArrayList<>();
        loadScoresFromFile();
    }

    @Override
    public void updateScore(ScoreRecord record) {
        scoreRecords.add(record);
        // 按得分降序排序
        scoreRecords.sort(Comparator.comparingInt(ScoreRecord::getScore).reversed());
        saveScoresToFile();
    }

    @Override
    public List<ScoreRecord> getAllScores() {
        return new ArrayList<>(scoreRecords);
    }

    @Override
    public boolean deleteScore(int index) {
        if (index < 0 || index >= scoreRecords.size()) {
            return false;
        }
        scoreRecords.remove(index);
        saveScoresToFile();
        return true;
    }

    @Override
    public List<ScoreRecord> getTopScores(int n) {
        int size = Math.min(n, scoreRecords.size());
        return new ArrayList<>(scoreRecords.subList(0, size));
    }

    @Override
    public void clearAllScores() {
        scoreRecords.clear();
        saveScoresToFile();
    }

    /**
     * 从文件加载得分记录
     */
    private void loadScoresFromFile() {
        File file = new File(SCORE_FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    ScoreRecord record = ScoreRecord.fromFileString(line);
                    scoreRecords.add(record);
                } catch (Exception e) {
                    System.err.println("Error parsing score record: " + line);
                }
            }
            // 加载后按得分降序排序
            scoreRecords.sort(Comparator.comparingInt(ScoreRecord::getScore).reversed());
        } catch (IOException e) {
            System.err.println("Error loading scores from file: " + e.getMessage());
        }
    }

    /**
     * 将得分记录保存到文件
     */
    private void saveScoresToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE_PATH))) {
            for (ScoreRecord record : scoreRecords) {
                writer.write(record.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving scores to file: " + e.getMessage());
        }
    }
}