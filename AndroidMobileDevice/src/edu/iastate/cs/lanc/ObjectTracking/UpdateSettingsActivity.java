package edu.iastate.cs.lanc.ObjectTracking;

import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.LocationUpdatePolicy;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.MobileDeviceStatus;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class UpdateSettingsActivity extends Activity{

	EditText editMyZoneRadius;
	EditText editMyLoad;
	EditText editMinTime;
	EditText editMinDistance;
	EditText editMyCurrentServerIP;
	EditText editTestTimeDifference;
	
	RadioGroup radioPowerMode;
	RadioButton power_inCharging;
	RadioButton power_battery;
    RadioGroup radioUpdatePolicy;
    RadioButton policy_onDemand;
    RadioButton policy_periodic;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_settings);
		showHint();
		addListenerOnRadioButton();
	}
    public void onSave(View view){
    	
    	Intent intent = getIntent();
    	
    	intent.putExtra("myZoneRadius", editMyZoneRadius.getText().toString());
    	intent.putExtra("myLoad", editMyLoad.getText().toString());
    	intent.putExtra("minTime", editMinTime.getText().toString());
    	intent.putExtra("minDistance", editMinDistance.getText().toString());
    	intent.putExtra("myServerIP",editMyCurrentServerIP.getText().toString());
    	intent.putExtra("testTimeDifference", editTestTimeDifference.getText().toString());
    	
    	int selectedRadioID = radioUpdatePolicy.getCheckedRadioButtonId();
    	RadioButton temp = (RadioButton) findViewById(selectedRadioID);
    	
    	if(temp.getText().toString().equals("ON_DEMAND")){
    		intent.putExtra("myUpdatePolicy", String.valueOf(LocationUpdatePolicy.ON_DEMAND));
    	}else if(temp.getText().toString().equals("PERIODIC")){
    		intent.putExtra("myUpdatePolicy", String.valueOf(LocationUpdatePolicy.PERIODIC));
    	}
    	
    	this.setResult(RESULT_OK,intent);
    	
    	finish();
    	
    }
    public void onCancel(View view){
    	
    	finish();
    }
    public void showHint(){
    	
 	SharedPreferences sharedPreferences = getSharedPreferences("Setting",Context.MODE_PRIVATE);
    	
    	String tempString = "";
 //   	int tempInt = 0;
    	
    	editMyZoneRadius = (EditText) findViewById(R.id.editTextMyZone);
    	tempString = sharedPreferences.getString(APP_CONST.MY_ZONE_RADIUS, "");
    	editMyZoneRadius.setHint(tempString);
    	
    	editMyLoad = (EditText) findViewById(R.id.editTextMyLoad);
    	tempString = sharedPreferences.getString(APP_CONST.MY_LOAD, "");
    	editMyLoad.setHint(tempString);
    	
    	editMinTime = (EditText) findViewById(R.id.editTextMinTime);
    	tempString = sharedPreferences.getString(APP_CONST.MY_LOCATION_UPDATE_MIN_TIME, "");
    	editMinTime.setHint(tempString);
    	
    	editMinDistance = (EditText) findViewById(R.id.editTextMinDistance);
    	tempString = sharedPreferences.getString(APP_CONST.MY_LOCATION_UPDATE_MIN_DISTANCE, "");
    	editMinDistance.setHint(tempString);
    	
    	editMyCurrentServerIP = (EditText) findViewById(R.id.editTextServerInfo);
    	tempString = sharedPreferences.getString(APP_CONST.MY_CURRENT_SERVER_IP, "");
    	editMyCurrentServerIP.setHint(tempString);
    	
    	editTestTimeDifference = (EditText) findViewById(R.id.editTextTestDifference);
    	
    	
    }
    
    private void addListenerOnRadioButton(){
    	
    	radioPowerMode = (RadioGroup) findViewById(R.id.radioPowerMode);
    	radioUpdatePolicy = (RadioGroup) findViewById(R.id.radioUpdatePolicy);
    	
    	power_inCharging = (RadioButton) findViewById(R.id.radioInCharging);
    	power_battery = (RadioButton) findViewById(R.id.radioBattery);
    	
    	policy_onDemand = (RadioButton) findViewById(R.id.radioOnDemand);
    	policy_periodic = (RadioButton) findViewById(R.id.radioPeriodic);
    	
    	MDApplication myApp = (MDApplication) getApplication();
		MobileDeviceStatus myStatus = myApp.getAmd().getMyStatus();
    	
    	if(myStatus.getMyBatteryStatus() == APP_CONST.POWERMODE_INCHARING){
    		
    		power_inCharging.setChecked(true);
    		
    	}else{
    		power_battery.setChecked(true);
    	}
    	
    	policy_onDemand.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if(policy_onDemand.isChecked()){
					
					editMinTime.setEnabled(false);
					editMinTime.setClickable(false);
					editMinTime.setFocusable(false);
					editMinTime.setFocusableInTouchMode(false);
					editMinDistance.setFocusable(false);
					editMinDistance.setFocusableInTouchMode(false);
					editMinDistance.setEnabled(false);
					editMinDistance.setClickable(false);
					
				}
				
			}
    		
    	});
    	policy_periodic.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if(policy_periodic.isChecked()){
					
					editMinTime.setEnabled(true);
					editMinTime.setClickable(true);
					editMinTime.setFocusable(true);
					editMinTime.setFocusableInTouchMode(true);
					editMinDistance.setFocusable(true);
					editMinDistance.setFocusableInTouchMode(true);
					editMinDistance.setEnabled(true);
					editMinDistance.setClickable(true);
					
				}
				
			}
    		
    	});
    	
    }
	
}
