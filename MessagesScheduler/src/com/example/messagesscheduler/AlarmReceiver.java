package com.example.messagesscheduler;

import java.util.ArrayList;
import java.util.Scanner;

import android.app.PendingIntent;
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
		String phone_number = "+55 35 9254-6711";

//		SharedPreferences file_prefs = context.getSharedPreferences("FILE_PREFS", Context.MODE_PRIVATE);
//		file_line = file_prefs.getInt("file_line", 0);
//
//		Scanner s = new Scanner(context.getResources().openRawResource(R.raw.messages));
//		try {
//			for (int i = 0; i < file_line; i++) {
//				s.nextLine();
//			}
//			message = s.nextLine();
//			Log.d("MESSAGE-->", message);
//		} finally {
//			s.close();
//		}
//
//		file_line++;
//		SharedPreferences.Editor editor = file_prefs.edit();
//		editor.putInt("file_line", file_line);
//		editor.commit();
		
		/************************* TEST **********************************/
		message = "teste";
		/*****************************************************************/

		EditText myMessage = new EditText(context);
		myMessage.getText().append(love_letter_emoticon + " " + message);

		/* SMS */
		smsManager = SmsManager.getDefault();
		ArrayList<String> messageParts = smsManager.divideMessage(myMessage.getText().toString());
		ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(context, SmsSentReceiver.class), 0);
		for (int i = 0; i < messageParts.size(); i++) {
			sentPendingIntents.add(i, sentPI);
		}
		smsManager.sendMultipartTextMessage(phone_number, null, messageParts, sentPendingIntents, null);
	}

}