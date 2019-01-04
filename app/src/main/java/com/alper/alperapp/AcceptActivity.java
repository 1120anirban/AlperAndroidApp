package com.alper.alperapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AcceptActivity extends FragmentActivity implements OnMapReadyCallback {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String jobDetails;
    private GoogleMap mMap;
    //private Button mAcceptJob;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng driverLocation = new LatLng(-34.0, 151.2);
        LatLng sourceLocation = new LatLng(-34.0, 151.3);
        LatLng destinationLocation = new LatLng(-34.0, 151.4);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //spotLocation(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        };

        if(Build.VERSION.SDK_INT < 23) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                driverLocation = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
            }
        }

        Bundle extras = getIntent().getExtras();
        jobDetails = extras.getString("jobDetails");
        String sourceAddress = getSourceAddress(jobDetails);
        if(extras!=null)
        {
            Geocoder geocoderSource = new Geocoder(this);
            try {
                List<Address> list = geocoderSource.getFromLocationName(sourceAddress, 1);
                Address address = list.get(0);
                sourceLocation = new LatLng(address.getLatitude(), address.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String destinationAddress = getDestinationAddress(jobDetails);
        if(extras!=null)
        {
            Geocoder geocoderDest = new Geocoder(this);
            try {
                List<Address> list = geocoderDest.getFromLocationName(destinationAddress, 1);
                Address address = list.get(0);
                destinationLocation = new LatLng(address.getLatitude(), address.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Marker> markers = new ArrayList<>();
        markers.add(mMap.addMarker(new MarkerOptions().position(driverLocation).title("Driver").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
        markers.add(mMap.addMarker(new MarkerOptions().position(sourceLocation).title("Pick-up").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))));
        markers.add(mMap.addMarker(new MarkerOptions().position(destinationLocation).title("Drop-off").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 9));
        Toast.makeText(AcceptActivity.this, "Directin towards Pickup", Toast.LENGTH_LONG).show();
        directTowards(driverLocation, sourceLocation);
    }

    private void directTowards(LatLng start, LatLng target){

        double startLatitute = 0.0;
        double startLongitute = 0.0;
        double targetLatitute = 0.0;
        double targetLongitute = 0.0;

        startLatitute = start.latitude;
        startLongitute = start.longitude;
        targetLatitute = target.latitude;
        targetLongitute = target.longitude;

        Intent directionIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + startLatitute + "," + startLongitute + "&daddr=" + targetLatitute + "," + targetLongitute));
        startActivity(directionIntent);
    }

    private String getSourceAddress(String jobDetails) {
        String sourceAddress = "";
        int endIndexSrcAddr = jobDetails.indexOf(" : To : ");
        sourceAddress = jobDetails.substring(6,endIndexSrcAddr).trim();
        return sourceAddress;
    }

    private String getDestinationAddress(String jobDetails) {
        String destinationAddress = "";
        int startIndexSrcAddr = jobDetails.indexOf(" : To : ");
        int endIndexSrcAddr = jobDetails.indexOf("\n");
        destinationAddress = jobDetails.substring(startIndexSrcAddr+8, endIndexSrcAddr).trim();
        return destinationAddress;
    }
}