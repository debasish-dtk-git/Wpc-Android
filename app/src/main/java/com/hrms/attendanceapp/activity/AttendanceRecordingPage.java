package com.hrms.attendanceapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.Constants;

public class AttendanceRecordingPage extends AppCompatActivity {
    private ImageView imageView;
    private ImageView imgLogo;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private TextView textrecMess;

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
        setContentView(R.layout.activity_attendance_recording_page);

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

      /*  strId = sh_Pref.getString("USER_CODE", "");
        String strName = sh_Pref.getString("USER_NAME", "");
        String strcomName = sh_Pref.getString("COM_NAME", "");*/
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");

        String mess = getIntent().getStringExtra("TIMEIN");

        imgLogo = (ImageView)findViewById(R.id.imageview_logo__);

       textrecMess =  (TextView)findViewById(R.id.textview_attendancerecd__);
       textrecMess.setText(mess);

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(AttendanceRecordingPage.this)
                    .load(imurl)
                    .into(imgLogo);


        }



        imageView = (ImageView)findViewById(R.id.imageview_attendance_back_);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceRecordingPage.this, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
