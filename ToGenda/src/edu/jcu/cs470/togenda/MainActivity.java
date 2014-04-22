package edu.jcu.cs470.togenda;
/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.squareup.timessquare.CalendarPickerView;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */



public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	public Drawable backgroundColor;
	public AlertDialog alertDialog;
	public int gPosition = 0;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] pageList;

	private List<RowItem> rowItems;
	private NavListAdapter adapter;
	String[] menutitles;
	TypedArray menuIcons;

	public Typeface robotoLight;
	public Typeface robotoBold;

	TaskCreator taskCreator;
	EventCard eventCard;
	DBAdapter db;

	private static final int MiliSecDay = 86400000;	//Number of Milliseconds in a day.
	private Cursor mCursor = null;
	private static final String[] COLS = new String[]{ CalendarContract.Instances.EVENT_ID, 
		CalendarContract.Instances.TITLE,  CalendarContract.Events.DESCRIPTION, CalendarContract.Instances.START_DAY, 
		CalendarContract.Instances.BEGIN, CalendarContract.Instances.END, CalendarContract.Instances.END_MINUTE, 
		CalendarContract.Instances.EVENT_COLOR_KEY, CalendarContract.Events.CALENDAR_COLOR_KEY, CalendarContract.Instances.EVENT_COLOR, 
		CalendarContract.Events.ALL_DAY};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		pageList = getResources().getStringArray(R.array.navItemList);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		robotoLight=Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
		robotoBold=Typeface.createFromAsset(getAssets(),"fonts/Roboto-Black.ttf");

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		//mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		rowItems = new ArrayList<RowItem>();

		menutitles = getResources().getStringArray(R.array.navItemList);
		menuIcons = getResources().obtainTypedArray(R.array.navIconList);

		for (int i = 0; i < menutitles.length; i++) {
			RowItem items = new RowItem(menutitles[i], menuIcons.getResourceId(
					i, -1));
			rowItems.add(items);
		}

		menuIcons.recycle();

		adapter = new NavListAdapter(getApplicationContext(), rowItems);

		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setIcon(R.drawable.ic_event);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_navigation_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				//getActionBar().setTitle(mTitle);
				getActionBar().setTitle(pageList[gPosition]);
				if (gPosition == 0)
				{
					getActionBar().setIcon(R.drawable.ic_event);
				}
				else if (gPosition == 1)
				{
					getActionBar().setIcon(R.drawable.ic_list);
				}
				else if (gPosition == 2)
				{
					getActionBar().setIcon(R.drawable.ic_date);
				}
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				getActionBar().setIcon(R.drawable.ic_launcher);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) 
		{
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.new_event).setVisible(!drawerOpen&&gPosition!=2);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {
		case R.id.new_event:
			Intent intent = new Intent(this, TaskCreator.class);
			startActivity(intent);
			//			//danny workspace
			//			//get title
			//			EditText taskName = (EditText)findViewById(R.id.taskTitle);
			//			String title = taskName.getText().toString();
			//			//get content
			//			EditText taskContent = (EditText)findViewById(R.id.taskInfo);
			//			String content = taskContent.getText().toString();
			//			//get date
			//			Long date = taskCreator.getDate();
			//			//get color ID
			//			int colorId = 4; //eventCard.getColorID();
			//			int priority = 1; //test values
			//			db.open();
			//			db.insertBlogger(title, content, date, colorId, priority);
			//			db.close();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			selectItem(position);
			setNavDrawerItemNormal();
			TextView txtview = ((TextView) view.findViewById(R.id.navItemText));
			txtview.setTypeface(robotoBold);
		}
	}

	public void setNavDrawerItemNormal()
	{
		for (int i=0; i< mDrawerList.getChildCount(); i++)
		{
			View v = mDrawerList.getChildAt(i);
			TextView txtview = ((TextView) v.findViewById(R.id.navItemText));
			txtview.setTypeface(robotoLight);
		}
	}

	private void selectItem(int position) {

		Fragment newFragment;
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		switch (position) {
		case 0:
			newFragment = new AgendaFragment();
			transaction.replace(R.id.content_frame, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case 1:
			newFragment = new ToDoFragment();
			transaction.replace(R.id.content_frame, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case 2:
			newFragment = new CalendarFragment();
			transaction.replace(R.id.content_frame, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case 3:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		gPosition = position;
		setTitle(pageList[position]);
		mDrawerLayout.closeDrawer(mDrawerList);  
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void openCal(View v){
		//Log.d(getTag(), "Selected time in millis: " + calendar.getSelectedDate().getTime());
		Long ldate = ((CalendarPickerView) findViewById(R.id.calendar_view)).getSelectedDate().getTime();

		String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(ldate));

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(dateString);
		LayoutInflater inflater = this.getLayoutInflater();

		View mydialogview = inflater.inflate(R.layout.day_agenda, null);


		alertDialog.setView(mydialogview);
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//do nothing
			}
		});
		alertDialog.setIcon(R.drawable.ic_date_dark);
		alertDialog.show();

		// init CardView
		CardUI CardView = (CardUI) mydialogview.findViewById(R.id.cardsviewday);
		CardView.setSwipeable(false); //Global variable for now. Need to change library so we can set swipable on per-card basis.

		//Date formating

		//getting current time for use in query

		//Getting URI for calendar
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(eventsUriBuilder, ldate); //start time = now
		ContentUris.appendId(eventsUriBuilder, ldate+(MiliSecDay));//End time = (now + 1 week)
		Uri eventsUri = eventsUriBuilder.build();

		//Fill cursor with desired calendar events.
		mCursor = this.getContentResolver().query(eventsUri, COLS, null, null, ldate + " ASC");
		mCursor.moveToFirst();

		ArrayList<CardTemplate> cardList = new ArrayList<CardTemplate>();

		boolean makeCards = true;

		CardStack stack = new CardStack();

		while(makeCards)
		{		
			EventCard newCard = getEvent();
			if (newCard.AllDay == true)
			{
				if (newCard.startTime <= ldate)
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

		Collections.sort(cardList);

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

		if (cardList.isEmpty())
		{
			//Create cardtype that explains that there are no current events.
		}

		// draw cards
		CardView.refresh();


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