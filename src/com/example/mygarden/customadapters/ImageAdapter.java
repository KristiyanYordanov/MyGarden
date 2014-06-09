package com.example.mygarden.customadapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.mygarden.AddPlantActivity;
import com.example.mygarden.PlantsActivity;

public class ImageAdapter extends BaseAdapter {
	private static final int PADDING = 3;
	private static final int WIDTH = 270;
	private static final int HEIGHT = 250;
	private Context mContext;
	private List<Bitmap> mThumbIds;
	private List<Integer> plantsListIds;

	public ImageAdapter(Context c, List<Bitmap> images, List<Integer> ids) {
		mContext = c;
		this.mThumbIds = images;
		this.plantsListIds = ids;
	}

	@Override
	public int getCount() {
		return mThumbIds.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	// Will get called to provide the ID that
	// is passed to OnItemClickListener.onItemClick()
	@Override
	public long getItemId(int position) {
		Intent intent=new Intent(mContext, AddPlantActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = (ImageView) convertView;

		// if convertView's not recycled, initialize some attributes
		if (imageView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
			imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}

		imageView.setImageBitmap(mThumbIds.get(position));
		return imageView;
	}
}
