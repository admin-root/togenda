package edu.jcu.cs470.togenda;

public class CalEvent {

	//Calendar table
	String CALENDAR_ACCOUNT_NAME;
	String CALENDAR_ACCOUNT_TYPE;
	String CALENDAR_NAME;
	String CALENDAR_CALENDAR_DISPLAY_NAME;
	String CALENDAR_CALENDAR_COLOR;
	String CALENDAR_CALENDAR_ACCESS_LEVEL;
	String CALENDAR_OWNER_ACCOUNT;
	
	//Event table
	String EVENTS_DTSTART; //start date time
	String EVENTS_DTEND; //end date time
	String EVENTS_DURATION; //event duration
	String EVENTS_RRULE; //if event is reoccuring
	String EVENTS_EVENTTIMEZONE;
	String EVENTS_CALENDARID;
	
	//Instance table. For repeating events?
	String	INSTANCE_BEGIN;	//The beginning time of the instance, in UTC milliseconds.
	String	INSTANCE_END;	//The ending time of the instance, in UTC milliseconds.
	String	INSTANCE_END_DAY;	//The Julian end day of the instance, relative to the local time zone.
	String	INSTANCE_END_MINUTE;	//The end minute of the instance measured from midnight in the local time zone.
	String	INSTANCE_EVENT_ID;	//The _id of the event for this instance.
	String	INSTANCE_START_DAY;	//The Julian start day of the instance, relative to the local time zone.
	String	INSTANCE_START_MINUTE;	//The start minute of the instance measured from midnight in the local time zone.
	
	//List of who's attending events.
	String ATTENDEES_ATTENDEE_NAME;
	String ATTENDEES_ATTENDEE_EMAIL;
	String ATTENDEES_ATTENDEE_RELATIONSHIP;
	String ATTENDEES_ATTENDEE_TYPE;
	String ATTENDEES_ATTENDEE_STATUS;
	String ATTENDEES_ATTENDEE_IDENTITY;
	String ATTENDEES_ATTENDEE_ID_NAMESPACE;
	
	public CalEvent(String CalId, String Begin, String End, String StartDay, String StartMinute,String EndDay, String EndMinute)
	{
		
	}
	
}
