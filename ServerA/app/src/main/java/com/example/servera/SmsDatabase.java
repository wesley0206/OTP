package com.example.servera;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.annotation.NonNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {SmsEntity.class}, version = 2, exportSchema = false)
public abstract class SmsDatabase extends RoomDatabase {

    public abstract SmsDao smsDao();

    private static volatile SmsDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static SmsDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (SmsDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    SmsDatabase.class, "sms-database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                // 在这里执行预先插入的数据操作
                SmsDao dao = instance.smsDao();
                dao.insert(new SmsEntity("+15551234567", "123", "123", "Code1"));

                // 可以添加更多的数据
            });
        }
    };
}
