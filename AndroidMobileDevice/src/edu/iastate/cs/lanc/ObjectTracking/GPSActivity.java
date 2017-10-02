package edu.iastate.cs.lanc.ObjectTracking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/*
 import object.POJO.Geopage;
 import object.POJO.MobileDeviceStatus;
 import object.POJO.Region;
 import research.gps.self.UpdateSettingsActivity;
 */

import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.*;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.*;
import edu.iastate.cs.lanc.ObjectTracking.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;



public class GPSActivity extends Activity {

	public static final int SETTINGS_UPDATE_REQUEST_CODE = 1;
	public static final int MAP_VIEW_REQUEST_CODE = 2;
	public static final int GEOPAGE_LIST_REQUEST_CODE = 3;

	// public static final int TIME_DIFFERENCE = 5;
	public static long BATTERY_FULL_TIME = System.currentTimeMillis();
	public static long UpdateTimeDiff = 600000;

	TextView textMyID;
	static TextView textLat;
	static TextView textLong;
	static TextView textAlt;
	static TextView textTime;
	TextView textBatteryStatus;
	TextView textMobileDeviceStatus;
    
	private static boolean isInFront;
	
	static int counter = 0;

	TimeStampedLocation tsl = new TimeStampedLocation(0, 0, 0, 0);
	long time;

	boolean serviceStopped = false;
	boolean firstTimeOfDay = true;
	boolean startToWriteBatteryStatus = false;
	static int timeDifference = 0;
	long lastRecordedTime = 0;
	// boolean DIDNeedToRequest = false;

	static MobileDeviceStatus myStatus;

	int currentBatteryStatus = -1;

	private static Handler locationListenerHandler;
	
	private static Context myContext;
	
	BroadcastReceiver myBatteryReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		

		isInFront = true;
		MDApplication myApp = (MDApplication) getApplication();
		myStatus = myApp.getAmd().getMyStatus();

		myContext = getApplicationContext();
		
		SetUpViews();
		TestGeopageList();
		
		myBatteryReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				// RecordDate("CurrentTime","BatteryStatus");

				if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

					int status = intent.getIntExtra("status",
							BatteryManager.BATTERY_STATUS_UNKNOWN);

