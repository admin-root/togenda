package edu.jcu.cs470.togenda;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

@SuppressLint("SimpleDateFormat")
public class AgendaFragment extends Fragment{

	private View myFragmentView;
	private static final int MiliSecDay = 86400000;	//Number of Milliseconds in a day.
	private Cursor mCursor = null;
	//	private String whereClauseTrue = "CalendarContract.Events.ALL_DAY=1";
	//	private String whereClauseFalse = "CalendarContract.Events.ALL_DAY=0";

	//Contains all columns we are to recieve from Google Calendar.
	private static final String[] COLS = new String[]{ CalendarContract.Instances.EVENT_ID, 
		CalendarContract.Instances.TITLE,  CalendarContract.Events.DESCRIPTION, CalendarContract.Instances.START_DAY, 
		CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.END_MINUTE, 
		CalendarContract.Instances.EVENT_COLOR_KEY, CalendarContract.Events.CALENDAR_COLOR_KEY, CalendarContract.Instances.EVENT_COLOR, 
		CalendarContract.Events.ALL_DAY};

	public Drawable backgroundColor;
	AlertDialog alertDialog;

	@SuppressWarnings("unchecked")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for getActivity() fragment

		myFragmentView = inflater.inflate(R.layout.fragment_main, container, false);
		
		// init CardView
		CardUI CardView = (CardUI) myFragmentView.findViewById(R.id.cardsviewday);
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

		//Getting URI for calendar
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true)); //start time = now
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true)+(MiliSecDay*7));//End time = (now + 1 week)
		Uri eventsUri = eventsUriBuilder.build();

		//Fill cursor with desired calendar events.
		mCursor = getActivity().getContentResolver().query(eventsUri, COLS, null, null, CalendarContract.Instances.DTSTART + " ASC");
		mCursor.moveToFirst();

		ArrayList<CardTemplate> cardList = new ArrayList<CardTemplate>();

		boolean makeCards = true;

		CardStack stack = new CardStack();
		stack.setTitle(datelabel);
		stack.setColor("#33b5e5");

		while(makeCards)
		{		
			EventCard newCard = getEvent();
			if (newCard.AllDay == true)
			{
				if (newCard.startTime <= t.toMillis(true))
				{
					stack.add(newCard);
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

		Collections.sort(cardList); //works now

		//cardList.add(new EventCard("sample card"));
		//cardList.add(new TaskCard("sample task"));
		cardList.add(new TaskCard("Example Task", "Example Description", 0000000000,"5","5","0",true));

		//GET TASKS HERE

		//SORT TASKS + EVENTS TOGETHER HERE

		CardView.addStack(stack);

		if (!cardList.isEmpty())
		{
			//Stacked cards are kind of awkward to use, and when placed in excession they cause lag.
			//Ordinary events will no longer be stacked. Instead, only full-day events will be stacked with each other when
			//multiple full-day events exist on the same day.
			//full day events aren't properly implemented yet.
			//Will use a different card format for full day events, as well as tasks so that different types of entries are
			//easily identified.

			for (int cards = cardList.size(); cards >= 1; cards--)
			{
				CardView.addCard(cardList.get(cards-1));
			}
		}

		CardView.addCard(new EventCard("No events"));
		
		
		if (cardList.isEmpty())
		{
			//Create cardtype that explains that there are no current events.
		}
		
		// draw cards
		CardView.refresh();
		
		return myFragmentView;
	}

//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) 
//		{        
//		case R.id.new_event:
//			//DANNY, TYPE HERE
//			//CREATES DIALOG POPUP
//			alertDialog = new AlertDialog.Builder(getActivity()).create();
//			alertDialog.setTitle("New Task");
//			//EditText taskname = new EditText(getApplicationContext());
//			//taskname.setTextColor(000000);
//			LayoutInflater inflater = getActivity().getLayoutInflater();
//			alertDialog.setView(inflater.inflate(R.layout.dialogue_task_creator, null));
//			//interface
//			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					// here you can add functions
//					//do nothing
//				}
//			});
//			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Create", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					// here you can add functions
//					//add to database
//					//refresh cardview
//				}
//			});
//			alertDialog.setIcon(R.drawable.ic_action_new_event);
//			alertDialog.show();
//			return true;        
//		default:            
//			return super.onOptionsItemSelected(item);
//
//		}
//	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
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
		try
		{
			EventCard event;
			String title;
			long start;
			long end;
			//			int color; //holds custom color case
			String colorKey, colorKey2;
			String desc;
			String eventId;
			boolean last = false;
			boolean allday = false;

			try {

				//				CalendarContract.Instances.EVENT_ID,
				eventId = mCursor.getString(0);
				//				CalendarContract.Instances.TITLE,
				title = mCursor.getString(1);
				//				CalendarContract.Events.DESCRIPTION, 
				desc = mCursor.getString(2);
				//				CalendarContract.Instances.START_DAY,

				//				CalendarContract.Instances.START_MINUTE,
				start = mCursor.getLong(4);					//MIGHT REQUIRE LONG
				//				CalendarContract.Instances.END_DAY,

				//				CalendarContract.Instances.END_MINUTE,
				end = mCursor.getLong(5);					//MIGHT REQUIRE LONG
				//				CalendarContract.Instances.EVENT_COLOR_KEY,
				colorKey = mCursor.getString(7);
				colorKey2 = mCursor.getString(8);
				//				CalendarContract.Instances.EVENT_COLOR};

				if (mCursor.getInt(10) == 1)
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
			event = new EventCard(title, desc, start, end, colorKey, colorKey2, true, true, eventId, last, allday);

			return event;
		}
		catch(Exception ex)
		{
			return new EventCard("no event");
		}
	}
}
