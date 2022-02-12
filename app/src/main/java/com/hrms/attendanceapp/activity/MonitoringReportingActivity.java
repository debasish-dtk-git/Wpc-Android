package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.adapter.MonitoringReportAdapter;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.utils.Utils;

public class MonitoringReportingActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lin;
    private ImageView imgBack;
    private ExpandableListView expandList;
    MonitoringReportAdapter monitoringReportAdapter;
    private LinearLayout linMonitoringReporting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(MonitoringReportingActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_monitoring_reporting);

        lin = (LinearLayout)findViewById(R.id.activity_notidtls);

        imgBack = (ImageView)findViewById(R.id.imageview_moni_report_org_back);
        imgBack.setOnClickListener(this);

        linMonitoringReporting = (LinearLayout)findViewById(R.id.lin__monitoring_reporting);

        expandList = (ExpandableListView)findViewById(R.id.listview_monitoring_reporting);

        if( Constants.monitorList.size()>0) {
            linMonitoringReporting.setVisibility(View.VISIBLE);
            monitoringReportAdapter = new MonitoringReportAdapter(MonitoringReportingActivity.this,  Constants.monitorList);
            //monitoringReportAdapter.setCustomButtonListner(OrganisationActivity.this);
            expandList.setAdapter(monitoringReportAdapter);


            monitoringReportAdapter.notifyDataSetChanged();

            expandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {

                    return true;

                }
            });

        }
        else{
            linMonitoringReporting.setVisibility(View.GONE);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_moni_report_org_back:
                finish();
                break;
        }
    }
}