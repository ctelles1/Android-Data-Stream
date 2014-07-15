package com.example.gyroaccelapp;

import com.example.gyroaccelapp.R.layout;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
	TextView xViewA = null;
	TextView yViewA = null;
	TextView zViewA = null;
	TextView xViewG = null;
	TextView yViewG = null;
	TextView zViewG = null;
	Sensor mAccel;
	Sensor mGyro;
	int mNorm;
	int mGame;
	int mUI;
	int mFast;
	int mDelay;
	float axisX;
	float axisY;
	float axisZ;
	float norm;
	float game;
	float ui;
	long elapsedMillis;
	Chronometer fast;
	TextView normalTime;
	TextView nTime;
	TextView gTime;
	TextView uTime;
	TextView fTime;
	TextView allTime;
	String delay;
	Chronometer focus;
	Button normD, gameD, uiD, fastD;
	String fin;
	TextView mText;
	String time;
	String hour;
	long stoppedMilliseconds;
	TextView mon;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// final RelativeLayout relativeLayout = new RelativeLayout(this);

		mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		mNorm = SensorManager.SENSOR_DELAY_NORMAL;
		mGame = SensorManager.SENSOR_DELAY_GAME;
		mUI = SensorManager.SENSOR_DELAY_UI;
		mFast = SensorManager.SENSOR_DELAY_FASTEST;

		mSensorManager.registerListener(this, mAccel, mDelay);
		mSensorManager.registerListener(this, mGyro, mDelay);

		xViewA = (TextView) findViewById(R.id.xbox);
		yViewA = (TextView) findViewById(R.id.ybox);
		zViewA = (TextView) findViewById(R.id.zbox);
		xViewG = (TextView) findViewById(R.id.xboxo);
		yViewG = (TextView) findViewById(R.id.yboxo);
		zViewG = (TextView) findViewById(R.id.zboxo);

		nTime = (TextView) findViewById(R.id.chronoNormal);
		gTime = (TextView) findViewById(R.id.chronoGame);
		uTime = (TextView) findViewById(R.id.chronoUI);
		fTime = (TextView) findViewById(R.id.chronoFast);

		normD = (Button) findViewById(R.id.ND);
		gameD = (Button) findViewById(R.id.GD);
		uiD = (Button) findViewById(R.id.UD);
		fastD = (Button) findViewById(R.id.FD);

		focus = (Chronometer) findViewById(R.id.chronometer1);
		// long elapsedMillis = SystemClock.elapsedRealtime() - focus.getBase();
		// focus = new Chronometer(this);
		// relativeLayout.addView(focus);
		// setContentView(relativeLayout);
		//
		// int size = 1;
		// TextView[] tv = new TextView[size];
		// for (int n = 0; n < size; n++) {
		// nTime = new TextView(this);
		// relativeLayout.addView(nTime);
		// tv[n] = nTime;
		// }
		normD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDelay = mNorm;
				allTime = nTime;
				delay();
			}
		});

		gameD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDelay = mGame;
				allTime = gTime;
				delay();
			}
		});

		uiD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDelay = mUI;
				allTime = uTime;
				delay();
			}
		});

		fastD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDelay = mFast;
				allTime = fTime;
				delay();
			}
		});

	}

	public void onSensorChanged(SensorEvent event) {
		Log.d(tag, "onSensorChanged: " + event.sensor + ", x: "
				+ event.values[0] + ", y: " + event.values[1] + ", z: "
				+ event.values[2]);

		axisX = event.values[0];
		axisY = event.values[1];
		axisZ = event.values[2];

		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			xViewG.setText("Gyro X: " + axisX);
			yViewG.setText("Gyro Y: " + axisY);
			zViewG.setText("Gyro Z: " + axisZ);
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			xViewA.setText("Accel X: " + axisX);
			yViewA.setText("Accel Y: " + axisY);
			zViewA.setText("Accel Z: " + axisZ);
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

	public void delay() {
		mSensorManager.unregisterListener(this);
		mSensorManager.registerListener(this, mAccel, mDelay);
		mSensorManager.registerListener(this, mGyro, mDelay);
		focus.setBase(SystemClock.elapsedRealtime());
		focus.start();
		fast = focus;
		elapsed();
		allTime.setText(delay + " Delay Elapsed: " + fin);

		try {
			hour = String.valueOf(mText.getText());
			time = String.valueOf(focus.getBase());
			int h = java.lang.Integer.parseInt(hour) / 60;
			int t = java.lang.Integer.parseInt(time) * 1000 / 60;
			int mm = h * t;
			allTime.setText(Integer.toString(mm));
			fin = "" + mm;
		} catch (Exception e) {
			// display the exception here if so inclined
		} finally {
			Toast.makeText(this, "Elapsing " + delay + " Delay Time",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void elapsed() {
		if (allTime == nTime) {
			delay = "Normal";
		} else if (allTime == gTime) {
			delay = "Game";
		} else if (allTime == uTime) {
			delay = "UI";
		} else if (allTime == fTime) {
			delay = "Fast";
		}
	}

	// private void showElapsedTime() {
	// elapsedMillis = SystemClock.elapsedRealtime() - focus.getBase();
	// normalTime.makeText(
	// "Elapsed milliseconds: "
	// + Integer.toString((int) elapsedMillis)).show();
	// normalTime = ((TextView)nTime);
	// }

	// public void onClick(View v) {
	//
	// int stoppedMilliseconds = 0;
	//
	// String chronoText = focus.getText().toString();
	// String array[] = chronoText.split(":");
	// if (array.length == 2) {
	// stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
	// + Integer.parseInt(array[1]) * 1000;
	// } else if (array.length == 3) {
	// stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
	// + Integer.parseInt(array[1]) * 60 * 1000
	// + Integer.parseInt(array[2]) * 1000;
	// }
	//
	// focus.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
	// focus.start();
	// }
}
