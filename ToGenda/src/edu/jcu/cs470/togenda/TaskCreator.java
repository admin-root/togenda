/* Saeed Tawil, Danny Gonzalez
 * Spring 2014
 * Description: This class is used to create a new or edit and existing task. This class will
 * 				also add the task's title, contents, due date, selected color ID, and priority
 * 				level to the tasks database.
 */
package edu.jcu.cs470.togenda;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

public class TaskCreator extends FragmentActivity implements OnDateSetListener{
	private AlertDialog alertDialog;
	private ColorDrawable color;
	int[] colors;
	private DatePickerDialog datePickerDialog;
	private Calendar calendar;
	long milliseconds;
	DBAdapter db;
	String title, content;
	int colorNumber;
	boolean editing = false;
	private int taskID;
	int priority;
	Long newDate;
	Cursor TaskCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_creator);
		getActionBar().setIcon(R.drawable.ic_new_event);

		//gets the calendar for date selection
		calendar = Calendar.getInstance();
		datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
		//copy the database from the assets folder to where the app can find it
		String dir = "/data/data/"+getPackageName()+"/databases/";
		String path = dir+"tasks.db";
		//THERE'S A BETTER WAY TO DO THIS
		//We shoudln't be storing the database on the SDcard.
		//Too many incompatibility issues with different devices. Especially with Android 4.4
		//should not need to create the databases directory, will for testing purposes
		File f = new File(dir);
		if(!f.exists())
		{
			File directory = new File(dir);
			directory.mkdirs();
			try
			{
				//copy from input-stream to output-stream
				copyDataBase(getBaseContext().getAssets().open("tasks.db"), new FileOutputStream(path));
			}
			catch(FileNotFoundException ex)
			{
				ex.printStackTrace();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		//exercise the database
		db = new DBAdapter(this);
		
		//editing an already existing task
		Intent i= getIntent();
		if(i.hasExtra("TaskID"))
		{
			taskID = i.getIntExtra("TaskID", 0);
			//String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(ldate));
			calendar = Calendar.getInstance();
			datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
			db = new DBAdapter(this);
			db.open();
			TaskCursor = db.getTask(taskID);
			//get task name from database
			TextView TaskName = (TextView) findViewById(R.id.taskTitle);
			TaskName.setText(TaskCursor.getString(1));		
			//get tasks description from database
			TextView TaskDesc = (TextView) findViewById(R.id.taskInfo);
			TaskDesc.setText(TaskCursor.getString(2));
			//get task due date from database
			newDate = TaskCursor.getLong(3);
			CheckBox dateCheck = (CheckBox) findViewById(R.id.datebox);
			Button thisButton = (Button) findViewById(R.id.dateButton);
			if (newDate != 0)
			{
				dateCheck.setChecked(true);
				thisButton.setEnabled(true);
				Date due = new Date(newDate);
				SimpleDateFormat editDate = new SimpleDateFormat("M/d/yyyy");
				String date = editDate.format(due);
				thisButton.setText(date);
				milliseconds = newDate;
			}
			if (newDate == 0)
			{
				thisButton.setText("Choose date");
			}
			//get the color ID from the database
			getColor(TaskCursor.getInt(4));
			
			editing = true;
			db.close();
		}
	}

	//creates/edit task
	public void create(View v)
	{
		EditText taskName = (EditText)findViewById(R.id.taskTitle);
		EditText taskContent = (EditText)findViewById(R.id.taskInfo);
		
		//create default task
		if(editing == false)
		{
			title = taskName.getText().toString();
			content = taskContent.getText().toString();
			Long date = getDate();
			int size = 1;

			if(!title.equals(""))
			{
				db.open();
				db.insertTask(title, content, date, colorNumber, size);
				db.close();
				finish();
			}
			else
			{
				if(title.equals(""))
				{
					Toast.makeText(this, "Insert a Title", Toast.LENGTH_LONG).show();
				}
			}
		}
		//editing task
		else //editing == true
		{
			title = taskName.getText().toString();
			content = taskContent.getText().toString();
			Long date = getDate();
			int size = 1;

			if(!title.equals(""))
			{
				db.open();
				db.updateTask(taskID, title, content, date, colorNumber, priority, size);
				db.close();
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
			else
			{
				if(title.equals(""))
				{
					Toast.makeText(this, "Insert a Title", Toast.LENGTH_LONG).show();
				}
			}
			editing = false;
		}
	}

	//implements cancel button
	public void cancel(View v)
	{
		finish();
	}
	
	//copies tasks database
	private void copyDataBase(InputStream in, FileOutputStream out) throws IOException
	{
		//copy 1024 bytes at a time
		byte[] buffer = new byte [1024];
		int length;
		while((length = in.read(buffer)) > 0)
		{
			out.write(buffer, 0, length);
		}
		in.close();
		out.close();
	}

	//gets the task content as a string value
	public String getContent()
	{
		return content;
	}
	
	//creates the claendar dialogue to select due date
	public void dateClick(View v)
	{
		datePickerDialog.setVibrate(false);
		datePickerDialog.setYearRange(1985, 2028);
		datePickerDialog.setCloseOnSingleTapDay(false);
		datePickerDialog.show(getSupportFragmentManager(), "Due Date");
	}

	//allows addition of due date if dateBox CheckBox is selected
	public void dateCheck(View v)
	{
		if (((CheckBox) v).isChecked()) 
		{
			findViewById(R.id.dateButton).setEnabled(true);
		}
		else
		{
			findViewById(R.id.dateButton).setEnabled(false);
		}
	}

	//setting the due date
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) 
	{
		Button thisButton = (Button)findViewById(R.id.dateButton);
		thisButton.setText(String.valueOf(month+1) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
		String string_date = String.valueOf(month+1) + "-" + String.valueOf(day) + "-" + String.valueOf(year);
		SimpleDateFormat f = new SimpleDateFormat("M-d-yyyy");
		Date d;
		try 
		{
			d = f.parse(string_date);
			milliseconds = d.getTime(); //THIS IS OUR TIME IN LONG FORMAT
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
	}

	//gets the due date in milliseconds
	public long getDate()
	{
		return milliseconds;
	}

	//creates the color select dialogue
	public void colorPick(View v)
	{
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Task Color");
		LayoutInflater inflater = this.getLayoutInflater();
		alertDialog.setView(inflater.inflate(R.layout.colorpick, null));
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				// here you can add functions
				//do nothing
			}
		});
		alertDialog.setIcon(R.drawable.ic_tint_dark);
		alertDialog.show();
	}

	//setting colorBack and color ID depending on which color is selecte
	@SuppressWarnings("deprecation")
	public void color1(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 1;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color2(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 2;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color3(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 3;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color4(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 4;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color5(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 5;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color6(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 6;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color7(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 7;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color8(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 8;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color9(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 9;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color10(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 10;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color11(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 11;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color12(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 12;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color13(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 13;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color14(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 14;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color15(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 15;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color16(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 16;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color17(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 17;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color18(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 18;
		alertDialog.dismiss();
	}	
	@SuppressWarnings("deprecation")
	public void color19(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 19;
		alertDialog.dismiss();
	}	
	@SuppressWarnings("deprecation")
	public void color20(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 20;
		alertDialog.dismiss();
	}	
	@SuppressWarnings("deprecation")
	public void color21(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 21;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color22(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 22;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color23(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 23;
		alertDialog.dismiss();
	}
	@SuppressWarnings("deprecation")
	public void color24(View v)
	{
		color = (ColorDrawable) v.getBackground();	
		findViewById(R.id.colorBack).setBackgroundDrawable(color);		
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		colorNumber = 24;
		alertDialog.dismiss();
	}
	
	//gets the color of the colorBack segment from the assigned color ID from the database
	public void getColor(int colorId)
	{
		if(colorId == 1)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal1);
		}
		else if(colorId == 2)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal2);
		}
		else if(colorId == 3)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal3);
		}
		else if(colorId == 4)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal4);
		}
		else if(colorId == 5)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal5);
		}
		else if(colorId == 6)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal6);
		}
		else if(colorId == 7)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal7);
		}
		else if(colorId == 8)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal8);
		}
		else if(colorId == 9)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal9);
		}
		else if(colorId == 10)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal10);
		}
		else if(colorId == 11)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal11);
		}
		else if(colorId == 12)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal12);
		}
		else if(colorId == 13)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal13);
		}
		else if(colorId == 14)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal14);
		}
		else if(colorId == 15)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal15);
		}
		else if(colorId == 16)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal16);
		}
		else if(colorId == 17)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal17);
		}
		else if(colorId == 18)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal18);
		}
		else if(colorId == 19)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal19);
		}
		else if(colorId == 20)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal20);
		}
		else if(colorId == 21)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal21);
		}
		else if(colorId == 22)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal22);
		}
		else if(colorId == 23)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal23);
		}
		else if(colorId == 24)
		{
			findViewById(R.id.colorBack).setBackgroundResource(R.color.gCal24);
		}
	}
}