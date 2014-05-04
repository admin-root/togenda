package edu.jcu.cs470.togenda;

import java.util.ArrayList;
import java.util.Collections;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.ImageView;
import android.widget.RemoteViews;


public class WidgetProvider extends AppWidgetProvider{

	static DBAdapter db;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// Get all ids
		ComponentName thisWidget = new ComponentName(context,
				WidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			db = getDatabaseHelper(context);

			db.open();
			Cursor TaskCursor = db.getAllTasks();

			ArrayList<CardTemplate> TaskList = new ArrayList<CardTemplate>();

			Boolean makeCards = false;
			try{

				if (TaskCursor != null)
				{
					makeCards = true;
				}
				while(makeCards)
				{
					TaskList.add(new TaskCard(TaskCursor.getInt(0),TaskCursor.getString(1), TaskCursor.getString(2), TaskCursor.getLong(3),
							TaskCursor.getInt(4),TaskCursor.getInt(5)));
					if(TaskCursor.isLast()) 
					{
						makeCards = false;
					}
					else
					{
						TaskCursor.moveToNext();
					}
				}
				remoteViews.setTextViewText(R.id.WidgetLabel, TaskList.get(0).getTitle());
				remoteViews.setTextViewText(R.id.description, TaskList.get(0).getDesc());
				remoteViews.setTextViewText(R.id.Time, TaskList.get(0).getTime());
				remoteViews.setImageViewResource(R.id.navItemIcon, R.drawable.ic_bell_on_dark);
			}
			catch(Exception E)
			{
				remoteViews.setTextViewText(R.id.WidgetLabel, "No tasks.");
				remoteViews.setTextViewText(R.id.description, "You're all caught up!");
				remoteViews.setImageViewResource(R.id.navItemIcon, R.drawable.ic_bell_off_dark);
			}
			db.close();


			//SORT TASKS + EVENTS TOGETHER HERE
			Collections.sort(TaskList);

			// Set the text




			// Register an onClickListener
			Intent intent = new Intent(context, WidgetProvider.class);

			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.WidgetLabel, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	private static DBAdapter getDatabaseHelper(Context context) {

		if (db == null) {
			db = new DBAdapter(context);
			db.open();
		}
		return db;
	}

}