					if (status == BatteryManager.BATTERY_STATUS_CHARGING) {

						// textBatteryStatus.setText("Level:"
						// + String.valueOf(intent.getIntExtra("level", 0))
						// + "%;" + "In Charging...");

						currentBatteryStatus = intent.getIntExtra("level", 0);
						textBatteryStatus.setText("Level:"
								+ String.valueOf(currentBatteryStatus) + "%"
								+ "In Charing...");

						int rawlevel = intent.getIntExtra(
								BatteryManager.EXTRA_LEVEL, -1);
						int scale = intent.getIntExtra(
								BatteryManager.EXTRA_SCALE, -1);
						int level = -1;
						if (rawlevel >= 0 && scale > 0) {
							level = (rawlevel * 100) / scale;
						}

						// RecordDate("CurrentTime","BatteryStatus");
						if (level % 5 == 0) {
							long currentTime = System.currentTimeMillis();
							RecordDate(currentTime,
									(currentTime - BATTERY_FULL_TIME),
									Integer.toString(level));
						}
						// currentBatteryStatus = APP_CONST.POWERMODE_INCHARING;

					} else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {

						currentBatteryStatus = intent.getIntExtra("level", 0);
						textBatteryStatus.setText("DISCHARGING.Level:"
								+ String.valueOf(currentBatteryStatus) + "%");

						int rawlevel = intent.getIntExtra(
								BatteryManager.EXTRA_LEVEL, -1);
						int scale = intent.getIntExtra(
								BatteryManager.EXTRA_SCALE, -1);
						int level = -1;
						if (rawlevel >= 0 && scale > 0) {
							level = (rawlevel * 100) / scale;
						}

						if (level % 5 == 0) {
							long currentTime = System.currentTimeMillis();
							RecordDate(currentTime,
									(currentTime - BATTERY_FULL_TIME),
									Integer.toString(level));
						}

					} else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {

						currentBatteryStatus = intent.getIntExtra("level", 0);
						textBatteryStatus.setText("NOTCHARGING.Level:"
								+ String.valueOf(currentBatteryStatus) + "%");

						int rawlevel = intent.getIntExtra(
								BatteryManager.EXTRA_LEVEL, -1);
						int scale = intent.getIntExtra(
								BatteryManager.EXTRA_SCALE, -1);
						int level = -1;
						if (rawlevel >= 0 && scale > 0) {
							level = (rawlevel * 100) / scale;
						}

						if (level % 5 == 0) {
							long currentTime = System.currentTimeMillis();
							RecordDate(currentTime,
									(currentTime - BATTERY_FULL_TIME),
									Integer.toString(level));
						}
					} else {

						textBatteryStatus.setText("Unknown Status.");

						currentBatteryStatus = intent.getIntExtra("level", 0);
						textBatteryStatus.setText("UNKNOWNSTATUS.Level:"
								+ String.valueOf(currentBatteryStatus) + "%");

						int rawlevel = intent.getIntExtra(
								BatteryManager.EXTRA_LEVEL, -1);
						int scale = intent.getIntExtra(
								BatteryManager.EXTRA_SCALE, -1);
						int level = -1;
						if (rawlevel >= 0 && scale > 0) {
							level = (rawlevel * 100) / scale;
						}

						if (level % 5 == 0) {
							long currentTime = System.currentTimeMillis();
							RecordDate(currentTime,
									(currentTime - BATTERY_FULL_TIME),
									Integer.toString(level));
						}
					}

				}

				myStatus.setMyBatteryStatus(currentBatteryStatus);

			}

		};

		this.registerReceiver(this.myBatteryReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		if(this.locationListenerHandler == null){
			
			this.locationListenerHandler =  new Handler(){
				
				@Override
				public void handleMessage(Message msg){
					
					UpdateUI(msg);
				}
				
			};
		}

	}

	private void SetUpViews() {

		textLat = (TextView) findViewById(R.id.textLat);
		textLong = (TextView) findViewById(R.id.textLong);
		textAlt = (TextView) findViewById(R.id.textAlt);
		textTime = (TextView) findViewById(R.id.textTime);

		textMyID = (TextView) findViewById(R.id.textMyID);
		textMyID.setText(Integer.toString(myStatus.getMyID()));
		
		textBatteryStatus = (TextView) findViewById(R.id.textBatteryStatus);
		textMobileDeviceStatus = (TextView) findViewById(R.id.textMobileDeviceStatus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		MenuInflater menuInflater = getMenuInflater();

		menuInflater.inflate(R.menu.menu, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		menu.clear();
		getMenuInflater().inflate(R.menu.menu, menu);
		MenuItem stop_service = menu.findItem(R.id.stop_services);
		if (serviceStopped) {
			stop_service.setTitle("Start Service");
		} else {

			stop_service.setTitle("Stop Service");
		}

		MenuItem inbox = menu.findItem(R.id.geopage_list);
		inbox.setTitle("Inbox(" + myStatus.getMyGeopageList().size() + ")");

		return super.onPrepareOptionsMenu(menu);

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		if (item.getItemId() == R.id.update_status_item) {

			Intent intent = new Intent(this, UpdateSettingsActivity.class);

			startActivityForResult(intent, SETTINGS_UPDATE_REQUEST_CODE);

			return true;
		} else if (item.getItemId() == R.id.stop_services) {
			if(!serviceStopped){
				Message msg = Message.obtain(ServiceManager.serviceManagerHandler,APP_CONST.STATUS_NETWORK_GPS_BOTH_STOP);
				ServiceManager.serviceManagerHandler.sendMessage(msg);
				serviceStopped = true;
			}else{
				Message msg = Message.obtain(ServiceManager.serviceManagerHandler,APP_CONST.STATUS_NETWORK_GPS_BOTH_RESTART);
				ServiceManager.serviceManagerHandler.sendMessage(msg);
				serviceStopped = false;
			}
			
			
			
		} else if (item.getItemId() == R.id.geopage_list) {
			Intent geopageListIntent = new Intent(this,
					AndroidGeopageListActivity.class);
			startActivityForResult(geopageListIntent, GEOPAGE_LIST_REQUEST_CODE);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		SharedPreferences sharedPreferences = getSharedPreferences("Setting",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		if (requestCode == SETTINGS_UPDATE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				// boolean minTimeChanged = false;
				// boolean minDistanceChanged = false;

				String myZoneRadius = data.getStringExtra("myZoneRadius");
				String myLoad = data.getStringExtra("myLoad");

				String minTime = data.getStringExtra("minTime");
				String minDistance = data.getStringExtra("minDistance");

				String myCurrentServerIP = data.getStringExtra("myServerIP");
				String myLocationUpdatePolicy = data
						.getStringExtra("myUpdatePolicy");

				String testTimeDifference = data
						.getStringExtra("testTimeDifference");

				if (testTimeDifference.equals("")) {
					MobileDevice.setTestModeDifference(0);
					startToWriteBatteryStatus = true;
				} else {
					timeDifference = Integer.parseInt(testTimeDifference);
					MobileDevice.setTestModeDifference(timeDifference);
					startToWriteBatteryStatus = true;
				}

				int tempInt = 0;

				if (!myLoad.equals("")) {

					// tempInt = Integer.parseInt(myLoad);
					editor.putString(APP_CONST.MY_LOAD, myLoad);

				}
				if (!myZoneRadius.equals("")) {

					editor.putString(APP_CONST.MY_ZONE_RADIUS, myZoneRadius);

				}
				if (!minTime.equals("")) {

					tempInt = Integer.parseInt(minTime);
					editor.putString(APP_CONST.MY_LOCATION_UPDATE_MIN_TIME,
							minTime);
					// if(myStatus.getMyLocationUpdatePolicy().getTimeDuration()
					// != tempInt){
					// minTimeChanged = true;
					// }

				}
				if (!minDistance.equals("")) {

					tempInt = Integer.parseInt(minDistance);
					editor.putString(APP_CONST.MY_LOCATION_UPDATE_MIN_DISTANCE,
							minDistance);
					// if(myStatus.getMyLocationUpdatePolicy().getUpdateDeviation()
					// != tempFloat){
					// minDistanceChanged = true;
					// }

				}
				if (!myCurrentServerIP.equals("")) {

					editor.putString(APP_CONST.MY_CURRENT_SERVER_IP,
							myCurrentServerIP);

				}

				if (myLocationUpdatePolicy.equals(String
						.valueOf(LocationUpdatePolicy.ON_DEMAND))) {
					editor.putString(APP_CONST.MY_LOCATION_UPDATE_POLICY,
							myLocationUpdatePolicy);
				} else if (myLocationUpdatePolicy.equals(String
						.valueOf(LocationUpdatePolicy.PERIODIC))) {
					editor.putString(APP_CONST.MY_LOCATION_UPDATE_POLICY,
							myLocationUpdatePolicy);
				}

				editor.commit();

			}
		}
	}

	private void RecordDate(long timeStamp, long timeDiff, String batteryStatus) {

		if (startToWriteBatteryStatus) {
			// int batterySta = Integer.parseInt(batteryStatus);
			// if((batterySta%5) ==0){

			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/Epost");
			if (!dir.exists())
				dir.mkdirs();
			File file = new File(dir, "BatteryTracking_" + timeDifference
					+ ".txt");

			String test = timeStamp + " " + timeDiff + " " + batteryStatus;

			BufferedWriter bw;

			try {
				bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(test);
				bw.newLine();
				bw.flush();
				bw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// }
			// lastRecordedTime = timeStamp;
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.gps);
	}


	@Override
	protected void onDestroy() {
		// this.unregisterReceiver(this.myBatteryReceiver);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// this.unregisterReceiver(this.myBatteryReceiver);
		super.onPause();
		isInFront = false;
	}

	@Override
	protected void onStop() {
		// this.unregisterReceiver(this.myBatteryReceiver);
		super.onStop();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInFront = true;
	}

	public static Handler getLocationListenerHandler() {
		
		if(locationListenerHandler == null){
			
			locationListenerHandler = new Handler(){
				
				@Override
				public void handleMessage(Message msg){
					
					UpdateUI(msg);
		
				}
				
			};
			
		}
		
		return locationListenerHandler;
	}
	
	private static void UpdateUI(Message msg){
		
		if(isInFront){
			
			switch(msg.what){
			
			case APP_CONST.MD_LOCATION_CHANGED:
				textLat.setText(Double.toString(myStatus.getMyLocation().getX()));
				textLong.setText(Double.toString(myStatus.getMyLocation().getY()));
				textAlt.setText(Double.toString(myStatus.getMyLocation().getZ()));
//				textTime.setText((new Date()).toString() + counter++);
				textTime.setText((new Date()).toString());
				break;
				
            case APP_CONST.MD_GEOPAGE_TRIGGERED:
				
				Vibrator vibrator = (Vibrator) myContext.getSystemService(VIBRATOR_SERVICE);
				vibrator.vibrate(800);
				
				String webURL = myStatus.getMyGeopageList().get(msg.arg1).getURL();
				
				Intent webPageIntent = new Intent(
						Intent.ACTION_VIEW, Uri.parse(webURL));
				webPageIntent
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// context.startActivity(webPageIntent);
				myContext.startActivity(webPageIntent);
				
				break;	
				default:
			}
				
		}
			
	}
    private void TestGeopageList(){
		
		MobileDeviceStatus myStatus = ((MDApplication) getApplication()).getAmd().getMyStatus();
		Geopage g1 = new Geopage();
		g1.setRegion(new Region(43,-90,500));
		g1.setURL("http://www.cnn.com");
		
		Geopage g2 = new Geopage();
		g2.setRegion(new Region(42,-91,500));
		g2.setURL("http://www.google.com");
		
		Geopage g3 = new Geopage();
		g3.setRegion(new Region(41,-92,500));
		g3.setURL("http://www.bing.com");
		
		if(myStatus.getMyGeopageList() != null){
			myStatus.getMyGeopageList().add(g1);
			myStatus.getMyGeopageList().add(g2);
			myStatus.getMyGeopageList().add(g3);
		}else{
			System.out.println("Can't load GeopageList!!!");
		}
		
	}
}
