package SQLPrototype;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TasksDataSource {
	//This the class to handle DAO.It maintains the database connection and supports
	//adding new comments and fetching all comments.
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {MySQLiteHelper.COLUMN_KEY, MySQLiteHelper.COLUMN_NAME};
	
	//constructor
	public TasksDataSource(Context context)
	{
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public Tasks createTask(String name, String content, long timeStart, long timeEnd, long dueDate, int colorId, int priority)
	{
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, content);
		values.put(MySQLiteHelper.COLUMN_NAME, timeStart);
		values.put(MySQLiteHelper.COLUMN_NAME, timeEnd);
		values.put(MySQLiteHelper.COLUMN_NAME, dueDate);
		values.put(MySQLiteHelper.COLUMN_NAME, colorId);
		values.put(MySQLiteHelper.COLUMN_NAME, priority);
		//get the id (which is auto-generated)
		long insertID = database.insert(MySQLiteHelper.TABLE_NAME, null, values);
		//getting the comment to return
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns,
			MySQLiteHelper.COLUMN_KEY+"="+insertID/*where key = this number*/, null, null, null, null);
		cursor.moveToFirst();
		Tasks newTask = cursorToTask(cursor); //place holder
		cursor.close();
		return newTask;
	}
	
	private Tasks cursorToTask(Cursor cursor)
	{
		Tasks task = new Tasks();
		task.setId(cursor.getLong(0)); //go to first column and get long value
		task.setName(cursor.getString(1));
		task.setComment(cursor.getString(2));
		task.setTimeStart(cursor.getLong(3));
		task.setTimeEnd(cursor.getLong(4));
		task.setDueDate(cursor.getLong(5));
		task.setColorID(cursor.getInt(6));
		task.setPriority(cursor.getInt(7));
		return task;
	}
	
	public void deleteTask (Tasks task)
	{
		long id = task.getId();
		database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_KEY+ "="+id, null);
	}
	
	//get all of the Comments from the database
	public List<Tasks> getAllComments()
	{
		List<Tasks> myTasks = new ArrayList<Tasks>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		//while there are more records, convert them from a cursor to a comment
		while(!cursor.isAfterLast())
		{
			Tasks task = cursorToTask(cursor);
			myTasks.add(task);
			cursor.moveToNext();
		}
		cursor.close();
		return myTasks;
	}
	
	//update a comment
	public void updateTask(Tasks task)
	{
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, task.getComment());
		values.put(MySQLiteHelper.COLUMN_NAME, task.getTimeStart());
		values.put(MySQLiteHelper.COLUMN_NAME, task.getTimeEnd());
		values.put(MySQLiteHelper.COLUMN_NAME, task.getDueDate());
		values.put(MySQLiteHelper.COLUMN_NAME, task.getColorID());
		values.put(MySQLiteHelper.COLUMN_NAME, task.getPriority());
		database.update(MySQLiteHelper.TABLE_NAME, values, MySQLiteHelper.COLUMN_KEY+"=?", 
				new String[] {String.valueOf(task.getId())});
	}
}
