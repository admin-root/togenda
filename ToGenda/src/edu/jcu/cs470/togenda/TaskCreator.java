package edu.jcu.cs470.togenda;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

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

public class TaskCreator extends FragmentActivity implements OnDateSetListener{
	private AlertDialog alertDialog;
	private ColorDrawable color;
	int[] colors;
	private DatePickerDialog datePickerDialog;
	private Calendar calendar;
	long milliseconds;
	DBAdapter db;
	String title, content;
	int colorId;
	int colorNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_creator);
		getActionBar().setIcon(R.drawable.ic_new_event);
		
		calendar = Calendar.getInstance();
		datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
		
		//copy the database from the assets folder to where the app can find it
		String dir = "/data/data/"+getPackageName()+"/databases/";
		String path = dir+"tasks.db";
		//THERE'S A BETTER WAY TO DO THIS
		//We shoudln't be storing the database on the SDcard.
		//Too many incompatibility issues with different devices. Especially with Android 4.4
		// -Saeed
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
	
	public void create(View v)
	{
		
		EditText taskName = (EditText)findViewById(R.id.taskTitle);
		title = taskName.getText().toString();
		EditText taskContent = (EditText)findViewById(R.id.taskInfo);
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
		if (((CheckBox) v).isChecked()) 
		{
			findViewById(R.id.dateButton).setEnabled(true);
		}
		else
		{
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
	
	public String getContent()
	{
		return content;
	}
	
	public long getDate()
	{
		return milliseconds;
	}
}
