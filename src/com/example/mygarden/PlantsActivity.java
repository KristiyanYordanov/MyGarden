package com.example.mygarden;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.mygarden.db.DatabaseOpenHelperPlant;

public class PlantsActivity extends ListActivity {

	private SQLiteDatabase mDB = null;
	private DatabaseOpenHelperPlant mDbHelperPlant;
	private SimpleCursorAdapter mAdapter;
	int gardenId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plants);
		System.out.println("ListActivity onCreate!");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gardenId = getIntent().getIntExtra("GARDEN_ID", 0);
		}
		System.out.println("Plant gardenId=" + gardenId);
		
		
		// Create a new DatabaseHelper
		mDbHelperPlant = new DatabaseOpenHelperPlant(this);
		// Get the underlying database for writing
		mDB = mDbHelperPlant.getWritableDatabase();
		// Create a cursor
		Cursor c = readPlants();
		System.out.println("c.getCount()=" + c.getCount());
		mAdapter = new SimpleCursorAdapter(this, R.layout.list_plants, c,
				DatabaseOpenHelperPlant.columns, new int[] { R.id._id,
						R.id.plant_name }, 0);

		setListAdapter(mAdapter);

		final Button button = (Button) findViewById(R.id.add_plant);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent addPlantActivity = new Intent(getApplicationContext(),
						AddPlantActivity.class);
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
				System.out.println("Plant onItemClick gardenId=" + gardenId);
				Intent plantActivity = new Intent(getApplicationContext(),
						PlantsActivity.class);
				plantActivity.putExtra("GARDEN_ID", gardenId);
				startActivity(plantActivity);

			}
		});

	}

	@Override
	protected void onResume() {
		System.out.println("PlantActivity onResume!");
		if (mAdapter.getCount() ==0) {
			Cursor c = readPlants();
			System.out.println("c.getCount()=" + c.getCount());
			mAdapter = new SimpleCursorAdapter(this, R.layout.list_plants, c,
					DatabaseOpenHelperPlant.columns, new int[] { R.id._id,
							R.id.plant_name }, 0);

			setListAdapter(mAdapter);
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
//		mDbHelperPlant.deleteDatabase();
		super.onStop();
	}

	@Override
	protected void onRestart() {
		System.out.println("PlantActivity onRestart!");
		Cursor c = readPlants();
		System.out.println("c.getCount()=" + c.getCount());
		mAdapter = new SimpleCursorAdapter(this, R.layout.list_plants, c,
				DatabaseOpenHelperPlant.columns, new int[] { R.id._id,
						R.id.plant_name }, 0);

		setListAdapter(mAdapter);
		super.onRestart();
	}

	// Returns all artist records in the database
	private Cursor readPlants() {
		System.out.println("gardenid=" + gardenId);
		String whereClause = DatabaseOpenHelperPlant.GARDEN_ID + "= ?";
		String[] whereArgs = new String[] { Integer.valueOf(gardenId).toString() };
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
				StartActivity.class);
		startActivity(startActivity);
	}
	
}
