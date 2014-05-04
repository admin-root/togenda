package edu.jcu.cs470.togenda;

import com.terlici.dragndroplist.DragNDropCursorAdapter;
import com.terlici.dragndroplist.DragNDropListView;

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
		// Inflate the layout for getActivity() fragment
		myFragmentView = inflater.inflate(R.layout.todo_fragment, container, false);
		db = new DBAdapter(getActivity());
		//String[] names = getResources().getStringArray(R.array.random_names);
		//ArrayList<String> list = new ArrayList<String>(Arrays.asList(names));
		db.open();
		Cursor TaskCursor = db.getAllTasks();

		Log.d("test", "test");

		DragNDropListView list = (DragNDropListView) getActivity().findViewById(android.R.id.list);

		DragNDropCursorAdapter adapter = new DragNDropCursorAdapter(getActivity().getApplicationContext(),
				R.layout.task_list_item,
				TaskCursor,
				new String[]{"text"},
				new int[]{R.id.textView1},
				R.id.handler);

		list.setDragNDropAdapter(adapter);

		db.close();


		return myFragmentView;
	}

}
