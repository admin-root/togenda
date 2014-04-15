package SQLPrototype2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
	//variables
	public DBAdapter db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_item);

		//copy the database from the assets folder to where the app can find it
		String dir = "/data/data/"+getPackageName()+"/databases/";
		String path = dir+"InclassBlogger.db";
		//should not need to create the databases directory, will for testing purposes
		File f = new File(dir);
		if(!f.exists())
		{
			File directory = new File(dir);
			directory.mkdirs();
			try
			{
				//copy from input-stream to output-stream
				copyDataBase(getBaseContext().getAssets().open("InclassBlogger.db"), new FileOutputStream(path));
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
		getBloggers();
		addBlogger();
		getBlogger();
		updateBlogger();
		deleteBlogger();
		getBloggers();
	}

	public void displayBlogger(Cursor cursor)
	{
		Toast.makeText(this, "id: "+cursor.getLong(0)+" Email: "+cursor.getString(1)+
			" Name: "+cursor.getString(2)+"\n", Toast.LENGTH_LONG).show();
	}
	
	//get all the bloggers
	public void getBloggers()
	{
		db.open();
		Cursor cursor = db.getAllTasks();
		if(cursor.moveToFirst())
		{
			do //works once then check
			{
				displayBlogger(cursor);
			}
			while(cursor.moveToNext());
		}
		db.close();
	}
	
	//get a specific blogger (with id = 2)
	public void getBlogger()
	{
		db.open();
		Cursor cursor = db.getTask(2);
		if(cursor.moveToFirst())
		{
			displayBlogger(cursor);
		}
		else
		{
			Toast.makeText(this, "No such bloggerr found", Toast.LENGTH_LONG).show();
		}
		db.close();
	}
	
	//add a single blogger
	public void addBlogger()
	{
		db.open();
//		if(db.insertBlogger("tony tiger", "tony@jcu.edu") >= 0)
//		{
//			Toast.makeText(this, "Add successful", Toast.LENGTH_LONG).show();
//		}
//		if(db.insertBlogger("mary jackson", "mary@jcu.edu") >= 0)
//		{
//			Toast.makeText(this, "Add successful", Toast.LENGTH_LONG).show();
//		}
		db.close();
	}	
	
	//delete blogger (with id = 1)
	public void deleteBlogger()
	{
		db.open();
		if(db.deleteTask(1))
		{
			Toast.makeText(this, "Delete successful", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this, "Delete failed", Toast.LENGTH_LONG).show();
		}
		db.close();
	}	
	
	//update  blooger (with id = 4)
	public void updateBlogger()
	{
		db.open();
//		if(db.updateTask(4, "marc@jcu.edu", "marc kirschenbaum"))
//		{
//			Toast.makeText(this, "Update successful", Toast.LENGTH_LONG).show();
//		}
//		else
//		{
//			Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show();
//		}
		db.close();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
