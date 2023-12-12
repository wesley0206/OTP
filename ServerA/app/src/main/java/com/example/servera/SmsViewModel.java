package com.example.servera;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SmsViewModel extends AndroidViewModel {

    private SmsRepository smsRepository;
    private LiveData<SmsEntity> latestSms;

    public SmsViewModel(Application application) {
        super(application);
        smsRepository = new SmsRepository(application);
        latestSms = smsRepository.getLatestSms();
    }

    public LiveData<SmsEntity> getLatestSms() {
        return latestSms;
    }

}
