package com.example.mygarden;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.mygarden.db.DatabaseOpenHelper;

public class StartActivity extends ListActivity {

	private SQLiteDatabase mDB = null;
	private DatabaseOpenHelper mDbHelper;
	private SimpleCursorAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		System.out.println("StartActivity onCreate!");
		// Create a new DatabaseHelper
		mDbHelper = new DatabaseOpenHelper(this);
		// Get the underlying database for writing
		mDB = mDbHelper.getWritableDatabase();

		// Create a cursor
		Cursor c = readGardens();
		System.out.println("c.getCount()=" + c.getCount());
		mAdapter = new SimpleCursorAdapter(this, R.layout.list_gardens, c,
				DatabaseOpenHelper.columns, new int[] { 0, R.id.name }, 0);

		setListAdapter(mAdapter);

		final Button button = (Button) findViewById(R.id.add_garden);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent addNewGardenIntent = new Intent(getApplicationContext(),
						AddGardenActivity.class);
				startActivity(addNewGardenIntent);
			}
		});

		ListView gardenList;
		gardenList = getListView();
		gardenList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {

				SQLiteCursor cursor = (SQLiteCursor) mAdapter.getItem(position);
				cursor.moveToPosition(position);
				// get _ID of the selected row
				int gardenId = cursor.getInt(0);
				// Toast.makeText(getApplicationContext(),
				// "List View Clicked:" + gardenId, Toast.LENGTH_LONG)
				// .show();

				Intent plantsActivity = new Intent(getApplicationContext(),
						PlantsActivity.class);
				plantsActivity.putExtra("GARDEN_ID", gardenId);
				startActivity(plantsActivity);

			}
		});

	}

	@Override
	protected void onResume() {
		System.out.println("StartActivity onResume!");
		super.onResume();
	}

	@Override
	protected void onPause() {
		System.out.println("StartActivity onPause!");
		super.onPause();
	}

	@Override
	protected void onStop() {
		System.out.println("StartActivity onStop!");
		// mDbHelper.deleteDatabase();

		super.onStop();
	}

	@Override
	protected void onRestart() {
		System.out.println("StartActivity onRestart!");
		Cursor c = readGardens();
		System.out.println("c.getCount()=" + c.getCount());
		mAdapter = new SimpleCursorAdapter(this, R.layout.list_gardens, c,
				DatabaseOpenHelper.columns, new int[] { 0, R.id.name }, 0);

		setListAdapter(mAdapter);
		super.onRestart();
	}

	// Returns all artist records in the database
	private Cursor readGardens() {
		String orderBy = DatabaseOpenHelper.GARDEN_NAME;
		return mDB.query(DatabaseOpenHelper.TABLE_NAME,
				DatabaseOpenHelper.columns, null, new String[] {}, null, null,
				orderBy);
	}

	// Modify the contents of the database
	private void fix() {

		// Sorry Lady Gaga :-(
		mDB.delete(DatabaseOpenHelper.TABLE_NAME,
				DatabaseOpenHelper.GARDEN_NAME + "=?",
				new String[] { "Lady Gaga" });

		// fix the Man in Black
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.GARDEN_NAME, "Johnny Cash");

		mDB.update(DatabaseOpenHelper.TABLE_NAME, values,
				DatabaseOpenHelper.GARDEN_NAME + "=?",
				new String[] { "Jawny Cash" });

	}

	// Delete all records
	private void clearAll() {

		mDB.delete(DatabaseOpenHelper.TABLE_NAME, null, null);

	}

	// Close database
	@Override
	protected void onDestroy() {

		mDB.close();
		// mDbHelper.deleteDatabase();

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Intent exitIntent = new Intent(Intent.ACTION_MAIN);
		exitIntent.addCategory(Intent.CATEGORY_HOME);
		exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(exitIntent);
	}
}
