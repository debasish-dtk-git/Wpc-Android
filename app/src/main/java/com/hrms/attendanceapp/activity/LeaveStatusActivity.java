package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.LeaveStatus;
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

public class LeaveStatusActivity extends AppCompatActivity implements View.OnClickListener {
    private int width;
    private int height;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private String strId = "";
    private LinearLayout lin;
    private ImageView imgLogo;
    private String strName = "";
   int j=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setStatusBarGradiant(LeaveStatusActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leave_status);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();



        strId = sh_Pref.getString("USER_CODE", "");
        strName = sh_Pref.getString("USER_NAME", "");
        String strcomName = sh_Pref.getString("COM_NAME", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");


        lin = (LinearLayout)findViewById(R.id.activity_leav_status);

        imgBack = (ImageView)findViewById(R.id.imageview_leavestatus_back);
        imgBack.setOnClickListener(this);


        imgLogo = (ImageView)findViewById(R.id.imageview_leavestatus_logo);

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(LeaveStatusActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }


        leavestatUser(strId);



    }

    private void leavestatUser(String strId) {



        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(LeaveStatusActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getViewLevSataus(strId);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        statusRespn(jsonresponse);

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


    private void statusRespn(String response) {

        ArrayList<LeaveStatus> statusList = new ArrayList<LeaveStatus>();
        JSONObject jresData = null;
        try {
            jresData = new JSONObject(response);
            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if (satus.equalsIgnoreCase("true")) {




                    JSONArray jsonarry = jresData.getJSONArray(String.valueOf(0));
                    for(int i=0;i<jsonarry.length();i++)
                    {
                        JSONObject jsonobj = jsonarry.getJSONObject(i);


                        LeaveStatus statusobj = new LeaveStatus();

                        String strid = jsonobj.getString("id");
                        statusobj.setId(strid);

                        String strinHand = jsonobj.getString("leave_in_hand");
                        statusobj.setStatus(strinHand);

                        String strtype = jsonobj.getString("leave_type_name");


                        String stralis = jsonobj.getString("alies");
                        statusobj.setName(stralis);







                        statusList.add(statusobj);


                    }


                    inittab(statusList);


                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(LeaveStatusActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }





        } catch (JSONException e) {


        }

    }


    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.imageview_leavestatus_back:
             finish();
             break;
     }
    }




    private void inittab(ArrayList<LeaveStatus> lvstatsList) {

        LinearLayout stk = (LinearLayout) findViewById(R.id.table_lvstats);

        LinearLayout linmain = new LinearLayout(this);

        /*  TableRow tbrow0 = new TableRow(this);*/

        linmain.setBackgroundResource(R.drawable.tablebar);
        linmain.setOrientation(LinearLayout.HORIZONTAL);
        linmain.setGravity(Gravity.CENTER);
       LinearLayout.LayoutParams paramsmani = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        paramsmani.setMargins(0, 0, 0, 0);
        linmain.setLayoutParams(paramsmani);


        LinearLayout linmain1 = new LinearLayout(this);



        linmain1.setBackgroundColor(Color.parseColor("#eebdbd"));
        linmain1.setOrientation(LinearLayout.HORIZONTAL);
        linmain1.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams paramsmani1 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        paramsmani1.setMargins(0, 0, 0, 0);
        linmain1.setLayoutParams(paramsmani1);


        for(LeaveStatus lvst : lvstatsList) {

            LinearLayout lin1 = new LinearLayout(this);


            lin1.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams paramslin = new LinearLayout.LayoutParams
                    (0, LinearLayout.LayoutParams.WRAP_CONTENT,1);

            paramslin.setMargins(0, 0, 0, 0);
            lin1.setLayoutParams(paramslin);


            TextView tv0 = new TextView(this);

            tv0.setText(lvst.getName());
            switch (width) {
                case 480:

                    tv0.setTextSize(9);

                    break;
                case 600:

                    tv0.setTextSize(20);

                    break;
                case 720:

                    tv0.setTextSize(12);

                    break;

                case 1080:
                    tv0.setTextSize(13);

                    break;
                case 1200:

                    tv0.setTextSize(14);


                    break;
                default:
                    tv0.setTextSize(12);

                    break;

            }

            tv0.setTextColor(Color.WHITE);
            tv0.setGravity(Gravity.CENTER);
            tv0.setPadding(10, 20, 10, 20);
            lin1.addView(tv0);
            linmain.addView(lin1);
            stk.removeAllViews();
            stk.addView(linmain);




            LinearLayout lin2 = new LinearLayout(this);


            lin2.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams paramslin1 = new LinearLayout.LayoutParams
                    (0, LinearLayout.LayoutParams.WRAP_CONTENT,1);

            paramslin1.setMargins(0, 0, 0, 0);
            lin2.setLayoutParams(paramslin1);


            TextView tv1 = new TextView(this);

            tv1.setText(lvst.getStatus());
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

                    tv1.setTextSize(20);
                    break;
                case 1200:

                    tv1.setTextSize(19);

                    break;
                default:
                    tv1.setTextSize(12);

                    break;

            }

            tv1.setTextColor(Color.BLACK);
            tv1.setGravity(Gravity.CENTER);
            tv1.setPadding(10, 20, 10, 20);
            lin2.addView(tv1);
            linmain1.addView(lin2);
           // stk.removeAllViews();
            stk.addView(linmain1);



















        /*TextView tv1 = new TextView(this);
        tv1.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT , TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        tv1.setText(" PL IN HAND ");

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
                tv1.setTextSize(20);

                break;
            case 1200:

                tv1.setTextSize(19);


                break;
            default:
                tv1.setTextSize(12);

                break;

        }



        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setPadding(10, 40, 10, 40);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT , TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        tv2.setText(" ML IN HAND ");

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

                tv2.setTextSize(20);
                break;
            case 1200:

                tv2.setTextSize(19);


                break;
            default:

                tv2.setTextSize(12);

                break;

        }



        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setPadding(10,40,10,40);
        tbrow0.addView(tv2);


        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT , TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        tv3.setText(" SPL IN HAND ");

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

                tv3.setTextSize(20);
                break;
            case 1200:

                tv3.setTextSize(19);


                break;
            default:

                tv3.setTextSize(12);

                break;

        }



        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setPadding(10,40,10,40);
        tbrow0.addView(tv3);
*/
            j++;
            //stk.addView(tbrow0, 0);

        }

           /* TableRow tbrow = new TableRow(this);

            tbrow.setBackgroundColor(Color.parseColor("#CCEEFA"));


            tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView t1v = new TextView(this);
            t1v.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT , TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            t1v.setText(lvst.getStatus());


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
                    t1v.setTextSize(18);

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
            t1v.setPadding(10,40,10,40);
            tbrow.addView(t1v);
*/           /* TextView t2v = new TextView(this);
            t2v.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT , TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            t2v.setText(lvst.getPl());


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

                    t2v.setTextSize(18);
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
            t2v.setPadding(10,40,10,40);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT , TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            t3v.setText(lvst.getMl());

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

                    t3v.setTextSize(18);
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
            t3v.setPadding(10,40,10,40);
            tbrow.addView(t3v);

            TextView t4v = new TextView(this);
            t4v.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT , TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            t4v.setText(lvst.getSpl());

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

                    t4v.setTextSize(18);
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
            t4v.setPadding(10,40,10,40);
            tbrow.addView(t4v);
*/
          /*  stk.addView(tbrow);

           count++;
        }*/

    }










}
