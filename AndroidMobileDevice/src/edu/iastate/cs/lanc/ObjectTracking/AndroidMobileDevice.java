package edu.iastate.cs.lanc.ObjectTracking;

//import android.widget.Toast;
import java.math.BigDecimal;
import java.util.ArrayList;

import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.MobileDevice;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.GPSReader;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.Geopage;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.MobileDeviceStatus;
import android.app.Application;
import android.location.*;

public class AndroidMobileDevice extends MobileDevice implements Runnable
{
	private final int GEOPAGE_COUNT = 50;
	
	boolean isGeopageTriggered[] = new boolean[GEOPAGE_COUNT];

	public AndroidMobileDevice(MobileDeviceStatus mobileDeviceStatus)
	{
		super(mobileDeviceStatus);
		
		for(int i=0;i<GEOPAGE_COUNT;i++){
			isGeopageTriggered[i] = false;
		}
	}

	public AndroidMobileDevice(){
		super();
		for(int i=0;i<GEOPAGE_COUNT;i++){
			isGeopageTriggered[i] = false;
		}
	}

	public int CheckGeopageTrigger(){
		
		int index = 0;
		boolean isInGeopageRegion = false;
		
//        float result[] = new float[3];
//        double startX = 0;
//        double startY = 0;
//        double endX = 0;
//        double endY = 0;
        
        for(Geopage g:getMyStatus().getMyGeopageList()){
 /*       	
        	startX = g.getRegion().getCenterX();
        	startY = g.getRegion().getCenterY();
        	endX = getMyStatus().getMyLocation().getX();
        	endY = getMyStatus().getMyLocation().getY();
        	
        	BigDecimal bdEndX = new BigDecimal(endX);
        	BigDecimal bdEndY = new BigDecimal(endY);
        	
//        	Location.distanceBetween(startX, startY, endX, endY, result);
        	Location.distanceBetween(startX, startY, bdEndX.doubleValue(), bdEndY.doubleValue(), result);
        	
        	
        	System.out.println("Check if Geopage Triggered:"+ startX + "," + startY + ","+endX + ","+endY + ",");
        	System.out.println("Distance is:"+result[0]);
 */    	
        	isInGeopageRegion = IsInRegion(g.getRegion(),getMyStatus().getMyLocation());
        	
        	if(!isGeopageTriggered[index]){
        		
        		if(isInGeopageRegion){
        			isGeopageTriggered[index] = true;
            		return index;
            	}
        	}
        	
        	index++;
        }

        return -1;
		
	}
	
	public void UpdateLocation(){
		
		super.UpdateLocation(getMyStatus().getMyLocation());
		
	}
	
	public void UpdateLocationOnDemand(){
		
		
		
	}
	public boolean MoveOutOfResidentDomain(){
		/*
		boolean isMoveOut = false;
		float result[] = new float[3];
		
		double startX = getMyStatus().getMyDomain().getCenterX();
		double startY = getMyStatus().getMyDomain().getCenterY();
		double endX = getMyStatus().getMyLocation().getX();
		double endY = getMyStatus().getMyLocation().getY();
		double residentDomainRadius = getMyStatus().getMyDomain().getRadius();
		
 		Location.distanceBetween(startX, startY, endX, endY, result);
 		System.out.println("Check if moved out of current resident domain:"+ startX + "," + startY + ","+endX + ","+endY + ",");
 		
 		if(result[0] > residentDomainRadius){
 			isMoveOut = true;
 		}
 		
 		isMoveOut = IsInRegion(getMyStatus().getMyDomain(),getMyStatus().getMyLocation());
 		
 		return isMoveOut;
 		*/
		return super.MoveOutOfResidentDomain();
		
	}
	
	public void RequestResidentDomain(){
		
		for(int i=0;i<GEOPAGE_COUNT;i++){
			isGeopageTriggered[i] = false;
		}
		super.RequestResidentDomain(getMyStatus());
	}
	
	public boolean DistanceBetween2Locations(double lX,double lY,double uX,double uY,double radius){
		
		float result[] = new float[3];
		
		Location.distanceBetween(lX, lY, uX, uY, result);
		
		if(result[0] <= radius)
			return true;
		else return false;
	}
	
	
	@Override
	public void run() {
		
		init();
	}
}
