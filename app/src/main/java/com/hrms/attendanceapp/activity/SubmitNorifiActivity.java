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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SubmitNorifiActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBack;
    private LinearLayout lin;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private String strEmpUserIdd = "";
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
    private String emppId = "";
    private String strReg = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(SubmitNorifiActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_submit_norifi);



        sh_Pref = getSharedPreferences("Login Credentials", Context.MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        strEmpUserIdd = sh_Pref.getString("USER_IDD", "");
        strReg = sh_Pref.getString("REG", "");





        lin = (LinearLayout)findViewById(R.id.activity_noti_submit);




        imgBack = (ImageView)findViewById(R.id.imageview_notisubmit_org_back);
        imgBack.setOnClickListener(this);

        mySpinner = (Spinner) findViewById(R.id.mySpinner);

        //MultiSpinner mySpinner = (MultiSpinner)findViewById(R.id.spinnerMultiSpinner);

        edtSub = (EditText)findViewById(R.id.edittext_sub_org);
        edtMessage = (EditText)findViewById(R.id.edittext_desc_org);
        btnsubmit = (Button)findViewById(R.id.button_org_message_submit);
        btnsubmit.setOnClickListener(this);

        callEmpList(strEmpUserIdd);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_notisubmit_org_back:
                finish();
                break;
            case R.id.button_org_message_submit:
                //https://workpermitcloud.co.uk/hrms/api/addnofication

                String stSub = edtSub.getText().toString().trim();
                String stMess = edtMessage.getText().toString().trim();

                if(emppId.equalsIgnoreCase("")) {
                    Toast.makeText(SubmitNorifiActivity.this,"Please Select Employee", Toast.LENGTH_LONG).show();

                }
                else if(stSub.length() == 0) {
                    Toast.makeText(SubmitNorifiActivity.this,"Please Enter Any Subject", Toast.LENGTH_LONG).show();
                }
                else if(stMess.length() == 0)
                {
                    Toast.makeText(SubmitNorifiActivity.this,"Please Enter Any Message", Toast.LENGTH_LONG).show();
                }
                else {


                    try {
                        JSONObject jsonObject1 = new JSONObject();
                        JSONObject jsonObject_ = new JSONObject();
                        jsonObject_.put("emp", emppId);
                        JSONArray jsonArrayemp = new JSONArray();
                        jsonArrayemp.put(jsonObject_);


                        jsonObject1.put("employee_id", jsonArrayemp);
                        jsonObject1.put("emid", strReg);
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
        customProgress.showProgress(SubmitNorifiActivity.this, messag, false);


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


        Call<String> call = service.getSaveNoti(consumers.toString());


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






                                    Toast.makeText(SubmitNorifiActivity.this, msg, Toast.LENGTH_LONG).show();
                                    Intent intent_pay = new Intent(SubmitNorifiActivity.this, OrganisationActivity.class);
                                    intent_pay.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent_pay);
                                    finish();


                                }
                                else{
                                    Toast.makeText(SubmitNorifiActivity.this, msg, Toast.LENGTH_LONG).show();

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


























    private void callEmpList(String userId) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(SubmitNorifiActivity.this, messag, false);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getViewEmp(userId);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        empRespn(jsonresponse);

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

    private void empRespn(String jsonresponse) {

        try {



            JSONObject jresData = new JSONObject(jsonresponse);

            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if(satus.equalsIgnoreCase("true"))
                {


                    String user_type = jresData.getString("user_type");


                    final ArrayList<EmpList> empist = new ArrayList<EmpList>();

                    EmpList emplt1_ = new EmpList();
                    emplt1_.setName("Select Employee");
                    empist.add(emplt1_);
                    JSONArray jsonArray = jresData.getJSONArray("employee");
                    for(int i = 0; i<jsonArray.length(); i++)
                    {

                        JSONObject jsonobj_ = jsonArray.getJSONObject(i);
                        EmpList emplt_ = new EmpList();

                        String id = jsonobj_.getString("id");

                        String employeeid = jsonobj_.getString("emp_code");
                        emplt_.setId(employeeid);


                        String stremp_fname = jsonobj_.getString("emp_fname");
                        String stremp_mname = jsonobj_.getString("emp_mname");
                        String stremp_lname = jsonobj_.getString("emp_lname");

                        emplt_.setName(stremp_fname+" "+stremp_lname+" [ "+ employeeid +" ]");

                        empist.add(emplt_);


                       // notilist.add(new NotiList(id, Utils.parseDateToddMMyyyy(strdate), subject, strcontent));


                    }


                 spinnerEmpArrayAdapter  = new ArrayAdapter(SubmitNorifiActivity.this,R.layout.emp_item, empist);
                 spinnerEmpArrayAdapter.setDropDownViewResource(R.layout.emp_item1);
                    mySpinner.setAdapter(new NothingSelectedSpinnerAdapter(spinnerEmpArrayAdapter, R.layout.info_spinner_row_nothing_counry, SubmitNorifiActivity.this));
                    mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            // TODO Auto-generated method stub
                            EmpList selectedItemText = (EmpList) parent.getItemAtPosition(position);
                            if(selectedItemText!=null){
                                String empname = selectedItemText.getName();
                                emppId = selectedItemText.getId();

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub

                        }});




                   /* MyAdapter myAdapter = new MyAdapter(SubmitNorifiActivity.this, R.layout.maritalstatus_item, empist);
                    mySpinner.setAdapter(myAdapter);
                    *//*final AdapterTagSpinnerItem adapterTagSpinnerItem = new AdapterTagSpinnerItem(this, 0, empist, mySpinner);
                    mySpinner.setAdapter(adapterTagSpinnerItem);*/

                  /*  MyAdapter myAdapter = new MyAdapter(SubmitNorifiActivity.this, 0, empist);
                    mySpinner.setAdapter(myAdapter);


                    SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
                    Map<String, ?> category = sharedPreferences.getAll();
                    Toast.makeText(getApplicationContext(), "" + category, Toast.LENGTH_LONG).show();*/






                }
                else{
                    String error = jresData.getString("message");
                    Toast.makeText(SubmitNorifiActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }







        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }




}