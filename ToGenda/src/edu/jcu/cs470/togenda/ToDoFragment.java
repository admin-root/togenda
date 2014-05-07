package edu.jcu.cs470.togenda;


import com.terlici.dragndroplist.DragNDropCursorAdapter;
import com.terlici.dragndroplist.DragNDropListView;
import com.terlici.dragndroplist.DragNDropListView.OnItemDragNDropListener;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//Displays the Application's ToDo list and allows for sorting.

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

		if (TaskCursor.getCount() > 0){
			list = (DragNDropListView) myFragmentView.findViewById(android.R.id.list);

			Log.d("cursor", TaskCursor.getString(1));

			adapter = new DragNDropCursorAdapter(myFragmentView.getContext(),
					R.layout.task_list_item,
					TaskCursor,
					new String[]{DBAdapter.COLUMN_NAME, DBAdapter.COLUMN_CONTENT},
					new int[]{R.id.TaskItemTitle, R.id.textView3},
					R.id.DragHandle);

			Log.d("apater", "test");

			list.setDragNDropAdapter(adapter);
			list.setOnItemDragNDropListener(new OnItemDragNDropListener() {

				public void onItemDrop(DragNDropListView parent, View view,
						int startPosition, int endPosition, long id) {

					db = new DBAdapter(getActivity());
					db.open();
					Cursor TaskCursor = db.getAllTasks();

					boolean sort = true;

					while (sort)
					{
						Log.d("while", "loop");
						if (startPosition > endPosition)
						{
							if (TaskCursor.getInt(5) == startPosition)
							{
								db.updateTask(TaskCursor.getInt(0), TaskCursor.getString(1), TaskCursor.getString(2), TaskCursor.getLong(3), TaskCursor.getInt(4), endPosition, TaskCursor.getInt(6));
							}
							else if (TaskCursor.getInt(5) >= endPosition && TaskCursor.getInt(5) <= startPosition)
							{
								db.updateTask(TaskCursor.getInt(0), TaskCursor.getString(1), TaskCursor.getString(2), TaskCursor.getLong(3), TaskCursor.getInt(4), TaskCursor.getInt(5)+1, TaskCursor.getInt(6));
							}
						}
						else if (startPosition < endPosition)
						{
							if (TaskCursor.getInt(5) == startPosition)
							{
								db.updateTask(TaskCursor.getInt(0), TaskCursor.getString(1), TaskCursor.getString(2), TaskCursor.getLong(3), TaskCursor.getInt(4), endPosition, TaskCursor.getInt(6));
							}
							else if (TaskCursor.getInt(5) <= endPosition && TaskCursor.getInt(5) >= startPosition)
							{
								db.updateTask(TaskCursor.getInt(0), TaskCursor.getString(1), TaskCursor.getString(2), TaskCursor.getLong(3), TaskCursor.getInt(4), TaskCursor.getInt(5)-1, TaskCursor.getInt(6));
							}

						}
						
						if (TaskCursor.isLast())
						{
							sort = false;
						}
						else
						{
							TaskCursor.moveToNext();
						}
					}

					db.close();

				}

				public void onItemDrag(DragNDropListView parent, View view, int position,
						long id) {
					// TODO Auto-generated method stub

				}
			});
		}
		Log.d("apater", "test");
		db.close();

		return myFragmentView;
	}
}
