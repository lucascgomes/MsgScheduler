package com.example.messagesscheduler;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	
	static TimePickerFragment newInstance(int hour, int minute) {
		TimePickerFragment timePicker = new TimePickerFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("hour", hour);
        args.putInt("minute", minute);
        timePicker.setArguments(args);

        return timePicker;
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		int hour = getArguments().getInt("hour");
		int minute = getArguments().getInt("minute");

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
	}
}