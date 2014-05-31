package com.example.mygarden;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class GraphicsFrameAnimationActivity extends Activity {
	private AnimationDrawable mAnim;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ImageView imageView = (ImageView) findViewById(R.id.countdown_frame);

		imageView.setBackgroundResource(R.drawable.view_animation);

		mAnim = (AnimationDrawable) imageView.getBackground();

		checkIfAnimationDone(mAnim);
		
	}

	private void checkIfAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
                    checkIfAnimationDone(a);
                } else{
                	Intent startActivity = new Intent(getApplicationContext(),
							GardensActivity.class);
					startActivity(startActivity);
                }
            }
        }, timeBetweenChecks);
    };
    
	@Override
	protected void onPause() {
		super.onPause();
		if (mAnim.isRunning()) {
			System.out.println("Anim stop");
			mAnim.stop();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			mAnim.start();
		}
	}
}