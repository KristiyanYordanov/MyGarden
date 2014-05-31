package com.example.mygarden;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.mygarden.db.DatabaseOpenHelperPlant;

public class PlantsActivity extends ListActivity {

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;

	private ArrayList<Integer> mThumbIdsFlowers = new ArrayList<Integer>(
			Arrays.asList(R.drawable.output_0, R.drawable.output_0,
					R.drawable.output_0, R.drawable.output_0,
					R.drawable.output_0, R.drawable.output_0,
					R.drawable.output_0, R.drawable.output_0,
					R.drawable.output_0, R.drawable.output_0,
					R.drawable.output_0, R.drawable.output_0));

	protected static final String EXTRA_RES_ID = "POS";
	private ViewFlipper mFlipper;
	private GestureDetector mGestureDetector;
	int list = 0;
	int grid = 1;
	boolean moreLeft = false;
	boolean moreRight = false;

	private SQLiteDatabase mDB = null;
	private DatabaseOpenHelperPlant mDbHelperPlant;
	private SimpleCursorAdapter mAdapter;
	int gardenId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plant_flipper);
		System.out.println("PlantActivity onCreate!");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gardenId = getIntent().getIntExtra("GARDEN_ID", 0);
		}

		switchLayoutStateTo(list);
		moreRight = true;
		mFlipper = (ViewFlipper) findViewById(R.id.plant_flipper);

		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						try {

							if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {

								return false;
							}

							if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
									&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
								if (moreRight) {
									moreLeft = true;
									moreRight = false;
									switchLayoutStateTo(grid);

								}
							} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
									&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

								if (moreLeft) {
									switchLayoutStateTo(list);
									moreLeft = false;
									moreRight = true;
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						return false;
					}
				});

	}

	@Override
	protected void onResume() {
		System.out.println("PlantActivity onResume!");
		if (null != mAdapter) {
			if (mAdapter.getCount() == 0) {
				Cursor c = readPlants();
				System.out.println("c.getCount()=" + c.getCount());
				mAdapter = new SimpleCursorAdapter(this, R.layout.list_plants,
						c, DatabaseOpenHelperPlant.columns, new int[] { 0,
								R.id.plant_name }, 0);

				setListAdapter(mAdapter);
			}
		}

		super.onResume();
	}

	@Override
	protected void onPause() {
		System.out.println("PlantActivity onPause!");
		super.onPause();
	}

	@Override
	protected void onStop() {
		System.out.println("PlantActivity onStop!");
		// mDbHelperPlant.deleteDatabase();
		super.onStop();
	}

	@Override
	protected void onRestart() {
		System.out.println("PlantActivity onRestart!");
		Cursor c = readPlants();

		mAdapter = new SimpleCursorAdapter(this, R.layout.list_plants, c,
				DatabaseOpenHelperPlant.columns,
				new int[] { 0, R.id.plant_name }, 0);

		setListAdapter(mAdapter);
		super.onRestart();
	}

	// Returns all artist records in the database
	private Cursor readPlants() {

		String whereClause = DatabaseOpenHelperPlant.GARDEN_ID + "= ?";
		String[] whereArgs = new String[] { Integer.valueOf(gardenId)
				.toString() };
		String orderBy = DatabaseOpenHelperPlant.PLANT_NAME;
		return mDB.query(DatabaseOpenHelperPlant.TABLE_NAME,
				DatabaseOpenHelperPlant.columns, whereClause, whereArgs, null,
				null, orderBy);
	}

	// Close database
	@Override
	protected void onDestroy() {
		mDB.close();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Intent startActivity = new Intent(getApplicationContext(),
				GardensActivity.class);
		startActivity(startActivity);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	public void switchLayoutStateTo(int switchTo) {
		if (null != mFlipper) {
			mFlipper.setInAnimation(inFromRightAnimation());
			mFlipper.setOutAnimation(outToLeftAnimation());

		}

		if (switchTo == 0) {
			// Create a new DatabaseHelper
			mDbHelperPlant = new DatabaseOpenHelperPlant(this);
			// Get the underlying database for writing
			mDB = mDbHelperPlant.getWritableDatabase();
			// Create a cursor
			Cursor c = readPlants();

			mAdapter = new SimpleCursorAdapter(this, R.layout.list_plants, c,
					DatabaseOpenHelperPlant.columns, new int[] { 0,
							R.id.plant_name }, 0);

			setListAdapter(mAdapter);

			final Button button = (Button) findViewById(R.id.add_plant);

			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent addPlantActivity = new Intent(
							getApplicationContext(), AddPlantActivity.class);
					addPlantActivity.putExtra("GARDEN_ID", gardenId);
					startActivity(addPlantActivity);
				}
			});

			ListView catlist;
			catlist = getListView();
			catlist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						final int position, long id) {

					Toast.makeText(getApplicationContext(),
							"List View Clicked:" + position, Toast.LENGTH_LONG)
							.show();
					Intent plantActivity = new Intent(getApplicationContext(),
							PlantsActivity.class);
					plantActivity.putExtra("GARDEN_ID", gardenId);
					startActivity(plantActivity);

				}
			});
		} else {
			GridView gridview = (GridView) findViewById(R.id.gridview);

			gridview.setAdapter(new ImageAdapter(this, mThumbIdsFlowers));

			gridview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Intent intent = new Intent(PlantsActivity.this,
							ImageViewActivity.class);
					intent.putExtra(EXTRA_RES_ID, (int) id);
					startActivity(intent);
				}
			});
		}
		if (null != mFlipper) {
			mFlipper.showPrevious();
		}

	}

	private Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(500);
		inFromRight.setInterpolator(new LinearInterpolator());
		return inFromRight;
	}

	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new LinearInterpolator());
		return outtoLeft;
	}

}
