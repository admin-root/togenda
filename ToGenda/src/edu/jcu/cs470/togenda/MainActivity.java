package edu.jcu.cs470.togenda;

import com.fima.cardsui.views.CardUI;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.content.ContentUris;
import android.database.Cursor; 
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.text.format.Time;

public class MainActivity extends Activity{

	private static final int MiliSecDay = 86400000;	//Number of Milliseconds in a day.
	private Cursor mCursor = null;
	private String whereClauseTrue = "CalendarContract.Events.ALL_DAY=1";
	private String whereClauseFalse = "CalendarContract.Events.ALL_DAY=0";

	//Contains all columns we are to recieve from Google Calendar.
	private static final String[] COLS = new String[]{ CalendarContract.Instances.EVENT_ID, 
		CalendarContract.Instances.TITLE,  CalendarContract.Events.DESCRIPTION, CalendarContract.Instances.START_DAY, 
		CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.END_MINUTE, 
		CalendarContract.Instances.EVENT_COLOR_KEY, CalendarContract.Events.CALENDAR_COLOR_KEY, CalendarContract.Instances.EVENT_COLOR, 
		CalendarContract.Events.ALL_DAY};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init CardView
		CardUI CardView = (CardUI) findViewById(R.id.cardsview);
		CardView.setSwipeable(false); //Global variable for now. Need to change library so we can set swipable on per-card basis.

		//Date formating
		Format df = DateFormat.getDateFormat(this);
		Format tf = DateFormat.getTimeFormat(this); 
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();

		//Date Card
		CardView.addCard(new DateHolder(sdf.format(d)+", "+tf.format(d)+", "+df.format(d)));

		//getting current time for use in query
		Time t = new Time();
		t.setToNow();

		//Getting URI for calendar
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true)); //start time = now
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true)+(MiliSecDay*7));//End time = (now + 1 week)
		Uri eventsUri = eventsUriBuilder.build();

		//Fill cursor with desired calendar events.
		mCursor = getContentResolver().query(eventsUri, COLS, null, null, CalendarContract.Instances.DTSTART + " ASC");
		mCursor.moveToFirst();

		ArrayList<CardTemplate> cardList = new ArrayList<CardTemplate>();

		boolean makeCards = true;

		while(makeCards)
		{
			EventCard newCard = getEvent();
			if (newCard.getTitle() == "allDay")
			{
				//skip
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
		cardList.add(new TaskCard("sample task"));

		//GET TASKS HERE

		//SORT TASKS + EVENTS TOGETHER HERE

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

		// draw cards
		CardView.refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{        
		case R.id.new_event:
			Toast.makeText(getApplicationContext(), "Unimplemented", Toast.LENGTH_SHORT).show();
			return true;        
		default:            
			return super.onOptionsItemSelected(item);

		}
	}

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
			int color; //holds custom color case
			String colorKey, colorKey2;
			String desc;
			String eventId;
			boolean last = false;

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
			event = new EventCard(title, desc, start, end, colorKey, colorKey2, false, true, eventId, last);

			return event;
		}
		catch(Exception ex)
		{
			return new EventCard("no event");
		}
	}

}
