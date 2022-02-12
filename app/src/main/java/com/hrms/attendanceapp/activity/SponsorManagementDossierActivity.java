package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.utils.Utils;

import java.io.UnsupportedEncodingException;

public class SponsorManagementDossierActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    WebView webView;
    ProgressBar progressBar;
    private ImageView backbutton;
    private String strReg = "";
    private String emId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(SponsorManagementDossierActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sponsor_management_dossier);
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        strReg = sh_Pref.getString("REG", "");


        emId = toBase64(strReg).trim();

        initViews();




    }

    private void initViews() {
        backbutton = (ImageView) findViewById(R.id.backbutton_sponsormanagementdossier_org);
        backbutton.setOnClickListener(this);


        webView = (WebView) findViewById(R.id.webview_sponsormanagementdossier_org);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_sponsormanagementdossier_org);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);


        LoadWebViewUrl(Constants.img_url+"sponsor-management-dossier");
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
            Toast.makeText(SponsorManagementDossierActivity.this, "Unexpected SSL error occurred.Reload page again.", Toast.LENGTH_SHORT).show();

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
        if (Utils.checkInternetConnection(SponsorManagementDossierActivity.this))
            webView.loadUrl(url);
        else {

            Toast.makeText(SponsorManagementDossierActivity.this, "Oops!! There is no internet connection. Please enable your internet connection.", Toast.LENGTH_LONG).show();

        }
    }






    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backbutton_sponsormanagementdossier_org:
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

