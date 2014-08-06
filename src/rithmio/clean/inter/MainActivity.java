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

	public boolean accelerometerIsOn = false;
	public boolean gyroscopeIsOn = false;

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

		mXaxisAccelerometer = false;
		mYaxisAccelerometer = false;
		mZaxisAccelerometer = false;
		mXaxisGyroscope = false;
		mYaxisGyroscope = false;
		mZaxisGyroscope = false;

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

	public void sensorRegisterNormalDelayGyroscope(View v) {
		// function(sensor, rate);
		mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_NORMAL;
		registerSensors();
		// Changes button background color
		delayButtonColorChangeGyroscope();
	}

	public void sensorRegisterUIDelayGyroscope(View v) {
		// function(sensor, rate);
		mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_UI;
		registerSensors();
		// Changes button background color
		delayButtonColorChangeGyroscope();
	}

	public void sensorRegisterGameDelayGyroscope(View v) {
		// function(sensor, rate);
		mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_GAME;
		registerSensors();
		// Changes button background color
		delayButtonColorChangeGyroscope();
	}

	public void sensorRegisterFastestDelayGyroscope(View v) {
		// function(sensor, rate);
		mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_FASTEST;
		registerSensors();
		// Changes button background color
		delayButtonColorChangeGyroscope();
	}

	public void sensorRegisterNormalDelayAccelerometer(View v) {
		// function(sensor, rate);
		mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_NORMAL;
		registerSensors();
		// Changes button background color
		delayButtonColorChangeAccelerometer();
	}

	public void sensorRegisterUiDelayAccelerometer(View v) {
		// function(sensor, rate);
		mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_UI;
		registerSensors();
		// Changes button background color
		delayButtonColorChangeAccelerometer();
	}

	public void sensorRegisterGameDelayAccelerometer(View v) {
		// function(sensor, rate);
		mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_GAME;
		registerSensors();
		// Changes button background color
		delayButtonColorChangeAccelerometer();
	}

	public void sensorRegisterFastestDelayAccelerometer(View v) {
		// function(sensor, rate);
		mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_FASTEST;
		registerSensors();
		// Changes button background color
		delayButtonColorChangeAccelerometer();
	}

	boolean mXaxisAccelerometer, mYaxisAccelerometer, mZaxisAccelerometer,
			mXaxisGyroscope, mYaxisGyroscope, mZaxisGyroscope;

	public void checkBoxAxis(View v) {
		switch (v.getId()) {
		case R.id.checkBoxXaxisAccelerometer:
			mXaxisAccelerometer = !mXaxisAccelerometer;
		case R.id.checkBoxYaxisAccelerometer:
			if (mYaxisAccelerometer == false) {
				mYaxisAccelerometer = true;
			} else if (mYaxisAccelerometer == true) {
				mYaxisAccelerometer = false;
			}
		case R.id.checkBoxZaxisAccelerometer:
			if (mZaxisAccelerometer == false) {
				mZaxisAccelerometer = true;
			} else if (mZaxisAccelerometer == true) {
				mZaxisAccelerometer = false;
			}
		case R.id.checkBoxXaxisGyroscope:
			if (mXaxisGyroscope == false) {
				mXaxisGyroscope = true;
			} else if (mXaxisGyroscope == true) {
				mXaxisGyroscope = false;
			}
		case R.id.checkBoxYaxisGyroscope:
			if (mYaxisGyroscope == false) {
				mYaxisGyroscope = true;
			} else if (mYaxisGyroscope == true) {
				mYaxisGyroscope = false;
			}
		case R.id.checkBoxZaxisGyroscope:
			if (mZaxisGyroscope == false) {
				mZaxisGyroscope = true;
			} else if (mZaxisGyroscope == true) {
				mZaxisGyroscope = false;
			}
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
	 * Registers Accelerometer and/or Gyroscope sensors, and onClickListeners
	 * for each Sensor Delay button. Each button then sets the current Sensor
	 * Delay being used to the specified button clicked.
	 */
	// public void buttonOnClickListener() {

	// Registers Accelerometer and/or Gyroscope sensors,
	// depending on toggle button state. This register is necessary to start
	// the sensor readings when sensor power buttons are pressed.
	// normalDelayButton.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// // Sets current Sensor Delay to Normal Delay
	// mCurrentSensorDelay = SensorManager.SENSOR_DELAY_NORMAL;
	//
	// // This register is necessary to start the readings when the
	// // specific Sensor Delay button is pressed
	// register();
	//
	// }
	// });
	//
	// uiDelayButton.setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// // Sets current Sensor Delay to UI Delay
	//
	//
	// }
	// });
	//
	// gameDelayButton.setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// // Sets current Sensor Delay to Game Delay
	//
	// register();
	//
	// }
	// });
	//
	// fastestDelayButton.setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// // Sets current sensor delay to Fastest Delay
	//
	// register();
	//
	// }
	// });
	// }

	/**
	 * onClickListener for Accelerometer toggle button
	 * 
	 * @param v
	 */
	public void accelerometerPower(View v) {
		if (!accelerometerIsOn == true) {
			accelerometerIsOn = true;
		} else if (accelerometerIsOn == true) {
			accelerometerIsOn = false;
		}
		registerSensors();
	}

	// onClickListener for Gyroscope toggle button
	public void gyroscopePower(View v) {
		if (!gyroscopeIsOn == true) {
			gyroscopeIsOn = true;
		} else if (gyroscopeIsOn == true) {
			gyroscopeIsOn = false;
		}
		registerSensors();
	}

	/**
	 * Unregisters both listeners, then registers whichever sensor is powered
	 * on.
	 */
	public void registerSensors() {

		if (accelerometerIsOn || gyroscopeIsOn) {

			// This is necessary so that the current listener is unregistered.
			// Otherwise, the Sensor Delay does not change.
			mSensorManager.unregisterListener(MainActivity.this);

			if (accelerometerIsOn) {

				// Registers the Gyroscope sensor
				mSensorManager.registerListener(this, mAccelerometerSensor,
						mCurrentSensorDelayAccelerometer);

			}
			if (gyroscopeIsOn) {

				// Registers the Accelerometer sensor
				mSensorManager.registerListener(this, mGyroscopeSensor,
						mCurrentSensorDelayGyroscope);

			}
			// Starts sensor register and button color change functions
			// buttonOnClickListener();

		} else {

			// Sets Gyroscope power button status to off
			gyroscopeIsOn = false;

			// Sets Accelerometer power button status to off
			accelerometerIsOn = false;

			mSensorManager.unregisterListener(MainActivity.this);

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
			
			if (mXaxisGyroscope == true) {
				mXaxisGyro.setText("Gyro X: off");
			}
			if (mYaxisGyroscope == true) {
				mYaxisGyro.setText("Gyro Y: off");
			}
			if (mZaxisGyroscope == true) {
				mZaxisGyro.setText("Gyro Z: off");
			}
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mXaxisAccel.setText("Accel X: " + AxisX);
			mYaxisAccel.setText("Accel Y: " + AxisY);
			mZaxisAccel.setText("Accel Z: " + AxisZ);
			if (mXaxisAccelerometer == true) {
				mXaxisAccel.setText("Accel X: off");
			}
			if (mYaxisAccelerometer == true) {
				mYaxisAccel.setText("Accel Y: off");
			}
			if (mZaxisAccelerometer == true) {
				mZaxisAccel.setText("Accel Z: off");
			}
		}
	}

	public void onResume() {
		super.onResume();
		registerSensors();
		// Resumes sensor readings on restart
		// buttonOnClickListener();
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
		Log.d(tag, "onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
	}

}