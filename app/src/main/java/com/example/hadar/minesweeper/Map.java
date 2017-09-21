package com.example.hadar.minesweeper;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class Map implements OnMapReadyCallback {
    private static final String TAG ="map";
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double latitude, longitude;
    private Context context;
    private Geocoder gc;
    private LatLng MyLatLng;
    private MarkerOptions markerOptionsMyLocation, markerOptionsPlayerLocation;

    public Map(SupportMapFragment mapFragment,double latitude,double longitude, Context context){
        this.mapFragment=mapFragment;
        this.latitude=latitude;
        this.longitude=longitude;
        this.context=context;
        mapFragment.getMapAsync(this);

        gc=new Geocoder(context);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            Log.d(TAG,"set my location");
            setMyLocationOnTheMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMyLocationOnTheMap() throws IOException {
        //to specific location
        MyLatLng = new LatLng(latitude, longitude);
        markerOptionsMyLocation = new MarkerOptions();

        //to specific address
        List<android.location.Address> ls=gc.getFromLocation(latitude, longitude, 1);
        android.location.Address address=ls.get(0);
        String location=address.getLocality();

        //Place current location marker
        markerOptionsMyLocation.position(MyLatLng);
        markerOptionsMyLocation.title("Current Position");
        markerOptionsMyLocation.snippet("Location: " + location);
        mMap.addMarker(markerOptionsMyLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MyLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    public void setMarkersOnMap(UserInfo userInfo) throws IOException {
        //place users markers
        mMap.clear();
        mMap.addMarker(markerOptionsMyLocation);

        //to specific location
        LatLng PlayerLatLng = new LatLng(userInfo.getLatitude(), userInfo.getLongitude());
        markerOptionsPlayerLocation = new MarkerOptions();
        Log.d(TAG,"set Markers array");

        //to specific address
        List<android.location.Address> ls=gc.getFromLocation(userInfo.getLatitude(), userInfo.getLongitude(), 1);
        android.location.Address address=ls.get(0);
        String location=address.getLocality();

        //set markers on the map
        markerOptionsPlayerLocation.position(PlayerLatLng);
        markerOptionsPlayerLocation.title("Name: "+userInfo.getName() + " , Time: " + userInfo.getPoints());
        markerOptionsPlayerLocation.snippet("Location: " + location);
        markerOptionsPlayerLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.pos));
        mMap.addMarker(markerOptionsPlayerLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PlayerLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void moveCameraToCurrentPos() {
        mMap.clear();
        mMap.addMarker(markerOptionsMyLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MyLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }
}
