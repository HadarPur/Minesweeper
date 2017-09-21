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
    private Marker mCurrLocationMarker;
    private double latitude, longitude;
    private Context context;
    private Geocoder gc;
    private LatLng MylatLng, PlayerlatLng;
    private MarkerOptions markerOptionsMylocation, markerOptionsPlayerLocation;

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
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            Log.d(TAG,"set my location");
            setMyLocationOnTheMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMyLocationOnTheMap() throws IOException {
        //to specific location
        MylatLng = new LatLng(latitude, longitude);
        markerOptionsMylocation = new MarkerOptions();

        //to specific address
        List<android.location.Address> ls=gc.getFromLocation(latitude, longitude, 1);
        android.location.Address address=ls.get(0);
        String location=address.getLocality();

        //Place current location marker
        markerOptionsMylocation.position(MylatLng);
        markerOptionsMylocation.title("Current Position");
        markerOptionsMylocation.snippet("Location: " + location);
        mCurrLocationMarker = mMap.addMarker(markerOptionsMylocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MylatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    public void setMarkersOnMap(UserInfo userInfo) throws IOException {
        //place users markers
        mMap.clear();
        mMap.addMarker(markerOptionsMylocation);

        //to specific location
        PlayerlatLng = new LatLng(userInfo.getLatitude(), userInfo.getLongitude());
        markerOptionsPlayerLocation = new MarkerOptions();
        Log.d(TAG,"set Markers array");

        //to specific address
        List<android.location.Address> ls=gc.getFromLocation(userInfo.getLatitude(), userInfo.getLongitude(), 1);
        android.location.Address address=ls.get(0);
        String location=address.getLocality();

        //set markers on the map
        markerOptionsPlayerLocation.position(PlayerlatLng);
        markerOptionsPlayerLocation.title("Name: "+userInfo.getName() + " , Time: " + userInfo.getPoints());
        markerOptionsPlayerLocation.snippet("Location: " + location);
        markerOptionsPlayerLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.pos));
        mMap.addMarker(markerOptionsPlayerLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PlayerlatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
