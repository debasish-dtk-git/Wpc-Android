package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.utils.CustomProgress;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NotiDetailsEmpActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private LinearLayout lin;
    private String strId = "";
    private String notiId = "";
    private TextView txtname;
    private TextView txtDate;
    private TextView txtTitle;
    private TextView txtDesc;
    private String strcompnyimg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(NotiDetailsEmpActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_noti_details_emp);

        sh_Pref = getSharedPreferences("Login Credentials", Context.MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        strId = sh_Pref.getString("USER_CODE", "");

        notiId = getIntent().getStringExtra("NOTI_ID");
        strcompnyimg = sh_Pref.getString("COM_LOGO", "");



        lin = (LinearLayout)findViewById(R.id.activity_notidtls_emp);




        imgBack = (ImageView)findViewById(R.id.imageview_notidtls_emp_back);
        imgBack.setOnClickListener(this);

        txtname = (TextView)findViewById(R.id.textview_notidtls_name_emp);
        txtDate = (TextView)findViewById(R.id.textview_notidtls_date_emp);
        txtTitle = (TextView)findViewById(R.id.textview_notidtls_title_emp);
        txtDesc = (TextView)findViewById(R.id.textview_notidtls_desc_emp);


        callViewotification(strId, notiId);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_notidtls_emp_back:
                finish();
                break;
        }
    }


    private void callViewotification(String userId, String notiId) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(NotiDetailsEmpActivity.this, messag, false);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getViewNotificationdtls(userId, notiId);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        notifiRespn(jsonresponse);

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

    private void notifiRespn(String jsonresponse) {

        try {



            JSONObject jresData = new JSONObject(jsonresponse);

            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if(satus.equalsIgnoreCase("true"))
                {





                    String user_type = jresData.getString("user_type");



                    JSONObject jsonobj = jresData.getJSONObject("notification");





                    String id = jsonobj.getString("id");

                    String employeeid = jsonobj.getString("employee_id");
                    String emid = jsonobj.getString("emid");
                    String subject = jsonobj.getString("sub");
                    String strcontent = jsonobj.getString("content");
                    String strdate = jsonobj.getString("cr_date");
                    String stremp_fname = jsonobj.getString("emp_fname");
                    String stremp_mname = jsonobj.getString("emp_mname");
                    String stremp_lname = jsonobj.getString("emp_lname");





                    txtDate.setText(Utils.parseDateToddMMyyyy(strdate));
                    if(stremp_mname.equalsIgnoreCase(""))
                        txtname.setText("From - "+stremp_fname+" "+stremp_lname);
                    else{

                        txtname.setText("From - "+stremp_fname+" "+stremp_mname+" "+stremp_lname);
                    }





                    txtTitle.setText(subject.substring(0, 1).toUpperCase() + subject.substring(1).toLowerCase());
                    txtDesc.setText(strcontent.substring(0, 1).toUpperCase() + strcontent.substring(1).toLowerCase());





                }
                else{
                    String error = jresData.getString("message");
                    Toast.makeText(NotiDetailsEmpActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }







        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }


}





