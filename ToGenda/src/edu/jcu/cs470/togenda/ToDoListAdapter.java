package edu.jcu.cs470.togenda;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobeta.android.dslv.*;

public class ToDoListAdapter extends DragSortCursorAdapter {

	Context context;
	List<DragListItem> rowItem;
	DBAdapter db;

	//	public ToDoListAdapter(Context context, List<DragListItem> rowItem) {
	//		this.context = context;
	//		this.rowItem = rowItem;
	//	}

	public ToDoListAdapter(Context context, Cursor c) {
		super(context, c);

		try{

			boolean makeCards = false;
			if (c != null)
			{
				makeCards = true;
			}
			while(makeCards)
			{
				DragListItem items = new DragListItem(c.getString(1), c.getString(2), c.getLong(3), c.getInt(4), c.getInt(5));
				rowItem.add(items);

				if(c.isLast()) 
				{
					makeCards = false;
				}
				else
				{
					c.moveToNext();
				}
			}
		}
		catch(Exception E)
		{
		}

	}

	public ToDoListAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	public ToDoListAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	private class ViewHolder {
		ImageView icon;
		TextView title;
		TextView Due;
		TextView Desc;
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public DragListItem getItem(int from)
	{
		return rowItem.get(from);
	}

	public void remove(DragListItem item) {
		// TODO Auto-generated method stub
		rowItem.remove(item);
	}

	public void insert(DragListItem item, int to) {
		// TODO Auto-generated method stub
		rowItem.add(to, item);
	}

}