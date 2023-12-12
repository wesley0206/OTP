package com.example.servera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.room.Room;
import androidx.lifecycle.MutableLiveData;

public class SmsReceiver extends BroadcastReceiver {
    private MutableLiveData<SmsEntity> latestSms = new MutableLiveData<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SmsReceiver", "Received SMS");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();

                    String[] parts = messageBody.split(",");

                    if (parts.length == 2) {
                        String userId = parts[0].trim();
                        String password = parts[1].trim();


                        // 使用 Room Database 進行資料庫操作
                        AppExecutor.getInstance().diskIO().execute(() -> {
                            SmsDatabase smsDatabase = SmsDatabase.getInstance(context);
                            SmsDao smsDao = smsDatabase.smsDao();

                            // 根据电话号码从数据库中检索账户和密码
                            SmsEntity user = smsDao.getUserByPhoneNumber(sender);

                            if (user != null && userId.equals(user.getUserId()) && password.equals(user.getPassword())) {
                                // 生成随机验证码
                                String verificationCode = generateRandomCode();
                                sendVerificationCode(sender, verificationCode);
                                // 将验证码加入到数据库中
                                user.setVerificationCode(verificationCode);
                                smsDao.updateVerificationCode(user);
                            }
                            else{
                                sendResult(sender,"no data");
                            }


                        });
                    } else if (isFourDigitNumber(messageBody)){
                        // 收到回傳驗證碼，比較完再回傳結果
                        AppExecutor.getInstance().diskIO().execute(() -> {
                            // 获取存储在数据库中的最新短信
                            SmsDatabase smsDatabase = SmsDatabase.getInstance(context);
                            SmsDao smsDao = smsDatabase.smsDao();

                            // 根据电话号码从数据库中检索账户和密码
                            SmsEntity user = smsDao.getUserByPhoneNumber(sender);

                            if (user != null && user.getVerificationCode().equals(messageBody)) {
                                sendResult(sender,"驗證碼符合");
                            }
                            else {
                                sendResult(sender,"驗證碼不符合");
                            }
                        });
                    }
                }
            }
        }
    }

    private String generateRandomCode() {
        // 生成隨機驗證碼
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

    private void sendVerificationCode(String phoneNumber, String verificationCode) {
        // 使用 SmsManager 來發送回覆簡訊
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        String message = "Your verification code is: " + verificationCode;
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void sendResult(String phoneNumber, String resultMessage) {
        // 使用 SmsManager 发送回傳驗證結果
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, resultMessage, null, null);
    }
    private boolean isFourDigitNumber(String input) {
        return input.matches("\\d{4}");
    }
}
