package com.example.gyroaccelapp;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
	TextView mNormLastClick, mUiLastClick, mGameLastClick, mFastLastClick;
	TextView mNormTotalTime, mUiTotalTime, mGameTotalTime, mFastTotalTime;
	TextView mNormClickCount, mUiClickCount, mGameClickCount, mFastClickCount;
	TextView mchronoNormText, mchronoGameText, mchronoUiText, mchronoFastText;
	TextView mTotal;
	String mDelaySelectionTextView;
	Sensor mSensorTypeAccel, mSensorTypeGyro, mSensorType;
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
	Button DelayButton;
	Button toSuper;
	Button toUnsuper;
	ToggleButton toggleGyro, toggleAccel;

	// Creates handler for Supervised and Unsupervised button clicks for color
	// to change for only 100 milliseconds
	final Handler handler = new Handler();
	final long delay = 100;

	// Information for logo spin animation
	private static final float ROTATE_FROM = 0.0f;
	private static final float ROTATE_TO = -1.0f * 360.0f;// 3.141592654f *
															// 32.0f;

	/******************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// This version of the app, the layout is locked on the
		// vertical/portrait position
		setContentView(R.layout.activity_main_vertical);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Validate whether an accelerometer or gyroscope is present or not.
		// Android Manifest file specifies that app cannot be used/downloaded if
		// phone does not have the sensors onboard. This functionality can be
		// taken out if the sensor check is kept on the Manifest
		mSensorTypeAccel = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorTypeGyro = mSensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		// Creates toast to indicate to user whether their phone has the
		// required sensors ////// Check if works. if not, revert to yes/no for
		// each
		if (mSensorTypeAccel != null && mSensorTypeGyro != null) {
			Toast.makeText(this, "Required Sensors Found", Toast.LENGTH_SHORT)
					.show();
		} else if (mSensorTypeAccel == null && mSensorTypeGyro != null) {
			Toast.makeText(this, "No Accelerometer Found", Toast.LENGTH_SHORT)
					.show();
		} else if (mSensorTypeGyro == null && mSensorTypeAccel != null) {
			Toast.makeText(this, "No Gyroscope Found", Toast.LENGTH_SHORT)
					.show();
		} else if (mSensorTypeGyro == null && mSensorTypeAccel == null) {
			Toast.makeText(this, "No Sensors Found", Toast.LENGTH_SHORT).show();
		}

		// Variables for Sensor Delays
		mSensorDelayNorm = SensorManager.SENSOR_DELAY_NORMAL;
		mSensorDelayGame = SensorManager.SENSOR_DELAY_GAME;
		mSensorDelayUI = SensorManager.SENSOR_DELAY_UI;
		mSensorDelayFast = SensorManager.SENSOR_DELAY_FASTEST;

		// Sets initial Sensor Delay when the first power button is clicked to
		// Normal Delay (longest delay). Otherwise, the default delay would be
		// Fastest
		mSensorDelaySwitch = mSensorDelayNorm;

		// Variables for each axis of the Accelerometer and Gyroscope
		mXaxisAccel = (TextView) findViewById(R.id.xbox);
		mYaxisAccel = (TextView) findViewById(R.id.ybox);
		mZaxisAccel = (TextView) findViewById(R.id.zbox);
		mXaxisGyro = (TextView) findViewById(R.id.xboxo);
		mYaxisGyro = (TextView) findViewById(R.id.yboxo);
		mZaxisGyro = (TextView) findViewById(R.id.zboxo);

		// Variables for the TextView which displays how long it has been since
		// the Sensor Delays have been changed
		mNormLastClick = (TextView) findViewById(R.id.normLast);
		mGameLastClick = (TextView) findViewById(R.id.gameLast);
		mUiLastClick = (TextView) findViewById(R.id.uiLast);
		mFastLastClick = (TextView) findViewById(R.id.fastLast);

		// Variables for the TextView which displays total time spent in the
		// specified Sensor Delay
		mNormTotalTime = (TextView) findViewById(R.id.normTotal);
		mGameTotalTime = (TextView) findViewById(R.id.gameTotal);
		mUiTotalTime = (TextView) findViewById(R.id.uiTotal);
		mFastTotalTime = (TextView) findViewById(R.id.fastTotal);

		// Variables for the TextView which displays how many times the
		// specified Sensor Delay button has been clicked (no actual
		// functionality, simply a tested feature. Can be eliminated)
		mNormClickCount = (TextView) findViewById(R.id.normCount);
		mGameClickCount = (TextView) findViewById(R.id.gameCount);
		mUiClickCount = (TextView) findViewById(R.id.uiCount);
		mFastClickCount = (TextView) findViewById(R.id.fastCount);

		// Variables for the TextView which displays name of the sensor delays.
		// Text itself does not change, but variables are in place to change the
		// color of the Sensor Delay text box whenever it is activated
		mchronoNormText = (TextView) findViewById(R.id.chronoNormal);
		mchronoUiText = (TextView) findViewById(R.id.chronoUI);
		mchronoGameText = (TextView) findViewById(R.id.chronoGame);
		mchronoFastText = (TextView) findViewById(R.id.chronoFast);

		// Buttons to choose between Normal, Game, UI, and Fastest Sensor Delays
		normDelayButton = (Button) findViewById(R.id.ND);
		gameDelayButton = (Button) findViewById(R.id.GD);
		uiDelayButton = (Button) findViewById(R.id.UD);
		fastDelayButton = (Button) findViewById(R.id.FD);

		// Sets the background colors of the Sensor Delay buttons to gray, which
		// increases the viewed button size. This keeps it the same color, but
		// ensures that it doesn't look weird when it changes color to green as
		// it is activated
		normDelayButton.setBackgroundColor(Color.LTGRAY);
		uiDelayButton.setBackgroundColor(Color.LTGRAY);
		gameDelayButton.setBackgroundColor(Color.LTGRAY);
		fastDelayButton.setBackgroundColor(Color.LTGRAY);

		// Buttons for Supervised and Unsupervised function/activity/fragment
		// calls. Their colors are set to Rithmio-specified yellow and green.
		// Can be changed at another point - file with color names is located at
		// res/values/color.xml
		toSuper = (Button) findViewById(R.id.toSupervised);
		toSuper.setBackgroundColor(getResources().getColor(
				R.color.RithmioYellow));
		toUnsuper = (Button) findViewById(R.id.toUnsupervised);
		toUnsuper.setBackgroundColor(getResources().getColor(
				R.color.RithmioGreen));

		// Starts the chronometer when the app/activity is opened
		chronometerClock = (Chronometer) findViewById(R.id.chronometer1);
		chronometerClock.setBase(SystemClock.elapsedRealtime());
		chronometerClock.start();

		// Toggle Buttons to power on and off the Accelerometer and Gyroscope
		// sensor data readings
		toggleGyro = (ToggleButton) findViewById(R.id.powerGyro);
		toggleAccel = (ToggleButton) findViewById(R.id.powerAccel);

		// Sets state of Accelerometer and Gyroscope power toggle buttons to off
		// as app/activity is initialized
		toggleGyro.setChecked(false);
		toggleAccel.setChecked(false);

		// onClickListener for Gyroscope toggle button
		toggleGyro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleGyro.isChecked()) {

					// Sets current sensor type to Gyroscope
					mSensorType = mSensorTypeGyro;

					// Turns on Gyroscope functionality redundancy check (See
					// onClickListeners for the sensor delay buttons)
					toggleButtonCounterGyro = 1;

					// Starts sensor data collection loops
					buttonOnClickListener();

				} else {

					// Unregisters Gyroscope sensor listener
					mSensorManager.unregisterListener(MainActivity.this,
							mSensorTypeGyro);

					// Turns off Gyroscope functionality redundancy check
					toggleButtonCounterGyro = 0;

					// Sets Gyroscope power button status to off
					toggleGyro.setChecked(false);

				}
			}
		});

		// onClickListener for Accelerometer toggle button
		toggleAccel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleAccel.isChecked()) {

					// Sets current sensor type to Accelerometer
					mSensorType = mSensorTypeAccel;

					// Turns on Accelerometer functionality redundancy check
					toggleButtonCounterAccel = 1;

					// Starts sensor data collection loops
					buttonOnClickListener();

				} else {

					// Unregisters Accelerometer sensor listener
					mSensorManager.unregisterListener(MainActivity.this,
							mSensorTypeAccel);

					// Turns off Accelerometer functionality redundancy check
					toggleButtonCounterAccel = 0;

					// Sets Accelerometer power button status to off
					toggleAccel.setChecked(false);

				}
			}
		});

		toSuper.setOnClickListener(new OnClickListener() { /*
															 * Check if onClick
															 * makes difference
															 * from onTouch
															 */// //////////////
			@Override
			public void onClick(View v) {

				// Sets color for Supervised Learning button to gray when
				// clicked
				toSuper.setBackgroundColor(getResources().getColor(
						R.color.RithmioGray));

				// Starts button color change function
				backgroundColorChange();

				// Insert here call for Supervised Learning
				// function/activity/fragment

			}
		});
		toUnsuper.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent arg1) {

				// Sets color for Unsupervised Learning button to black when
				// clicked
				toUnsuper.setBackgroundColor(getResources().getColor(
						R.color.Black));

				// Starts button color change function
				backgroundColorChange();

				return false;
			}
		});

		// Logo spin animation
		ImageView favicon = (ImageView) findViewById(R.id.logo);
		RotateAnimation r;
		r = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		r.setDuration((long) 1000);
		r.setRepeatCount(-1);
		favicon.startAnimation(r);

	}

	/**
	 * Changes button color back to original after specified amount of time in
	 * milliseconds (final long delay = 100).
	 */
	void backgroundColorChange() {
		handler.postDelayed(new Runnable() {
			public void run() {

				// Sets Supervised Learning button back to Rithmio Yellow
				toSuper.setBackgroundColor(getResources().getColor(
						R.color.RithmioYellow));

				// Sets Unsupervised Learning button back to Rithmio Green
				toUnsuper.setBackgroundColor(getResources().getColor(
						R.color.RithmioGreen));

			}

			// Current delay is 100 milliseconds. Amount set in variable
			// initializer
		}, delay);
	}

	/**
	 * Called when both Accelerometer and Gyroscope power buttons are toggled
	 * off. Sets their state to off, sets redundant state check to off, and
	 * unregisters both listeners.
	 */
	public void setCheckedFalse() {

		toggleAccel.setChecked(false);
		toggleGyro.setChecked(false);

		toggleButtonCounter = 0;

		mSensorManager.unregisterListener(MainActivity.this, mSensorTypeAccel);
		mSensorManager.unregisterListener(MainActivity.this, mSensorTypeGyro);
	}

	/**
	 * Starts time count, registers Accelerometer and/or Gyroscope sensors, and
	 * onClickListeners for each Sensor Delay button. Each button then sets the
	 * current Sensor Delay being used to the specified button clicked, starts
	 * the Sensor Delay statistics loop, then sets the current Sensor Delay as
	 * the "previous" sensor delay.
	 */
	public void buttonOnClickListener() {

		// Gets values for start and end time calculations
		setStartAndEndTime();

		// Registers Accelerometer and/or Gyroscope sensors, depending on toggle
		// button state
		register();

		normDelayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Checks if either Accelerometer or Gyroscope sensors are off
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					// Sets current Sensor Delay to Normal Delay
					mSensorDelaySwitch = mSensorDelayNorm;

					// Starts Sensor Delay statistics
					currentTime();

					// To be used in total time calculations to specify which
					// sensor delay was being used last and where to display the
					// information for how long it has been used
					mPreviousSelectedDelay = mSensorDelayNorm;

					// Checks if both toggle buttons are off, starts function
					// which unregisters both sensors
				} else if (toggleButtonCounterAccel == 0
						&& toggleButtonCounterGyro == 0) {

					setCheckedFalse();
				}
			}
		});

		uiDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					// Sets current Sensor Delay to UI Delay
					mSensorDelaySwitch = mSensorDelayUI;

					currentTime();

					mPreviousSelectedDelay = mSensorDelayUI;

				} else if (toggleButtonCounterAccel == 0
						&& toggleButtonCounterGyro == 0) {

					setCheckedFalse();
				}
			}
		});

		gameDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					// Sets current Sensor Delay to Game Delay
					mSensorDelaySwitch = mSensorDelayGame;

					currentTime();

					mPreviousSelectedDelay = mSensorDelayGame;

				} else if (toggleButtonCounterAccel == 0
						&& toggleButtonCounterGyro == 0) {

					setCheckedFalse();
				}
			}
		});

		fastDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					// Sets current sensor delay to Fastest Delay
					mSensorDelaySwitch = mSensorDelayFast;

					currentTime();

					mPreviousSelectedDelay = mSensorDelayFast;

				} else if (toggleButtonCounterAccel == 0
						&& toggleButtonCounterGyro == 0) {

					setCheckedFalse();
				}
			}
		});
	}

	/**
	 * Gets system time, calculates time since the last button click, and adds
	 * that to the current Sensor Delays' total time. Adds one to button click
	 * counter.
	 */
	public void currentTime() {

		// ///////////// Check if this is even necessary
		if (toggleButtonCounterAccel == 1 || toggleButtonCounterGyro == 1) {

			// Gets current system to calculate time since last click
			mEndTime = System.nanoTime();

			if (mSensorDelaySwitch == mSensorDelayNorm) {

				// Calculates time since last button click, sets to milliseconds
				// //////////////// Check if this and next function call need to
				// be within if function
				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;

				// Calculates total time spent in current Sensor Delay
				totalSensorTime();

				// Increases current sensors' click count by 1
				mClickCountNorm++;

				// Sets current click counter displayed to Normal
				// ///////////////////
				// Check if this is even necessary
				mClickCounter = mClickCountNorm;

			} else if (mSensorDelaySwitch == mSensorDelayUI) {

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				totalSensorTime();

				mClickCountUI++;
				mClickCounter = mClickCountUI;

			} else if (mSensorDelaySwitch == mSensorDelayGame) {

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				totalSensorTime();

				mClickCountGame++;
				mClickCounter = mClickCountGame;

			} else if (mSensorDelaySwitch == mSensorDelayFast) {

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				totalSensorTime();

				mClickCountFast++;
				mClickCounter = mClickCountFast;

			}

			// Sets current button click calculations as text on display and
			// changes the selected Sensor Delay button color as well as
			// highlights the name of the Sensor Delay being used on the
			// statistics table
			clickStatisticsText();

			// Gets current system time and sets it to both Start Time and End
			// Time ///////// Check if End Time is necessary
			setStartAndEndTime();

			// Registers sensors /////// Check if necessary
			register();

			// ///Check if necessary
		} else if (toggleButtonCounterAccel == 0
				&& toggleButtonCounterGyro == 0) {

			// Unregisters sensors
			setCheckedFalse();

			// Stops function
			return;

		}
	}

	/**
	 * Checks which Sensor Delay was previously used, and adds the current time
	 * since the last button click to the total time that the specified Sensor
	 * Delay has been on. Converts time to seconds.
	 */
	public void totalSensorTime() {

		if (mPreviousSelectedDelay == mSensorDelayNorm) {

			// Adds time since last click to Sensor Delay total time
			mTotalTimeNorm = mTotalTimeNorm + durationSinceLastClick;

			mTotalTime = mTotalTimeNorm / 1000;

		} else if (mPreviousSelectedDelay == mSensorDelayUI) {

			mTotalTimeUI = mTotalTimeUI + durationSinceLastClick;

			mTotalTime = mTotalTimeUI / 1000;

		} else if (mPreviousSelectedDelay == mSensorDelayGame) {

			mTotalTimeGame = mTotalTimeGame + durationSinceLastClick;

			mTotalTime = mTotalTimeGame / 1000;

		} else if (mPreviousSelectedDelay == mSensorDelayFast) {

			mTotalTimeFast = mTotalTimeFast + durationSinceLastClick;

			mTotalTime = mTotalTimeFast / 1000;

		}

	}

	/**
	 * Checks the current Delay selection, displays the time since the last
	 * Sensor Delay change, checks for and displays Sensor Delay total time to
	 * update, updates click count, and changes button and text box colors.
	 */
	public void clickStatisticsText() {

		if (mSensorDelaySwitch == mSensorDelayNorm) {

			// Sets time since last Sensor Delay change
			mNormLastClick.setText(String.valueOf(durationSinceLastClick));

			// ///////// Check if necessary
			mNormLastClick.setText("" + durationSinceLastClick);

			// Sets current total time to be displayed to the current Sensor
			// Delay chosen
			mTotal = mNormTotalTime;

			// Overrides the previous equation if the previous Sensor Delay
			// chosen is not the same as the current one
			// ///////////////////Check if this can be in total()
			previousMode();

			// Updates the total running-time value for the previously-activated
			// delay /////////// Check if this can be put after if - else if
			total();

			// Sets new amount (+1) to current Sensor Delay click count
			mNormClickCount.setText("" + mClickCounter);

			// Changes background color of current Sensor Delay Button and
			// TextView box //////// Check if can be put after if - else if
			delayColorChange();

		} else if (mSensorDelaySwitch == mSensorDelayUI) {

			mUiLastClick.setText(String.valueOf(durationSinceLastClick));

			mUiLastClick.setText("" + durationSinceLastClick);

			mTotal = mUiTotalTime;

			previousMode();

			total();

			mUiClickCount.setText("" + mClickCounter);

			delayColorChange();

		} else if (mSensorDelaySwitch == mSensorDelayGame) {

			mGameLastClick.setText(String.valueOf(durationSinceLastClick));

			mGameLastClick.setText("" + durationSinceLastClick);

			mTotal = mGameTotalTime;

			previousMode();

			total();

			mGameClickCount.setText("" + mClickCounter);

			delayColorChange();

		} else if (mSensorDelaySwitch == mSensorDelayFast) {

			mFastLastClick.setText(String.valueOf(durationSinceLastClick));

			mFastLastClick.setText("" + durationSinceLastClick);

			mTotal = mFastTotalTime;

			previousMode();

			total();

			mFastClickCount.setText("" + mClickCounter);

			delayColorChange();

		}

	}

	/**
	 * Gets system time and sets it to variables End Time and Start Time
	 */
	public void setStartAndEndTime() {

		// ///// Check if necessary
		mEndTime = System.nanoTime();

		mStartTime = System.nanoTime();

	}

	/**
	 * Unregisters both listeners, then registers whichever sensor is powered
	 * on.
	 */
	public void register() {

		// //// Check if necessary
		mSensorManager.unregisterListener(this);

		if (toggleGyro.isChecked()) {

			// Registers the Gyroscope sensor
			mSensorManager.registerListener(this, mSensorTypeGyro,
					mSensorDelaySwitch);

		}
		if (toggleAccel.isChecked()) {

			// Registers the Accelerometer sensor
			mSensorManager.registerListener(this, mSensorTypeAccel,
					mSensorDelaySwitch);

		}
	}

	/**
	 * Checks for the previously selected delay and sets the displayed total
	 * time TextView to its calculated total time. /////Check if necessary
	 */
	public void previousMode() {

		// ////// Check if necessary
		if (mPreviousSelectedDelay == mSensorDelaySwitch) {

			return;

		} else if (mPreviousSelectedDelay == mSensorDelayNorm) {

			mTotal = mNormTotalTime;

		} else if (mPreviousSelectedDelay == mSensorDelayUI) {

			mTotal = mUiTotalTime;

		} else if (mPreviousSelectedDelay == mSensorDelayGame) {

			mTotal = mGameTotalTime;

		} else if (mPreviousSelectedDelay == mSensorDelayFast) {

			mTotal = mFastTotalTime;

		}
	}

	/**
	 * Updates the total running-time value for the previously-activated delay.
	 */
	public void total() {

		// Read documentation to check if both are necessary
		mTotal.setText(String.valueOf(mTotalTime));

		mTotal.setText("" + mTotalTime);

	}

	/**
	 * Changes background color of current Sensor Delay Button and TextView box.
	 */
	public void delayColorChange() {
		if (mSensorDelaySwitch == mSensorDelayNorm) {

			mchronoNormText.setBackgroundColor(0x55000000);
			mchronoUiText.setBackgroundColor(0x55FFFFFF);
			mchronoGameText.setBackgroundColor(0x55FFFFFF);
			mchronoFastText.setBackgroundColor(0x55FFFFFF);

			normDelayButton.setBackgroundColor(Color.GREEN);
			uiDelayButton.setBackgroundColor(Color.LTGRAY);
			gameDelayButton.setBackgroundColor(Color.LTGRAY);
			fastDelayButton.setBackgroundColor(Color.LTGRAY);

		} else if (mSensorDelaySwitch == mSensorDelayUI) {

			mchronoNormText.setBackgroundColor(0x55FFFFFF);
			mchronoUiText.setBackgroundColor(0x55000000);
			mchronoGameText.setBackgroundColor(0x55FFFFFF);
			mchronoFastText.setBackgroundColor(0x55FFFFFF);

			normDelayButton.setBackgroundColor(Color.LTGRAY);
			uiDelayButton.setBackgroundColor(Color.GREEN);
			gameDelayButton.setBackgroundColor(Color.LTGRAY);
			fastDelayButton.setBackgroundColor(Color.LTGRAY);

		} else if (mSensorDelaySwitch == mSensorDelayGame) {

			mchronoNormText.setBackgroundColor(0x55FFFFFF);
			mchronoUiText.setBackgroundColor(0x55FFFFFF);
			mchronoGameText.setBackgroundColor(0x55000000);
			mchronoFastText.setBackgroundColor(0x55FFFFFF);

			normDelayButton.setBackgroundColor(Color.LTGRAY);
			uiDelayButton.setBackgroundColor(Color.LTGRAY);
			gameDelayButton.setBackgroundColor(Color.GREEN);
			fastDelayButton.setBackgroundColor(Color.LTGRAY);

		} else if (mSensorDelaySwitch == mSensorDelayFast) {

			mchronoNormText.setBackgroundColor(0x55FFFFFF);
			mchronoUiText.setBackgroundColor(0x55FFFFFF);
			mchronoGameText.setBackgroundColor(0x55FFFFFF);
			mchronoFastText.setBackgroundColor(0x55000000);

			normDelayButton.setBackgroundColor(Color.LTGRAY);
			uiDelayButton.setBackgroundColor(Color.LTGRAY);
			gameDelayButton.setBackgroundColor(Color.LTGRAY);
			fastDelayButton.setBackgroundColor(Color.GREEN);

		}

	}

	/**
	 * Displays Accelerometer and Gyroscope sensor readings for the X, Y, and Z
	 * axis.
	 * 
	 * @param event
	 *            Gathers sensor values for each axis.
	 */
	public void onSensorChanged(SensorEvent event) {
		Log.d(tag, "onSensorChanged: " + event.sensor + ", x: "
				+ event.values[0] + ", y: " + event.values[1] + ", z: "
				+ event.values[2]);

		float AxisX = event.values[0];
		float AxisY = event.values[1];
		float AxisZ = event.values[2];

		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			mXaxisGyro.setText("Gyro X: " + AxisX);
			mYaxisGyro.setText("Gyro Y: " + AxisY);
			mZaxisGyro.setText("Gyro Z: " + AxisZ);
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mXaxisAccel.setText("Accel X: " + AxisX);
			mYaxisAccel.setText("Accel Y: " + AxisY);
			mZaxisAccel.setText("Accel Z: " + AxisZ);
		}

	}

	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregisters both sensors when app is paused (home screen or screen
		// lock), modify or delete this to maintain sensors running despite app
		// pause. Currently only app activity functioning outside is chronometer
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(tag, "onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
	}

}
