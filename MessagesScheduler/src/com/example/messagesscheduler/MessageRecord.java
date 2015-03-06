package com.example.messagesscheduler;

import android.provider.BaseColumns;

//Contract and model class
public class MessageRecord {
	private String message;
	private int message_number;
	private String datetime;
	private boolean was_sent;
	
	public MessageRecord(){}
	
	public MessageRecord(String message, int message_number, String datetime, boolean was_sent) {
		super();
		this.message = message;
		this.message_number = message_number;
		this.datetime = datetime;
		this.was_sent = was_sent;
	}

	/* Inner class that defines the table contents */
    public static abstract class MessageEntry implements BaseColumns {
        public static final String TABLE_NAME = "message_entry";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_MESSAGE_NUMBER = "message_number";
        public static final String COLUMN_NAME_DATETIME = "datetime";
        public static final String COLUMN_NAME_WAS_SENT = "was_sent";
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMessage_number() {
		return message_number;
	}

	public void setMessage_number(int message_number) {
		this.message_number = message_number;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public boolean was_sent() {
		return was_sent;
	}

	public void set_sent(boolean was_sent) {
		this.was_sent = was_sent;
	}

	@Override
	public String toString() {
		return "MessageRecordContract [message=" + message
				+ ", message_number=" + message_number + ", datetime="
				+ datetime + ", was_sent=" + was_sent + "]";
	}
}
