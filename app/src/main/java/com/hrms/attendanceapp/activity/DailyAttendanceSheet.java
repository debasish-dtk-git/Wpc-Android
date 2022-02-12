package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.adapter.AttandenceListAdapter;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.AttandenceList;
import com.hrms.attendanceapp.getset.AttandenceSubList;
import com.hrms.attendanceapp.utils.CustomProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DailyAttendanceSheet extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private String userId;
    private String attandence_URL = "";
    private ProgressDialog pDialog;
    private final Handler handler = new Handler();
    private int year = 0;
    private int month = 0;
    private ExpandableListView expandList;
    AttandenceListAdapter attandenceListAdapter;
    private LinearLayout linHeader;
    private LinearLayout lin;
    private String strId = "";
    private ImageView imgLogo;
    private String  dateString = "";

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
        setContentView(R.layout.activity_daily_attendance_sheet);

       /* Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;

        year = calendar.get(Calendar.YEAR);

      String curmonthyy =  String.format("%02d", month)+"/"+String.valueOf(year);*/

        SimpleDateFormat sdfss = new SimpleDateFormat("MM/yyyy");
        dateString = sdfss.format(new Date());

       /* params.put("mm", String.format("%02d", month));
        params.put("yyyy", String.valueOf(year));*/

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        strId = sh_Pref.getString("USER_CODE", "");
        String strName = sh_Pref.getString("USER_NAME", "");
        String strcomName = sh_Pref.getString("COM_NAME", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");


        lin = (LinearLayout)findViewById(R.id.activity_dilyattendance_sheet);

        imgLogo = (ImageView)findViewById(R.id.imageview_dailyattendance_sheet_logo);

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(DailyAttendanceSheet.this)
                    .load(imurl)
                    .into(imgLogo);


        }


        imgBack = (ImageView)findViewById(R.id.imageview_dailyattendance_sheet_back);
        imgBack.setOnClickListener(this);

        linHeader = (LinearLayout)findViewById(R.id.lin_date_header);

        expandList = (ExpandableListView)findViewById(R.id.listview_daily_attendence_list);



        if(!strId.equalsIgnoreCase(""))
        {

            attandencelistUser(strId, dateString);

        }
    }

    private void attandencelistUser(String strId, String dateString) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(DailyAttendanceSheet.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getattandencelist(strId, dateString);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        showJSON(jsonresponse);

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



       private void showJSON(String response) {


        try {

            JSONObject jsonobj = new JSONObject(response);

            String resultsatus = jsonobj.getString("resultstatus");
            String empstatus = jsonobj.getString("status");
            if(resultsatus.equalsIgnoreCase("true"))
            {
                if(empstatus.equalsIgnoreCase("active")) {
                    linHeader.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = jsonobj.getJSONArray("0");

                    ArrayList<AttandenceList> attList = new ArrayList<AttandenceList>();


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobj1 = jsonArray.getJSONObject(i);


                        AttandenceList atenobj = new AttandenceList();

                        /*String id = jsonobj1.getString("id");*/
                        atenobj.setId(String.valueOf(i));

                        String date = jsonobj1.getString("date");
                        atenobj.setDate(date);

                        String month = jsonobj1.getString("month");
                        atenobj.setMonth(month);

                        JSONArray jsonrray = jsonobj1.getJSONArray("log");
                        ArrayList<AttandenceSubList> subcat= new ArrayList<AttandenceSubList>();
                        for (int j = 0; j < jsonrray.length(); j++) {

                            JSONObject jsonobj_ = jsonrray.getJSONObject(j);

                            AttandenceSubList attensub_ = new AttandenceSubList();

                            String timein = jsonobj_.getString("timein");
                            attensub_.setTimein(timein);

                            String timeout = jsonobj_.getString("timeout");
                            attensub_.setTimeout(timeout);

                            String timeInLoc = jsonobj_.getString("time_in_location");
                            attensub_.setTimein_location(timeInLoc);

                            String timeOutLoc = jsonobj_.getString("time_out_location");
                            attensub_.setTimeout_location(timeOutLoc);

                            String dutyhours = jsonobj_.getString("duty_hours");
                            attensub_.setDutyhours(dutyhours);
                            subcat.add(attensub_);

                        }


                        atenobj.setAttandenceSubLists(subcat);

                        attList.add(atenobj);

                        String preStdata = "";
                        for(int ii = 0; ii< attList.size();ii++)
                        {


                            if(preStdata.equalsIgnoreCase(""))
                            {
                                preStdata = attList.get(ii).getDate();
                            }
                            else if(preStdata.equalsIgnoreCase(attList.get(ii).getDate()))
                            {
                                attList.remove(ii);
                            }
                            else{

                                preStdata = attList.get(ii).getDate();
                            }



                        }
                    }


                    attandenceListAdapter = new AttandenceListAdapter(DailyAttendanceSheet.this, attList);
                    expandList.setAdapter(attandenceListAdapter);


                    attandenceListAdapter.notifyDataSetChanged();

                    expandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v,
                                                    int groupPosition, long id) {

                            return true;

                        }
                    });


                }
                else{
                    Intent intent = new Intent(DailyAttendanceSheet.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }






            }else{
                linHeader.setVisibility(View.GONE);

                String message = jsonobj.getString("msg");
                Toast.makeText(DailyAttendanceSheet.this, message, Toast.LENGTH_LONG).show();

            }






        } catch (JSONException e) {
            e.printStackTrace();
            linHeader.setVisibility(View.GONE);
        }


    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageview_dailyattendance_sheet_back:
                /*Intent intent = new Intent(DailyAttendanceSheet.this, AttendanceActivity.class);
                startActivity(intent);*/
                finish();
                break;
        }
    }

    public void showAlert(final String message, final String title)
    {

        Runnable updater = new Runnable()
        {
            public void run()
            {
                new AlertDialog.Builder(DailyAttendanceSheet.this).setTitle(title)
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

