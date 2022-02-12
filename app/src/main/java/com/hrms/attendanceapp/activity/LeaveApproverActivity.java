package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.adapter.LeaveAdapter;
import com.hrms.attendanceapp.adapter.ProductAdapter;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.LeaveList;
import com.hrms.attendanceapp.getset.Upcomingholiday;
import com.hrms.attendanceapp.getset.ViewLeaveList;
import com.hrms.attendanceapp.utils.CustomProgress;
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

public class LeaveApproverActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private String strId = "";
    private LinearLayout lin;
    private ImageView imgLogo;
    Integer count=0;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(LeaveApproverActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leave_approver);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();


        strId = sh_Pref.getString("USER_CODE", "");
        //strName = sh_Pref.getString("USER_NAME", "");
        String strcomName = sh_Pref.getString("COM_NAME", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");


        lin = (LinearLayout)findViewById(R.id.activity_leav);

        imgBack = (ImageView)findViewById(R.id.imageview_leaveapp_back);
        imgBack.setOnClickListener(this);


        imgLogo = (ImageView)findViewById(R.id.imageview_leaveappove_logo);

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(LeaveApproverActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }


        userLeaveApprover(strId);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageview_leaveapp_back:
                finish();
                break;
        }
    }

    private void userLeaveApprover(String strUserid) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(LeaveApproverActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getLeaveAppver(strUserid);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        approvRespn(jsonresponse);

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




    private void approvRespn(String response) {


        JSONObject jresData = null;
        try {
            jresData = new JSONObject(response);
            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if (satus.equalsIgnoreCase("true")) {



                    ArrayList<ViewLeaveList> vlvList_ = new ArrayList<ViewLeaveList>();
                    JSONArray jsonarryviewleav = jresData.getJSONArray("0");
                    for(int i=0;i<jsonarryviewleav.length();i++)
                    {
                        JSONObject jsonobj = jsonarryviewleav.getJSONObject(i);


                        ViewLeaveList levobj = new ViewLeaveList();

                        String id= jsonobj.getString("id");
                        levobj.setId(id);

                        String empName = jsonobj.getString("employee_name");


                        String leavetype = jsonobj.getString("leave_type_name");
                        levobj.setLeavetype(leavetype);

                       /* String dateofApply = jsonobj.getString("date_of_apply");
                        levobj.setDateOfApply(dateofApply);*/

                        String fdate = jsonobj.getString("from_date");
                        levobj.setFromdate(fdate);

                        String tdate= jsonobj.getString("to_date");
                        levobj.setTodate(tdate);

                        /*String nofleav = jsonobj.getString("no_of_leave");
                        levobj.setNoOfLeave(nofleav);*/

                        String status= jsonobj.getString("status");
                        levobj.setStatus(status);

                        vlvList_.add(levobj);

                    }
                    if(vlvList_.size()>0)

                          inittab(vlvList_);


                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(LeaveApproverActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }





        } catch (JSONException e) {


        }

    }


    private void inittab(ArrayList<ViewLeaveList> holidyList) {

        {
            TableLayout stk = (TableLayout) findViewById(R.id.table_request_leave);
            TableRow tbrow0 = new TableRow(this);
            tbrow0.setBackgroundResource(R.drawable.tablebar);
            tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv0 = new TextView(this);
            tv0.setGravity(Gravity.CENTER);
            tv0.setText("   Leave Type  ");
            switch (width) {
                case 480:

                    tv0.setTextSize(14);

                    break;
                case 600:

                    tv0.setTextSize(25);

                    break;
                case 720:

                    tv0.setTextSize(12);

                    break;

                case 1080:

                    tv0.setTextSize(15);
                    break;
                case 1200:

                    tv0.setTextSize(19);

                    break;
                default:
                    tv0.setTextSize(12);

                    break;

            }

            tv0.setTextColor(Color.WHITE);
            tv0.setPadding(10,30,10,30);
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setGravity(Gravity.CENTER);
            tv1.setText("   From Date   ");

            switch (width) {
                case 480:

                    tv1.setTextSize(14);

                    break;
                case 600:

                    tv1.setTextSize(25);

                    break;
                case 720:

                    tv1.setTextSize(12);

                    break;

                case 1080:
                    tv1.setTextSize(15);

                    break;
                case 1200:

                    tv1.setTextSize(19);


                    break;
                default:
                    tv1.setTextSize(12);

                    break;

            }



            tv1.setTextColor(Color.WHITE);
            tv1.setPadding(10,30,10,30);
            tbrow0.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setGravity(Gravity.CENTER);
            tv2.setText("   To Date   ");

            switch (width) {
                case 480:

                    tv2.setTextSize(14);

                    break;
                case 600:

                    tv2.setTextSize(25);

                    break;
                case 720:


                    tv2.setTextSize(12);
                    break;

                case 1080:

                    tv2.setTextSize(15);
                    break;
                case 1200:

                    tv2.setTextSize(19);


                    break;
                default:

                    tv2.setTextSize(12);

                    break;

            }



            tv2.setTextColor(Color.WHITE);
            tv2.setPadding(10,30,10,30);
            tbrow0.addView(tv2);
            TextView tv3 = new TextView(this);

            tv3.setGravity(Gravity.CENTER);
            tv3.setText("      Status     ");

            switch (width) {
                case 480:

                    tv3.setTextSize(14);

                    break;
                case 600:

                    tv3.setTextSize(25);

                    break;
                case 720:

                    tv3.setTextSize(12);

                    break;

                case 1080:

                    tv3.setTextSize(15);
                    break;
                case 1200:

                    tv3.setTextSize(19);


                    break;
                default:
                    tv3.setTextSize(12);

                    break;

            }



            tv3.setTextColor(Color.WHITE);
            tv3.setPadding(10,30,10,30);
            tbrow0.addView(tv3);


            TextView tv4 = new TextView(this);


            tv4.setGravity(Gravity.CENTER);
            tv4.setText("  Action     ");

            switch (width) {
                case 480:

                    tv4.setTextSize(14);

                    break;
                case 600:

                    tv4.setTextSize(25);

                    break;
                case 720:

                    tv4.setTextSize(12);

                    break;

                case 1080:

                    tv4.setTextSize(15);
                    break;
                case 1200:

                    tv4.setTextSize(19);


                    break;
                default:
                    tv4.setTextSize(12);

                    break;

            }



            tv4.setTextColor(Color.WHITE);
            tv4.setPadding(10,30,10,30);
            tbrow0.addView(tv4);



            stk.addView(tbrow0,0);


            for(ViewLeaveList holidayList : holidyList)
            {



                TableRow tbrow = new TableRow(this);
                if(count%2!=0)
                    tbrow.setBackgroundColor(Color.parseColor("#eebdbd"));
                else
                    tbrow.setBackgroundColor(Color.parseColor("#ffe2e2"));

                tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                tbrow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView t1v = new TextView(this);
                t1v.setText(holidayList.getLeavetype());


                switch (width) {
                    case 480:

                        t1v.setTextSize(9);

                        break;
                    case 600:

                        t1v.setTextSize(20);

                        break;
                    case 720:

                        t1v.setTextSize(12);

                        break;

                    case 1080:
                        t1v.setTextSize(13);

                        break;
                    case 1200:

                        t1v.setTextSize(14);


                        break;
                    default:
                        t1v.setTextSize(12);

                        break;

                }



                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                t1v.setPadding(10,20,10,20);
                tbrow.addView(t1v);
                TextView t2v = new TextView(this);

                t2v.setText(Utils.parseDateToddMMyyyy(holidayList.getFromdate()));


                switch (width) {
                    case 480:

                        t2v.setTextSize(9);

                        break;
                    case 600:

                        t2v.setTextSize(20);

                        break;
                    case 720:

                        t2v.setTextSize(12);

                        break;

                    case 1080:

                        t2v.setTextSize(13);
                        break;
                    case 1200:

                        t2v.setTextSize(14);


                        break;
                    default:
                        t2v.setTextSize(12);

                        break;

                }



                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.CENTER);
                t2v.setPadding(10,20,10,20);
                tbrow.addView(t2v);
                TextView t3v = new TextView(this);

                t3v.setText(Utils.parseDateToddMMyyyy(holidayList.getTodate()));

                switch (width) {
                    case 480:

                        t3v.setTextSize(9);

                        break;
                    case 600:

                        t3v.setTextSize(20);

                        break;
                    case 720:

                        t3v.setTextSize(12);

                        break;

                    case 1080:

                        t3v.setTextSize(13);
                        break;
                    case 1200:

                        t3v.setTextSize(14);


                        break;
                    default:
                        t3v.setTextSize(12);

                        break;

                }



                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                t3v.setPadding(10,20,10,20);
                tbrow.addView(t3v);
                TextView t4v = new TextView(this);
                t4v.setText(holidayList.getStatus());

                switch (width) {
                    case 480:

                        t4v.setTextSize(9);

                        break;
                    case 600:
                        t4v.setTextSize(20);


                        break;
                    case 720:

                        t4v.setTextSize(12);

                        break;

                    case 1080:

                        t4v.setTextSize(13);
                        break;
                    case 1200:

                        t4v.setTextSize(14);


                        break;
                    default:
                        t4v.setTextSize(12);

                        break;

                }

                if(holidayList.getStatus().equalsIgnoreCase("NOT APPROVED"))
                {
                    t4v.setBackgroundResource(R.drawable.notapprov_shape);
                    t4v.setTextColor(Color.BLACK);
                }
                else if(holidayList.getStatus().equalsIgnoreCase("APPROVED"))
                {
                    t4v.setBackgroundResource(R.drawable.aproved_shape);
                    t4v.setTextColor(Color.WHITE);
                }
                else{
                    t4v.setBackgroundResource(R.drawable.reconment_shape);
                    t4v.setTextColor(Color.WHITE);
                }



                t4v.setGravity(Gravity.CENTER);
                t4v.setPadding(10,10,10,10);
                tbrow.addView(t4v);

               /* LinearLayout li1 = new LinearLayout(this);
                li1.setPadding(50, 50, 50, 50);*/
                ImageView img1 = new ImageView(this);

                img1.setLayoutParams(new TableRow.LayoutParams(90, 90));

                img1.setId(Integer.parseInt(holidayList.getId()));

                if(holidayList.getStatus().equalsIgnoreCase("NOT APPROVED")) {
                    img1.setImageResource(R.drawable.eye_icon);


                    img1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int idd = (int) v.getId();
                            Intent intent = new Intent(LeaveApproverActivity.this, LeaveApproverDtlsActivity.class);
                            intent.putExtra("IDD", idd);
                            startActivity(intent);
                        }
                    });

                }

                img1.setPadding(10,10,10,10);
                tbrow.addView(img1);







                stk.addView(tbrow);

                count++;
            }

        }
    }











}
