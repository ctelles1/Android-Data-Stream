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

	TextView mNormLastClick, mUiLastClick, mGameLastClick, mFastLastClick;
	TextView mNormTotalTime, mUiTotalTime, mGameTotalTime, mFastTotalTime;
	TextView mNormClickCount, mUiClickCount, mGameClickCount, mFastClickCount;
	TextView mchronoNormalText, mchronoGameText, mchronoUiText,
			mchronoFastText;

	TextView mSelection;

	String mDelaySelectionTextView;

	Sensor mSensorTypeAccel, mSensorTypeGyro, mSensorType;

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

	long mTotalTime;
	long mTotalTimeNorm;
	long mEndTimeGame;
	long mEndTimeUI;
	long mEndTimeFast;

	int mSensorDelayNormal;
	int mSensorDelayGame;
	int mSensorDelayUI;
	int mSensorDelayFastest;
	int mSensorDelaySwitch;

	int currentDelaySelection;

	int delayModeNorm = 1;
	int delayModeUI = 2;
	int delayModeGame = 3;
	int delayModeFast = 4;
	int delayMode;

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

		mSelection = (TextView) findViewById(R.id.selection);

		mchronoNormalText = (TextView) findViewById(R.id.chronoNormal);
		mchronoUiText = (TextView) findViewById(R.id.chronoUI);
		mchronoGameText = (TextView) findViewById(R.id.chronoGame);
		mchronoFastText = (TextView) findViewById(R.id.chronoFast);

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
				togglebutton();
			}
		});
		toggleAccel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				togglebutton();
			}
		});

	}

	public void togglebutton() {

		if (toggleGyro.isChecked()) {

			mSensorType = mSensorTypeGyro;
			mainButtonFunction();

		}
		if (toggleAccel.isChecked()) {

			mSensorType = mSensorTypeAccel;
			mainButtonFunction();

		} else {

			mSensorManager.unregisterListener(MainActivity.this);

			toggleGyro.setChecked(false);

			toggleButtonCounter = 0;

		}
	}

	public void mainButtonFunction() {
		setStartAndEndTime();

		toggleButtonCounter = 1;

		mSensorManager.registerListener(MainActivity.this, mSensorType,
				mSensorDelaySwitch);

		mSensorManager.registerListener(MainActivity.this, mSensorType,
				mSensorDelaySwitch);

		normDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				currentDelaySelection = mSensorDelayNormal;
				currentTime();
				previousSelectedDelay = mSensorDelayNormal;

			}
		});

		uiDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				currentDelaySelection = mSensorDelayUI;
				currentTime();
				previousSelectedDelay = mSensorDelayUI;

			}
		});

		gameDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				currentDelaySelection = mSensorDelayGame;
				currentTime();
				previousSelectedDelay = mSensorDelayGame;

			}
		});

		fastDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				currentDelaySelection = mSensorDelayFastest;
				currentTime();
				previousSelectedDelay = mSensorDelayFastest;

			}
		});
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
		if (toggleButtonCounter == 1) {

			if (currentDelaySelection == mSensorDelayNormal) {

				mNormEndTime = System.nanoTime(); // put this as EndTime earlier

				mEndTime = mNormEndTime;
				mStartTime = mNormStartTime;
				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();// Calculates time since last button click

				mClickCounterNorm++;
				mClickCounter = mClickCounterNorm;

				mSensorDelaySwitch = mSensorDelayNormal;
				mDelaySelectionTextView = "Normal";

			} else if (currentDelaySelection == mSensorDelayUI) {

				mUIEndTime = System.nanoTime();

				mEndTime = mUIEndTime;
				mStartTime = mUIStartTime;
				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();

				mClickCounterUI++;
				mClickCounter = mClickCounterUI;

				mSensorDelaySwitch = mSensorDelayUI;
				mDelaySelectionTextView = "UI";

			} else if (currentDelaySelection == mSensorDelayGame) {

				mGameEndTime = System.nanoTime();

				mEndTime = mGameEndTime;
				mStartTime = mGameStartTime;
				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();

				mClickG++;
				mClickCounter = mClickG;

				mSensorDelaySwitch = mSensorDelayGame;
				mDelaySelectionTextView = "Game";

			} else if (currentDelaySelection == mSensorDelayFastest) {

				mFastEndTime = System.nanoTime();

				mEndTime = mFastEndTime;
				mStartTime = mFastStartTime;
				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();

				mClickF++;
				mClickCounter = mClickF;

				mSensorDelaySwitch = mSensorDelayFastest;
				mDelaySelectionTextView = "Fast";

			}

			clickStatisticsText(); // Sets text on display

			listenerOffOnSwitch(); // Unregisters current Delay setting
									// listeners and Registers them in new Delay
									// settings

		} else {

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

		if (delayMode == delayModeNorm) {

			mTotalTimeNorm = mTotalTimeNorm + durationSinceLastClick;

			mTotalTime = mTotalTimeNorm / 1000;

		} else if (delayMode == delayModeUI) {

			mEndTimeUI = mEndTimeUI + durationSinceLastClick;
			mTotalTime = mEndTimeUI / 1000;

		} else if (delayMode == delayModeGame) {

			mEndTimeGame = mEndTimeGame + durationSinceLastClick;
			mTotalTime = mEndTimeGame / 1000;

		} else if (delayMode == delayModeFast) {

			mEndTimeFast = mEndTimeFast + durationSinceLastClick;
			mTotalTime = mEndTimeFast / 1000;

		}

	}

	/**
	 * Called in currentTime() after if statements of button clicks are done.
	 * Unregisters all listeners, then registers them again in the newly chosen
	 * delay settings.
	 */
	/*
	 * @@@@ IF STATEMENTS | LISTENERS | SWITCHES @@@@
	 * 
	 * unregisterListener - unregisters any sensors currently on
	 * 
	 * registerListener - registers a listenerfor Sensor Delay Types
	 * (mSensorType[Accel/Gyro], mSensorDelaySwitch(Norm/UI/Game/Fast))
	 * 
	 * @@@@ FUNCTIONS CALLED @@@@
	 * 
	 * setStartAndEndTime
	 */
	public void listenerOffOnSwitch() {

		mSensorManager.unregisterListener(this);

		if (toggleGyro.isChecked()) {

			mSensorManager.registerListener(this, mSensorTypeGyro,
					mSensorDelaySwitch);

		}
		if (toggleAccel.isChecked()) {

			mSensorManager.registerListener(this, mSensorTypeAccel,
					mSensorDelaySwitch);

		}

		setStartAndEndTime(); // Gets values for start and end time calculations

	}

	/**
	 * Called in currentTime() after all calculations are made and TextViews are
	 * changed. After checking for the current Delay selection, converts long
	 * durationSinceLastClick to string and displays it, calls previousMode(),
	 * displays click counter, calls delayColorChange(), and sets delayMode to
	 * specified delay for use in timeSinceLastClick().
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

		if (currentDelaySelection == mSensorDelayNormal) {

			mNormLastClick.setText(String.valueOf(durationSinceLastClick));

			mNormLastClick.setText("" + durationSinceLastClick);

			previousMode();

			mNormClickCount.setText("" + mClickCounter);

			delayColorChange();

			delayMode = delayModeNorm;

		} else if (currentDelaySelection == mSensorDelayUI) {

			mUiLastClick.setText(String.valueOf(durationSinceLastClick));

			mUiLastClick.setText("" + durationSinceLastClick);

			previousMode();

			mUiClickCount.setText("" + mClickCounter);

			delayColorChange();

			delayMode = delayModeUI;

		} else if (currentDelaySelection == mSensorDelayGame) {

			mGameLastClick.setText(String.valueOf(durationSinceLastClick));

			mGameLastClick.setText("" + durationSinceLastClick);

			previousMode();

			mGameClickCount.setText("" + mClickCounter);

			delayColorChange();

			delayMode = delayModeGame;

		} else if (currentDelaySelection == mSensorDelayFastest) {

			mFastLastClick.setText(String.valueOf(durationSinceLastClick));

			mFastLastClick.setText("" + durationSinceLastClick);

			previousMode();

			mFastClickCount.setText("" + mClickCounter);

			delayColorChange();

			delayMode = delayModeFast;

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

		mNormEndTime = System.nanoTime();
		mGameEndTime = System.nanoTime();
		mUIEndTime = System.nanoTime();
		mFastEndTime = System.nanoTime();

		mNormStartTime = System.nanoTime();
		mGameStartTime = System.nanoTime();
		mUIStartTime = System.nanoTime();
		mFastStartTime = System.nanoTime();

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
		if (previousSelectedDelay == mSensorDelayNormal) {

			mNormTotalTime.setText(String.valueOf(mTotalTime));

			mNormTotalTime.setText("" + mTotalTime);

		} else if (previousSelectedDelay == mSensorDelayUI) {

			mUiTotalTime.setText(String.valueOf(mTotalTime));

			mUiTotalTime.setText("" + mTotalTime);

		} else if (previousSelectedDelay == mSensorDelayGame) {

			mGameTotalTime.setText(String.valueOf(mTotalTime));

			mGameTotalTime.setText("" + mTotalTime);

		} else if (previousSelectedDelay == mSensorDelayFastest) {

			mFastTotalTime.setText(String.valueOf(mTotalTime));

			mFastTotalTime.setText("" + mTotalTime);

		}

		mSelection.setText("Current Delay Selection: "
				+ mDelaySelectionTextView);

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
		if (currentDelaySelection == mSensorDelayNormal) {

			mchronoNormalText.setBackgroundColor(0x55000000);
			mchronoUiText.setBackgroundColor(0x55FFFFFF);
			mchronoGameText.setBackgroundColor(0x55FFFFFF);
			mchronoFastText.setBackgroundColor(0x55FFFFFF);

		} else if (currentDelaySelection == mSensorDelayUI) {

			mchronoNormalText.setBackgroundColor(0x55FFFFFF);
			mchronoUiText.setBackgroundColor(0x55000000);
			mchronoGameText.setBackgroundColor(0x55FFFFFF);
			mchronoFastText.setBackgroundColor(0x55FFFFFF);

		} else if (currentDelaySelection == mSensorDelayGame) {

			mchronoNormalText.setBackgroundColor(0x55FFFFFF);
			mchronoUiText.setBackgroundColor(0x55FFFFFF);
			mchronoGameText.setBackgroundColor(0x55000000);
			mchronoFastText.setBackgroundColor(0x55FFFFFF);

		} else if (currentDelaySelection == mSensorDelayFastest) {

			mchronoNormalText.setBackgroundColor(0x55FFFFFF);
			mchronoUiText.setBackgroundColor(0x55FFFFFF);
			mchronoGameText.setBackgroundColor(0x55FFFFFF);
			mchronoFastText.setBackgroundColor(0x55000000);

		}

	}
}
