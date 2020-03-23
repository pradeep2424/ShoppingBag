package com.example.myapplication.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    RelativeLayout rlRootLayout;

//    private PrefManagerConfig prefManagerConfig;

    boolean isUserLoggedIn;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        printHashKey();

        init();
        getCurrentLocation();
        loadNextPage();

    }

//    public void printHashKey() {
//        try {
//            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(
//                    getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String hashKey = new String(Base64.encode(md.digest(), 0));
//                Log.i("*****", "printHashKey() Hash Key : " + hashKey);
//            }
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("*****", "printHashKey()", e);
//        } catch (Exception e) {
//            Log.e("*****", "printHashKey()", e);
//        }
//    }

    private void init() {
//        prefManagerConfig = new PrefManagerConfig(this);
        rlRootLayout = findViewById(R.id.rl_rootLayout);
    }

    private void loadNextPage() {
//        isUserLoggedIn = prefManagerConfig.getIsUserLoggedIn();
//        mobileNumberber = prefManagerConfig.getMobileNo();

//        if (isUserLoggedIn && !mobileNumber.equalsIgnoreCase(prefManagerConfig.SP_DEFAULT_VALUE)) {
//            getUserDetails();
//
////            getAreaDetails();
//
////            new Handler().postDelayed(new Runnable() {
////                public void run() {
////                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
////                    startActivity(intent);
////                    finish();
////                }
////            }, 2000);
//
//        } else {

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, GetStartedMobileNumberActivity.class);
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
//        }
    }

//    private JSONObject createJsonUserDetails() {
//        JSONObject postParam = new JSONObject();
//
//        try {
//            postParam.put("Username", "test");
//            postParam.put("Password", "test");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return postParam;
//    }

    private void getCurrentLocation() {
        if (requestLocationPermission()) {
            getLatitudeLongitude();
        }
    }

    private boolean requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                return false;
            }
        }
        return true;

    }

    private void getLatitudeLongitude() {
//        try {
//            double latitude;
//            double longitude;
//
//            GPSTracker tracker = new GPSTracker(this);
//            if (!tracker.canGetLocation()) {
//                tracker.showSettingsAlert();
//            } else {
//                latitude = tracker.getLatitude();
//                longitude = tracker.getLongitude();
//
//                getAddressFromMap(latitude, longitude);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    private void getAddressFromMap(double latitude, double longitude) throws IOException {
//        Geocoder gCoder = new Geocoder(this);
//        List<Address> addresses = gCoder.getFromLocation(latitude, longitude, 1);
//        if (addresses != null && addresses.size() > 0) {
//
//            AddressData addressData = new AddressData(latitude, longitude, addresses);
//            Application.locationAddressData = addressData;
//
////            Toast.makeText(myContext, "country: " + addresses.get(0).getCountryName(), Toast.LENGTH_LONG).show();
//        }
//    }


    public void showSnackbarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }
}
