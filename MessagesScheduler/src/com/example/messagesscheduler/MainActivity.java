/*
 * TODO:
 * Ordenar query DESC
 * Fix bug when alarm is set for a previous hour
 * Search for problem with text color and background color
 * Make an test option in app?
 * Fazer msg_number unique in db?
 */

package com.example.messagesscheduler;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements TimePickerFragment.Listener{
	
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initClock();
		drawMessages(1);
		drawMessages(0);
	}
	
	OnClickListener resendMessage = new OnClickListener() {
	    @Override
	    public void onClick(View view) {
	    	Toast.makeText(getApplicationContext(), "Sending...", Toast.LENGTH_LONG).show();
	    	Intent intent = new Intent(view.getContext(), AlarmReceiver.class);
	    	Integer messageNumber = (Integer)view.getTag();
	    	intent.putExtra("RESEND_MESSAGE_NUMBER", messageNumber.intValue());
	    	sendBroadcast(intent);
	    	//Make this view disappear?
	    	//Do these happen automatically?
	    }
	};
	
	private void drawMessages(int was_sent){
		// gets all sent messages from db
		MessageRecordDbHelper db = new MessageRecordDbHelper(this);
		List<MessageRecord> messages = db.getAllMessagesSentWith(was_sent);
		
		LinearLayout messageLinearLayout;
		TextView msgTextView; 
		TextView dateTextView;
		FrameLayout msgFrame;
		
		if (was_sent==1){
			messageLinearLayout = (LinearLayout) findViewById(R.id.mylinearLayout);
		}
		else{
			messageLinearLayout = (LinearLayout) findViewById(R.id.messageNotSentLayout);
		}
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 10, 0, 0);
		
		for (MessageRecord message: messages){
			msgFrame = new FrameLayout(this);
			msgTextView = new TextView(this);
			dateTextView = new TextView(this);
			
			msgTextView.setText("#" + message.getMessage_number() + ":" + " " + message.getMessage() + "\n");
			msgTextView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
			
			if(was_sent==1){
				dateTextView.setText(message.getDatetime());
				dateTextView.setTextAppearance(this, android.R.style.TextAppearance_Small);
				dateTextView.setGravity(Gravity.BOTTOM | Gravity.END);
				msgFrame.setBackgroundColor(Color.argb(100,255, 255, 255)); //white backgroud color
			}
			else{
				dateTextView.setText("Not sent");
				dateTextView.setTextAppearance(this, android.R.style.TextAppearance_Small);
				dateTextView.setGravity(Gravity.BOTTOM | Gravity.END);
				msgFrame.setBackgroundColor(Color.argb(100,255, 128, 0)); //orange backgroud color
				msgFrame.setTag(Integer.valueOf(message.getMessage_number()));
				msgFrame.setOnClickListener(resendMessage);
			}
			
			msgFrame.setPadding(4, 4, 4, 4);
			msgFrame.addView(msgTextView);
			msgFrame.addView(dateTextView);
			messageLinearLayout.addView(msgFrame,layoutParams);
		}
	}
	
	private void initClock(){
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
