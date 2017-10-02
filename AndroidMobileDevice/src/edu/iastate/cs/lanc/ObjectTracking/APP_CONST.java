package edu.iastate.cs.lanc.ObjectTracking;

public class APP_CONST {

	
	public static final int STATUS_NOTHING_HAPPENS = 0;
	public static final int STATUS_NETWORK_RESTART = 1;
	public static final int STATUS_GPS_RESTART = 2;
	public static final int STATUS_REQUEST_DID = 3;
	public static final int STATUS_NETWORK_GPS_BOTH_RESTART = 4;
	public static final int STATUS_NORMAL_START = 5;
	public static final int STATUS_NETWORK_GPS_BOTH_STOP = 6;
	public static final String FIRST_RUN = "firstTime";
	
	public static final String SERVER1 = "129.186.93.111";
	public static final String SERVER1_PORT = "4000";
	public static final int MY_LOAD_VALUE = 15;
	public static final int MY_ZONE_RADIUS_VALUE = 500;
	public static final int MY_LOCATION_UPDATE_MIN_TIME_VALUE = 500;
	public static final int MY_LOCATION_UPDATE_MIN_DISTANCE_VALUE = 1;
	
	public static final String DID = "DID";
	public static final String MY_LOAD = "myLoad";
	public static final String MY_ZONE_RADIUS = "myZoneRadius";
	public static final String MY_LOCATION_UPDATE_POLICY = "myLocationUpdatePolicyType";
	public static final String MY_LOCATION_UPDATE_MIN_TIME = "myLocationUpdateMinTime";
	public static final String MY_LOCATION_UPDATE_MIN_DISTANCE = "myLocationUpdateMinDistance";
	public static final String MY_CURRENT_SERVER_IP = "myCurrentServerIP";
	public static final String MY_CURRENT_SERVER_PORT = "myCurrentServerPort";
	
	public static final int POWERMODE_INCHARING = 101;
	
	public static final int MD_LOCATION_CHANGED = 1000;
	public static final int MD_NEW_RESIDENTDOMAIN = 1001;
	public static final int MD_GEOPAGE_TRIGGERED = 1002;
	
	public static final int MD_EASY_GEOPAGE_NEED_TO_SEND = 1003;
//	public static final String STATUS_LOCATION = "location";
//	public static final String STATUS_GEOPAGE_TRIGGERED = "geopageTriggered";
	public static final int MESSAGE_OPTION_FIELD = -1; 
	
}
