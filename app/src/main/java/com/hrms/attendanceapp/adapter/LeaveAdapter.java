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
import com.hrms.attendanceapp.getset.LeaveList;
import com.hrms.attendanceapp.getset.Upcomingholiday;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<LeaveList> productList;

    //getting the context and product list with constructor
    public LeaveAdapter(Context mCtx, List<LeaveList> productList) {
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

        View rootView = LayoutInflater.from(mCtx).inflate(R.layout.layout_leave, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ProductViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        LeaveList product = productList.get(position);
        if(position%2 == 0)
        {
            int[] colors = {Color.parseColor("#5b87f5"), Color.parseColor("#dae4fd")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, colors);

            gd.setCornerRadius(0f);
            //apply the button background to newly created drawable gradient
            holder.cartview.setBackground(gd);
        }
        else{
            int[] colors = {Color.parseColor("#80104a"), Color.parseColor("#e7d2dd")};

            //create a new gradient color
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT, colors);

            gd.setCornerRadius(0f);
            //apply the button background to newly created drawable gradient
            holder.cartview.setBackground(gd);
        }



        //binding the data with the viewholder views
         holder.texttype.setText(product.getLeavetype()+" ("+product.getAlies()+")");
         holder.textlvHand.setText(product.getLeaveinhand());


        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        CardView cartview;
        TextView texttype, textlvHand;


        public ProductViewHolder(View itemView) {
            super(itemView);
             cartview = itemView.findViewById(R.id.post_card_view1);
             texttype = itemView.findViewById(R.id.textview_leave_type);
             textlvHand = itemView.findViewById(R.id.textview_leaveinhand);


        }
    }
}
