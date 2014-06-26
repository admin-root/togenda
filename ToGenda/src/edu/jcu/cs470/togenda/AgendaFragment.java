package edu.jcu.cs470.togenda;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.Format; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import android.content.ContentUris;
import android.database.Cursor; 
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.text.format.Time;


//This fragment holds the apps Agenda View
//When created, it will queery the user's Google Calendar for events in the next week. It will make Event cards for each of these.
//The app will also queery it's own Task database for tasks that the user entered, and will create a task card for each of those.
//It will sort all Event cards and schedule Task cards in the free spaces in between the Events.
//If there is a free hour available, an available task will be scheduled into it.

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("SimpleDateFormat")
public class AgendaFragment extends Fragment{

	private View myFragmentView;
	private static final int MiliSecDay = 86400000;	//Number of Milliseconds in a day.
	private Cursor mCursor = null;

	//Contains all columns we are to recieve from Google Calendar.
	private static final String[] COLS = new String[]{ CalendarContract.Instances.EVENT_ID, 
		CalendarContract.Instances.TITLE,  CalendarContract.Events.DESCRIPTION, CalendarContract.Instances.START_DAY, 
		CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.END_MINUTE, 
		CalendarContract.Events.DISPLAY_COLOR, CalendarContract.Events.CALENDAR_COLOR_KEY, CalendarContract.Events.ALL_DAY};

	public CardUI CardView;

	DBAdapter db;

	public Drawable backgroundColor;
	AlertDialog alertDialog;
	TaskCreator taskCreator;

	@SuppressWarnings("unchecked") //Suppressor on the for the "sort" function.
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for getActivity() fragment

		myFragmentView = inflater.inflate(R.layout.fragment_main, container, false);

		// init CardView
		CardView = (CardUI) myFragmentView.findViewById(R.id.cardsviewday);
		CardView.setSwipeable(false); //Global variable for now. Need to change library so we can set swipable on per-card basis.

		//Date formating
		Format df = DateFormat.getDateFormat(getActivity());
		Format tf = DateFormat.getTimeFormat(getActivity()); 
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();

		//Date Card
		String datelabel = sdf.format(d)+", "+tf.format(d)+", "+df.format(d);

		//getting current time for use in query
		Time t = new Time();
		t.setToNow();
		db = new DBAdapter(getActivity());

