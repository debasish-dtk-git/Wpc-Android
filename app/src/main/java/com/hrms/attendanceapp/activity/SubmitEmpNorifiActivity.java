package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.adapter.NothingSelectedSpinnerAdapter;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.EmpList;
import com.hrms.attendanceapp.utils.CustomProgress;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SubmitEmpNorifiActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBack;
    private LinearLayout lin;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private Spinner mySpinner;

    public static String oneSpace =" ";
    public static int tikMark =0X2714;
    public static int crossMark =0X2715;
    public static int tikMarkAroundBox =0X2611;
    public static int crossMarkAroundBox =0X274E;
    public static String dash ="-";
    ArrayAdapter<EmpList> spinnerEmpArrayAdapter;
    private EditText edtSub;
    private EditText edtMessage;
    private Button btnsubmit;
    //private String emppId = "";
    private String strEmpcode = "";
    private String stemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(SubmitEmpNorifiActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_submit_emp_norifi);
        sh_Pref = getSharedPreferences("Login Credentials", Context.MODE_PRIVATE);
        toEdit = sh_Pref.edit();


        strEmpcode = sh_Pref.getString("EMPCODE", "");
        stemId = sh_Pref.getString("EMID", "");






        lin = (LinearLayout)findViewById(R.id.activity_noti_submit_emp);




        imgBack = (ImageView)findViewById(R.id.imageview_notisubmit_emp_back);
        imgBack.setOnClickListener(this);

       // mySpinner = (Spinner) findViewById(R.id.mySpinner_emp);

        //MultiSpinner mySpinner = (MultiSpinner)findViewById(R.id.spinnerMultiSpinner);

        edtSub = (EditText)findViewById(R.id.edittext_sub_emp);
        edtMessage = (EditText)findViewById(R.id.edittext_desc_emp);
        btnsubmit = (Button)findViewById(R.id.button_emp_message_submit);
        btnsubmit.setOnClickListener(this);

        //callEmpList(strId);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_notisubmit_emp_back:
                finish();
                break;
            case R.id.button_emp_message_submit:
                //https://workpermitcloud.co.uk/hrms/api/addnofication

                String stSub = edtSub.getText().toString().trim();
                String stMess = edtMessage.getText().toString().trim();

                /*if(emppId.equalsIgnoreCase("")) {
                    Toast.makeText(SubmitEmpNorifiActivity.this,"Please Select Employee", Toast.LENGTH_LONG).show();

                }*/
                if(stSub.length() == 0) {
                    Toast.makeText(SubmitEmpNorifiActivity.this,"Please Enter Any Subject", Toast.LENGTH_LONG).show();
                }
                else if(stMess.length() == 0)
                {
                    Toast.makeText(SubmitEmpNorifiActivity.this,"Please Enter Any Message", Toast.LENGTH_LONG).show();
                }
                else {


                    try {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("employee_id", strEmpcode);
                        jsonObject1.put("emid", stemId);
                        jsonObject1.put("sub", stSub);
                        jsonObject1.put("content", stMess);

                        callSaveNoti(jsonObject1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }



                break;
        }
    }



    private void callSaveNoti(JSONObject consumers) {

        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(SubmitEmpNorifiActivity.this, messag, false);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL).client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getSaveNotiEmp(consumers.toString());


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar

                customProgress.hideProgress();

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();


                        try {

                            JSONObject jresData = new JSONObject(jsonresponse);




                            String status = jresData.getString("status");
                            String msg = jresData.getString("msg");
                            if(status.equalsIgnoreCase("true")) {






                                Toast.makeText(SubmitEmpNorifiActivity.this, msg, Toast.LENGTH_LONG).show();
                                Intent intent_pay = new Intent(SubmitEmpNorifiActivity.this, MainActivity.class);
                                intent_pay.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent_pay);
                                finish();


                            }
                            else{
                                Toast.makeText(SubmitEmpNorifiActivity.this, msg, Toast.LENGTH_LONG).show();

                            }







                        } catch (JSONException e) {
                            e.printStackTrace();
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

                    // todo log to some central bug tracking service
                }





            }


        });



    }






























}