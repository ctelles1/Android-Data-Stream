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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemClock;
import android.view.Menu;
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
	TextView mNormTime;
	TextView mGameTime;
	TextView mUITime;
	TextView mFastTime;
	TextView mAllTime;

	String mTextDelay;

	Sensor mAccel;
	Sensor mGyro;

	long duration;
	int dur = (int) duration;

	long startTime;
	long endTime;

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
		mSensorManager.registerListener(this, mAccel, mDelay);
		mSensorManager.registerListener(this, mGyro, mDelay);

		mxViewA = (TextView) findViewById(R.id.xbox);
		myViewA = (TextView) findViewById(R.id.ybox);
		mzViewA = (TextView) findViewById(R.id.zbox);
		mxViewG = (TextView) findViewById(R.id.xboxo);
		myViewG = (TextView) findViewById(R.id.yboxo);
		mzViewG = (TextView) findViewById(R.id.zboxo);

		mNormTime = (TextView) findViewById(R.id.chronoNormal);
		mGameTime = (TextView) findViewById(R.id.chronoGame);
		mUITime = (TextView) findViewById(R.id.chronoUI);
		mFastTime = (TextView) findViewById(R.id.chronoFast);

		normD = (Button) findViewById(R.id.ND);
		gameD = (Button) findViewById(R.id.GD);
		uiD = (Button) findViewById(R.id.UD);
		fastD = (Button) findViewById(R.id.FD);

		startTime = System.nanoTime();
		focus = (Chronometer) findViewById(R.id.chronometer1);
		focus.setBase(SystemClock.elapsedRealtime());
		focus.start();

		// GridView gridview = (GridView) findViewById(R.id.gridview);
		// gridview.setAdapter(new ImageAdapter(this));
		//
		// gridview.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View v, int position,
		// long id) {
		// Toast.makeText(HelloGridView.this, "" + position,
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		// create a separate start time and end time for each delay setting

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

	public void previousTime() {

		endTime = System.nanoTime();

		mode();// Calculates time since last button click

		if (prevTime == prevNTime) {

			mClickN++; // Increases click counter
			mClick = mClickN; // Sets displayed counter to current click counter

			mDelay = mNorm; // Sensor Delay Setting
			mAllTime = mNormTime; // TextView
			mTextDelay = "Normal"; // String for TextView

			mode = modeN;

			mNormTime.setText(String.valueOf(duration)); // Converts long to
															// string

		} else if (prevTime == prevUTime) {

			mClickU++;
			mClick = mClickU;

			mDelay = mUI;
			mAllTime = mUITime;
			mTextDelay = "UI";

			mode = modeU;

			mUITime.setText(String.valueOf(duration));

		} else if (prevTime == prevGTime) {

			mClickG++;
			mClick = mClickG;

			mDelay = mGame;
			mAllTime = mGameTime;
			mTextDelay = "Game";

			mode = modeG;

			mGameTime.setText(String.valueOf(duration));

		} else if (prevTime == prevFTime) {

			mClickF++;
			mClick = mClickF;

			mDelay = mFast;
			mAllTime = mFastTime;
			mTextDelay = "Fast";

			mode = modeF;

			mFastTime.setText(String.valueOf(duration));

		}

		delay(); // Starts function which registers listener and sets text

	}

	public void delay() {
		mSensorManager.unregisterListener(this);
		mSensorManager.registerListener(this, mAccel, mDelay);
		mSensorManager.registerListener(this, mGyro, mDelay);
		endTime = System.nanoTime();
		startTime = System.nanoTime();
		mAllTime.setText(mTextDelay + " in milli: " + duration
				+ " | total secs:" + mEndT + " | clicks: " + mClick);

	}

	public void mode() {

		duration = (endTime - startTime) / 1000000; // Gets last click time and
													// converts to milliseconds

		if (mode == modeN) {

			mEndN = mEndN + duration; // Adds time since last to total time of
										// button being clicked
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

	}

}