package com.alphaforce.picspot;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.View.OnClickListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity{
	private double longitude = 0, latitude = 0, altitude = 0, distance =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				
				longitude = location.getLongitude();
				latitude = location.getLatitude();
				altitude = location.getAltitude();
				Location currentLocation = new Location("current");
				currentLocation.setLatitude(latitude);
				currentLocation.setLongitude(longitude);
				
				Location nedderman = new Location("Nedderman");
				nedderman.setLatitude(32.00);
				nedderman.setLongitude(-97.00);
				
				distance =  currentLocation.distanceTo(nedderman);
				
				String longmessage= "Longitude = " + longitude;
				String latmessage= "Latitude = " + latitude;
				String distmessage= "distance = " + distance;
								
				
				TextView longview = (TextView)findViewById(R.id.longitude);
				longview.setText(longmessage);
				TextView latview = (TextView)findViewById(R.id.latitude);
				latview.setText(latmessage);
				TextView altview = (TextView)findViewById(R.id.distance);
				altview.setText(distmessage);
							
			}
		};
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
		
	//	Button saveButton = (Button)findViewById(R.id.Save);
	//	saveButton.setOnClickListener(clickListener);
		
    };
/*    
    private OnClickListener clickListener = new OnClickListener(){
    	public void onClick(View v){
    		coordinates.add(new Coordinates(latitude, longitude));
    		for(int i=0; i < coordinates.size(); i++){
    			System.out.println(coordinates.get(i));
    		}
    	}

    };
  */  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

