package rithmio.learn.test;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EditLearnedRithms extends Fragment {

	// Defines variables and sensors

	// Functionality
	//
	// final String tag = "GAP";
	// SensorManager mSensorManager = null;
	// Sensor mAccelerometerSensor, mGyroscopeSensor;
	//
	// int mSensorDelayNormal, mSensorDelayGame, mSensorDelayUI,
	// mSensorDelayFastest;
	// int mCurrentSensorDelayGyroscope;
	// int mCurrentSensorDelayAccelerometer;
	//
	// public boolean accelerometerIsOn;
	// public boolean gyroscopeIsOn;
	//
	// boolean mXaxisAccelerometer, mYaxisAccelerometer, mZaxisAccelerometer,
	// mXaxisGyroscope, mYaxisGyroscope, mZaxisGyroscope;
	//
	// PrintWriter captureFile;
	// boolean samplingStarted = false;
	//
	// // GUI
	//
	// TextView mXaxisAccel = null;
	// TextView mYaxisAccel = null;
	// TextView mZaxisAccel = null;
	// TextView mXaxisGyro = null;
	// TextView mYaxisGyro = null;
	// TextView mZaxisGyro = null;
	// Button normalDelayButtonGyroscope, gameDelayButtonGyroscope,
	// uiDelayButtonGyroscope, fastestDelayButtonGyroscope;
	// Button normalDelayButtonAccelerometer, gameDelayButtonAccelerometer,
	// uiDelayButtonAccelerometer, fastestDelayButtonAccelerometer;
	// Button toSupervisedLearning, toUnsupervisedLearning;
	// ToggleButton toggleButtonGyroscope, toggleButtonAccelerometer;
	//
	// // Creates handler for Supervised and Unsupervised button clicks for
	// color
	// // to change for only 100 milliseconds
	// final Handler handler = new Handler();
	// final long delay = 100;
	//
	// // Information for logo spin animation
	// private static final float ROTATE_FROM = 0.0f;
	// private static final float ROTATE_TO = -1.0f * 360.0f;
	//
	// TextView currentAccuracyAccelerometer;
	// TextView currentAccuracyGyroscope;
	// String curAccA;
	// String curAccG;

	View fragmentView;

	/******************************************************************************/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(null, null, savedInstanceState);

		// // This version of the app, the layout is locked on the
		// // vertical/portrait position

		// if (container == null)
		// return null;
		//
		// if (fragmentView != null)
		// return fragmentView;
		//
		// fragmentView = inflater.inflate(R.layout.supervision_selection,
		// container, false);
		// return fragmentView;
		//
		return inflater.inflate(R.layout.supervision_selection, container,
				false);

		//
		// mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		//
		// // Validate whether an accelerometer or gyroscope is present or not.
		// // Android Manifest file specifies that app cannot be used/downloaded
		// if
		// // phone does not have the sensors onboard. This functionality can be
		// // taken out if the sensor check is kept on the Manifest.
		// mAccelerometerSensor = mSensorManager
		// .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// mGyroscopeSensor = mSensorManager
		// .getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		//
		// // Creates a toast verifying that the device contains the required
		// // sensors.
		// sensorCheckToaster();
		//
		// // Sets initial Sensor Delay when the first power button is clicked
		// to
		// // Normal Delay (longest delay). Otherwise, the default delay would
		// be
		// // Fastest
		// mCurrentSensorDelayGyroscope = SensorManager.SENSOR_DELAY_NORMAL;
		// mCurrentSensorDelayAccelerometer = SensorManager.SENSOR_DELAY_NORMAL;
		//
		// // Declares the TextViews and Buttons being used
		// textViewAndButtonDeclaration();
		//
		// // Sets and starts the animation for the logo spin
		// logoAnimation();
		//
		// startSampling();

	}

	// /**
	// * Creates toast to indicate to user whether their phone has the required
	// * sensors
	// */
	// public void sensorCheckToaster() {
	//
	// if (mAccelerometerSensor != null && mGyroscopeSensor != null) {
	// Toast.makeText(this, "Required Sensors Found", Toast.LENGTH_SHORT)
	// .show();
	//
	// // accelerometerIsOn = true;
	// //
	// // gyroscopeIsOn = true;
	//
	// } else if (mAccelerometerSensor == null && mGyroscopeSensor != null) {
	// Toast.makeText(this, "No Accelerometer Found", Toast.LENGTH_SHORT)
	// .show();
	//
	// // accelerometerIsOn = false;
	// //
	// // gyroscopeIsOn = true;
	//
	// } else if (mGyroscopeSensor == null && mAccelerometerSensor != null) {
	// Toast.makeText(this, "No Gyroscope Found", Toast.LENGTH_SHORT)
	// .show();
	//
	// // accelerometerIsOn = true;
	// //
	// // gyroscopeIsOn = false;
	//
	// } else if (mGyroscopeSensor == null && mAccelerometerSensor == null) {
	// Toast.makeText(this, "No Sensors Found", Toast.LENGTH_SHORT).show();
	//
	// // accelerometerIsOn = false;
	// //
	// // gyroscopeIsOn = false;
	//
	// }
	// }
	//
	// /**
	// * Changes background color of current Sensor Delay Button for Gyroscope.
	// * Uses Rithmio-specified yellow.
	// */
	// public void delayButtonColorChangeGyroscope() {
	// if (mCurrentSensorDelayGyroscope == SensorManager.SENSOR_DELAY_NORMAL) {
	//
	// normalDelayButtonGyroscope.setBackgroundColor(getResources()
	// .getColor(R.color.RithmioYellow));
	// uiDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// gameDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// fastestDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	//
	// } else if (mCurrentSensorDelayGyroscope == SensorManager.SENSOR_DELAY_UI)
	// {
	//
	// normalDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// uiDelayButtonGyroscope.setBackgroundColor(getResources().getColor(
	// R.color.RithmioYellow));
	// gameDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// fastestDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	//
	// } else if (mCurrentSensorDelayGyroscope ==
	// SensorManager.SENSOR_DELAY_GAME) {
	//
	// normalDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// uiDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// gameDelayButtonGyroscope.setBackgroundColor(getResources()
	// .getColor(R.color.RithmioYellow));
	// fastestDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	//
	// } else if (mCurrentSensorDelayGyroscope ==
	// SensorManager.SENSOR_DELAY_FASTEST) {
	//
	// normalDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// uiDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// gameDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// fastestDelayButtonGyroscope.setBackgroundColor(getResources()
	// .getColor(R.color.RithmioYellow));
	//
	// }
	// }
	//
	// /**
	// * Changes background color of current Sensor Delay Button for
	// * Accelerometer. Uses Rithmio-specified green.
	// */
	// public void delayButtonColorChangeAccelerometer() {
	// if (mCurrentSensorDelayAccelerometer ==
	// SensorManager.SENSOR_DELAY_NORMAL) {
	// normalDelayButtonAccelerometer.setBackgroundColor(getResources()
	// .getColor(R.color.RithmioGreen));
	// uiDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// gameDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// fastestDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// } else if (mCurrentSensorDelayAccelerometer ==
	// SensorManager.SENSOR_DELAY_UI) {
	// normalDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// uiDelayButtonAccelerometer.setBackgroundColor(getResources()
	// .getColor(R.color.RithmioGreen));
	// gameDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// fastestDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// } else if (mCurrentSensorDelayAccelerometer ==
	// SensorManager.SENSOR_DELAY_GAME) {
	// normalDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// uiDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// gameDelayButtonAccelerometer.setBackgroundColor(getResources()
	// .getColor(R.color.RithmioGreen));
	// fastestDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// } else if (mCurrentSensorDelayAccelerometer ==
	// SensorManager.SENSOR_DELAY_FASTEST) {
	// normalDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// uiDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// gameDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// fastestDelayButtonAccelerometer.setBackgroundColor(getResources()
	// .getColor(R.color.RithmioGreen));
	// }
	// }
	//
	// /**
	// * Receives onClick from toggleButton_AccelerometerPower, turns
	// * Accelerometer Sensor readings on or off, and registers the sensors.
	// *
	// * @param v
	// */
	// public void accelerometerPower(View v) {
	// accelerometerIsOn = !accelerometerIsOn;
	// registerSensors();
	// }
	//
	// /**
	// * Receives onClick from toggleButton_GyroscopePower, turns Gyroscope
	// Sensor
	// * readings on or off, and registers the sensors.
	// *
	// * @param v
	// */
	// public void gyroscopePower(View v) {
	// gyroscopeIsOn = !gyroscopeIsOn;
	// registerSensors();
	// }
	//
	// /**
	// * Unregisters both listeners, then registers whichever sensor is powered
	// * on.
	// */
	// public void registerSensors() {
	// // This is necessary so that the current listener is unregistered.
	// // Otherwise, the Sensor Delay does not change.
	// mSensorManager.unregisterListener(EditLearnedRithms.this);
	// if (accelerometerIsOn) {
	// // Registers the Accelerometer sensor
	// mSensorManager.registerListener(this, mAccelerometerSensor,
	// mCurrentSensorDelayAccelerometer);
	// }
	// if (gyroscopeIsOn) {
	// // Registers the Gyroscope sensor
	// mSensorManager.registerListener(this, mGyroscopeSensor,
	// mCurrentSensorDelayGyroscope);
	// }
	// }
	//
	// /**
	// * Displays Accelerometer and Gyroscope sensor readings for the X, Y, and
	// Z
	// * axis.
	// *
	// * @param event
	// * Gathers sensor values for each axis.
	// */
	// public void onSensorChanged(SensorEvent event) {
	// Log.d(tag, "onSensorChanged: " + event.sensor + ", x: "
	// + event.values[0] + ", y: " + event.values[1] + ", z: "
	// + event.values[2]);
	// if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
	// frontEndAxisGyro(event.values[0], event.values[1], event.values[2]);
	// if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
	// frontEndAxisAccel(event.values[0], event.values[1], event.values[2]);
	//
	// // test csv
	// processSample(event);
	//
	// }
	//
	// /**
	// * @param gx
	// * @param gy
	// * @param gz
	// */
	// public void frontEndAxisGyro(float gx, float gy, float gz) {
	// mXaxisGyro.setText("Gyro X: " + gx);
	// mYaxisGyro.setText("Gyro Y: " + gy);
	// mZaxisGyro.setText("Gyro Z: " + gz);
	// if (mXaxisGyroscope == true)
	// mXaxisGyro.setText(null);
	// if (mYaxisGyroscope == true)
	// mYaxisGyro.setText(null);
	// if (mZaxisGyroscope == true)
	// mZaxisGyro.setText(null);
	// }
	//
	// /**
	// * @param ax
	// * @param ay
	// * @param az
	// */
	// public void frontEndAxisAccel(float ax, float ay, float az) {
	// mXaxisAccel.setText("Accel X: " + ax);
	// mYaxisAccel.setText("Accel Y: " + ay);
	// mZaxisAccel.setText("Accel Z: " + az);
	// if (mXaxisAccelerometer == true)
	// mXaxisAccel.setText(null);
	// if (mYaxisAccelerometer == true)
	// mYaxisAccel.setText(null);
	// if (mZaxisAccelerometer == true)
	// mZaxisAccel.setText(null);
	// }
	//
	public void onResume() {
		super.onResume();
		// Resumes sensor readings on restart
		// registerSensors();
	}

	@Override
	public void onPause() {
		super.onPause();

		// csv test lines
		// stopSampling(); // just in case the activity-level service management
		// // fails
		//
		// // Unregisters both sensors when app is paused (home screen or screen
		// // lock), modify or delete this to maintain sensors running despite
		// app
		// // pause.
		// mSensorManager.unregisterListener(this);
	}

