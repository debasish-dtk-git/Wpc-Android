package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.utils.CustomProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE_GALLERY_IMAGE = 1003;
    private EditText edtComName;
    private EditText edtfName;
    private EditText edtDesig;
    private EditText edtEmailid;
    private EditText edtPassw;
    private EditText edtConPsw;
    private EditText edtPhoneNo;
    //private TextView txtLogo;
    private Button btnRegi;
    private Button btnLogin;
    private File flew;
    private String stphotoPah = "";
    private Bitmap bmp = null;
    private EditText edtlName;
    private JSONObject jsonObject1;
    private LinearLayout lin;
    boolean imgflag=false;
    private ImageView imgTerms1, imgTerms2;
    private String cont1 = "";
    private String cont2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(RegistrationActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registration);


      /*  if(Build.VERSION.SDK_INT > 22) {
            Utils.verifyStoragePermissions(RegistrationActivity.this);
        }
        Constants.imageFilePath = "";*/

        lin = (LinearLayout)findViewById(R.id.act_regi);

        edtComName = (EditText)findViewById(R.id.edittext_emp_comname);
        edtfName = (EditText)findViewById(R.id.edittext_emp_fname);
        edtlName = (EditText)findViewById(R.id.edittext_emp_lname);
        //edtDesig = (EditText)findViewById(R.id.edittext_emp_desig);
        edtEmailid = (EditText)findViewById(R.id.edittext_emp_email);
        edtPassw = (EditText)findViewById(R.id.edittext_emp_pass);
        edtConPsw = (EditText)findViewById(R.id.edittext_emp_conpass);
        edtPhoneNo = (EditText)findViewById(R.id.edittext_emp_phone_no);

       /* txtLogo = (TextView)findViewById(R.id.textview_emp_org_logo);
        txtLogo.setOnClickListener(this);*/

        imgTerms1 = (ImageView) findViewById(R.id.imgeview_terms1_);
        imgTerms1.setImageResource(R.drawable.tick_blank);
        imgTerms1.setOnClickListener(this);
        imgTerms2 = (ImageView) findViewById(R.id.imgeview_terms2_);
        imgTerms2.setImageResource(R.drawable.tick_blank);
        imgTerms2.setOnClickListener(this);

        btnRegi = (Button)findViewById(R.id.button_regi__);
        btnRegi.setOnClickListener(this);

        btnLogin = (Button)findViewById(R.id.button_login_reg);
        btnLogin.setOnClickListener(this);

       /* Constants.imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", Constants.imageFilePath);*/
    }

    /**
     * Request for camera app to open and capture image.
     * @param isFromGallery-if true then launch gallery app else camera app.
     */
   /* public void startIntent(boolean isFromGallery) {
          if (isFromGallery) {
            File imageFile = new File(Constants.imageFilePath);
            Uri imageFileUri = Uri.fromFile(imageFile); // convert path to Uri
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);   // set the image file name
            startActivityForResult(
                    Intent.createChooser(intent, "Select File"),
                    REQUEST_CODE_GALLERY_IMAGE);
        }
    }*/


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            final String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();


            //copy the selected file of gallery into app's sdcard folder and perform the compression operations on it.
            //And override the original image with the newly resized image.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CommonUtils.copyFile(picturePath, Constants.imageFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            new ImageCompression().execute(Constants.imageFilePath);

        }
    }

