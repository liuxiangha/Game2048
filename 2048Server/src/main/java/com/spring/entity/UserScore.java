package com.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "play_score")
public class UserScore {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "max_score")
    private Integer maxScore;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        checkParameter(userId);
        this.userId = userId;    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        checkParameter(maxScore);
        this.maxScore = maxScore;
    }

    private void checkParameter(Long parameter) {
        if (parameter <= Long.MIN_VALUE || parameter >= Long.MAX_VALUE) {
            throw new IllegalArgumentException("User Score userId parameter error.");
        }
    }

    private void checkParameter(Integer parameter) {
        if (parameter <= Integer.MIN_VALUE || parameter >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("User Score maxScore parameter error.");
        }
    }
}
