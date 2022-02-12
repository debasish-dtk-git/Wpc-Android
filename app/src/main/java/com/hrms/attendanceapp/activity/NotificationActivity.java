package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.adapter.NotiAdapter;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.NotiList;
import com.hrms.attendanceapp.utils.CustomProgress;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private LinearLayout lin;
    private String strEmpUserIdd = "";
    private RecyclerView recyclerViewNoti;
    private String strcompnyimg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(NotificationActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notification);

        sh_Pref = getSharedPreferences("Login Credentials", Context.MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        strEmpUserIdd = sh_Pref.getString("USER_IDD", "");

        strcompnyimg = sh_Pref.getString("COM_LOGO", "");



        lin = (LinearLayout)findViewById(R.id.activity_noti);




        imgBack = (ImageView)findViewById(R.id.imageview_noti_org_back);
        imgBack.setOnClickListener(this);


        recyclerViewNoti = (RecyclerView) findViewById(R.id.recycler_view_noti_org);
        recyclerViewNoti.setHasFixedSize(true);
        recyclerViewNoti.setLayoutManager(new LinearLayoutManager(this));






        callNotificationList(strEmpUserIdd);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_noti_org_back:
                finish();
                break;
        }
    }



    private void callNotificationList(String userId) {


        //setProgressDialog();
        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(NotificationActivity.this, messag, false);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getViewNotification(userId);


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


                    final ArrayList<NotiList> notilist = new ArrayList<NotiList>();
                    JSONArray jsonArray = jresData.getJSONArray("notification");
                    for(int i = 0; i<jsonArray.length(); i++)
                    {

                        JSONObject jsonobj_ = jsonArray.getJSONObject(i);


                        String id = jsonobj_.getString("id");

                        String employeeid = jsonobj_.getString("employee_id");
                        String emid = jsonobj_.getString("emid");
                        String subject = jsonobj_.getString("sub");
                        String strcontent = jsonobj_.getString("content");
                        String strdate = jsonobj_.getString("cr_date");
                        String strReadfu = jsonobj_.getString("read_fu");

                        String stremp_fname = jsonobj_.getString("emp_fname");
                        String stremp_mname = jsonobj_.getString("emp_mname");
                        String stremp_lname = jsonobj_.getString("emp_lname");

                        String stremp_img = jsonobj_.getString("emp_image");






                        notilist.add(new NotiList(id, Utils.parseDateToddMMyyyy(strdate), subject, strcontent, strReadfu, stremp_img));


                    }



                    NotiAdapter notiadapter = new NotiAdapter(this, notilist);

                    //setting adapter to recyclerview
                    recyclerViewNoti.setAdapter(notiadapter);


                    notiadapter.notifyDataSetChanged();// Notify the adapter
                    notiadapter.setOnItemClickListener(new NotiAdapter.onRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClickListener(View view, int position) {
                            //perform click logic here (position is passed)


                           String strId = notilist.get(position).getId();
                           Intent intent = new Intent(NotificationActivity.this, NotiDetailsActivity.class);
                           intent.putExtra("NOTI_ID", strId);
                           startActivity(intent);




                        }
                    });

                }
                else{
                    String error = jresData.getString("message");
                    Toast.makeText(NotificationActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }







        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }






}
