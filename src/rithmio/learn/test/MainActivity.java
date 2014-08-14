package rithmio.learn.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		SensorEventListener {

	final String tag = "GAP";

	boolean turnSensorPowerOnForDEBUG = false; // Currently only for debugging
												// purposes, can be reused to
												// keep accelerometer and
												// gyroscope powered on
												// throughout application use

	// Back-end Functionality //

	SensorManager mSensorManager;
	Sensor mAccelerometerSensor, mGyroscopeSensor;
	int mCurrentSensorDelayGyroscope;
	int mCurrentSensorDelayAccelerometer;
	boolean isAccelerometerOn, isGyroscopeOn;
	boolean isXAccelOn, isYAccelOn, isZAccelOn, isXGyroOn, isYGyroOn,
			isZGyroOn;
	boolean isSamplingOn = false;
	PrintWriter sensorDataFile;

	// GUI //

	TextView mXaxisAccel = null;
	TextView mYaxisAccel = null;
	TextView mZaxisAccel = null;
	TextView mXaxisGyro = null;
	TextView mYaxisGyro = null;
	TextView mZaxisGyro = null;
	Fragment fr;
	Button editRithmFragmentButton, recognizeRithmFragmentButton;
	ImageView supervisedLearningFragmentButton,
			unsupervisedLearningFragmentButton; // fragment switch buttons not
												// in use, explained in
												// frontEndInitialization()
	ImageView rithmioLogo;
	float ROTATE_FROM = 0.0f;
	float ROTATE_TO = -1.0f * 360.0f; // Rithmio logo spins once a second
										// (360.0f), counter-clockwise (-1.0f)

	/******************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometerSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyroscopeSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		// Sets initial Sensor Delay when the application starts to Normal Delay
		// (longest delay). Otherwise, the default delay would be Fastest
		mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_NORMAL;
		mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_NORMAL;

		// Creates a toast verifying that the device contains the required
		// sensors. Does not toast if all sensors (Accelerometer & Gyroscope)
		// are in device
		sensorCheckToaster();

		// Declares the TextViews and Buttons being used
		frontEndInitialization();

		// Sets and starts the animation for the Rithmio logo spin
		logoAnimation();

		if (turnSensorPowerOnForDEBUG) { // Place the [true] somewhere else if
											// starting sensors during runtime
			isAccelerometerOn = true;
			isGyroscopeOn = true;
		}

	}

	// ////////////////////// BACK-END FUNCTIONALITY ///////////////////////

	public void onResume() {
		super.onResume();
		// registerSensors(); // Resumes sensor readings on restart

	}

	@Override
	public void onPause() {
		super.onPause();

		// just in case the activity-level service management fails
		stopSampling();

		// Unregisters both sensors when application is paused (home screen or
		// screen lock), modify or delete this to maintain sensors running
		// despite application pause.
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(tag, "onAccuracyChanged: " + sensor.getName() + ", accuracy: "
				+ accuracy);
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

		// Values are sent to front-end TextViews
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
			frontEndAxisGyro(event.values[0], event.values[1], event.values[2]);
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			frontEndAxisAccel(event.values[0], event.values[1], event.values[2]);

		// Values are sent to CSV file
		if (isSamplingOn) {
			processSample(event);
		}
	}

	/**
	 * Writes the accelerometer and gyroscope sensor values to a CSV file which
	 * is created whenever the application opens (when startSampling() is run).
	 * 
	 * @param sensorEvent
	 *            Values for each individual axis for each sensor
	 */
	private void processSample(SensorEvent sensorEvent) {
		float values[] = sensorEvent.values;
		if (values.length < 3) // Does not record values less than three
								// integers or decimals in length
			return;
		if (sensorDataFile != null) {
			sensorDataFile.print(sensorEvent.timestamp + ", ");
			if (isAccelerometerOn) {
				if (!isXAccelOn) {
					sensorDataFile.print(values[0] + ", ");
				}
				if (!isYAccelOn) {
					sensorDataFile.print(values[1] + ", ");
				}
				if (!isZAccelOn) {
					sensorDataFile.print(values[2] + ", ");
				}
			}
			if (isGyroscopeOn) {
				if (!isXGyroOn) {
					sensorDataFile.print(values[0] + ", ");
				}
				if (!isYGyroOn) {
					sensorDataFile.print(values[1] + ", ");
				}
				if (!isZGyroOn) {
					sensorDataFile.print(values[2]);
				}
			}
			sensorDataFile.println();
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
		GregorianCalendar gcal = new GregorianCalendar();
		// CSV file name is determined here to be "Rithmio_currentDate.csv"
		String fileName = "Rithmio_" + gcal.get(Calendar.YEAR) + "_"
				+ Integer.toString(gcal.get(Calendar.MONTH) + 1) + "_"
				+ gcal.get(Calendar.DAY_OF_MONTH) + "_"
				+ gcal.get(Calendar.HOUR_OF_DAY) + "_"
				+ gcal.get(Calendar.MINUTE) + "_" + gcal.get(Calendar.SECOND)
				+ ".csv";
		// Set to be stored in external directory (SD card in tested devices)
		File captureFileName = new File(
				Environment.getExternalStorageDirectory(), fileName);
		try {
			sensorDataFile = new PrintWriter(new FileWriter(captureFileName,
					false));
		} catch (IOException ex) {
			Log.e(tag, ex.getMessage(), ex);
		}
		// Sets sampling to ON so that values are written to file as per
		// onSensorChanged() call of processSample().
		isSamplingOn = true;
	}

	/**
	 * Stops taking sensor values and closes the CSV file.
	 */
	private void stopSampling() {

		if (mSensorManager != null) {
			Log.d(tag, "unregisterListener/SamplingService");
			mSensorManager.unregisterListener(this);
		}
		if (sensorDataFile != null) {
			sensorDataFile.close();
			sensorDataFile = null;
		}
		isSamplingOn = false;

	}

	/**
	 * Unregisters both listeners, then registers the listeners for whichever
	 * sensor is powered on.
	 * <p>
	 * This function is necessary if Sensor Delay is being changed during
	 * application runtime. Otherwise sensor listener registration happens at
	 * startSampling().
	 */
	public void registerSensors() {
		// This is necessary so that the current listener is unregistered.
		// Otherwise, the Sensor Delay does not change.
		mSensorManager.unregisterListener(MainActivity.this);
		// accelerometerIsOn and gyroscopeIsOn are booleans
		if (isAccelerometerOn) {
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
	 * Currently an onClick called from layout. Turns Accelerometer Sensor
	 * readings on or off, and registers the sensors.
	 * 
	 * @param v
	 */
	public void accelerometerPower(View v) {
		isAccelerometerOn = !isAccelerometerOn;
		registerSensors();
	}

	/**
	 * Currently an onClick called from layout. Turns Gyroscope Sensor readings
	 * on or off, and registers the sensors.
	 * 
	 * @param v
	 */
	public void gyroscopePower(View v) {
		isGyroscopeOn = !isGyroscopeOn;
		registerSensors();
	}

	/**
	 * Currently an onClick called from layout. Sets the current Sensor Delay
	 * being used by either Accelerometer or Gyroscope.
	 * <p>
	 * Can be modified to be a method initialized on onCreate or called from
	 * other methods.
	 * <p>
	 * Case number can be switched by to the id of the button to be clicked.
	 * <p>
	 * 
	 * EXAMPLE: case 1 to case R.id.button_NormalDelayAccelerometer.
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

	/**
	 * Currently an onClick called from layout. Turns on or off the use of
	 * specified axis for Accelerometer or Gyroscope.
	 * <p>
	 * Can be modified to be a method initialized on onCreate or called from
	 * other methods.
	 * <p>
	 * Case number can be switched by to the id of the button/check box to be
	 * clicked.
	 * <p>
	 * 
	 * EXAMPLE: case 1 to case R.id.checkBoxXaxisAccelerometer.
	 * 
	 * @param v
	 */
	public void checkBoxAxis(View v) {
		switch (v.getId()) {
		case 1:
			isXAccelOn = !isXAccelOn;
			break;
		case 2:
			isYAccelOn = !isYAccelOn;
			break;
		case 3:
			isZAccelOn = !isZAccelOn;
			break;
		case 4:
			isXGyroOn = !isXGyroOn;
			break;
		case 5:
			isYGyroOn = !isYGyroOn;
			break;
		case 6:
			isZGyroOn = !isZGyroOn;
			break;
		}
	}

	// ////////////////////// GUI////////////////////

	/**
	 * Sets the TextView for the current gyroscope axis values. Values retrieved
	 * from onSensorChanged.
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
	 * Sets the text for the current accelerometer axis values. Values retrieved
	 * from onSensorChanged.
	 * 
	 * @param ax
	 *            X-Axis for Accelerometer
	 * @param ay
	 *            Y-Axis for Accelerometer
	 * @param az
	 *            Z-Axis for Accelerometer
	 */
	public void frontEndAxisAccel(float ax, float ay, float az) {
		if (isXAccelOn)
			mXaxisAccel.setText(null);
		if (isYAccelOn)
			mYaxisAccel.setText(null);
		if (isZAccelOn)
			mZaxisAccel.setText(null);
	}

	/**
	 * Receives onClick from layout and switches the fragment displayed. On
	 * learning fragments, startSampling() is called, which starts data
	 * collection and writing to csv file.
	 * 
	 * @param v
	 */
	public void selectFragment(View v) {
		if (v == findViewById(R.id.editRithms)) {
			fr = new EditRithmNameFragment();
		} else if (v == findViewById(R.id.recognizeRithms)) {
			fr = new RecognitionFragment();
		} else if (v == findViewById(R.id.learnSupervised)) {
			// Currently an onClick called from layout. Opens CSV file to start
			// taking sensor readings. Will be edited to include supervised
			// learning functions.
			fr = new SupervisedLearningFragment();
			startSampling();
		} else if (v == findViewById(R.id.learnUnsupervised)) {
			// Currently an onClick called from layout. Opens CSV file to start
			// taking sensor readings. Will be edited to include unsupervised
			// learning functions.
			fr = new UnsupervisedLearningFragment();
			startSampling();
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_switch, fr);
		fragmentTransaction.commit();

	}

	/**
	 * Creates toast to indicate to user whether the device does not have the
	 * required sensors.
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

	/**
	 * Initializes variables for fragment switch buttons and logo image, so it
	 * can be animated and used as a button to route to Rithmio website.
	 * <p>
	 * Fragment buttons are currently not in use, as they are being called by id
	 * in selectFrag(); can be modified to make 'onClick' call 'onLongClick' or
	 * other useful function.
	 */
	public void frontEndInitialization() {
		editRithmFragmentButton = (Button) findViewById(R.id.editRithms);
		recognizeRithmFragmentButton = (Button) findViewById(R.id.recognizeRithms);
		supervisedLearningFragmentButton = (ImageView) findViewById(R.id.learnSupervised);
		unsupervisedLearningFragmentButton = (ImageView) findViewById(R.id.learnUnsupervised);
		rithmioLogo = (ImageView) findViewById(R.id.imageView_RithmioLogo);
	}

	/**
	 * Makes Rithmio logo spin once a second as long as the application is open.
	 */
	public void logoAnimation() {
		RotateAnimation rotatingLogo;
		rotatingLogo = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotatingLogo.setDuration((long) 1000); // # is in milliseconds
		rotatingLogo.setRepeatCount(-1); // -1 means it's infinite
		rithmioLogo.startAnimation(rotatingLogo);
	}

	/**
	 * Clicking on the spinning logo takes to Rithmio website
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