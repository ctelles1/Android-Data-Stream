package rithmio.learn.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends FragmentActivity implements SensorEventListener {

	final String tag = "GAP";
	boolean DEBUG = false;
	// Functionality

	SensorManager mSensorManager;
	Sensor mAccelerometerSensor, mGyroscopeSensor;

	int mSensorDelayNormal, mSensorDelayGame, mSensorDelayUI,
			mSensorDelayFastest;
	int mCurrentSensorDelayGyroscope;
	int mCurrentSensorDelayAccelerometer;

	public boolean isAccelerometerOn;
	public boolean isGyroscopeOn;

	boolean isXAccelOn, isYAccelOn, isZAccelOn, isXGyroOn, isYGyroOn,
			isZGyroOn;

	PrintWriter captureFile;
	boolean isSamplingOn = false;

	// GUI

	TextView mXaxisAccel = null;
	TextView mYaxisAccel = null;
	TextView mZaxisAccel = null;
	TextView mXaxisGyro = null;
	TextView mYaxisGyro = null;
	TextView mZaxisGyro = null;

	Button newEdit, newRecog;

	// Information for logo spin animation
	private static final float ROTATE_FROM = 0.0f;
	private static final float ROTATE_TO = -1.0f * 360.0f;

	Fragment fr;
	ImageView rithmioLogo;

	/******************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_menu);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		mAccelerometerSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyroscopeSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		// Sets initial Sensor Delay when the app starts to Normal Delay
		// (longest delay). Otherwise, the default delay would be Fastest
		mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_NORMAL;
		mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_NORMAL;

		// Creates a toast verifying that the device contains the required
		// sensors.
		sensorCheckToaster();

		// Declares the TextViews and Buttons being used
		frontEndInitialization();

		// Sets and starts the animation for the logo spin
		logoAnimation();

		if (DEBUG) {
			isAccelerometerOn = true;
			isGyroscopeOn = true;
		}
	}

	// ////////////////////////// FUNCTIONALITY ///////////////////////////

	public void onResume() {
		super.onResume();
		// Resumes sensor readings on restart
		registerSensors();
	}

	@Override
	public void onPause() {
		super.onPause();

		stopSampling(); // just in case the activity-level service management
						// fails

		// Unregisters both sensors when app is paused (home screen or screen
		// lock), modify or delete this to maintain sensors running despite app
		// pause.
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(tag, "onAccuracyChanged: " + sensor.getName() + ", accuracy: "
				+ accuracy);
	}

	/**
	 * Unregisters both listeners, then registers whichever sensor is powered
	 * on.
	 */
	public void registerSensors() {
		// This is necessary so that the current listener is unregistered.
		// Otherwise, the Sensor Delay does not change.
		mSensorManager.unregisterListener(MainMenu.this);
		if (isAccelerometerOn) { // accelerometerIsOn and gyroscopeIsOn are
									// booleans
			// Registers the Accelerometer sensor
			mSensorManager.registerListener(this, mAccelerometerSensor,
					mCurrentSensorDelayAccelerometer);
		}
		if (isGyroscopeOn) {
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

		// Values to CSV file
		if (isSamplingOn) {
			processSample(event);
		}
	}

	/**
	 * Writes the accelerometer and gyroscope sensor values to a CSV file which
	 * is created whenever the application opens.
	 * 
	 * @param sensorEvent
	 *            Values for each individual axis for each sensor
	 */
	private void processSample(SensorEvent sensorEvent) {
		float values[] = sensorEvent.values;
		if (values.length < 3)
			return;
		if (captureFile != null) {
			captureFile.print(sensorEvent.timestamp + ", ");
			if (isAccelerometerOn) {
				if (!isXAccelOn) {
					captureFile.print(values[0] + ", ");
				}
				if (!isYAccelOn) {
					captureFile.print(values[1] + ", ");
				}
				if (!isZAccelOn) {
					captureFile.print(values[2] + ", ");
				}
			}
			if (isGyroscopeOn) {
				if (!isXGyroOn) {
					captureFile.print(values[0] + ", ");
				}
				if (!isYGyroOn) {
					captureFile.print(values[1] + ", ");
				}
				if (!isZGyroOn) {
					captureFile.print(values[2]);
				}
			}
			captureFile.println();
		}
	}

	/**
	 * Starts the accelerometer and gyroscope sensor readings, creates a CSV
	 * file which is used to store the sensor values taken from
	 * proccessSample().
	 */
	private void startSampling() {

		if (isAccelerometerOn) {
			mSensorManager.registerListener(this, mAccelerometerSensor,
					mCurrentSensorDelayAccelerometer);
		}
		if (isGyroscopeOn) {
			mSensorManager.registerListener(this, mGyroscopeSensor,
					mCurrentSensorDelayGyroscope);
		}
		// captureFile = null;
		GregorianCalendar gcal = new GregorianCalendar();
		String fileName = "Zithmio_" + gcal.get(Calendar.YEAR) + "_"
				+ Integer.toString(gcal.get(Calendar.MONTH) + 1) + "_"
				+ gcal.get(Calendar.DAY_OF_MONTH) + "_"
				+ gcal.get(Calendar.HOUR_OF_DAY) + "_"
				+ gcal.get(Calendar.MINUTE) + "_" + gcal.get(Calendar.SECOND)
				+ ".csv";
		File captureFileName = new File(
				Environment.getExternalStorageDirectory(), fileName);
		try {
			captureFile = new PrintWriter(
					new FileWriter(captureFileName, false));
		} catch (IOException ex) {
			Log.e(tag, ex.getMessage(), ex);
		}
		isSamplingOn = true;
	}

	/**
	 * Stops taking sensor values and closes the CSV file for editing.
	 */
	private void stopSampling() {

		if (mSensorManager != null) {
			Log.d(tag, "unregisterListener/SamplingService");
			mSensorManager.unregisterListener(this);
		}
		if (captureFile != null) {
			captureFile.close();
			captureFile = null;
		}
		isSamplingOn = false;

	}

	/**
	 * Currently an onClick. Turns Accelerometer Sensor readings on or off, and
	 * registers the sensors.
	 * 
	 * @param v
	 */
	public void accelerometerPower(View v) {
		isAccelerometerOn = !isAccelerometerOn;
		registerSensors();
	}

	/**
	 * Currently an onClick. Turns Gyroscope Sensor readings on or off, and
	 * registers the sensors.
	 * 
	 * @param v
	 */
	public void gyroscopePower(View v) {
		isGyroscopeOn = !isGyroscopeOn;
		registerSensors();
	}

	/**
	 * Starts taking sensor readings. Will be edited to include supervised
	 * learning functions.
	 * 
	 * @param v
	 */
	public void learnSupervised(View v) {
		startSampling();
	}

	/**
	 * Starts taking sensor readings. Will be edited to include unsupervised
	 * learning functions.
	 * 
	 * @param v
	 */
	public void learnUnsupervised(View v) {
		startSampling();
	}

	/**
	 * Case # corresponds to the id of the button to be clicked
	 * 
	 * EXAMPLE: case R.id.button_NormalDelayAccelerometer:
	 * 
	 * @param v
	 */
	public void sensorDelayChange(View v) {
		switch (v.getId()) {
		case 1:
			mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_NORMAL;
			registerSensors();
			break;
		case 2:
			mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_UI;
			registerSensors();
			break;
		case 3:
			mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_GAME;
			registerSensors();
			break;
		case 4:
			mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_FASTEST;
			registerSensors();
			break;
		case 5:
			mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_NORMAL;
			registerSensors();
			break;
		case 6:
			mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_UI;
			registerSensors();
			break;
		case 7:
			mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_GAME;
			registerSensors();
			break;
		case 8:
			mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_FASTEST;
			registerSensors();
			break;
		}
	}

	// ////////////////////// GUI////////////////////

	/**
	 * Sets the text for the current gyroscope axis values.
	 * 
	 * @param gx
	 *            X-Axis for Gyroscope
	 * @param gy
	 *            Y-Axis for Gyroscope
	 * @param gz
	 *            Z-Axis for Gyroscope
	 */
	public void frontEndAxisGyro(float gx, float gy, float gz) {
		if (isXGyroOn == true)
			mXaxisGyro.setText(null);
		if (isYGyroOn == true)
			mYaxisGyro.setText(null);
		if (isZGyroOn == true)
			mZaxisGyro.setText(null);
	}

	/**
	 * Sets the text for the current accelerometer axis values.
	 * 
	 * @param ax
	 *            X-Axis for Accelerometer
	 * @param ay
	 *            Y-Axis for Accelerometer
	 * @param az
	 *            Z-Axis for Accelerometer
	 */
	public void frontEndAxisAccel(float ax, float ay, float az) {
		if (isXAccelOn == true)
			mXaxisAccel.setText(null);
		if (isYAccelOn == true)
			mYaxisAccel.setText(null);
		if (isZAccelOn == true)
			mZaxisAccel.setText(null);
	}

	/**
	 * Receives onClick from main_menu.xml and switches the fragment displayed.
	 * 
	 * @param v
	 */
	public void selectFrag(View v) {
		if (v == findViewById(R.id.editRithms)) {
			fr = new EditLearnedRithms();
		} else if (v == findViewById(R.id.recognizeRithms)) {
			fr = new RecognizeRithms();
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_switch, fr);
		fragmentTransaction.commit();
	}

	/**
	 * Creates toast to indicate to user whether their phone has the required
	 * sensors.
	 */
	public void sensorCheckToaster() {
		if (mAccelerometerSensor == null && mGyroscopeSensor != null) {
			Toast.makeText(this, "No Accelerometer Found", Toast.LENGTH_SHORT)
					.show();
		} else if (mGyroscopeSensor == null && mAccelerometerSensor != null) {
			Toast.makeText(this, "No Gyroscope Found", Toast.LENGTH_SHORT)
					.show();
		} else if (mGyroscopeSensor == null && mAccelerometerSensor == null) {
			Toast.makeText(this, "No Sensors Found", Toast.LENGTH_SHORT).show();
		}
	}

	// public void frontEndAccuracyGyroscope(int accuracy) {
	// if (accuracy == 0) {
	// curAccG = "Unreliable";
	// } else if (accuracy == 1) {
	// curAccG = "Low accuracy";
	// } else if (accuracy == 2) {
	// curAccG = "Medium accuracy";
	// } else if (accuracy == 3) {
	// curAccG = "High accuracy";
	// }
	// currentAccuracyGyroscope.setText(curAccG);
	// }

	/**
	 * Variables for each axis of the Accelerometer and Gyroscope
	 */
	public void frontEndInitialization() {

		newEdit = (Button) findViewById(R.id.editRithms);
		newEdit.setBackgroundColor(getResources().getColor(
				R.color.RithmioYellow));
		newRecog = (Button) findViewById(R.id.recognizeRithms);
		newRecog.setBackgroundColor(getResources().getColor(
				R.color.RithmioGreen));
		rithmioLogo = (ImageView) findViewById(R.id.imageView_RithmioLogo);

	}

	/**
	 * Makes Rithmio Logo spin once a second
	 */
	public void logoAnimation() {
		// Logo spin animation, one per second
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

}