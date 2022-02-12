package com.hrms.attendanceapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.ViewLeaveList;
import com.hrms.attendanceapp.utils.CustomProgress;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LeaveApproverDtlsActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private String strId = "";
    private LinearLayout lin;
    private ImageView imgLogo;
    Integer count=0;
    private int width;
    private int height;
    private String lvId = "";
    private TextView txtEmpType, txtEmpCode, txtEmpName, txtEmpleavtype, txtEmpleavstatus, txtEmpNoOfLeave, txtEmpFromdate, txtEmpTodate;
    private Spinner spinnerreqStatus;
    private EditText edtremarks;
    private Button btnApply;
    private ArrayAdapter<String> spinnerexpstatusArrayAdapter;
    private String strReqstatus = "";
    private String applyId = "";
    private String empId = "";
    private String leaveType = "";
    private String noofLeave = "";
    private String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(LeaveApproverDtlsActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leave_approver_dtls);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();


        strId = sh_Pref.getString("USER_CODE", "");
        String strcomName = sh_Pref.getString("COM_NAME", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");

        lvId = getIntent().getStringExtra("IDD");

        initView();



        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(LeaveApproverDtlsActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }

        userLeaveApproverDtls(strId, lvId);


    }

    private void initView() {

        lin = (LinearLayout)findViewById(R.id.activity_approverdtls);

        imgBack = (ImageView)findViewById(R.id.imageview_leavedtls_back);
        imgBack.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_leaveappdtls_logo);



        txtEmpType = (TextView) findViewById(R.id.textview_emptype);

        txtEmpCode = (TextView) findViewById(R.id.textview_empcode);

        txtEmpName = (TextView) findViewById(R.id.textview_empname);

        txtEmpleavtype = (TextView) findViewById(R.id.textview_empleavtype);

        txtEmpleavstatus = (TextView) findViewById(R.id.textview_empleavstatus);

        txtEmpNoOfLeave = (TextView) findViewById(R.id.textview_empno_of_leav);

        txtEmpFromdate = (TextView) findViewById(R.id.textview_empfromdate);

        txtEmpTodate = (TextView) findViewById(R.id.textview_emptodate);







    }

    private void userLeaveApproverDtls(String strUserid, String lvId) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(LeaveApproverDtlsActivity.this, messag, false);




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getLeaveAppverDtl(strUserid, lvId);


        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();
                        dtlsRespn(jsonresponse);

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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dtlsRespn(String response) {


        JSONObject jresData = null;
        try {
            jresData = new JSONObject(response);
            if (jresData.has("status")) {
                String satus = jresData.getString("status");
                if (satus.equalsIgnoreCase("true")) {

                 TextView  text1 = (TextView)findViewById(R.id.textview01_);
                    text1.setVisibility(View.VISIBLE);
                 LinearLayout lindtl = (LinearLayout)findViewById(R.id.lin_dtls);
                    lindtl.setVisibility(View.VISIBLE);


                    LinearLayout linStatus = (LinearLayout)findViewById(R.id.lin_status);
                    linStatus.setVisibility(View.VISIBLE);
                    spinnerreqStatus = (Spinner)findViewById(R.id.spinner_leave_req_status);
                    callreqStatus(spinnerreqStatus);
                    edtremarks = (EditText)findViewById(R.id.edittext_remarks);

                    btnApply = (Button)findViewById(R.id.button_apply);
                    btnApply.setOnClickListener(this);

                    JSONObject jsonobj0 = jresData.getJSONObject("0");

                    applyId = jsonobj0.getString("id");
                    empId = jsonobj0.getString("employee_id");
                    String empName = jsonobj0.getString("employee_name");
                    String fromdate = jsonobj0.getString("from_date");
                    String todate = jsonobj0.getString("to_date");
                    noofLeave = jsonobj0.getString("no_of_leave");
                    status = jsonobj0.getString("status");
                    String leaveTypename = jsonobj0.getString("leave_type_name");
                    String empStatus = jsonobj0.getString("emp_status");

                    leaveType = jsonobj0.getString("leave_type");

                   // https://hrmplus.co.uk/api/leaverequapediti?employee_id=3&id=6

                   // https://hrmplus.co.uk/api/leaverequesteditapi?user_id=3&leave_type=1&no_of_leave=1&apply_id=6&leave_check=RECOMMENDED&status_remarks=it%20is%20recomm&employee_id=7714&status=NOT%20APPROVED


                  //  "id":"6","employee_id":"7714","employee_name":"Ankita Das","emp_reporting_auth":"1471","emp_lv_sanc_auth":"6678","date_of_apply":"2020-09-16","leave_type":"1","half_cl":null,"from_date":"2020-09-18","to_date":"2020-09-18","no_of_leave":"1","doc_image":null,"status":"RECOMMENDED","status_remarks":"it is recomm","updated_at":null,"created_at":null,"emid":"EM1","leave_type_name":"CASUAL LEAVE","alies":"CL","emp_status":"REGULAR"

                    txtEmpType.setText(Html.fromHtml(getColoredSpanned("Employment Type : ", "#000000") + getColoredSpanned(empStatus, "#D35400 "), Html.FROM_HTML_MODE_LEGACY));

                    txtEmpCode.setText(Html.fromHtml(getColoredSpanned("Employee Code : ", "#000000") + getColoredSpanned(empId, "#D35400 "), Html.FROM_HTML_MODE_LEGACY));

                    txtEmpName.setText(Html.fromHtml(getColoredSpanned("Employee Name : ", "#000000") + getColoredSpanned(empName, "#D35400 "), Html.FROM_HTML_MODE_LEGACY));

                    txtEmpleavtype.setText(Html.fromHtml(getColoredSpanned("Leave Type : ", "#000000") + getColoredSpanned(leaveType, "#D35400 "), Html.FROM_HTML_MODE_LEGACY));

                    txtEmpleavstatus.setText(Html.fromHtml(getColoredSpanned("Leave Status : ", "#000000") + getColoredSpanned(status, "#D35400 "), Html.FROM_HTML_MODE_LEGACY));

                    txtEmpNoOfLeave.setText(Html.fromHtml(getColoredSpanned("No. of Leave : ", "#000000") + getColoredSpanned(noofLeave, "#D35400 "), Html.FROM_HTML_MODE_LEGACY));

                    txtEmpFromdate.setText(Html.fromHtml(getColoredSpanned("From Date : ", "#000000") + getColoredSpanned(Utils.parseDateToddMMyyyy(fromdate), "#D35400 "), Html.FROM_HTML_MODE_LEGACY));

                    txtEmpTodate.setText(Html.fromHtml(getColoredSpanned("To Date : ", "#000000") + getColoredSpanned(Utils.parseDateToddMMyyyy(todate), "#D35400 "), Html.FROM_HTML_MODE_LEGACY));






                    ArrayList<ViewLeaveList> vlvList_ = new ArrayList<ViewLeaveList>();
                    JSONArray jsonarryviewleav = jresData.getJSONArray("1");
                    for(int i=0;i<jsonarryviewleav.length();i++)
                    {
                        JSONObject jsonobj = jsonarryviewleav.getJSONObject(i);



                        ViewLeaveList levobj = new ViewLeaveList();

                        String id= jsonobj.getString("id");
                        levobj.setId(id);

                        String emplName = jsonobj.getString("employee_name");

                        String leavetype = jsonobj.getString("leave_type_name");


                        String dateofApply = jsonobj.getString("date_of_apply");
                        levobj.setDateOfApply(dateofApply);

                        String fdate = jsonobj.getString("from_date");
                        levobj.setFromdate(fdate);

                        String tdate= jsonobj.getString("to_date");
                        levobj.setTodate(tdate);

                        String nofleav = jsonobj.getString("no_of_leave");
                        levobj.setNoOfLeave(nofleav);

                        String emstatus= jsonobj.getString("status");
                        levobj.setStatus(emstatus);

                        vlvList_.add(levobj);

                    }
                    if(vlvList_.size()>0) {
                        TextView text2 = (TextView) findViewById(R.id.textview02_);
                        text2.setVisibility(View.VISIBLE);

                        inittab(vlvList_);
                    }

                }
                else{
                    String error = jresData.getString("msg");
                    Toast.makeText(LeaveApproverDtlsActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }





        } catch (JSONException e) {


        }

    }



    private void inittab(ArrayList<ViewLeaveList> holidyList) {

        {
            TableLayout stk = (TableLayout) findViewById(R.id.table_approv_leavelist);
            TableRow tbrow0 = new TableRow(this);
            tbrow0.setBackgroundResource(R.drawable.tablebar);
            tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv0 = new TextView(this);
            tv0.setGravity(Gravity.CENTER);
            tv0.setText("   SL No  ");
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
            tv3.setText("  Date of\n    Application");

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
            tv4.setText("   No. of\n      Leave    ");

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



            TextView tv5 = new TextView(this);


            tv5.setGravity(Gravity.CENTER);
            tv5.setText("   Status    ");

            switch (width) {
                case 480:

                    tv5.setTextSize(14);

                    break;
                case 600:

                    tv5.setTextSize(25);

                    break;
                case 720:

                    tv5.setTextSize(12);

                    break;

                case 1080:

                    tv5.setTextSize(15);
                    break;
                case 1200:

                    tv5.setTextSize(19);


                    break;
                default:
                    tv5.setTextSize(12);

                    break;

            }



            tv5.setTextColor(Color.WHITE);
            tv5.setPadding(10,30,10,30);
            tbrow0.addView(tv5);




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
                t1v.setText(String.valueOf(count+1));


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
                t1v.setPadding(10,40,10,40);
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
                t2v.setPadding(10,40,10,40);
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
                t3v.setPadding(10,40,10,40);
                tbrow.addView(t3v);
                TextView t4v = new TextView(this);
                t4v.setText(Utils.parseDateToddMMyyyy(holidayList.getDateOfApply()));

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
                t4v.setPadding(10,40,10,40);
                tbrow.addView(t4v);



                TextView t5v = new TextView(this);
                t5v.setText(holidayList.getNoOfLeave());

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
                t5v.setPadding(10,40,10,40);
                tbrow.addView(t5v);





                TextView t6v = new TextView(this);
                t6v.setText(holidayList.getStatus());

                switch (width) {
                    case 480:

                        t6v.setTextSize(9);

                        break;
                    case 600:
                        t6v.setTextSize(20);


                        break;
                    case 720:

                        t6v.setTextSize(12);

                        break;

                    case 1080:

                        t6v.setTextSize(13);
                        break;
                    case 1200:

                        t6v.setTextSize(14);


                        break;
                    default:
                        t6v.setTextSize(12);

                        break;

                }

                t6v.setBackgroundResource(R.drawable.aproved_shape);
                t6v.setTextColor(Color.WHITE);
                t6v.setGravity(Gravity.CENTER);
                t6v.setPadding(10,10,10,10);
                tbrow.addView(t6v);







                stk.addView(tbrow);

                count++;
            }

        }
    }







    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageview_leavedtls_back:
                finish();
                break;
            case R.id.button_apply:
              String strRemarks = edtremarks.getText().toString().trim();

                break;
        }
    }



    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }


    private void callreqStatus(Spinner spinner) {



        String[] expStatus = new String[]{
                "Leave Request Status",
                "NOT APPROVED",
                "APPROVED",
                "RECOMMENDED",
                "REJECTED",
                "CANCEL"


        };

        final List<String> expstatuslist = new ArrayList<>(Arrays.asList(expStatus));

        // Initializing an ArrayAdapter
        spinnerexpstatusArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.requestrstatus_item, expstatuslist);

        spinnerexpstatusArrayAdapter.setDropDownViewResource(R.layout.requestrstatus_item);
        spinner.setAdapter(spinnerexpstatusArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    strReqstatus = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}
