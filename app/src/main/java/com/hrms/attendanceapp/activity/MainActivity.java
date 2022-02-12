package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.adapter.LeaveAdapter;
import com.hrms.attendanceapp.adapter.LeaveApproveAdapter;
import com.hrms.attendanceapp.adapter.ProductAdapter;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.HolidayList;
import com.hrms.attendanceapp.getset.LeaveList;
import com.hrms.attendanceapp.getset.Upcomingholiday;
import com.hrms.attendanceapp.getset.ViewLeaveList;
import com.hrms.attendanceapp.utils.CustomProgress;
import com.hrms.attendanceapp.utils.GlideImageLoader;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private LinearLayout linUp_profile;
    private LinearLayout linHolidayList;
    private LinearLayout linPayslip;
    private LinearLayout linAttendance;
    private LinearLayout linApplyLeave;
    private LinearLayout linLvStatus;
    private LinearLayout linsupport;
    private TextView txtEmp;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private LinearLayout linDailyAttendence;
    private LinearLayout linDocUpload;
    private LinearLayout linAttenUpload;
    private Context context;
    private int width;
    private int height;
    View layouttobring;
    private Animation animUp;
    private Animation animDown;
    LinearLayout slide_menu;
    int flag_swip1=0;
    int flag = 0;
    float x1,x2;
    float y1, y2;
    private ImageView imgmenu;
    Integer count=0;
    private String dayName = "";
    private String currentDate = "";
    private TextView txtmain;
    private String versionName = "";
    int versionCode = -1;
    private String strURL = "";
    private final Handler handler = new Handler();
    private ImageView imgNoti;
    private ImageView imgSliderLogo;
    private LinearLayout lin, linMenuDash;
    private RecyclerView recyclerViewholidy;
    private RecyclerView recyclerViewleaveApprove;
    private TextView txt1, txt3;
    private TextView txt1_info, txt3_info;
    private String strleaveApprov = "";
    private LinearLayout linApproveLeave;
    private LinearLayout lin_logout;
    private String strProfileimg = "";
    private String imurl = "";
    private String strcontactagreement = "";
    private LinearLayout linContactAggrement;
    private LinearLayout linChangeOfCircumstance;
    public static TextView txtCartBadge;
    private LinearLayout linMesscentre;
    private TextView txtMain;
    private String strId = "";
    private LinearLayout linerMy_profile;
    private LinearLayout linerAttendance;
    private LinearLayout linerLeave_status;
    private LinearLayout linerChange_of_circumstamce;
    private LinearLayout linerLeave_application;
    private LinearLayout linerAttendance_sheet;
    private LinearLayout linerContract_agreement;
    private LinearLayout linerMessage_centre;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(MainActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        strId = sh_Pref.getString("USER_CODE", "");
        String strName = sh_Pref.getString("USER_NAME", "");
        String strcomName = sh_Pref.getString("COM_NAME", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");



        strcontactagreement = sh_Pref.getString("Contract_agrrement", "");



        getVersionInfo();

        lin = (LinearLayout)findViewById(R.id.activity_main_);

        linMenuDash = (LinearLayout)findViewById(R.id.lin_menu_dashbd);
        txt1 = (TextView)findViewById(R.id.textview_headng1);
        //txt2 = (TextView)findViewById(R.id.textview_headng2);
        txt3 = (TextView)findViewById(R.id.textview_headng3);

        int numberOfColumns = 2;
        //getting the recyclerview from xml
        recyclerViewholidy = (RecyclerView) findViewById(R.id.recycler_view_holiday);
        recyclerViewholidy.setHasFixedSize(true);
        recyclerViewholidy.setLayoutManager(new GridLayoutManager(this, numberOfColumns));



        /*recyclerViewleave = (RecyclerView) findViewById(R.id.recycler_view_leave);
        recyclerViewleave.setHasFixedSize(true);
        recyclerViewleave.setLayoutManager(new LinearLayoutManager(this));*/


        recyclerViewleaveApprove = (RecyclerView) findViewById(R.id.recycler_view_leave_approv);
        recyclerViewleaveApprove.setHasFixedSize(true);
        recyclerViewleaveApprove.setLayoutManager(new LinearLayoutManager(this));



        userHoliday(strId);


        txtMain = (TextView)findViewById(R.id.text_main);
        txtMain.setText(strcomName);
        imgNoti = (ImageView)findViewById(R.id.imageview_notification_emp);
        imgNoti.setOnClickListener(this);

        txtCartBadge = (TextView) findViewById(R.id.cart_badge_dashboard_emp);




        imgmenu = (ImageView)findViewById(R.id.imageview_menu);
        imgmenu.setOnClickListener(this);

        linerMy_profile = (LinearLayout)findViewById(R.id.linerlay_my_profile_);
        linerMy_profile.setOnClickListener(this);

        linerAttendance = (LinearLayout)findViewById(R.id.linerlay_attendance_);
        linerAttendance.setOnClickListener(this);

        linerLeave_application = (LinearLayout)findViewById(R.id.linerlay_leave_application_);
        linerLeave_application.setOnClickListener(this);

        linerAttendance_sheet = (LinearLayout)findViewById(R.id.linerlay_attendance_sheet_);
        linerAttendance_sheet.setOnClickListener(this);

        linerChange_of_circumstamce = (LinearLayout)findViewById(R.id.linerlay_change_of_circumstamce_);
        linerChange_of_circumstamce.setOnClickListener(this);

        linerContract_agreement = (LinearLayout)findViewById(R.id.linerlay_contract_agreement_);
        linerContract_agreement.setOnClickListener(this);

        linerMessage_centre = (LinearLayout)findViewById(R.id.linerlay_message_centre_);
        linerMessage_centre.setOnClickListener(this);

        linerLeave_status = (LinearLayout)findViewById(R.id.linerlay_leave_status_);
        linerLeave_status.setOnClickListener(this);







        layouttobring = findViewById(R.id.slide_menu_list);
        animUp = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_right);
        animDown = AnimationUtils.loadAnimation(this, R.anim.anim_slide_out_right);


        slide_menu =(LinearLayout) layouttobring.findViewById(R.id.slide_menu_layout);
        layouttobring.setVisibility(View.GONE);

        imgSliderLogo = (ImageView)layouttobring.findViewById(R.id.imageview_logo_slider);
        progressbar = (ProgressBar)layouttobring.findViewById(R.id.homeprogress_slider);


        txtEmp = (TextView)layouttobring.findViewById(R.id.textview_empname);
        txtEmp.setText("Welcome "+strName);






        linUp_profile = (LinearLayout)layouttobring.findViewById(R.id.lin_upprofile);
        linUp_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                int flag = 0;

                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            // Do some stuff


                           Intent intent1 = new Intent(MainActivity.this, PersonalDtlsActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });

        linHolidayList = (LinearLayout)layouttobring.findViewById(R.id.lin_holiday_list);
        linHolidayList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                int flag = 0;
                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            // Do some stuff
                           Intent intent2 = new Intent(MainActivity.this, HolidayListActivity.class);
                            startActivity(intent2);
                            //finish();
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();
            }
        });

       /* linPayslip = (LinearLayout)layouttobring.findViewById(R.id.lin_payslip);
        linPayslip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);

                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10);
                            // Do some stuff
                           *//* Intent intent3 = new Intent(MainActivity.this, PayslipActivity.class);
                            startActivity(intent3);*//*

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();
            }
        });
*/
        linAttendance = (LinearLayout)layouttobring.findViewById(R.id.lin_attendance);
        linAttendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                int flag = 0;

                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            // Do some stuff
                            Intent intent4 = new Intent(MainActivity.this, AttendanceActivity.class);
                            startActivity(intent4);
                            //finish();
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();

            }
        });



            linApproveLeave = (LinearLayout)layouttobring.findViewById(R.id.lin_leaveappver);
            linApproveLeave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    layouttobring.startAnimation(animDown);

                    layouttobring.setVisibility(View.GONE);
                    flag_swip1=0;
                    int flag = 0;
                    Thread closeActivity = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                // Do some stuff
                                if(strleaveApprov.equalsIgnoreCase("yes")) {
                                    Intent intent5 = new Intent(MainActivity.this, LeaveApproverActivity.class);
                                    startActivity(intent5);
                                    //finish();
                                }
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                            }
                        }
                    });


                    closeActivity.start();
                }
            });


        linApplyLeave = (LinearLayout)layouttobring.findViewById(R.id.lin_leaveappli);
        linApplyLeave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                int flag = 0;
                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            // Do some stuff
                            Intent intent5 = new Intent(MainActivity.this, LeaveApplyActivity.class);
                            startActivity(intent5);
                            //finish();
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();
            }
        });

        linLvStatus = (LinearLayout)layouttobring.findViewById(R.id.lin_lv_status);
        linLvStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                int flag = 0;
                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            // Do some stuff
                            Intent intent6 = new Intent(MainActivity.this, LeaveStatusActivity.class);
                            startActivity(intent6);
                            //finish();
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();
            }
        });

        linDailyAttendence = (LinearLayout)layouttobring.findViewById(R.id.lin_dailyattendnst);
        linDailyAttendence.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                int flag = 0;
                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            // Do some stuff
                            Intent intent7 = new Intent(MainActivity.this, DailyAttendanceSheet.class);
                            startActivity(intent7);
                            //finish();
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();
            }
        });





        linContactAggrement = (LinearLayout)layouttobring.findViewById(R.id.lin_contact_aggrement);
        linContactAggrement.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                int flag = 0;
                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            if(strcontactagreement.equalsIgnoreCase("Yes")) {
                                Intent intent5 = new Intent(MainActivity.this, ContractAgrrementActivity.class);
                                startActivity(intent5);
                                //finish();
                            }
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();
            }
        });


        linChangeOfCircumstance = (LinearLayout)layouttobring.findViewById(R.id.lin_change_of_circumstnce);
        linChangeOfCircumstance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                int flag = 0;
                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);

                                Intent intent5 = new Intent(MainActivity.this, ChangeCircumstancesActivity.class);
                                startActivity(intent5);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();
            }
        });

        linMesscentre = (LinearLayout)findViewById(R.id.lin_messg_centre_emp);
        linMesscentre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                flag = 0;

                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            // Do some stuff
                            Intent intent1 = new Intent(MainActivity.this, SubmitEmpNorifiActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });









        linsupport = (LinearLayout)layouttobring.findViewById(R.id.support);
        linsupport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1=0;
                int flag = 0;
                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            // Do some stuff
                           Intent intent7 = new Intent(MainActivity.this,SupportActivity.class);
                            startActivity(intent7);
                            //finish();
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();
            }
        });




        lin_logout = (LinearLayout)layouttobring.findViewById(R.id.logout);
        lin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1 = 0;
                int flag = 0;
                callLogout();
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        userNoti(strId);

        strProfileimg = sh_Pref.getString("USER_PHOTO", "");

        imurl = Constants.img_url+strProfileimg;
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profile_pic)
                .error(R.drawable.profile_pic)
                .priority(Priority.HIGH);

        new GlideImageLoader(imgSliderLogo, progressbar).load(imurl,options);



    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub


        float StartX = 0, EndX = 0, EndY = 0, diffX, diffY;
        float StartY = 0;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                StartX = event.getX();
                StartY = event.getY();
                Constants.StartX=(int) StartX;
                Constants.EndX=(int) StartX;
                Constants.StartY=(int) StartY;
                Constants.EndY=(int) StartY;
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                EndX= event.getX();
                EndY= event.getY();
                Constants.EndX=(int) EndX;
                Constants.EndY=(int) EndY;
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                //Consume if necessary and perform the fling / swipe action
                //if it has been determined to be a fling / swipe
                break;
            }

        }
        diffX=Constants.EndX-Constants.StartX;
        diffY=Constants.EndY-Constants.StartY;
        if(diffX>diffY){
            if((diffX>70)&&(flag==0) && Constants.StartX<200){
                if(flag_swip1==0){

                    layouttobring.setVisibility(View.VISIBLE);

                    layouttobring.startAnimation(animUp);


                    flag=1;
                    flag_swip1=1;
                }
            }
        }
        if((diffX<-140)&&(flag==1)){
            if(flag_swip1==1){

                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);

                flag=0;
                flag_swip1=0;
            }
        }
        return super.dispatchTouchEvent(event);

    }












    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new AlertDialog.Builder(MainActivity.this)
                // .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                        quit();
                    }
                }).create().show();
    }

    public void quit() {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);
    }









    private void userHoliday(String strUserid) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(MainActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getViewHoliday(strUserid);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        holidayRespn(jsonresponse);

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




    private void holidayRespn(String response) {

        ArrayList<Upcomingholiday> upcomhlidyList = new ArrayList<Upcomingholiday>();
        JSONObject jresData = null;
        try {
            jresData = new JSONObject(response);
            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if (satus.equalsIgnoreCase("true")) {

                    strleaveApprov =  jresData.getString("Leave_approver");
                    strProfileimg =  jresData.getString("img");

                    toEdit.putString("USER_PHOTO", strProfileimg);
                    toEdit.commit();

                    String imurl = Constants.img_url + strProfileimg;
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
                            .error(R.drawable.profile_pic)
                            .priority(Priority.HIGH);

                    new GlideImageLoader(imgSliderLogo, progressbar).load(imurl,options);


                    txt1.setVisibility(View.VISIBLE);
                    //txt2.setVisibility(View.VISIBLE);
                    txt3.setVisibility(View.VISIBLE);
                    JSONArray jsonarry = jresData.getJSONArray(String.valueOf(0));
                    for(int i=0;i<jsonarry.length();i++)
                    {
                        JSONObject jsonobj = jsonarry.getJSONObject(i);


                        Upcomingholiday upholiobj = new Upcomingholiday();

                        String strid = jsonobj.getString("id");
                        upholiobj.setId(strid);

                        String stryear = jsonobj.getString("years");
                        upholiobj.setYear(stryear);

                        String strholidaytype = jsonobj.getString("holiday_descripion");
                        upholiobj.setHolidaytype(strholidaytype);

                        String strformdate = jsonobj.getString("from_date");
                        upholiobj.setFromdate(strformdate);




                        String strtodate = jsonobj.getString("to_date");
                        String strnoday = jsonobj.getString("day");


                        upcomhlidyList.add(upholiobj);


                    }
                    if(upcomhlidyList.size()>0) {
                        ProductAdapter adapter = new ProductAdapter(this, upcomhlidyList);

                        //setting adapter to recyclerview
                        recyclerViewholidy.setAdapter(adapter);
                        linMenuDash.setVisibility(View.VISIBLE);

                    }
                    else{
                        txt1_info = (TextView)findViewById(R.id.textview_headng1_info);
                        txt1_info.setVisibility(View.VISIBLE);
                        linMenuDash.setVisibility(View.VISIBLE);
                    }
                    ArrayList<LeaveList> leaveList = new ArrayList<LeaveList>();
                    JSONArray jsonarryleave = jresData.getJSONArray(String.valueOf(1));
                    for(int i=0;i<jsonarryleave.length();i++)
                    {
                        JSONObject jsonobj = jsonarryleave.getJSONObject(i);


                        LeaveList leaveobj = new LeaveList();

                        String strid = jsonobj.getString("id");
                        leaveobj.setId(strid);

                        String ilvinhnd= jsonobj.getString("leave_in_hand");
                        leaveobj.setLeaveinhand(ilvinhnd);

                        String strtype = jsonobj.getString("leave_type_name");
                        leaveobj.setLeavetype(strtype);

                        String stralies = jsonobj.getString("alies");
                        leaveobj.setAlies(stralies);



                        leaveList.add(leaveobj);


                    }

                   /* if(leaveList.size()>0) {
                        LeaveAdapter leavadapter = new LeaveAdapter(this, leaveList);

                        //setting adapter to recyclerview
                        recyclerViewleave.setAdapter(leavadapter);

                    }
                    else{
                        txt2_info = (TextView)findViewById(R.id.textview_headng2_info);
                        txt2_info.setVisibility(View.VISIBLE);
                    }*/

                    ArrayList<ViewLeaveList> vlvList_ = new ArrayList<ViewLeaveList>();
                    JSONArray jsonarryviewleav = jresData.getJSONArray("2");
                    for(int i=0;i<jsonarryviewleav.length();i++)
                    {
                        JSONObject jsonobj = jsonarryviewleav.getJSONObject(i);


                        ViewLeaveList levobj = new ViewLeaveList();

                        String id= jsonobj.getString("id");


                        String leavetype = jsonobj.getString("leave_type_name");
                        levobj.setLeavetype(leavetype);

                        String dateofApply = jsonobj.getString("date_of_apply");
                        levobj.setDateOfApply(dateofApply);

                        String fdate = jsonobj.getString("from_date");
                        levobj.setFromdate(fdate);

                        String tdate= jsonobj.getString("to_date");
                        levobj.setTodate(tdate);

                        String nofleav = jsonobj.getString("no_of_leave");
                        levobj.setNoOfLeave(nofleav);

                        String status= jsonobj.getString("status");
                        levobj.setStatus(status);

                        String status_remarks= jsonobj.getString("status_remarks");
                        levobj.setRemarks(status_remarks);




                        vlvList_.add(levobj);

                    }
                 if(vlvList_.size()>0) {

                     LeaveApproveAdapter leavapvdapter = new LeaveApproveAdapter(this, vlvList_);

                     //setting adapter to recyclerview
                     recyclerViewleaveApprove.setAdapter(leavapvdapter);
                 }else{
                        txt3_info = (TextView)findViewById(R.id.textview_headng3_info);
                        txt3_info.setVisibility(View.VISIBLE);
                    }



                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }





        } catch (JSONException e) {


        }

    }








    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageview_menu:
                layouttobring.setVisibility(View.VISIBLE);

                layouttobring.startAnimation(animUp);

                flag_swip1=1;
                flag = 1;
                break;
            case R.id.imageview_notification_emp:

                if(flag_swip1==1 && flag==1)
                {
                    layouttobring.startAnimation(animDown);

                    layouttobring.setVisibility(View.GONE);
                    flag_swip1=0;
                    flag = 0;

                    Thread closeActivity = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                // Do some stuff
                                Intent intentnoti = new Intent(MainActivity.this, NotificationEmpActivity.class);
                                startActivity(intentnoti);

                            } catch (Exception e) {
                                e.getLocalizedMessage();
                            }
                        }
                    });


                    closeActivity.start();


                }else {
                    Intent intentnoti = new Intent(MainActivity.this, NotificationEmpActivity.class);
                    startActivity(intentnoti);
                }
                break;
            case R.id.linerlay_my_profile_:
                Intent intent1 = new Intent(MainActivity.this, PersonalDtlsActivity.class);
                startActivity(intent1);
                break;
            case R.id.linerlay_attendance_:
                Intent intent2 = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(intent2);
                break;
            case R.id.linerlay_leave_application_:
                Intent intent3 = new Intent(MainActivity.this, LeaveApplyActivity.class);
                startActivity(intent3);
                break;
            case R.id.linerlay_attendance_sheet_:
                if(flag_swip1==0 && flag==0) {
                    Intent intent4 = new Intent(MainActivity.this, DailyAttendanceSheet.class);
                    startActivity(intent4);
                }
                break;
            case R.id.linerlay_change_of_circumstamce_:
                Intent intent5 = new Intent(MainActivity.this, ChangeCircumstancesActivity.class);
                startActivity(intent5);
                break;
            case R.id.linerlay_contract_agreement_:
                if(strcontactagreement.equalsIgnoreCase("Yes")) {
                    Intent intent6 = new Intent(MainActivity.this, ContractAgrrementActivity.class);
                    startActivity(intent6);
                }
                break;
            case R.id.linerlay_message_centre_:
                Intent intent7 = new Intent(MainActivity.this, SubmitEmpNorifiActivity.class);
                startActivity(intent7);
                break;
            case R.id.linerlay_leave_status_:
                if(flag_swip1==0 && flag==0) {
                    Intent intent8 = new Intent(MainActivity.this, LeaveStatusActivity.class);
                    startActivity(intent8);
                }
                break;

        }
    }







    private void userNoti(String strUserid) {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getViewNoti(strUserid);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        JSONObject jresData = null;
                        try {
                            jresData = new JSONObject(jsonresponse);
                            if (jresData.has("status")) {
                                String satus = jresData.getString("status");
                                if (satus.equalsIgnoreCase("true")) {


                                    String usertype = jresData.getString("user_type");
                                    String countNoti = jresData.getString("countnotification");

                                    setUploadBadge(Integer.parseInt(countNoti));
                                }
                                else{
                                    String error = jresData.getString("msg");
                                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                                }
                            }





                        } catch (JSONException e) {


                        }


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




    public static void setUploadBadge(int itemCount) {

        if (txtCartBadge != null) {
            if (itemCount == 0) {
                if (txtCartBadge.getVisibility() != View.GONE) {
                    txtCartBadge.setVisibility(View.GONE);
                }
            } else {
                txtCartBadge.setText(String.valueOf(itemCount));
                if (txtCartBadge.getVisibility() != View.VISIBLE) {
                    txtCartBadge.setVisibility(View.VISIBLE);
                }
            }
        }

    }



    //get the current version number and name
    private void getVersionInfo() {

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }


    private void callLogout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.app_icon);
        builder.setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        toEdit.putString("EMID", "");
                        toEdit.putString("EMPCODE", "");
                        toEdit.putString("USER_CODE", "");
                        toEdit.putString("USER_NAME", "");
                        toEdit.putString("COM_LOGO", "");
                        toEdit.putString("COM_NAME", "");
                        toEdit.putString("USER_TYPE", "");
                        toEdit.putString("Contract_agrrement", "");


                        toEdit.commit();
                        toEdit.clear();
                        Intent homeIntent = new Intent(MainActivity.this, LoginActivity.class);
                        //homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(homeIntent);
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }






    public void showAlert(final String message, final String title)
    {

        Runnable updater = new Runnable()
        {
            public void run()
            {
                new AlertDialog.Builder(MainActivity.this).setTitle(title)
                        .setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // code to cancel the "quit" box
                        // dialog1.cancel();
                    }
                })
                        .show();
            }
        };
        handler.post(updater);
    }

}
