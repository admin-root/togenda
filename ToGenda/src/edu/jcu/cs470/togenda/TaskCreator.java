package edu.jcu.cs470.togenda;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class TaskCreator extends Activity{
	
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
//		//EditText taskname = new EditText(getApplicationContext());
//		//taskname.setTextColor(000000);
//		LayoutInflater inflater = this.getLayoutInflater();
//		alertDialog.setView(inflater.inflate(R.layout.task_creator, null));
//		
//		//danny workspace
//		EditText taskName = (EditText)findViewById(R.id.taskTitle);
//		String title = taskName.getText().toString();
//		EditText taskContent = (EditText)findViewById(R.id.taskInfo);
//		String content = taskContent.getText().toString();
//		//import code from SQLPrototype-Main activity to here (add,delete,update,get)
//		//import DBAdapter and Tasks to this package
//		
//		
//		//interface
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
				//do nothing
			}
		});
//		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Create", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				// here you can add functions
//				//add to database
//				//refresh cardview
//			}
//		});
		alertDialog.setIcon(R.drawable.ic_tint_dark);
		alertDialog.show();
	}
	
	public void colorButton(View v){
		color = v.getBackground();
		findViewById(R.id.colorBack).setBackgroundDrawable(color);
		alertDialog.dismiss();
		
	}
}
