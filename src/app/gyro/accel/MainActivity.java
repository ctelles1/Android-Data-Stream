package app.gyro.accel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends Activity implements SensorEventListener {

	// Defines variables and sensors

	final String tag = "GAP";

	SensorManager mSensorManager = null;
	Sensor mSensorTypeAccel, mSensorTypeGyro, mSensorType;
	TextView mXaxisAccel = null;
	TextView mYaxisAccel = null;
	TextView mZaxisAccel = null;
	TextView mXaxisGyro = null;
	TextView mYaxisGyro = null;
	TextView mZaxisGyro = null;
	TextView mNormLastClick, mUiLastClick, mGameLastClick, mFastLastClick;
	TextView mNormTotalTime, mUiTotalTime, mGameTotalTime, mFastTotalTime;
	TextView mNormClickCount, mUiClickCount, mGameClickCount, mFastClickCount;
	TextView mchronoNormText, mchronoGameText, mchronoUiText, mchronoFastText;
	TextView mTotal;
	String mDelaySelectionTextView;
	long timeElapsed;
	long durationSinceLastClick;
	long mStartTime;
	long mEndTime;
	long mTotalTime;
	long mTotalTimeNorm, mTotalTimeGame, mTotalTimeUI, mTotalTimeFast;
	int mSensorDelayNorm, mSensorDelayGame, mSensorDelayUI, mSensorDelayFast;
	int mSensorDelaySwitch;
	int mPreviousSelectedDelay = -1;
	int toggleButtonCounter = 0;
	int toggleButtonCounterAccel = 0;
	int toggleButtonCounterGyro = 0;
	int mClickCountNorm, mClickCountUI, mClickCountGame, mClickCountFast;
	int mClickCounter;
	Chronometer chronometerClock;
	Button normDelayButton, gameDelayButton, uiDelayButton, fastDelayButton;
	ToggleButton toggleGyro, toggleAccel;
	Intent intent;

	Button next;

	/******************************************************************************/

	/** Called when the activity is first created. */
	/*
	 * Public: Sets up variables to specified sensors, time readings, TextViews
	 * Buttons, chronometer, and starts onClickListener for Sensor Delay
	 * Buttons.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// next = (Button) findViewById(R.id.activity_button);
		// next.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View view) {
		// Toast.makeText(getApplicationContext(), "text",
		// Toast.LENGTH_SHORT).show();
		// Intent myIntent = new Intent(view.getContext(),
		// DrawerActivity.class);
		// startActivityForResult(myIntent, 0);
		// }
		// });

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Validate whether an accelerometer or gyroscope is present or not
		mSensorTypeAccel = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorTypeGyro = mSensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		if (mSensorTypeAccel != null) {
			Toast.makeText(this, "Accelerometer Found.", Toast.LENGTH_SHORT)
					.show();
		} else if (mSensorTypeAccel == null) {
			Toast.makeText(this, "No Accelerometer Found.", Toast.LENGTH_SHORT)
					.show();
		}
		if (mSensorTypeGyro != null) {
			Toast.makeText(this, "Gyroscope Found.", Toast.LENGTH_SHORT).show();
		} else if (mSensorTypeGyro == null) {
			Toast.makeText(this, "No Gyroscope Found.", Toast.LENGTH_SHORT)
					.show();
		}

		mSensorDelayNorm = SensorManager.SENSOR_DELAY_NORMAL;
		mSensorDelayGame = SensorManager.SENSOR_DELAY_GAME;
		mSensorDelayUI = SensorManager.SENSOR_DELAY_UI;
		mSensorDelayFast = SensorManager.SENSOR_DELAY_FASTEST;

		mSensorDelaySwitch = mSensorDelayNorm;

		mXaxisAccel = (TextView) findViewById(R.id.xbox);
		mYaxisAccel = (TextView) findViewById(R.id.ybox);
		mZaxisAccel = (TextView) findViewById(R.id.zbox);
		mXaxisGyro = (TextView) findViewById(R.id.xboxo);
		mYaxisGyro = (TextView) findViewById(R.id.yboxo);
		mZaxisGyro = (TextView) findViewById(R.id.zboxo);

		mNormLastClick = (TextView) findViewById(R.id.normLast);
		mGameLastClick = (TextView) findViewById(R.id.gameLast);
		mUiLastClick = (TextView) findViewById(R.id.uiLast);
		mFastLastClick = (TextView) findViewById(R.id.fastLast);

		mNormTotalTime = (TextView) findViewById(R.id.normTotal);
		mGameTotalTime = (TextView) findViewById(R.id.gameTotal);
		mUiTotalTime = (TextView) findViewById(R.id.uiTotal);
		mFastTotalTime = (TextView) findViewById(R.id.fastTotal);

		mNormClickCount = (TextView) findViewById(R.id.normCount);
		mGameClickCount = (TextView) findViewById(R.id.gameCount);
		mUiClickCount = (TextView) findViewById(R.id.uiCount);
		mFastClickCount = (TextView) findViewById(R.id.fastCount);

		mchronoNormText = (TextView) findViewById(R.id.chronoNormal);
		mchronoUiText = (TextView) findViewById(R.id.chronoUI);
		mchronoGameText = (TextView) findViewById(R.id.chronoGame);
		mchronoFastText = (TextView) findViewById(R.id.chronoFast);

		normDelayButton = (Button) findViewById(R.id.ND);
		gameDelayButton = (Button) findViewById(R.id.GD);
		uiDelayButton = (Button) findViewById(R.id.UD);
		fastDelayButton = (Button) findViewById(R.id.FD);

		normDelayButton.setBackgroundColor(Color.LTGRAY);
		uiDelayButton.setBackgroundColor(Color.LTGRAY);
		gameDelayButton.setBackgroundColor(Color.LTGRAY);
		fastDelayButton.setBackgroundColor(Color.LTGRAY);

		chronometerClock = (Chronometer) findViewById(R.id.chronometer1);
		chronometerClock.setBase(SystemClock.elapsedRealtime());
		chronometerClock.start();

		toggleGyro = (ToggleButton) findViewById(R.id.powerGyro);
		toggleAccel = (ToggleButton) findViewById(R.id.powerAccel);

		toggleGyro.setChecked(false);
		toggleAccel.setChecked(false);

		timeElapsed = SystemClock.elapsedRealtime()
				- chronometerClock.getBase();

		toggleGyro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleGyro.isChecked()) {

					mSensorType = mSensorTypeGyro;

					toggleButtonCounterGyro = 1;

					mButtonOnClickListener();

				} else {

					mSensorManager.unregisterListener(MainActivity.this,
							mSensorTypeGyro);

					toggleButtonCounterGyro = 0;

					toggleGyro.setChecked(false);

				}
			}
		});
		toggleAccel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleAccel.isChecked()) {

					mSensorType = mSensorTypeAccel;

					toggleButtonCounterAccel = 1;

					mButtonOnClickListener();

				} else {

					mSensorManager.unregisterListener(MainActivity.this,
							mSensorTypeAccel);

					toggleButtonCounterAccel = 0;

					toggleAccel.setChecked(false);

				}
			}
		});

//		Button supervised = (Button) findViewById(R.id.toSupervised);
//		supervised.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent(getBaseContext(), ActivityB.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				getIntent().putExtra("START_TIME", timeElapsed);
//				startActivity(intent);
//			}
//		});
//		Button unsupervised = (Button) findViewById(R.id.toUnsupervised);
//		unsupervised.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent(getBaseContext(), ActivityC.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				getIntent().putExtra("START_TIME", timeElapsed);
//				startActivity(intent);
//			}
//		});

	}

	public void setCheckedFalse() {

		toggleAccel.setChecked(false);

		toggleGyro.setChecked(false);

		toggleButtonCounter = 0;

		mSensorManager.unregisterListener(MainActivity.this, mSensorTypeAccel);

		mSensorManager.unregisterListener(MainActivity.this, mSensorTypeGyro);
	}

	public void mButtonOnClickListener() {

		setStartAndEndTime(); // Gets values for start and end time calculations

		register();

		normDelayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (toggleButtonCounterAccel == 1
						|| toggleButtonCounterGyro == 1) {

					mSensorDelaySwitch = mSensorDelayNorm;

					currentTime();

					mPreviousSelectedDelay = mSensorDelayNorm;

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

					mSensorDelaySwitch = mSensorDelayUI;

					currentTime();

					mPreviousSelectedDelay = mSensorDelayUI;

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

					mSensorDelaySwitch = mSensorDelayGame;

					currentTime();

					mPreviousSelectedDelay = mSensorDelayGame;

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

					mSensorDelaySwitch = mSensorDelayFast;

					currentTime();

					mPreviousSelectedDelay = mSensorDelayFast;

				} else if (toggleButtonCounterAccel == 0
						&& toggleButtonCounterGyro == 0) {

					setCheckedFalse();
				}
			}
		});
	}

	public void currentTime() {
		if (toggleButtonCounterAccel == 1 || toggleButtonCounterGyro == 1) {

			if (mSensorDelaySwitch == mSensorDelayNorm) {

				mEndTime = System.nanoTime(); // put this as EndTime earlier

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();// Calculates time since last button click

				mClickCountNorm++;
				mClickCounter = mClickCountNorm;

			} else if (mSensorDelaySwitch == mSensorDelayUI) {

				mEndTime = System.nanoTime();

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();

				mClickCountUI++;
				mClickCounter = mClickCountUI;

			} else if (mSensorDelaySwitch == mSensorDelayGame) {

				mEndTime = System.nanoTime();

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();

				mClickCountGame++;
				mClickCounter = mClickCountGame;

			} else if (mSensorDelaySwitch == mSensorDelayFast) {

				mEndTime = System.nanoTime();

				durationSinceLastClick = (mEndTime - mStartTime) / 1000000;
				timeSinceLastClick();

				mClickCountFast++;
				mClickCounter = mClickCountFast;

			}

			clickStatisticsText(); // Sets text on display

			setStartAndEndTime(); // Unregisters current Delay setting
									// listeners and Registers them in new Delay
									// settings

			register();

		} else if (toggleButtonCounterAccel == 0
				&& toggleButtonCounterGyro == 0) {

			setCheckedFalse();

			return;

		}
	}

	public void timeSinceLastClick() {

		if (mPreviousSelectedDelay == mSensorDelayNorm) {

			mTotalTimeNorm = mTotalTimeNorm + durationSinceLastClick;

			mTotalTime = mTotalTimeNorm / 1000;

		} else if (mPreviousSelectedDelay == mSensorDelayUI) {

			mTotalTimeUI = mTotalTimeUI + durationSinceLastClick;

			mTotalTime = mTotalTimeUI / 1000;

		} else if (mPreviousSelectedDelay == mSensorDelayGame) {

			mTotalTimeGame = mTotalTimeGame + durationSinceLastClick;

			mTotalTime = mTotalTimeGame / 1000;

		} else if (mPreviousSelectedDelay == mSensorDelayFast) {

			mTotalTimeFast = mTotalTimeFast + durationSinceLastClick;

			mTotalTime = mTotalTimeFast / 1000;

		}

	}

	public void clickStatisticsText() {

		if (mSensorDelaySwitch == mSensorDelayNorm) {

			mNormLastClick.setText(String.valueOf(durationSinceLastClick));

			mNormLastClick.setText("" + durationSinceLastClick);

			mTotal = mNormTotalTime;

			previousMode();

			total();

			mNormClickCount.setText("" + mClickCounter);

			delayColorChange();

		} else if (mSensorDelaySwitch == mSensorDelayUI) {

			mUiLastClick.setText(String.valueOf(durationSinceLastClick));

			mUiLastClick.setText("" + durationSinceLastClick);

			mTotal = mUiTotalTime;

			previousMode();

			total();

			mUiClickCount.setText("" + mClickCounter);

			delayColorChange();

		} else if (mSensorDelaySwitch == mSensorDelayGame) {

			mGameLastClick.setText(String.valueOf(durationSinceLastClick));

			mGameLastClick.setText("" + durationSinceLastClick);

			mTotal = mGameTotalTime;

			previousMode();

			total();

			mGameClickCount.setText("" + mClickCounter);

			delayColorChange();

		} else if (mSensorDelaySwitch == mSensorDelayFast) {

			mFastLastClick.setText(String.valueOf(durationSinceLastClick));

			mFastLastClick.setText("" + durationSinceLastClick);

			mTotal = mFastTotalTime;

			previousMode();

			total();

			mFastClickCount.setText("" + mClickCounter);

			delayColorChange();

		}

	}

	public void setStartAndEndTime() {

		mEndTime = System.nanoTime();

		mStartTime = System.nanoTime();

	}

	public void register() {
		mSensorManager.unregisterListener(this);

		if (toggleGyro.isChecked()) {

			mSensorManager.registerListener(this, mSensorTypeGyro,
					mSensorDelaySwitch);

		}
		if (toggleAccel.isChecked()) {

			mSensorManager.registerListener(this, mSensorTypeAccel,
					mSensorDelaySwitch);

		}
	}

	public void previousMode() {

		if (mPreviousSelectedDelay == mSensorDelaySwitch) {

			return;

		} else if (mPreviousSelectedDelay == mSensorDelayNorm) {

			mTotal = mNormTotalTime;

		} else if (mPreviousSelectedDelay == mSensorDelayUI) {

			mTotal = mUiTotalTime;

		} else if (mPreviousSelectedDelay == mSensorDelayGame) {

			mTotal = mGameTotalTime;

		} else if (mPreviousSelectedDelay == mSensorDelayFast) {

			mTotal = mFastTotalTime;

		}

	}

	public void total() {

		mTotal.setText(String.valueOf(mTotalTime));

		mTotal.setText("" + mTotalTime);

	}

	public void delayColorChange() {
		if (mSensorDelaySwitch == mSensorDelayNorm) {

			mchronoNormText.setBackgroundColor(0x55000000);
			mchronoUiText.setBackgroundColor(0x55FFFFFF);
			mchronoGameText.setBackgroundColor(0x55FFFFFF);
			mchronoFastText.setBackgroundColor(0x55FFFFFF);

			normDelayButton.setBackgroundColor(Color.GREEN);
			uiDelayButton.setBackgroundColor(Color.LTGRAY);
			gameDelayButton.setBackgroundColor(Color.LTGRAY);
			fastDelayButton.setBackgroundColor(Color.LTGRAY);

		} else if (mSensorDelaySwitch == mSensorDelayUI) {

			mchronoNormText.setBackgroundColor(0x55FFFFFF);
			mchronoUiText.setBackgroundColor(0x55000000);
			mchronoGameText.setBackgroundColor(0x55FFFFFF);
			mchronoFastText.setBackgroundColor(0x55FFFFFF);

			normDelayButton.setBackgroundColor(Color.LTGRAY);
			uiDelayButton.setBackgroundColor(Color.GREEN);
			gameDelayButton.setBackgroundColor(Color.LTGRAY);
			fastDelayButton.setBackgroundColor(Color.LTGRAY);

		} else if (mSensorDelaySwitch == mSensorDelayGame) {

			mchronoNormText.setBackgroundColor(0x55FFFFFF);
			mchronoUiText.setBackgroundColor(0x55FFFFFF);
			mchronoGameText.setBackgroundColor(0x55000000);
			mchronoFastText.setBackgroundColor(0x55FFFFFF);

			normDelayButton.setBackgroundColor(Color.LTGRAY);
			uiDelayButton.setBackgroundColor(Color.LTGRAY);
			gameDelayButton.setBackgroundColor(Color.GREEN);
			fastDelayButton.setBackgroundColor(Color.LTGRAY);

		} else if (mSensorDelaySwitch == mSensorDelayFast) {

			mchronoNormText.setBackgroundColor(0x55FFFFFF);
			mchronoUiText.setBackgroundColor(0x55FFFFFF);
			mchronoGameText.setBackgroundColor(0x55FFFFFF);
			mchronoFastText.setBackgroundColor(0x55000000);

			normDelayButton.setBackgroundColor(Color.LTGRAY);
			uiDelayButton.setBackgroundColor(Color.LTGRAY);
			gameDelayButton.setBackgroundColor(Color.LTGRAY);
			fastDelayButton.setBackgroundColor(Color.GREEN);

		}

	}

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

		register();

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

}