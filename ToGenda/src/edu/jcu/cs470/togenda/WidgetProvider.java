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
import android.util.Log;
import android.widget.RemoteViews;

//A basic widget that displays the user's next task.

public class WidgetProvider extends AppWidgetProvider{

	static DBAdapter db;
	int[] allWidgetIds;

	@SuppressWarnings("unchecked")
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d("WidgetProvider", "onUpdate");
		// Get all ids
		ComponentName thisWidget = new ComponentName(context,
				WidgetProvider.class);
		allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		
		if (appWidgetIds.length > 0){
			for (int widgetId : allWidgetIds) {
				Log.d("WidgetProvider", "for WidgetIDs");
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
						Log.d("WidgetProvider", "while");
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


				//SORT TASKS HERE
				Collections.sort(TaskList);

				//Launch app when widget is clicked.
				Intent intent = new Intent(context, MainActivity.class);			
				intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
				remoteViews.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);
				appWidgetManager.updateAppWidget(widgetId, remoteViews);
				Log.d("WidgetProvider", "done");
			}
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
