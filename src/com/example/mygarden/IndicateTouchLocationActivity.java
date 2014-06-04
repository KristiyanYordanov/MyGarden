package com.example.mygarden;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.Toast;

public class IndicateTouchLocationActivity extends Activity {

	private static final int MIN_DXDY = 2;

	// Assume no more than 20 simultaneous touches
	final private static int MAX_TOUCHES = 12;

	private ArrayList<Integer> mThumbIdsFlowers = new ArrayList<Integer>(
			Arrays.asList(R.drawable.flowernew, R.drawable.flowernewnew,
					R.drawable.messenger, R.drawable.blue_flower,
					R.drawable.flower, R.drawable.pink_flower));

	int c = 0;
	// Pool of MarkerViews
	final private static LinkedList<MarkerView> mInactiveMarkers = new LinkedList<MarkerView>();

	// Set of MarkerViews currently visible on the display
	@SuppressLint("UseSparseArrays")
	final private static Map<Integer, MarkerView> mActiveMarkers = new HashMap<Integer, MarkerView>();

	protected static final String TAG = "IndicateTouchLocationActivity";

	private FrameLayout mFrame;
	Bitmap bitmap0;
	Bitmap bitmap1;
	Bitmap bitmap2;
	Bitmap bitmap3;
	Bitmap bitmap4;
	Bitmap bitmap5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw);

		bitmap0 = BitmapFactory.decodeResource(getResources(),
				mThumbIdsFlowers.get(0));
		bitmap1 = BitmapFactory.decodeResource(getResources(),
				mThumbIdsFlowers.get(1));
		bitmap2 = BitmapFactory.decodeResource(getResources(),
				mThumbIdsFlowers.get(2));
		bitmap3 = BitmapFactory.decodeResource(getResources(),
				mThumbIdsFlowers.get(3));
		bitmap4 = BitmapFactory.decodeResource(getResources(),
				mThumbIdsFlowers.get(4));
		bitmap5 = BitmapFactory.decodeResource(getResources(),
				mThumbIdsFlowers.get(5));

		mFrame = (FrameLayout) findViewById(R.id.frame);

		// Initialize pool of View.
		initViews();

		// Create and set on touch listener
		mFrame.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getActionMasked()) {

				// Show new MarkerView

				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN: {
					if (c < mThumbIdsFlowers.size() - 1) {
						c++;
					} else {
						c = 0;
					}
					mInactiveMarkers.add(new MarkerView(
							getApplicationContext(), -1, -1, 0));

					int pointerIndex = event.getActionIndex();
					int pointerID = event.getPointerId(pointerIndex);

					MarkerView marker = mInactiveMarkers.remove();

					if (null != marker) {

						mActiveMarkers.put(pointerID, marker);

						marker.setXLoc(event.getX(pointerIndex));
						marker.setYLoc(event.getY(pointerIndex));
						marker.setCount(c);

						updateTouches(mActiveMarkers.size());

						mFrame.addView(marker);
					}
					break;
				}

				// Remove one MarkerView

				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP: {
					//
					// int pointerIndex = event.getActionIndex();
					// int pointerID = event.getPointerId(pointerIndex);
					//
					// MarkerView marker = mActiveMarkers.remove(pointerID);
					//
					// if (null != marker) {
					//
					// mInactiveMarkers.add(marker);
					//
					// updateTouches(mActiveMarkers.size());
					//
					// mFrame.removeView(marker);
					// }
					// break;
					break;

				}

				// Move all currently active MarkerViews

				case MotionEvent.ACTION_MOVE: {

					for (int idx = 0; idx < event.getPointerCount(); idx++) {

						int ID = event.getPointerId(idx);

						MarkerView marker = mActiveMarkers.get(ID);
						if (null != marker) {

							// Redraw only if finger has travel ed a minimum
							// distance
							if (Math.abs(marker.getXLoc() - event.getX(idx)) > MIN_DXDY
									|| Math.abs(marker.getYLoc()
											- event.getY(idx)) > MIN_DXDY) {

								// Set new location

								marker.setXLoc(event.getX(idx));
								marker.setYLoc(event.getY(idx));

								// Request re-draw
								marker.invalidate();
							}
						}
					}

					break;
				}

				default:

					Log.i(TAG, "unhandled action");
				}

				return true;
			}

			// update number of touches on each active MarkerView
			private void updateTouches(int numActive) {
				for (MarkerView marker : mActiveMarkers.values()) {
					marker.setTouches(numActive);
				}
			}
		});

		mFrame.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				System.out.println("long click");
				Toast toast = new Toast(getApplicationContext());

				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_LONG);

				toast.setView(getLayoutInflater().inflate(
						R.layout.custom_toast, null));

				toast.show();
				return true;
			}
		});
	}

	private void initViews() {
		for (int idx = 0; idx < MAX_TOUCHES; idx++) {
			mInactiveMarkers.add(new MarkerView(this, -1, -1, 0));
		}
	}

	private class MarkerView extends View {
		private float mX, mY;
		final static private int MAX_SIZE = 100;
		private int mTouches = 0;
		final private Paint mPaint = new Paint();
		int count;

		public MarkerView(Context context, float x, float y, int c) {
			super(context);
			mX = x;
			mY = y;
			mPaint.setStyle(Style.FILL_AND_STROKE);
			count = c;
			Random rnd = new Random();
			mPaint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256),
					rnd.nextInt(256));

		}

		float getXLoc() {
			return mX;
		}

		void setXLoc(float x) {
			mX = x;
		}

		float getYLoc() {
			return mY;
		}

		void setYLoc(float y) {
			mY = y;
		}

		void setTouches(int touches) {
			mTouches = touches;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			System.out.println("count=" + count);
			if (count == 0) {
				canvas.drawBitmap(bitmap0, mX, mY, null);
			} else if (count == 1) {
				canvas.drawBitmap(bitmap1, mX, mY, null);
			} else if (count == 2) {
				canvas.drawBitmap(bitmap2, mX, mY, null);
			} else if (count == 3) {
				canvas.drawBitmap(bitmap3, mX, mY, null);
			} else if (count == 4) {
				canvas.drawBitmap(bitmap4, mX, mY, null);
			} else if (count == 5) {
				canvas.drawBitmap(bitmap5, mX, mY, null);
			}

		}
	}

	public void onPause() {
		saveSignature();
		super.onPause();
	}

	public void saveSignature() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int h = metrics.heightPixels;
		int w = metrics.widthPixels;
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		mFrame.draw(canvas);

		File file = new File(Environment.getExternalStorageDirectory()
				+ "/sign.png");
		OutputStream stream = null;
		try {
			file.createNewFile();
			stream = new FileOutputStream(file);
			/*
			 * Write bitmap to file using JPEG or PNG and 80% quality hint for
			 * JPEG.
			 */
			bitmap.compress(CompressFormat.PNG, 80, stream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stream != null) {
					stream.flush();
					stream.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}