package edu.iastate.cs.lanc.ObjectTracking;

import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.MobileDeviceStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MDLocationListener implements LocationListener {

	private Handler simpleViewHandler;
	private Handler mapViewHandler;
	private Handler serviceManagerHandler;
	private MDApplication myApp;

	public MDLocationListener(MDApplication myApp,Handler simpleViewHandler,Handler mapViewHandler,Handler serviceManagerHandler) {

		this.myApp = myApp;
		this.simpleViewHandler = simpleViewHandler;
		this.mapViewHandler = mapViewHandler;
		this.serviceManagerHandler = serviceManagerHandler;
		
	}

	@Override
	public void onLocationChanged(Location loc) {

		MobileDeviceStatus myStatus = myApp.getAmd().getMyStatus();
		
//		System.out.println("Current address: "+ loc.getLatitude() + " " + loc.getLongitude());
		
		if(loc != null){
			
			myStatus.getMyLocation().setX(loc.getLatitude());
			myStatus.getMyLocation().setY(loc.getLongitude());
			myStatus.getMyLocation().setZ(loc.getAltitude());
			
			myStatus.getMyLocation().setT(loc.getTime());
			
		}
		
		
		
		if (simpleViewHandler != null) {	
			Message msg = Message.obtain(simpleViewHandler,APP_CONST.MD_LOCATION_CHANGED,APP_CONST.MESSAGE_OPTION_FIELD,APP_CONST.MESSAGE_OPTION_FIELD);
			msg.setTarget(simpleViewHandler);
			msg.sendToTarget();
			System.out.println("Current address: "+ loc.getLatitude() + " " + loc.getLongitude());
		}else{
			System.out.println("SimpleView doesn't work");
		}
		if (mapViewHandler != null) {
			Message msg2 = Message.obtain(simpleViewHandler,APP_CONST.MD_LOCATION_CHANGED,APP_CONST.MESSAGE_OPTION_FIELD,APP_CONST.MESSAGE_OPTION_FIELD);
			msg2.setTarget(mapViewHandler);
			msg2.sendToTarget();
		}
		if(serviceManagerHandler != null){
			
			Message msg3 = Message.obtain(simpleViewHandler,APP_CONST.MD_LOCATION_CHANGED,APP_CONST.MESSAGE_OPTION_FIELD,APP_CONST.MESSAGE_OPTION_FIELD);
			serviceManagerHandler.sendMessage(msg3);
		}
		
		
		
		
	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

}
