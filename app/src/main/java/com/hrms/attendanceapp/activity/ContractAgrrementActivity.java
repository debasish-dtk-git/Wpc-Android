package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.utils.Utils;

import java.io.UnsupportedEncodingException;

public class ContractAgrrementActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    WebView webView;
    ProgressBar progressBar;
    private ImageView backbutton;
    private String strReg = "";
    private String emId = "";
    private LinearLayout lin;
    private String strEmId = "";
    private String stEmpCode = "";
    private ImageView imgLogo;
    private String st_EmId = "";
    private String st_mpCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(ContractAgrrementActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contract_agrrement);
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();




        strEmId = sh_Pref.getString("EMID", "");
        stEmpCode = sh_Pref.getString("EMPCODE", "");


        String strcomLogo = sh_Pref.getString("COM_LOGO", "");


        st_EmId = toBase64(strEmId);
        st_mpCode = toBase64(stEmpCode);

        initViews();

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(ContractAgrrementActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }






    }

    private void initViews() {

        lin = (LinearLayout)findViewById(R.id.act_conactaggrement);
        backbutton = (ImageView) findViewById(R.id.backbutton_contact_aggre);
        backbutton.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_logo_contact_aggre);
        webView = (WebView) findViewById(R.id.webview_contact_aggre);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_contact_aggre);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);



        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Contact_Of_Employment");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();

            }
        });





        LoadWebViewUrl(Constants.img_url+"appaddemployee-new/contract-agreement/"+st_EmId+"/"+st_mpCode);
    }



    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!progressBar.isShown())
                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressBar.isShown())
                progressBar.setVisibility(View.GONE);



        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (progressBar.isShown())
                progressBar.setVisibility(View.GONE);
            //  Toast.makeText(MyProfileActivity.this, "Unexpected error occurred.Reload page again.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (progressBar.isShown())
                progressBar.setVisibility(View.GONE);
            //Toast.makeText(MyProfileActivity.this, "Unexpected error occurred.Reload page again.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            if (progressBar.isShown())
                progressBar.setVisibility(View.GONE);
            Toast.makeText(ContractAgrrementActivity.this, "Unexpected SSL error occurred.Reload page again.", Toast.LENGTH_SHORT).show();

        }

    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            isWebViewCanGoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void isWebViewCanGoBack() {
        if (webView.canGoBack())
            webView.goBack();
        else
            finish();
    }


    private void LoadWebViewUrl(String url) {
        if (Utils.checkInternetConnection(ContractAgrrementActivity.this))
            webView.loadUrl(url);
        else {

            Toast.makeText(ContractAgrrementActivity.this, "Oops!! There is no internet connection. Please enable your internet connection.", Toast.LENGTH_LONG).show();

        }
    }






    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backbutton_contact_aggre:
                finish();
                break;
        }
    }



    public static String toBase64(String message) {
        byte[] data;
        try {
            data = message.getBytes("UTF-8");
            String base64Sms = Base64.encodeToString(data, Base64.DEFAULT);
            return base64Sms;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

}


