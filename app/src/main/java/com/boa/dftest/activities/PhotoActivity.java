package com.boa.dftest.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import com.boa.dftest.R;

public class PhotoActivity extends AppCompatActivity
{
	private View mContentView;
	private boolean mVisible;
	private static final boolean AUTO_HIDE			= true;
	private static final int AUTO_HIDE_DELAY_MILLIS	= 3000;
	private static final int UI_ANIMATION_DELAY		= 300;
	private final Handler mHideHandler				= new Handler();
	private final Runnable mHidePart2Runnable		= new Runnable()
	{
		@SuppressLint("InlinedApi")
		@Override
		public void run()
		{
			mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
	};
	private final Runnable mShowPart2Runnable = new Runnable()
	{
		@Override
		public void run()
		{
			ActionBar actionBar = getSupportActionBar();
			
			if(actionBar != null)
			{
				actionBar.show();
			}
		}
	};
	private final Runnable mHideRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			hide();
		}
	};
	private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener()
	{
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent)
		{
			if(AUTO_HIDE)
			{
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			
			return false;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		mVisible		= true;
		mContentView	= findViewById(R.id.fullscreen_content);
		mContentView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				toggle();
			}
		});
		
		findViewById(R.id.ivClose).setOnTouchListener(mDelayHideTouchListener);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		delayedHide(100);
	}
	
	private void toggle()
	{
		if(mVisible)
		{
			hide();
		}
		else
		{
			show();
		}
	}
	
	private void hide()
	{
		ActionBar actionBar = getSupportActionBar();
		
		if(actionBar != null)
		{
			actionBar.hide();
		}
		
		mVisible = false;
		mHideHandler.removeCallbacks(mShowPart2Runnable);
		mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
	}

	@SuppressLint("InlinedApi")
	private void show()
	{
		mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		mVisible = true;
		mHideHandler.removeCallbacks(mHidePart2Runnable);
		mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
	}
	
	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis)
	{
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}