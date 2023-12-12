package com.example.servera;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

// TODO: add database.json
// TODO: check whether the user info exist, if exists, trigger RandomNumberGenerator(), and send it to the user phone number
// TODO: add RandomNumberGenerator() which return a `OTPcode`
// TODO: add `ReceiveSMS()` to get `ReceivedCode`
// TODO: if `ReceivedCode` == `OTPcode`, send `valid` message back to the user

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_SEND_SMS = 1;
    private static final int PERMISSION_RECEIVE_SMS = 2;
    private TextView smsContentTextView;
    private SmsViewModel smsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsContentTextView = findViewById(R.id.smsContentTextView);

        // 創建 ViewModel
        smsViewModel = new ViewModelProvider(this).get(SmsViewModel.class);

        // 觀察 LiveData 以更新 UI
        smsViewModel.getLatestSms().observe(this, new Observer<SmsEntity>() {
            @Override
            public void onChanged(SmsEntity smsEntity) {
                if (smsEntity != null) {
                    // 更新 UI，顯示最近一條簡訊的內容
                    String smsContent = "Sender: " + smsEntity.getSender() + "\n"
                            + "User ID: " + smsEntity.getUserId() + "\n"
                            + "Password: " + smsEntity.getPassword() + "\n" + "Verification Code:" + smsEntity.getVerificationCode();
                    smsContentTextView.setText(smsContent);
                } else {
                    // 如果沒有簡訊，可以顯示默認的內容或做其他處理
                    smsContentTextView.setText("目前沒有簡訊");
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_SEND_SMS);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                        PERMISSION_RECEIVE_SMS);
            }
        }
    }
}
