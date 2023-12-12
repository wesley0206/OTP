package com.example.servera;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SmsDao {

    @Insert
    void insert(SmsEntity smsEntity);

    @Query("SELECT * FROM sms_table ORDER BY id DESC LIMIT 1")
    LiveData<SmsEntity> getLatestSms();

    @Query("SELECT * FROM sms_table ORDER BY id DESC LIMIT 1")
    SmsEntity getLatestSmsSync();

    @Query("SELECT * FROM sms_table WHERE sender = :phoneNumber LIMIT 1")
    SmsEntity getUserByPhoneNumber(String phoneNumber);

    @Update
    void updateVerificationCode(SmsEntity smsEntity);
    // 可以添加其他的資料庫操作方法
}