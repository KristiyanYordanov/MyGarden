package com.example.mygarden;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setImageResource(intent.getIntExtra(PlantsActivity.EXTRA_RES_ID, 0));
		setContentView(imageView);
	}
}