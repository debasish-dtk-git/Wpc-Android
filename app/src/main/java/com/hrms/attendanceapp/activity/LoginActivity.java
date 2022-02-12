package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.utils.CustomProgress;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin, btnRegestration;
    private EditText edtUserName;
    private EditText edtPass;
    private String strUserName = "";
    private String strPsw = "";
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private LinearLayout lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(LoginActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        lin = (LinearLayout)findViewById(R.id.activity_login);

        edtUserName = (EditText)findViewById(R.id.edittext_username);
        edtPass = (EditText)findViewById(R.id.edittext_password);


        btnLogin = (Button)findViewById(R.id.button_login);
        btnLogin.setOnClickListener(this);

        btnRegestration = (Button)findViewById(R.id.button_registration);
        btnRegestration.setOnClickListener(this);


    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = ContextCompat.getDrawable(activity, R.drawable.statusbar_login);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            window.setNavigationBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }






    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_registration:
                Intent intetn = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intetn);
                finish();

                break;
            case R.id.button_login:

                strUserName = edtUserName.getText().toString().trim();
                strPsw = edtPass.getText().toString().trim();

                if(TextUtils.isEmpty(strUserName)) {

                    edtUserName.setError("Please Enter Your UserName");
                    return;
                }
                else{

                    if(TextUtils.isEmpty(strPsw)) {

                        edtPass.setError("Please Enter Your Password");
                        return;
                    }
                    else {


                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        userSignIn(strUserName, strPsw);

                    }
                }


                break;
        }
    }


    private void userSignIn(String strUserName, String strPsw) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Signing In...";
        customProgress.showProgress(LoginActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getLogin(strUserName, strPsw);


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

    private void loginRespn(String jsonresponse) {

        try {

            JSONObject jresData = new JSONObject(jsonresponse);

            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if(satus.equalsIgnoreCase("true"))
                {

                 String userType = jresData.getString( "user_type");

                 if(userType.equalsIgnoreCase("employee")) {

                     String message = jresData.getString("msg");
                     String companyname = jresData.getString("Company Name");
                     String companylogo = jresData.getString("Company Logo");
                     String empId = jresData.getString("user_id");
                     String leaveApprover = jresData.getString("Leave_approver");
                     String contactAgteement = jresData.getString("Contract_agrrement");



                     JSONObject jsonobj = jresData.getJSONObject("0");

                     String em_Id = jsonobj.getString("emid");

                     String empCode = jsonobj.getString("emp_code");
                     String fname = jsonobj.getString("emp_fname");
                     String mname = jsonobj.getString("emp_mname");
                     String lname = jsonobj.getString("emp_lname");

                     String empName = "";
                     if (mname.equalsIgnoreCase("")) {
                         empName = fname + " " + lname;
                     } else {
                         empName = fname + " " + mname + " " + lname;
                     }

                     String empPhoto = jsonobj.getString("emp_image");

                     sharedPrefernces(em_Id, empCode, empId, empName, companyname, companylogo, leaveApprover, empPhoto, userType, contactAgteement);

                     Intent intent_dashboard = new Intent(LoginActivity.this, MainActivity.class);

                     startActivity(intent_dashboard);
                     finish();
                 }
                 else if (userType.equalsIgnoreCase("employer"))
                 {
                     String employeruserId = jresData.getString( "employer_user_id");


                     JSONObject jsonobj = jresData.getJSONObject("0");

                     String id = jsonobj.getString("id");
                     String comname = jsonobj.getString("com_name");
                     String fname = jsonobj.getString("f_name");
                     String mname = jsonobj.getString("l_name");
                     String email = jsonobj.getString("email");


                     String phno = jsonobj.getString("p_no");
                     String regId = jsonobj.getString("reg").trim();
                     String logo = jsonobj.getString("logo");




                     sharedPreferncesEmp(regId, comname, logo, userType, employeruserId);

                     Intent intent_dashboard = new Intent(LoginActivity.this, OrganisationActivity.class);
                     startActivity(intent_dashboard);
                     finish();
                 }
                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }







        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }





    private void sharedPrefernces(String em_Id, String empCode, String struserid,  String name, String compnyName, String companyLogo, String leaveApprover, String empPhoto, String userType, String contactAgteement) {
        // TODO Auto-generated method stub

        toEdit.putString("EMID", em_Id);
        toEdit.putString("EMPCODE", empCode);

        toEdit.putString("USER_CODE", struserid);
        toEdit.putString("USER_NAME", name);
        toEdit.putString("COM_NAME", compnyName);
        toEdit.putString("COM_LOGO", companyLogo);
        toEdit.putString("COM_LVAPROV", leaveApprover);
        toEdit.putString("USER_PHOTO", empPhoto);

        toEdit.putString("USER_TYPE", userType);

        toEdit.putString("Contract_agrrement", contactAgteement);

        toEdit.commit();
    }



    private void sharedPreferncesEmp(String struserid, String compnyName, String companyLogo, String userType, String employeruserId) {
        // TODO Auto-generated method stub
        toEdit.putString("REG", struserid);
        toEdit.putString("COM_NAME", compnyName);
        toEdit.putString("COM_LOGO", companyLogo);
        toEdit.putString("USER_TYPE", userType);
        toEdit.putString("USER_IDD", employeruserId);

        toEdit.commit();
    }




}
