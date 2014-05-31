package com.example.mygarden;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mygarden.db.DatabaseOpenHelperPlant;

public class AddPlantActivity extends Activity {

	private SQLiteDatabase mDB = null;
	private DatabaseOpenHelperPlant mDbHelper;
	int gardenId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_plant);

		gardenId = getIntent().getIntExtra("GARDEN_ID", 1);

		final EditText name = (EditText) findViewById(R.id.plant_name);
		final EditText place = (EditText) findViewById(R.id.plant_place);
		final EditText type = (EditText) findViewById(R.id.plant_type);
		final Button addGarden = (Button) findViewById(R.id.add_plant);

		// Create a new DatabaseHelper
		mDbHelper = new DatabaseOpenHelperPlant(this);
		// Get the underlying database for writing
		mDB = mDbHelper.getWritableDatabase();

		addGarden.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (name.getText().length() == 0) {
					Toast toast = new Toast(getApplicationContext());

					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.setDuration(Toast.LENGTH_LONG);

					toast.setView(getLayoutInflater().inflate(
							R.layout.custom_toast, null));

					toast.show();
				} else {
					ContentValues values = new ContentValues();

					values.put(DatabaseOpenHelperPlant.PLANT_NAME, name
							.getText().toString());
					if (!place.getText().toString().equals("")) {
						values.put(DatabaseOpenHelperPlant.PLANT_PLACE, place
								.getText().toString());
					}

					if (!type.getText().toString().equals("")) {
						values.put(DatabaseOpenHelperPlant.PLANT_TYPE, type
								.getText().toString());
					}

					values.put(DatabaseOpenHelperPlant.GARDEN_ID, gardenId);
					mDB.insert(DatabaseOpenHelperPlant.TABLE_NAME, null, values);

					values.clear();
					Intent plantsActivity = new Intent(getApplicationContext(),
							PlantsActivity.class);
					plantsActivity.putExtra("GARDEN_ID", gardenId);
					startActivity(plantsActivity);

				}
			}
		});
	}
}
