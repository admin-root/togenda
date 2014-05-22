package edu.jcu.cs470.togenda;

import java.sql.Time;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//Card that displays a calendar event.

@SuppressWarnings("rawtypes")
public class EventCard extends CardTemplate implements Comparable{

	Long startTime;
	Long endTime;
	String startLabel;
	String endLabel;
	int colorKey, colorKey2;
	boolean last = false;
	boolean AllDay = false;

	int[] colors, colors2;
	public String eventId = "0";

	public EventCard(String title)	//Basic Constructor
	{
		super(title);
	}

	@Override
	protected int getCardLayoutId() //Defines which layout should be used when inflating.
	{
		return R.layout.card_ex;
	}

	@Override
	protected void applyTo(View convertView) 
	{
		((TextView) convertView.findViewById(R.id.EventLabel)).setText(titlePlay);
		//((TextView) convertView.findViewById(R.id.description)).setText(description);
		((TextView) convertView.findViewById(R.id.description)).setText("colorKey: "+ String.valueOf(colorKey) + " colorKey2: " + String.valueOf(colorKey2));
		if (AllDay)	//Different label if the event is an "all day" event.
		{
			((TextView) convertView.findViewById(R.id.Time)).setText("All day");
		}
		else{
			((TextView) convertView.findViewById(R.id.Time)).setText(startLabel + " - " + endLabel);	//states start and end time.
		}
		
		try{
			colors = new int[11];
			colors[1] = R.color.gCal16;
			colors[2] = R.color.gCal15;
			colors[3] = R.color.gCal7;
			colors[4] = R.color.gCal13;
			colors[5] = R.color.gCal9;
			colors[6] = R.color.gCal11;
			colors[7] = R.color.gCal12;
			colors[8] = R.color.gCal22;
			colors[9] = R.color.gCal3;
			colors[10] = R.color.gCal21;
			colors[11] = R.color.gCal19;
			((LinearLayout) convertView.findViewById(R.id.background)).setBackgroundResource(colors[colorKey]);
		}
		catch (Exception e)
		{
			//White card
			//((LinearLayout) convertView.findViewById(R.id.background)).setBackgroundResource(R.color.gCal15);
		}		
		try{
			//Chooses color based on google defaults.
			colors2 = new int[25];
			colors2[1] = R.color.gCal1;
			colors2[2] = R.color.gCal2;
			colors2[3] = R.color.gCal3;
			colors2[4] = R.color.gCal4;
			colors2[5] = R.color.gCal5;
			colors2[6] = R.color.gCal6;
			colors2[7] = R.color.gCal7;
			colors2[8] = R.color.gCal8;
			colors2[9] = R.color.gCal9;
			colors2[10] = R.color.gCal10;
			colors2[11] = R.color.gCal11;
			colors2[12] = R.color.gCal12;
			colors2[13] = R.color.gCal13;
			colors2[14] = R.color.gCal14;
			colors2[15] = R.color.gCal15;
			colors2[16] = R.color.gCal16;
			colors2[17] = R.color.gCal17;
			colors2[18] = R.color.gCal18;
			colors2[19] = R.color.gCal19;
			colors2[20] = R.color.gCal20;
			colors2[21] = R.color.gCal21;
			colors2[22] = R.color.gCal22;
			colors2[23] = R.color.gCal23;
			colors2[24] = R.color.gCal24;
			((LinearLayout) convertView.findViewById(R.id.background)).setBackgroundResource(colors2[colorKey]);
		}
		catch (Exception e)
		{

		}
	}

	@SuppressLint("SimpleDateFormat")
	public EventCard(String titlePlay, String description, long start, long end, String color, String color2, Boolean hasOverflow, 
			Boolean isClickable, final String eventId, boolean last, boolean fullday) //Full constructor
	{
		this.titlePlay = titlePlay;
		this.description = description;
		SimpleDateFormat _12HourSDFwDM = new SimpleDateFormat("M/d h:mm a");
		SimpleDateFormat _12HourSDF = new SimpleDateFormat("h:mm a");
		SimpleDateFormat _DaySDF = new SimpleDateFormat("M/d");
		this.startTime = start;
		this.endTime = end;
		this.startLabel = _12HourSDFwDM.format(new Time(start));
		if (_DaySDF.format(new Time(start))==_DaySDF.format(new Time(end)))	//Check if start and end time are on same day
		{
			this.endLabel = _12HourSDFwDM.format(new Time(end));	//Formats end time with day
		}
		else
		{
			this.endLabel = _12HourSDF.format(new Time(end));	//Formats end time
		}
		this.AllDay = fullday;
		try{
			this.colorKey = Integer.parseInt(color);
			this.colorKey2 = Integer.parseInt(color2);
		}
		catch (Exception e)
		{
			Log.d("EventCard Color", "I tried so hard");
		}
		this.hasOverflow = hasOverflow;
		this.eventId = eventId;
		this.last = last;

		if (isClickable)
		{
			this.setOnClickListener( new OnClickListener() 
			{
				public void onClick(View v) 
				{
					//Opens intent to user's default calendar app that supports Google Calendar events.
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("content://com.android.calendar/events/" + String.valueOf(eventId))); 
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NO_HISTORY
							| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
					v.getContext().startActivity(intent);

				}
			});
		};

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
	public int compareTo(Object another) { //for sorting
		if (this.getStart() < ((EventCard) another).getStart())
		{
			return 1;
		}
		else if (this.getStart() > ((EventCard) another).getStart())
		{
			return -1;
		}
		else if (this.getStart() == ((EventCard) another).getStart())
		{
			if (this.getEnd() < ((EventCard) another).getEnd())
			{
				return 1;
			}
			else if (this.getEnd() < ((EventCard) another).getEnd())
			{
				return -1;
			}
		}
		return 0;
	}

	public EventCard()
	{
		super();
	}
}
