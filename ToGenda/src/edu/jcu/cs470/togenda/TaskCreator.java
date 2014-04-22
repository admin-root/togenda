package edu.jcu.cs470.togenda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

public class TaskCreator extends FragmentActivity implements OnDateSetListener{

	private AlertDialog alertDialog;
	private Drawable color;
	
	private DatePickerDialog datePickerDialog;
	private Calendar calendar;
	
	long milliseconds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_creator);
		getActionBar().setIcon(R.drawable.ic_new_event);
		
		calendar = Calendar.getInstance();
		
		datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
	}

	public void colorPick(View v){
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Task Color");
		LayoutInflater inflater = this.getLayoutInflater();
		alertDialog.setView(inflater.inflate(R.layout.colorpick, null));
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
				//do nothing
			}
		});
		alertDialog.setIcon(R.drawable.ic_tint_dark);
		alertDialog.show();
	}

	public void colorButton(View v)
	{
		color = v.getBackground();
		findViewById(R.id.colorBack).setBackgroundDrawable(color);
		//Can't use newer method without increasing our min version
		//this method will still be good for a very long time.
		alertDialog.dismiss();
	}

	public void create(View v)
	{
		//implement creation actions
		finish();
	}

	public void cancel(View v)
	{
		finish();
	}

	public void dateClick(View v){

		datePickerDialog.setVibrate(false);
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(getSupportFragmentManager(), "Due Date");
		
	}
	
	public void dateCheck(View v){
		if (((CheckBox) v).isChecked()) {
			findViewById(R.id.dateButton).setEnabled(true);
		}
		else{
			findViewById(R.id.dateButton).setEnabled(false);
		}
	}

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
			//Toast.makeText(this, "long:" + String.valueOf(milliseconds), Toast.LENGTH_SHORT).show();
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
}
