package com.spring.entity;

import java.io.Serializable;
import java.sql.Date;

public class Play2048IdClass implements Serializable {
    private Long    userId;
    private Integer score;
    private Date    playTime;

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

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }
}
