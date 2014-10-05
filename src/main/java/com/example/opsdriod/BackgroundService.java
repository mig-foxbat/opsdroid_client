package com.example.opsdriod;

import android.app.IntentService;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chlr on 9/29/14.
 */
public class BackgroundService extends IntentService {

    public BackgroundService(String name) {
        super(name);
    }

    public BackgroundService() {
        super("BackgroundService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        int datekey = Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format(new Date()));
        new ServiceWorker().refreshDataForDate(datekey);
    }
}
