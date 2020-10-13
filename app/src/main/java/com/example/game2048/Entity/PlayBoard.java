package com.example.game2048.Entity;

import java.sql.Date;

public class PlayBoard{
    private Long userId;
    private Integer takeTime;
    private Date playTime;
    private String initialBoard;

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

    public String getInitialBoard() {
        return initialBoard;
    }

    public void setInitialBoard(String initialBoard) {
        this.initialBoard = initialBoard;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }
}
