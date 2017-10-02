package edu.iastate.cs.lanc.ObjectTracking;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class AndroidMapActivity extends MapActivity{

	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	
	static Handler locationListenerHandler;
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.map);
		SetUpViews();
		
		locationListenerHandler = new Handler(){
			
			@Override
			public void handleMessage(Message msg){
				
				Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT);
			}
			
		};
		
	}
	private void SetUpViews(){
		mapView = (MapView)findViewById(R.id.v1map);
		mapView.setBuiltInZoomControls(true);
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		mapView.invalidate();
	}
	
}
