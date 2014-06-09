package com.example.mygarden;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
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
import android.widget.ViewFlipper;

import com.example.mygarden.bean.Plant;
import com.example.mygarden.customadapters.ImageAdapter;
import com.example.mygarden.customadapters.PlantListImageAdapter;
import com.example.mygarden.db.DatabaseOpenHelperPlant;

public class PlantsActivity extends Activity {

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

	private PlantListImageAdapter adapter;
	protected static final String EXTRA_RES_ID = "POS";

	private ViewFlipper mFlipper;
	private GestureDetector mGestureDetector;
	private boolean moreLeft = false;
	private boolean moreRight = false;

	private SQLiteDatabase mDB = null;
	private DatabaseOpenHelperPlant mDbHelperPlant;
	private int gardenId;
	private GridView gridview;
	private ListView listView;
	private View.OnTouchListener gestureListener;
	private Button button = null;
	private Animation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plant_flipper);
		System.out.println("PlantActivity onCreate!");
		button = (Button) findViewById(R.id.add_plant);
		listView = (ListView) findViewById(R.id.list);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gardenId = getIntent().getIntExtra("GARDEN_ID", 0);
		}
		// Create a new DatabaseHelper
		mDbHelperPlant = new DatabaseOpenHelperPlant(this);
		// Get the underlying database for writing
		mDB = mDbHelperPlant.getWritableDatabase();
		list();
		moreRight = true;
		mFlipper = (ViewFlipper) findViewById(R.id.plant_flipper);

		final GestureDetector gestureDetector = new GestureDetector(this,
				new MyGestureDetector());

		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		listView.setOnTouchListener(gestureListener);

	}

	class MyGestureDetector extends SimpleOnGestureListener {

		// Detect a single-click and call my own handler.
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// ListView lv = getListView();
			// int pos = lv.pointToPosition((int) e.getX(), (int) e.getY());
			// myOnItemClick(pos);
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {

					return false;
				}

				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (moreRight) {
						moreLeft = true;
						moreRight = false;
						switchLayoutStateTo(moreLeft);
						gridview.setOnTouchListener(gestureListener);

					}
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (moreLeft) {
						moreLeft = false;
						moreRight = true;
						switchLayoutStateTo(moreLeft);
						listView.setOnTouchListener(gestureListener);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

	}

	@Override
	protected void onResume() {
		System.out.println("PlantActivity onResume!");
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
		super.onStop();
	}

	@Override
	protected void onRestart() {
		System.out.println("PlantActivity onRestart!");
		list();
		super.onRestart();
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

	public void switchLayoutStateTo(boolean moreRight) {

		if (moreRight) {
			System.out.println("moreRight");
			// gridview code
			if (null != mFlipper) {
				mFlipper.setInAnimation(inFromRightAnimation());
				mFlipper.setOutAnimation(outToLeftAnimation());
			}
			grid();
		} else {

			// list code
			System.out.println("!moreRight");
			if (null != mFlipper) {
				mFlipper.setInAnimation(inFromLeftAnimation());
				mFlipper.setOutAnimation(outToRightAnimation());
			}
			list();
		}
		if (null != mFlipper) {
			mFlipper.showPrevious();
		}

	}

	private Animation inFromRightAnimation() {
		animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		animation.setDuration(500);
		animation.setInterpolator(new LinearInterpolator());
		return animation;
	}

	private Animation outToLeftAnimation() {
		animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		animation.setDuration(500);
		animation.setInterpolator(new LinearInterpolator());
		return animation;
	}

	private Animation inFromLeftAnimation() {
		animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		animation.setDuration(500);
		animation.setInterpolator(new LinearInterpolator());
		return animation;
	}

	private Animation outToRightAnimation() {
		animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		animation.setDuration(500);
		animation.setInterpolator(new LinearInterpolator());
		return animation;
	}

	public void list() {
		ArrayList<Plant> imageArry = new ArrayList<Plant>();
		List<Plant> plants = mDbHelperPlant.getPlatsById(gardenId);
		for (Plant cn : plants) {
			// add plants data in arrayList
			imageArry.add(cn);
		}
		adapter = new PlantListImageAdapter(this, R.layout.list_plants,
				imageArry);
		ListView dataList = (ListView) findViewById(R.id.list);
		dataList.setAdapter(adapter);

		dataList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg) {
				Intent intent = new Intent(getApplicationContext(),
						AddPlantActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent addPlantActivity = new Intent(getApplicationContext(),
						AddPlantActivity.class);
				addPlantActivity.putExtra("GARDEN_ID", gardenId);
				startActivity(addPlantActivity);
			}
		});
	}

	public void grid() {
		gridview = (GridView) findViewById(R.id.gridview);
		ArrayList<Plant> imageArry = new ArrayList<Plant>();
		List<Plant> plants = mDbHelperPlant.getPlantImagesById(gardenId);
		ArrayList<Bitmap> plantsList = new ArrayList<Bitmap>();
		ArrayList<Integer> plantsListIds = new ArrayList<Integer>();
		for (Plant cn : plants) {
			// add plants data in arrayList
			imageArry.add(cn);
			ByteArrayInputStream imageStream = new ByteArrayInputStream(
					cn.getPlantImage());
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			plantsList.add(theImage);

			plantsListIds.add(cn.get_id());

		}

		gridview.setAdapter(new ImageAdapter(this, plantsList, plantsListIds));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(PlantsActivity.this,
						ImageViewActivity.class);
				intent.putExtra(EXTRA_RES_ID, (int) id);
				startActivity(intent);
			}
		});
		final Button button = (Button) findViewById(R.id.add_plant_from_grid);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent addPlantActivity = new Intent(getApplicationContext(),
						AddPlantActivity.class);
				addPlantActivity.putExtra("GARDEN_ID", gardenId);
				startActivity(addPlantActivity);
			}
		});
	}
}
