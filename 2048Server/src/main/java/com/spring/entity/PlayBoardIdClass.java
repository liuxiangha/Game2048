package com.spring.entity;

import java.io.Serializable;
import java.sql.Date;

public class PlayBoardIdClass implements Serializable {
    private Long userId;
    private Integer takeTime;
    private Date playTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Integer takeTime) {
        this.takeTime = takeTime;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }
}
