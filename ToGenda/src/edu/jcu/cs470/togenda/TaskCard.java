/* Saeed Tawil, Danny Gonzalez
 * Spring 2014
 * Description: This class is used to edit it elements in the card_play.xml file with the 
 * 				specific elements taken from the task database to create a unique task card
 * 				to be seen.
 */
package edu.jcu.cs470.togenda;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

//Displays a task card object

@SuppressWarnings("rawtypes")
public class TaskCard extends CardTemplate implements Comparable{
	String title;
	String description;
	Long dueDate;
	Long startTime;
	Long endTime;
	String startLabel;
	String endLabel;
	int colorId;
	boolean last = false;
	DBAdapter db;
	int Priority;
	int color;
	int[] colors;

	public TaskCard(String thisTitle)
	{
		title = thisTitle;
	}

	@Override
	protected int getCardLayoutId() 
	{
		return R.layout.card_play;
	}

	//
	@SuppressLint("SimpleDateFormat")
	public TaskCard(final int taskID, String titlePlay, String descText, final long due, int color, int priority,  final Context c, final FragmentManager fm, int size) {
		this.title = titlePlay;
		this.description = descText;
		this.dueDate = due;
		@SuppressWarnings("unused")
		SimpleDateFormat _12HourSDFwDM = new SimpleDateFormat("M/d h:mm a");
		SimpleDateFormat _12HourSDF = new SimpleDateFormat("h:mm a");
		this.endTime = due;
		this.endLabel = _12HourSDF.format(new Time(due));
		this.colorId = color;
		this.Priority = priority;
		this.hasOverflow = false;
		this.isClickable = true;
		db = new DBAdapter(c);
		this.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//creates dialogue to edit or remove task
				AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
				//displays task title in view
				alertDialog.setTitle(title);
				LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View dialogContent = inflater.inflate(R.layout.task_dialog, null);
				//displays task description in view
				TextView tdDesc = (TextView) dialogContent.findViewById(R.id.td_desc);
				if (description.equals(""))
				{
					tdDesc.setText("no description");
				}
				else
				{
					tdDesc.setText(description);
				}
				//displays due date/"No Due Date" message in view
				if (due != 0)
				{
					TextView tdDue = (TextView) dialogContent.findViewById(R.id.td_due);
					Date dueDate = new Date(due);
					SimpleDateFormat f = new SimpleDateFormat("M-d-yyyy");
					String date = f.format(dueDate);
					tdDue.setText("Due: "+date);
				}
				else
				{
					TextView tdDue = (TextView) dialogContent.findViewById(R.id.td_due);
					tdDue.setText("No Due Date");
				}
				alertDialog.setView(dialogContent);
				//removes task from view and database
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Complete", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						db.open();
						db.deleteTask(taskID);
						db.close();

						FragmentTransaction tr = fm.beginTransaction();
						tr.replace(R.id.content_frame, new AgendaFragment());
						tr.commit();
					}
				});
				//creates task as an intent to edit in TaskCreator class
				alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Edit", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent intent = new Intent(c, TaskCreator.class);
						intent.putExtra("TaskID",taskID);
						c.startActivity(intent);
					}
				});
				//leave dialogue without removing or editing task
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						//Do nothing.
					}
				});
				alertDialog.setIcon(R.drawable.ic_time_dark);
				alertDialog.show();
			}
		});
	}

	//creates the values in the task card that is displayed in the card_play.xml
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void applyTo(View convertView) 
	{
		//displays task title
		((TextView) convertView.findViewById(R.id.EventLabel)).setText(title);
		//displays task description
		((TextView) convertView.findViewById(R.id.description)).setText(description);
		//Chooses color based on google defaults.
		if (colorId > 0 && colorId < 25)
		{
			colors = new int[25];
			colors[1] = R.color.gCal1;
			colors[2] = R.color.gCal2;
			colors[3] = R.color.gCal3;
			colors[4] = R.color.gCal4;
			colors[5] = R.color.gCal5;
			colors[6] = R.color.gCal6;
			colors[7] = R.color.gCal7;
			colors[8] = R.color.gCal8;
			colors[9] = R.color.gCal9;
			colors[10] = R.color.gCal10;
			colors[11] = R.color.gCal11;
			colors[12] = R.color.gCal12;
			colors[13] = R.color.gCal13;
			colors[14] = R.color.gCal14;
			colors[15] = R.color.gCal15;
			colors[16] = R.color.gCal16;
			colors[17] = R.color.gCal17;
			colors[18] = R.color.gCal18;
			colors[19] = R.color.gCal19;
			colors[20] = R.color.gCal20;
			colors[21] = R.color.gCal21;
			colors[22] = R.color.gCal22;
			colors[23] = R.color.gCal23;
			colors[24] = R.color.gCal24;
			((ImageView) convertView.findViewById(R.id.stripe)).setBackgroundResource(colors[colorId]);
		}
		else
		{
			((ImageView) convertView.findViewById(R.id.stripe)).setBackgroundResource(R.color.gCal15);
		}
		//Displays due date
		Date due = new Date(dueDate);
		SimpleDateFormat f = new SimpleDateFormat("M-d-yyyy");
		String date = f.format(due);
		if (dueDate != 0)
		{
			((TextView) convertView.findViewById(R.id.DueDate)).setText("Due: "+date);
		}
		else
		{
			((TextView) convertView.findViewById(R.id.DueDate)).setVisibility(View.INVISIBLE);
		}
	}

	//
	@SuppressLint("SimpleDateFormat")
	public TaskCard(final int taskID, String titlePlay, String descText, final long due, int color, int priority) 
	{
		this.title = titlePlay;
		this.description = descText;
		@SuppressWarnings("unused")
		SimpleDateFormat _12HourSDFwDM = new SimpleDateFormat("M/d h:mm a");
		SimpleDateFormat _12HourSDF = new SimpleDateFormat("h:mm a");
		this.endTime = due;
		this.endLabel = _12HourSDF.format(new Time(due));
		this.color = color;
		this.Priority = priority;
		this.hasOverflow = false;
		this.isClickable = true;
	}

	//set the description of the task
	public void setDescription(String desc)
	{
		this.description = desc;
	}

	//checks if a task is the last task in the list
	public boolean isLast()
	{
		return last;
	}

	//gets the task priority
	public long getPriority()
	{
		return this.Priority;
	}

	//for sorting tasks for placement
	@Override
	public int compareTo(Object another)  
	{	
		if (this.getPriority() < ((TaskCard) another).getPriority())
		{
			return 1;
		}
		else if (this.getPriority() > ((TaskCard) another).getPriority())
		{
			return -1;
		}
		return 0;
	}

	public TaskCard()
	{
		super();
	}
	
	//get the title of the task
	public String getTitle()
	{
		return this.title;
	}

	//gets the description of the task
	public String getDesc()
	{
		return this.description;
	}
	
	//gets the color ID
	public int getColorID()
	{
		return this.colorId;
	}
}
