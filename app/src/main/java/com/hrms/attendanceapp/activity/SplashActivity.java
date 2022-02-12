package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hrms.attendanceapp.R;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private String userType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /** Hiding Title bar of this activity screen */
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        /** Making this activity, full screen */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        userType = sh_Pref.getString("USER_TYPE", "");

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 2 seconds
                    sleep(2*1000);


                  if(userType.equalsIgnoreCase("")) {
                        // After 5 seconds redirect to another intent
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);

                        //Remove activity
                        finish();
                    }
                    else if(userType.equalsIgnoreCase("employee")){
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        // i.putExtra("EMPNAME", userName);
                        startActivity(i);

                        //Remove activity
                        finish();

                    }
                    else if(userType.equalsIgnoreCase("employer"))
                  {
                      Intent intent_dashboard = new Intent(SplashActivity.this, OrganisationActivity.class);
                      startActivity(intent_dashboard);
                      finish();
                  }

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();

    }








    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
