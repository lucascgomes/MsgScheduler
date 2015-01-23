package com.example.messagesscheduler;

import java.util.Scanner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity{
	
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
		
		TimePickerFragment dialog = TimePickerFragment.newInstance(hour, minute);
        dialog.show(this.getFragmentManager(), "TimePickerFragment");
	}
	
}
