package edu.jcu.cs470.togenda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.support.v4.app.FragmentActivity;

public class TaskCreator extends FragmentActivity {//implements OnDateSetListener, TimePickerDialog.OnTimeSetListener{

	AlertDialog alertDialog;
	Drawable color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_creator);
		getActionBar().setIcon(R.drawable.ic_new_event);
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

	}
	public void dateCheck(View v){
		if (((CheckBox) v).isChecked()) {
			findViewById(R.id.dateButton).setEnabled(true);
		}
		else{
			findViewById(R.id.dateButton).setEnabled(false);
		}
	}
}
