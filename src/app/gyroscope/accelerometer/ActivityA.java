//package app.gyroscope.accelerometer;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.view.Menu;
//import android.view.View;
//import android.widget.Chronometer;
//
//public class ActivityA extends Activity {
// 
//	long timeElapsed;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
////		setContentView(R.layout.activity_main);
//
//		Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer1);
//		timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
//		if (timeElapsed != 0) {
//			chronometer.getBase();
//			chronometer.start();
//		}
////
////		TextView t = (TextView) findViewById(R.id.textView1);
////		t.setText("TextView t=(TextView)findViewById(R.id.textView1);    t.setText('This is your activity');     Buttonb=(Button)findViewById(R.id.button1);      b.setOnClickListener(new OnClickListener() {       public void onClick(View v) {		   Intent intent = new Intent(getBaseContext(), ActivityB.class);				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);	    getIntent().putExtra('START_TIME', timeElapsed);             startActivity(intent);	}})                 On the second activity           Button b = (Button) findViewById(R.id.button2);		Intent intent = getIntent();		b.setOnClickListener(new OnClickListener() {			public void onClick(View v) {				getIntent().getFloatExtra('START_TIME', 0f);           finish();			}		});");
////		Button b = (Button) findViewById(R.id.button1);
////		b.setOnClickListener(new OnClickListener() {
////			public void onClick(View v) {
////				Intent intent = new Intent(getBaseContext(), ActivityB.class);
////				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
////				getIntent().putExtra("START_TIME", timeElapsed);
////				startActivity(intent);
////			}
////		});
////		Button c = (Button) findViewById(R.id.button2);
////		c.setOnClickListener(new OnClickListener() {
////			public void onClick(View v) {
////				Intent intent = new Intent(getBaseContext(), ActivityC.class);
////				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
////				getIntent().putExtra("START_TIME", timeElapsed);
////				startActivity(intent);
////			}
////		});
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
////	@Override
////	public boolean onOptionsItemSelected(MenuItem item) {
////		// Handle action bar item clicks here. The action bar will
////		// automatically handle clicks on the Home/Up button, so long
////		// as you specify a parent activity in AndroidManifest.xml.
////		int id = item.getItemId();
////		// if (id == R.id.action_settings) {
////		// return true;
////		// }
////		return super.onOptionsItemSelected(item);
////	}
//
//	public void onPause(View v) {
//
//	}
//
//	public void onResume(View v) {
//
//	}
// }