		//Getting URI for calendar
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true)); //start time = now
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true)+(MiliSecDay*7));//End time = (now + 1 week)
		Uri eventsUri = eventsUriBuilder.build();

		//Fill cursor with desired calendar events.
		mCursor = getActivity().getContentResolver().query(eventsUri, COLS, null, null, CalendarContract.Instances.DTSTART + " ASC");
		mCursor.moveToFirst();

		//Array List of all card types.
		ArrayList<CardTemplate> cardList = new ArrayList<CardTemplate>();

		boolean makeCards = true;

		//Card stack is for "all day" events
		CardStack stack = new CardStack();
		stack.setTitle(datelabel);
		stack.setColor("#33b5e5");

		while(makeCards)
		{
			Log.d("AgendaFragment", "Making Event Cards");
			EventCard newCard = getEvent();
			if (newCard.AllDay == true)	//If full day event
			{
				if (newCard.startTime <= t.toMillis(true))	//And is for today
				{
					stack.add(newCard); //add to the "all day" stack
				}
			}
			else if (newCard.getTitle() != "no event") //"no event" == try-catch block
			{
				cardList.add(newCard);
			}
			else
			{
				makeCards = false; //do not add card
			}
			if (newCard.isLast() == true)
			{
				makeCards = false;
			}
		}

		Collections.sort(cardList); //sort event cards

		//GET TASKS HERE
		db.open();
		Cursor TaskCursor = db.getAllTasks();

		//Task list array
		ArrayList<CardTemplate> TaskList = new ArrayList<CardTemplate>();

		try{

			if (TaskCursor != null)
			{
				makeCards = true;
			}
			while(makeCards) // make task cards
			{
				Log.d("AgendaFragment", "Making Task Cards");
				//0 = task ID, 1 = title; 2 = description; 3 = date; 4 = color ID; 5 = priority
				TaskList.add(new TaskCard(TaskCursor.getInt(0),TaskCursor.getString(1), TaskCursor.getString(2), TaskCursor.getLong(3),
						TaskCursor.getInt(4),TaskCursor.getInt(5), getActivity(), getFragmentManager(), TaskCursor.getInt(6)));
				if(TaskCursor.isLast()) 
				{
					makeCards = false;
				}
				else
				{
					TaskCursor.moveToNext();
				}
			}
		}
		catch(Exception E)
		{
		}
		db.close();

		//SORT TASKS
		Collections.sort(TaskList);

		CardView.addStack(stack); //add the "all day" stack to the card view.

		int eventLength = cardList.size();	//Number of events
		int taskLength = TaskList.size();	//number of tasks
		Long blockStart = t.toMillis(true);	//time scheduling from (starts at current time)
		Long MiliHalfHour = (long) 1800000;	//Number of milliseconds in a half hour

		while (eventLength > 0) //as long as there are still events in the list
		{
			Log.d("AgendaFragment", "Sorting with Events");
			if(taskLength > 0)	//as long as there are still tasks in the list
			{
				if (cardList.get(eventLength-1).getStart()-blockStart >= (MiliHalfHour*taskLength))	//Check if there is time to schedule a task
				{
					//Schedule task
					CardView.addCard(TaskList.get(taskLength-1));
					blockStart += MiliHalfHour*taskLength;
					taskLength-=1;

				}
				else
				{
					blockStart = cardList.get(eventLength-1).getEnd();
					//put event
					CardView.addCard(cardList.get(eventLength-1));
					eventLength-=1;
				}
			}
			else
			{
				blockStart = cardList.get(eventLength-1).getEnd();
				//put event
				Log.d("Agenda Fragment", "Put Event");
				CardView.addCard(cardList.get(eventLength-1));
				eventLength-=1;
			}
		}
		Log.d("Agenda Fragment", "Done Sorting");
		while(taskLength>0)
		{
			Log.d("Agenda Fragment", "Listing Tasks");
			CardView.addCard(TaskList.get(taskLength-1));
			taskLength-=1;
		}

		if (cardList.isEmpty())
		{
			//Create cardtype that explains that there are no current events.
		}
		// draw cards
		Log.d("Agenda Fragment", "Refresh");
		CardView.refresh();
		return myFragmentView;
	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public EventCard getEvent() {
		//Gets events from Google Calendar stored on the user's device.

		Log.d("AgendaFragment", "Fetch Event");

		try
		{
			EventCard event;
			String title;
			long start;
			long end;
			//			int color; //holds custom color case
			String colorKey, colorCode;
			String desc;
			String eventId;
			boolean last = false;
			boolean allday = false;

			try {
				//Get Event from Google Calendar

				//CalendarContract.Instances.EVENT_ID,
				eventId = mCursor.getString(0);
				//CalendarContract.Instances.TITLE,
				title = mCursor.getString(1);
				//CalendarContract.Events.DESCRIPTION, 
				desc = mCursor.getString(2);

				start = mCursor.getLong(4);					//MIGHT REQUIRE LONG
				//				CalendarContract.Instances.END_DAY,

				//				CalendarContract.Instances.END_MINUTE,
				end = mCursor.getLong(5);					//MIGHT REQUIRE LONG
				//				CalendarContract.Instances.EVENT_COLOR_KEY,
				colorCode = mCursor.getString(7);
				colorKey = mCursor.getString(8);
				//CalendarContract.Instances.EVENT_COLOR};
				if (mCursor.getInt(9) == 1)
				{
					allday = true;
				}

			} catch (Exception e) {
				//ignore
				return new EventCard("no event");
			}

			if(mCursor.isLast()) 
			{
				last = true;
			}
			else
			{
				mCursor.moveToNext();
			}

			//creates event (title,description,star time,end time,event color, calendar color,is clickable,have overflow button,id,is last)
			event = new EventCard(title, desc, start, end, colorCode, colorKey, true, true, eventId, last, allday);

			return event;
		}
		catch(Exception ex)
		{
			return new EventCard("no event");
		}
	}
}
