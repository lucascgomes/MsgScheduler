package com.example.messagesscheduler;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	SmsManager smsManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		smsManager = SmsManager.getDefault();
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
	
	public void sendMessage(View view){
		EditText myMessage = (EditText)findViewById(R.id.editText1);
		Log.d("MESSAGE", myMessage.getText().toString());
		
		myMessage.getText().append(" \uD83D\uDC8C"); //love letter emoji
		ArrayList<String> messageParts = smsManager.divideMessage(myMessage.getText().toString());
		smsManager.sendMultipartTextMessage("+5512991565706", null, messageParts, null, null);
	}
}
