package edu.jcu.cs470.togenda;

import java.util.Calendar;
import java.util.Date;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class CalendarFragment extends Fragment{

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
		calendar.init(today, nextYear.getTime()).inMode(SelectionMode.SINGLE).withSelectedDate(today);
		
		

		return myFragmentView;
	}
	public void openCal(View v){
		//Log.d(getTag(), "Selected time in millis: " + calendar.getSelectedDate().getTime());
        //Toast.makeText(getActivity(), "Selected: " + calendar.getSelectedDate().getTime(), Toast.LENGTH_SHORT).show();
	}

}
