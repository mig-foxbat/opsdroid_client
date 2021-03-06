package org.foxbat.opsdroid.model;
import android.content.Context;
import android.database.Cursor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chlr on 9/21/14.
 */
public class OpswiseMasterManager  {

    private static final String MASTER_TABLENAME = "opswise_master";
    private static final String SLAVE_TABLENAME = "task_notification";
    private DatabaseHandler dbhandler;
    private long rentention_window = 7L;
    private Context context;
    public OpswiseMasterManager(Context context) {
        this.context = context;
        dbhandler = new DatabaseHandler();
    }

    public void deleteDate(int datekey) {
        dbhandler.getWritableDatabase();
        String value =new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis() - rentention_window * 24 * 3600 * 1000));
        String sql = String.format("DELETE FROM %s WHERE datekey = %d or datekey < %d;", MASTER_TABLENAME,datekey,Integer.parseInt(value));
        dbhandler.execRawSQL(sql);
        dbhandler.close();
    }


    public void updateTask(JSONObject json) {
        dbhandler.getWritableDatabase();
        try {
            String delete_sql = "DELETE FROM %s WHERE sys_id = '%s'";
            String select_sql = "SELECT distinct datekey FROM %s WHERE sys_id = '%s'";
            List<Integer> datekeylist = new ArrayList<>(10);
            Cursor cursor = dbhandler.executeSQL(String.format(select_sql, MASTER_TABLENAME, json.get("sys_id")), null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                datekeylist.add(cursor.getInt(0));
            }

            dbhandler.execRawSQL(String.format(delete_sql, MASTER_TABLENAME, json.get("sys_id")));

            for(int datekey : datekeylist) {
                json.put("datekey",String.valueOf(datekey));
                dbhandler.insertToTable(MASTER_TABLENAME,json);
            }
            dbhandler.close();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void populateTable(JSONArray rows) {
        dbhandler.insertToTable(MASTER_TABLENAME,rows);
    }

//    public void populateStatusTable(int datekey) {
//
//        String[] status_codes = (new Utils(context)).getSelectedSyncValue();
//        if (status_codes.length > 0) {
//            final String sql = "SELECT sys_id,task_name, FROM opswise_master a LEFT OUTER JOIN task_notification b\n" +
//                    "ON a.sys_id = b.sys_id\n" +
//                    "WHERE a.datekey = %d AND b.sys_id IS NULL and a.status_code in %s;";
//                    status_codes.toString().replace("[", "(").replace("");
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//                    Set<String> selected_status = prefs.getStringSet("selected_status", null);
//                    String[] status_list = selected_status.toArray(new String[0]);
//                    status_list.toString();
//        }
//    }


}
