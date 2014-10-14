package org.foxbat.opsdroid.taskdetail;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.rest.AppManager;
import org.foxbat.opsdroid.rest.UrlSynthesizer;
import org.foxbat.opsdroid.task.TaskRecord;
import org.foxbat.opsdroid.task.TaskTypeAdapter;
import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by chlr on 10/10/14.
 */
public class TaskDetailHistoryFragment extends ListFragment {

    TaskDetailHistoryAdapter adapter;
    ProgressDialog pd;
    TaskRecord record;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_detail_history_fragment, container, false);
        record = TaskTypeAdapter.getInstance(this.getActivity()).getTaskRecord(this.getArguments().getInt("position", 0));
        adapter = TaskDetailHistoryAdapter.getInstance(this.getActivity());
        this.setListAdapter(adapter);
        String url = new UrlSynthesizer().task_history(record.task_id);

        if (!isDataCached(url)) {
            pd = new ProgressDialog(this.getActivity());
            pd.setIndeterminate(true);
            pd.setTitle("Please wait");
            pd.setMessage("Fetching Data");
            pd.show();
            this.makeRequest(new UrlSynthesizer().task_history(record.task_id));
        } else {
            try {
                String data = new String(AppManager.getInstance().getRequestQueue().getCache().get(url).data);
                JSONArray json_arr = new JSONArray(data);
                adapter.refreshData(json_arr);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(TaskDetailHistoryFragment.this.getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private boolean isDataCached(String url) {
        try {
            return (AppManager.getInstance().getRequestQueue().getCache().get(url).data == null) ? false : true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void makeRequest(String url) {

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray json_arr) {
                adapter.refreshData(json_arr);
                adapter.notifyDataSetChanged();
                if (pd != null) {
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
              try {
                Toast.makeText(TaskDetailHistoryFragment.this.getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                if (pd != null) {
                    pd.dismiss();
                } }
              catch (NullPointerException exp) {
                  exp.printStackTrace();
              }
            }
        }

        );

        AppManager.getInstance().addToRequestQueue(req);

    }


}


