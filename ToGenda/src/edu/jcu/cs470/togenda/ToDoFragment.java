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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ToDoFragment extends Fragment {

	private View myFragmentView;
	DBAdapter db;
	DragNDropCursorAdapter  adapter;
	DragNDropListView list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.todo_fragment, container, false);
		db = new DBAdapter(getActivity());
		db.open();
		Cursor TaskCursor = db.getAllTasks();
		
		list = (DragNDropListView) myFragmentView.findViewById(android.R.id.list);

		Log.d("cursor", TaskCursor.getString(1));
		
		adapter = new DragNDropCursorAdapter(myFragmentView.getContext(),
		                           R.layout.task_list_item,
		                           TaskCursor,
		                           new String[]{DBAdapter.COLUMN_NAME, DBAdapter.COLUMN_CONTENT, DBAdapter.COLUMN_DUE},
		                           new int[]{R.id.TaskItemTitle, R.id.textView3, R.id.textView2},
		                           R.id.DragHandle);

		Log.d("apater", "test");
		
		list.setDragNDropAdapter(adapter);
		
		Log.d("apater", "test");
		
		db.close();
		
		return myFragmentView;
	}
}
