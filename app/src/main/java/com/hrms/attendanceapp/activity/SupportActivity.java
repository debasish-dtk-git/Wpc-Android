package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.Constants;

public class SupportActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBACK;
    private TextView emailsupport;
    private TextView phonesupport;
    private ImageView imgCall1;
    //private TextView txtCall2;
    private Context mContext;
    private Activity mActivity;
    private static final int MY_PERMISSION_REQUEST_CODE = 123;
    private String strcall = "";
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgLogo;
    private ImageView imgWhatsapp;

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
        setContentView(R.layout.activity_support);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = SupportActivity.this;

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        String strId = sh_Pref.getString("USER_CODE", "");
        String strName = sh_Pref.getString("USER_NAME", "");
        String strcomName = sh_Pref.getString("COM_NAME", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");

        imgLogo = (ImageView)findViewById(R.id.imageview_support_logo);

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(SupportActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }



        imgBACK = (ImageView)findViewById(R.id.imageview_support_back);
        imgBACK.setOnClickListener(this);

        imgCall1 = (ImageView)findViewById(R.id.imagview_call_);
        imgCall1.setOnClickListener(this);

        imgWhatsapp = (ImageView)findViewById(R.id.imagview_whatsapp_);
        imgWhatsapp.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageview_support_back:
                finish();
                break;
            case R.id.imagview_call_:

                strcall = "020 8087 2343";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                } else {
                    CallContact();
                }

                break;
            case R.id.imagview_whatsapp_:
               /* strcall = "020 8087 2343";
                String url = "https://api.whatsapp.com/send?phone="+strcall;
                try {
                    PackageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(this, "Whatsapp is not installed in your phone.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }*/
                break;

        }
    }



    protected void checkPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                    // show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setMessage("Call Phone permission is required.");
                    builder.setTitle("Please grant permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    mActivity,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSION_REQUEST_CODE
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            mActivity,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSION_REQUEST_CODE
                    );
                }
            }else {
                // Permission already granted
                CallContact();
            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case MY_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission granted
                    CallContact();
                }else {
                    // Permission denied
                }
            }
        }
    }


    // Custom method to call a phone number programmatically using dialer app
    protected void CallContact() {
        /*
            Intent
                An intent is an abstract description of an operation to be performed. It can be
                used with startActivity to launch an Activity, broadcastIntent to send it to any
                interested BroadcastReceiver components, and startService(Intent) or
                bindService(Intent, ServiceConnection, int) to communicate with a background Service.

                An Intent provides a facility for performing late runtime binding between the code
                in different applications. Its most significant use is in the launching of
                activities, where it can be thought of as the glue between activities.
                It is basically a passive data structure holding an abstract description
                of an action to be performed.
        */
        /*
            String ACTION_CALL
                Activity Action: Perform a call to someone specified by the data.

                Input: If nothing, an empty dialer is started; else getData() is URI of a phone
                number to be dialed or a tel: URI of an explicit phone number.

                Output: nothing.

                Note: there will be restrictions on which applications can initiate a
                call; most applications should use the ACTION_DIAL.

                Note: this Intent cannot be used to call emergency numbers. Applications can
                dial emergency numbers using ACTION_DIAL, however.

                Note: if you app targets M and above and declares as using the
                CALL_PHONE permission which is not granted, then attempting to use this
                action will result in a SecurityException.

                Constant Value: "android.intent.action.CALL"
        */
        // Initialize an intent to make phone call using dialer app
        // It open the dialer app and make call to the given phone number automatically
        Intent intent = new Intent(Intent.ACTION_DIAL);
        // Send phone number to intent as data
        intent.setData(Uri.parse("tel:" + strcall));
        // Start the dialer app activity to make phone call
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);

    }











}


