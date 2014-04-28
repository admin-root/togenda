package edu.jcu.cs470.togenda;

import java.sql.Time;
import java.text.SimpleDateFormat;

import com.fima.cardsui.views.CardUI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class TaskCard extends CardTemplate implements Comparable{

	String title;
	String description;
	Long startTime;
	Long endTime;
	String startLabel;
	String endLabel;
	boolean last = false;
	DBAdapter db;

	int[] colors;
	//	private OnCardSwiped onCardSwipedListener;
	//	private String eventId = "0";

	public TaskCard(String thisTitle){
		title = thisTitle;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_play;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.EventLabel)).setText(title);
		((TextView) convertView.findViewById(R.id.description)).setText(description);
		//((TextView) convertView.findViewById(R.id.Time)).setText(startLabel + " - " + endLabel);

		//		if (color!=null){
		//
		//			if (Integer.parseInt(color) < 25 && Integer.parseInt(color) > 0) //Later: compensate for custom colors
		//			{
		//				//Chooses color based on google defualts.
		//				colors = new int[25];
		//				colors[1] = R.color.gCal1;
		//				colors[2] = R.color.gCal2;
		//				colors[3] = R.color.gCal3;
		//				colors[4] = R.color.gCal4;
		//				colors[5] = R.color.gCal5;
		//				colors[6] = R.color.gCal6;
		//				colors[7] = R.color.gCal7;
		//				colors[8] = R.color.gCal8;
		//				colors[9] = R.color.gCal9;
		//				colors[10] = R.color.gCal10;
		//				colors[11] = R.color.gCal11;
		//				colors[12] = R.color.gCal12;
		//				colors[13] = R.color.gCal13;
		//				colors[14] = R.color.gCal14;
		//				colors[15] = R.color.gCal15;
		//				colors[16] = R.color.gCal16;
		//				colors[17] = R.color.gCal17;
		//				colors[18] = R.color.gCal18;
		//				colors[19] = R.color.gCal19;
		//				colors[20] = R.color.gCal20;
		//				colors[21] = R.color.gCal21;
		//				colors[22] = R.color.gCal22;
		//				colors[23] = R.color.gCal23;
		//				colors[24] = R.color.gCal24;
		//				((LinearLayout) convertView.findViewById(R.id.background)).setBackgroundResource(colors[Integer.parseInt(color)]);
		//			}
		//		}
	}

	//	@Override
	//	public void setOnCardSwipedListener(OnCardSwiped onEpisodeSwipedListener) {
	//		this.onCardSwipedListener = onEpisodeSwipedListener;
	//	}

	@SuppressLint("SimpleDateFormat")
	public TaskCard(final int taskID, String titlePlay, String descText, final long due, String color, String priority, final Context c) {
		this.title = titlePlay;
		this.description = descText;
		@SuppressWarnings("unused")
		SimpleDateFormat _12HourSDFwDM = new SimpleDateFormat("M/d h:mm a");
		SimpleDateFormat _12HourSDF = new SimpleDateFormat("h:mm a");
		this.endTime = due;
		this.endLabel = _12HourSDF.format(new Time(due));
		this.color = color;

		this.hasOverflow = false;
		this.isClickable = true;
		//		this.eventId = eventId;
		db = new DBAdapter(c);
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
				alertDialog.setTitle(title);
				LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View dialogContent = inflater.inflate(R.layout.task_dialog, null);

				TextView tdDesc = (TextView) dialogContent.findViewById(R.id.td_desc);
				tdDesc.setText(description);
				if (due != 0)
				{
					TextView tdDue = (TextView) dialogContent.findViewById(R.id.td_due);
					tdDue.setText(String.valueOf(due));
				}
				alertDialog.setView(dialogContent);
				//interface
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Complete", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						db.open();
						db.deleteTask(taskID);
						db.close();
					}
				});
				alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Edit", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(c, TaskEditor.class);
						intent.putExtra("TaskID",taskID);
						c.startActivity(intent);
					}
				});
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Do nothing.
					}
				});
				alertDialog.setIcon(R.drawable.ic_time_dark);
				alertDialog.show();
			}
		});
	}

	public void setDescription(String desc)
	{
		this.description = desc;
	}

	public boolean isLast()
	{
		return last;
	}

	public long getStart()
	{
		return this.startTime;
	}

	public long getEnd()
	{
		return this.endTime;
	}

	@Override
	public int compareTo(Object another)  //for sorting
	{	
		if (this.getStart() < ((TaskCard) another).getStart())
		{
			return 1;
		}
		else if (this.getStart() > ((TaskCard) another).getStart())
		{
			return -1;
		}
		else if (this.getStart() == ((TaskCard) another).getStart())
		{
			if (this.getEnd() < ((TaskCard) another).getEnd())
			{
				return 1;
			}
			else if (this.getEnd() < ((TaskCard) another).getEnd())
			{
				return -1;
			}
		}
		return 0;
	}

	public TaskCard()
	{
		super();
	}
}
