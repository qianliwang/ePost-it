package edu.iastate.cs.lanc.ObjectTracking;

import java.util.ArrayList;

import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.Geopage;
import edu.iastate.cs.lanc.ObjectTracking.MobileDevice.POJO.MobileDeviceStatus;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AndroidGeopageListActivity extends ListActivity{
	
	@Override
	public void onCreate(Bundle icicle){
		
		super.onCreate(icicle);
		
		MDApplication myApp = (MDApplication) getApplication();
		MobileDeviceStatus myStatus = myApp.getAmd().getMyStatus();
		
		ArrayList<String> currentGeopages = new ArrayList<String>();
		
		for(Geopage g :myStatus.getMyGeopageList()){
			
			currentGeopages.add(g.getURL());
		}
		
//		for(int i=0;i<=9;i++){
//			currentGeopages.add("http://yingcai_office.cs.iastate.edu/test/"+String.valueOf(i));
//		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.geopage_list,currentGeopages);
		
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
//		super.onListItemClick(l, v, position, id);
		
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item+" selected",Toast.LENGTH_SHORT).show();
		
	}
	
	

}
