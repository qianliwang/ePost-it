package edu.iastate.cs.lanc.ObjectTracking;

import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.MobileDevice;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.Geopage;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.LocationUpdatePolicy;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ServiceManager implements Runnable {
//public class ServiceManager{

	private LocationManager locManager;
	private LocationListener locListener;
	private MDApplication myApp;
//	private MobileDevice mobileDevice;

	private Thread networkThread;
	
	private Thread locUpdateOnDemandThread;
	
	public static Handler serviceManagerHandler;
	
	private Handler refSimpleViewHandler;
	
	private Handler refMapViewHandler;

	public ServiceManager(MDApplication myApp,LocationManager locManager, Handler simpleViewHandler,Handler mapViewHandler) {
		
		this.myApp = myApp;
		this.locManager = locManager;
        InitialHandler();
        this.refSimpleViewHandler = simpleViewHandler;
        this.refMapViewHandler = mapViewHandler;
        
		locListener = new MDLocationListener(myApp,simpleViewHandler,mapViewHandler,serviceManagerHandler);
	}

	public boolean StartLocationService() {

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setCostAllowed(false);
		
		String providerName = locManager.getBestProvider(criteria, true);
		
		if (providerName != null){
			
			locManager.requestLocationUpdates(providerName, myApp.getAmd().getMyStatus().getMyLocationUpdatePolicy().getTimeDuration(), myApp.getAmd().getMyStatus().getMyLocationUpdatePolicy().getUpdateDeviation(),
					locListener);
//			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, myApp.getAmd().getMyStatus().getMyLocationUpdatePolicy().getTimeDuration(), myApp.getAmd().getMyStatus().getMyLocationUpdatePolicy().getUpdateDeviation(),
//					locListener);
			
			if(locManager.getLastKnownLocation(providerName)!= null){
				
				myApp.getAmd().getMyStatus().getMyLocation().setX(locManager.getLastKnownLocation(providerName).getLatitude());
				myApp.getAmd().getMyStatus().getMyLocation().setY(locManager.getLastKnownLocation(providerName).getLongitude());
				myApp.getAmd().getMyStatus().getMyLocation().setZ(locManager.getLastKnownLocation(providerName).getLongitude());
			}
			
			

		}else{
			
		}
		
		
				
		System.out.println("Location Service is running!!");
		return true;
	}

	public boolean StopLocationService() {

		locManager.removeUpdates(locListener);
		return true;
	}

	public boolean StartNetworkService(){
		
		networkThread = new Thread(){
			public void run(){
				myApp.getAmd().init();
			}
		};
		
		networkThread.start();
		
		System.out.println("Network Service is running!!");
		
		return true;
	}

	public boolean StopNetworkService(){
		
//		networkThread.stop();
		return true;
	}
	@Override
	public void run() {
				
		StartLocationService();
		StartNetworkService();

	}
	
	private void InitialHandler(){
		
		System.out.println("Initialize Handler!!!");
		
		 serviceManagerHandler = new Handler(){
				
				@Override
				public void handleMessage(Message msg){
					
					switch(msg.what){
					case APP_CONST.MD_LOCATION_CHANGED:
						
						if(myApp.getAmd().getMyStatus().getMyLocationUpdatePolicy().getUpdatePolicy() == LocationUpdatePolicy.ON_DEMAND){
							locUpdateOnDemandThread = new Thread(){
					            public void run(){
					        		
					        		try{
					        			while(true){
					        				
					        				myApp.getAmd().UpdateLocation();
					        				Thread.sleep(5000);
					        				
					        			    }
					        			}
					        		catch(Exception e){
					        			
					        		}finally{
					        			
					        		}	
					            }
							};
							locUpdateOnDemandThread.start();
						}else{
							
							myApp.getAmd().UpdateLocation();
									
						}
						
						if(myApp.getAmd().MoveOutOfResidentDomain()){
							
							myApp.getAmd().RequestResidentDomain();
							
							Message msg1 = Message.obtain(refMapViewHandler,APP_CONST.MD_NEW_RESIDENTDOMAIN,APP_CONST.MESSAGE_OPTION_FIELD,APP_CONST.MESSAGE_OPTION_FIELD);
							refMapViewHandler.sendMessage(msg1);
							
							System.out.println("New ResidentDomain!!");
						}else{
							
							System.out.println("No New ResidentDomain!!");
							
						}
						
						int index = myApp.getAmd().CheckGeopageTrigger();  
						
						if(index != -1){
							Message msg2 = Message.obtain(refSimpleViewHandler,APP_CONST.MD_GEOPAGE_TRIGGERED,index,APP_CONST.MESSAGE_OPTION_FIELD);
							refSimpleViewHandler.sendMessage(msg2);
							
							System.out.println("Geopage "+ index + " triggered!!!" );
							
						}else{
							
							System.out.println("No geopage Triggered!!");
						}
						break;
					case APP_CONST.STATUS_NETWORK_GPS_BOTH_STOP:
						StopLocationService();
						StopNetworkService();
						break;
					case APP_CONST.STATUS_NETWORK_GPS_BOTH_RESTART:
						StartLocationService();
						StartNetworkService();
						break;
					case APP_CONST.MD_EASY_GEOPAGE_NEED_TO_SEND:
						
						Geopage g = (Geopage) msg.obj;
						myApp.getAmd().SendEasyPageToServer(g);
						break;
						
				}
					}

				
				
			};
	}

}
