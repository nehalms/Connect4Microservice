package com.example.Connect4Api.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DbPlayer {

    private String _id;
    private String userId;
    private String userName;
    private FrnRowStats frnRowStats;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public FrnRowStats getFrnRowStats() {
        return frnRowStats;
    }

    public void setFrnRowStats(FrnRowStats frnRowStats) {
        this.frnRowStats = frnRowStats;
    }
}
