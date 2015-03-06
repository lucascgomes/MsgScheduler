package com.example.messagesscheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.EditText;

public class AlarmReceiver extends BroadcastReceiver {

	private SmsManager smsManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		int file_line;
		String message;
		String love_letter_emoticon = "\uD83D\uDC8C";
		String phone_number = "+55 35 9254-6711";
		
		/* READ LINE FROM TEXT FILE */
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
		file_line = 20;
		file_line++;
		message = "teste";
		/*****************************************************************/
		
		/* SAVE MESSAGE RECORD IN DB */
		MessageRecordDbHelper db = new MessageRecordDbHelper(context);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault());
		db.addMessageRecord(new MessageRecord(message, file_line-1, dateFormatter.format(new Date()),false));

		EditText myMessage = new EditText(context);
		myMessage.getText().append(love_letter_emoticon + " " + message);

		/* SMS SEND */
		smsManager = SmsManager.getDefault();
		ArrayList<String> messageParts = smsManager.divideMessage(myMessage.getText().toString());
		ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
		
		Intent sentIntent = new Intent(context, SmsSentReceiver.class);
		sentIntent.putExtra("MESSAGE_NUMBER", (int)file_line-1);
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 1, sentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		for (int i = 0; i < messageParts.size(); i++) {
			sentPendingIntents.add(i, sentPI);
		}
		smsManager.sendMultipartTextMessage(phone_number, null, messageParts, sentPendingIntents, null);
	}

}