package edu.hitsz.dao;

import java.util.List;

/**
 * 得分数据访问对象接口 (DAO Interface)
 * 定义了对得分记录的基本操作
 * 
 * @author hitsz
 */
public interface ScoreDao {

    void updateScore(ScoreRecord record);

    List<ScoreRecord> getAllScores();

    List<ScoreRecord> getTopScores(int n);

    boolean deleteScore(int index);
    
    void clearAllScores();
}