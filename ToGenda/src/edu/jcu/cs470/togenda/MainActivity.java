package edu.jcu.cs470.togenda;

//AOSP code is for basic implementation for Navigation Drawer
//Advanced themeing and style was not included.
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
import java.util.ArrayList;
import java.util.List;

import com.squareup.timessquare.CalendarPickerView;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

//Main Class
//Handles drawer opening and closing. Selecting from Drawer, and switching between fragments

public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;	//Layout of Navigation Drawer
	private ListView mDrawerList;	//List view in DrawerLayout
	private ActionBarDrawerToggle mDrawerToggle;	//Toggles the Drawer Open and Close.

	//For task card Dialog
	public Drawable backgroundColor;
	public AlertDialog alertDialog;
	
	//Navigation Drawer and Action Bar labels
	public int gPosition = 0;	//Current Positions in NavBar list.
	private CharSequence mDrawerTitle;
	public CharSequence mTitle;
	private String[] pageList;	//Array

	private List<RowItem> rowItems;	//Of navigation drawer items
	private NavListAdapter adapter;	//Creates rowitems
	String[] menutitles;	//of menu icons
	TypedArray menuIcons;	//Icons for nav drawer list

	//Font faces.

	public Typeface robotoLight;
	public Typeface robotoBold;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = mDrawerTitle = getTitle();	//Actionbar text
		
		//NavDrawer stuff
		pageList = getResources().getStringArray(R.array.navItemList);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		//These  fonts are built into android 4.4
		//We include them in our assets for devices running below that.
		robotoLight=Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
		robotoBold=Typeface.createFromAsset(getAssets(),"fonts/Roboto-Black.ttf");

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		//mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		rowItems = new ArrayList<RowItem>();

		//Drawer list text + icons
		menutitles = getResources().getStringArray(R.array.navItemList);
		menuIcons = getResources().obtainTypedArray(R.array.navIconList);

		//Create drawer list items
		for (int i = 0; i < menutitles.length; i++) {
			RowItem items = new RowItem(menutitles[i], menuIcons.getResourceId(
					i, -1));
			rowItems.add(items);
		}

		menuIcons.recycle();
		
		//sets adaptor
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
		case R.id.new_event:	//if the user clicked the new event button
			Intent intent = new Intent(this, TaskCreator.class);
			startActivityForResult(intent, 1);	//new event intent
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			if (position != 3)	//position 3 is the settings intent, which should never be highlights
			{
				//highlight and font stuff.
				setNavDrawerItemNormal();
				TextView txtview = ((TextView) view.findViewById(R.id.navItemText));
				txtview.setTypeface(robotoBold);
			}
			selectItem(position);
		}
	}

	//Changes the font of unselected items to thin
	public void setNavDrawerItemNormal()
	{
		for (int i=0; i< mDrawerList.getChildCount(); i++)
		{
			View v = mDrawerList.getChildAt(i);
			TextView txtview = ((TextView) v.findViewById(R.id.navItemText));
			txtview.setTypeface(robotoLight);
		}
	}

	private void selectItem(int position) 
	{
		//switches to the correct fragment.
		
		Fragment newFragment;
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch (position) {
		case 0:
			newFragment = new AgendaFragment();
			transaction.replace(R.id.content_frame, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			gPosition = position;
			setTitle(pageList[position]);
			break;
		case 1:
			Log.d("Case1", "test");
			newFragment = new ToDoFragment();
			transaction.replace(R.id.content_frame, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			Log.d("Case1", "test");
			gPosition = position;
			setTitle(pageList[position]);
			break;
		case 2:
			newFragment = new CalendarFragment();
			transaction.replace(R.id.content_frame, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			gPosition = position;
			setTitle(pageList[position]);
			break;
		case 3:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
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

	public void openCal(View v)
	{
		//for the Calendar Fragment's button
		Long ldate = ((CalendarPickerView) findViewById(R.id.calendar_view)).getSelectedDate().getTime();	
		Intent intent = new Intent(this, DayAgenda.class);
		intent.putExtra("longdate",ldate);
		startActivity(intent);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//refresh the current fragment.
		FragmentTransaction tr = getFragmentManager().beginTransaction();
		if (gPosition == 0)
		{
			tr.replace(R.id.content_frame, new AgendaFragment());
		}
		else	//if not in agenda view, go to todo view.
		{
			tr.replace(R.id.content_frame, new ToDoFragment());
		}
		tr.commit();

	}
	
	@Override
	protected void onStop() {
		
	    android.os.Process.killProcess(android.os.Process.myPid());

	    super.onStop();
	}
}