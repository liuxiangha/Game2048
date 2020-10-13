package com.spring.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "play_2048")
@IdClass(Play2048IdClass.class)
public class Play2048 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "score")
    private Integer score;

    @Id
    @Column(name = "play_time")
    private Date playTime;

    @Column(name = "final_result")
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
