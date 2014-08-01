//package app.gyroscope.accelerometer;
///*
// * Copyright 2013 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package app.gyro.accel;
//
//import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.SearchManager;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//
///**
// * This example illustrates a common usage of the DrawerLayout widget in the
// * Android support library.
// * <p/>
// * <p>
// * When a navigation (left) drawer is present, the host activity should detect
// * presses of the action bar's Up affordance as a signal to open and close the
// * navigation drawer. The ActionBarDrawerToggle facilitates this behavior. Items
// * within the drawer should fall into one of two categories:
// * </p>
// * <p/>
// * <ul>
// * <li><strong>View switches</strong>. A view switch follows the same basic
// * policies as list or tab navigation in that a view switch does not create
// * navigation history. This pattern should only be used at the root activity of
// * a task, leaving some form of Up navigation active for activities further down
// * the navigation hierarchy.</li>
// * <li><strong>Selective Up</strong>. The drawer allows the user to choose an
// * alternate parent for Up navigation. This allows a user to jump across an
// * app's navigation hierarchy at will. The application should treat this as it
// * treats Up navigation from a different task, replacing the current task stack
// * using TaskStackBuilder or similar. This is the only form of navigation drawer
// * that should be used outside of the root activity of a task.</li>
// * </ul>
// * <p/>
// * <p>
// * Right side drawers should be used for actions, not navigation. This follows
// * the pattern established by the Action Bar that navigation should be to the
// * left and actions to the right. An action should be an operation performed on
// * the current contents of the window, for example enabling or disabling a data
// * overlay on top of the current content.
// * </p>
// */
//public class DrawerActivity extends Activity {
//	private DrawerLayout mDrawerLayout;
//	private ListView mDrawerList;
//	private ActionBarDrawerToggle mDrawerToggle;
//
//	private CharSequence mDrawerTitle;
//	private CharSequence mTitle;
//	private String[] mPlanetTitles;
//
////	public MainActivity xyz;
//
//	// Intent launchActivity2 = new Intent(DrawerActivity.this,
//	// MainActivity.class);
//	// startActivity(Intent launchActivity2);
//
//	// Intent intent = new Intent(DrawerActivity.this, MainActivity.class);
//	// DrawerActivity.this.startActivity(intent);
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main_drawer);
//
////		xyz = new MainActivity();
//
//		mTitle = mDrawerTitle = getTitle();
//		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
//		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//		mDrawerList = (ListView) findViewById(R.id.left_drawer);
//
//		// set a custom shadow that overlays the main content when the drawer
//		// opens
//		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
//				GravityCompat.START);
//		// set up the drawer's list view with items and click listener
//		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//				R.layout.drawer_list_item, mPlanetTitles));
//		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//
//		// enable ActionBar app icon to behave as action to toggle nav drawer
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//		getActionBar().setHomeButtonEnabled(true);
//
//		// ActionBarDrawerToggle ties together the the proper interactions
//		// between the sliding drawer and the action bar app icon
//		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
//		mDrawerLayout, /* DrawerLayout object */
//		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
//		R.string.drawer_open, /* "open drawer" description for accessibility */
//		R.string.drawer_close /* "close drawer" description for accessibility */
//		) {
//			public void onDrawerClosed(View view) {
//				getActionBar().setTitle(mTitle);
//				invalidateOptionsMenu(); // creates call to
//											// onPrepareOptionsMenu()
//			}
//
//			public void onDrawerOpened(View drawerView) {
//				getActionBar().setTitle(mDrawerTitle);
//				invalidateOptionsMenu(); // creates call to
//											// onPrepareOptionsMenu()
//			}
//		};
//		mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//		if (savedInstanceState == null) {
//			selectItem(0);
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.main, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	/* Called whenever we call invalidateOptionsMenu() */
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		// If the nav drawer is open, hide action items related to the content
//		// view
//		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
//		return super.onPrepareOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// The action bar home/up action should open or close the drawer.
//		// ActionBarDrawerToggle will take care of this.
//		if (mDrawerToggle.onOptionsItemSelected(item)) {
//			return true;
//		}
//		// Handle action buttons
//		switch (item.getItemId()) {
//		case R.id.action_websearch:
//			// create intent to perform web search for this planet
//			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//			// catch event that there's no activity to handle intent
//			if (intent.resolveActivity(getPackageManager()) != null) {
//				startActivity(intent);
//			} else {
//				Toast.makeText(this, R.string.app_not_available,
//						Toast.LENGTH_LONG).show();
//			}
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
//
//	/* The click listner for ListView in the navigation drawer */
//	private class DrawerItemClickListener implements
//			ListView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			selectItem(position);
//		}
//	}
//
//	private void selectItem(int position) {
//		// update the main content by replacing fragments
//		Fragment fragment = new PlanetFragment();
//		Bundle args = new Bundle();
//		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//		fragment.setArguments(args);
//
//		FragmentManager fragmentManager = getFragmentManager();
//		fragmentManager.beginTransaction()
//				.replace(R.id.content_frame, fragment).commit();
//
//		// update selected item and title, then close the drawer
//		mDrawerList.setItemChecked(position, true);
//		setTitle(mPlanetTitles[position]);
//		mDrawerLayout.closeDrawer(mDrawerList);
//	}
//
//	@Override
//	public void setTitle(CharSequence title) {
//		mTitle = title;
//		getActionBar().setTitle(mTitle);
//	}
//
//	/**
//	 * When using the ActionBarDrawerToggle, you must call it during
//	 * onPostCreate() and onConfigurationChanged()...
//	 */
//
//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//		// Sync the toggle state after onRestoreInstanceState has occurred.
//		mDrawerToggle.syncState();
//	}
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		// Pass any configuration change to the drawer toggls
//		mDrawerToggle.onConfigurationChanged(newConfig);
//	}
//
//	/**
//	 * Fragment that appears in the "content_frame", shows a planet
//	 */
//	public static class PlanetFragment extends Fragment {
//
//		public static final String ARG_PLANET_NUMBER = "planet_number";
//
//		public PlanetFragment() {
//			// Empty constructor required for fragment subclasses
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.main, container, false);
//			int i = getArguments().getInt(ARG_PLANET_NUMBER);
//			String planet = getResources()
//					.getStringArray(R.array.planets_array)[i];
//
//			// Intent intent = new Intent(getActivity(), MainActivity.class);
//			// startActivity(intent);
//
//			// SHOULD START THE OTHER ACTIVITY
//			//
//			// activitybutton = (Button) findViewById(R.id.activity_button);
//			// activitybutton.setOnClickListener(new OnClickListener() {
//			// public void onClick(View v) {
//			// intent = new Intent(main.this, DrawerActivity.class);
//			// startActivity(intent);
//			// finish();
//			// }
//			// });
//
//			// int imageId = getResources().getIdentifier(
//			// planet.toLowerCase(Locale.getDefault()), "drawable",
//			// getActivity().getPackageName());
//			// ((ImageView) rootView.findViewById(R.id.image))
//			// .setImageResource(imageId);
//			getActivity().setTitle(planet);
//			return rootView;
//		}
//
//		// Defines variables and sensors
//
////		final String tag = "GAP";
////
////		SensorManager mSensorManager = null;
////		Sensor mSensorTypeAccel, mSensorTypeGyro, mSensorType;
////		TextView mXaxisAccel = null;
////		TextView mYaxisAccel = null;
////		TextView mZaxisAccel = null;
////		TextView mXaxisGyro = null;
////		TextView mYaxisGyro = null;
////		TextView mZaxisGyro = null;
////		TextView mNormLastClick, mUiLastClick, mGameLastClick, mFastLastClick;
////		TextView mNormTotalTime, mUiTotalTime, mGameTotalTime, mFastTotalTime;
////		TextView mNormClickCount, mUiClickCount, mGameClickCount,
////				mFastClickCount;
////		TextView mchronoNormText, mchronoGameText, mchronoUiText,
////				mchronoFastText;
////		TextView mTotal;
////		String mDelaySelectionTextView;
////		long durationSinceLastClick;
////		long mStartTime;
////		long mEndTime;
////		long mTotalTime;
////		long mTotalTimeNorm, mTotalTimeGame, mTotalTimeUI, mTotalTimeFast;
////		int mSensorDelayNorm, mSensorDelayGame, mSensorDelayUI,
////				mSensorDelayFast;
////		int mSensorDelaySwitch;
////		int mPreviousSelectedDelay = -1;
////		int toggleButtonCounter = 0;
////		int toggleButtonCounterAccel = 0;
////		int toggleButtonCounterGyro = 0;
////		int mClickCountNorm, mClickCountUI, mClickCountGame, mClickCountFast;
////		int mClickCounter;
////		Chronometer chronometerClock;
////		Button normDelayButton, gameDelayButton, uiDelayButton,
////				fastDelayButton;
////		ToggleButton toggleGyro, toggleAccel;
////
////		Button next;
//
//		// @Override
//		// public void onCreate(Bundle savedInstanceState) {
//		//
//		// super.onCreate(savedInstanceState);
//		//
//		// setContentView(R.layout.main);
//		//
//		// next = (Button) findViewById(R.id.activity_button);
//		// next.setOnClickListener(new View.OnClickListener() {
//		// public void onClick(View view) {
//		// Toast.makeText(getApplicationContext(), "text",
//		// Toast.LENGTH_SHORT).show();
//		// // Intent myIntent = new Intent(view.getContext(),
//		// // MainActivity.class);
//		// // startActivityForResult(myIntent, 0);
//		// }
//		// });
//		//
//		// mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//		//
//		// // Validate whether an accelerometer or gyroscope is present or not
//		// mSensorTypeAccel = mSensorManager
//		// .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//		// mSensorTypeGyro = mSensorManager
//		// .getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//		//
//		// if (mSensorTypeAccel != null) {
//		// Toast.makeText(this, "Accelerometer Found.", Toast.LENGTH_SHORT)
//		// .show();
//		// } else if (mSensorTypeAccel == null) {
//		// Toast.makeText(this, "No Accelerometer Found.",
//		// Toast.LENGTH_SHORT).show();
//		// }
//		// if (mSensorTypeGyro != null) {
//		// Toast.makeText(this, "Gyroscope Found.", Toast.LENGTH_SHORT)
//		// .show();
//		// } else if (mSensorTypeGyro == null) {
//		// Toast.makeText(this, "No Gyroscope Found.", Toast.LENGTH_SHORT)
//		// .show();
//		// }
//		//
//		// mSensorDelayNorm = SensorManager.SENSOR_DELAY_NORMAL;
//		// mSensorDelayGame = SensorManager.SENSOR_DELAY_GAME;
//		// mSensorDelayUI = SensorManager.SENSOR_DELAY_UI;
//		// mSensorDelayFast = SensorManager.SENSOR_DELAY_FASTEST;
//		//
//		// mSensorDelaySwitch = mSensorDelayNorm;
//		//
//		// mXaxisAccel = (TextView) findViewById(R.id.xbox);
//		// mYaxisAccel = (TextView) findViewById(R.id.ybox);
//		// mZaxisAccel = (TextView) findViewById(R.id.zbox);
//		// mXaxisGyro = (TextView) findViewById(R.id.xboxo);
//		// mYaxisGyro = (TextView) findViewById(R.id.yboxo);
//		// mZaxisGyro = (TextView) findViewById(R.id.zboxo);
//		//
//		// mNormLastClick = (TextView) findViewById(R.id.normLast);
//		// mGameLastClick = (TextView) findViewById(R.id.gameLast);
//		// mUiLastClick = (TextView) findViewById(R.id.uiLast);
//		// mFastLastClick = (TextView) findViewById(R.id.fastLast);
//		//
//		// mNormTotalTime = (TextView) findViewById(R.id.normTotal);
//		// mGameTotalTime = (TextView) findViewById(R.id.gameTotal);
//		// mUiTotalTime = (TextView) findViewById(R.id.uiTotal);
//		// mFastTotalTime = (TextView) findViewById(R.id.fastTotal);
//		//
//		// mNormClickCount = (TextView) findViewById(R.id.normCount);
//		// mGameClickCount = (TextView) findViewById(R.id.gameCount);
//		// mUiClickCount = (TextView) findViewById(R.id.uiCount);
//		// mFastClickCount = (TextView) findViewById(R.id.fastCount);
//		//
//		// mchronoNormText = (TextView) findViewById(R.id.chronoNormal);
//		// mchronoUiText = (TextView) findViewById(R.id.chronoUI);
//		// mchronoGameText = (TextView) findViewById(R.id.chronoGame);
//		// mchronoFastText = (TextView) findViewById(R.id.chronoFast);
//		//
//		// normDelayButton = (Button) findViewById(R.id.ND);
//		// gameDelayButton = (Button) findViewById(R.id.GD);
//		// uiDelayButton = (Button) findViewById(R.id.UD);
//		// fastDelayButton = (Button) findViewById(R.id.FD);
//		//
//		// normDelayButton.setBackgroundColor(Color.LTGRAY);
//		// uiDelayButton.setBackgroundColor(Color.LTGRAY);
//		// gameDelayButton.setBackgroundColor(Color.LTGRAY);
//		// fastDelayButton.setBackgroundColor(Color.LTGRAY);
//		//
//		// chronometerClock = (Chronometer) findViewById(R.id.chronometer1);
//		// chronometerClock.setBase(SystemClock.elapsedRealtime());
//		// chronometerClock.start();
//		//
//		// toggleGyro = (ToggleButton) findViewById(R.id.powerGyro);
//		// toggleAccel = (ToggleButton) findViewById(R.id.powerAccel);
//		//
//		// toggleGyro.setChecked(false);
//		// toggleAccel.setChecked(false);
//		//
//		// toggleGyro.setOnClickListener(new OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// if (toggleGyro.isChecked()) {
//		//
//		// mSensorType = mSensorTypeGyro;
//		//
//		// toggleButtonCounterGyro = 1;
//		//
//		// mButtonOnClickListener();
//		//
//		// } else {
//		//
//		// mSensorManager.unregisterListener(PlanetFragment.this,
//		// mSensorTypeGyro);
//		//
//		// toggleButtonCounterGyro = 0;
//		//
//		// toggleGyro.setChecked(false);
//		//
//		// }
//		// }
//		// });
//		// toggleAccel.setOnClickListener(new OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// if (toggleAccel.isChecked()) {
//		//
//		// mSensorType = mSensorTypeAccel;
//		//
//		// toggleButtonCounterAccel = 1;
//		//
//		// mButtonOnClickListener();
//		//
//		// } else {
//		//
//		// mSensorManager.unregisterListener(PlanetFragment.this,
//		// mSensorTypeAccel);
//		//
//		// toggleButtonCounterAccel = 0;
//		//
//		// toggleAccel.setChecked(false);
//		//
//		// }
//		// }
//		// });
//		//
//		// }
//		//
//		// public void setCheckedFalse() {
//		//
//		// toggleAccel.setChecked(false);
//		//
//		// toggleGyro.setChecked(false);
//		//
//		// toggleButtonCounter = 0;
//		//
//		// mSensorManager.unregisterListener(MainActivity.this,
//		// mSensorTypeAccel);
//		//
//		// mSensorManager.unregisterListener(MainActivity.this,
//		// mSensorTypeGyro);
//		// }
//		//
//		// public void mButtonOnClickListener() {
//		//
//		// setStartAndEndTime(); // Gets values for start and end time
//		// // calculations
//		//
//		// register();
//		//
//		// normDelayButton.setOnClickListener(new View.OnClickListener() {
//		//
//		// @Override
//		// public void onClick(View v) {
//		// if (toggleButtonCounterAccel == 1
//		// || toggleButtonCounterGyro == 1) {
//		//
//		// mSensorDelaySwitch = mSensorDelayNorm;
//		//
//		// currentTime();
//		//
//		// mPreviousSelectedDelay = mSensorDelayNorm;
//		//
//		// } else if (toggleButtonCounterAccel == 0
//		// && toggleButtonCounterGyro == 0) {
//		//
//		// setCheckedFalse();
//		//
//		// }
//		// }
//		// });
//		//
//		// uiDelayButton.setOnClickListener(new View.OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// if (toggleButtonCounterAccel == 1
//		// || toggleButtonCounterGyro == 1) {
//		//
//		// mSensorDelaySwitch = mSensorDelayUI;
//		//
//		// currentTime();
//		//
//		// mPreviousSelectedDelay = mSensorDelayUI;
//		//
//		// } else if (toggleButtonCounterAccel == 0
//		// && toggleButtonCounterGyro == 0) {
//		//
//		// setCheckedFalse();
//		//
//		// }
//		// }
//		// });
//		//
//		// gameDelayButton.setOnClickListener(new View.OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// if (toggleButtonCounterAccel == 1
//		// || toggleButtonCounterGyro == 1) {
//		//
//		// mSensorDelaySwitch = mSensorDelayGame;
//		//
//		// currentTime();
//		//
//		// mPreviousSelectedDelay = mSensorDelayGame;
//		//
//		// } else if (toggleButtonCounterAccel == 0
//		// && toggleButtonCounterGyro == 0) {
//		//
//		// setCheckedFalse();
//		// }
//		// }
//		// });
//		//
//		// fastDelayButton.setOnClickListener(new View.OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// if (toggleButtonCounterAccel == 1
//		// || toggleButtonCounterGyro == 1) {
//		//
//		// mSensorDelaySwitch = mSensorDelayFast;
//		//
//		// currentTime();
//		//
//		// mPreviousSelectedDelay = mSensorDelayFast;
//		//
//		// } else if (toggleButtonCounterAccel == 0
//		// && toggleButtonCounterGyro == 0) {
//		//
//		// setCheckedFalse();
//		// }
//		// }
//		// });
//		// }
//		//
//		// public void currentTime() {
//		// if (toggleButtonCounterAccel == 1 || toggleButtonCounterGyro == 1) {
//		//
//		// if (mSensorDelaySwitch == mSensorDelayNorm) {
//		//
//		// mEndTime = System.nanoTime(); // put this as EndTime earlier
//		//
//		// durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
//		// timeSinceLastClick();// Calculates time since last button
//		// // click
//		//
//		// mClickCountNorm++;
//		// mClickCounter = mClickCountNorm;
//		//
//		// } else if (mSensorDelaySwitch == mSensorDelayUI) {
//		//
//		// mEndTime = System.nanoTime();
//		//
//		// durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
//		// timeSinceLastClick();
//		//
//		// mClickCountUI++;
//		// mClickCounter = mClickCountUI;
//		//
//		// } else if (mSensorDelaySwitch == mSensorDelayGame) {
//		//
//		// mEndTime = System.nanoTime();
//		//
//		// durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
//		// timeSinceLastClick();
//		//
//		// mClickCountGame++;
//		// mClickCounter = mClickCountGame;
//		//
//		// } else if (mSensorDelaySwitch == mSensorDelayFast) {
//		//
//		// mEndTime = System.nanoTime();
//		//
//		// durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
//		// timeSinceLastClick();
//		//
//		// mClickCountFast++;
//		// mClickCounter = mClickCountFast;
//		//
//		// }
//		//
//		// clickStatisticsText(); // Sets text on display
//		//
//		// setStartAndEndTime(); // Unregisters current Delay setting
//		// // listeners and Registers them in new
//		// // Delay
//		// // settings
//		//
//		// register();
//		//
//		// } else if (toggleButtonCounterAccel == 0
//		// && toggleButtonCounterGyro == 0) {
//		//
//		// setCheckedFalse();
//		//
//		// return;
//		//
//		// }
//		// }
//		//
//		// public void timeSinceLastClick() {
//		//
//		// if (mPreviousSelectedDelay == mSensorDelayNorm) {
//		//
//		// mTotalTimeNorm = mTotalTimeNorm + durationSinceLastClick;
//		//
//		// mTotalTime = mTotalTimeNorm / 1000;
//		//
//		// } else if (mPreviousSelectedDelay == mSensorDelayUI) {
//		//
//		// mTotalTimeUI = mTotalTimeUI + durationSinceLastClick;
//		//
//		// mTotalTime = mTotalTimeUI / 1000;
//		//
//		// } else if (mPreviousSelectedDelay == mSensorDelayGame) {
//		//
//		// mTotalTimeGame = mTotalTimeGame + durationSinceLastClick;
//		//
//		// mTotalTime = mTotalTimeGame / 1000;
//		//
//		// } else if (mPreviousSelectedDelay == mSensorDelayFast) {
//		//
//		// mTotalTimeFast = mTotalTimeFast + durationSinceLastClick;
//		//
//		// mTotalTime = mTotalTimeFast / 1000;
//		//
//		// }
//		//
//		// }
//		//
//		// public void clickStatisticsText() {
//		//
//		// if (mSensorDelaySwitch == mSensorDelayNorm) {
//		//
//		// mNormLastClick.setText(String.valueOf(durationSinceLastClick));
//		//
//		// mNormLastClick.setText("" + durationSinceLastClick);
//		//
//		// mTotal = mNormTotalTime;
//		//
//		// previousMode();
//		//
//		// total();
//		//
//		// mNormClickCount.setText("" + mClickCounter);
//		//
//		// delayColorChange();
//		//
//		// } else if (mSensorDelaySwitch == mSensorDelayUI) {
//		//
//		// mUiLastClick.setText(String.valueOf(durationSinceLastClick));
//		//
//		// mUiLastClick.setText("" + durationSinceLastClick);
//		//
//		// mTotal = mUiTotalTime;
//		//
//		// previousMode();
//		//
//		// total();
//		//
//		// mUiClickCount.setText("" + mClickCounter);
//		//
//		// delayColorChange();
//		//
//		// } else if (mSensorDelaySwitch == mSensorDelayGame) {
//		//
//		// mGameLastClick.setText(String.valueOf(durationSinceLastClick));
//		//
//		// mGameLastClick.setText("" + durationSinceLastClick);
//		//
//		// mTotal = mGameTotalTime;
//		//
//		// previousMode();
//		//
//		// total();
//		//
//		// mGameClickCount.setText("" + mClickCounter);
//		//
//		// delayColorChange();
//		//
//		// } else if (mSensorDelaySwitch == mSensorDelayFast) {
//		//
//		// mFastLastClick.setText(String.valueOf(durationSinceLastClick));
//		//
//		// mFastLastClick.setText("" + durationSinceLastClick);
//		//
//		// mTotal = mFastTotalTime;
//		//
//		// previousMode();
//		//
//		// total();
//		//
//		// mFastClickCount.setText("" + mClickCounter);
//		//
//		// delayColorChange();
//		//
//		// }
//		//
//		// }
//		//
//		// public void setStartAndEndTime() {
//		//
//		// mEndTime = System.nanoTime();
//		//
//		// mStartTime = System.nanoTime();
//		//
//		// }
//		//
//		// public void register() {
//		// mSensorManager.unregisterListener(this);
//		//
//		// if (toggleGyro.isChecked()) {
//		//
//		// mSensorManager.registerListener(this, mSensorTypeGyro,
//		// mSensorDelaySwitch);
//		//
//		// }
//		// if (toggleAccel.isChecked()) {
//		//
//		// mSensorManager.registerListener(this, mSensorTypeAccel,
//		// mSensorDelaySwitch);
//		//
//		// }
//		// }
//		//
//		// public void previousMode() {
//		//
//		// if (mPreviousSelectedDelay == mSensorDelaySwitch) {
//		//
//		// return;
//		//
//		// } else if (mPreviousSelectedDelay == mSensorDelayNorm) {
//		//
//		// mTotal = mNormTotalTime;
//		//
//		// } else if (mPreviousSelectedDelay == mSensorDelayUI) {
//		//
//		// mTotal = mUiTotalTime;
//		//
//		// } else if (mPreviousSelectedDelay == mSensorDelayGame) {
//		//
//		// mTotal = mGameTotalTime;
//		//
//		// } else if (mPreviousSelectedDelay == mSensorDelayFast) {
//		//
//		// mTotal = mFastTotalTime;
//		//
//		// }
//		//
//		// }
//		//
//		// public void total() {
//		//
//		// mTotal.setText(String.valueOf(mTotalTime));
//		//
//		// mTotal.setText("" + mTotalTime);
//		//
//		// }
//		//
//		// public void delayColorChange() {
//		// if (mSensorDelaySwitch == mSensorDelayNorm) {
//		//
//		// mchronoNormText.setBackgroundColor(0x55000000);
//		// mchronoUiText.setBackgroundColor(0x55FFFFFF);
//		// mchronoGameText.setBackgroundColor(0x55FFFFFF);
//		// mchronoFastText.setBackgroundColor(0x55FFFFFF);
//		//
//		// normDelayButton.setBackgroundColor(Color.GREEN);
//		// uiDelayButton.setBackgroundColor(Color.LTGRAY);
//		// gameDelayButton.setBackgroundColor(Color.LTGRAY);
//		// fastDelayButton.setBackgroundColor(Color.LTGRAY);
//		//
//		// } else if (mSensorDelaySwitch == mSensorDelayUI) {
//		//
//		// mchronoNormText.setBackgroundColor(0x55FFFFFF);
//		// mchronoUiText.setBackgroundColor(0x55000000);
//		// mchronoGameText.setBackgroundColor(0x55FFFFFF);
//		// mchronoFastText.setBackgroundColor(0x55FFFFFF);
//		//
//		// normDelayButton.setBackgroundColor(Color.LTGRAY);
//		// uiDelayButton.setBackgroundColor(Color.GREEN);
//		// gameDelayButton.setBackgroundColor(Color.LTGRAY);
//		// fastDelayButton.setBackgroundColor(Color.LTGRAY);
//		//
//		// } else if (mSensorDelaySwitch == mSensorDelayGame) {
//		//
//		// mchronoNormText.setBackgroundColor(0x55FFFFFF);
//		// mchronoUiText.setBackgroundColor(0x55FFFFFF);
//		// mchronoGameText.setBackgroundColor(0x55000000);
//		// mchronoFastText.setBackgroundColor(0x55FFFFFF);
//		//
//		// normDelayButton.setBackgroundColor(Color.LTGRAY);
//		// uiDelayButton.setBackgroundColor(Color.LTGRAY);
//		// gameDelayButton.setBackgroundColor(Color.GREEN);
//		// fastDelayButton.setBackgroundColor(Color.LTGRAY);
//		//
//		// } else if (mSensorDelaySwitch == mSensorDelayFast) {
//		//
//		// mchronoNormText.setBackgroundColor(0x55FFFFFF);
//		// mchronoUiText.setBackgroundColor(0x55FFFFFF);
//		// mchronoGameText.setBackgroundColor(0x55FFFFFF);
//		// mchronoFastText.setBackgroundColor(0x55000000);
//		//
//		// normDelayButton.setBackgroundColor(Color.LTGRAY);
//		// uiDelayButton.setBackgroundColor(Color.LTGRAY);
//		// gameDelayButton.setBackgroundColor(Color.LTGRAY);
//		// fastDelayButton.setBackgroundColor(Color.GREEN);
//		//
//		// }
//		//
//		// }
//		//
//		// public void onSensorChanged(SensorEvent event) {
//		// Log.d(tag, "onSensorChanged: " + event.sensor + ", x: "
//		// + event.values[0] + ", y: " + event.values[1] + ", z: "
//		// + event.values[2]);
//		//
//		// float AxisX = event.values[0];
//		// float AxisY = event.values[1];
//		// float AxisZ = event.values[2];
//		//
//		// if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//		// mXaxisGyro.setText("Gyro X: " + AxisX);
//		// mYaxisGyro.setText("Gyro Y: " + AxisY);
//		// mZaxisGyro.setText("Gyro Z: " + AxisZ);
//		// }
//		// if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//		// mXaxisAccel.setText("Accel X: " + AxisX);
//		// mYaxisAccel.setText("Accel Y: " + AxisY);
//		// mZaxisAccel.setText("Accel Z: " + AxisZ);
//		// }
//		//
//		// }
//		//
//		// @Override
//		// public void onAccuracyChanged(Sensor sensor, int accuracy) {
//		// Log.d(tag, "onAccuracyChanged: " + sensor + ", accuracy: "
//		// + accuracy);
//		// }
//		//
//		// public void onResume() {
//		// super.onResume();
//		// }
//		//
//		// @Override
//		// public void onPause() {
//		// super.onPause();
//		// mSensorManager.unregisterListener(this);
//		// }
//	}
//}