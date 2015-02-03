package com.example.messagesscheduler;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements TimePickerFragment.Listener{
	
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences clock_settings = getSharedPreferences("CLOCK_SETTINGS", Context.MODE_PRIVATE);
		int scheduled_hour = clock_settings.getInt("hour", -1);
		int scheduled_minute = clock_settings.getInt("minute", -1);
		
		if (scheduled_hour == -1){
			setTextClockTime(20, 00);
			SharedPreferences.Editor editor = clock_settings.edit();
			editor.putInt("hour", 20);
			editor.putInt("minute", 00);
			editor.commit();
			Toast.makeText(getApplicationContext(), "Welcome for the first time!", Toast.LENGTH_LONG).show();
		}
		else{
			setTextClockTime(scheduled_hour, scheduled_minute);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void scheduleSMS(View view){
		TextView smsClock = (TextView) view;
		String smsClockStr = smsClock.getText().toString();  
		int hour = Integer.parseInt(smsClockStr.substring(0, 2));
		int minute = Integer.parseInt(smsClockStr.substring(3, 5));
		Log.d("CLOCK", hour +"  "+ minute);
		
		TimePickerFragment timePickerDialog = TimePickerFragment.newInstance(hour, minute);
		timePickerDialog.show(this.getFragmentManager(), "TimePickerFragment");
		timePickerDialog.setListener(this);
	}
	
	private void setAlarm(int hourOfDay, int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		
		alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);	
	}
	
	private void setTextClockTime(int hourOfDay, int minute){
		TextView scheduledTime = (TextView) findViewById(R.id.textView2);
		String hourStr = "" + hourOfDay;
		String minuteStr = "" + minute;
		if (hourOfDay <10){
			hourStr = "0" + hourStr;
		}
		if (minute <10){
			minuteStr = "0" + minuteStr;
		}
		scheduledTime.setText(hourStr + ":" + minuteStr);
	}
	
	//Callback is being called twice?
	public void setTime(int hourOfDay, int minute) {
		setTextClockTime(hourOfDay, minute);
		setAlarm(hourOfDay, minute);
		
		SharedPreferences clock_settings = getSharedPreferences("CLOCK_SETTINGS", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = clock_settings.edit();
		editor.putInt("hour", hourOfDay);
		editor.putInt("minute", minute);
		editor.commit();
	}
	
}
