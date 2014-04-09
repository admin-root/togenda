package SQLPrototype;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.DropBoxManager;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	//defining the table and column names
	public static final String TABLE_NAME = "comments";
	public static final String COLUMN_KEY = "_id";
	public static final String COLUMN_NAME = "comment";
	//construct the structure of the database
	private static final String DATABASE_NAME = "comments.db";
	private static final String DATABASE_CREATE = "create table if not exists "+TABLE_NAME+
		"("+COLUMN_KEY+" integer primary key autoincrement, "+COLUMN_NAME+" TEXT not null)";
	private static final int DATABASE_VERSION = 1;
	
	public MySQLiteHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version "+
			oldVersion+" to "+newVersion+" , which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);
	}

}
