package com.example.gyroaccelapp;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends Activity implements SensorEventListener {

	// Defines variables and sensors

	final String tag = "GAP";

	SensorManager mSensorManager = null;

	TextView mXaxisAccel = null;
	TextView mYaxisAccel = null;
	TextView mZaxisAccel = null;
	TextView mXaxisGyro = null;
	TextView mYaxisGyro = null;
	TextView mZaxisGyro = null;

	TextView mNormLastClick;
	TextView mUiLastClick;
	TextView mGameLastClick;
	TextView mFastLastClick;
	TextView mNormTotalTime;
	TextView mUiTotalTime;
	TextView mGameTotalTime;
	TextView mFastTotalTime;
	TextView mNormClickCount;
	TextView mUiClickCount;
	TextView mGameClickCount;
	TextView mFastClickCount;

	String mdelaySelectionTextView;

	Sensor mSensorTypeAccel;
	Sensor mSensorTypeGyro;

	long durationSinceLastClick;
	int dur = (int) durationSinceLastClick;

	long mNormStartTime;
	long mGameStartTime;
	long mUIStartTime;
	long mFastStartTime;
	long mStartTime;

	long mNormEndTime;
	long mGameEndTime;
	long mUIEndTime;
	long mFastEndTime;
	long mEndTime;

	long mEndT;
	long mEndTimeNorm;
	long mEndTimeGame;
	long mEndTimeUI;
	long mEndTimeFast;

	int mSensorDelayNormal;
	int mSensorDelayGame;
	int mSensorDelayUI;
	int mSensorDelayFastest;
	int mSensorDelaySwitch;

	int currentlyNormalSelected = 1;
	int currentlyUiSelected = 2;
	int currentlyGameSelected = 3;
	int currentlyFastSelected = 4;
	int currentDelaySelection;

	int delayModeNorm = 1;
	int delayModeUI = 2;
	int delayModeGame = 3;
	int delayModeGast = 4;
	int delayMode;

	int previouslyNormalSelected = 1;
	int previouslyUiSelected = 2;
	int previouslyGameSelected = 3;
	int previouslyFastSelected = 4;
	int previousSelectedDelay;

	int toggleButtonCounter;
	int mClickCounterNorm;
	int mClickCounterUI;
	int mClickG;
	int mClickF;
	int mClickCounter;

	float mAxisX;
	float mAxisY;
	float mAxisZ;

	Chronometer chronometerClock;

	Button normDelayButton, gameDelayButton, uiDelayButton, fastDelayButton;
	ToggleButton toggleButton;

	/******************************************************************************/

	/** Called when the activity is first created. */
	/*
	 * Public: Sets up variables to specified sensors, time readings, TextViews
	 * Buttons, chronometer, and starts onClickListener for Sensor Delay
	 * Buttons.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		mSensorTypeAccel = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorTypeGyro = mSensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		mSensorDelayNormal = SensorManager.SENSOR_DELAY_NORMAL;
		mSensorDelayGame = SensorManager.SENSOR_DELAY_GAME;
		mSensorDelayUI = SensorManager.SENSOR_DELAY_UI;
		mSensorDelayFastest = SensorManager.SENSOR_DELAY_FASTEST;

		mSensorDelaySwitch = mSensorDelayNormal;

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

		normDelayButton = (Button) findViewById(R.id.ND);
		gameDelayButton = (Button) findViewById(R.id.GD);
		uiDelayButton = (Button) findViewById(R.id.UD);
		fastDelayButton = (Button) findViewById(R.id.FD);

		mNormStartTime = System.nanoTime();
		mGameStartTime = System.nanoTime();
		mUIStartTime = System.nanoTime();
		mFastStartTime = System.nanoTime();

		chronometerClock = (Chronometer) findViewById(R.id.chronometer1);
		chronometerClock.setBase(SystemClock.elapsedRealtime());
		chronometerClock.start();

		toggleButton = (ToggleButton) findViewById(R.id.powerSwitch);

		toggleButton.setChecked(false);

		/**
		 * Button Click listeners for On/Off Switch and Sensor Delay choice.
		 * Starts keeping time and registers listeners for Accelerometer and
		 * Gyroscope sensors after button click. Leads to function which starts
		 * the Click Statistics calculation and display loop.
		 */
		/*
		 * @@@@ IF STATEMENTS | LISTENERS | SWITCHES @@@@
		 * 
		 * onClickListener for On/Off Toggle Button - toggleButton
		 * 
		 * onClickListener for Sensor Delay Buttons (4) - *DelayButton
		 * 
		 * registerListener for Sensor Delay Types (mSensorType[Accel/Gyro],
		 * mSensorDelaySwitch)
		 * 
		 * @@@@ FUNCTIONS CALLED @@@@
		 * 
		 * currentTime
		 * 
		 * setStartAndEndTime
		 * 
		 * @@@@@ VARIABLES @@@@@
		 * 
		 * toggleButtonCounter - On/Off Toggle Button redundancy check
		 * 
		 * currentDelaySelection - variable to make sure the methods currentTime
		 * and clickStatisticsUpdateSetText are updating the values for the
		 * right Delay Setting
		 * 
		 *
		 * previousSelectedDelay - variable to make sure the method previousMode
		 * is updating the values for the right Delay Setting
		 */
		toggleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (toggleButton.isChecked()) {

					setStartAndEndTime();

					toggleButtonCounter = 1;

					mSensorManager.registerListener(MainActivity.this,
							mSensorTypeAccel, mSensorDelaySwitch);

					mSensorManager.registerListener(MainActivity.this,
							mSensorTypeGyro, mSensorDelaySwitch);

					normDelayButton
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									currentDelaySelection = currentlyNormalSelected;
									currentTime();
									previousSelectedDelay = previouslyNormalSelected;

								}
							});

					uiDelayButton
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									currentDelaySelection = currentlyUiSelected;
									currentTime();
									previousSelectedDelay = previouslyUiSelected;

								}
							});

					gameDelayButton
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									currentDelaySelection = currentlyGameSelected;
									currentTime();
									previousSelectedDelay = previouslyGameSelected;

								}
							});

					fastDelayButton
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									currentDelaySelection = currentlyFastSelected;
									currentTime();
									previousSelectedDelay = previouslyFastSelected;

								}
							});

				} else {
					mSensorManager.unregisterListener(MainActivity.this);

					toggleButton.setChecked(false);

					toggleButtonCounter = 0;

				}
			}
		});

	}

	/** Called when the sensors pick up new readings. */
	/*
	 * Public: Duplicate some text an arbitrary number of times.
	 * 
	 * text - the String to be duplicated. count - The Integer number of times
	 * to duplicate the text.
	 * 
	 * Examples
	 * 
	 * multiplex('Tom', 4) # => 'TomTomTomTom'
	 * 
	 * Returns the duplicated String.
	 */
	public void onSensorChanged(SensorEvent event) {
		Log.d(tag, "onSensorChanged: " + event.sensor + ", x: "
				+ event.values[0] + ", y: " + event.values[1] + ", z: "
				+ event.values[2]);

		mAxisX = event.values[0];
		mAxisY = event.values[1];
		mAxisZ = event.values[2];

		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			mXaxisGyro.setText("Gyro X: " + mAxisX);
			mYaxisGyro.setText("Gyro Y: " + mAxisY);
			mZaxisGyro.setText("Gyro Z: " + mAxisZ);
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mXaxisAccel.setText("Accel X: " + mAxisX);
			mYaxisAccel.setText("Accel Y: " + mAxisY);
			mZaxisAccel.setText("Accel Z: " + mAxisZ);
		}

	}

	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(tag, "onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
	}

	/** Called on button click from onCreate method. */
	/*
	 * Public: Duplicate some text an arbitrary number of times.
	 * 
	 * text - the String to be duplicated. count - The Integer number of times
	 * to duplicate the text.
	 * 
	 * Examples
	 * 
	 * multiplex('Tom', 4) # => 'TomTomTomTom'
	 * 
	 * Returns the duplicated String.
	 */
	public void currentTime() {
		if (toggleButtonCounter == 1) {

			if (currentDelaySelection == currentlyNormalSelected) {

				mNormEndTime = System.nanoTime();

				mEndTime = mNormEndTime;
				mStartTime = mNormStartTime;

				timeSinceLastClick();// Calculates time since last button click

				mClickCounterNorm++; // Increases click counter
				mClickCounter = mClickCounterNorm; // Sets displayed counter to
													// current click
				// counter

				mSensorDelaySwitch = mSensorDelayNormal; // Sensor Delay Setting
				mdelaySelectionTextView = "Normal"; // String for TextView
				clickStatisticsUpdateSetText();

			} else if (currentDelaySelection == currentlyUiSelected) {

				mUIEndTime = System.nanoTime();

				mEndTime = mUIEndTime;
				mStartTime = mUIStartTime;

				timeSinceLastClick();

				mClickCounterUI++;
				mClickCounter = mClickCounterUI;

				mSensorDelaySwitch = mSensorDelayUI;
				mdelaySelectionTextView = "UI";
				clickStatisticsUpdateSetText();

			} else if (currentDelaySelection == currentlyGameSelected) {

				mGameEndTime = System.nanoTime();

				mEndTime = mGameEndTime;
				mStartTime = mGameStartTime;

				timeSinceLastClick();

				mClickG++;
				mClickCounter = mClickG;

				mSensorDelaySwitch = mSensorDelayGame;
				mdelaySelectionTextView = "Game";
				clickStatisticsUpdateSetText();

			} else if (currentDelaySelection == currentlyFastSelected) {

				mFastEndTime = System.nanoTime();

				mEndTime = mFastEndTime;
				mStartTime = mFastStartTime;

				timeSinceLastClick();

				mClickF++;
				mClickCounter = mClickF;

				mSensorDelaySwitch = mSensorDelayFastest;
				mdelaySelectionTextView = "Fast";
				clickStatisticsUpdateSetText();
			}

			listenerOffOnSwitch();

		} else {

			return;

		}
	}

	/** Called when the sensors pick up new readings. */
	/*
	 * Public: Duplicate some text an arbitrary number of times.
	 * 
	 * text - the String to be duplicated. count - The Integer number of times
	 * to duplicate the text.
	 * 
	 * Examples
	 * 
	 * multiplex('Tom', 4) # => 'TomTomTomTom'
	 * 
	 * Returns the duplicated String.
	 */
	public void timeSinceLastClick() {

		durationSinceLastClick = (mEndTime - mStartTime) / 1000000; // Gets last
																	// click
																	// time and
																	// converts
																	// to
																	// milliseconds

		if (delayMode == delayModeNorm) {

			mEndTimeNorm = mEndTimeNorm + durationSinceLastClick; // Adds time
																	// since
																	// last to
																	// total
																	// time of
																	// button
																	// being
																	// clicked

			mEndT = mEndTimeNorm / 1000;// Converts milliseconds to seconds

		} else if (delayMode == delayModeUI) {

			mEndTimeUI = mEndTimeUI + durationSinceLastClick;
			mEndT = mEndTimeUI / 1000;

		} else if (delayMode == delayModeGame) {

			mEndTimeGame = mEndTimeGame + durationSinceLastClick;
			mEndT = mEndTimeGame / 1000;

		} else if (delayMode == delayModeGast) {

			mEndTimeFast = mEndTimeFast + durationSinceLastClick;
			mEndT = mEndTimeFast / 1000;

		}

	}

	/** Called when the sensors pick up new readings. */
	/*
	 * Public: Duplicate some text an arbitrary number of times.
	 * 
	 * text - the String to be duplicated. count - The Integer number of times
	 * to duplicate the text.
	 * 
	 * Examples
	 * 
	 * multiplex('Tom', 4) # => 'TomTomTomTom'
	 * 
	 * Returns the duplicated String.
	 */
	public void listenerOffOnSwitch() {

		mSensorManager.unregisterListener(this);
		mSensorManager.registerListener(this, mSensorTypeAccel,
				mSensorDelaySwitch);
		mSensorManager.registerListener(this, mSensorTypeGyro,
				mSensorDelaySwitch);

		setStartAndEndTime();

	}

	/** Called when the sensors pick up new readings. */
	/*
	 * Public: Duplicate some text an arbitrary number of times.
	 * 
	 * text - the String to be duplicated. count - The Integer number of times
	 * to duplicate the text.
	 * 
	 * Examples
	 * 
	 * multiplex('Tom', 4) # => 'TomTomTomTom'
	 * 
	 * Returns the duplicated String.
	 */
	public void clickStatisticsUpdateSetText() {

		if (currentDelaySelection == currentlyNormalSelected) {

			mNormLastClick.setText(String.valueOf(durationSinceLastClick)); // Converts
																			// long
																			// to
																			// string

			mNormLastClick.setText("" + durationSinceLastClick);

			previousMode();

			mNormClickCount.setText("" + mClickCounter);
			delayMode = delayModeNorm;

		} else if (currentDelaySelection == currentlyUiSelected) {

			mUiLastClick.setText(String.valueOf(durationSinceLastClick));

			mUiLastClick.setText("" + durationSinceLastClick);

			previousMode();

			mUiClickCount.setText("" + mClickCounter);
			delayMode = delayModeUI;

		} else if (currentDelaySelection == currentlyGameSelected) {

			mGameLastClick.setText(String.valueOf(durationSinceLastClick));

			mGameLastClick.setText("" + durationSinceLastClick);

			previousMode();

			mGameClickCount.setText("" + mClickCounter);
			delayMode = delayModeGame;

		} else if (currentDelaySelection == currentlyFastSelected) {

			mFastLastClick.setText(String.valueOf(durationSinceLastClick));

			mFastLastClick.setText("" + durationSinceLastClick);

			previousMode();

			mFastClickCount.setText("" + mClickCounter);
			delayMode = delayModeGast;

		}

	}

	/** Called when the sensors pick up new readings. */
	/*
	 * Public: Duplicate some text an arbitrary number of times.
	 * 
	 * text - the String to be duplicated. count - The Integer number of times
	 * to duplicate the text.
	 * 
	 * Examples
	 * 
	 * multiplex('Tom', 4) # => 'TomTomTomTom'
	 * 
	 * 
	 * - Sets time keeping counter
	 * 
	 * 
	 * Returns the duplicated String.
	 */
	public void setStartAndEndTime() {

		mNormEndTime = System.nanoTime();
		mGameEndTime = System.nanoTime();
		mUIEndTime = System.nanoTime();
		mFastEndTime = System.nanoTime();

		mNormStartTime = System.nanoTime();
		mGameStartTime = System.nanoTime();
		mUIStartTime = System.nanoTime();
		mFastStartTime = System.nanoTime();

	}

	/** Called when the sensors pick up new readings. */
	/*
	 * Public: Duplicate some text an arbitrary number of times.
	 * 
	 * text - the String to be duplicated. count - The Integer number of times
	 * to duplicate the text.
	 * 
	 * Examples
	 * 
	 * multiplex('Tom', 4) # => 'TomTomTomTom'
	 * 
	 * Returns the duplicated String.
	 */
	public void previousMode() {
		if (previousSelectedDelay == previouslyNormalSelected) {

			mNormTotalTime.setText(String.valueOf(mEndT));

			mNormTotalTime.setText("" + mEndT);

		} else if (previousSelectedDelay == previouslyUiSelected) {

			mUiTotalTime.setText(String.valueOf(mEndT));

			mUiTotalTime.setText("" + mEndT);

		} else if (previousSelectedDelay == previouslyGameSelected) {

			mGameTotalTime.setText(String.valueOf(mEndT));

			mGameTotalTime.setText("" + mEndT);

		} else if (previousSelectedDelay == previouslyFastSelected) {

			mFastTotalTime.setText(String.valueOf(mEndT));

			mFastTotalTime.setText("" + mEndT);

		}
	}
}
