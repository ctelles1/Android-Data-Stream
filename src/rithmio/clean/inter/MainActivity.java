package rithmio.clean.inter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements SensorEventListener {

	// Defines variables and sensors

	// Functionality

	final String tag = "GAP";
	SensorManager mSensorManager = null;
	Sensor mAccelerometerSensor, mGyroscopeSensor;

	int mSensorDelayNormal, mSensorDelayGame, mSensorDelayUI,
			mSensorDelayFastest;
	int mCurrentSensorDelayGyroscope;
	int mCurrentSensorDelayAccelerometer;

	public boolean accelerometerIsOn;
	public boolean gyroscopeIsOn;

	boolean mXaxisAccelerometer, mYaxisAccelerometer, mZaxisAccelerometer,
			mXaxisGyroscope, mYaxisGyroscope, mZaxisGyroscope;

	// GUI

	TextView mXaxisAccel = null;
	TextView mYaxisAccel = null;
	TextView mZaxisAccel = null;
	TextView mXaxisGyro = null;
	TextView mYaxisGyro = null;
	TextView mZaxisGyro = null;
	Button normalDelayButtonGyroscope, gameDelayButtonGyroscope,
			uiDelayButtonGyroscope, fastestDelayButtonGyroscope;
	Button normalDelayButtonAccelerometer, gameDelayButtonAccelerometer,
			uiDelayButtonAccelerometer, fastestDelayButtonAccelerometer;
	Button toSupervisedLearning, toUnsupervisedLearning;
	ToggleButton toggleButtonGyroscope, toggleButtonAccelerometer;

	// Creates handler for Supervised and Unsupervised button clicks for color
	// to change for only 100 milliseconds
	final Handler handler = new Handler();
	final long delay = 100;

	// Information for logo spin animation
	private static final float ROTATE_FROM = 0.0f;
	private static final float ROTATE_TO = -1.0f * 360.0f;

	TextView currentAccuracyAccelerometer;
	TextView currentAccuracyGyroscope;
	String curAccA;
	String curAccG;

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
		// taken out if the sensor check is kept on the Manifest.
		mAccelerometerSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyroscopeSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		// Creates a toast verifying that the device contains the required
		// sensors.
		sensorCheckToaster();

		// Sets initial Sensor Delay when the first power button is clicked to
		// Normal Delay (longest delay). Otherwise, the default delay would be
		// Fastest
		mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_NORMAL;
		mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_NORMAL;

		// Declares the TextViews and Buttons being used
		textViewAndButtonDeclaration();

		// Sets and starts the animation for the logo spin
		logoAnimation();

	}

	public void sensorCheckToaster() {
		// Creates toast to indicate to user whether their phone has the
		// required sensors
		if (mAccelerometerSensor != null && mGyroscopeSensor != null) {
			Toast.makeText(this, "Required Sensors Found", Toast.LENGTH_SHORT)
					.show();

			// accelerometerIsOn = true;
			//
			// gyroscopeIsOn = true;

		} else if (mAccelerometerSensor == null && mGyroscopeSensor != null) {
			Toast.makeText(this, "No Accelerometer Found", Toast.LENGTH_SHORT)
					.show();

			// accelerometerIsOn = false;
			//
			// gyroscopeIsOn = true;

		} else if (mGyroscopeSensor == null && mAccelerometerSensor != null) {
			Toast.makeText(this, "No Gyroscope Found", Toast.LENGTH_SHORT)
					.show();

			// accelerometerIsOn = true;
			//
			// gyroscopeIsOn = false;

		} else if (mGyroscopeSensor == null && mAccelerometerSensor == null) {
			Toast.makeText(this, "No Sensors Found", Toast.LENGTH_SHORT).show();

			// accelerometerIsOn = false;
			//
			// gyroscopeIsOn = false;

		}
	}

	public void sensorRegisterTest(View v) {
		switch (v.getId()) {
		case R.id.button_NormalDelayAccelerometer:
			/** Start a new Activity MyCards.java */
			// function(sensor, rate);
			mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_NORMAL;
			registerSensors();
			// Changes button background color
			delayButtonColorChangeAccelerometer();
			break;
		case R.id.button_UiDelayAccelerometer:
			mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_UI;
			registerSensors();
			delayButtonColorChangeAccelerometer();
			break;
		case R.id.button_GameDelayAccelerometer:
			mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_GAME;
			registerSensors();
			delayButtonColorChangeAccelerometer();
			break;
		case R.id.button_FastestDelayAccelerometer:
			mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_FASTEST;
			registerSensors();
			delayButtonColorChangeAccelerometer();
			break;
		case R.id.button_NormalDelayGyroscope:
			mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_NORMAL;
			registerSensors();
			delayButtonColorChangeGyroscope();
			break;
		case R.id.button_UiDelayGyroscope:
			mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_UI;
			registerSensors();
			delayButtonColorChangeGyroscope();
			break;
		case R.id.button_GameDelayGyroscope:
			mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_GAME;
			registerSensors();
			delayButtonColorChangeGyroscope();
			break;
		case R.id.button_FastestDelayGyroscope:
			mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_FASTEST;
			registerSensors();
			delayButtonColorChangeGyroscope();
			break;
		}
	}

	public void checkBoxAxis(View v) {
		switch (v.getId()) {
		case R.id.checkBoxXaxisAccelerometer:
			mXaxisAccelerometer = !mXaxisAccelerometer;
			break;
		case R.id.checkBoxYaxisAccelerometer:
			mYaxisAccelerometer = !mYaxisAccelerometer;
			break;
		case R.id.checkBoxZaxisAccelerometer:
			mZaxisAccelerometer = !mZaxisAccelerometer;
			break;
		case R.id.checkBoxXaxisGyroscope:
			mXaxisGyroscope = !mXaxisGyroscope;
			break;
		case R.id.checkBoxYaxisGyroscope:
			mYaxisGyroscope = !mYaxisGyroscope;
			break;
		case R.id.checkBoxZaxisGyroscope:
			mZaxisGyroscope = !mZaxisGyroscope;
			break;
		}
	}

	/**
	 * Changes background color of current Sensor Delay Button. Uses
	 * Rithmio-specified green.
	 */
	public void delayButtonColorChangeGyroscope() {
		if (mCurrentSensorDelayGyroscope == SensorManager.SENSOR_DELAY_NORMAL) {

			normalDelayButtonGyroscope.setBackgroundColor(getResources()
					.getColor(R.color.RithmioYellow));
			uiDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
			gameDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
			fastestDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);

		} else if (mCurrentSensorDelayGyroscope == SensorManager.SENSOR_DELAY_UI) {

			normalDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
			uiDelayButtonGyroscope.setBackgroundColor(getResources().getColor(
					R.color.RithmioYellow));
			gameDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
			fastestDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);

		} else if (mCurrentSensorDelayGyroscope == SensorManager.SENSOR_DELAY_GAME) {

			normalDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
			uiDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
			gameDelayButtonGyroscope.setBackgroundColor(getResources()
					.getColor(R.color.RithmioYellow));
			fastestDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);

		} else if (mCurrentSensorDelayGyroscope == SensorManager.SENSOR_DELAY_FASTEST) {

			normalDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
			uiDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
			gameDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
			fastestDelayButtonGyroscope.setBackgroundColor(getResources()
					.getColor(R.color.RithmioYellow));

		}
	}

	public void delayButtonColorChangeAccelerometer() {
		if (mCurrentSensorDelayAccelerometer == SensorManager.SENSOR_DELAY_NORMAL) {

			normalDelayButtonAccelerometer.setBackgroundColor(getResources()
					.getColor(R.color.RithmioGreen));
			uiDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
			gameDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
			fastestDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);

		} else if (mCurrentSensorDelayAccelerometer == SensorManager.SENSOR_DELAY_UI) {

			normalDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
			uiDelayButtonAccelerometer.setBackgroundColor(getResources()
					.getColor(R.color.RithmioGreen));
			gameDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
			fastestDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);

		} else if (mCurrentSensorDelayAccelerometer == SensorManager.SENSOR_DELAY_GAME) {

			normalDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
			uiDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
			gameDelayButtonAccelerometer.setBackgroundColor(getResources()
					.getColor(R.color.RithmioGreen));
			fastestDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);

		} else if (mCurrentSensorDelayAccelerometer == SensorManager.SENSOR_DELAY_FASTEST) {

			normalDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
			uiDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
			gameDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
			fastestDelayButtonAccelerometer.setBackgroundColor(getResources()
					.getColor(R.color.RithmioGreen));

		}
	}

	/**
	 * /** onClickListener for Accelerometer toggle button
	 * 
	 * @param v
	 */
	public void accelerometerPower(View v) {
		accelerometerIsOn = !accelerometerIsOn;
		registerSensors();
	}

	// onClickListener for Gyroscope toggle button
	public void gyroscopePower(View v) {
		gyroscopeIsOn = !gyroscopeIsOn;
		registerSensors();
	}

	/**
	 * Unregisters both listeners, then registers whichever sensor is powered
	 * on.
	 */
	// public void registerSensors(SensorType, delayRate)
	public void registerSensors() {

		// This is necessary so that the current listener is unregistered.
		// Otherwise, the Sensor Delay does not change.
		mSensorManager.unregisterListener(MainActivity.this);

		if (accelerometerIsOn) {

			// Registers the Accelerometer sensor
			mSensorManager.registerListener(this, mAccelerometerSensor,
					mCurrentSensorDelayAccelerometer);

		}
		if (gyroscopeIsOn) {

			// Registers the Gyroscope sensor
			mSensorManager.registerListener(this, mGyroscopeSensor,
					mCurrentSensorDelayGyroscope);

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
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
			frontEndAxisGyro(event.values[0], event.values[1], event.values[2]);

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			frontEndAxisAccel(event.values[0], event.values[1], event.values[2]);

	}

	public void frontEndAxisGyro(float gx, float gy, float gz) {
		mXaxisGyro.setText("Gyro X: " + gx);
		mYaxisGyro.setText("Gyro Y: " + gy);
		mZaxisGyro.setText("Gyro Z: " + gz);

		if (mXaxisGyroscope == true)
			mXaxisGyro.setText(null);
		if (mYaxisGyroscope == true)
			mYaxisGyro.setText(null);
		if (mZaxisGyroscope == true)
			mZaxisGyro.setText(null);

	}

	public void frontEndAxisAccel(float ax, float ay, float az) {

		mXaxisAccel.setText("Accel X: " + ax);
		mYaxisAccel.setText("Accel Y: " + ay);
		mZaxisAccel.setText("Accel Z: " + az);

		if (mXaxisAccelerometer == true)
			mXaxisAccel.setText(null);
		if (mYaxisAccelerometer == true)
			mYaxisAccel.setText(null);
		if (mZaxisAccelerometer == true)
			mZaxisAccel.setText(null);

	}

	// float implementation csv

	public void onResume() {
		super.onResume();
		// Resumes sensor readings on restart
		registerSensors();
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregisters both sensors when app is paused (home screen or screen
		// lock), modify or delete this to maintain sensors running despite app
		// pause.
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(tag, "onAccuracyChanged: " + sensor.getName() + ", accuracy: "
				+ accuracy);
		if (sensor == mAccelerometerSensor)
			frontEndAccuracyAccelerometer(accuracy);
		if (sensor == mGyroscopeSensor)
			frontEndAccuracyGyroscope(accuracy);
	}

	public void frontEndAccuracyAccelerometer(int accuracy) {
		if (accuracy == 0) {
			curAccA = "Unreliable";
		} else if (accuracy == 1) {
			curAccA = "Low accuracy";
		} else if (accuracy == 2) {
			curAccA = "Medium accuracy";
		} else if (accuracy == 3) {
			curAccA = "High accuracy";
		}
		currentAccuracyAccelerometer.setText(curAccA);
	}

	public void frontEndAccuracyGyroscope(int accuracy) {
		if (accuracy == 0) {
			curAccG = "Unreliable";
		} else if (accuracy == 1) {
			curAccG = "Low accuracy";
		} else if (accuracy == 2) {
			curAccG = "Medium accuracy";
		} else if (accuracy == 3) {
			curAccG = "High accuracy";
		}
		currentAccuracyGyroscope.setText(curAccG);
	}

	public void textViewAndButtonDeclaration() {

		// Variables for each axis of the Accelerometer and Gyroscope
		mXaxisAccel = (TextView) findViewById(R.id.textView_xAxisAccel);
		mYaxisAccel = (TextView) findViewById(R.id.textView_yAxisAccel);
		mZaxisAccel = (TextView) findViewById(R.id.textView_zAxisAccel);
		mXaxisGyro = (TextView) findViewById(R.id.textView_xAxisGyro);
		mYaxisGyro = (TextView) findViewById(R.id.textView_yAxisGyro);
		mZaxisGyro = (TextView) findViewById(R.id.textView_zAxisGyro);

		// Buttons to choose between Normal, Game, UI, and Fastest Sensor Delays
		normalDelayButtonGyroscope = (Button) findViewById(R.id.button_NormalDelayGyroscope);
		gameDelayButtonGyroscope = (Button) findViewById(R.id.button_GameDelayGyroscope);
		uiDelayButtonGyroscope = (Button) findViewById(R.id.button_UiDelayGyroscope);
		fastestDelayButtonGyroscope = (Button) findViewById(R.id.button_FastestDelayGyroscope);

		// Sets the background colors of the Sensor Delay buttons to gray, which
		// increases the viewed button size. This keeps it the same color, but
		// ensures that it doesn't look weird when it changes color to green as
		// it is activated
		normalDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
		uiDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
		gameDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
		fastestDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);

		// Buttons to choose between Normal, Game, UI, and Fastest Sensor Delays
		normalDelayButtonAccelerometer = (Button) findViewById(R.id.button_NormalDelayAccelerometer);
		gameDelayButtonAccelerometer = (Button) findViewById(R.id.button_GameDelayAccelerometer);
		uiDelayButtonAccelerometer = (Button) findViewById(R.id.button_UiDelayAccelerometer);
		fastestDelayButtonAccelerometer = (Button) findViewById(R.id.button_FastestDelayAccelerometer);

		// Sets the background colors of the Sensor Delay buttons to gray, which
		// increases the viewed button size. This keeps it the same color, but
		// ensures that it doesn't look weird when it changes color to green as
		// it is activated
		normalDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
		uiDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
		gameDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
		fastestDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);

		// Buttons for Supervised and Unsupervised function/activity/fragment
		// calls. Their colors are set to Rithmio-specified yellow and green.
		// Can be changed at another point - file with color names is located at
		// res/values/color.xml
		toSupervisedLearning = (Button) findViewById(R.id.button_ToSupervised);
		toSupervisedLearning.setBackgroundColor(getResources().getColor(
				R.color.RithmioYellow));
		toUnsupervisedLearning = (Button) findViewById(R.id.button_ToUnsupervised);
		toUnsupervisedLearning.setBackgroundColor(getResources().getColor(
				R.color.RithmioGreen));

		// Toggle Buttons to power on and off the Accelerometer and Gyroscope
		// sensor data readings
		toggleButtonGyroscope = (ToggleButton) findViewById(R.id.toggleButton_GyroscopePower);
		toggleButtonAccelerometer = (ToggleButton) findViewById(R.id.toggleButton_AccelerometerPower);

		currentAccuracyAccelerometer = (TextView) findViewById(R.id.currentAccuracyAccel);
		currentAccuracyGyroscope = (TextView) findViewById(R.id.currentAccuracyGyro);

	}

	public void logoAnimation() {
		// Logo spin animation, one per second
		ImageView rithmioLogo = (ImageView) findViewById(R.id.imageView_RithmioLogo);
		RotateAnimation rotatingLogo;
		rotatingLogo = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotatingLogo.setDuration((long) 1000); // # is in milliseconds
		rotatingLogo.setRepeatCount(-1); // -1 means it's infinite
		rithmioLogo.startAnimation(rotatingLogo);
	}

	/**
	 * Clicking on the spinning logo takes to rithmio website
	 * 
	 * @param v
	 * 
	 */
	public void logoClick(View v) {
		Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
		myWebLink.setData(Uri.parse("http://rithmio.com"));
		startActivity(myWebLink);
	}

	public void startSupervisedLearning(View v) {

		// Sets color for Supervised Learning button to gray when
		// clicked
		toSupervisedLearning.setBackgroundColor(getResources().getColor(
				R.color.RithmioGray));

		// Starts button color change function
		learningButtonsBackgroundColorChange();

		// Insert here call for Supervised Learning
		// function/activity/fragment call

	}

	public void startUnsupervisedLearning(View v) {

		// Sets color for Supervised Learning button to gray when
		// clicked
		toUnsupervisedLearning.setBackgroundColor(getResources().getColor(
				R.color.RithmioGray));

		// Starts button color change function
		learningButtonsBackgroundColorChange();

		// Insert here call for Unsupervised Learning
		// function/activity/fragment call
	}

	/**
	 * Changes button color back to original after specified amount of time
	 * (delay = 100 milliseconds).
	 */
	public void learningButtonsBackgroundColorChange() {
		handler.postDelayed(new Runnable() {
			public void run() {

				// Sets Supervised Learning button back to Rithmio Yellow
				toSupervisedLearning.setBackgroundColor(getResources()
						.getColor(R.color.RithmioYellow));

				// Sets Unsupervised Learning button back to Rithmio Green
				toUnsupervisedLearning.setBackgroundColor(getResources()
						.getColor(R.color.RithmioGreen));

			}

			// Current delay is 100 milliseconds. Amount set in variable
			// initializer
		}, delay);
	}

}