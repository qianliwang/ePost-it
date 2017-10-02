package edu.iastate.cs.lanc.ObjectTracking;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainTabActivity extends TabActivity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
 
        TabHost tabHost = getTabHost();
 
        // Tab for Photos
        TabSpec simpleViewSpec = tabHost.newTabSpec("SimpleView");
        // setting Title and Icon for the Tab
        simpleViewSpec.setIndicator("SimpleView", getResources().getDrawable(R.drawable.simpleview));
        Intent gpsIntent = new Intent(this, GPSActivity.class);
        simpleViewSpec.setContent(gpsIntent);
 
        // Tab for Songs
        TabSpec mapViewSpec = tabHost.newTabSpec("MapView");
        mapViewSpec.setIndicator("MapView", getResources().getDrawable(R.drawable.mapview));
//        Intent mapIntent = new Intent(this, AndroidMapActivity.class);
        Intent mapIntent = new Intent(this, BasicMapActivity.class);
        mapViewSpec.setContent(mapIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(simpleViewSpec); // Adding photos tab
        tabHost.addTab(mapViewSpec); // Adding songs tab
        
    
	}

	
	
	
}
