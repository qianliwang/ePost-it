package edu.iastate.cs.lanc.ObjectTracking;

import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.MobileDevice;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.MobileDeviceStatus;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {
	
	private final long SPLASH_DISPLAY_LENGTH = 2000;
	private final int FIRST_TIME_RUN = 100;
	private final int REGISTRATION_READY = 200;
	private long ms = 0;
	private boolean splashActive = true;
	private boolean paused = false;
	
	private LocationManager locManager;
	private MDApplication mdApplication;
	
	private Handler regHandler;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        mdApplication = (MDApplication)getApplication();
        
        regHandler = new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		
        		if(msg.what == FIRST_TIME_RUN){
        			ShowRegistrationDialog();
        		}
        		
        	}
        };
        
        ServiceManager sm = new ServiceManager(mdApplication,locManager,GPSActivity.getLocationListenerHandler(),BasicMapActivity.getLocationListenerHandler());
        sm.run();
        
        Thread mythread = new Thread(){
        	public void run(){
        		
        		try{
        			while(splashActive && ms< SPLASH_DISPLAY_LENGTH){
        				if(!paused){
        					ms = ms + 100;
        				}
        				sleep(100);
        			}
        		}
        		catch(Exception e){
        			
        		}finally{
        			
        			if(mdApplication.isFirstTimeRun()){
        	        	Message msg = Message.obtain(regHandler,FIRST_TIME_RUN);
        	        	regHandler.sendMessage(msg);
        	        }else{
        	        	Intent intent = new Intent(SplashScreenActivity.this,MainTabActivity.class);
            			startActivity(intent);
        	        }
        				
        		}
        		
        	}
        };
        mythread.start();
    }
    
	private void ShowRegistrationDialog() {

		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_ialog_registration, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Registration");

		// set view
		alertDialogBuilder.setView(textEntryView);

		final EditText editEmail = (EditText) textEntryView
				.findViewById(R.id.registration_email_editText);
		final EditText editPhoneNumber = (EditText) textEntryView
				.findViewById(R.id.registration_phonenumber_editText);
		final EditText registration_serverIP = (EditText) textEntryView
				.findViewById(R.id.registration_serverIP_editText);
		registration_serverIP.setHint(APP_CONST.SERVER1);

		// set dialog message
		alertDialogBuilder
				.setMessage("Thanks for using our service.Please register the User ID by your EMAIL.");
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Register",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						String email = editEmail.getText().toString();
						String phoneNumber = editPhoneNumber.getText()
								.toString();
						String reg_serverIP = registration_serverIP.getText()
								.toString();

						// For test the REQUEST_DID;
						// myStatus.setMyID(111111);
						if ((email.equals("")) && (phoneNumber.equals(""))) {

							AlertDialog.Builder alertDialog = new AlertDialog.Builder(
									SplashScreenActivity.this);
							alertDialog.setTitle("Error");
							alertDialog
									.setMessage("Please input Both the Email and Phone Number to register. Thank you.");

							alertDialog.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											ShowRegistrationDialog();
										}
									});

							alertDialog.show();

						} else {

							System.out.println(email);
							System.out.println(phoneNumber);

							if (reg_serverIP.equals("")) {
								reg_serverIP = APP_CONST.SERVER1;
							}

							mdApplication.getAmd().getMyStatus();
							
							int tempID = mdApplication.getAmd().RequestDID(reg_serverIP,
									mdApplication.getAmd().getMyStatus().getMyCurServerPort(), email,
									phoneNumber);

							System.out.println("new ID is " + tempID);

	//						myStatus.setMyID(tempID);

							if (tempID >= 0) {

								SharedPreferences sharedPreferences = getSharedPreferences(
										"Setting", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = sharedPreferences
										.edit();

								editor.putString(APP_CONST.DID,
										String.valueOf(tempID));

								editor.commit();

							}
							
							mdApplication.setFirstTimeRun(false);
							Intent intent = new Intent(SplashScreenActivity.this,MainTabActivity.class);
		        			startActivity(intent);

						}

					}
				});
		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
}