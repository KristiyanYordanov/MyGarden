package com.example.mygarden.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelperPlant extends SQLiteOpenHelper {
	
	public final static String TABLE_NAME = "plant";
	public final static String PLANT_NAME = "plant_name";
	public final static String PLANT_PLACE = "plant_place";
	public final static String PLANT_TYPE = "plant_type";
	public final static String PLANT_IMAGE = "plant_image";
	public final static String GARDEN_ID = "garden_id";
	public final static String _ID = "_id";
	public  final static String[] columns = { _ID, PLANT_NAME, PLANT_PLACE, PLANT_TYPE, PLANT_IMAGE, GARDEN_ID  };

	final private static String CREATE_CMD =

	"CREATE TABLE " + TABLE_NAME + " (" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ PLANT_NAME + " TEXT NOT NULL,"
			+ PLANT_PLACE + " TEXT, "
			+ PLANT_TYPE + " TEXT, "
			+ PLANT_IMAGE + " BLOB, "
			+ GARDEN_ID + " INTEGER, "
			+ "FOREIGN KEY(" + GARDEN_ID  + ") REFERENCES "
			+ DatabaseOpenHelper.TABLE_NAME + "(" + DatabaseOpenHelper._ID + ")"
					+ ")";

	final private static String NAME = "plant_db";
	final private static Integer VERSION = 1;
	final private Context mContext;

	public DatabaseOpenHelperPlant(Context context) {
		super(context, NAME, null, VERSION);
		this.mContext = context;
		//mContext.deleteDatabase(NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
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
