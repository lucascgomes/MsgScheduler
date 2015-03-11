package com.example.messagesscheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
	private String phone_number = "+55 35 9254-6711";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int msg_number = intent.getIntExtra("RESEND_MESSAGE_NUMBER", 1234);
		MessageRecord messageRecord;
		
		if (msg_number == 1234){
			messageRecord = getNewMessage(context);
		}
		else{
			MessageRecordDbHelper db = new MessageRecordDbHelper(context);
			messageRecord = db.getMessage(msg_number);
		}
		
		sendMessage(context, messageRecord.getMessage(), messageRecord.getMessage_number());
	}
	
	private String getMessageFromFile(Context context, int line_number){
		String message;
		Scanner s = new Scanner(context.getResources().openRawResource(R.raw.messages));
		try {
			for (int i = 0; i < line_number; i++) {
				s.nextLine();
			}
			message = s.nextLine();
			Log.d("MESSAGE-->", message);
		} finally {
			s.close();
		}
		return message;
	}
	
	private MessageRecord getNewMessage(Context context){
		int line_number;
		String message;	
		
		/* READ LINE FROM TEXT FILE */
		SharedPreferences file_prefs = context.getSharedPreferences("FILE_PREFS", Context.MODE_PRIVATE);
		line_number = file_prefs.getInt("file_line", 0); //get line number to read

		message = getMessageFromFile(context, line_number);

		line_number++;
		SharedPreferences.Editor editor = file_prefs.edit();
		editor.putInt("file_line", line_number);
		editor.commit();
		
		//** test **
		//	file_line = 20;
		//	file_line++;
		//	message = "teste";
		
		/* SAVE MESSAGE RECORD IN DB */
		MessageRecordDbHelper db = new MessageRecordDbHelper(context);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault());
		MessageRecord newMessage = new MessageRecord(message, line_number-1, dateFormatter.format(new Date()),false);
		db.addMessageRecord(newMessage);
		
		return newMessage;
	}
	
	private void sendMessage(Context context, String message, int messageNumber){
		String love_letter_emoticon = "\uD83D\uDC8C";
		EditText myMessage = new EditText(context);
		myMessage.getText().append(love_letter_emoticon + " " + message);

		/* SMS SEND */
		smsManager = SmsManager.getDefault();
		ArrayList<String> messageParts = smsManager.divideMessage(myMessage.getText().toString());
		ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
		
		Intent sentIntent = new Intent(context, SmsSentReceiver.class);
		sentIntent.putExtra("MESSAGE_NUMBER", (int)messageNumber);
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 1, sentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		for (int i = 0; i < messageParts.size(); i++) {
			sentPendingIntents.add(i, sentPI);
		}
		smsManager.sendMultipartTextMessage(phone_number, null, messageParts, sentPendingIntents, null);
	}

}