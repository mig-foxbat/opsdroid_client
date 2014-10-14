package org.foxbat.opsdroid;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import org.foxbat.opsdroid.service.BackgroundService;

/**
 * Created by chlr on 9/19/14.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getFragmentManager().beginTransaction().replace(android.R.id.content,new PrefFragment()).commit();
    }


    public class PrefFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            PreferenceManager.setDefaultValues(getActivity(),
                    R.xml.preferences, false);
            addPreferencesFromResource(R.xml.preferences);
            this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences preferences, String s) {
            if (s.equalsIgnoreCase("send_notifications")) {
               // handleNotification();
            }
        }

        @SuppressWarnings("unused")
        private void handleNotification() {

            Context context = SettingsActivity.this.getApplicationContext();
            AlarmManager alarm = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));
            Intent intent = new Intent(context, BackgroundService.class);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean notification_flag = prefs.getBoolean("send_notifications",false);
            if (notification_flag) {
               PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
               boolean already_set = (PendingIntent.getBroadcast(context, 0,intent,PendingIntent.FLAG_NO_CREATE) != null);
               if (already_set)
                    alarm.cancel(pintent);
               alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,AlarmManager.INTERVAL_FIFTEEN_MINUTES,AlarmManager.INTERVAL_FIFTEEN_MINUTES,pintent);
               Log.v(this.getClass().getName(),"Alarm Manager set");
            }
            else {
                PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
                alarm.cancel(pintent);
                Log.v(this.getClass().getName(),"Alarm Manager Canceled");
            }
        }

    }

}
