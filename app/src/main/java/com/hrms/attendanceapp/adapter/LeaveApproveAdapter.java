package com.hrms.attendanceapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.getset.LeaveList;
import com.hrms.attendanceapp.getset.ViewLeaveList;
import com.hrms.attendanceapp.utils.Utils;

import java.util.List;

public class LeaveApproveAdapter extends RecyclerView.Adapter<LeaveApproveAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<ViewLeaveList> productList;

    //getting the context and product list with constructor
    public LeaveApproveAdapter(Context mCtx, List<ViewLeaveList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        /*LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_products, null);
        return new ProductViewHolder(view);
*/

        View rootView = LayoutInflater.from(mCtx).inflate(R.layout.layout_leave_approve, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ProductViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        ViewLeaveList product = productList.get(position);
        if(position%2 == 0)
        {
            holder.cartview.setBackgroundColor(Color.parseColor("#e0f8f9"));
        }
        else{


            holder.cartview.setBackgroundColor(Color.parseColor("#ffe1ed"));
        }


        if (Build.VERSION.SDK_INT >= 24) {
            holder.texttype.setText(Html.fromHtml(getColoredSpanned("Leave Type ", "#970842") + getColoredSpanned(": "+product.getLeavetype(), "#000000 "), Html.FROM_HTML_MODE_LEGACY));
            holder.textstatus.setText(Html.fromHtml(getColoredSpanned("Status ", "#970842") + getColoredSpanned(": "+product.getStatus(), "#000000 "), Html.FROM_HTML_MODE_LEGACY));
            holder.textDateofAppli.setText(Html.fromHtml(getColoredSpanned("Date of Application ", "#970842") + getColoredSpanned(": "+Utils.parseDateToddMMyyyy(product.getDateOfApply()), "#000000 "), Html.FROM_HTML_MODE_LEGACY));
            holder.textDateofleave.setText(Html.fromHtml(getColoredSpanned("No. of Leave(s) ", "#970842") + getColoredSpanned(": "+product.getNoOfLeave(), "#000000 "), Html.FROM_HTML_MODE_LEGACY));
            holder.textduration.setText(Html.fromHtml(getColoredSpanned("Duration ", "#970842") + getColoredSpanned(": "+ Utils.parseDateToddMMyyyy(product.getFromdate()) + " to " + Utils.parseDateToddMMyyyy(product.getTodate()), "#000000 "), Html.FROM_HTML_MODE_LEGACY));
           if(product.getRemarks().equalsIgnoreCase("null"))
            holder.textremarks.setText(Html.fromHtml(getColoredSpanned("Remarks", "#970842") + getColoredSpanned("- No Information Available", "#000000 "), Html.FROM_HTML_MODE_LEGACY));
           else
               holder.textremarks.setText(Html.fromHtml(getColoredSpanned("Remarks", "#970842") + getColoredSpanned(": "+product.getRemarks(), "#000000 "), Html.FROM_HTML_MODE_LEGACY));


        } else {
            holder.texttype.setText(Html.fromHtml("<font color='#970842'>Leave Type </font> <font color='#000000'> - "+product.getLeavetype()+"</font>"));
            holder.textstatus.setText(Html.fromHtml("<font color='#970842'>Status </font> <font color='#000000'> - "+product.getStatus()+"</font>"));
            holder.textDateofAppli.setText(Html.fromHtml("<font color='#970842'>Date of Application </font> <font color='#000000'> - "+Utils.parseDateToddMMyyyy(product.getDateOfApply())+"</font>"));

            holder.textDateofleave.setText(Html.fromHtml("<font color='#970842'>No. of Leave(s) </font> <font color='#000000'> - "+product.getNoOfLeave()+"</font>"));
            holder.textduration.setText(Html.fromHtml("<font color='#970842'>Duration </font> <font color='#000000'> - "+Utils.parseDateToddMMyyyy(product.getDateOfApply())+"</font>"));
            holder.textremarks.setText(Html.fromHtml("<font color='#970842'>Remarks (If Any) </font> <font color='#000000'> - "+product.getRemarks()+"</font>"));


        }




        //binding the data with the viewholder views
       //  holder.texttype.setText(product.getLeavetype()+"("+product.getAlies()+")");
        // holder.textlvHand.setText(product.getLeaveinhand());


        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        CardView cartview;
        TextView texttype, textstatus, textDateofAppli, textDateofleave, textduration, textremarks;


        public ProductViewHolder(View itemView) {
            super(itemView);
             cartview = itemView.findViewById(R.id.post_card_view1);
             texttype = itemView.findViewById(R.id.textview_leave_type);
             textstatus = itemView.findViewById(R.id.textview_status);
             textDateofAppli = itemView.findViewById(R.id.textview_date_of_appli);
             textDateofleave = itemView.findViewById(R.id.textview_no_of_leave);
             textduration = itemView.findViewById(R.id.textview_duration);
             textremarks = itemView.findViewById(R.id.textview_remarks);


        }
    }


    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
