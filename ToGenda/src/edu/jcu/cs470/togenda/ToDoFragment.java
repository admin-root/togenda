package edu.jcu.cs470.togenda;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ToDoFragment extends Fragment {

	private View myFragmentView;
	DBAdapter db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.todo_fragment, container, false);
		db = new DBAdapter(getActivity());
		//String[] names = getResources().getStringArray(R.array.random_names);
		//ArrayList<String> list = new ArrayList<String>(Arrays.asList(names));
		db.open();
		Cursor TaskCursor = db.getAllTasks();
		db.close();
		
		Log.d("test", "test");
		//controller.setRemoveMode(removeMode);\
		
		return myFragmentView;
	}
}
