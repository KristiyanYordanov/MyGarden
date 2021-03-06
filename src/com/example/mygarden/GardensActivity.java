package com.example.mygarden;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.mygarden.db.DatabaseOpenHelper;
import com.example.mygarden.receivers.NotificationPublisher;

public class GardensActivity extends ListActivity {

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

				Intent plantsActivity = new Intent(getApplicationContext(),
						PlantsActivity.class);
				plantsActivity.putExtra("GARDEN_ID", gardenId);
				startActivity(plantsActivity);

				scheduleNotification(getNotification("5 second delay"), 5000);
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

	private void scheduleNotification(Notification notification, int delay) {

		Intent notificationIntent = new Intent(this,
				NotificationPublisher.class);
		notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
		notificationIntent.putExtra(NotificationPublisher.NOTIFICATION,
				notification);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		long futureInMillis = SystemClock.elapsedRealtime() + delay;
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis,
				pendingIntent);
	}

	private Notification getNotification(String content) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			System.out.println("Jelly bean");
			Notification.Builder builder = new Notification.Builder(this);
			builder.setContentTitle("Watering the plants");
			builder.setContentText(content);
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setOnlyAlertOnce(true);

			return builder.build();
		} else {
			System.out.println("ice cream");
			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					this);
			builder.setContentTitle("Watering the plants");
			builder.setContentText(content);
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setOnlyAlertOnce(true);

			return builder.build();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.top_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.flower_drawer:

			Intent addNewGardenIntent = new Intent(getApplicationContext(),
					FlowerDrawer.class);
			startActivity(addNewGardenIntent);
			return true;
		case R.id.more_help:
			Toast.makeText(getApplicationContext(), "you've been helped more",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.even_more_help:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.help_guide:
			Toast.makeText(getApplicationContext(), "ContextMenu Shown",
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return false;
		}
	}

}
