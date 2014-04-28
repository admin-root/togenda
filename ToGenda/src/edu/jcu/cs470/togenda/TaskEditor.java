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
import SQLPrototype2.DBAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

public class TaskEditor extends FragmentActivity implements OnDateSetListener{
	private AlertDialog alertDialog;
	private ColorDrawable color;
	int[] colors;
	private DatePickerDialog datePickerDialog;
	private Calendar calendar;
	long milliseconds;
	DBAdapter db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_creator);
		getActionBar().setIcon(R.drawable.ic_save);
		getActionBar().setTitle(R.string.modify);
		
		calendar = Calendar.getInstance();
		datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
		
		//copy the database from the assets folder to where the app can find it
				String dir = "/data/data/"+getPackageName()+"/databases/";
				String path = dir+"tasks.db";
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
	}
	
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

	@SuppressWarnings("deprecation")
	public void colorButton(View v)
	{
		color = (ColorDrawable) v.getBackground();
		findViewById(R.id.colorBack).setBackgroundDrawable(color);
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		alertDialog.dismiss();
//		Toast.makeText(this, String.valueOf(getColorId()), Toast.LENGTH_LONG).show();
	}

	public void create(View v)
	{
		//danny workspace
		//get title
		EditText taskName = (EditText)findViewById(R.id.taskTitle);
		String title = taskName.getText().toString();
		//get content
		EditText taskContent = (EditText)findViewById(R.id.taskInfo);
		String content = taskContent.getText().toString();
		//get date
		Long date = getDate();
		//get color ID
		int colorId = getColorId(); //currently returning 0
		//get priority
		int priority = 1; //test values
		db.open();
		db.insertBlogger(title, content, date, colorId, priority);
		db.close();
		
		Toast.makeText(this, title, Toast.LENGTH_LONG).show();
		Toast.makeText(this, content, Toast.LENGTH_LONG).show();
		Toast.makeText(this, String.valueOf(date), Toast.LENGTH_LONG).show();
		Toast.makeText(this, String.valueOf(colorId), Toast.LENGTH_LONG).show();
		Toast.makeText(this, String.valueOf(priority), Toast.LENGTH_LONG).show();
		
		
		
		finish();
	}

	public void cancel(View v)
	{
		finish();
	}

	public void dateClick(View v)
	{
		datePickerDialog.setVibrate(false);
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(getSupportFragmentManager(), "Due Date");
	}
	
	public void dateCheck(View v)
	{
		if (((CheckBox) v).isChecked()) {
			findViewById(R.id.dateButton).setEnabled(true);
		}
		else{
			findViewById(R.id.dateButton).setEnabled(false);
		}
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) 
	{
		Button thisButton = (Button)findViewById(R.id.dateButton);
		thisButton.setText(String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year));
		String string_date = String.valueOf(month) + "-" + String.valueOf(day) + "-" + String.valueOf(year);
		SimpleDateFormat f = new SimpleDateFormat("d-M-yyyy");
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
	
	public long getDate()
	{
		return milliseconds;
	}
	
	public int getColorId()
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
		for(int i = 1; i < 25; i ++)
		{
			//if(color.getColor() == findViewById(R.id.colorBack).getBackground(color[i]))
			if(color == getResources().getDrawable(colors[i]))
			{
				return i;
			}	
		}
		return 15;
	}
}
