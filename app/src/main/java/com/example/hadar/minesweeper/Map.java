package com.example.hadar.minesweeper;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Map implements OnMapReadyCallback {
    private static final String TAG ="map";
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Location loc;
    private TableFrame tableFrame;
    private Marker mCurrLocationMarker;
    private Marker[] mPlayerMarkers;
    private double latitude, longitude;
    //GPSTracker gpsTracker;

    public Map(SupportMapFragment mapFragment,double latitude,double longitude){
        this.mapFragment=mapFragment;
        this.latitude=latitude;
        this.longitude=longitude;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        setMyLocationOnTheMap();
        //setMarkersOnMap();
    }

    public void setMyLocationOnTheMap() {
        //Place current location marker
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    public void setMarkersOnMap() {
        MarkerOptions markerOptions = new MarkerOptions();
        ArrayList<UserInfo> users= new ArrayList<>();

        users.addAll(tableFrame.getUserInfo());
        mPlayerMarkers=new Marker[users.size()];

        Log.d(TAG,"size = "+users.size());
        for(int i=0; i<users.size();i++) {
            UserInfo user = users.get(i);
            LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
            markerOptions.position(latLng);
            markerOptions.title(user.getName()+ " , score:" +user.getPoints());
            markerOptions.snippet("location:" +user.getLatitude()+","+user.getLongitude());
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            mPlayerMarkers[i]=mMap.addMarker(markerOptions);
        }
    }
}
