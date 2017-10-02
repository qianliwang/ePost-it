package edu.iastate.cs.lanc.ObjectTracking;

import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.MobileDevice;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.LocationUpdatePolicy;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.MobileDeviceStatus;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.Toast;

public class MDApplication extends Application{

	private AndroidMobileDevice amd;
	
	private static MDApplication instance;
	
	private boolean firstTimeRun;
	
//	private boolean firstTimeOfDay = true;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
	    super.onCreate();
	    instance = this;
	    instance.InitializaeInstance();
	}

	@Override
	public void onLowMemory() {
	    super.onLowMemory();
	}

	@Override
	public void onTerminate() {
	    super.onTerminate();
	}
	
	protected void InitializaeInstance(){
		
		amd = new AndroidMobileDevice();
		LoadPreference();
		
	}
	
	public boolean isFirstTimeRun(){
		
		return firstTimeRun;
	}
	public void setFirstTimeRun(boolean status){
		
		this.firstTimeRun = status;
		SharedPreferences sharedPreferences = getSharedPreferences("Setting",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		editor.putBoolean(APP_CONST.FIRST_RUN,status);
		editor.commit();
	
	}
	
	public MobileDevice getMobileDevice(){
		return amd;
	}
	
	private void InitializePreference() {

		SharedPreferences sharedPreferences = getSharedPreferences("Setting",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		// editor.putString(APP_CORecordDate("CurrentTime","BatteryStatus");NST.DID,
		// String.valueOf(-1));
		editor.putString(APP_CONST.MY_LOAD, String.valueOf(APP_CONST.MY_LOAD_VALUE));
		editor.putString(APP_CONST.MY_ZONE_RADIUS, String.valueOf(APP_CONST.MY_ZONE_RADIUS_VALUE));
		editor.putString(APP_CONST.MY_LOCATION_UPDATE_POLICY,
				String.valueOf(LocationUpdatePolicy.PERIODIC));
		editor.putString(APP_CONST.MY_LOCATION_UPDATE_MIN_TIME,
				String.valueOf(APP_CONST.MY_LOCATION_UPDATE_MIN_TIME_VALUE));
		editor.putString(APP_CONST.MY_LOCATION_UPDATE_MIN_DISTANCE,
				String.valueOf(APP_CONST.MY_LOCATION_UPDATE_MIN_DISTANCE_VALUE));
		editor.putString(APP_CONST.MY_CURRENT_SERVER_IP, APP_CONST.SERVER1);
		editor.putString(APP_CONST.MY_CURRENT_SERVER_PORT,
				APP_CONST.SERVER1_PORT);

		editor.commit();
	}
	
	private void LoadPreference() {

		SharedPreferences sharedPreferences = getSharedPreferences("Setting",
				Context.MODE_PRIVATE);
		
		MobileDeviceStatus myStatus = new MobileDeviceStatus();

		String tempString = "";
		
		firstTimeRun = sharedPreferences.getBoolean(APP_CONST.FIRST_RUN, true);
		
		if (firstTimeRun) {;

			InitializePreference();

		}
		    tempString = sharedPreferences.getString(APP_CONST.DID, "0");
			myStatus.setMyID(Integer.parseInt(tempString));
			myStatus.setMyCurServerIP(sharedPreferences.getString(
					APP_CONST.MY_CURRENT_SERVER_IP, ""));
			myStatus.setMyCurServerPort(Integer.parseInt(sharedPreferences
					.getString(APP_CONST.MY_CURRENT_SERVER_PORT, "")));
			myStatus.getMyZone().setRadius(
					Double.parseDouble(sharedPreferences.getString(
							APP_CONST.MY_ZONE_RADIUS, "")));
			myStatus.getMyLocationUpdatePolicy().setUpdatePolicy(
					(byte) Integer.parseInt(sharedPreferences.getString(
							APP_CONST.MY_LOCATION_UPDATE_POLICY, "")));
			myStatus.getMyLocationUpdatePolicy().setTimeDuration(
					Integer.parseInt(sharedPreferences.getString(
							APP_CONST.MY_LOCATION_UPDATE_MIN_TIME, "")));
			myStatus.getMyLocationUpdatePolicy().setUpdateDeviation(
					Integer.parseInt(sharedPreferences.getString(
							APP_CONST.MY_LOCATION_UPDATE_MIN_DISTANCE, "")));

		amd.setMyStatus(myStatus);
	}

	public AndroidMobileDevice getAmd() {
		return amd;
	}

	public void setAmd(AndroidMobileDevice amd) {
		this.amd = amd;
	}
	
}
