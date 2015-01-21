package com.example.messagesscheduler;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	
	private SmsManager smsManager;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String message = "ALARM worked!";
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		
		smsManager = SmsManager.getDefault();
		EditText myMessage = new EditText(context);
		myMessage.getText().append("Teste alarm \uD83D\uDC8C");
		ArrayList<String> messageParts = smsManager.divideMessage(myMessage.getText().toString());
		smsManager.sendMultipartTextMessage("+5512991565706", null, messageParts, null, null);
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