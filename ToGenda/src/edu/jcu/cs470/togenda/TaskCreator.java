package edu.jcu.cs470.togenda;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.os.Build;

public class TaskCreator extends Activity implements OnClickListener{
	ColorDrawable newColor;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button = (Button)findViewById(R.id.button1);
		button.setVisibility(View.INVISIBLE);
	}
	
	public void getColor(View v)
	{
		Button colorSelect = (Button)(v);
		newColor = (ColorDrawable)colorSelect.getBackground();
//		Button testType = (Button)findViewById(R.id.colorTest);
//		testType.setBackground(newColor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) 
	{
		CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox1);
		if(checkBox.isChecked())
		{
			button.setVisibility(View.VISIBLE);
		}
		else
		{
			button.setVisibility(View.INVISIBLE);
		}
	}

}
