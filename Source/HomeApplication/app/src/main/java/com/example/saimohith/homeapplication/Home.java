package com.example.saimohith.homeapplication;


import android.Manifest;
import android.content.Context;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Home extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Geocoder geocoder;
    public double lat;
    public double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        geocoder = new Geocoder(this);
        StringBuilder Address = new StringBuilder();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            return;
        }
        mMap.setMyLocationEnabled(true);


        LocationManager userCurLoc = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        LocationListener userLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }



        };
        LatLng userCoordinates = null;

        userCurLoc.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, userLocationListener);
        lat = userCurLoc
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                .getLatitude();
        lng = userCurLoc
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                .getLongitude();
        userCoordinates = new LatLng(lat,lng);
        try {
            List<Address> add = geocoder.getFromLocation(lat, lng, 1);
            Address adrs = add.get(0);
            Address =  new StringBuilder();
            for (int i = 0; i < adrs.getMaxAddressLineIndex(); i++) {
                Address.append(adrs.getAddressLine(i)).append("\t");
            }
            Address.append(adrs.getCountryName()).append("\t");

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userCoordinates, 7));
        mMap.addMarker(new MarkerOptions().position(userCoordinates)
                .title("Your current address.").snippet(Address.toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.aa)));
    }
}

