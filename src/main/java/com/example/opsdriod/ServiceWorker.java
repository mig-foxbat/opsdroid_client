package com.example.opsdriod;

import android.content.Context;
import android.net.ConnectivityManager;
import com.example.opsdriod.model.DatabaseHandler;
import com.example.opsdriod.model.OpswiseMasterManager;
import com.example.opsdriod.rest.RequestDispatcher;
import com.example.opsdriod.rest.UrlSynthesizer;
import com.example.opsdriod.utils.AppObjectRepository;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.text.DateFormat;

import java.sql.ResultSet;
import java.util.Date;

/**
 * Created by chlr on 9/29/14.
 */
public class ServiceWorker {


    public void refreshDataForDate(int datekey) {
        ConnectivityManager cm = (ConnectivityManager)AppObjectRepository.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected() == true) {
            JSONArray arr = (new RequestDispatcher()).getJsonArrayResponse(new UrlSynthesizer().task_date(datekey));
            OpswiseMasterManager ops_master_mgr = new OpswiseMasterManager();
            ops_master_mgr.deleteDate(datekey);
            ops_master_mgr.populateTable(arr);
        }
    }





}