*/
    /**
     * Asynchronos task to reduce an image size without affecting its quality and set in imageview.
     */
   /* public class ImageCompression extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return CommonUtils.compressImage(strings[0]);
        }

        protected void onPostExecute(String imagePath) {
            stphotoPah = Utils.getBase64FromFile(imagePath);
            File  flew = new File(imagePath);
            try {
                bmp = getBitmapFromUri(Uri.fromFile(flew));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

           String strImagePath = uploadFile(imagePath);

            txtLogo.setText(strImagePath);



        }
    }*/

    private String uploadFile(String uslpath) {
        String sourcefileuri = "";

        int day, month, year;
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);

        String name=(hour+""+minute+""+second+"_"+day+"_"+(month+1)+"_"+year);
        String tag=name+".jpg";
        sourcefileuri = uslpath.replace(uslpath,tag);

        return sourcefileuri;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }






    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgeview_terms1_:
                if (!imgflag) {

                    imgTerms1.setImageResource(R.drawable.tick);
                    imgflag=true;
                    cont1 = "y";


                }
                else {

                    imgTerms1.setImageResource(R.drawable.tick_blank);
                    imgflag=false;
                    cont1 = "";

                }

                break;
            case R.id.imgeview_terms2_:
                if (!imgflag) {

                    imgTerms2.setImageResource(R.drawable.tick);
                    imgflag=true;
                    cont2 = "y";


                }
                else {

                    imgTerms2.setImageResource(R.drawable.tick_blank);
                    imgflag=false;
                    cont2 = "";

                }

                break;
            case R.id.button_regi__:



                String strComName = edtComName.getText().toString().trim();
                String strFName = edtfName.getText().toString().trim();
                String strLName = edtlName.getText().toString().trim();
                //String strDesign = edtDesig.getText().toString().trim();
                String strEmail = edtEmailid.getText().toString().trim();
                String strPasswd = edtPassw.getText().toString().trim();
                String strConPasswd = edtConPsw.getText().toString().trim();
                String strPhone = edtPhoneNo.getText().toString().trim();


                if(strComName.length() == 0) {
                    edtComName.setError("Company Name required!");

                }
                else if(strFName.length() == 0) {
                    edtfName.setError("First Name required!");

                }
                else if(strLName.length() == 0) {
                    edtlName.setError("Last Name required!");

                }
               /* else if(strDesign.length() == 0) {
                    edtDesig.setError("Designation required!");

                }*/
                else if(strEmail.length() == 0)
                {
                    edtEmailid.setError("Email required!");
                }
                else if(!isValidEmail(strEmail)) {
                    Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                }
                else if(strPasswd.length()==0) {
                    edtPassw.setError("Password required!");
                }
                else if(!strPasswd.equalsIgnoreCase(strConPasswd)) {
                    Toast.makeText(getApplicationContext(),"Password and Confirm Password should be same.",Toast.LENGTH_SHORT).show();
                }
                else if(strPhone.length()==0) {
                    edtPhoneNo.setError("Phone No required!");
                }
                else if(cont1.equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Agree Terms & Conditions and Privacy Policy",Toast.LENGTH_SHORT).show();
                }
                else if(cont2.equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Agree I understand that they do not, in any way, replace immigration advice",Toast.LENGTH_SHORT).show();
                }
                else{

                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                   // https://workpermitcloud.co.uk/hrms/api/registrationapi?email=dasankita406@gmail.com&pass=1234&com_name=abc&f_name=ahjj&l_name=mmm&p_no=858394561
                  /*  jsonObject1 = new JSONObject();

                    try {
                        jsonObject1.put("com_name", strComName);
                        jsonObject1.put("f_name", strFName);
                        jsonObject1.put("l_name", strLName);
                       //jsonObject1.put("desig", "");
                        jsonObject1.put("email", strEmail);
                        jsonObject1.put("pass", strPasswd);

                        jsonObject1.put("p_no", strPhone);
                       // jsonObject1.put("image", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    userSignUp(strComName, strFName, strLName, strEmail, strPasswd, strPhone);
                }

                break;
            case R.id.button_login_reg:
                Intent intetn = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intetn);
                finish();

                break;

        }
    }





    private void userSignUp(String strComName, String strFName, String strLName, String strEmail, String strPasswd, String strPhone) {

        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(RegistrationActivity.this, messag, false);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL).client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<String> call = service.getSignUp(strComName, strFName, strLName, strEmail, strPasswd, strPhone);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar

                customProgress.hideProgress();



                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();


                        try {

                            JSONObject jsonobj = new JSONObject(jsonresponse);

                            String status = jsonobj.getString("status");
                            if(status.equalsIgnoreCase("true"))
                            {
                                String mess = jsonobj.getString("msg");

                                Toast.makeText(RegistrationActivity.this, mess, Toast.LENGTH_LONG).show();

                                Intent intetn = new Intent(RegistrationActivity.this, LoginActivity.class);
                                intetn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intetn);
                                finish();



                            }







                        } catch (JSONException e) {
                            e.printStackTrace();
                        }






                    } else {

                        Snackbar snackbar = Snackbar
                                .make(lin, "No Data Found, Please try again later.", Snackbar.LENGTH_LONG);


                        snackbar.show();



                    }


                }
            }



            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar
                customProgress.hideProgress();


                if (t instanceof IOException) {

                    Snackbar snackbar = Snackbar
                            .make(lin, "Not connected to Internet", Snackbar.LENGTH_LONG);


                    snackbar.show();




                }
                else {

                    Snackbar snackbar = Snackbar
                            .make(lin, "Internet Connection is unavailable, Please try again later.", Snackbar.LENGTH_LONG);


                    snackbar.show();

                    // todo log to some central bug tracking service
                }





            }


        });



    }







    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = ContextCompat.getDrawable(activity, R.drawable.statusbar_login);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            window.setNavigationBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
