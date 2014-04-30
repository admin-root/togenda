package edu.jcu.cs470.togenda;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	//defining the table and column names
	public static final String KEY_ID = "_id";
	public static final String COLUMN_NAME = "title";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_DUE = "due_date";
	public static final String COLUMN_COLOR = "color_id";
	public static final String COLUMN_PRIORITY = "priority";
	public static final String COLUMN_SIZE = "size";
	//construct the structure of the database
	public static final String DATABASE_NAME = "tasks.db";
	public static final String DATABASE_TABLE = "tasks";
	private static final int DATABASE_VERSION = 5;
	private static final String DATABASE_CREATE = "create table "+DATABASE_TABLE+" ("+KEY_ID+
		" integer primary key autoincrement, "+COLUMN_NAME+" text not null, "+COLUMN_CONTENT+" text not null, "
		+COLUMN_DUE+" text not null, "+COLUMN_COLOR+" INTEGER not null, "+COLUMN_PRIORITY+" INTEGER not null, "+COLUMN_SIZE+" INTEGER not null)";
	//variables
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	Context context;

	public DBAdapter(Context context)
	{
		this.context = context;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			try
			{
				db.execSQL(DATABASE_CREATE);
			}
			catch(SQLException sqlex)
			{
				sqlex.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			Log.w("DBAdapter", "Upgrading database from version "+oldVersion+" to "
					+newVersion+", which will destroy all old data");
			db.execSQL("drop table if exists "+DATABASE_TABLE);
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		DBHelper.close();
	}

	public long insertTask(String title, String content, long due, int colorId, int size)
	{
		int priority = db.rawQuery("select * from "+DATABASE_TABLE,null).getCount();
		
		ContentValues row = new ContentValues();
		row.put(COLUMN_NAME, title);
		row.put(COLUMN_CONTENT, content);
		row.put(COLUMN_DUE, due);
		row.put(COLUMN_COLOR, colorId);
		row.put(COLUMN_PRIORITY, priority+1);
		row.put(COLUMN_SIZE, size);
		return db.insert(DATABASE_TABLE, null, row);
	}

	public boolean deleteTask(long rowId)
	{
		return db.delete(DATABASE_TABLE, KEY_ID+"="+rowId, null) > 0;
	}

	public Cursor getAllTasks()
	{
		String[] COLS = new String[] {KEY_ID, COLUMN_NAME, COLUMN_CONTENT, COLUMN_DUE, 
				COLUMN_COLOR, COLUMN_PRIORITY, COLUMN_SIZE};
		
		Cursor cursor = db.query(DATABASE_TABLE, COLS, null, null, null, null, null);
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		else
		{
		}
		return cursor;
	}

	public Cursor getTask(long rowId) throws SQLException
	{
		Cursor cursor = db.query(DATABASE_TABLE, new String[] {KEY_ID, COLUMN_NAME, COLUMN_CONTENT, COLUMN_DUE, 
			COLUMN_COLOR, COLUMN_PRIORITY, COLUMN_SIZE}, KEY_ID+"="+rowId, null, null, null, null);
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public boolean updateTask(long rowID, String title, String content, long due, String color, int priority, int size)
	{
		ContentValues args = new ContentValues();
		args.put(COLUMN_NAME, title);
		args.put(COLUMN_CONTENT, content);
		args.put(COLUMN_DUE, due);
		args.put(COLUMN_COLOR, color);
		args.put(COLUMN_PRIORITY, priority);
		args.put(COLUMN_SIZE, size);
		return db.update(DATABASE_TABLE, args, KEY_ID+"="+rowID, null) > 0;
	}
}









