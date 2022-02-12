package com.hrms.attendanceapp.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import de.hdodenhof.circleimageview.CircleImageView;

public class GlideImageLoader {

    private CircleImageView cImageView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    public GlideImageLoader(CircleImageView profilepic, ProgressBar progresbar) {

        cImageView = profilepic;
        mProgressBar = progresbar;
    }


    public GlideImageLoader(ImageView profilepic, ProgressBar progresbar) {

        mImageView = profilepic;
        mProgressBar = progresbar;
    }

    public void load(final String url, RequestOptions options) {
        if (url == null || options == null) return;

        onConnecting();

        //set Listener & start
        ProgressAppGlideModule.expect(url, new ProgressAppGlideModule.UIonProgressListener() {
            @Override
            public void onProgress(long bytesRead, long expectedLength) {
                if (mProgressBar != null) {
                    mProgressBar.setProgress((int) (100 * bytesRead / expectedLength));
                }
            }

            @Override
            public float getGranualityPercentage() {
                return 1.0f;
            }
        });


        if(cImageView == null) {
            //Get Image
            Glide.with(mImageView.getContext())
                    .load(url)
                    //.transition(withCrossFade())
                    .apply(options.skipMemoryCache(true))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            ProgressAppGlideModule.forget(url);
                            onFinished();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ProgressAppGlideModule.forget(url);
                            onFinished();
                            return false;
                        }
                    })
                    .into(mImageView);
        }
        else{
            //Get Image
            Glide.with(cImageView.getContext())
                    .load(url)
                    //.transition(withCrossFade())
                    .apply(options.skipMemoryCache(true))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            ProgressAppGlideModule.forget(url);
                            onFinished();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            ProgressAppGlideModule.forget(url);
                            onFinished();
                            return false;
                        }
                    })
                    .into(cImageView);
        }
    }


    private void onConnecting() {
        if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
    }

    private void onFinished() {
        if (mProgressBar != null && mImageView != null) {
            mProgressBar.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }
        else{
            mProgressBar.setVisibility(View.GONE);
            cImageView.setVisibility(View.VISIBLE);
        }
    }
}
