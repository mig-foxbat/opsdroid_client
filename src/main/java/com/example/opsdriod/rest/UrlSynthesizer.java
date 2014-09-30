package com.example.opsdriod.rest;

import android.content.SharedPreferences;
import com.example.opsdriod.utils.AppObjectRepository;

/**
 * Created by chlr on 9/20/14.
 */
public class UrlSynthesizer {

    private final String baseurl;

    public UrlSynthesizer() {
        SharedPreferences preferences = AppObjectRepository.getAppSharedPreferences();
        baseurl = "http://"+preferences.getString("server",null)+":"+preferences.getString("port","80")+"/";
    }

    public String task_date(int date) {
        StringBuilder url = new StringBuilder();
        url.append(baseurl);
        url.append("tasks/date/");
        url.append(date);
        return url.toString();
    }

    public String task_status(int status) {
        StringBuilder url = new StringBuilder();
        url.append(baseurl);
        url.append("tasks/status/");
        url.append(status);
        return url.toString();
    }

    public String task_type(String type) {
        StringBuilder url = new StringBuilder();
        url.append(baseurl);
        url.append("tasks/type/");
        url.append(type);
        return url.toString();
    }

    public String task_info(String sys_id) {
        StringBuilder url = new StringBuilder();
        url.append(baseurl);
        url.append("tasks/exec_id/");
        url.append(sys_id);
        return url.toString();
    }

    public String task_run_log(String agent,String sys_id) {
        StringBuilder url = new StringBuilder();
        url.append(baseurl);
        url.append("tasks/log/");
        url.append("agent/");
        url.append(agent+"/");
        url.append(sys_id);
        return url.toString();
    }

}
