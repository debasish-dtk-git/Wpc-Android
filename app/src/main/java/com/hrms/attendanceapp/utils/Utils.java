package com.hrms.attendanceapp.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.Constants;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {
	
	Activity mContext;
	SharedPreferences persistentStore;
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA
	};
	
	public Utils(Activity context)
	{
		mContext = context;
		persistentStore = mContext.getSharedPreferences(Constants.PERSISTENT_STORE_NAME, Context.MODE_PRIVATE);
	}


	// The public static function which can be called from other classes
	public static void darkenStatusBar(Activity activity, int color) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

			activity.getWindow().setStatusBarColor(
					darkenColor(
							ContextCompat.getColor(activity, color)));
		}

	}


	// Code to darken the color supplied (mostly color of toolbar)
	private static int darkenColor(int color) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= 0.8f;
		return Color.HSVToColor(hsv);
	}




	public static Object chkNull(Object pData){
		return (pData==null?"":pData);
	}






	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static boolean checkAllCon(Context ctx){

		ConnectivityManager mConnectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony =  (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null ||!mConnectivity.getBackgroundDataSetting()) {
			return false;
		}

		// Only update if WiFi or 3G is connected and not roaming
		int netType = info.getType();
		int netSubtype = info.getSubtype();

		if (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_MOBILE) {
			return info.isConnected();
		} else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {
			return info.isConnected();
		} else {
			return false;
		}
	}

	public static boolean checkInternetConnection(Activity activity) {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Network network = null;
			if (connectivityManager == null) {
				return false;
			} else {
				network = connectivityManager.getActiveNetwork();
				NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
				if (networkCapabilities == null) {
					return false;
				}
				if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
						networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
					return true;
				}
			}
		} else {
			if (connectivityManager == null) {
				return false;
			}
			if (connectivityManager.getActiveNetworkInfo() == null) {
				return false;
			}
			return connectivityManager.getActiveNetworkInfo().isConnected();
		}
		return false;
	}



public void showDialog(int msg)

{
	new AlertDialog.Builder(mContext)
	.setMessage(msg)
	.setCancelable(false)
	.setPositiveButton("OK", new OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
		}
	}).show();
		
	
}		

public void showDialog(String msg)

{
	new AlertDialog.Builder(mContext)
	.setMessage(msg)
	.setCancelable(false)
	.setPositiveButton("OK", new OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
			
		}
	}).show();
		
	
	
}


	public static String getDate(int date)
	{
		String modifydate = "";

		if(date < 10)
		{
			modifydate = "0"+String.valueOf(date);
		}
		else{
			modifydate = String.valueOf(date);
		}
		return modifydate;
	}




	public static String parseDateToddMMyyyy(String time) {
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


	public static String parseDateToyyyyMMdd(String time) {
		String inputPattern = "dd-MM-yyyy";
		String outputPattern = "yyyy-MM-dd";
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





	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void setStatusBarGradiant(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			Drawable background = ContextCompat.getDrawable(activity, R.drawable.statusbar);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
			window.setNavigationBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
			window.setBackgroundDrawable(background);
		}
	}


	public static void verifyStoragePermissions(Activity activity) {
		// Check if we have read or write permission
		int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
		int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

		if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE
			);
		}
	}



	public static String getBase64FromFile(String path)
	{
		Bitmap bmp = null;
		ByteArrayOutputStream baos = null;
		byte[] baat = null;
		String encodeString = null;
		try
		{
			bmp = BitmapFactory.decodeFile(path);
			baos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			baat = baos.toByteArray();
			encodeString = Base64.encodeToString(baat, Base64.DEFAULT);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return encodeString;
	}









}
