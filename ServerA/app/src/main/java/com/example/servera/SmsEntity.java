package com.example.servera;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sms_table")
public class SmsEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String sender;
    private String userId;
    private String password;
    private String verificationCode;

    // getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public SmsEntity(String sender, String userId, String password, String verificationCode) {
        this.sender = sender;
        this.userId = userId;
        this.password = password;
        this.verificationCode = verificationCode;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getVerificationCode() {
        return verificationCode;
    }
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

}
