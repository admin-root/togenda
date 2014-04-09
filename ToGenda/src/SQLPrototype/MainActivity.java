package SQLPrototype;
import java.util.List;
import java.util.Random;

import edu.jcu.cs470.togenda.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	private TasksDataSource datasource;
	private EditText indexSelector, modifiedComment;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		datasource = new TasksDataSource(this);
		datasource.open();
		//use adapter to display the contents on the screen
		List<Tasks> values = datasource.getAllComments();
		ArrayAdapter<Tasks> adapter = new ArrayAdapter<Tasks>(this, android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
//		indexSelector = (EditText)findViewById(R.id.indexEditText);
//		modifiedComment = (EditText)findViewById(R.id.modifyEditText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v)
	{
		ArrayAdapter<Tasks> adapter = (ArrayAdapter<Tasks>)getListAdapter();
		Tasks task = null;
//		switch(v.getId())
//		{
		//adding
//		case R.id.addButton:
//			String[] possibleComments = new String[] {"one","two","three","four"};
//			int nextInt = new Random().nextInt(4);
//			//save comment to the database
//			task = datasource.createTask("name", "content", 150, 250, 250, 24, 1);
//			adapter.add(task);
//			break;
			
		//deleting
//		case R.id.delete:
//			//delete where the user indicates 0 based index
//			if(getListAdapter().getCount()>0)
//			{
//				if(!indexSelector.getText().toString().equals(""))
//				{
//					try
//					{
//						//make sure it is a valid index
//						int position = Integer.parseInt(indexSelector.getText().toString());
//						if(position >= 0 && position < getListAdapter().getCount())
//						{
//							//delete from database and screen
//							task = (Tasks)getListAdapter().getItem(position);
//							//delete from database
//							datasource.deleteTask(task);
//							//delete from ArrayList from the screen 
//							adapter.remove(task);
//						}
//						else
//						{
//							Toast.makeText(getApplicationContext(), "No such position", Toast.LENGTH_LONG).show();
//						}
//
//					}
//					catch(Exception ex)
//					{
//						//should never get here
//						Toast.makeText(getApplicationContext(), "Should not be here", Toast.LENGTH_LONG).show();
//					}
//				}
//				else
//				{
//					Toast.makeText(getApplicationContext(), "Must enter a position", Toast.LENGTH_LONG).show();
//				}
//			}
//			break;
			
		//modify
		adapter.notifyDataSetChanged(); //automatic update
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
	}

}
