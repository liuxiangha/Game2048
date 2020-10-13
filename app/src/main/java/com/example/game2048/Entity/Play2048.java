package com.example.game2048.Entity;

import java.sql.Date;

public class Play2048 {
    private Long userId;

    private Integer score;

    private Date playTime;

    private String finialResult;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFinialResult() {
        return finialResult;
    }

    public void setFinialResult(String finialResult) {
        this.finialResult = finialResult;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }
}
