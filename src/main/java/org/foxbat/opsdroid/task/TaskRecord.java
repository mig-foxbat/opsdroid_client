package org.foxbat.opsdroid.task;

/**
 * Created by chlr on 10/7/14.
 */
public class TaskRecord {

    public String ins_name,sys_id,task_id,sys_class_name;
    public String task_name,summary,task_ref_count,status_code;
    public String queued_time,start_time,end_time,duration;
    public String retry_interval,retry_maximum,retry_indefinitely,attempt_count;
    public String sys_updated_by,sys_created_by,execution_user,invoked_by,agent;


    public TaskRecord(String ins_name, String sys_id, String task_id, String sys_class_name) {

        this.ins_name = ins_name.toLowerCase();
        this.sys_id = sys_id;
        this.task_id = task_id;
        this.sys_class_name = sys_class_name;
    }


    public void setMainBlock(String task_name,String summary,String task_ref_count,String status_code) {
        this.task_name = task_name.toLowerCase();
        this.summary = summary;
        this.task_ref_count = task_ref_count;
        this.status_code = status_code;
    }

    public void setTimeBlock(String queued_time,String start_time, String end_time, String duration) {
        this.queued_time = queued_time;
        this.start_time = start_time;
        this.end_time = end_time;
        this.duration = duration;
    }

    public void setRetryBlock(String retry_interval,String retry_maximum,String retry_indefinitely, String attempt_count) {
        this.retry_interval = retry_interval;
        this.retry_maximum = retry_maximum;
        this.retry_indefinitely = retry_indefinitely;
        this.attempt_count = attempt_count;
    }

    public void setUserBlock(String sys_updated_by,String sys_created_by, String execution_user,String invoked_by,String agent) {
        this.sys_updated_by = sys_updated_by;
        this.sys_created_by = sys_created_by;
        this.execution_user = execution_user;
        this.invoked_by = invoked_by;
        this.agent = agent;
    }


}
