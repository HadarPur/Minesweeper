package com.example.hadar.minesweeper;

import android.content.Context;
import android.location.Geocoder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class Map implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private double latitude, longitude;
    private Context context;
    private Geocoder gc;
    private LatLng MyLatLng;
    private MarkerOptions markerOptionsMyLocation, markerOptionsPlayerLocation;

    //c'tor
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
            setMyLocationOnTheMap();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //set marker with my location on the map
    public void setMyLocationOnTheMap() throws IOException {
        //to specific location
        MyLatLng = new LatLng(latitude, longitude);
        markerOptionsMyLocation = new MarkerOptions();

        //to specific address
        List<android.location.Address> ls=gc.getFromLocation(latitude, longitude, 1);
        android.location.Address address=ls.get(0);
        String street=address.getAddressLine(0);

        //Place current location marker
        markerOptionsMyLocation.position(MyLatLng);
        markerOptionsMyLocation.title("Current Position");
        markerOptionsMyLocation.snippet("Location: " + street);
        mMap.addMarker(markerOptionsMyLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MyLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }


    //set marker with player's location on the map
    public void setMarkersOnMap(UserInfo userInfo) throws IOException {
        //place users markers
        mMap.clear();
        mMap.addMarker(markerOptionsMyLocation);

        //to specific location
        LatLng PlayerLatLng = new LatLng(userInfo.getLatitude(), userInfo.getLongitude());
        markerOptionsPlayerLocation = new MarkerOptions();

        //to specific address
        List<android.location.Address> ls=gc.getFromLocation(userInfo.getLatitude(), userInfo.getLongitude(), 1);
        android.location.Address address=ls.get(0);
        //get current province/City
        String street=address.getAddressLine(0);

        //set markers on the map
        markerOptionsPlayerLocation.position(PlayerLatLng);
        markerOptionsPlayerLocation.title("Name: "+userInfo.getName() + " , Time: " + userInfo.getPoints()+" sec");
        markerOptionsPlayerLocation.snippet("Location: "+street);
        markerOptionsPlayerLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark));
        mMap.addMarker(markerOptionsPlayerLocation);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PlayerLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    //move camera to current location
    public void moveCameraToCurrentPos() {
        mMap.clear();
        mMap.addMarker(markerOptionsMyLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MyLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }
}
