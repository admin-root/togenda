package edu.jcu.cs470.togenda;

import java.util.Calendar;
import java.util.Date;

import com.squareup.timessquare.CalendarPickerView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalendarFragment extends Fragment {

	private View myFragmentView;
	private CalendarPickerView calendar;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for getActivity() fragment
		myFragmentView = inflater.inflate(R.layout.fragment_calendar, container, false);

		Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		calendar = (CalendarPickerView) myFragmentView.findViewById(R.id.calendar_view);
		Date today = new Date();
		calendar.init(today, nextYear.getTime()).withSelectedDate(today);

		return myFragmentView;
	}

}
