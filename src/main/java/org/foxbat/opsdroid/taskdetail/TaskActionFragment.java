package org.foxbat.opsdroid.taskdetail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.rest.AppManager;
import org.foxbat.opsdroid.rest.UrlSynthesizer;
import org.foxbat.opsdroid.task.TaskRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chlr on 10/14/14.
 */
public class TaskActionFragment extends Fragment implements View.OnClickListener {

    TaskRecord record;
    Map<String,String> datamap;

    public TaskActionFragment() {
        if (datamap == null) this.datamap = getDataMap();
    }

    public TaskActionFragment(TaskRecord record)  {
        this.record = record;
        if (datamap == null) this.datamap = getDataMap();
    }


    private Map<String,String> getDataMap() {

        Map<String,String> map = new HashMap<>(3);
        map.put("force_finish","Force Finish");
        map.put("rerun","Re-run");
        map.put("cancel","Cancel");
        return map;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.task_action_fragment,container,false);
        (view.findViewById(R.id.rerun)).setEnabled(isReRunnable());
        (view.findViewById(R.id.force_finish)).setEnabled(isForceFinishable());
        (view.findViewById(R.id.cancel)).setEnabled(isCancleable());
        (view.findViewById(R.id.rerun)).setOnClickListener(this);
        (view.findViewById(R.id.force_finish)).setOnClickListener(this);
        (view.findViewById(R.id.cancel)).setOnClickListener(this);

         view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
         return view;
    }

    private boolean isForceFinishable() {
        return this.record.sys_class_name.equalsIgnoreCase("ops_task_unix");
    }

    private boolean isReRunnable() {
        return this.record.sys_class_name.equalsIgnoreCase("ops_task_unix");
    }


    private boolean isCancleable() {
        return this.record.sys_class_name.equalsIgnoreCase("ops_task_unix");
    }


    @Override
    public void onClick(View view) {
        String tag = (String)view.getTag();
        String action = datamap.get(tag);
        (new AlertDialogFragment(tag,action)).show(this.getFragmentManager(),null);

    }

    private Document getDomElement(String xml){
        Document doc;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        return doc;
    }


    private void makeRequest(String tag) {
        Log.v(this.getClass().getName(),tag);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc = TaskActionFragment.this.getDomElement(response);
                Element element = (Element)doc.getElementsByTagName("message").item(0);
                String value = element.getAttribute("value");
                Toast.makeText(TaskActionFragment.this.getActivity().getApplicationContext(), value, Toast.LENGTH_LONG).show();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    Log.e(this.getClass().getName(),"Error Response code: " +  error.networkResponse.statusCode);
                    Toast.makeText(TaskActionFragment.this.getActivity().getApplicationContext(), (error.getMessage() == null ? "Network Error" : error.getMessage()), Toast.LENGTH_LONG).show();
                }
            }
        };

        StringRequest request = new StringRequest(
                Request.Method.POST,
                new UrlSynthesizer().task_action(tag,TaskActionFragment.this.record.sys_id),
                listener,
                errorListener)

        {

            @Override
            protected Map<String,String> getParams() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TaskActionFragment.this.getActivity());
                Map<String, String>  params = new HashMap<>();
                params.put("user_name",prefs.getString("username",""));
                params.put("user_password",prefs.getString("password",""));
                return params;
            }


        };

        AppManager.getInstance().addToRequestQueue(request);



    }




    public class AlertDialogFragment extends DialogFragment {

        String tag,action;
        public  AlertDialogFragment(String tag,String action) {
            this.tag = tag;
            this.action = action;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
            alertDialogBuilder.setTitle("Achtung!!!");
            alertDialogBuilder.setMessage("confirm action "+action);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    TaskActionFragment.this.makeRequest(tag);
                    dialogInterface.dismiss();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });


            return alertDialogBuilder.create();

        }

    }

}



