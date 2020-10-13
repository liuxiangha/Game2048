package com.example.game2048.Entity;

public class PlayUser {
    private Long userId;
    private String userPassword;
    private String userName;
    private String userGender;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        checkParameter(userId);
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        checkParameter(userPassword);
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        checkParameter(userName);
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        checkParameter(userGender);
        this.userGender = userGender;
    }

    private void checkParameter(String parameter) {
        if (parameter == null || parameter.trim().length() == 0) {
            throw new IllegalArgumentException("Play user parameter error.");
        }
    }

    private void checkParameter(Long parameter) {
        if (parameter <= Long.MIN_VALUE || parameter >= Long.MAX_VALUE) {
            throw new IllegalArgumentException("Play user id parameter error.");
        }
    }
}
