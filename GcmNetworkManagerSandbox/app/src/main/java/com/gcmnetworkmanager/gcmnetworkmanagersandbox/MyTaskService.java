package com.gcmnetworkmanager.gcmnetworkmanagersandbox;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

public class MyTaskService extends GcmTaskService {

    public static final String GCM_ONEOFF_TAG = "oneoff|[0,0]";
    public static final String GCM_REPEAT_TAG = "repeat|[7200,1800]";

    @Override
    public void onInitializeTasks() {
    /* called when app is updated to a new version or GPS are updated to new version
    you have to schedule your repeating tasks again, unfortunately */
        super.onInitializeTasks();
    }

    @Override
    public int onRunTask(TaskParams taskParams) {

        // YOUR CODE HERE



//        Handler h = new Handler(getMainLooper());
//
//        if (taskParams.getTag().equals(GCM_REPEAT_TAG)) {
//            h.post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(MainActivity.context, "REPEATING executed", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }

    public static void scheduleRepeat(Context context) {
        try {
            PeriodicTask periodic = new PeriodicTask.Builder()
                    //specify target service - must extend GcmTaskService
                    .setService(MyTaskService.class)
                    //repeat every 60 seconds
                    .setPeriod(60)
                    //specify how much earlier the task can be executed (in seconds)
                    .setFlex(30)
                    //tag that is unique to this task (can be used to cancel task)
                    .setTag(GCM_REPEAT_TAG)
                    //whether the task persists after device reboot
                    .setPersisted(true)
                    //if another task with same tag is already scheduled, replace it with this task
                    .setUpdateCurrent(true)
                    //set required network state, this line is optional
                    .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                    //request that charging must be connected, this line is optional
                    .setRequiresCharging(false)
                    .build();

            GcmNetworkManager.getInstance(context).schedule(periodic);
            Log.v("GCMNETWORKMANAGER", "repeating task scheduled");
        } catch (Exception e) {
            Log.e("GCMNETWORKMANAGER", "scheduling failed");
            e.printStackTrace();
        }
    }

    public static void cancelRepeat(Context context) {
        GcmNetworkManager
                .getInstance(context)
                .cancelTask(GCM_REPEAT_TAG, MyTaskService.class);
    }
}
