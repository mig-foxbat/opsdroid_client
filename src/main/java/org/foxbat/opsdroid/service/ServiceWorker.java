package org.foxbat.opsdroid.service;

import android.content.Context;
import android.net.ConnectivityManager;
import org.foxbat.opsdroid.model.OpswiseMasterManager;
import org.foxbat.opsdroid.rest.RequestDispatcher;
import org.foxbat.opsdroid.rest.UrlSynthesizer;
import org.foxbat.opsdroid.utils.AppObjectRepository;
import org.json.JSONArray;

/**
 * Created by chlr on 9/29/14.
 */
public class ServiceWorker {

    private Context context;

    public ServiceWorker(Context context) {
        this.context = context;
    }

    public void refreshDataForDate(int datekey) {
        ConnectivityManager cm = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected() == true) {
            JSONArray arr = (new RequestDispatcher()).getJsonArrayResponse(new UrlSynthesizer().task_date(datekey));
            OpswiseMasterManager ops_master_mgr = new OpswiseMasterManager(context);
            ops_master_mgr.deleteDate(datekey);
            ops_master_mgr.populateTable(arr);
        }
    }

}
