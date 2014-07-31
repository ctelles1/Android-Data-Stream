//package app.gyroscope.accelerometer;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
//
//public class ActivityC extends Activity implements SensorEventListener {
//	long timeElapsed;
//	TextView mXaxisAccel = null;
//	TextView mYaxisAccel = null;
//	TextView mZaxisAccel = null;
//	TextView mXaxisGyro = null;
//	TextView mYaxisGyro = null;
//	TextView mZaxisGyro = null;
//	final String tag = "GAP";
//	Intent intent;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_c);
//
//		mXaxisAccel = (TextView) findViewById(R.id.xbox);
//		mYaxisAccel = (TextView) findViewById(R.id.ybox);
//		mZaxisAccel = (TextView) findViewById(R.id.zbox);
//		mXaxisGyro = (TextView) findViewById(R.id.xboxo);
//		mYaxisGyro = (TextView) findViewById(R.id.yboxo);
//		mZaxisGyro = (TextView) findViewById(R.id.zboxo);
//
//		Button d = (Button) findViewById(R.id.unsupervisedToMain);
//		intent = getIntent();
//		d.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				getIntent().getFloatExtra("START_TIME", 0f);
//				finish();
//			}
//		});
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.accel) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	public void onSensorChanged(SensorEvent event) {
//		Log.d(tag, "onSensorChanged: " + event.sensor + ", x: "
//				+ event.values[0] + ", y: " + event.values[1] + ", z: "
//				+ event.values[2]);
//
//		float AxisX = event.values[0];
//		float AxisY = event.values[1];
//		float AxisZ = event.values[2];
//
//		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//			mXaxisGyro.setText("Gyro X: " + AxisX);
//			mYaxisGyro.setText("Gyro Y: " + AxisY);
//			mZaxisGyro.setText("Gyro Z: " + AxisZ);
//		}
//		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//			mXaxisAccel.setText("Accel X: " + AxisX);
//			mYaxisAccel.setText("Accel Y: " + AxisY);
//			mZaxisAccel.setText("Accel Z: " + AxisZ);
//		}
//
//	}
//
//	@Override
//	public void onAccuracyChanged(Sensor sensor, int accuracy) {
//		Log.d(tag, "onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
//	}
//
//	public void onPause(View v) {
//
//	}
//
//	public void onResume(View v) {
//
//	}
//}
