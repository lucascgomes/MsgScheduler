package com.example.messagesscheduler;

import java.util.Calendar;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements TimePickerFragment.Listener{
	
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Read lines
		int line = 0;
		Scanner s = new Scanner(getResources().openRawResource(R.raw.messages));
		try {
			for (int i=0; i<line; i++){
				s.nextLine();
			}
		    String word = s.nextLine();
	        Log.d("MESSAGE-->", word);
		} finally {
		    s.close();
		}
		// end of reading lines
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
	
	//Callback is being called twice?
	public void setTime(int hourOfDay, int minute) {
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
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		
		alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
	}
	
}
