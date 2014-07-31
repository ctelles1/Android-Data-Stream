package app.gyro.accelerometer;
//package app.gyro.accel;
//
//import java.util.Timer;
//
//import com.taweechai.realtimeopenglchart.opengl.RealtimeChartSurfaceView;
//import android.app.Activity;
//import android.content.Intent;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class ActivityB extends Activity implements SensorEventListener {
//	long timeElapsed;
//	final String tag = "GAP";
//	TextView mYaxisAccel = null;
//	TextView mXaxisAccel = null;
//
//	TextView mZaxisAccel = null;
//	TextView mXaxisGyro = null;
//	TextView mYaxisGyro = null;
//	TextView mZaxisGyro = null;
//	Intent intent;
//
//	float glPointSize;
//
//	private Handler customHandler = new Handler();
//	private RealtimeChartSurfaceView glChart;
//	private LinearLayout glChartContainer;
//	Timer t = new Timer();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_b);
//		glChartContainer = (LinearLayout) findViewById(R.id.chartView);
//		glChart = new RealtimeChartSurfaceView(this);
//		glChartContainer.addView(glChart, new ViewGroup.LayoutParams(
//				ViewGroup.LayoutParams.MATCH_PARENT,
//				ViewGroup.LayoutParams.MATCH_PARENT));
//		glChartContainer.setVisibility(View.VISIBLE);
//		// Set the schedule function and rate
//		updateTimerThread.run();
//		mXaxisAccel = (TextView) findViewById(R.id.xbox);
//		mYaxisAccel = (TextView) findViewById(R.id.ybox);
//		mZaxisAccel = (TextView) findViewById(R.id.zbox);
//		mXaxisGyro = (TextView) findViewById(R.id.xboxo);
//		mYaxisGyro = (TextView) findViewById(R.id.yboxo);
//		mZaxisGyro = (TextView) findViewById(R.id.zboxo);
//		TextView t = (TextView) findViewById(R.id.textSupervised);
//		t.setText("Supervised Learning");
//		Button b = (Button) findViewById(R.id.supervisedToMain);
//		intent = getIntent();
//		b.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				getIntent().getFloatExtra("START_TIME", 0f);
//				finish();
//			}
//		});
//
//		// glBegin(GL_POINTS);
//		// for(float i = 0; i <= 100; ){
//		// glVertex2f(i,i);
//		// i+=0.01;
//		// }
//		// glEnd();
//		//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	int j = 0;
//	private Runnable updateTimerThread = new Runnable() {
//		public void run() {
//			try {
//				Thread.sleep(30);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			float[] f1 = new float[350];
//			int frequency = 10;
//
//			for (int i = 0; i < f1.length; i++) {
//				// Generate example signal
//				f1[i] = (float) i
//						* 1.0f
//						* (float) Math.sin((float) i
//								* ((float) (2 * Math.PI) * (float) j * 1
//										* frequency / 44100));
//			}
//			j++;
//			if (j > 400) {
//				j = 0;
//			}
//
//			glChart.setChartData(f1);
//			customHandler.postDelayed(this, 0);
//		}
//	};
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
////		if (id == R.id.action_settings) {
////			return true;
////		}
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
////
//// class ThirdGLEventListener implements GLEventListener {
////
//// /**
//// * Interface to the GLU library.
//// */
//// private GLU glu;
////
//// /**
//// * Take care of initialization here.
//// */
//// public void init(GLAutoDrawable gld) {
//// GL gl = gld.getGL();
//// glu = new GLU();
////
//// gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
////
//// gl.glViewport(-250, -150, 250, 150);
//// gl.glMatrixMode(GL.GL_PROJECTION);
//// gl.glLoadIdentity();
//// glu.gluOrtho2D(-250.0, 250.0, -150.0, 150.0);
//// }
////
//// /**
//// * Take care of drawing here.
//// */
//// public void display(GLAutoDrawable drawable) {
////
//// GL gl = drawable.getGL();
////
//// gl.glClear(GL.GL_COLOR_BUFFER_BIT);
////
//// /*
//// * put your code here
//// */
////
//// drawLine(gl, 0, 0, 100, 100);
////
//// }
////
//// public void reshape(GLAutoDrawable drawable, int x, int y, int width,
//// int height) {
//// }
////
//// public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
//// boolean deviceChanged) {
//// }
////
//// public void reshape(GLAutoDrawable drawable, int x, int y, int width,
//// int height) {
//// }
////
//// public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
//// boolean deviceChanged) {
//// }
////
//// private void drawLine(GL gl, int x1, int y1, int x2, int y2) {
//// gl.glPointSize(1.0f);
//// gl.glBegin(GL.GL_POINTS);
////
//// int samples = 100;
//// float dx = (x2 - x1) / (float) samples;
//// float dy = (y2 - y1) / (float) samples;
////
//// for (int i = 0; i < samples; i++) {
//// gl.glVertex2f(i * dx, i * dy);
//// }
////
//// gl.glEnd();// end drawing of points
//// }
//// }
