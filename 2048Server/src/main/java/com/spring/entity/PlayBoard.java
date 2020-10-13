package com.spring.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "play_board")
@IdClass(value = PlayBoardIdClass.class)
public class PlayBoard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "take_time")
    private Integer takeTime;

    @Id
    @Column(name = "play_time")
    private Date playTime;

    @Column(name = "initial_board")
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

