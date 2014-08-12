package rithmio.learn.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainMenu extends FragmentActivity implements SensorEventListener {

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

	PrintWriter captureFile;
	boolean samplingStarted = false;

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

	Fragment fr;
	int i = 0;
	// LinearLayout ll = (LinearLayout) findViewById(R.id.scroll);

	LinearLayout containerLayout;
	static int totalEditTexts = 0;

	/******************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// This version of the app, the layout is locked on the
		// vertical/portrait position
		setContentView(R.layout.main_menu);
		// containerLayout = (LinearLayout) findViewById(R.id.scroll);

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

	// public void addEdit(View v) {

	// i++;
	// TextView tv = new TextView(getApplicationContext());
	// tv.setText("Rithm" + i);
	// ll.addView(tv);
	// EditText et = new EditText(getApplicationContext());
	// et.setText(i + ")");
	// ll.addView(et);

	// LinearLayout linearlayout = (LinearLayout) findViewById(R.id.scroll);
	// EditText[] edt = new EditText[100];
	// for (int i = 0; i < 100; i++) {
	// edt[i] = new EditText(getBaseContext());
	// edt[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	// edt[i].setHint("Change the name for Rithm " + i);
	// linearlayout.addView(edt[i]);
	// }
	// setContentView(R.layout.activity_dynamic_views);

	// selectFrag(v);
	//
	// ScrollView scrollview = (ScrollView)
	// findViewById(R.id.rithm_editing);
	// LinearLayout linearlayout = (LinearLayout) findViewById(R.id.scroll);
	// i++;
	// EditText et = new EditText(getBaseContext());
	// et.setText("Rithm " + i);
	// linearlayout.addView(et);
	// setContentView(scrollview);
	// }

	public void onBackPressed() {
		totalEditTexts++;
		if (totalEditTexts > 100)
			return;
		EditText editText = new EditText(this);
		containerLayout.addView(editText);
		editText.setGravity(Gravity.LEFT);
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) editText
				.getLayoutParams();
		layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
		editText.setLayoutParams(layoutParams);
		// if you want to identify the created editTexts, set a tag, like below
		editText.setHint("Please enter new Rithm name");
	}

	public void selectFrag(View v) {
		// if (v == findViewById(R.id.learnRithms)) {
		// fr = new SelectSupervision();
		// } else
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
		if (mAccelerometerSensor != null && mGyroscopeSensor != null) {
			Toast.makeText(this, "Required Sensors Found", Toast.LENGTH_SHORT)
					.show();
		} else if (mAccelerometerSensor == null && mGyroscopeSensor != null) {
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
	 * Receives onClick from toggleButton_AccelerometerPower, turns
	 * Accelerometer Sensor readings on or off, and registers the sensors.
	 * 
	 * @param v
	 */
	public void accelerometerPower(View v) {
		accelerometerIsOn = !accelerometerIsOn;
		registerSensors();
	}

	/**
	 * Receives onClick from toggleButton_GyroscopePower, turns Gyroscope Sensor
	 * readings on or off, and registers the sensors.
	 * 
	 * @param v
	 */
	public void gyroscopePower(View v) {
		gyroscopeIsOn = !gyroscopeIsOn;
		registerSensors();
	}

	/**
	 * Unregisters both listeners, then registers whichever sensor is powered
	 * on.
	 */
	public void registerSensors() {
		// This is necessary so that the current listener is unregistered.
		// Otherwise, the Sensor Delay does not change.
		mSensorManager.unregisterListener(MainMenu.this);
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

		// Values to Csv file
		processSample(event);

	}

	/**
	 * @param gx
	 * @param gy
	 * @param gz
	 */
	public void frontEndAxisGyro(float gx, float gy, float gz) {
		if (mXaxisGyroscope == true)
			mXaxisGyro.setText(null);
		if (mYaxisGyroscope == true)
			mYaxisGyro.setText(null);
		if (mZaxisGyroscope == true)
			mZaxisGyro.setText(null);
	}

	/**
	 * @param ax
	 * @param ay
	 * @param az
	 */
	public void frontEndAxisAccel(float ax, float ay, float az) {
		if (mXaxisAccelerometer == true)
			mXaxisAccel.setText(null);
		if (mYaxisAccelerometer == true)
			mYaxisAccel.setText(null);
		if (mZaxisAccelerometer == true)
			mZaxisAccel.setText(null);
	}

	public void onResume() {
		super.onResume();
		// Resumes sensor readings on restart
		registerSensors();
	}

	@Override
	public void onPause() {
		super.onPause();

		// csv test lines
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
		if (sensor == mAccelerometerSensor)
			frontEndAccuracyAccelerometer(accuracy);
		if (sensor == mGyroscopeSensor)
			frontEndAccuracyGyroscope(accuracy);
	}

	/**
	 * @param accuracy
	 */
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

	/**
	 * @param accuracy
	 */
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

	/**
	 * 
	 */
	public void textViewAndButtonDeclaration() {

		// // Variables for each axis of the Accelerometer and Gyroscope

		// Button newActivity = (Button) findViewById(R.id.learnRithms);
		// newActivity.setBackgroundColor(getResources().getColor(
		// R.color.RithmioGreen));
		Button newEdit = (Button) findViewById(R.id.editRithms);
		newEdit.setBackgroundColor(getResources().getColor(
				R.color.RithmioYellow));
		Button newRecog = (Button) findViewById(R.id.recognizeRithms);
		newRecog.setBackgroundColor(getResources().getColor(
				R.color.RithmioGreen));

	}

	/**  
	 * 
	 */
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

	public void learnSupervised(View v) {

		// startSampling();
	}

	public void learnUnsupervised(View v) {
		
		// startSampling();
	}

	/**
	 * @param sensorEvent
	 */
	private void processSample(SensorEvent sensorEvent) {
		float values[] = sensorEvent.values;
		if (values.length < 3)
			return;
		if (captureFile != null) {
			captureFile.print(sensorEvent.timestamp + ", ");
			if (accelerometerIsOn) {
				if (!mXaxisAccelerometer) {
					captureFile.print(values[0] + ", ");
				}
				if (!mYaxisAccelerometer) {
					captureFile.print(values[1] + ", ");
				}
				if (!mZaxisAccelerometer) {
					captureFile.print(values[2] + ", ");
				}
			}
			if (gyroscopeIsOn) {
				if (!mXaxisGyroscope) {
					captureFile.print(values[0] + ", ");
				}
				if (!mYaxisGyroscope) {
					captureFile.print(values[1] + ", ");
				}
				if (!mZaxisGyroscope) {
					captureFile.print(values[2]);
				}
			}
			captureFile.println();
		}
	}

	/**
	 * 
	 */
	private void stopSampling() {
		if (!samplingStarted)
			return;
		if (mSensorManager != null) {
			Log.d(tag, "unregisterListener/SamplingService");
			mSensorManager.unregisterListener(this);
		}
		if (captureFile != null) {
			captureFile.close();
			captureFile = null;
		}
		samplingStarted = false;
	}

	/**
	 * 
	 */
	private void startSampling() {
		if (samplingStarted)
			return;
		List<Sensor> sensors = mSensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		mAccelerometerSensor = sensors.size() == 0 ? null : sensors.get(0);
		sensors = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
		mGyroscopeSensor = sensors.size() == 0 ? null : sensors.get(0);

		if ((mAccelerometerSensor != null) && (mGyroscopeSensor != null)) {
			Log.d(tag, "registerListener/SamplingService");
			mSensorManager.registerListener(this, mAccelerometerSensor,
					mCurrentSensorDelayAccelerometer);
			mSensorManager.registerListener(this, mGyroscopeSensor,
					mCurrentSensorDelayGyroscope);

		} else {
			Log.d(tag, "Sensor(s) missing: accelSensor: "
					+ mAccelerometerSensor + "; gyroSensor: "
					+ mGyroscopeSensor);
		}
		captureFile = null;
		GregorianCalendar gcal = new GregorianCalendar();
		String fileName = "Tithmio_" + gcal.get(Calendar.YEAR) + "_"
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
		samplingStarted = true;
	}

}