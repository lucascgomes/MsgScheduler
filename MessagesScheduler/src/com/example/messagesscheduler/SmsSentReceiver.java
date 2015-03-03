package com.example.messagesscheduler;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SmsSentReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String contentTitle;
		String contentText;
		
		int msg_number = intent.getIntExtra("MESSAGE_NUMBER", 1234);
		
		if (getResultCode() == Activity.RESULT_OK){
			contentTitle = "Message #" + msg_number + " sent";
			contentText = "Click to see the message";
		}
		else{
			contentTitle = "Message not sent";
			contentText = "Try again later";
		}
		
		Notification.Builder mBuilder =
		        new Notification.Builder(context)
		        .setSmallIcon(R.drawable.ic_love_letter)
		        .setContentTitle(contentTitle)
		        .setContentText(contentText);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, MainActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            2,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());
	}
}
