package org.foxbat.opsdroid.trigger;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.foxbat.opsdroid.OpsListFragment;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.rest.AppManager;
import org.foxbat.opsdroid.rest.UrlSynthesizer;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by chlr on 9/19/14.
 */
public class TriggerFragment extends OpsListFragment {

    TriggerListAdapter adapter;
    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        adapter = TriggerListAdapter.getInstance(this.getActivity());

    }

    @Override
    public void onStart() {
        Log.v(this.getClass().getName(),"on start called");
        super.onStart();
        this.getListView().setAdapter(adapter);
        String url = new UrlSynthesizer().trigger_list();
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
                adapter.refreshData(json_arr);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(TriggerFragment.this.getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.triggerfragment,container,false);

        EditText et = (EditText)view.findViewById(R.id.triggernamefilter);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }



    private boolean isDataCached(String url) {
        try {
            return (AppManager.getInstance().getRequestQueue().getCache().get(url).data == null) ? false : true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        Log.v(this.getClass().getName(),"List Item Click detected");
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        Fragment fragment = new TriggerDetailFragment();
        Bundle bundle = new Bundle();
        TriggerListAdapter.TriggerListRecord record = adapter.getRecord((int)id);
        bundle.putString("trigger_id",record.trigger_id);
        bundle.putString("sys_class_name",record.sys_class_name);
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_holder,fragment);
        transaction.commit();
    }

    private void makeRequest(String url) {
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray json_arr) {
                adapter = TriggerListAdapter.getInstance(TriggerFragment.this.getActivity());
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
                Toast.makeText(TriggerFragment.this.getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void refreshData() {
        pd = new ProgressDialog(this.getActivity());
        pd.setIndeterminate(true);
        pd.setTitle("Please wait");
        pd.setMessage("Fetching Data");
        pd.show();
        makeRequest(new UrlSynthesizer().trigger_list());
    }
}