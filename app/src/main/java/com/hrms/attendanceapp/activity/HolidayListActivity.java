package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.HolidayList;
import com.hrms.attendanceapp.utils.CustomProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HolidayListActivity extends AppCompatActivity implements View.OnClickListener{

    private int width;
    private int height;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private String strId = "";
    private String holidaylst_URL = "";
    private final Handler handler = new Handler();
    Integer count=0;
    private int year;
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
        setContentView(R.layout.activity_holiday_list);
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


        lin = (LinearLayout)findViewById(R.id.activity_holidaylist);

        imgBack = (ImageView)findViewById(R.id.imageview_holiday_back);
        imgBack.setOnClickListener(this);





        imgLogo = (ImageView)findViewById(R.id.imageview_holiday_logo);

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(HolidayListActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }

        if(!strId.equalsIgnoreCase(""))
        {

            holidaylistUser(strId);



        }
    }


    private void holidaylistUser(String strId) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(HolidayListActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getHolidaylist(strId);


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

    private void holidayRespn(String jsonresponse) {

        try {

            JSONObject jresData = new JSONObject(jsonresponse);

            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if(satus.equalsIgnoreCase("true"))
                {



                    ArrayList<HolidayList> holidyList = new ArrayList<HolidayList>();
                    JSONArray jsonarry = jresData.getJSONArray("0");
                    for(int i=0;i<jsonarry.length();i++)
                    {
                        JSONObject jsonobj = jsonarry.getJSONObject(i);


                        HolidayList holidyltobj = new HolidayList();

                        String id= jsonobj.getString("id");
                        holidyltobj.setId(id);

                        String yr = jsonobj.getString("years");
                        holidyltobj.setYear(yr);

                        String hdtype= jsonobj.getString("holiday_descripion");
                        holidyltobj.setType(hdtype);

                        String fdate = jsonobj.getString("from_date");
                        holidyltobj.setFromdate(fdate);

                        String tdate= jsonobj.getString("to_date");
                        holidyltobj.setTodate(tdate);

                        String ndays = jsonobj.getString("day");
                        holidyltobj.setNodays(ndays);

                        holidyList.add(holidyltobj);
                    }
                    inittab(holidyList);





                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(HolidayListActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }







        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }















    private void inittab(ArrayList<HolidayList> holidyList) {

        {
            TableLayout stk = (TableLayout) findViewById(R.id.table_holiday);
            TableRow tbrow0 = new TableRow(this);
            tbrow0.setBackgroundColor(Color.parseColor("#828282"));
            tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv0 = new TextView(this);

            tv0.setText(" Year ");
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

            tv1.setText(" Holidays Type ");

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

            tv2.setText(" From Date ");

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


            tv3.setText(" To Date ");

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


            tv4.setText(" No Days ");

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


            for(HolidayList holidayList : holidyList)
            {



                TableRow tbrow = new TableRow(this);
                if(count%2!=0)
                    tbrow.setBackgroundColor(Color.parseColor("#CCEEFA"));
                else
                    tbrow.setBackgroundColor(Color.parseColor("#F5F1F1"));

                tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                tbrow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView t1v = new TextView(this);
                t1v.setText(holidayList.getYear());


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

                t2v.setText(holidayList.getType());


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
                t2v.setGravity(Gravity.LEFT);
                t2v.setPadding(10,20,10,20);
                tbrow.addView(t2v);
                TextView t3v = new TextView(this);

                t3v.setText(parseDateToddMMyyyy(holidayList.getFromdate()));

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
                t4v.setText(parseDateToddMMyyyy(holidayList.getTodate()));

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



                t4v.setTextColor(Color.BLACK);
                t4v.setGravity(Gravity.CENTER);
                t4v.setPadding(10,20,10,20);
                tbrow.addView(t4v);

                TextView t5v = new TextView(this);
                t5v.setText(holidayList.getNodays());

                switch (width) {
                    case 480:

                        t5v.setTextSize(9);

                        break;
                    case 600:
                        t5v.setTextSize(20);


                        break;
                    case 720:

                        t5v.setTextSize(12);

                        break;

                    case 1080:

                        t5v.setTextSize(13);
                        break;
                    case 1200:

                        t5v.setTextSize(14);


                        break;
                    default:
                        t5v.setTextSize(12);

                        break;

                }



                t5v.setTextColor(Color.BLACK);
                t5v.setGravity(Gravity.CENTER);
                t5v.setPadding(10,20,10,20);
                tbrow.addView(t5v);

                stk.addView(tbrow);

                count++;
            }

        }
    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageview_holiday_back:
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
                new AlertDialog.Builder(HolidayListActivity.this).setTitle(title)
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

