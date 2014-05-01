package edu.jcu.cs470.togenda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ToDoFragment extends Fragment {

	private View myFragmentView;
	DragSortListView listView;
	ToDoListAdapter adapter;
	DBAdapter db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for getActivity() fragment
		Log.d("test", "test");
		myFragmentView = inflater.inflate(R.layout.todo_fragment, container, false);
		db = new DBAdapter(getActivity());
		Log.d("test", "test");
		listView = (DragSortListView) myFragmentView.findViewById(R.id.draglistview);
		//String[] names = getResources().getStringArray(R.array.random_names);
		//ArrayList<String> list = new ArrayList<String>(Arrays.asList(names));
		Log.d("test", "test");
		db.open();
		Cursor TaskCursor = db.getAllTasks();

		Log.d("test", "test");

		adapter = new ToDoListAdapter(myFragmentView.getContext(), TaskCursor);
		db.close();
		listView.setAdapter(adapter);
		listView.setDropListener(onDrop);
		listView.setRemoveListener(onRemove);
		
		Log.d("test", "test");

		DragSortController controller = new DragSortController(listView);
		controller.setDragHandleId(R.id.imageView1);
		//controller.setClickRemoveId(R.id.);
		controller.setRemoveEnabled(false);
		controller.setSortEnabled(true);
		controller.setDragInitMode(1);
		//controller.setRemoveMode(removeMode);
		
		Log.d("test", "test");
		
		listView.setFloatViewManager(controller);
		listView.setOnTouchListener(controller);
		listView.setDragEnabled(true);
		
		Log.d("test", "test");
		
		return myFragmentView;
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener()
	{
		
		@Override
		public void drop(int from, int to)
		{
			Log.d("DragListener", "test 1");
			if (from != to)
			{
				Log.d("DragListener", "test 2");
				DragListItem item = adapter.getItem(from);
				adapter.remove(item);
				adapter.insert(item, to);
				Log.d("DragListener", "test 3");
			}
		}
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener()
	{
		@Override
		public void remove(int which)
		{
			Log.d("DropListener", "test 1");
			adapter.remove(adapter.getItem(which));
			Log.d("DropListener", "test 2");
		}
	};

}
