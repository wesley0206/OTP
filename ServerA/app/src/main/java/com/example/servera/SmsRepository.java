package com.example.servera;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;



public class SmsRepository {

    private SmsDao smsDao;
    private MutableLiveData<SmsEntity> latestSms;

    public SmsRepository(Application application) {
        SmsDatabase smsDatabase = SmsDatabase.getInstance(application);
        smsDao = smsDatabase.smsDao();
        latestSms = new MutableLiveData<>();
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                latestSms.postValue(smsDao.getLatestSmsSync());
            }
        });
    }

    public LiveData<SmsEntity> getLatestSms() {
        return latestSms;
    }

}