//	FragmentTransaction ft;

	public void backToMenu(View v) {
		// Intent backMenu = new Intent(SelectSupervision.this, MainMenu.class);
		// startActivity(backMenu);
//		ft.addToBackStack(null);
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
	}
	//
	// public void backToMenu(View v) {
	// // Create new fragment and transaction
	// Fragment newFragment = new chartsFragment();
	// // consider using Java coding conventions (upper first char class
	// // names!!!)
	// FragmentTransaction transaction = getFragmentManager()
	// .beginTransaction();
	//
	// // Replace whatever is in the fragment_container view with this
	// // fragment,
	// // and add the transaction to the back stack
	// transaction.replace(R.id.fragment_container, newFragment);
	// transaction.addToBackStack(null);
	//
	// // Commit the transaction
	// transaction.commit();
	// }
	//
	// // I just created a new class called Chart, had it extend Activity,
	// // placed it in the Manifest, and had it load the xml layout I had
	// intended
	// // for a fragment.
	//
	// public void ButtonClick(View view) {
	// Fragment mFragment = new YourNextFragment();
	// getSupportFragmentManager().beginTransaction()
	// .replace(R.id.content_frame, mFragment).commit();
	// }

	//
	// @Override
	// public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// Log.d(tag, "onAccuracyChanged: " + sensor.getName() + ", accuracy: "
	// + accuracy);
	// if (sensor == mAccelerometerSensor)
	// frontEndAccuracyAccelerometer(accuracy);
	// if (sensor == mGyroscopeSensor)
	// frontEndAccuracyGyroscope(accuracy);
	// }
	//
	// /**
	// * @param accuracy
	// */
	// public void frontEndAccuracyAccelerometer(int accuracy) {
	// if (accuracy == 0) {
	// curAccA = "Unreliable";
	// } else if (accuracy == 1) {
	// curAccA = "Low accuracy";
	// } else if (accuracy == 2) {
	// curAccA = "Medium accuracy";
	// } else if (accuracy == 3) {
	// curAccA = "High accuracy";
	// }
	// currentAccuracyAccelerometer.setText(curAccA);
	// }
	//
	// /**
	// * @param accuracy
	// */
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
	//
	// public void learnRithms(View v) {
	// Intent intent = new Intent(EditLearnedRithms.this,
	// SelectSupervision.class);
	// startActivity(learnIntent);
	// }
	//
	// public void editRithms(View v) {
	//
	// }
	//
	// public void recognizeRithms(View v) {
	//
	// }
	//
	// /**
	// *
	// */
	// public void textViewAndButtonDeclaration() {
	//
	// // // Variables for each axis of the Accelerometer and Gyroscope
	//
	// //
	//
	// Button learnNewRithms = (Button) findViewById(R.id.NewActivity);
	// Button editRithms = (Button) findViewById(R.id.EditDelete);
	// Button recognizeRithms = (Button) findViewById(R.id.Recognition);
	//
	// //
	//
	// //
	//
	// // mXaxisAccel = (TextView) findViewById(R.id.textView_xAxisAccel);
	// // mYaxisAccel = (TextView) findViewById(R.id.textView_yAxisAccel);
	// // mZaxisAccel = (TextView) findViewById(R.id.textView_zAxisAccel);
	// // mXaxisGyro = (TextView) findViewById(R.id.textView_xAxisGyro);
	// // mYaxisGyro = (TextView) findViewById(R.id.textView_yAxisGyro);
	// // mZaxisGyro = (TextView) findViewById(R.id.textView_zAxisGyro);
	// //
	// // // Buttons to choose between Normal, Game, UI, and Fastest Sensor
	// // Delays
	// // normalDelayButtonGyroscope = (Button)
	// // findViewById(R.id.button_NormalDelayGyroscope);
	// // gameDelayButtonGyroscope = (Button)
	// // findViewById(R.id.button_GameDelayGyroscope);
	// // uiDelayButtonGyroscope = (Button)
	// // findViewById(R.id.button_UiDelayGyroscope);
	// // fastestDelayButtonGyroscope = (Button)
	// // findViewById(R.id.button_FastestDelayGyroscope);
	// //
	// // // Sets the background colors of the Sensor Delay buttons to gray,
	// // which
	// // // increases the viewed button size. This keeps it the same color,
	// // but
	// // // ensures that it doesn't look weird when it changes color to green
	// // as
	// // // it is activated
	// // normalDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// // uiDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// // gameDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// // fastestDelayButtonGyroscope.setBackgroundColor(Color.LTGRAY);
	// //
	// // // Buttons to choose between Normal, Game, UI, and Fastest Sensor
	// // Delays
	// // normalDelayButtonAccelerometer = (Button)
	// // findViewById(R.id.button_NormalDelayAccelerometer);
	// // gameDelayButtonAccelerometer = (Button)
	// // findViewById(R.id.button_GameDelayAccelerometer);
	// // uiDelayButtonAccelerometer = (Button)
	// // findViewById(R.id.button_UiDelayAccelerometer);
	// // fastestDelayButtonAccelerometer = (Button)
	// // findViewById(R.id.button_FastestDelayAccelerometer);
	// //
	// // // Sets the background colors of the Sensor Delay buttons to gray,
	// // which
	// // // increases the viewed button size. This keeps it the same color,
	// // but
	// // // ensures that it doesn't look weird when it changes color to green
	// // as
	// // // it is activated
	// // normalDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// // uiDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// // gameDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// // fastestDelayButtonAccelerometer.setBackgroundColor(Color.LTGRAY);
	// //
	// // // Buttons for Supervised and Unsupervised function/activity/fragment
	// // // calls. Their colors are set to Rithmio-specified yellow and green.
	// // // Can be changed at another point - file with color names is located
	// // at
	// // // res/values/color.xml
	// // toSupervisedLearning = (Button)
	// // findViewById(R.id.button_ToSupervised);
	// // toSupervisedLearning.setBackgroundColor(getResources().getColor(
	// // R.color.RithmioYellow));
	// // toUnsupervisedLearning = (Button)
	// // findViewById(R.id.button_ToUnsupervised);
	// // toUnsupervisedLearning.setBackgroundColor(getResources().getColor(
	// // R.color.RithmioGreen));
	// //
	// // // Toggle Buttons to power on and off the Accelerometer and Gyroscope
	// // // sensor data readings
	// // toggleButtonGyroscope = (ToggleButton)
	// // findViewById(R.id.toggleButton_GyroscopePower);
	// // toggleButtonAccelerometer = (ToggleButton)
	// // findViewById(R.id.toggleButton_AccelerometerPower);
	// //
	// // currentAccuracyAccelerometer = (TextView)
	// // findViewById(R.id.currentAccuracyAccel);
	// // currentAccuracyGyroscope = (TextView)
	// // findViewById(R.id.currentAccuracyGyro);
	//
	// }
	//
	// /**
	// *
	// */
	// public void logoAnimation() {
	// // Logo spin animation, one per second
	// ImageView rithmioLogo = (ImageView)
	// findViewById(R.id.imageView_RithmioLogo);
	// RotateAnimation rotatingLogo;
	// rotatingLogo = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
	// Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
	// 0.5f);
	// rotatingLogo.setDuration((long) 1000); // # is in milliseconds
	// rotatingLogo.setRepeatCount(-1); // -1 means it's infinite
	// rithmioLogo.startAnimation(rotatingLogo);
	// }
	//
	// /**
	// * Clicking on the spinning logo takes to rithmio website
	// *
	// * @param v
	// *
	// */
	// public void logoClick(View v) {
	// Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
	// myWebLink.setData(Uri.parse("http://rithmio.com"));
	// startActivity(myWebLink);
	// }
	//
	// public void startSupervisedLearning(View v) {
	//
	// // Sets color for Supervised Learning button to gray when
	// // clicked
	// toSupervisedLearning.setBackgroundColor(getResources().getColor(
	// R.color.RithmioGray));
	//
	// // Starts button color change function
	// learningButtonsBackgroundColorChange();
	//
	// // Insert here call for Supervised Learning
	// // function/activity/fragment call
	//
	// }
	//
	// public void startUnsupervisedLearning(View v) {
	//
	// // Sets color for Supervised Learning button to gray when
	// // clicked
	// toUnsupervisedLearning.setBackgroundColor(getResources().getColor(
	// R.color.RithmioGray));
	//
	// // Starts button color change function
	// learningButtonsBackgroundColorChange();
	//
	// // Insert here call for Unsupervised Learning
	// // function/activity/fragment call
	// }
	//
	// /**
	// * Changes button color back to original after specified amount of time
	// * (delay = 100 milliseconds).
	// */
	// public void learningButtonsBackgroundColorChange() {
	// handler.postDelayed(new Runnable() {
	// public void run() {
	//
	// // Sets Supervised Learning button back to Rithmio Yellow
	// toSupervisedLearning.setBackgroundColor(getResources()
	// .getColor(R.color.RithmioYellow));
	//
	// // Sets Unsupervised Learning button back to Rithmio Green
	// toUnsupervisedLearning.setBackgroundColor(getResources()
	// .getColor(R.color.RithmioGreen));
	//
	// }
	//
	// // Current delay is 100 milliseconds. Amount set in variable
	// // initializer
	// }, delay);
	// }
	//
	// /**
	// * @param sensorEvent
	// */
	// private void processSample(SensorEvent sensorEvent) {
	// float values[] = sensorEvent.values;
	// if (values.length < 3)
	// return;
	// if (captureFile != null) {
	// captureFile.print(sensorEvent.timestamp + ", ");
	// if (accelerometerIsOn) {
	// if (!mXaxisAccelerometer) {
	// captureFile.print(values[0] + ", ");
	// }
	// if (!mYaxisAccelerometer) {
	// captureFile.print(values[1] + ", ");
	// }
	// if (!mZaxisAccelerometer) {
	// captureFile.print(values[2] + ", ");
	// }
	// }
	// if (gyroscopeIsOn) {
	// if (!mXaxisGyroscope) {
	// captureFile.print(values[0] + ", ");
	// }
	// if (!mYaxisGyroscope) {
	// captureFile.print(values[1] + ", ");
	// }
	// if (!mZaxisGyroscope) {
	// captureFile.print(values[2]);
	// }
	// }
	// captureFile.println();
	// }
	// }
	//
	// /**
	// *
	// */
	// private void stopSampling() {
	// if (!samplingStarted)
	// return;
	// if (mSensorManager != null) {
	// Log.d(tag, "unregisterListener/SamplingService");
	// mSensorManager.unregisterListener(this);
	// }
	// if (captureFile != null) {
	// captureFile.close();
	// captureFile = null;
	// }
	// samplingStarted = false;
	// }
	//
	// /**
	// *
	// */
	// private void startSampling() {
	// if (samplingStarted)
	// return;
	// List<Sensor> sensors = mSensorManager
	// .getSensorList(Sensor.TYPE_ACCELEROMETER);
	// mAccelerometerSensor = sensors.size() == 0 ? null : sensors.get(0);
	// sensors = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
	// mGyroscopeSensor = sensors.size() == 0 ? null : sensors.get(0);
	//
	// if ((mAccelerometerSensor != null) && (mGyroscopeSensor != null)) {
	// Log.d(tag, "registerListener/SamplingService");
	// mSensorManager.registerListener(this, mAccelerometerSensor,
	// mCurrentSensorDelayAccelerometer);
	// mSensorManager.registerListener(this, mGyroscopeSensor,
	// mCurrentSensorDelayGyroscope);
	//
	// } else {
	// Log.d(tag, "Sensor(s) missing: accelSensor: "
	// + mAccelerometerSensor + "; gyroSensor: "
	// + mGyroscopeSensor);
	// }
	// captureFile = null;
	// GregorianCalendar gcal = new GregorianCalendar();
	// String fileName = "Tithmio_" + gcal.get(Calendar.YEAR) + "_"
	// + Integer.toString(gcal.get(Calendar.MONTH) + 1) + "_"
	// + gcal.get(Calendar.DAY_OF_MONTH) + "_"
	// + gcal.get(Calendar.HOUR_OF_DAY) + "_"
	// + gcal.get(Calendar.MINUTE) + "_" + gcal.get(Calendar.SECOND)
	// + ".csv";
	// File captureFileName = new File(
	// Environment.getExternalStorageDirectory(), fileName);
	// try {
	// captureFile = new PrintWriter(
	// new FileWriter(captureFileName, false));
	// } catch (IOException ex) {
	// Log.e(tag, ex.getMessage(), ex);
	// }
	// samplingStarted = true;
	// }
}