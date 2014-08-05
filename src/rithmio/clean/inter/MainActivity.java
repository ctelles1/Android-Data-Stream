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
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

	Sensor mSensorTypeAccel, mSensorTypeGyro, mSensorType;

	int mSensorDelayNorm, mSensorDelayGame, mSensorDelayUI, mSensorDelayFast;
	int mSensorDelaySwitch;
	int toggleButtonCounterAccel = 0;
	int toggleButtonCounterGyro = 0;

	Button normDelayButton, gameDelayButton, uiDelayButton, fastDelayButton;
	Button DelayButton;
	Button toSuper;
	Button toUnsuper;
	ToggleButton toggleGyro, toggleAccel;

	// Creates handler for Supervised and Unsupervised button clicks for color
	// to change for only 100 milliseconds
	final Handler handler = new Handler();
	final long delay = 100;

	// Information for logo spin animation
	private static final float ROTATE_FROM = 0.0f;
	private static final float ROTATE_TO = -1.0f * 360.0f;// 3.141592654f *
															// 32.0f;

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
		mSensorTypeAccel = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorTypeGyro = mSensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		// Creates toast to indicate to user whether their phone has the
		// required sensors
		if (mSensorTypeAccel != null && mSensorTypeGyro != null) {
			Toast.makeText(this, "Required Sensors Found", Toast.LENGTH_SHORT)
					.show();
		} else if (mSensorTypeAccel == null && mSensorTypeGyro != null) {
			Toast.makeText(this, "No Accelerometer Found", Toast.LENGTH_SHORT)
					.show();
		} else if (mSensorTypeGyro == null && mSensorTypeAccel != null) {
			Toast.makeText(this, "No Gyroscope Found", Toast.LENGTH_SHORT)
					.show();
		} else if (mSensorTypeGyro == null && mSensorTypeAccel == null) {
			Toast.makeText(this, "No Sensors Found", Toast.LENGTH_SHORT).show();
		}

		// Variables for Sensor Delays
		mSensorDelayNorm = SensorManager.SENSOR_DELAY_NORMAL;
		mSensorDelayGame = SensorManager.SENSOR_DELAY_GAME;
		mSensorDelayUI = SensorManager.SENSOR_DELAY_UI;
		mSensorDelayFast = SensorManager.SENSOR_DELAY_FASTEST;

		// Sets initial Sensor Delay when the first power button is clicked to
		// Normal Delay (longest delay). Otherwise, the default delay would be
		// Fastest
		mSensorDelaySwitch = mSensorDelayNorm;

		// Variables for each axis of the Accelerometer and Gyroscope
		mXaxisAccel = (TextView) findViewById(R.id.textView_xAxisAccel);
		mYaxisAccel = (TextView) findViewById(R.id.textView_yAxisAccel);
		mZaxisAccel = (TextView) findViewById(R.id.textView_zAxisAccel);
		mXaxisGyro = (TextView) findViewById(R.id.textView_xAxisGyro);
		mYaxisGyro = (TextView) findViewById(R.id.textView_yAxisGyro);
		mZaxisGyro = (TextView) findViewById(R.id.textView_zAxisGyro);

		// Buttons to choose between Normal, Game, UI, and Fastest Sensor Delays
		normDelayButton = (Button) findViewById(R.id.button_NormalDelay);
		gameDelayButton = (Button) findViewById(R.id.button_GameDelay);
		uiDelayButton = (Button) findViewById(R.id.button_UiDelay);
		fastDelayButton = (Button) findViewById(R.id.button_FastestDelay);

		// Sets the background colors of the Sensor Delay buttons to gray, which
		// increases the viewed button size. This keeps it the same color, but
		// ensures that it doesn't look weird when it changes color to green as
		// it is activated
		normDelayButton.setBackgroundColor(Color.LTGRAY);
		uiDelayButton.setBackgroundColor(Color.LTGRAY);
		gameDelayButton.setBackgroundColor(Color.LTGRAY);
		fastDelayButton.setBackgroundColor(Color.LTGRAY);

		// Buttons for Supervised and Unsupervised function/activity/fragment
		// calls. Their colors are set to Rithmio-specified yellow and green.
		// Can be changed at another point - file with color names is located at
		// res/values/color.xml
		toSuper = (Button) findViewById(R.id.button_ToSupervised);
		toSuper.setBackgroundColor(getResources().getColor(
				R.color.RithmioYellow));
		toUnsuper = (Button) findViewById(R.id.button_ToUnsupervised);
		toUnsuper.setBackgroundColor(getResources().getColor(
				R.color.RithmioGreen));

		// Toggle Buttons to power on and off the Accelerometer and Gyroscope
		// sensor data readings
		toggleGyro = (ToggleButton) findViewById(R.id.toggleButton_GyroscopePower);
		toggleAccel = (ToggleButton) findViewById(R.id.toggleButton_AccelerometerPower);

		// Sets state of Accelerometer and Gyroscope power toggle buttons to off
		// as app/activity is initialized
		toggleGyro.setChecked(false);
		toggleAccel.setChecked(false);

		// onClickListener for Gyroscope toggle button
		toggleGyro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleGyro.isChecked()) {

					// Sets current sensor type to Gyroscope
					mSensorType = mSensorTypeGyro;

					// Turns on Gyroscope functionality redundancy check (See
					// onClickListeners for the sensor delay buttons)
					toggleButtonCounterGyro = 1;

					// Starts sensor register and button color change functions
					buttonOnClickListener();

				} else {

					// Unregisters Gyroscope sensor listener
					mSensorManager.unregisterListener(MainActivity.this,
							mSensorTypeGyro);

					// Turns off Gyroscope functionality redundancy check
					toggleButtonCounterGyro = 0;

					// Sets Gyroscope power button status to off
					toggleGyro.setChecked(false);

				}
			}
		});

		// onClickListener for Accelerometer toggle button
		toggleAccel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleAccel.isChecked()) {

					// Sets current sensor type to Accelerometer
					mSensorType = mSensorTypeAccel;

					// Turns on Accelerometer functionality redundancy check
					toggleButtonCounterAccel = 1;

					// Starts sensor register and button color change functions
					buttonOnClickListener();

				} else {

					// Unregisters Accelerometer sensor listener
					mSensorManager.unregisterListener(MainActivity.this,
							mSensorTypeAccel);

					// Turns off Accelerometer functionality redundancy check
					toggleButtonCounterAccel = 0;

					// Sets Accelerometer power button status to off
					toggleAccel.setChecked(false);

				}
			}
		});

		toSuper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Sets color for Supervised Learning button to gray when
				// clicked
				toSuper.setBackgroundColor(getResources().getColor(
						R.color.RithmioGray));

				// Starts button color change function
				backgroundColorChange();

				// Insert here call for Supervised Learning
				// function/activity/fragment call

			}
		});
		toUnsuper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Sets color for Supervised Learning button to gray when
				// clicked
				toUnsuper.setBackgroundColor(getResources().getColor(
						R.color.RithmioGray));

				// Starts button color change function
				backgroundColorChange();

				// Insert here call for Unsupervised Learning
				// function/activity/fragment call
			}
		});

		// Logo spin animation, one per second
		ImageView favicon = (ImageView) findViewById(R.id.imageView_RithmioLogo);
		RotateAnimation r;
		r = new RotateAnimation(ROTATE_FROM, ROTATE_TO,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		r.setDuration((long) 1000);
		r.setRepeatCount(-1);
		favicon.startAnimation(r);

		// Clicking on logo takes to rithmio website
		favicon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myWebLink = new Intent(
						android.content.Intent.ACTION_VIEW);
				myWebLink.setData(Uri.parse("http://rithmio.com"));
				startActivity(myWebLink);
			}
		});

	}

	/**
	 * Changes button color back to original after specified amount of time in
	 * milliseconds (final long delay = 100).
	 */
	void backgroundColorChange() {
		handler.postDelayed(new Runnable() {
			public void run() {

				// Sets Supervised Learning button back to Rithmio Yellow
				toSuper.setBackgroundColor(getResources().getColor(
						R.color.RithmioYellow));

				// Sets Unsupervised Learning button back to Rithmio Green
				toUnsuper.setBackgroundColor(getResources().getColor(
						R.color.RithmioGreen));

			}

			// Current delay is 100 milliseconds. Amount set in variable
			// initializer
		}, delay);
	}

	/**
	 * Called when both Accelerometer and Gyroscope power buttons are toggled
	 * off. Sets their state to off, sets redundant state check to off, and
	 * unregisters both listeners.
	 */
	public void setCheckedFalse() {

		toggleAccel.setChecked(false);
		toggleGyro.setChecked(false);

		toggleButtonCounterAccel = 0;
		toggleButtonCounterGyro = 0;

		mSensorManager.unregisterListener(MainActivity.this, mSensorTypeAccel);
		mSensorManager.unregisterListener(MainActivity.this, mSensorTypeGyro);
	}

	/**
	 * Starts time count, registers Accelerometer and/or Gyroscope sensors, and
	 * onClickListeners for each Sensor Delay button. Each button then sets the
	 * current Sensor Delay being used to the specified button clicked, starts
	 * the Sensor Delay statistics loop, then sets the current Sensor Delay as
	 * the "previous" sensor delay.
	 */
	public void buttonOnClickListener() {

		// Registers Accelerometer and/or Gyroscope sensors,
		// depending on toggle button state. This register is necessary to start
		// the sensor readings when sensor power buttons are pressed.
		register();

		normDelayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Checks if either Accelerometer or Gyroscope sensors are off
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					// Sets current Sensor Delay to Normal Delay
					mSensorDelaySwitch = mSensorDelayNorm;

					// This register is necessary to start the readings when the
					// specific Sensor Delay button is pressed
					register();

					// Checks if both toggle buttons are off, starts function
					// which unregisters both sensors
				} else if (toggleButtonCounterAccel == 0
						&& toggleButtonCounterGyro == 0) {

					setCheckedFalse();
				}
			}
		});

		uiDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					// Sets current Sensor Delay to UI Delay
					mSensorDelaySwitch = mSensorDelayUI;

					register();

				} else if (toggleButtonCounterAccel == 0
						&& toggleButtonCounterGyro == 0) {

					setCheckedFalse();
				}
			}
		});

		gameDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					// Sets current Sensor Delay to Game Delay
					mSensorDelaySwitch = mSensorDelayGame;

					register();

				} else if (toggleButtonCounterAccel == 0
						&& toggleButtonCounterGyro == 0) {

					setCheckedFalse();
				}
			}
		});

		fastDelayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					// Sets current sensor delay to Fastest Delay
					mSensorDelaySwitch = mSensorDelayFast;

					register();

				} else if (toggleButtonCounterAccel == 0
						&& toggleButtonCounterGyro == 0) {

					setCheckedFalse();
				}
			}
		});
	}

	/**
	 * Unregisters both listeners, then registers whichever sensor is powered
	 * on.
	 */
	public void register() {

		// This is necessary so that the current listener is unregistered.
		// Otherwise, the Sensor Delay does not change.
		mSensorManager.unregisterListener(this);

		if (toggleGyro.isChecked()) {

			// Registers the Gyroscope sensor
			mSensorManager.registerListener(this, mSensorTypeGyro,
					mSensorDelaySwitch);

		}
		if (toggleAccel.isChecked()) {

			// Registers the Accelerometer sensor
			mSensorManager.registerListener(this, mSensorTypeAccel,
					mSensorDelaySwitch);

		}

		// Changes button background color
		delayColorChange();

	}

	/**
	 * Changes background color of current Sensor Delay Button. Uses
	 * Rithmio-specified green.
	 */
	public void delayColorChange() {
		if (mSensorDelaySwitch == mSensorDelayNorm) {

			normDelayButton.setBackgroundColor(getResources().getColor(
					R.color.RithmioGreen));
			uiDelayButton.setBackgroundColor(Color.LTGRAY);
			gameDelayButton.setBackgroundColor(Color.LTGRAY);
			fastDelayButton.setBackgroundColor(Color.LTGRAY);

		} else if (mSensorDelaySwitch == mSensorDelayUI) {

			normDelayButton.setBackgroundColor(Color.LTGRAY);
			uiDelayButton.setBackgroundColor(getResources().getColor(
					R.color.RithmioGreen));
			gameDelayButton.setBackgroundColor(Color.LTGRAY);
			fastDelayButton.setBackgroundColor(Color.LTGRAY);

		} else if (mSensorDelaySwitch == mSensorDelayGame) {

			normDelayButton.setBackgroundColor(Color.LTGRAY);
			uiDelayButton.setBackgroundColor(Color.LTGRAY);
			gameDelayButton.setBackgroundColor(getResources().getColor(
					R.color.RithmioGreen));
			fastDelayButton.setBackgroundColor(Color.LTGRAY);

		} else if (mSensorDelaySwitch == mSensorDelayFast) {

			normDelayButton.setBackgroundColor(Color.LTGRAY);
			uiDelayButton.setBackgroundColor(Color.LTGRAY);
			gameDelayButton.setBackgroundColor(Color.LTGRAY);
			fastDelayButton.setBackgroundColor(getResources().getColor(
					R.color.RithmioGreen));

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
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mXaxisAccel.setText("Accel X: " + AxisX);
			mYaxisAccel.setText("Accel Y: " + AxisY);
			mZaxisAccel.setText("Accel Z: " + AxisZ);
		}
	}

	public void onResume() {
		super.onResume();

		// Resumes sensor readings on restart
		buttonOnClickListener();
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
