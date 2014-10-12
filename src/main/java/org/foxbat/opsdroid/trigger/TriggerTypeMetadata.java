package org.foxbat.opsdroid.trigger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chlr on 10/11/14.
 */
public class TriggerTypeMetadata {

    static Map<String, Map<String,String>> metadata;

    static {
        metadata = new HashMap<>(10);
        metadata.put(
                "ops_trigger_time", ops_trigger_time());
        metadata.put(
                "ops_trigger_cron", ops_trigger_cron());
    }


    private static Map<String,String> ops_trigger_time() {
          Map<String,String> map = new HashMap<>(10);
          map.put("time_style","Time Style");
          map.put("time","Time");
          map.put("time_interval","Time Interval");
          map.put("interval_units","Interval Units");
          map.put("offset","Offset");
        return map;
    }

    private static Map<String,String> ops_trigger_cron() {
        Map<String,String> map = new HashMap<>(10);
        map.put("criteria","Criteria");
        return map;
    }


}
