package edu.jcu.cs470.togenda;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ToDoListAdapter extends BaseAdapter {

	Context context;
	List<DragListItem> rowItem;
	
	ToDoListAdapter(Context context, List<DragListItem> rowItem) {
		this.context = context;
		this.rowItem = rowItem;
	}

	private class ViewHolder {
		ImageView icon;
		TextView title;
		TextView Due;
		TextView Desc;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.task_list_item, null);
			holder = new ViewHolder();
			//holder.icon = (ImageView) convertView.findViewById(R.id.navItemIcon);
			holder.title = (TextView) convertView.findViewById(R.id.textView1);
			holder.Due = (TextView) convertView.findViewById(R.id.textView2);
			holder.Desc = (TextView) convertView.findViewById(R.id.textView3);
			
			DragListItem row_pos = rowItem.get(position);
			// setting the image resource and title
			holder.title.setText(row_pos.getTitle());
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;

	}

	@Override
	public int getCount() {
		return rowItem.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rowItem.indexOf(getItem(position));
	}

	public void remove(Object item) {
		// TODO Auto-generated method stub
		rowItem.remove(item);
	}

	public void insert(Object item, int to) {
		// TODO Auto-generated method stub
		rowItem.add(to, (DragListItem) item);
	}

}