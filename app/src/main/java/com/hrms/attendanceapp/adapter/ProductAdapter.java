package com.hrms.attendanceapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.getset.Upcomingholiday;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Upcomingholiday> productList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<Upcomingholiday> productList) {
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

        View rootView = LayoutInflater.from(mCtx).inflate(R.layout.layout_products, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ProductViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Upcomingholiday product = productList.get(position);
        if(position%2 == 0)
        {
           /* int[] colors = {Color.parseColor("#b22d01"), Color.parseColor("#f93e04")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, colors);

            gd.setCornerRadius(0f);
            //apply the button background to newly created drawable gradient
            holder.cartview.setBackground(gd);*/

            holder.cartview.setBackgroundColor(Color.parseColor("#e0f8f9"));
        }
        else{
           /* int[] colors = {Color.parseColor("#3f4887"), Color.parseColor("#035ef6")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, colors);

            gd.setCornerRadius(0f);
            //apply the button background to newly created drawable gradient
            holder.cartview.setBackground(gd);*/
            holder.cartview.setBackgroundColor(Color.parseColor("#ffe1ed"));
        }

        String[] items1 = product.getFromdate().split("-");

        String year = items1[0];
        String month = items1[1];
        String day = items1[2];
        Calendar cal= Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        int monthnum= Integer.parseInt(month);
        cal.set(Calendar.MONTH, monthnum - 1);
        String month_name = month_date.format(cal.getTime());

        //binding the data with the viewholder views
         holder.textholidy.setText(product.getHolidaytype());
         holder.textday.setText(day);
         holder.textmonth.setText(month_name+", "+year);


        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        CardView cartview;
        TextView textholidy, textday, textmonth;


        public ProductViewHolder(View itemView) {
            super(itemView);
             cartview = itemView.findViewById(R.id.card_view_subcat__);
             textday = itemView.findViewById(R.id.textview_day);
             textmonth = itemView.findViewById(R.id.textview_month_year);
            textholidy = itemView.findViewById(R.id.textview_holiday_type);


        }
    }
}
