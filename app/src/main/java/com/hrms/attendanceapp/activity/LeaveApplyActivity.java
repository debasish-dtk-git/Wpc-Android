package com.hrms.attendanceapp.activity;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.adapter.NothingSelectedSpinnerAdapter;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.HolidayList;
import com.hrms.attendanceapp.getset.Leave;
import com.hrms.attendanceapp.utils.CustomProgress;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LeaveApplyActivity extends AppCompatActivity implements View.OnClickListener {

    DatePickerDialog picker;
    Calendar cldr;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private String strId = "";
    private LinearLayout lin;
    private ImageView imgLogo;
    private TextView txtLeave;
    private Spinner spinnerLeave;
    private TextView txtLeavehand;
    private TextView txtFromDate;
    private TextView txttoDate;
    private TextView txtDayNo;
    private TextView txtApplyDate;
    private String currntformattedDate = "";
    private ArrayAdapter<Leave> spinnerLeaveArrayAdapter;
    private Button btnLeaveAply;
    private String strLeaveId = "";
    private String strName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setStatusBarGradiant(LeaveApplyActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leave_apply);

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();


        Calendar c = Calendar.getInstance();


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currntformattedDate = df.format(c.getTime());

        strId = sh_Pref.getString("USER_CODE", "");
        strName = sh_Pref.getString("USER_NAME", "");
        String strcomName = sh_Pref.getString("COM_NAME", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");


        lin = (LinearLayout)findViewById(R.id.activity_leav);

        imgBack = (ImageView)findViewById(R.id.imageview_leaveapp_back);
        imgBack.setOnClickListener(this);

        txtLeave = (TextView)findViewById(R.id.textview_leaveapp_heading);
        txtLeave.setText(strName);


        imgLogo = (ImageView)findViewById(R.id.imageview_leaveapp_logo);

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(LeaveApplyActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }

        spinnerLeave = (Spinner)findViewById(R.id.spinner_item_leaveapply);

        txtLeavehand = (TextView)findViewById(R.id.textview_employee_leav_in_hand);
        txtFromDate = (TextView)findViewById(R.id.textview_employee_from_date);
        txtFromDate.setOnClickListener(this);
        txttoDate = (TextView)findViewById(R.id.textview_employee_to_date);
        txttoDate.setOnClickListener(this);
        txtDayNo = (TextView)findViewById(R.id.textview_employee_no_days);

        txtApplyDate = (TextView)findViewById(R.id.textview_employee_current_date);

        txtApplyDate.setText(Utils.parseDateToddMMyyyy(currntformattedDate));
        btnLeaveAply = (Button)findViewById(R.id.button_leaveapply);
        btnLeaveAply.setOnClickListener(this);

        callLeaveType(strId);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_leaveapp_back:
                finish();
                break;
            case R.id.textview_employee_from_date:

                cldr = Calendar.getInstance();
                int day1 = cldr.get(Calendar.DAY_OF_MONTH);
                int month1 = cldr.get(Calendar.MONTH);
                int year1 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(LeaveApplyActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                                if(isAfterToday(year, monthOfYear, dayOfMonth))

                                txtFromDate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year1, month1, day1);
                picker.show();



                break;
            case R.id.textview_employee_to_date:

                cldr = Calendar.getInstance();
                int day2 = cldr.get(Calendar.DAY_OF_MONTH);
                int month2 = cldr.get(Calendar.MONTH);
                int year2 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(LeaveApplyActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                if(isAfterToday(year, monthOfYear, dayOfMonth)) //{
                                    txttoDate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);

                                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                                    String startDate = txtFromDate.getText().toString().trim();
                                   // String endDate = "03-03-2017";
                                    if (startDate.equalsIgnoreCase("From Date")) {

                                    } else {
                                        LocalDate startDateValue = LocalDate.parse(startDate, dateFormatter);
                                        LocalDate endDateValue = LocalDate.parse(txttoDate.getText().toString().trim(), dateFormatter);
                                        int days = (int) ChronoUnit.DAYS.between(startDateValue, endDateValue) + 1;
                                        txtDayNo.setText(String.valueOf(days));
                                    }
                               // }

                            }
                        }, year2, month2, day2);
                picker.show();



                break;

            case R.id.button_leaveapply:
               String leavehand =  txtLeavehand.getText().toString().trim();
               String formdat = txtFromDate.getText().toString().trim();

                String todat = txttoDate.getText().toString().trim();

                String dayNo = txtDayNo.getText().toString().trim();

                String applydat = txtApplyDate.getText().toString().trim();

                callSubmitLieaveApply(strId, strName, strLeaveId, leavehand, Utils.parseDateToyyyyMMdd(formdat), Utils.parseDateToyyyyMMdd(todat), dayNo, Utils.parseDateToyyyyMMdd(applydat));
                break;
        }
    }




    private void callLeaveType(String strId) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(LeaveApplyActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getLeaveTypelist(strId);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        leaveRespn(jsonresponse);

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


    private void leaveRespn(String jsonresponse) {

        try {

            JSONObject jresData = new JSONObject(jsonresponse);

            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if(satus.equalsIgnoreCase("true"))
                {



                    ArrayList<Leave> leaveList = new ArrayList<Leave>();
                    JSONArray jsonarry = jresData.getJSONArray("0");
                    for(int i=0;i<jsonarry.length();i++)
                    {
                        JSONObject jsonobj = jsonarry.getJSONObject(i);


                        Leave leaveobj = new Leave();



                        String id= jsonobj.getString("id");
                        leaveobj.setId(id);

                        String name = jsonobj.getString("leave_type_name");
                        leaveobj.setName(name);

                        String alies = jsonobj.getString("alies");


                        leaveList.add(leaveobj);
                    }


                    spinnerLeaveArrayAdapter = new ArrayAdapter(LeaveApplyActivity.this,R.layout.simple_spinner_item_leave, leaveList);
                    spinnerLeaveArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_item_leave);

                    spinnerLeave.setAdapter(new NothingSelectedSpinnerAdapter(spinnerLeaveArrayAdapter, R.layout.info_spinner_row_nothing_leavetype, LeaveApplyActivity.this));
                    //simple_spinner_item
                    spinnerLeave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            // TODO Auto-generated method stub
                            Leave selectedItemText = (Leave) parent.getItemAtPosition(position);
                            if(selectedItemText!=null){
                                strLeaveId = selectedItemText.getId();
                               // Toast.makeText(LeaveApplyActivity.this, strLeaveId, Toast.LENGTH_LONG).show();
                                callleaveinHand(strId, strLeaveId);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub

                        }});












                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(LeaveApplyActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }







        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void callleaveinHand(String strId, String strLeaveId) {



        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(LeaveApplyActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getLeaveHand(strId, strLeaveId);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        jsonRespn(jsonresponse);

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



    private void jsonRespn(String jsonresponse) {

        try {

            JSONObject jresData = new JSONObject(jsonresponse);

            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if(satus.equalsIgnoreCase("true"))
                {


                    String leave = jresData.getString("leave_inhand");
                    txtLeavehand.setText(leave);







                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(LeaveApplyActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }







        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }














    private void callSubmitLieaveApply(String strId, String strName, String strLeaveId, String leavehand, String fromdate, String todate, String dayNo, String currntdate) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(LeaveApplyActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getLvapplysubmit(strId, strName, strLeaveId, leavehand, fromdate, todate, dayNo, currntdate);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        jsonRespnsubmi(jsonresponse);

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




    private void jsonRespnsubmi(String jsonresponse) {

        try {

            JSONObject jresData = new JSONObject(jsonresponse);

            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if(satus.equalsIgnoreCase("true"))
                {


                    String msg = jresData.getString("msg");
                    Toast.makeText(LeaveApplyActivity.this, msg, Toast.LENGTH_LONG).show();

                    Intent intetn = new Intent(LeaveApplyActivity.this, MainActivity.class);
                    intetn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intetn);
                    finish();





                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(LeaveApplyActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }







        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static boolean isAfterToday(int year, int month, int day)
    {
        Calendar today = Calendar.getInstance();
        Calendar myDate = Calendar.getInstance();

        myDate.set(year, month, day);

        if (myDate.before(today))
        {
            return false;
        }
        return true;
    }



}
