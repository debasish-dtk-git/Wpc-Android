package com.hrms.attendanceapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.NotiList;
import com.hrms.attendanceapp.utils.GlideImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.ProductViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<NotiList> productList;


    private NotiAdapter.onRecyclerViewItemClickListener mItemClickListener;

    public void setOnItemClickListener(NotiAdapter.onRecyclerViewItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onRecyclerViewItemClickListener {
        void onItemClickListener(View view, int position);
    }



    //getting the context and product list with constructor
    public NotiAdapter(Context mCtx, List<NotiList> productList) {
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

        View rootView = LayoutInflater.from(mCtx).inflate(R.layout.layout_noti, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ProductViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        NotiList product = productList.get(position);

            //apply the button background to newly created drawable gradient
            holder.cartview.setCardBackgroundColor(Color.WHITE);

        String imurl = Constants.img_url + product.getImg();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .priority(Priority.HIGH);

        new GlideImageLoader(holder.imgview, holder.progressbar).load(imurl,options);
        if(product.getReadfu().equalsIgnoreCase("1")) {
            //binding the data with the viewholder views
            holder.textTitle.setText(product.getTitle().substring(0, 1).toUpperCase() + product.getTitle().substring(1).toLowerCase());
            holder.textTitle.setTextColor(Color.parseColor("#9c9b9b"));
        }
        else{
            holder.textTitle.setText(product.getTitle().substring(0, 1).toUpperCase() + product.getTitle().substring(1).toLowerCase());
            holder.textTitle.setTextColor(Color.parseColor("#444343"));
        }


        holder.textdesc.setText(product.getDesc().substring(0, 1).toUpperCase() + product.getDesc().substring(1).toLowerCase());
         holder.textlvDate.setText(product.getDate());


        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cartview;
        CircleImageView imgview;
        ProgressBar progressbar;
        TextView textTitle, textdesc, textlvDate;


        public ProductViewHolder(View itemView) {
            super(itemView);
             cartview = itemView.findViewById(R.id.post_card_view1);
            imgview = itemView.findViewById(R.id.imageview_noti_emp);
            progressbar = itemView.findViewById(R.id.homeprogress_noti_emp);
             textTitle = itemView.findViewById(R.id.textview_noti_title);
             textdesc = itemView.findViewById(R.id.textview_noti_desc);
             textlvDate = itemView.findViewById(R.id.textview_date);

            cartview.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(v, getAdapterPosition());
            }
        }
    }
}
