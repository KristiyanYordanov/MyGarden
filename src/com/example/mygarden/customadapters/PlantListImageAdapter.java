package com.example.mygarden.customadapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mygarden.R;
import com.example.mygarden.bean.Plant;

public class PlantListImageAdapter extends ArrayAdapter<Plant> {

	Context context;
	int layoutResourceId;
	ArrayList<Plant> data = new ArrayList<Plant>();

	public PlantListImageAdapter(Context context, int layoutResourceId,
			ArrayList<Plant> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ImageHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ImageHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.plantName);
			holder.imgIcon = (ImageView) row.findViewById(R.id.plantImage);
			row.setTag(holder);
		} else {
			holder = (ImageHolder) row.getTag();
		}
		Plant plant = data.get(position);
		holder.txtTitle.setText(plant.getPlantName());
		// convert byte to bitmap take from Plant class
		byte[] outImage = plant.getPlantImage();
		System.out.println("outImage=" + outImage);
		if (null != outImage) {
			Bitmap bm = convertBlobToBitmap(outImage);
			holder.imgIcon.setImageBitmap(bm);
		}
		return row;
	}

	static class ImageHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}

	public Bitmap convertBlobToBitmap(byte[] blobByteArray) {
		Bitmap tempBitmap = null;
		if (blobByteArray != null)
			tempBitmap = BitmapFactory.decodeByteArray(blobByteArray, 0,
					blobByteArray.length);
		return tempBitmap;
	}
}
