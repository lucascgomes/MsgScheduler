package com.example.messagesscheduler;

import java.util.ArrayList;
import java.util.Scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;

public class AlarmReceiver extends BroadcastReceiver {
	
	private SmsManager smsManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Read line from the text file
		int file_line;
		String message;
		String love_letter_emoticon = "\uD83D\uDC8C";
		String phone_number = "+5512991565706";
		
		SharedPreferences file_prefs = context.getSharedPreferences("FILE_PREFS", Context.MODE_PRIVATE);
		file_line = file_prefs.getInt("file_line", 0);
		
		Scanner s = new Scanner(context.getResources().openRawResource(R.raw.messages));
		try {
			for (int i=0; i<file_line; i++){
				s.nextLine();
			}
		    message = s.nextLine();
	        Log.d("MESSAGE-->", message);
		} finally {
		    s.close();
		}
		
		file_line++;
		SharedPreferences.Editor editor = file_prefs.edit();
		editor.putInt("file_line", file_line);
		editor.commit();
		
		EditText myMessage = new EditText(context);
		myMessage.getText().append(love_letter_emoticon + " " + message );
		
		smsManager = SmsManager.getDefault();
		ArrayList<String> messageParts = smsManager.divideMessage(myMessage.getText().toString());
		smsManager.sendMultipartTextMessage(phone_number, null, messageParts, null, null);
	}

	// public void setAlarm(Context context){
	// Log.d("Carbon","Alrm SET !!");
	//
	// // get a Calendar object with current time
	// Calendar cal = Calendar.getInstance();
	// // add 30 seconds to the calendar object
	// cal.add(Calendar.SECOND, 30);
	// Intent intent = new Intent(context, AlarmReceiver.class);
	// PendingIntent sender = PendingIntent.getBroadcast(context, 192837,
	// intent, PendingIntent.FLAG_UPDATE_CURRENT);
	//
	// // Get the AlarmManager service
	// AlarmManager am = (AlarmManager)
	// context.getSystemService(context.ALARM_SERVICE);
	// am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
	// }
}