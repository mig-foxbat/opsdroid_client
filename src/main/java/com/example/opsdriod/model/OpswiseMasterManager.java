package com.example.opsdriod.model;
import org.json.JSONArray;

/**
 * Created by chlr on 9/21/14.
 */
public class OpswiseMasterManager  {

    private static final String TABLENAME = "opswise_master";
    private DatabaseHandler dbhandler;
    public OpswiseMasterManager() {
        dbhandler = new DatabaseHandler();
    }

    public void deleteDate(int datekey) {
        dbhandler.getWritableDatabase();
        String sql = String.format("DELETE FROM %s WHERE datekey = %d;",TABLENAME,datekey);
        dbhandler.executeQuery(sql,null);
    }

    public void populateTable(JSONArray rows) {
        dbhandler.insertToTable(TABLENAME,rows);
    }



}
