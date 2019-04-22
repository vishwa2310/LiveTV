package com.livetv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.provider.Settings.Secure;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class Activity_dashboard extends AppCompatActivity  {
    private TextView textView;
    private int resquestPermissionCode = 1;
    private Button button_next;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        if (checkPermission()) {
        } else {
            requestPermission();
        }
        textView = findViewById(R.id.textView);
        button_next = findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_dashboard.this, Activity_Video.class);
                startActivity(i);
            }
        });

/*
        private String android_id = Secure.getString(getContext().getContentResolver(),
                Secure.ANDROID_ID);*/
      //  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
      //  System.out.println("Latitude:" +  + ", Longitude:" + location.getLongitude());
       // getAddress(19.109005, 72.874766);
        textView();
    }


    private void textView() {
        try{
        textView.setText("android device id=" +Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID)+"\n" +"SERIAL: " + Build.SERIAL + "\n" +
                "MODEL: " + Build.MODEL + "\n" +
                "ID: " + Build.ID + "\n" +
                "Manufacture: " + Build.MANUFACTURER + "\n" +
                "Brand: " + Build.BRAND + "\n" +
                "Type: " + Build.TYPE + "\n" +
                "User: " + Build.USER + "\n" +
                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                "SDK:  " + Build.VERSION.SDK + "\n" +
                "BOARD: " + Build.BOARD + "\n" +
                "BRAND: " + Build.BRAND + "\n" +
                "HOST: " + Build.HOST + "\n" +
                "FINGERPRINT: " + Build.FINGERPRINT + "\n" +
                "Version Code: " + Build.VERSION.RELEASE);}catch (Exception e){
            Toast.makeText(context,"problem in device info",Toast.LENGTH_SHORT).show();;
        }
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(Activity_dashboard.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, resquestPermissionCode);
    }


    private boolean checkPermission() {
        int permission_write = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission_read = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
       /* int permission_coreloc = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int permission_fineloc = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);*/
        return permission_read == PackageManager.PERMISSION_GRANTED && permission_write == PackageManager.PERMISSION_GRANTED;

    }

/*
    @Override
    public void onLocationChanged(Location location) {
//System.out.println("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        getAddress(19.109005, 72.874766);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/
  /*  public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(Activity_dashboard.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

           // Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
         //   System.out.println("address**:" + add );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/
}
