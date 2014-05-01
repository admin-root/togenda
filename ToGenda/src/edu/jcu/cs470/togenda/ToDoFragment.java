package edu.jcu.cs470.togenda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ToDoFragment extends Fragment {

	private View myFragmentView;
	DragSortListView listView;
	ToDoListAdapter adapter;
	List<DragListItem> DragListItems;
	DBAdapter db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for getActivity() fragment

		myFragmentView = inflater.inflate(R.layout.todo_fragment, container, false);
		db = new DBAdapter(getActivity());
		
		listView = (DragSortListView) myFragmentView.findViewById(R.id.draglistview);
	    //String[] names = getResources().getStringArray(R.array.random_names);
	    //ArrayList<String> list = new ArrayList<String>(Arrays.asList(names));
		
		DragListItems = new ArrayList<DragListItem>();
		
		db.open();
		Cursor TaskCursor = db.getAllTasks();
		
		try{

			boolean makeCards = false;
			if (TaskCursor != null)
			{
				makeCards = true;
			}
			while(makeCards)
			{
					DragListItem items = new DragListItem(TaskCursor.getString(1), TaskCursor.getString(2), TaskCursor.getLong(3), TaskCursor.getInt(4), TaskCursor.getInt(5));
					DragListItems.add(items);
				
				if(TaskCursor.isLast()) 
				{
					makeCards = false;
				}
				else
				{
					TaskCursor.moveToNext();
				}
			}
		}
		catch(Exception E)
		{
		}
		db.close();
		
		
		
	    
	    adapter = new ToDoListAdapter(myFragmentView.getContext(), DragListItems);
	    listView.setAdapter(adapter);
	    listView.setDropListener(onDrop);
	    listView.setRemoveListener(onRemove);
	    

	    DragSortController controller = new DragSortController(listView);
	    controller.setDragHandleId(R.id.imageView1);
	            //controller.setClickRemoveId(R.id.);
	    controller.setRemoveEnabled(false);
	    controller.setSortEnabled(true);
	    controller.setDragInitMode(1);
	            //controller.setRemoveMode(removeMode);

	    listView.setFloatViewManager(controller);
	    listView.setOnTouchListener(controller);
	    listView.setDragEnabled(true);

		return myFragmentView;
	}
	
	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener()
	{
	    @Override
	    public void drop(int from, int to)
	    {
	        if (from != to)
	        {
	            Object item = adapter.getItem(from);
	            adapter.remove(item);
	            adapter.insert(item, to);
	        }
	    }
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener()
	{
	    @Override
	    public void remove(int which)
	    {
	        adapter.remove(adapter.getItem(which));
	    }
	};

}
