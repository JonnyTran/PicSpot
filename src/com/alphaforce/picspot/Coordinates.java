package com.alphaforce.picspot;

public class Coordinates {
	double latitude;
	double longitude;
	
	public Coordinates(double lat, double longi){
		latitude = lat;
		longitude = longi;
	}
	
	public String toString(){
		String coordStr = "";
		coordStr = coordStr + "(" + latitude + ", " + longitude + ")";
		return coordStr;
	}
}
