package com.hrms.attendanceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.getset.MonitorList;
import com.hrms.attendanceapp.getset.SubMonitorList;
import java.util.ArrayList;


/**
 * Created by Hirak on 30/04/2021.
 */

public class MonitoringReportAdapter extends BaseExpandableListAdapter {

    customButtonListener customListner;
    private String pon = "";

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }


    private Context context;
    private ArrayList<MonitorList> groups;

    public MonitoringReportAdapter(Context context, ArrayList<MonitorList> groups) {
        this.context = context;
        this.groups = groups;
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        ArrayList<SubMonitorList> chList = groups.get(groupPosition).getMonitoringsub();

        return chList.get(childPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        SubMonitorList vechild = (SubMonitorList) getChild(groupPosition,
                childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.monitoringreporting_job_sub, null);
        }



        TextView vlmobileno = (TextView)convertView.findViewById(R.id.textview_mobile_no_);
        TextView vlnatiinal = (TextView)convertView.findViewById(R.id.textview_nationality_);
        TextView vlnino = (TextView)convertView.findViewById(R.id.textview_ni_number);
        TextView tvdob = (TextView) convertView.findViewById(R.id.textview_dob_);
        TextView vlvisareminder90 = (TextView)convertView.findViewById(R.id.textview_visa_reminder_90_);
        TextView vlvisareminder60 = (TextView)convertView.findViewById(R.id.textview_visa_reminder_60_);
        TextView vlvisareminder30 = (TextView)convertView.findViewById(R.id.textview_visa_reminder_30_);
        TextView vlpassportno = (TextView)convertView.findViewById(R.id.textview_passport_no);
        TextView vladdres = (TextView)convertView.findViewById(R.id.textview_address_);

        vlmobileno.setText((vechild.getPhone() == "null") ? "" : vechild.getPhone());
        vlnatiinal.setText((vechild.getNationality() == "null") ? "" : vechild.getNationality());
        vlnino.setText((vechild.getNino() == "null") ? "" : vechild.getNino());
        tvdob.setText((vechild.getDob() == "null") ? "" : vechild.getDob());
        vlvisareminder90.setText((vechild.getVisa_exp_date_90() == "null") ? "" : vechild.getVisa_exp_date_90());
        vlvisareminder60.setText((vechild.getVisa_exp_date_60() == "null") ? "" : vechild.getVisa_exp_date_60());
        vlvisareminder30.setText((vechild.getVisa_exp_date_30() == "null") ? "" : vechild.getVisa_exp_date_30());
        vlpassportno.setText((vechild.getPass_doc_no() == "null") ? "" : vechild.getPass_doc_no());
        vladdres.setText((vechild.getAddress() == "null") ? "" : vechild.getAddress());



        return convertView;

    }




    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        ArrayList<SubMonitorList> chList = groups.get(groupPosition).getMonitoringsub();
        if(chList.size()>0)
        {
            return chList.size();
        }
        else{
            return 0;
        }


    }


    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return  groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groups.size();
    }






    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }




    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        MonitorList group = (MonitorList) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);


            convertView = inf.inflate(R.layout.monitoringreporting_job_no, null);

        }
        TextView tvempId = (TextView) convertView.findViewById(R.id.textview_emp_id_);
        TextView tvcusnm = (TextView) convertView.findViewById(R.id.textview_emp_name_);
        TextView vlvisaexp = (TextView)convertView.findViewById(R.id.textview_visa_expired_);


        ImageView listHeaderArrow = (ImageView) convertView.findViewById(R.id.imageview_sub_details_incom);
        tvempId.setText(group.getEmpcode());
        tvcusnm.setText(group.getName());
        vlvisaexp.setText((group.getVisa_exp_date() == "null") ? "" : group.getVisa_exp_date());



        //Set the arrow programatically, so we can control it
        if (getChildrenCount(groupPosition) > 0) {
            int imageResourceId = isExpanded ? R.drawable.minus_icon : R.drawable.plus_icon;
            listHeaderArrow.setImageResource(imageResourceId);
            listHeaderArrow.setVisibility(View.VISIBLE);

            listHeaderArrow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                    else ((ExpandableListView) parent).expandGroup(groupPosition, true);

                }
            });
        }
        else{
            listHeaderArrow.setVisibility(View.GONE);
        }
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}


