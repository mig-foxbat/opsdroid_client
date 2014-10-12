package org.foxbat.opsdroid.trigger;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.rest.AppManager;
import org.foxbat.opsdroid.rest.UrlSynthesizer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by chlr on 10/11/14.
 */
public class TriggerDetailFragment extends Fragment {

    final Map<String,String> payload;
    String url;
    ProgressDialog pd;

    public TriggerDetailFragment() {
        payload = new HashMap<>(10);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.trigger_detail,container,false);
         return view;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        bundle = this.getArguments();
        String trigger_id  = bundle.getString("trigger_id","");
        String sys_class_name = bundle.getString("sys_class_name","");
        url = new UrlSynthesizer().trigger_detail(sys_class_name,trigger_id);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!isDataCached(url)) {
            pd = new ProgressDialog(this.getActivity());
            pd.setIndeterminate(true);
            pd.setTitle("Please wait");
            pd.setMessage("Fetching Data");
            pd.show();
            this.makeRequest(url);
        } else {
            try {
                String data = new String(AppManager.getInstance().getRequestQueue().getCache().get(url).data);
                JSONArray json_arr = new JSONArray(data);
                JSONObject json = json_arr.getJSONObject(0);
                refreshData(json);
                updateUI();
            } catch (JSONException e) {
                Toast.makeText(TriggerDetailFragment.this.getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }


    private void refreshData(JSONObject json) {
        payload.clear();
        Iterator<String> it = json.keys();
        while(it.hasNext()) {
            try {
                String key = it.next();
                payload.put(key, json.getString(key));

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUI() {
        View view = this.getView();
        ((TextView)view.findViewById(R.id.trigger_name)).setText(payload.get("name"));
        ((TextView)view.findViewById(R.id.trigger_taskname)).setText(payload.get("task_name"));
        ((TextView)view.findViewById(R.id.trigger_version)).setText(payload.get("version"));
        ((TextView)view.findViewById(R.id.trigger_enabled)).setText(payload.get("enabled"));
        ((TextView)view.findViewById(R.id.trigger_calendar)).setText(payload.get("calendar_name"));
        ((TextView)view.findViewById(R.id.trigger_skip_active)).setText(payload.get("skip_when_active"));
        ((TextView)view.findViewById(R.id.trigger_next_scheduled_time)).setText(payload.get("next_scheduled_time"));
        ((TextView)view.findViewById(R.id.trigger_sys_created_by)).setText(payload.get("sys_created_by"));
        ((TextView)view.findViewById(R.id.trigger_sys_created_on)).setText(payload.get("sys_created_on"));
        ((TextView)view.findViewById(R.id.trigger_sys_updated_by)).setText(payload.get("sys_updated_by"));
        ((TextView)view.findViewById(R.id.trigger_sys_updated_on)).setText(payload.get("sys_updated_on"));

        TableLayout table = (TableLayout)view.findViewById(R.id.tableLayout1);

        Map<String,String> extra_data = TriggerTypeMetadata.metadata.get(this.getArguments().getString("sys_class_name",""));
        for(String key: extra_data.keySet()) {
           TableRow row  = getTableRow(extra_data.get(key),payload.get(key));
           table.addView(row);
        }
    }


    private TableRow getTableRow(String name, String value) {
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        TableRow row = (TableRow)inflater.inflate(R.layout.trigger_detail_extra,null);
        ((TextView)row.findViewWithTag("lable")).setText(name);
        ((TextView)row.findViewWithTag("value")).setText(value);
        return row;
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
                JSONObject json = null;
                try { json = json_arr.getJSONObject(0); } catch (JSONException e) { Toast.makeText(TriggerDetailFragment.this.getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();  };
                refreshData(json);
                updateUI();
                if (pd != null) {
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Toast.makeText(TriggerDetailFragment.this.getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        );

        AppManager.getInstance().addToRequestQueue(req);
    }




}