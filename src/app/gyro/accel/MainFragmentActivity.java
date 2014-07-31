/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.gyro.accel;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainFragmentActivity extends FragmentActivity implements
		ActionBar.TabListener {

	// Defines variables and sensors

	final String tag = "GAP";

	SensorManager mSensorManager = null;
	Sensor mSensorTypeAccel, mSensorTypeGyro, mSensorType;
	TextView mXaxisAccel = null;
	TextView mYaxisAccel = null;
	TextView mZaxisAccel = null;
	TextView mXaxisGyro = null;
	TextView mYaxisGyro = null;
	TextView mZaxisGyro = null;
	TextView mNormLastClick, mUiLastClick, mGameLastClick, mFastLastClick;
	TextView mNormTotalTime, mUiTotalTime, mGameTotalTime, mFastTotalTime;
	TextView mNormClickCount, mUiClickCount, mGameClickCount, mFastClickCount;
	TextView mchronoNormText, mchronoGameText, mchronoUiText, mchronoFastText;
	TextView mTotal;
	String mDelaySelectionTextView;
	long timeElapsed;
	long durationSinceLastClick;
	long mStartTime;
	long mEndTime;
	long mTotalTime;
	long mTotalTimeNorm, mTotalTimeGame, mTotalTimeUI, mTotalTimeFast;
	int mSensorDelayNorm, mSensorDelayGame, mSensorDelayUI, mSensorDelayFast;
	int mSensorDelaySwitch;
	int mPreviousSelectedDelay = -1;
	int toggleButtonCounter = 0;
	int toggleButtonCounterAccel = 0;
	int toggleButtonCounterGyro = 0;
	int mClickCountNorm, mClickCountUI, mClickCountGame, mClickCountFast;
	int mClickCounter;
	Chronometer chronometerClock;
	Button normDelayButton, gameDelayButton, uiDelayButton, fastDelayButton;
	ToggleButton toggleGyro, toggleAccel;
	Intent intent;
	Button next;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Validate whether an accelerometer or gyroscope is present or not
		mSensorTypeAccel = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorTypeGyro = mSensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		if (mSensorTypeAccel != null) {
			Toast.makeText(this, "Accelerometer Found.", Toast.LENGTH_SHORT)
					.show();
		} else if (mSensorTypeAccel == null) {
			Toast.makeText(this, "No Accelerometer Found.", Toast.LENGTH_SHORT)
					.show();
		}
		if (mSensorTypeGyro != null) {
			Toast.makeText(this, "Gyroscope Found.", Toast.LENGTH_SHORT).show();
		} else if (mSensorTypeGyro == null) {
			Toast.makeText(this, "No Gyroscope Found.", Toast.LENGTH_SHORT)
					.show();
		}

		mSensorDelayNorm = SensorManager.SENSOR_DELAY_NORMAL;
		mSensorDelayGame = SensorManager.SENSOR_DELAY_GAME;
		mSensorDelayUI = SensorManager.SENSOR_DELAY_UI;
		mSensorDelayFast = SensorManager.SENSOR_DELAY_FASTEST;

		mSensorDelaySwitch = mSensorDelayNorm;

		mXaxisAccel = (TextView) findViewById(R.id.xbox);
		mYaxisAccel = (TextView) findViewById(R.id.ybox);
		mZaxisAccel = (TextView) findViewById(R.id.zbox);
		mXaxisGyro = (TextView) findViewById(R.id.xboxo);
		mYaxisGyro = (TextView) findViewById(R.id.yboxo);
		mZaxisGyro = (TextView) findViewById(R.id.zboxo);

		mNormLastClick = (TextView) findViewById(R.id.normLast);
		mGameLastClick = (TextView) findViewById(R.id.gameLast);
		mUiLastClick = (TextView) findViewById(R.id.uiLast);
		mFastLastClick = (TextView) findViewById(R.id.fastLast);

		mNormTotalTime = (TextView) findViewById(R.id.normTotal);
		mGameTotalTime = (TextView) findViewById(R.id.gameTotal);
		mUiTotalTime = (TextView) findViewById(R.id.uiTotal);
		mFastTotalTime = (TextView) findViewById(R.id.fastTotal);

		mNormClickCount = (TextView) findViewById(R.id.normCount);
		mGameClickCount = (TextView) findViewById(R.id.gameCount);
		mUiClickCount = (TextView) findViewById(R.id.uiCount);
		mFastClickCount = (TextView) findViewById(R.id.fastCount);

		mchronoNormText = (TextView) findViewById(R.id.chronoNormal);
		mchronoUiText = (TextView) findViewById(R.id.chronoUI);
		mchronoGameText = (TextView) findViewById(R.id.chronoGame);
		mchronoFastText = (TextView) findViewById(R.id.chronoFast);

		// normDelayButton = (Button) findViewById(R.id.ND);
		// gameDelayButton = (Button) findViewById(R.id.GD);
		// uiDelayButton = (Button) findViewById(R.id.UD);
		// fastDelayButton = (Button) findViewById(R.id.FD);
		//
		// normDelayButton.setBackgroundColor(Color.LTGRAY);
		// uiDelayButton.setBackgroundColor(Color.LTGRAY);
		// gameDelayButton.setBackgroundColor(Color.LTGRAY);
		// fastDelayButton.setBackgroundColor(Color.LTGRAY);

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return new LaunchpadSectionFragment();
			case 1:
				return new DataStream();

			default:
				// The other sections of the app are dummy placeholders.
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Section " + (position + 1);
		}
	}

	public static class DataStream extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.main, container, false);

			// Intent datastream = new Intent(getActivity(),
			// DataStreamActivity.class);
			// getActivity().startActivity(datastream);

			return rootView;
		}

	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class LaunchpadSectionFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_section_launchpad, container, false);



			// Demonstration of a collection-browsing activity.
			rootView.findViewById(R.id.demo_collection_button)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent intent = new Intent(getActivity(),
									LearnedActivityCollection.class);
							startActivity(intent);
						}
					});

			return rootView;
		} 
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_dummy,
					container, false);
			Bundle args = getArguments();
			((TextView) rootView.findViewById(android.R.id.text1))
					.setText(getString(R.string.dummy_section_text,
							args.getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
