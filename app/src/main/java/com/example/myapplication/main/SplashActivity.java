package com.example.myapplication.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.UserDetails;
import com.example.myapplication.service.retrofit.ApiInterface;
import com.example.myapplication.service.retrofit.RetroClient;
import com.example.myapplication.sharedPreference.PrefManagerConfig;
import com.example.myapplication.utils.Application;
import com.example.myapplication.utils.GPSTracker;
import com.example.myapplication.utils.InternetConnection;
import com.google.android.material.snackbar.Snackbar;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.PlacePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    RelativeLayout rlRootLayout;

    private PrefManagerConfig prefManagerConfig;

    boolean isUserLoggedIn;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        printHashKey();8

        init();
        getCurrentLocation();
//        getSmsDetails();

        loadNextPage();

//        if (isUserLoggedIn && !mobileNumber.equalsIgnoreCase(ConstantValues.SP_DEFAULT_VALUE)) {
//            getUserDetails();
//            getAreaDetails();
//
//        } else {
//            loadNextPage();
//        }

//        insertUserDetails();

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
        prefManagerConfig = new PrefManagerConfig(this);
        rlRootLayout = findViewById(R.id.rl_rootLayout);
    }

    private void loadNextPage() {
        isUserLoggedIn = prefManagerConfig.getIsUserLoggedIn();
        mobileNumber = prefManagerConfig.getMobileNo();

        if (isUserLoggedIn && !mobileNumber.equalsIgnoreCase(prefManagerConfig.SP_DEFAULT_VALUE)) {
//            autoLogin();

            getUserDetails();
//            getAreaDetails();

//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }, 2000);

        } else {
//            callSignUpPage();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, GetStartedMobileNumberActivity.class);
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
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
        try {
            double latitude;
            double longitude;

            GPSTracker tracker = new GPSTracker(this);
            if (!tracker.canGetLocation()) {
                tracker.showSettingsAlert();
            } else {
                latitude = tracker.getLatitude();
                longitude = tracker.getLongitude();

                getAddressFromMap(latitude, longitude);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAddressFromMap(double latitude, double longitude) throws IOException {
        Geocoder gCoder = new Geocoder(this);
        List<Address> addresses = gCoder.getFromLocation(latitude, longitude, 1);
        if (addresses != null && addresses.size() > 0) {

            AddressData addressData = new AddressData(latitude, longitude, addresses);
            Application.locationAddressData = addressData;

//            Toast.makeText(myContext, "country: " + addresses.get(0).getCountryName(), Toast.LENGTH_LONG).show();
        }
    }

    private void getUserDetails() {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.getUserDetails(mobileNumber, mobileNumber);
            Call<ResponseBody> call = apiService.getUserDetails(mobileNumber, mobileNumber);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();

                            JSONObject jsonObj = new JSONObject(responseString);
                            String status = jsonObj.optString("Status");

                            String fname = jsonObj.optString("FName");
                            String lname = jsonObj.optString("LName");
                            String gender = jsonObj.optString("Gender");
                            String email = jsonObj.optString("Email");
                            String telephone = jsonObj.optString("Telephone");
                            String mobile = jsonObj.optString("Mobile");
                            String facebookId = jsonObj.optString("FacebookId");

                            int userID = jsonObj.optInt("UserID");
                            String username = jsonObj.optString("Username");
                            String password = jsonObj.optString("Pass");
                            String userPhoto = jsonObj.optString("UserPhoto");
                            String userRole = jsonObj.optString("UserRole");
                            String userType = jsonObj.optString("UserType");

                            String address = jsonObj.optString("Address");
                            String area = jsonObj.optString("Area");
                            String cityName = jsonObj.optString("CityName");
                            String stateName = jsonObj.optString("StateName");
                            int zipCode = jsonObj.optInt("ZipCode");

                            double totalPoints = jsonObj.optDouble("TotalPoints");
                            if (Double.isNaN(totalPoints)) {
                                totalPoints = 0;
                            }

                            String url = jsonObj.optString("URL");
                            String smsUsername = jsonObj.optString("SMSUsername");
                            String smsPass = jsonObj.optString("SMSPass");
                            String channel = jsonObj.optString("channel");
                            String sendSMS = jsonObj.optString("SendSMS");
                            String senderID = jsonObj.optString("SenderID");

                            String fullName = fname.concat(" ").concat(lname);

                            UserDetails userDetails = new UserDetails();
                            userDetails.setFirstName(fname);
                            userDetails.setLastName(lname);
                            userDetails.setFullName(fullName);
                            userDetails.setUserType(userType);
                            userDetails.setEmail(email);
                            userDetails.setMobile(mobile);
                            userDetails.setGender(gender);
                            userDetails.setTelephone(telephone);
                            userDetails.setFacebookId(facebookId);
                            userDetails.setUserID(userID);
                            userDetails.setUsername(username);
                            userDetails.setPassword(password);
                            userDetails.setUserPhoto(userPhoto);
                            userDetails.setUserRole(userRole);
                            userDetails.setAddress(address);
                            userDetails.setArea(area);
                            userDetails.setCityName(cityName);
                            userDetails.setStateName(stateName);
                            userDetails.setZipCode(zipCode);
                            userDetails.setTotalReferralPoints(totalPoints);
                            Application.userDetails = userDetails;

//                            SMSGatewayObject smsGatewayObject = new SMSGatewayObject();
//                            smsGatewayObject.setUrl(url);
//                            smsGatewayObject.setSmsUsername(smsUsername);
//                            smsGatewayObject.setSmsPass(smsPass);
//                            smsGatewayObject.setChannel(channel);
//                            smsGatewayObject.setSendSMS(sendSMS);
//                            smsGatewayObject.setSenderID(senderID);
//                            Application.smsGatewayObject = smsGatewayObject;

                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

//                            if (status.equalsIgnoreCase("Success")) {
//                                FirebaseUser user = mAuth.getCurrentUser();
//
//                                String accessToken = jsonObj.getString("AccessToken");
//                                int accountType = jsonObj.getInt("AccountType");
//                                String mainCorpNo = jsonObj.getString("MainCorpNo");
//                                String corpID = jsonObj.getString("CorpID");
//                                String emailID = jsonObj.getString("EmailID");
//                                String firstName = jsonObj.getString("FirstName");
//                                String lastName = jsonObj.getString("LastName");
//
//                                String hibernate = jsonObj.getString("Hibernate");
//                                boolean isGracePeriod = jsonObj.getBoolean("IsGracePeriod");
//                                boolean isTrial = jsonObj.getBoolean("IsTrial");
//                                msgHeader = jsonObj.getString("MessageHeader");
//                                message = jsonObj.getString("Message");
//
//                                int maxQuestionsAllowed = jsonObj.getInt("MaxQuestionsAllowed");
//                                int maxSurveysAllowed = jsonObj.getInt("MaxSurveysAllowed");
//
//                            } else if (status.equalsIgnoreCase("LoginFailed")) {
//
//                                String msg = jsonObj.getString("Message");
//                                String msgHeader = jsonObj.getString("MessageHeader");
//
//                                prefs.edit().clear().apply();
//                                signOutFirebaseAccounts();
//
//
//                                if (msgHeader.trim().equalsIgnoreCase("")) {
//                                    showSnackbarErrorMsg(msg);
//                                    callSignUpPage();
//                                } else {
//                                    accountBlockedDialog(msgHeader, msg);
//                                }
//
//                            } else if (status.equalsIgnoreCase("Invalid AccessToken")) {
//                                showSnackbarErrorMsg(getResources().getString(R.string.invalid_access_token));
//
//                            } else if (status.equalsIgnoreCase("Error")) {
//                                String msg = jsonObj.getString("Message");
//                                showSnackbarErrorMsg(msg);
//
//                            } else {
//                                showSnackbarErrorMsg("Unmatched response, Please try again.");
//                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
                        Log.e("Error onFailure : ", t.toString());
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            signOutFirebaseAccounts();

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getUserDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

//    private void getSmsDetails() {
//        if (InternetConnection.checkConnection(this)) {
//
//            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.getSMSDetails();
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    try {
//                        int statusCode = response.code();
//                        if (response.isSuccessful()) {
//
//                            String responseString = response.body().string();
//
//                            JSONObject jsonObj = new JSONObject(responseString);
//                            String status = jsonObj.optString("Status");
////
////                            String fname = jsonObj.optString("FName");
////                            String lname = jsonObj.optString("LName");
////                            String gender = jsonObj.optString("Gender");
////                            String email = jsonObj.optString("Email");
////                            String telephone = jsonObj.optString("Telephone");
////                            String mobile = jsonObj.optString("Mobile");
////                            String facebookId = jsonObj.optString("FacebookId");
////
////                            int userID = jsonObj.optInt("UserID");
////                            String username = jsonObj.optString("Username");
////                            String password = jsonObj.optString("Pass");
////                            String userPhoto = jsonObj.optString("UserPhoto");
////                            String userRole = jsonObj.optString("UserRole");
////                            String userType = jsonObj.optString("UserType");
////
////                            String address = jsonObj.optString("Address");
////                            String area = jsonObj.optString("Area");
////                            String cityName = jsonObj.optString("CityName");
////                            String stateName = jsonObj.optString("StateName");
////                            int zipCode = jsonObj.optInt("ZipCode");
//
//                            String url = jsonObj.optString("URL");
//                            String smsUsername = jsonObj.optString("SMSUsername");
//                            String smsPass = jsonObj.optString("SMSPass");
//                            String channel = jsonObj.optString("channel");
//                            String sendSMS = jsonObj.optString("SendSMS");
//                            String senderID = jsonObj.optString("SenderID");
//
//                            SMSGatewayObject smsGatewayObject = new SMSGatewayObject();
//                            smsGatewayObject.setUrl(url);
//                            smsGatewayObject.setSmsUsername(smsUsername);
//                            smsGatewayObject.setSmsPass(smsPass);
//                            smsGatewayObject.setChannel(channel);
//                            smsGatewayObject.setSendSMS(sendSMS);
//                            smsGatewayObject.setSenderID(senderID);
//                            Application.smsGatewayObject = smsGatewayObject;
//
//                            loadNextPage();
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    try {
//                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
//                        Log.e("Error onFailure : ", t.toString());
//                        t.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
////            signOutFirebaseAccounts();
//
//            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            getSmsDetails();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }

//    private void getAreaDetails() {
//        if (InternetConnection.checkConnection(this)) {
//
//            ApiInterface apiService = RetroClient.getApiService(this);
////            Call<ResponseBody> call = apiService.getUserDetails(createJsonUserDetails());
//            Call<ResponseBody> call = apiService.getAreaDetails();
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//
//                        int statusCode = response.code();
//                        if (response.isSuccessful()) {
//
//                            String responseString = response.body().string();
//                            JSONArray jsonArray = new JSONArray(responseString);
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObj = jsonArray.getJSONObject(i);
//
//                                String area = jsonObj.optString("Area");
//                                String areaId = jsonObj.optString("AreaId");
//                                String cityId = jsonObj.optString("CityId");
//                                String cityName = jsonObj.optString("CityName");
//                                String country = jsonObj.optString("Country");
//                                String countryId = jsonObj.optString("CountryId");
//                                String stateId = jsonObj.optString("StateId");
//                                String stateName = jsonObj.optString("StateName");
//                            }
//
//
////                            if (status.equalsIgnoreCase("Success")) {
////                                FirebaseUser user = mAuth.getCurrentUser();
////
////                                String accessToken = jsonObj.getString("AccessToken");
////                                int accountType = jsonObj.getInt("AccountType");
////                                String mainCorpNo = jsonObj.getString("MainCorpNo");
////                                String corpID = jsonObj.getString("CorpID");
////                                String emailID = jsonObj.getString("EmailID");
////                                String firstName = jsonObj.getString("FirstName");
////                                String lastName = jsonObj.getString("LastName");
////
////                                String hibernate = jsonObj.getString("Hibernate");
////                                boolean isGracePeriod = jsonObj.getBoolean("IsGracePeriod");
////                                boolean isTrial = jsonObj.getBoolean("IsTrial");
////                                msgHeader = jsonObj.getString("MessageHeader");
////                                message = jsonObj.getString("Message");
////
////                                int maxQuestionsAllowed = jsonObj.getInt("MaxQuestionsAllowed");
////                                int maxSurveysAllowed = jsonObj.getInt("MaxSurveysAllowed");
////
////                            } else if (status.equalsIgnoreCase("LoginFailed")) {
////
////                                String msg = jsonObj.getString("Message");
////                                String msgHeader = jsonObj.getString("MessageHeader");
////
////                                prefs.edit().clear().apply();
////                                signOutFirebaseAccounts();
////
////
////                                if (msgHeader.trim().equalsIgnoreCase("")) {
////                                    showSnackbarErrorMsg(msg);
////                                    callSignUpPage();
////                                } else {
////                                    accountBlockedDialog(msgHeader, msg);
////                                }
////
////                            } else if (status.equalsIgnoreCase("Invalid AccessToken")) {
////                                showSnackbarErrorMsg(getResources().getString(R.string.invalid_access_token));
////
////                            } else if (status.equalsIgnoreCase("Error")) {
////                                String msg = jsonObj.getString("Message");
////                                showSnackbarErrorMsg(msg);
////
////                            } else {
////                                showSnackbarErrorMsg("Unmatched response, Please try again.");
////                            }
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    try {
//                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
//                        Log.e("Error onFailure : ", t.toString());
//                        t.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
////            signOutFirebaseAccounts();
//
//            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            getAreaDetails();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }


//    private void insertUserDetails() {
//        if (InternetConnection.checkConnection(this)) {
//
//            UserDetails userDetails = new UserDetails();
//            userDetails.setUsername("Pradeep");
//            userDetails.setPassword("PradeepPass");
//
//            ApiInterface apiService = RetroClient.getApiService(this);
////            Call<ResponseBody> call = apiService.getUserDetails(createJsonUserDetails());
//            Call<ResponseBody> call = apiService.insertUserDetails(userDetails);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//
//                        int statusCode = response.code();
//                        if (response.isSuccessful()) {
//
//                            String responseString = response.body().string();
//                            JSONObject jsonObject = new JSONObject(responseString);
//
//
////                            if (status.equalsIgnoreCase("Success")) {
////                                FirebaseUser user = mAuth.getCurrentUser();
////
////                                String accessToken = jsonObj.getString("AccessToken");
////                                int accountType = jsonObj.getInt("AccountType");
////                                String mainCorpNo = jsonObj.getString("MainCorpNo");
////                                String corpID = jsonObj.getString("CorpID");
////                                String emailID = jsonObj.getString("EmailID");
////                                String firstName = jsonObj.getString("FirstName");
////                                String lastName = jsonObj.getString("LastName");
////
////                                String hibernate = jsonObj.getString("Hibernate");
////                                boolean isGracePeriod = jsonObj.getBoolean("IsGracePeriod");
////                                boolean isTrial = jsonObj.getBoolean("IsTrial");
////                                msgHeader = jsonObj.getString("MessageHeader");
////                                message = jsonObj.getString("Message");
////
////                                int maxQuestionsAllowed = jsonObj.getInt("MaxQuestionsAllowed");
////                                int maxSurveysAllowed = jsonObj.getInt("MaxSurveysAllowed");
////
////                            } else if (status.equalsIgnoreCase("LoginFailed")) {
////
////                                String msg = jsonObj.getString("Message");
////                                String msgHeader = jsonObj.getString("MessageHeader");
////
////                                prefs.edit().clear().apply();
////                                signOutFirebaseAccounts();
////
////
////                                if (msgHeader.trim().equalsIgnoreCase("")) {
////                                    showSnackbarErrorMsg(msg);
////                                    callSignUpPage();
////                                } else {
////                                    accountBlockedDialog(msgHeader, msg);
////                                }
////
////                            } else if (status.equalsIgnoreCase("Invalid AccessToken")) {
////                                showSnackbarErrorMsg(getResources().getString(R.string.invalid_access_token));
////
////                            } else if (status.equalsIgnoreCase("Error")) {
////                                String msg = jsonObj.getString("Message");
////                                showSnackbarErrorMsg(msg);
////
////                            } else {
////                                showSnackbarErrorMsg("Unmatched response, Please try again.");
////                            }
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    try {
//                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
//                        Log.e("Error onFailure : ", t.toString());
//                        t.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
////            signOutFirebaseAccounts();
//
//            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            getUserDetails();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
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
