package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.HolidayList;
import com.hrms.attendanceapp.utils.CustomProgress;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class AttendanceActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private ImageView imgBack;
    private LinearLayout linTimein;
    private LinearLayout linTimeout;
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    private String strlat = "";
    private String strlon = "";
    private String strURLadd = "";
    private String stgpsaddrs = "";
    private double latitude;
    private double longitude;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private String userId = "";
    private String cityName = "";
    private String str = "";
    private String currentDateTimeString = "";
    private String  dateString = "";
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private String strempid = "";
    private String strcurrentDate = "";
    private String strtimein = "";
    private String strcurrentMonth = "";
    Cursor cursor = null;
    private int colId;
    private String strtimeout = "";
    private String userTimeInStatus = "";
    private String userTimeOutStatus = "";
    private String strempidtimein = "";
    private String strcurrentDatetimein = "";
    private String strtimeinlat = "";
    private String strtimeinlon = "";
    private String strcurrentMonthtimein = "";
    private String strtimeoutlat = "";
    private String strtimeoutlon = "";
    private String struserName = "";
    private int colTimeId;
    CustomProgress customProgressa;
    private String str_timeout = "";
    private String str_timein = "";
    private String strId = "";
    private LinearLayout lin;
    private ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_attendance);
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();



        strId = sh_Pref.getString("USER_CODE", "");
        String strName = sh_Pref.getString("USER_NAME", "");
        String strcomName = sh_Pref.getString("COM_NAME", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");


        lin = (LinearLayout)findViewById(R.id.activity_attendance_);

        imgLogo = (ImageView)findViewById(R.id.imageview_attendance_logo);

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(AttendanceActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }

        imgBack = (ImageView) findViewById(R.id.imageview_attendance_back);
        imgBack.setOnClickListener(this);

        linTimein = (LinearLayout) findViewById(R.id.lin_time_in);
        linTimein.setOnClickListener(this);

        linTimeout = (LinearLayout) findViewById(R.id.lin_time_out);
        linTimeout.setOnClickListener(this);


        customProgressa = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgressa.showProgress(AttendanceActivity.this, messag, false);

        setUpGClient();


    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    @Override
    protected void onStart() {
        //gac.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        //gac.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(AttendanceActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(AttendanceActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(AttendanceActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }





    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(AttendanceActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(AttendanceActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }




    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            latitude = mylocation.getLatitude();
            longitude = mylocation.getLongitude();

            strlat =  Double.toString(mylocation.getLatitude());
            strlon = Double.toString(mylocation.getLongitude());
            //Toast.makeText(AttendanceActivity.this, strlat+" "+strlon, Toast.LENGTH_LONG).show();

            if (Utils.checkAllCon(AttendanceActivity.this)) {

                getAddressFromLocation(mylocation, getApplicationContext(), new GeoCoderHandler());
            }
            else{
                customProgressa.hideProgress();
            }

        }
    }

    public static void getAddressFromLocation(final Location location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // sending back first address line and locality
                        result = address.getAddressLine(0) + ", " + address.getLocality() + ", " + address.getCountryName();
                    }
                } catch (IOException e) {
                    Log.e("TAG", "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }
    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String result;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    result = bundle.getString("address");
                    break;
                default:
                    result = "Unable to pick the location due to network error";//null;
            }
            cityName = result;
            //Toast.makeText(AttendanceActivity.this, result, Toast.LENGTH_LONG).show();
            customProgressa.hideProgress();
        }
    }







    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);
    }




    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                } else {
                    //getMyLocation();
                    finish();

                    /*Intent locintent = new Intent(CustomerActivity.this,CustomerActivity.class);
                                        startActivity(locintent);
                                        finish();*/



                }

            }
        }
    };




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_attendance_back:
                finish();
                break;
            case R.id.lin_time_in:


                callDateTime();
                callTimein(strId);




                break;
            case R.id.lin_time_out:

               
                callDateTime();
                callTimeOut(strId);




                break;

        }
    }



    private void callTimeOut(String strId) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(AttendanceActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getTimeOut(strId, currentDateTimeString, str, dateString, cityName);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        timeoutRespn(jsonresponse);

                    } else {

                        Snackbar snackbar = Snackbar
                                .make(lin, "No Data Found, Please try again later.", Snackbar.LENGTH_LONG);


                        snackbar.show();
                        // Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }



            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();

                if (t instanceof IOException) {

                    Snackbar snackbar = Snackbar
                            .make(lin, "Not connected to Internet", Snackbar.LENGTH_LONG);


                    snackbar.show();




                }
                else {

                    Snackbar snackbar = Snackbar
                            .make(lin, "Internet Connection is unavailable, Please try again later.", Snackbar.LENGTH_LONG);


                    snackbar.show();
                    //showAlert("Internet Connection is unavailable, Please try again later.","Message");
                    // todo log to some central bug tracking service
                }

            }


        });
    }

    private void timeoutRespn(String jsonresponse) {

        try {

            //{"msg":"data is saved","resultstatus":"true","status":"active"}

            // {"msg":"Last Time out not completed","resultstatus":"false","status":"active"}

            JSONObject jresData = new JSONObject(jsonresponse);
            String status = jresData.getString("status");
            if(status.equalsIgnoreCase("active")) {
                String resultstatus = jresData.getString("resultstatus");
                if (resultstatus.equalsIgnoreCase("true")) {


                    String msg = jresData.getString("msg");
                    Toast.makeText(AttendanceActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AttendanceActivity.this, AttendanceRecordingPage.class);
                    intent.putExtra("TIMEIN", "Clock out Attendence Recorded Successfully...!!!");
                    startActivity(intent);
                    finish();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceActivity.this);
                    builder.setMessage("You are not Clockin Last Time, So Please Clockin First");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });

                    builder.setCancelable(false);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }
            }
            else{
                Intent intent = new Intent(AttendanceActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }











        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }





    private void showAlert(String s, String s1) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AttendanceActivity.this);
        mBuilder.setTitle(s);
        mBuilder.setMessage(s1);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=mBuilder.create();
        dialog.show();

    }


    private void callDateTime() {




        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        str = sdf.format(new Date());

//        Toast.makeText(AttendanceActivity.this, str, Toast.LENGTH_LONG).show();



        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
        currentDateTimeString = sdfs.format(new Date());
        //      Toast.makeText(AttendanceActivity.this, currentDateTimeString, Toast.LENGTH_LONG).show();


        SimpleDateFormat sdfss = new SimpleDateFormat("MM/yyyy");
        dateString = sdfss.format(new Date());
        //       Toast.makeText(AttendanceActivity.this, dateString, Toast.LENGTH_LONG).show();
    }




    private void callTimein(String strId) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(AttendanceActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getTimein(strId, currentDateTimeString, str, dateString, cityName);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        timeinRespn(jsonresponse);

                    } else {

                        Snackbar snackbar = Snackbar
                                .make(lin, "No Data Found, Please try again later.", Snackbar.LENGTH_LONG);


                        snackbar.show();
                        // Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }



            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();

                if (t instanceof IOException) {

                    Snackbar snackbar = Snackbar
                            .make(lin, "Not connected to Internet", Snackbar.LENGTH_LONG);


                    snackbar.show();




                }
                else {

                    Snackbar snackbar = Snackbar
                            .make(lin, "Internet Connection is unavailable, Please try again later.", Snackbar.LENGTH_LONG);


                    snackbar.show();
                    //showAlert("Internet Connection is unavailable, Please try again later.","Message");
                    // todo log to some central bug tracking service
                }

            }


        });
    }

    private void timeinRespn(String jsonresponse) {

        try {

            //{"msg":"data is saved","resultstatus":"true","status":"active"}

           // {"msg":"Last Time out not completed","resultstatus":"false","status":"active"}

            JSONObject jresData = new JSONObject(jsonresponse);
            String status = jresData.getString("status");
            if(status.equalsIgnoreCase("active")) {
                String resultstatus = jresData.getString("resultstatus");
                if (resultstatus.equalsIgnoreCase("true")) {


                   String msg = jresData.getString("msg");
                    Toast.makeText(AttendanceActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AttendanceActivity.this, AttendanceRecordingPage.class);
                    intent.putExtra("TIMEIN", "Clock in Attendence Recorded Successfully...!!!");
                    startActivity(intent);
                    finish();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceActivity.this);
                    builder.setMessage("You are not Clockout Last Time, So Please Clockout First");
//                              builder.setMessage("Last Time out not Completed");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });

                    builder.setCancelable(false);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }
            }
            else{
                Intent intent = new Intent(AttendanceActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }




        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }




}
