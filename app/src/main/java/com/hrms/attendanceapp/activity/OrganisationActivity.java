package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.adapter.LeaveAdapter;
import com.hrms.attendanceapp.adapter.LeaveApproveAdapter;
import com.hrms.attendanceapp.adapter.MonitoringReportAdapter;
import com.hrms.attendanceapp.adapter.ProductAdapter;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.LeaveList;
import com.hrms.attendanceapp.getset.MonitorList;
import com.hrms.attendanceapp.getset.SubMonitorList;
import com.hrms.attendanceapp.getset.Upcomingholiday;
import com.hrms.attendanceapp.getset.ViewLeaveList;
import com.hrms.attendanceapp.utils.CustomProgress;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrganisationActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linUp_profile;
    private LinearLayout linEmp_org;
    private LinearLayout lin_logout;
    private TextView txtEmp;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    View layouttobring;
    private Animation animUp;
    private Animation animDown;
    LinearLayout slide_menu;
    int flag_swip1=0;
    int flag = 0;
    float x1,x2;
    float y1, y2;
    private ImageView imgmenu;

    //global declaration
    private TextView timeUpdate;
    Calendar calendar;
    private String dayName = "";
    private String currentDate = "";
    private String strReg = "";
    private ImageView imgSliderLogo;
    private LinearLayout linDept;
    private LinearLayout linDesig;
    private LinearLayout linEmpType;
    private LinearLayout linPaygroup;
    private LinearLayout linAnnulpay;
    private LinearLayout linBank;
    private LinearLayout linTaxMaster;
    private LinearLayout linPayType;
    private LinearLayout lin;
    private LinearLayout linWedgespayMode;
    private LinearLayout linMigrantEmployee;
    private String strEmpUserIdd = "";
    private TextView txtActiveEmp, txtMigrantEmp;

    //private LinearLayout linMonitoringReporting;
    private LinearLayout linActiveEmp;
    private LinearLayout linMigrantEmp;
    public static TextView txtCartBadge;
    private LinearLayout linMesscentre;
    private ImageView imgNoti;
    private LinearLayout linMonitoringReporting;
    private LinearLayout linOrgProfile;
    private LinearLayout linRightToWorkCheck;
    private LinearLayout linChangeOfCircumstances;
    private LinearLayout linSponsorManagementDossier;
    private LinearLayout linKeyContact;
    private LinearLayout linContractAgreement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(OrganisationActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_organisation);

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        strReg = sh_Pref.getString("REG", "");
        strEmpUserIdd = sh_Pref.getString("USER_IDD", "");



        //in onStart()
        calendar = Calendar.getInstance();
        //date format is:  "Date-Month-Year Hour:Minutes am/pm"
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM"); //Date and time
        currentDate = sdf.format(calendar.getTime());

        //Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
       /* SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();
        dayName = sdf_.format(date);*/
        lin = (LinearLayout)findViewById(R.id.act_dashboard);


        timeUpdate = (TextView) findViewById(R.id.timeUpdate); //initialize in onCreate()
        timeUpdate.setText("  |  " + currentDate + "");


        imgmenu = (ImageView)findViewById(R.id.imageview_menu_org);
        imgmenu.setOnClickListener(this);


        linActiveEmp = (LinearLayout)findViewById(R.id.lin__active_employees);
        linActiveEmp.setOnClickListener(this);
        linMigrantEmp = (LinearLayout)findViewById(R.id.lin__migrant_employees);
        linMigrantEmp.setOnClickListener(this);
        txtActiveEmp = (TextView)findViewById(R.id.textview_active_employees);
        txtMigrantEmp = (TextView)findViewById(R.id.textview_migrant_employees);

        linMonitoringReporting = (LinearLayout)findViewById(R.id.lin_monitoring_reporting);
        linMonitoringReporting.setOnClickListener(this);


        linOrgProfile = (LinearLayout)findViewById(R.id.lin_org_profile);
        linOrgProfile.setOnClickListener(this);

       linRightToWorkCheck = (LinearLayout)findViewById(R.id.lin_right_to_work_check);
       linRightToWorkCheck.setOnClickListener(this);


        linChangeOfCircumstances= (LinearLayout)findViewById(R.id.lin_change_of_circumstances);
        linChangeOfCircumstances.setOnClickListener(this);


        linSponsorManagementDossier = (LinearLayout)findViewById(R.id.lin_sponsor_management_dossier);
        linSponsorManagementDossier.setOnClickListener(this);

        linKeyContact = (LinearLayout)findViewById(R.id.lin_key_contact);
        linKeyContact.setOnClickListener(this);

        linContractAgreement = (LinearLayout)findViewById(R.id.lin_contract_agreement);
        linContractAgreement.setOnClickListener(this);





        txtCartBadge = (TextView) findViewById(R.id.cart_badge_dashboard);

        imgNoti = (ImageView)findViewById(R.id.imageview_notification);
        imgNoti.setOnClickListener(this);


        layouttobring = findViewById(R.id.slide_menu_list_org);
        animUp = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_right);
        animDown = AnimationUtils.loadAnimation(this, R.anim.anim_slide_out_right);
        slide_menu=(LinearLayout) layouttobring.findViewById(R.id.slide_menu_layout_org);


        imgSliderLogo = (ImageView)layouttobring.findViewById(R.id.imageview_logo_slider_org);



        txtEmp = (TextView)layouttobring.findViewById(R.id.textview_empname_org);




        userProfile(strReg);
        userHoliday(strEmpUserIdd);







        linUp_profile = (LinearLayout)findViewById(R.id.lin_updateprofile_org);
        linUp_profile.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, OrgProfileActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });


        linEmp_org = (LinearLayout)findViewById(R.id.lin_emp_org);
        linEmp_org.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, EmployeeActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });



        linDept = (LinearLayout)findViewById(R.id.lin_dept_org);
        linDept.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, DepartmentActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });

        linDesig = (LinearLayout)findViewById(R.id.lin_desig_org);
        linDesig.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, DesignationActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });


        linEmpType = (LinearLayout)findViewById(R.id.lin_emptype_org);
        linEmpType.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, EmploymentTypeActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });



      /*  linPaygroup = (LinearLayout)findViewById(R.id.lin_paygroup_org);
        linPaygroup.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, PayGroupActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });



        linAnnulpay = (LinearLayout)findViewById(R.id.lin_annulpay_org);
        linAnnulpay.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, AnnualPayActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });


        linBank = (LinearLayout)findViewById(R.id.lin_bank_org);
        linBank.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, EmpBankActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });


        linTaxMaster = (LinearLayout)findViewById(R.id.lin_taxmaster_org);
        linTaxMaster.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, TaxMasterActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });


        linPayType = (LinearLayout)findViewById(R.id.lin_paytype_org);
        linPayType.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, PaymentTypeActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });



        linWedgespayMode = (LinearLayout)findViewById(R.id.lin_wedgespay_mode);
        linWedgespayMode.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, WedgesPayModeActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });
*/
        linMigrantEmployee = (LinearLayout)findViewById(R.id.lin_migrant_employee);
        linMigrantEmployee.setOnClickListener(new View.OnClickListener() {

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
                            Intent intent1 = new Intent(OrganisationActivity.this, MigrantEmployeeActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });


        linMesscentre = (LinearLayout)findViewById(R.id.lin_messg_centre_org);
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
                            Intent intent1 = new Intent(OrganisationActivity.this, SubmitNorifiActivity.class);
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });


                closeActivity.start();




            }
        });







        lin_logout = (LinearLayout)layouttobring.findViewById(R.id.lin_logout_org);
        lin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layouttobring.startAnimation(animDown);

                layouttobring.setVisibility(View.GONE);
                flag_swip1 = 0;
                flag = 0;
                callLogout();
            }
        });






    }

    @Override
    protected void onResume() {
        super.onResume();

        userNoti(strEmpUserIdd);
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
















   /* @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub


        switch (event.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = event.getX();
                y1 = event.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = event.getX();
                y2 = event.getY();

                //if left to right sweep event on screen
                if (x1 > x2 && flag_swip1==1)
                {
                    layouttobring.startAnimation(animDown);
   				*//*MainActivity.this.overridePendingTransition(R.anim.slide_left_in,
   		                   R.anim.slide_left_out);*//*
                    layouttobring.setVisibility(View.GONE);

                    flag_swip1=0;
                    Thread closeActivity = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                // Do some stuff
                                //listvehicles.setEnabled(true);
                                //finish();
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                            }
                        }
                    });


                    closeActivity.start();


                }


            }
        }




        return super.dispatchTouchEvent(event);
    }
*/




    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new AlertDialog.Builder(OrganisationActivity.this)
                // .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OrganisationActivity.super.onBackPressed();
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










    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageview_menu_org:
                //  Toast.makeText(MainActivity.this, "Alert", Toast.LENGTH_LONG).show();
                layouttobring.setVisibility(View.VISIBLE);

                layouttobring.startAnimation(animUp);

                flag_swip1=1;
                flag = 1;
                break;
            case R.id.imageview_notification:

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
                                Intent intentnoti = new Intent(OrganisationActivity.this, NotificationActivity.class);
                                startActivity(intentnoti);

                            } catch (Exception e) {
                                e.getLocalizedMessage();
                            }
                        }
                    });


                    closeActivity.start();


                }
                else {
                    Intent intentnoti = new Intent(OrganisationActivity.this, NotificationActivity.class);
                    startActivity(intentnoti);
                }
                break;
            case R.id.lin__active_employees:
                if(flag_swip1==0 && flag==0) {
                    Intent intent1 = new Intent(OrganisationActivity.this, ActiveEmployeesActivity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.lin__migrant_employees:
                if(flag_swip1==0 && flag==0) {
                    Intent intent2 = new Intent(OrganisationActivity.this, MigrantEmployeesActivity.class);
                    startActivity(intent2);
                }
                break;
            case R.id.lin_monitoring_reporting:
                if(flag_swip1==0 && flag==0) {
                    Intent intentm = new Intent(OrganisationActivity.this, MonitoringReportingActivity.class);
                    startActivity(intentm);
                }
                break;
            case R.id.lin_org_profile:
                if(flag_swip1==0 && flag==0) {
                    Intent intentorp = new Intent(OrganisationActivity.this, OrgProfileActivity.class);
                    startActivity(intentorp);
                }
                break;
            case R.id.lin_right_to_work_check:
                if(flag_swip1==0 && flag==0) {
                    Intent intentrtochk = new Intent(OrganisationActivity.this, RightToWorkActivity.class);
                    startActivity(intentrtochk);
                }
                break;
            case R.id.lin_change_of_circumstances:
                if(flag_swip1==0 && flag==0) {
                    Intent intentchngofcir = new Intent(OrganisationActivity.this, ChangeOfCircumstanceActivity.class);
                    startActivity(intentchngofcir);
                }

                break;
            case R.id.lin_sponsor_management_dossier:
                if(flag_swip1==0 && flag==0) {
                    Intent intentspon = new Intent(OrganisationActivity.this, SponsorManagementDossierActivity.class);
                    startActivity(intentspon);
                }
                break;
            case R.id.lin_key_contact:
                if(flag_swip1==0 && flag==0) {

                    Intent intentkeycon = new Intent(OrganisationActivity.this, KeyContactActivity.class);
                    startActivity(intentkeycon);
                }
                break;
            case R.id.lin_contract_agreement:
                if(flag_swip1==0 && flag==0) {
                    Intent intentconagree = new Intent(OrganisationActivity.this, ContractAgreementActivity.class);
                    startActivity(intentconagree);
                }
                break;

        }
    }



    private void userProfile(String strUserid) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(OrganisationActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getViewprofileOrg(strUserid);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        loginRespn(jsonresponse);

                    } else {

                        Snackbar snackbar = Snackbar
                                .make(lin, "No Data Found, Please try again later.", Snackbar.LENGTH_LONG);


                        snackbar.show();



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

    private void loginRespn(String jsonresponse) {

        try {

            JSONObject jresData = new JSONObject(jsonresponse);

            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if(satus.equalsIgnoreCase("true"))
                {



                    JSONObject jsonobj = jresData.getJSONObject("0");
                    String id = jsonobj.getString("id");

                    String compname= jsonobj.getString("com_name");
                    txtEmp.setText("Welcome "+compname);

                    String fname = jsonobj.getString("f_name");
                    String lname = jsonobj.getString("l_name");
                    String email = jsonobj.getString("email");
                    String phNo = jsonobj.getString("p_no");


                    String fax = jsonobj.getString("fax");





                    String website = jsonobj.getString("website");


                    String pan = jsonobj.getString("pan");


                    String logo = jsonobj.getString("logo");
                    if(logo.equalsIgnoreCase(""))
                    {
                        //setting the bitmap from the drawable folder
                        Bitmap bitmap = BitmapFactory.decodeResource(OrganisationActivity.this.getResources(), R.drawable.profile_pic);

                        //set the image to the imageView
                        imgSliderLogo.setImageBitmap(bitmap);

                    }
                    else{
                        String imurl = Constants.img_url + logo;
                        Glide
                                .with(OrganisationActivity.this)
                                .load(imurl)
                                .into(imgSliderLogo);

                    }

                    String designation = jsonobj.getString("desig");


                    String comReg = jsonobj.getString("com_reg");


                    String str_tradename = jsonobj.getString("trad_name");

                    String compyType = jsonobj.getString("com_type");





                    String str_comYear = jsonobj.getString("com_year");






                    String str_comNat = jsonobj.getString("com_nat");

                    String str_NoofEmp = jsonobj.getString("no_em");

                    String str_noemwork = jsonobj.getString("no_em_work");


                    String str_workper = jsonobj.getString("work_per");




                    String str_NoofDirector = jsonobj.getString("no_dire");






                    String str_comContact = jsonobj.getString("con_num");
                    String str_mail= jsonobj.getString("authemail");













                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(OrganisationActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }







        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }





    private void userHoliday(String strUserid) {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getViewHoliday(strUserid);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {


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

        Constants.monitorList.clear();
        JSONObject jresData = null;
        try {
            jresData = new JSONObject(response);
            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if (satus.equalsIgnoreCase("true")) {


                    String usertype = jresData.getString("user_type");
                    String totEmp = jresData.getString("total_employee");
                    String totMigrant = jresData.getString("total_migrant");

                    txtActiveEmp.setText(totEmp);
                    txtMigrantEmp.setText(totMigrant);


                    JSONArray jsonarry = jresData.getJSONArray("monitoring");
                    for(int i=0;i<jsonarry.length();i++)
                    {
                        MonitorList monitorobj = new MonitorList();
                        ArrayList<SubMonitorList> subcat1 = new ArrayList<SubMonitorList>();

                        JSONObject jsonobj = jsonarry.getJSONObject(i);

                        SubMonitorList appsucat1 = new SubMonitorList();



                        String strempcode = jsonobj.getString("emp_code");
                        monitorobj.setEmpcode(strempcode);

                        String strFname = jsonobj.getString("emp_fname");


                        String strMname = jsonobj.getString("emp_mname");

                        String strLname = jsonobj.getString("emp_lname");
                        if(strMname.equalsIgnoreCase(""))
                              monitorobj.setName(strFname+" "+strLname);
                        else{
                            monitorobj.setName(strFname+" "+strMname+" "+strLname);
                        }

                        String strdob = jsonobj.getString("emp_dob");
                        monitorobj.setDob(strdob);
                        appsucat1.setDob(strdob);

                        String strpsPhone = jsonobj.getString("emp_ps_phone");
                        monitorobj.setPhone(strpsPhone);
                        appsucat1.setPhone(strpsPhone);


                        String strNationality= jsonobj.getString("nationality");
                        monitorobj.setNationality(strNationality);
                        appsucat1.setNationality(strNationality);

                        String strNino = jsonobj.getString("ni_no");
                        monitorobj.setNino(strNino);
                        appsucat1.setNino(strNino);

                        String strVisaexpdate = jsonobj.getString("visa_exp_date");
                        monitorobj.setVisa_exp_date(strVisaexpdate);
                        appsucat1.setVisa_exp_date(strVisaexpdate);

                        String strVisaexpdate90 = jsonobj.getString("visa_exp_date_90");
                        monitorobj.setVisa_exp_date_90(strVisaexpdate90);
                        appsucat1.setVisa_exp_date_90(strVisaexpdate90);

                        String strVisaexpdate60 = jsonobj.getString("visa_exp_date_60");
                        monitorobj.setVisa_exp_date_60(strVisaexpdate60);
                        appsucat1.setVisa_exp_date_60(strVisaexpdate60);

                        String strVisaexpdate30 = jsonobj.getString("visa_exp_date_30");
                        monitorobj.setVisa_exp_date_30(strVisaexpdate30);
                        appsucat1.setVisa_exp_date_30(strVisaexpdate30);

                        String strPass_doc_no = jsonobj.getString("pass_doc_no");
                        monitorobj.setPass_doc_no(strPass_doc_no);
                        appsucat1.setPass_doc_no(strPass_doc_no);

                        String strAddress = jsonobj.getString("address");
                        monitorobj.setAddress(strAddress);
                        appsucat1.setAddress(strAddress);

                        subcat1.add(appsucat1);
                        monitorobj.setMonitoringsub(subcat1);

                        Constants.monitorList.add(monitorobj);


                    }



                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(OrganisationActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }





        } catch (JSONException e) {


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
                                    Toast.makeText(OrganisationActivity.this, error, Toast.LENGTH_LONG).show();
                                }
                            }





                        } catch (JSONException e) {


                        }


                    } else {

                        Snackbar snackbar = Snackbar
                                .make(lin, "No Data Found, Please try again later.", Snackbar.LENGTH_LONG);


                        snackbar.show();

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







    private void callLogout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrganisationActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.app_icon);
        builder.setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        toEdit.putString("REG", "");
                        toEdit.putString("COM_NAME", "");
                        toEdit.putString("COM_LOGO", "");
                        toEdit.putString("USER_TYPE", "");


                        toEdit.commit();
                        toEdit.clear();
                        Intent homeIntent = new Intent(OrganisationActivity.this, LoginActivity.class);
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


}
