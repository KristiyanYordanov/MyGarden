package com.example.mygarden.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CopyOfDatabaseOpenHelperGarden extends SQLiteOpenHelper {
	
	public final static String TABLE_NAME = "garden";
	public final static String GARDEN_NAME = "garden_name";
	public final static String GARDEN_PLACE = "garden_place";
	public final static String GARDEN_TYPE = "garden_type";
	public final static String _ID = "_id";
	public  final static String[] columns = { _ID, GARDEN_NAME, GARDEN_PLACE, GARDEN_TYPE  };

	final private static String CREATE_CMD =

	"CREATE TABLE " + TABLE_NAME + " (" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ GARDEN_NAME + " TEXT NOT NULL,"
			+ GARDEN_PLACE + " TEXT, "
			+ GARDEN_TYPE + " TEXT "
					+ ")";

	final private static String NAME = "garden_db";
	final private static Integer VERSION = 1;
	final private Context mContext;

	public CopyOfDatabaseOpenHelperGarden(Context context) {
		super(context, NAME, null, VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("created!!!!!!!!!!!!!!1");
		db.execSQL(CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	public void deleteDatabase() {
		mContext.deleteDatabase(NAME);
	}
}
