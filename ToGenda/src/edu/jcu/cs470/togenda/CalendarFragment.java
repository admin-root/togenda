package edu.jcu.cs470.togenda;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalendarFragment extends Fragment {

	private View myFragmentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for getActivity() fragment

		myFragmentView = inflater.inflate(R.layout.fragment_calendar, container, false);

		return myFragmentView;
	}

}
