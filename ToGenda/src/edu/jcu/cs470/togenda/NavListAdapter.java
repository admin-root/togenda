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

//Adaptor for list items in the navigation drawer.
//sets icons and text labels.

public class NavListAdapter extends BaseAdapter {

	Context context;
	List<RowItem> rowItem;

	public Typeface roboto;
	
	NavListAdapter(Context context, List<RowItem> rowItem) {
		this.context = context;
		this.rowItem = rowItem;
	}

	private class ViewHolder {
		ImageView icon;
		TextView title;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.navItemIcon);
			holder.title = (TextView) convertView.findViewById(R.id.navItemText);

			RowItem row_pos = rowItem.get(position);
			// setting the image resource and title
			holder.icon.setImageResource(row_pos.getIcon());
			holder.title.setText(row_pos.getTitle());
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			roboto = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf"); 
			holder.title.setTypeface(roboto);
		}
		else
		{
			roboto = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf"); 
			holder.title.setTypeface(roboto);
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

}