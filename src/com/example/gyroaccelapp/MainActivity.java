package com.example.gyroaccelapp;

import android.app.Activity;
import android.graphics.Color;
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

	// int mPreviousSelectedDelayNormal = 5;
	// int mPreviousSelectedDelayUI = 6;
	// int mPreviousSelectedDelayGame = 7;
	// int mPreviousSelectedDelayFastest = 8;
	int mPreviousSelectedDelay = -1;

	int toggleButtonCounter = 0;
	int toggleButtonCounterAccel = 0;
	int toggleButtonCounterGyro = 0;

	int mClickCountNorm, mClickCountUI, mClickCountGame, mClickCountFast;
	int mClickCounter;

	Chronometer chronometerClock;

	Button normDelayButton, gameDelayButton, uiDelayButton, fastDelayButton;
	Button DelayButton;
	ToggleButton toggleGyro, toggleAccel;

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

		// Validate whether an accelerometer or gyroscope is present or not
		mSensorTypeAccel = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorTypeGyro = mSensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

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

		normDelayButton = (Button) findViewById(R.id.ND);
		gameDelayButton = (Button) findViewById(R.id.GD);
		uiDelayButton = (Button) findViewById(R.id.UD);
		fastDelayButton = (Button) findViewById(R.id.FD);

		normDelayButton.setBackgroundColor(Color.LTGRAY);
		uiDelayButton.setBackgroundColor(Color.LTGRAY);
		gameDelayButton.setBackgroundColor(Color.LTGRAY);
		fastDelayButton.setBackgroundColor(Color.LTGRAY);

		chronometerClock = (Chronometer) findViewById(R.id.chronometer1);
		chronometerClock.setBase(SystemClock.elapsedRealtime());
		chronometerClock.start();

		toggleGyro = (ToggleButton) findViewById(R.id.powerGyro);
		toggleAccel = (ToggleButton) findViewById(R.id.powerAccel);

		toggleGyro.setChecked(false);
		toggleAccel.setChecked(false);

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
		 * @@@@ METHODS & FUNCTIONS CALLED @@@@
		 * 
		 * currentTime
		 * 
		 * setStartAndEndTime
		 * 
		 * listenerOffSwitch
		 * 
		 * @@@@@ VARIABLES @@@@@
		 * 
		 * toggleButtonCounter - On/Off Toggle Button redundancy check
		 * 
		 * currentDelaySelection - variable to make sure the methods currentTime
		 * and clickStatisticsText are updating the values for the right Delay
		 * Setting
		 * 
		 * 
		 * previousSelectedDelay - variable to make sure the method previousMode
		 * is updating the values for the right Delay Setting
		 */
		toggleGyro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleGyro.isChecked()) {

					mSensorType = mSensorTypeGyro;

					toggleButtonCounterGyro = 1;

					mButtonOnClickListener();

				} else {

					mSensorManager.unregisterListener(MainActivity.this,
							mSensorTypeGyro);

					toggleButtonCounterGyro = 0;

					toggleGyro.setChecked(false);

				}
			}
		});
		toggleAccel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleAccel.isChecked()) {

					mSensorType = mSensorTypeAccel;

					toggleButtonCounterAccel = 1;

					mButtonOnClickListener();

				} else {

					mSensorManager.unregisterListener(MainActivity.this,
							mSensorTypeAccel);

					toggleButtonCounterAccel = 0;

					toggleAccel.setChecked(false);

				}
			}
		});

	}

	public void setCheckedFalse() {

		toggleAccel.setChecked(false);

		toggleGyro.setChecked(false);

		toggleButtonCounter = 0;

		mSensorManager.unregisterListener(MainActivity.this, mSensorTypeAccel);

		mSensorManager.unregisterListener(MainActivity.this, mSensorTypeGyro);
	}

	public void mButtonOnClickListener() {

		setStartAndEndTime(); // Gets values for start and end time calculations

		register();

		normDelayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					mSensorDelaySwitch = mSensorDelayNorm;

					currentTime();

					mPreviousSelectedDelay = mSensorDelayNorm;

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
	 * Called on button click from onCreate method. Button Click listeners for
	 * On/Off Switch and Sensor Delay choice. Starts keeping time and registers
	 * listeners for Accelerometer and Gyroscope sensors after button click.
	 * Leads to function which starts the Click Statistics calculation and
	 * display loop.
	 */
	/*
	 * @@@@ IF STATEMENTS | LISTENERS | SWITCHES @@@@
	 * 
	 * toggleButtonCounter - On/Off button redundancy check
	 * 
	 * currentDelaySelection - chooses Sensor Delay data instruction loop based
	 * on Sensor Delay choice in onCreate onClickListener.
	 * 
	 * System.nanoTime - gets current system time up to nanoseconds
	 * 
	 * @@@@ METHODS & FUNCTIONS CALLED @@@@
	 * 
	 * timeSincLastClick
	 * 
	 * clickStatisticsText
	 * 
	 * @@@@@ VARIABLES @@@@@
	 * 
	 * m(Norm/Game/UI/Fast)EndTime - sets variable to current system time
	 * 
	 * mEndTime - sets "End Time" for calculation of time difference between
	 * clicks
	 * 
	 * mStartTime - sets "Start Time" for calculation of time difference between
	 * clicks
	 * 
	 * mClickCounter(Norm/Game/UI/Fast) - Increases counter by one every time
	 * button is clicked
	 * 
	 * mClickCounter - sets number to be displayed on screen to the currently
	 * handled click counter
	 * 
	 * mSensorDelaySwitch - sets current Sensor Delay to chosen setting
	 * 
	 * mDelaySelectionTextView - sets Current Delay text for display
	 */
	public void currentTime() {
		if (toggleButtonCounterAccel == 1 || toggleButtonCounterGyro == 1) {

			if (mSensorDelaySwitch == mSensorDelayNorm) {

				mEndTime = System.nanoTime(); // put this as EndTime earlier

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();// Calculates time since last button click

				mClickCountNorm++;
				mClickCounter = mClickCountNorm;

			} else if (mSensorDelaySwitch == mSensorDelayUI) {

				mEndTime = System.nanoTime();

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();

				mClickCountUI++;
				mClickCounter = mClickCountUI;

			} else if (mSensorDelaySwitch == mSensorDelayGame) {

				mEndTime = System.nanoTime();

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();

				mClickCountGame++;
				mClickCounter = mClickCountGame;

			} else if (mSensorDelaySwitch == mSensorDelayFast) {

				mEndTime = System.nanoTime();

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();

				mClickCountFast++;
				mClickCounter = mClickCountFast;

			}

			clickStatisticsText(); // Sets text on display

			setStartAndEndTime(); // Unregisters current Delay setting
									// listeners and Registers them in new Delay
									// settings

			register();

		} else if (toggleButtonCounterAccel == 0
				&& toggleButtonCounterGyro == 0) {

			setCheckedFalse();

			return;

		}
	}

	/**
	 * Called in currentTime(), after Sensor Delay choice and "End Time" is
	 * gathered. Calculates time since last button click in milliseconds, adds
	 * current time since last click to total Sensor Delay usage time, and
	 * converts current time of usage to seconds.
	 */
	/*
	 * @@@@ IF STATEMENTS | LISTENERS | SWITCHES @@@@
	 * 
	 * delayMode - Sensor Delay Switch set in currentTime()
	 * 
	 * @@@@@ VARIABLES @@@@@
	 * 
	 * durationSinceLastClick - time between last mStartTime and last mEndTime
	 * register
	 * 
	 * mEndTime - time gathered at button click
	 * 
	 * mStartTime - time gathered at end of Sensor Delay listener registration
	 * function
	 * 
	 * mEndTime(Norm/UI/Game/Fast) - sets "End Time" to currently chosen Delay
	 * total time elapsed plus time since last button click
	 * 
	 * mEndT - converts current "End Time" from milliseconds to seconds for
	 * display
	 */
	public void timeSinceLastClick() {

		if (mPreviousSelectedDelay == mSensorDelayNorm) {

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
	 * /** Called in currentTime() after all calculations are made and TextViews
	 * are changed. After checking for the current Delay selection, converts
	 * long durationSinceLastClick to string and displays it, calls
	 * previousMode(), displays click counter, calls delayColorChange(), and
	 * sets delayMode to specified delay for use in timeSinceLastClick().
	 */
	/*
	 * @@@@ IF STATEMENTS | LISTENERS | SWITCHES @@@@
	 * 
	 * currentDelaySelection - checks for current button selected as set in
	 * onClick
	 * 
	 * @@@@ METHODS & FUNCTIONS CALLED @@@@
	 * 
	 * previousMode()
	 * 
	 * delayColorChange()
	 * 
	 * .setText - sets the text inside the parentheses to the displayed id
	 * 
	 * @@@@@ VARIABLES @@@@@
	 * 
	 * m(Norm/UI/Game/Fast)LastClick - TextView displaying current time since
	 * last click
	 * 
	 * durationSinceLastClick - time since last click
	 * 
	 * m(Norm/UI/Game/Fast)ClickCount - TextView displaying click counter
	 * 
	 * mClickCounter - total number of clicks for specified sensor delay
	 * 
	 * delayMode - sets post-calculation and text display sensor delay mode for
	 * use in timeSinceLastClick()
	 * 
	 * delayMode(Norm/UI/Game/Fast) - sets delayMode to specified sensor delay
	 */
	public void clickStatisticsText() {

		if (mSensorDelaySwitch == mSensorDelayNorm) {

			mNormLastClick.setText(String.valueOf(durationSinceLastClick));

			mNormLastClick.setText("" + durationSinceLastClick);

			mTotal = mNormTotalTime;

			previousMode();

			total();

			mNormClickCount.setText("" + mClickCounter);

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
	 * Called in onCreate and listenerOffOnSwitch. Sets "End Time" and
	 * "Start Time" for each sensor delay so as to be used for next button
	 * click's "duration" (endTime - startTime) calculation.
	 */
	/*
	 * @@@@ METHODS & FUNCTIONS CALLED @@@@
	 * 
	 * System.nanoTime() - gets current system time and sets it to the variable
	 * 
	 * @@@@@ VARIABLES @@@@@
	 * 
	 * m(Norm/UI/Game/Fast)EndTime - sets current system time for EndTime
	 * variables
	 * 
	 * m(Norm/UI/Game/Fast)StartTime - sets current system time for StartTime
	 * variables
	 */
	public void setStartAndEndTime() {

		mEndTime = System.nanoTime();

		mStartTime = System.nanoTime();

	}
 
	public void register() {
		mSensorManager.unregisterListener(this);

		if (toggleGyro.isChecked()) {

			mSensorManager.registerListener(this, mSensorTypeGyro,
					mSensorDelaySwitch);

		}
		if (toggleAccel.isChecked()) {

			mSensorManager.registerListener(this, mSensorTypeAccel,
					mSensorDelaySwitch);

		}
	}

	/**
	 * Called in clickStatisticsText. Checks for the previously selected delay
	 * setting so as to then set the current time elapsed since the last click
	 * to the total amount of time elapsed under the specific previous setting.
	 * It does this by converting the long mEndT to string and using .setText to
	 * display it on variable m(Norm/UI/Game/Fast)TotalTime, and then sets the
	 * TextView for the current selected Delay setting.
	 */
	/*
	 * @@@@ IF STATEMENTS | LISTENERS | SWITCHES @@@@
	 * 
	 * previousSelectedDelay - checks for delay setting used previous to current
	 * one
	 * 
	 * @@@@ METHODS & FUNCTIONS CALLED @@@@
	 * 
	 * .setText
	 * 
	 * @@@@@ VARIABLES @@@@@
	 * 
	 * previousSelectedDelay - variable to make sure the method previousMode is
	 * updating the values for the right Delay Setting
	 * 
	 * previously(Normal/UI/Game/Fast)Selected - variable set in onClick after
	 * all changes to display due to button click have been made
	 * 
	 * m(Norm/UI/Game/Fast)TotalTime - TextView displaying total time elapsed in
	 * specified delay setting
	 * 
	 * mEndTime - current delay settings' total time elapsed
	 * 
	 * mSelection - TextView displaying name of current delay setting being used
	 * 
	 * mDelaySelectionTextView - current sensor delay setting text set in
	 * currentTime()
	 */
	public void previousMode() {

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

	public void total() {

		mTotal.setText(String.valueOf(mTotalTime));

		mTotal.setText("" + mTotalTime);

	}

	/**
	 * Called in clickStatisticsText. Sets background of TextView box for sensor
	 * delay setting name in display to dark gray, so as to signalize its
	 * current use. It does this by chekcing for the current delay selection and
	 * setting its TextView box to grey and the others to white, which clears
	 * them.
	 */
	/*
	 * @@@@ IF STATEMENTS | LISTENERS | SWITCHES @@@@
	 * 
	 * currentDelaySelection - checks for current sensor delay setting
	 * 
	 * @@@@ METHODS & FUNCTIONS CALLED @@@@
	 * 
	 * .setBackgroundColor - sets specified TextView box's color to (0xColor)
	 * 
	 * @@@@@ VARIABLES @@@@@
	 * 
	 * currentDelaySelection/currently(Normal/UI/Game/Fast)Selected - checking
	 * for current sensor delay setting
	 * 
	 * mchrono(Normal/UI/Game/Fast)Text - calls specified TextView box to change
	 * its background color and indicate its use
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
	 * Called when the sensors pick up new readings. Changes values of X, Y, and
	 * Z variables on Sensor Events on either Gyroscope or Accelerometer.
	 */
	/*
	 * @@@@ IF STATEMENTS | LISTENERS | SWITCHES @@@@
	 * 
	 * event.sensor.getType for Gyroscope and Accelerometer
	 * 
	 * @@@@@ VARIABLES @@@@@
	 * 
	 * mAxisX/Y/Z - event.values determined by Event Sensor readings, and set on
	 * UI by If Statements of event.sensor
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
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(tag, "onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
	}

}
