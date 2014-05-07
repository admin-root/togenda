package edu.jcu.cs470.togenda;

import java.util.Calendar;
import java.util.Date;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class CalendarFragment extends Fragment{

	//Displays a calendar from lib and a button.
	//You can select a date on the calendar. When clicking the button, a dayAgenda intent for whichever day was selected.
	//button onclick listener is in main class.
	
	private View myFragmentView;
	public CalendarPickerView calendar;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for getActivity() fragment
		myFragmentView = inflater.inflate(R.layout.fragment_calendar, container, false);

		Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		calendar = (CalendarPickerView) myFragmentView.findViewById(R.id.calendar_view);
		Date today = new Date();
		calendar.init(today, nextYear.getTime()).inMode(SelectionMode.SINGLE).withSelectedDate(today);
		
		

		return myFragmentView;
	}

}
