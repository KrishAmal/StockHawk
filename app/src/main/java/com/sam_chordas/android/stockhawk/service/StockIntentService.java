package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.TaskParams;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {
  Handler mHandler;

  public StockIntentService(){
    super(StockIntentService.class.getName());
    mHandler=new Handler();
  }

  public StockIntentService(String name) {
    super(name);
  }

  public class DisplayToast implements Runnable{
    private final Context context;
    String Text;

    public DisplayToast(Context context,String text){
      this.context=context;
      Text=text;
    }
    @Override
    public void run() {
      Toast.makeText(context,Text, Toast.LENGTH_SHORT).show();
    }
  }

  private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      // Get extra data included in the Intent
      mHandler.post(new DisplayToast(getApplicationContext(),"Invalid Stock Symbol"));
    }
  };

  @Override protected void onHandleIntent(Intent intent) {

    LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            new IntentFilter("EmptyResponse"));

    Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");
    StockTaskService stockTaskService = new StockTaskService(this);
    Bundle args = new Bundle();
    if (intent.getStringExtra("tag").equals("add")){
      args.putString("symbol", intent.getStringExtra("symbol"));
    }
    // We can call OnRunTask from the intent service to force it to run immediately instead of
    // scheduling a task.;
    stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));

    LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    super.onDestroy();
  }
}
