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

	// Defines variables and sensors before the class they are used in

	final String tag = "GAP";

	SensorManager mSensorManager = null;

	TextView mxViewA = null;
	TextView myViewA = null;
	TextView mzViewA = null;
	TextView mxViewG = null;
	TextView myViewG = null;
	TextView mzViewG = null;

	TextView mNormLast;
	TextView mUiLast;
	TextView mGameLast;
	TextView mFastLast;
	TextView mNormTotal;
	TextView mUiTotal;
	TextView mGameTotal;
	TextView mFastTotal;
	TextView mNormCount;
	TextView mUiCount;
	TextView mGameCount;
	TextView mFastCount;

	String mTextDelay;

	Sensor mAccel;
	Sensor mGyro;

	long duration;
	int dur = (int) duration;

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
	long mEndN;
	long mEndG;
	long mEndU;
	long mEndF;

	int mNorm;
	int mGame;
	int mUI;
	int mFast;
	int mDelay;

	int prevNTime = 1;
	int prevUTime = 2;
	int prevGTime = 3;
	int prevFTime = 4;
	int prevTime;

	int modeN = 1;
	int modeU = 2;
	int modeG = 3;
	int modeF = 4;
	int mode;

	int prevModeN = 1;
	int prevModeU = 2;
	int prevModeG = 3;
	int prevModeF = 4;
	int prevMode;

	int mModeChange;

	int tC;

	int mClickN;
	int mClickU;
	int mClickG;
	int mClickF;
	int mClick;

	float mAxisX;
	float mAxisY;
	float mAxisZ;

	Chronometer focus;

	Button normD, gameD, uiD, fastD;
	ToggleButton toggle;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		mNorm = SensorManager.SENSOR_DELAY_NORMAL;
		mGame = SensorManager.SENSOR_DELAY_GAME;
		mUI = SensorManager.SENSOR_DELAY_UI;
		mFast = SensorManager.SENSOR_DELAY_FASTEST;

		mDelay = mNorm;

		mxViewA = (TextView) findViewById(R.id.xbox);
		myViewA = (TextView) findViewById(R.id.ybox);
		mzViewA = (TextView) findViewById(R.id.zbox);
		mxViewG = (TextView) findViewById(R.id.xboxo);
		myViewG = (TextView) findViewById(R.id.yboxo);
		mzViewG = (TextView) findViewById(R.id.zboxo);

		mNormLast = (TextView) findViewById(R.id.normLast);
		mGameLast = (TextView) findViewById(R.id.gameLast);
		mUiLast = (TextView) findViewById(R.id.uiLast);
		mFastLast = (TextView) findViewById(R.id.fastLast);

		mNormTotal = (TextView) findViewById(R.id.normTotal);
		mGameTotal = (TextView) findViewById(R.id.gameTotal);
		mUiTotal = (TextView) findViewById(R.id.uiTotal);
		mFastTotal = (TextView) findViewById(R.id.fastTotal);

		mNormCount = (TextView) findViewById(R.id.normCount);
		mGameCount = (TextView) findViewById(R.id.gameCount);
		mUiCount = (TextView) findViewById(R.id.uiCount);
		mFastCount = (TextView) findViewById(R.id.fastCount);

		normD = (Button) findViewById(R.id.ND);
		gameD = (Button) findViewById(R.id.GD);
		uiD = (Button) findViewById(R.id.UD);
		fastD = (Button) findViewById(R.id.FD);

		toggle = (ToggleButton) findViewById(R.id.powerSwitch);

		mNormStartTime = System.nanoTime();
		mGameStartTime = System.nanoTime();
		mUIStartTime = System.nanoTime();
		mFastStartTime = System.nanoTime();

		focus = (Chronometer) findViewById(R.id.chronometer1);
		focus.setBase(SystemClock.elapsedRealtime());
		focus.start();

		toggle.setChecked(false);

		click();

	}

	public void onSensorChanged(SensorEvent event) {
		Log.d(tag, "onSensorChanged: " + event.sensor + ", x: "
				+ event.values[0] + ", y: " + event.values[1] + ", z: "
				+ event.values[2]);

		mAxisX = event.values[0];
		mAxisY = event.values[1];
		mAxisZ = event.values[2];

		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			mxViewG.setText("Gyro X: " + mAxisX);
			myViewG.setText("Gyro Y: " + mAxisY);
			mzViewG.setText("Gyro Z: " + mAxisZ);
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mxViewA.setText("Accel X: " + mAxisX);
			myViewA.setText("Accel Y: " + mAxisY);
			mzViewA.setText("Accel Z: " + mAxisZ);
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

	public void click() {
		toggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (toggle.isChecked()) {

					onoff();

					tC = 1;

					mSensorManager.registerListener(MainActivity.this, mAccel,
							mDelay);

					mSensorManager.registerListener(MainActivity.this, mGyro,
							mDelay);

					normD.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							prevTime = prevNTime;
							previousTime();

						}
					});

					uiD.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							prevTime = prevUTime;
							previousTime();

						}
					});

					gameD.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							prevTime = prevGTime;
							previousTime();

						}
					});

					fastD.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							prevTime = prevFTime;
							previousTime();

						}
					});

				} else {
					mSensorManager.unregisterListener(MainActivity.this);

					toggle.setChecked(false);

					tC = 0;

				}
			}
		});

		if (tC == 1) { // Redundant listener unregister and toggle unchecking

			return;

		} else {

			mSensorManager.unregisterListener(MainActivity.this);
			toggle.setChecked(false);

		}
	}

	public void previousTime() {
		if (tC == 1) {

			mModeChange = 0;

			if (prevTime == prevNTime) {

				mNormEndTime = System.nanoTime();

				mEndTime = mNormEndTime;
				mStartTime = mNormStartTime;

				mode();// Calculates time since last button click

				mClickN++; // Increases click counter
				mClick = mClickN; // Sets displayed counter to current click
									// counter

				mDelay = mNorm; // Sensor Delay Setting
				mTextDelay = "Normal"; // String for TextView

				text();

				mode = modeN;

			} else if (prevTime == prevUTime) {

				mUIEndTime = System.nanoTime();

				mEndTime = mUIEndTime;
				mStartTime = mUIStartTime;

				mode();

				mClickU++;
				mClick = mClickU;

				mDelay = mUI;
				mTextDelay = "UI";

				text();

				mode = modeU;

			} else if (prevTime == prevGTime) {

				mGameEndTime = System.nanoTime();

				mEndTime = mGameEndTime;
				mStartTime = mGameStartTime;

				mode();

				mClickG++;
				mClick = mClickG;

				mDelay = mGame;
				mTextDelay = "Game";

				text();

				mode = modeG;

			} else if (prevTime == prevFTime) {

				mFastEndTime = System.nanoTime();

				mEndTime = mFastEndTime;
				mStartTime = mFastStartTime;

				mode();

				mClickF++;
				mClick = mClickF;

				mDelay = mFast;
				mTextDelay = "Fast";

				text();

				mode = modeF;
			}

			delay();

		} else {

			return;

		}
	}

	public void mode() {

		// if (mModeChange == 0) {

		duration = (mEndTime - mStartTime) / 1000000; // Gets last click
														// time and converts
														// to milliseconds

		if (mode == modeN) {

			mEndN = mEndN + duration; // Adds time since last to total time
										// of button being clicked

			mEndT = mEndN / 1000;// Converts milliseconds to seconds

		} else if (mode == modeU) {

			mEndU = mEndU + duration;
			mEndT = mEndU / 1000;

		} else if (mode == modeG) {

			mEndG = mEndG + duration;
			mEndT = mEndG / 1000;

		} else if (mode == modeF) {

			mEndF = mEndF + duration;
			mEndT = mEndF / 1000;

		}
		// } else if (mModeChange == 1) {
		//
		// duration = (mEndTime - mStartTime) / 1000000;
		//
		// if (prevMode == prevModeN) {
		//
		// mEndN = mEndN + duration;
		//
		// mEndT = mEndN / 1000;
		//
		// mModeChange = 1;
		//
		// } else if (prevMode == prevModeU) {
		//
		// mEndU = mEndU + duration;
		// mEndT = mEndU / 1000;
		//
		// mModeChange = 1;
		//
		// } else if (prevMode == prevModeG) {
		//
		// mEndG = mEndG + duration;
		// mEndT = mEndG / 1000;
		//
		// mModeChange = 1;
		//
		// } else if (prevMode == prevModeF) {
		//
		// mEndF = mEndF + duration;
		// mEndT = mEndF / 1000;
		//
		// mModeChange = 1;
		//
		// }
		//
		//
		// }
	}

	public void delay() {

		mSensorManager.unregisterListener(this);
		mSensorManager.registerListener(this, mAccel, mDelay);
		mSensorManager.registerListener(this, mGyro, mDelay);

		onoff();

	}

	public void text() {

		if (mode == modeN) {

			mNormLast.setText(String.valueOf(duration)); // Converts long to
															// string
			mNormTotal.setText(String.valueOf(mEndT));

			mNormLast.setText("" + duration);
			mNormTotal.setText("" + mEndT);
			mNormCount.setText("" + mClick);

		} else if (mode == modeU) {

			mUiLast.setText(String.valueOf(duration));
			mUiTotal.setText(String.valueOf(mEndT));

			mUiLast.setText("" + duration);
			mUiTotal.setText("" + mEndT);
			mUiCount.setText("" + mClick);

		} else if (mode == modeG) {

			mGameLast.setText(String.valueOf(duration));
			mGameTotal.setText(String.valueOf(mEndT));

			mGameLast.setText("" + duration);
			mGameTotal.setText("" + mEndT);
			mGameCount.setText("" + mClick);

		} else if (mode == modeF) {

			mFastLast.setText(String.valueOf(duration));
			mFastTotal.setText(String.valueOf(mEndT));

			mFastLast.setText("" + duration);
			mFastTotal.setText("" + mEndT);
			mFastCount.setText("" + mClick);

		}

		click();

	}

	public void onoff() {

		mNormEndTime = System.nanoTime();
		mGameEndTime = System.nanoTime();
		mUIEndTime = System.nanoTime();
		mFastEndTime = System.nanoTime();

		mNormStartTime = System.nanoTime();
		mGameStartTime = System.nanoTime();
		mUIStartTime = System.nanoTime();
		mFastStartTime = System.nanoTime();

	}

}