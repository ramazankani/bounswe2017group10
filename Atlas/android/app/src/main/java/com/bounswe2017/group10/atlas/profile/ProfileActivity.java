package com.bounswe2017.group10.atlas.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bounswe2017.group10.atlas.R;
import com.bounswe2017.group10.atlas.home.GoogleMapsActivity;
import com.bounswe2017.group10.atlas.home.ListItemsFragment;
import com.bounswe2017.group10.atlas.httpbody.LocationRequest;
import com.bounswe2017.group10.atlas.remote.APIUtils;
import com.bounswe2017.group10.atlas.response.OnGetItemsResponse;
import com.bounswe2017.group10.atlas.util.Constants;
import com.bounswe2017.group10.atlas.util.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;

import static com.bounswe2017.group10.atlas.util.Utils.logout;

public class ProfileActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private ListItemsFragment mSearchItemsFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest lastLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == 0) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Find location.

            } else {
                // Permission request was denied.
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences pref = Utils.getSharedPref(this);
        String firstName = pref.getString(Constants.FIRSTNAME, "");
        String lastName = pref.getString(Constants.LASTNAME, "");
        String email = pref.getString(Constants.EMAIL, "");

        String nameText = getString(R.string.fullname, firstName, lastName);
        ((TextView) findViewById(R.id.user_profile_name)).setText(nameText);
        ((TextView) findViewById(R.id.user_profile_email)).setText(email);

        TextView logouttext = findViewById(R.id.plogout);
        logouttext.setOnClickListener((View btnview) -> {
            logout(getApplicationContext());
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSearchItemsFragment = new ListItemsFragment();
        setUpNearbySearchFragment();
        requestLocationPermissionIfNotGranted();
        /*
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lastLocation.setLongitude(new DecimalFormat(Constants.DECIMAL_FORMAT_STRING).format(location.getLongitude()));
                            lastLocation.setLongitude(new DecimalFormat(Constants.DECIMAL_FORMAT_STRING).format(location.getLatitude()));
                            // Logic to handle location object
                        }
                    }
                });
        */


        TextView nearbyHeritages = findViewById(R.id.nearbyHeritages);
        nearbyHeritages.setOnClickListener((View btnview) -> {
            /*
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            */
            Intent intent = new Intent(this, GoogleMapsActivity.class);
            startActivity(intent);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.profile_container, mSearchItemsFragment)
                    .addToBackStack(null)
                    .commit();

        });
    }
    /**
     * Set up the functionality of mSearchItemsFragment. This method sets how mSearchItemsFragment
     * requests its items from the server.
     */
    private void setUpNearbySearchFragment() {
        mSearchItemsFragment.setRequestStrategy(new ListItemsFragment.RequestStrategy() {
            @Override
            public void requestItems(Context context, int offset, OnGetItemsResponse.GetItemCallback getItemCallback) {
                // TODO: pagination for search results
                if (lastLocation != null){
                    String authStr = Utils.getSharedPref(getApplicationContext()).getString(Constants.AUTH_STR, Constants.NO_AUTH_STR);
                    OnGetItemsResponse respHandler = new OnGetItemsResponse(context, getItemCallback);
                    APIUtils.serverAPI().nearBySearch(authStr, lastLocation).enqueue(respHandler);
                }else {
                    //TODO:: Notify the user.
                }
            }
        });
        mSearchItemsFragment.setRequestImmediately(false);
    }
    private void requestLocationPermissionIfNotGranted(){
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        }

    }

}
