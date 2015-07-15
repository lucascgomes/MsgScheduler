package com.example.messagesscheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
        	
        	SharedPreferences clock_settings = context.getSharedPreferences("CLOCK_SETTINGS", Context.MODE_PRIVATE);
    		int scheduled_hour = clock_settings.getInt("hour", -1);
    		int scheduled_minute = clock_settings.getInt("minute", -1);
    		
    		
    		
    		if (scheduled_hour == -1){
    			setAlarm(context, 20, 15);
    		}
    		else{
    			setAlarm(context, scheduled_hour, scheduled_minute);
    		}
        }
    }
    
    private void setAlarm(Context context, int hourOfDay, int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		
		//If the alarm was scheduled for the past and no message was sent yet when the system restarted
		if (System.currentTimeMillis() > calendar.getTimeInMillis()){ 
			SharedPreferences file_prefs = context.getSharedPreferences("FILE_PREFS", Context.MODE_PRIVATE);
			int last_msg_id = file_prefs.getInt("file_line", 149) - 1;
			
			MessageRecordDbHelper db = new MessageRecordDbHelper(context);
			MessageRecord last_message = db.getMessage(last_msg_id);
			
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
			if (last_message.getDatetime().contains( dateFormatter.format(new Date()) )){ //if a message was already sent today return
				return ;
			}
		}
		
		alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);	
	}
}