package com.example.mygarden;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

@SuppressLint("Wakelock")
public class AlarmBroadcastReceiver extends BroadcastReceiver {
	  @Override
      public void onReceive(Context context, Intent intent) 
      {   
          PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
          PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
          wl.acquire();

          // Put here YOUR code.
          //Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
          SetAlarm(context);
         // CancelAlarm(context);
          wl.release();
      }

  public void SetAlarm(Context context)
  {
      AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
      Intent i = new Intent(context, AlarmBroadcastReceiver.class);
      PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
      am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60, pi); // Millisec * Second * Minute
  }

  public void CancelAlarm(Context context)
  {
      Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
      PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
      AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      alarmManager.cancel(sender);
  }
 }