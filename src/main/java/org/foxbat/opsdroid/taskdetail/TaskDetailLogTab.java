package org.foxbat.opsdroid.taskdetail;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.rest.AppManager;
import org.foxbat.opsdroid.rest.UrlSynthesizer;
import org.foxbat.opsdroid.task.TaskRecord;
import org.foxbat.opsdroid.task.TaskTypeAdapter;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by chlr on 10/8/14.
 */
public class TaskDetailLogTab extends Fragment {

    ProgressDialog bar;
    String[] options = new String[] {"stdout","stderr"};
    JSONObject json;
    TaskRecord record;

    @Override
    public void onStart(){
        super.onStart();
        Spinner sp  = (Spinner)this.getView().findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stdx = options[i];
                TextView tv =(TextView) TaskDetailLogTab.this.getView().findViewById(R.id.content);
                Log.v(this.getClass().getName(),stdx);
                if(stdx.equalsIgnoreCase("stderr"))
                {  try { tv.setText(json.getString("stderr")); } catch (Exception e) {    Toast.makeText(TaskDetailLogTab.this.getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show(); } ; }
                else
                {  try { tv.setText(json.getString("stdout")); } catch (JSONException e) {    Toast.makeText(TaskDetailLogTab.this.getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show(); } ; }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button bu = (Button)this.getView().findViewById(R.id.refresh);
        final String url  = new UrlSynthesizer().task_run_log(record.agent, record.sys_id);
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest(url);
            }
        });

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_detail_log_tab, container, false);
        record = TaskTypeAdapter.getInstance(this.getActivity()).getTaskRecord(this.getArguments().getInt("position", 0));
        final String url  = new UrlSynthesizer().task_run_log(record.agent, record.sys_id);
        Spinner sp  = (Spinner)v.findViewById(R.id.spinner);
        sp.setAdapter(new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_item,options));
        if (isDataCached(url)) {
            String data = new String(AppManager.getInstance().getRequestQueue().getCache().get(url).data);
            try {
                this.json = new JSONObject(data);
                TextView tv =(TextView)v.findViewById(R.id.content);
                String stdx = (String)sp.getSelectedItem();
                if(stdx.equalsIgnoreCase("stderr"))
                {  try { tv.setText(json.getString("stderr")); } catch (JSONException e) {    Toast.makeText(TaskDetailLogTab.this.getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show(); } ; }
                else
                {  try { tv.setText(json.getString("stdout")); } catch (JSONException e) {    Toast.makeText(TaskDetailLogTab.this.getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show(); } ; }

            } catch (JSONException e) {
                Toast.makeText(TaskDetailLogTab.this.getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

            return v;
        }
        bar = new ProgressDialog(this.getActivity());
        bar.setIndeterminate(true);
        bar.setTitle("Please wait");
        bar.setMessage("Fetching Data");
        bar.show();
        makeRequest(url);
        return v;
    }


    private boolean isDataCached(String url) {
        try {
            return (AppManager.getInstance().getRequestQueue().getCache().get(url).data == null) ? false : true;
        }
        catch (NullPointerException e)
        {
            return false;
        }
    }

    private void makeRequest(String url) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
               TaskDetailLogTab.this.json = json;
               TextView tv =  (TextView)TaskDetailLogTab.this.getView().findViewById(R.id.content);
               Spinner sp = (Spinner)TaskDetailLogTab.this.getView().findViewById(R.id.spinner);
                String stdx = (String)sp.getSelectedItem();
                if(stdx.equalsIgnoreCase("stderr"))
                {  try { tv.setText(json.getString("stderr")); } catch (JSONException e) {    Toast.makeText(TaskDetailLogTab.this.getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show(); } ; }
                else
                {  try { tv.setText(json.getString("stdout")); } catch (JSONException e) {    Toast.makeText(TaskDetailLogTab.this.getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show(); } ; }
               if (bar != null) bar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(TaskDetailLogTab.this.getActivity().getApplicationContext(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        Log.v(this.getClass().getName(), url);
        AppManager.getInstance().addToRequestQueue(req);
    }
}