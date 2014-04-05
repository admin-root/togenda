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
import android.widget.TextView;

import java.text.Format; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor; 
import android.provider.CalendarContract; 
import android.provider.CalendarContract.Calendars;
import android.text.format.DateFormat;
import android.text.format.Time;

public class MainActivity extends Activity {

	private Cursor mCursor = null; 
	
	//Contains all columns we are to recieve from Google Calendar.
	private static final String[] COLS = new String[]{ CalendarContract.Instances.EVENT_ID, 
		CalendarContract.Instances.TITLE,  CalendarContract.Events.DESCRIPTION, CalendarContract.Instances.START_DAY, 
		CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.END_MINUTE, 
		CalendarContract.Instances.EVENT_COLOR_KEY, CalendarContract.Events.CALENDAR_COLOR_KEY, CalendarContract.Instances.EVENT_COLOR};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init CardView
		CardUI CardView = (CardUI) findViewById(R.id.cardsview);
		CardView.setSwipeable(true); //Global variable for now. Need to change library so we can set swipable on per-card basis.

		//Date formating
		Format df = DateFormat.getDateFormat(this);
		Format tf = DateFormat.getTimeFormat(this); 
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();

		//setting TimeDate label
		TextView timedate = (TextView)findViewById(R.id.TimeandDate);
		timedate.setText(sdf.format(d)+", "+tf.format(d)+", "+df.format(d));
		
		//getting current time for use in query
		Time t = new Time();
		t.setToNow();

		//Getting URI for callendar
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true)); //start time = now
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true)+604800000);//End time = (now + 1 week)
		Uri eventsUri = eventsUriBuilder.build();
		
		//Fill cursor with desired calendar events.
		mCursor = getContentResolver().query(eventsUri, COLS, null, null, CalendarContract.Instances.DTSTART + " ASC");
		mCursor.moveToFirst();

		ArrayList<EventCard> cardList = new ArrayList<EventCard>();

		boolean makeCards = true;

		while(makeCards)
		{
			EventCard newCard = getEvent();
			if (newCard.getTitle() != "no event") //"no event" == try-catch block
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


		Collections.sort(cardList); //Doesn't yet work.

		//GET TASKS HERE

		//SORT TASKS + EVENTS TOGETHER HERE

		if (!cardList.isEmpty())
		{
			for (int cards = cardList.size(); cards >= 1; cards--)
			{
				CardView.addCard(cardList.get(cards-1));	//Draws cards on Card View.
			}
		}

		//Example cards below.
		
		//		// add AndroidViews Cards
		//		eventCardView.addCard(new MyCard("Get the CardsUI view"));
		//		eventCardView.addCardToLastStack(new MyCard("for Android at"));
		//		MyCard androidViewsCard = new MyCard("www.androidviews.net");
		//		androidViewsCard.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//				Intent intent = new Intent(Intent.ACTION_VIEW);
		//				intent.setData(Uri.parse("http://www.androidviews.net/"));
		//				startActivity(intent);
		//
		//			}
		//		});
		//		eventCardView.addCardToLastStack(androidViewsCard);
		//
		//		// add one card, and then add another one to the last stack.
		//		eventCardView.addCard(new MyCard("2 cards"));
		//		eventCardView.addCardToLastStack(new MyCard("2 cards"));
		//
		//		// add one card
		//		CardView.addCard(new TaskCard("Task Name"));
		//		CardView.addCard(new TaskCard("Stack Event"));
		//		CardView.addCardToLastStack(new EventCard("Stack Event"));
		//
		//		// create a stack
		//		CardStack stack = new CardStack();
		//		stack.setTitle("title test");
		//
		//		// add 3 cards to stack
		//		stack.add(new MyCard("3 cards"));
		//		stack.add(new MyCard("3 cards"));
		//		stack.add(new MyCard("3 cards"));
		//
		//		// add stack to cardView
		//		eventCardView.addStack(stack);

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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			int color;
			String colorKey, colorKey2;
			String desc;
			String eventId;
			boolean last = false;

			try {
				//				OLD
				//				//CalendarContract.Events.TITLE
				//				title = mCursor.getString(0);
				//				//CalendarContract.Events.DTSTART
				//				start = mCursor.getLong(1);
				//				//CalendarContract.Events.DTEND
				//				end = mCursor.getLong(2);
				//				//CalendarContract.Events.EVENT_COLOR
				//				color = mCursor.getString(3);
				//				//CalendarContract.Events.DESCRIPTION
				//				desc = mCursor.getString(4);
				//				//CalendarContract.Events._ID
				//				eventId = mCursor.getString(5);

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
		event = new EventCard(title, desc, start, end, colorKey, colorKey2, false, true, eventId, last);

		return event;
	}
	catch(Exception ex)
	{
		return new EventCard("no event");
	}
}

}
