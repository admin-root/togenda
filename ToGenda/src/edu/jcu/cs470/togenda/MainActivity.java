package edu.jcu.cs470.togenda;


import com.fima.cardsui.views.CardUI;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.text.Format; 
import java.util.ArrayList;
import android.database.Cursor; 
import android.provider.CalendarContract; 
import android.text.format.DateFormat;

public class MainActivity extends Activity {

	private Cursor mCursor = null; private static final String[] COLS = new String[]{ CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init CardView
		CardUI CardView = (CardUI) findViewById(R.id.cardsview);
		CardView.setSwipeable(true);

		mCursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, COLS, null, null, null);
		mCursor.moveToFirst();

		ArrayList<EventCard> cardList = new ArrayList<EventCard>();

		for (int e = 0; e <=9; e++)
		{
			String eventtext = getEvent();
			if (eventtext != "no event")
			{
				cardList.add(new EventCard(eventtext));
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
		CardView.addCard(new TaskCard("Task Name"));
		CardView.addCard(new TaskCard("Stack Event"));
		CardView.addCardToLastStack(new EventCard("Stack Event"));
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


	public String getEvent() {
		//TextView tv = (TextView)findViewById(R.id.data);
		try
		{
			String event;

			String title = "N/A";

			Long start = 0L;

			Format df = DateFormat.getDateFormat(this);
			Format tf = DateFormat.getTimeFormat(this); 

			if(!mCursor.isLast()) 
			{
				mCursor.moveToNext();

				try {
					title = mCursor.getString(0);

					start = mCursor.getLong(1);

				} catch (Exception e) {
					//ignore
					return "no event";
				}
			}
			else
			{
				return "no event";
			}

			event = (title+" on "+df.format(start)+" at "+tf.format(start));
			return event;
		}
		catch(Exception ex)
		{
			return "no event";
		}
	}

}
