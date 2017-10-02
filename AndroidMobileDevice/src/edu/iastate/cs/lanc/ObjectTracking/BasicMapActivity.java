/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.iastate.cs.lanc.ObjectTracking;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.MobileDevice;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.Geopage;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.MobileDeviceStatus;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.Region;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.Trigger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is not
 * installed/enabled/updated on a user's device.
 */
public class BasicMapActivity extends FragmentActivity implements OnMapClickListener, OnMapLongClickListener, OnMarkerClickListener{
    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
	
	public static final int CAMERA_REQUEST = 1008;
	
    private static GoogleMap mMap;
    
    private static MDApplication myApp;
    
    private static Handler locationListenerHandler;
    
    private static Context myContext;

    private static boolean isInFront;
    
    private static HashMap hMap;
    
    private AlertDialog geopageDialog;
    
    private Handler serviceManagerHandler;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_demo);
        
        myApp = (MDApplication) getApplication();
        myContext = getApplicationContext();
        isInFront = true;
        
        setUpMapIfNeeded();
        
        
     // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        
     // Showing status
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        }
        
//        TestGeopageList();
        hMap = new HashMap();
        SetGeopageMarkers();
        
        mMap.setMyLocationEnabled(true);
        
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        
        serviceManagerHandler = ServiceManager.serviceManagerHandler;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        isInFront = true;
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	isInFront = false;
    	
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not have been
     * completely destroyed during this process (it is likely that it would only be stopped or
     * paused), {@link #onCreate(Bundle)} may not be called again so we should call this method in
     * {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
 //       mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
   
    	// Creating a LatLng object for the current location
        LatLng latLng = new LatLng(myApp.getAmd().getMyStatus().getMyLocation().getX(),myApp.getAmd().getMyStatus().getMyLocation().getY());
 
        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
    
	public static Handler getLocationListenerHandler() {
		
        if(locationListenerHandler == null){
			
			locationListenerHandler = new Handler(){
				
				@Override
				public void handleMessage(Message msg){
					
					UpdateLocationUI(msg);
		
				}
				
			};
			
		}
		
		return locationListenerHandler;
	}
    private static void UpdateLocationUI(Message msg){
		
		if(isInFront){
			
			
			switch(msg.what){
			case APP_CONST.MD_LOCATION_CHANGED:
				double lat;
				double longt;
				
				if(myApp.getAmd().getMyStatus() == null){
					
					lat = 42;
					longt = -93;
					System.out.println("Fake Location!!");
					
				}else{
					lat = myApp.getAmd().getMyStatus().getMyLocation().getX();
					longt = myApp.getAmd().getMyStatus().getMyLocation().getY();
				}
				
				// Creating a LatLng object for the current location
		        LatLng latLng = new LatLng(lat, longt);
		 
		        // Showing the current location in Google Map
		        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		 
		        // Zoom in the Google Map
		        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				break;
			case APP_CONST.MD_NEW_RESIDENTDOMAIN:
				EmptyGeopageOnMap();
				break;
			case APP_CONST.MD_GEOPAGE_TRIGGERED:
				
				Vibrator vibrator = (Vibrator) myContext.getSystemService(VIBRATOR_SERVICE);
				vibrator.vibrate(800);
				
				String webURL = myApp.getAmd().getMyStatus().getMyGeopageList().get(msg.arg1).getURL();
				
				Intent webPageIntent = new Intent(
						Intent.ACTION_VIEW, Uri.parse(webURL));
				webPageIntent
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				myContext.startActivity(webPageIntent);
				break;
				default:
					break;
			}
		}
    }
	@Override
	public boolean onMarkerClick(Marker marker) {
		
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(800);


		String url = SearchGeopageURL(hMap,marker.getId());
		
		if(url == null || url == ""){
			
			Toast.makeText(myContext, "No Geopage Available", Toast.LENGTH_SHORT).show();
			
		}else{
			Intent webPageIntent = new Intent(
					Intent.ACTION_VIEW, Uri.parse(url));
			webPageIntent
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(webPageIntent);
			myContext.startActivity(webPageIntent);
		}
				
		return false;
	}

	@Override
	public void onMapLongClick(LatLng point) {

		((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
		geopageDialog = CreateGeopageDialog(point.latitude,point.longitude);
//		LatLng currentLatLng = new LatLng(myApp.getAmd().getMyStatus().getMyLocation().getX(),myApp.getAmd().getMyStatus().getMyLocation().getY());
		mMap.addMarker(new MarkerOptions().position(point).title(point.toString()));

	}

	@Override
	public void onMapClick(LatLng point) {

	}
	
	private void SetGeopageMarkers(){
		
		 MobileDeviceStatus myStatus = myApp.getAmd().getMyStatus();
		 
	        if(myStatus != null){
	        	
	        	for(Geopage g:myStatus.getMyGeopageList()){
	        		
	        		Marker tempMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(g.getRegion().getCenterX(),g.getRegion().getCenterY())));
	        		hMap.put(tempMarker, g.getURL());        		
	        	}
	        }
		
	}
	
	private void TestGeopageList(){
		
		MobileDeviceStatus myStatus = myApp.getAmd().getMyStatus();
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
	
	private String SearchGeopageURL(HashMap hMap,String markerID){
		
		Set set = hMap.entrySet();
		Iterator i = set.iterator();
		
		String url = "";
		
		while(i.hasNext()){
			
			Map.Entry me = (Map.Entry)i.next();
			
			String id = ((Marker)me.getKey()).getId();
			
			if(id.equals(markerID)){
				
				url = (String)me.getValue();
				break;
			}
			
		}
		
		return url;
	}
	
	private static void EmptyGeopageOnMap(){
		
		Set set = hMap.entrySet();
		Iterator i = set.iterator();
		
		String url = "";
		
		while(i.hasNext()){
			
			Map.Entry me = (Map.Entry)i.next();
			
			((Marker)me.getKey()).remove();
		}
	}
	
	private AlertDialog CreateGeopageDialog(final double x, final double y){
		
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.geopage_create_dialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Leave Your Message Here");

		// set view
		alertDialogBuilder.setView(textEntryView);
		
		final TextView photoTextView = (TextView)textEntryView.findViewById(R.id.photoText);
		photoTextView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

//				Toast.makeText(myContext, "Take a picture", Toast.LENGTH_SHORT).show();
				
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
				
				return false;
			}
		});
		
		final EditText editTextMessage = (EditText) textEntryView
				.findViewById(R.id.editTextTextMessage);
		
		alertDialogBuilder.setCancelable(false);
		
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

                       String textMessage = editTextMessage.getText().toString();
                       Geopage g = new Geopage();
                       g.getRegion().setRegionShape(Region.CIRCLE);
                       g.getRegion().setCenterX(x);
                       g.getRegion().setCenterY(y);
                       g.getRegion().setRadius(myApp.getAmd().getMyStatus().getMyZone().getRadius());
                       
                       g.getTrigger().setInOut(Trigger.MOVEIN);
                       
                       g.setComment(textMessage);
                       
                       myApp.getAmd().getMyStatus().getMyGeopageList().add(g);
                       
//                       Toast.makeText(myContext, textMessage, Toast.LENGTH_SHORT);
					   
                       SendEasypageToServer(g);
                       
                       dialog.cancel();
					}
				});
		alertDialogBuilder.setNegativeButton("Cancel",
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
		
		return alertDialog;
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		if(data != null){
			  if (requestCode == CAMERA_REQUEST) {  
				 
//				 geopageDialog.dismiss();
				  
//			     Uri uri =  (Uri) data.getExtras().get("data"); 		         
//			     Bitmap bitmapPicture = (Bitmap) data.getExtras().get("data"); 
			     Toast.makeText(BasicMapActivity.this, "Picture taken!!", Toast.LENGTH_SHORT).show();
			     
			}
			}
		
	}
	
	private void SendEasypageToServer(Geopage g){
		
		Message msg = Message.obtain();
		msg.what = APP_CONST.MD_EASY_GEOPAGE_NEED_TO_SEND;
		msg.obj = g;
		serviceManagerHandler.sendMessage(msg);
	}
	
}
