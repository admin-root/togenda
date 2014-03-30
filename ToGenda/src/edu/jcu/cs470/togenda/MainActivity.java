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
import java.util.Date;

import android.content.ContentUris;
import android.database.Cursor; 
import android.provider.CalendarContract; 
import android.provider.CalendarContract.Calendars;
import android.text.format.DateFormat;
import android.text.format.Time;

public class MainActivity extends Activity {

	private Cursor mCursor = null; private static final String[] COLS = new String[]{ CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.CALENDAR_COLOR_KEY, CalendarContract.Events.DESCRIPTION};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init CardView
		CardUI CardView = (CardUI) findViewById(R.id.cardsview);
		CardView.setSwipeable(true);
		
		Format df = DateFormat.getDateFormat(this);
		Format tf = DateFormat.getTimeFormat(this); 
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();

		TextView timedate = (TextView)findViewById(R.id.TimeandDate);
		timedate.setText(sdf.format(d)+", "+tf.format(d)+", "+df.format(d));
		
		//mCursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, COLS, null, null, null);
		
		
		//Cursor cur = null;
	    //String selection = "((" + CalendarContract.Events.DTSTART + " >= ?) AND (" + CalendarContract.Events.DTEND + " <= ?))";
	    Time t = new Time();
	    t.setToNow();
	    
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true));
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true)+86400000);
		Uri eventsUri = eventsUriBuilder.build();
		//Cursor cursor = null;       
		mCursor = getContentResolver().query(eventsUri, COLS, null, null, CalendarContract.Instances.DTSTART + " ASC");
		mCursor.moveToFirst();

		ArrayList<EventCard> cardList = new ArrayList<EventCard>();

		boolean makeCards = true;
		
		while(makeCards)
		{
			EventCard newCard = getEvent();
			if (newCard.getTitle() != "null")
			{
				cardList.add(newCard);//new EventCard(eventtext));
			}
			else
			{
				makeCards = false;
			}

		}

		//get tasks

		//Add sort here.

		if (!cardList.isEmpty())
		{
			for (int cards = cardList.size(); cards >= 1; cards--)
			{
				CardView.addCard(cardList.get(cards-1));
			}
		}

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
		//TextView tv = (TextView)findViewById(R.id.data);
		try
		{
			EventCard event;
			String title;
			Long start;
			Long end;
			String color;
			String desc;

			Format df = DateFormat.getDateFormat(this);
			Format tf = DateFormat.getTimeFormat(this); 

			if(!mCursor.isLast()) 
			{
				mCursor.moveToNext();

				try {
					//CalendarContract.Events.TITLE
					title = mCursor.getString(0);
					//CalendarContract.Events.DTSTART
					start = mCursor.getLong(1);
					//CalendarContract.Events.DTEND
					end = mCursor.getLong(2);
					//CalendarContract.Events.EVENT_COLOR
					color = mCursor.getString(3);
					//CalendarContract.Events.DESCRIPTION
					desc = mCursor.getString(4);
					

				} catch (Exception e) {
					//ignore
					return new EventCard("null");
				}
			}
			else
			{
				
				return new EventCard("null");
			}
			event = new EventCard(title, desc, start, end, color, false, true);
			return event;
		}
		catch(Exception ex)
		{
			return new EventCard("null");
		}
	}

}
