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
import com.hrms.attendanceapp.getset.AttandenceList;
import com.hrms.attendanceapp.getset.AttandenceSubList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 4/5/2019.
 */

public class AttandenceListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<AttandenceList> groups;

    public AttandenceListAdapter(Context context, ArrayList<AttandenceList> groups) {
        this.context = context;
        this.groups = groups;
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        ArrayList<AttandenceSubList> chList = groups.get(groupPosition).getAttandenceSubLists();

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
        AttandenceSubList vechild = (AttandenceSubList) getChild(groupPosition,
                childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.attendance_sub_list, null);
        }




        TextView vltimein = (TextView)convertView.findViewById(R.id.textview_attende_timein);
        TextView vltimeout = (TextView)convertView.findViewById(R.id.textview_attende_timeout);
        TextView vltimeinloc = (TextView)convertView.findViewById(R.id.textview_attende_timeinloc);
        TextView vltimeoutloc = (TextView)convertView.findViewById(R.id.textview_attende_timeoutloc);
        TextView vltimeoutdutyhours = (TextView)convertView.findViewById(R.id.textview_attende_dutyhours);


        vltimein.setText(vechild.getTimein());
        vltimeout.setText(vechild.getTimeout().replaceAll("null", ""));
        vltimeinloc.setText(vechild.getTimein_location().replaceAll("null", ""));
        vltimeoutloc.setText(vechild.getTimeout_location().replaceAll("null", ""));
        vltimeoutdutyhours.setText(vechild.getDutyhours().replaceAll("null", ""));





        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        ArrayList<AttandenceSubList> chList = groups.get(groupPosition).getAttandenceSubLists();
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
        AttandenceList group = (AttandenceList) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);


            convertView = inf.inflate(R.layout.attendance_list, null);

        }

        TextView tvdate = (TextView) convertView.findViewById(R.id.textview_atten_date);
        //TextView tvdutyhours = (TextView) convertView.findViewById(R.id.textview_atten_dutyhours);

        ImageView listHeaderArrow = (ImageView) convertView.findViewById(R.id.imageview_attendance_details_);

        tvdate.setText(parseDateToddMMyyyy(group.getDate()));
        //String string=group.getDutyhours().replaceAll("[^\\p{Print}]","");
        //tvdutyhours.setText(group.getDutyhours().replaceAll("null", ""));

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

}
