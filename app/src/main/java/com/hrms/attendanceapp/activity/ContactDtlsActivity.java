package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.BuildConfig;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.adapter.NothingSelectedSpinnerAdapter;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.CountryList;
import com.hrms.attendanceapp.utils.CommonUtils;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ContactDtlsActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private ImageView imgLogo;
    private CircleImageView profilepic;
    private ImageView captureImage;
    private Button btnNext;
    ArrayAdapter<CountryList> spinnernationalitystatusArrayAdapter;
    private EditText edtAdds1_pr;
    private EditText edtAdds2_pr;
    private EditText edtroadname_pr;
    private EditText edtcity_pr;
    private EditText edtZipcode_pr;
    private Spinner spinnerCountry_pr;

    private EditText edtAdds1_ps;
    private EditText edtAdds2_ps;
    private EditText edtroadname_ps;
    private EditText edtcity_ps;
    private EditText edtZipcode_ps;
    private Spinner spinnerCountry_ps;
    private CheckBox chkadds;
    private LinearLayout linUploadadds_pr;
    private LinearLayout linUploadadds_ps;
    private Button btnfromGally;
    private Button btntakephoto;
    private final int REQUEST_CODE_CLICK_IMAGE = 1002, REQUEST_CODE_GALLERY_IMAGE = 1003;
    private File flew;
    private String stphotoPah = "";
    private Bitmap bmp = null;
    int flag = 0;
    private Button btnfromGallyimg;
    private Button btntakephotoimg;
    private JSONObject jsonObject1;
    private LinearLayout lin;
    private String strProfilepic = "";
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(ContactDtlsActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact_dtls);

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        Constants.imageFilePath = "";


        initView();


        String strcomLogo= sh_Pref.getString("COM_LOGO", "");
        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        userId = sh_Pref.getString("USER_CODE", "");

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(ContactDtlsActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }


        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(ContactDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);
            // imgPhoto.setImageResource(R.drawable.noimage);
        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(ContactDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }

        Constants.imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", Constants.imageFilePath);





        for(int i=0; i<Constants.contyLists.size();i++)
        {
            if(Constants.str_country_pr.equalsIgnoreCase(Constants.contyLists.get(i).getName()))
            {



                //set the default according to value
                spinnerCountry_pr.setSelection(i+1);
            }
        }






        edtAdds1_pr.setText(Constants.str_Adds1_pr);
        edtAdds1_pr.setSelection(edtAdds1_pr.getText().length());

        edtAdds2_pr.setText(Constants.str_Adds2_pr);
        edtAdds2_pr.setSelection(edtAdds2_pr.getText().length());

        edtroadname_pr.setText(Constants.str_roadName_pr);
        edtroadname_pr.setSelection(edtroadname_pr.getText().length());

        edtcity_pr.setText(Constants.str_city_pr);
        edtcity_pr.setSelection(edtcity_pr.getText().length());


        edtZipcode_pr.setText(Constants.str_postcode_pr);
        edtZipcode_pr.setSelection(edtZipcode_pr.getText().length());

        if(Constants.str_Adds1_pr.equalsIgnoreCase(Constants.str_Adds1_ps)) {
            chkadds.setChecked(true);




            for(int i=0; i<Constants.contyLists.size();i++)
            {
                if(Constants.str_country_ps.equalsIgnoreCase(Constants.contyLists.get(i).getName()))
                {



                    //set the default according to value
                    spinnerCountry_ps.setSelection(i+1);
                }
            }






            edtAdds1_ps.setText(Constants.str_Adds1_pr);
            disableEditText(edtAdds1_ps);

            edtAdds2_ps.setText(Constants.str_Adds2_pr);
            disableEditText(edtAdds2_ps);

            edtroadname_ps.setText(Constants.str_roadName_pr);
            disableEditText(edtroadname_ps);

            edtcity_ps.setText(Constants.str_city_pr);
            disableEditText(edtcity_ps);


            edtZipcode_ps.setText(Constants.str_postcode_pr);
            disableEditText(edtZipcode_ps);




        }
        else{





            for(int i=0; i<Constants.contyLists.size();i++)
            {
                if(Constants.str_country_ps.equalsIgnoreCase(Constants.contyLists.get(i).getName()))
                {



                    //set the default according to value
                    spinnerCountry_ps.setSelection(i+1);
                }
            }








            edtAdds1_ps.setText(Constants.str_Adds1_ps);
            edtAdds1_ps.setSelection(edtAdds1_ps.getText().length());

            edtAdds2_ps.setText(Constants.str_Adds2_ps);
            edtAdds2_ps.setSelection(edtAdds2_ps.getText().length());

            edtroadname_ps.setText(Constants.str_roadName_ps);
            edtroadname_ps.setSelection(edtroadname_ps.getText().length());

            edtcity_ps.setText(Constants.str_city_ps);
            edtcity_ps.setSelection(edtcity_ps.getText().length());


            edtZipcode_ps.setText(Constants.str_postcode_ps);
            edtZipcode_ps.setSelection(edtZipcode_ps.getText().length());


        }





    }



    @Override
    protected void onResume() {
        super.onResume();

        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(ContactDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);

        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(ContactDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }


    }









    private void initView() {

        lin = (LinearLayout)findViewById(R.id.act_condtl);

        imgBack = (ImageView)findViewById(R.id.backbutton_7);
        imgBack.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_logo_profile7);

        profilepic = (CircleImageView) findViewById(R.id.profile_image7);

        captureImage = (ImageView) findViewById(R.id.captureimage_profile7);
        captureImage.setOnClickListener(this);







        edtAdds1_pr = (EditText)findViewById(R.id.edittext_employee_adds1_cont);
        edtAdds2_pr = (EditText)findViewById(R.id.edittext_employee_adds2_cont);

        edtroadname_pr = (EditText)findViewById(R.id.edittext_employee_roadname_cont);
        edtcity_pr = (EditText)findViewById(R.id.edittext_employee_city_cont);

        edtZipcode_pr = (EditText)findViewById(R.id.edittext_employee_zipcode_cont);



        spinnerCountry_pr = (Spinner)findViewById(R.id.spinner_employee__country_cont);
        callCountry_pr(spinnerCountry_pr);

        linUploadadds_pr = (LinearLayout)findViewById(R.id.lin_uploadadds_cont);
        linUploadadds_pr.setOnClickListener(this);


        edtAdds1_ps = (EditText)findViewById(R.id.edittext_employee_adds1_cont_);
        edtAdds2_ps = (EditText)findViewById(R.id.edittext_employee_adds2_cont_);

        edtroadname_ps = (EditText)findViewById(R.id.edittext_employee_roadname_cont_);
        edtcity_ps = (EditText)findViewById(R.id.edittext_employee_city_cont_);

        edtZipcode_ps = (EditText)findViewById(R.id.edittext_employee_zipcode_cont_);



        spinnerCountry_ps = (Spinner)findViewById(R.id.spinner_employee__country_cont_);
        callCountry_ps(spinnerCountry_ps);


        linUploadadds_ps = (LinearLayout)findViewById(R.id.lin_uploadadds_cont_);
        linUploadadds_ps.setOnClickListener(this);

        chkadds = (CheckBox)findViewById(R.id.checkbox_adds);
        chkadds.setOnClickListener(this);

        btnNext = (Button)findViewById(R.id.next7button);
        btnNext.setOnClickListener(this);





    }


    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.backbutton_7:
               finish();
               break;
           case R.id.captureimage_profile7:
               flag = 5;
               PopupWindow popupwindow_obj = popupUpImgDisplay();
               popupwindow_obj.showAsDropDown(v, 0, 0);

               break;
           case R.id.next7button:

              Constants.adds1_pr = edtAdds1_pr.getText().toString().trim();
              Constants.adds2_pr = edtAdds2_pr.getText().toString().trim();
              Constants.roadname_pr = edtroadname_pr.getText().toString().trim();
              Constants.city_pr = edtcity_pr.getText().toString().trim();
              Constants.Zipcode_pr =  edtZipcode_pr.getText().toString().trim();

               Constants.adds1_ps = edtAdds1_ps.getText().toString().trim();
               Constants.adds2_ps = edtAdds2_ps.getText().toString().trim();
               Constants.roadname_ps = edtroadname_ps.getText().toString().trim();
               Constants.city_ps = edtcity_ps.getText().toString().trim();
               Constants.Zipcode_ps =  edtZipcode_ps.getText().toString().trim();


               Intent intent = new Intent(ContactDtlsActivity.this, HistorydtlsActivity.class);
                startActivity(intent);
               break;
           case R.id.checkbox_adds:
               if (chkadds.isChecked())
               {

                      /* if (Constants.str_country_ps.equalsIgnoreCase("")) {


                       } else {

                           //ArrayAdapter myAdap
                           spinnernationalitystatusArrayAdapter = (ArrayAdapter) spinnerCountry_ps.getAdapter(); //cast to an ArrayAdapter

                           int maritalStatPosition = spinnernationalitystatusArrayAdapter.getPosition(Constants.str_country_pr);

                           //set the default according to value
                           spinnerCountry_ps.setSelection(maritalStatPosition);
                           spinnerCountry_ps.setEnabled(false);
                       }*/

                   for(int i=0; i<Constants.contyLists.size();i++)
                   {
                       if(Constants.str_country_ps.equalsIgnoreCase(Constants.contyLists.get(i).getName()))
                       {



                           //set the default according to value
                           spinnerCountry_ps.setSelection(i+1);
                       }
                   }








                       edtAdds1_ps.setText(Constants.str_Adds1_pr);
                       disableEditText(edtAdds1_ps);

                       edtAdds2_ps.setText(Constants.str_Adds2_pr);
                       disableEditText(edtAdds2_ps);

                       edtroadname_ps.setText(Constants.str_roadName_pr);
                       disableEditText(edtroadname_ps);

                       edtcity_ps.setText(Constants.str_city_pr);
                       disableEditText(edtcity_ps);


                       edtZipcode_ps.setText(Constants.str_postcode_pr);
                       disableEditText(edtZipcode_ps);



                   }
               else{
                   edtAdds1_ps.setText("");

                   edtAdds2_ps.setText("");

                   edtroadname_ps.setText("");

                   edtcity_ps.setText("");



                   edtZipcode_ps.setText("");

                   callCountry_ps(spinnerCountry_ps);
                   spinnerCountry_ps.setEnabled(true);
               }

               break;
           case R.id.lin_uploadadds_cont:
               flag = 1;
               PopupWindow popupwindow_obj2 = popupDisplay();
               popupwindow_obj2.showAsDropDown(v, 0, 0);

               break;
           case R.id.lin_uploadadds_cont_:
               flag = 2;
               PopupWindow popupwindow_obj1 = popupDisplay();
               popupwindow_obj1.showAsDropDown(v, 0, 0);

               break;
       }
    }

    public PopupWindow popupUpImgDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) ContactDtlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.custom_imgup_dialog_box, null);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(view, Gravity.RIGHT, 30, -450);

        btnfromGallyimg = (Button)view.findViewById(R.id.btnChoosePath_imgup);
        btnfromGallyimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
                startIntent(true);

            }
        });

        btntakephotoimg = (Button)view.findViewById(R.id.btnTakePhoto_imgup);
        btntakephotoimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
                startIntent(false);


            }
        });

        return popupWindow;
    }





    public PopupWindow popupDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) ContactDtlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.custom_dialog_box, null);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        btnfromGally = (Button)view.findViewById(R.id.btnChoosePath);
        btnfromGally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startIntent(true);

            }
        });

        btntakephoto = (Button)view.findViewById(R.id.btnTakePhoto);
        btntakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
                startIntent(false);


            }
        });

        return popupWindow;
    }











    private void callCountry_pr(Spinner spinnerCountry) {

        spinnernationalitystatusArrayAdapter  = new ArrayAdapter(ContactDtlsActivity.this,R.layout.maritalstatus_item, Constants.contyLists);
        spinnernationalitystatusArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerCountry.setAdapter(new NothingSelectedSpinnerAdapter(spinnernationalitystatusArrayAdapter, R.layout.info_spinner_row_nothing_counry, ContactDtlsActivity.this));
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                CountryList selectedItemText = (CountryList) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    Constants.country_pr = selectedItemText.getName();
                    String natiId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});



    }








    private void callCountry_ps(Spinner spinnerCountry) {

        spinnernationalitystatusArrayAdapter  = new ArrayAdapter(ContactDtlsActivity.this,R.layout.maritalstatus_item, Constants.contyLists);
        spinnernationalitystatusArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerCountry.setAdapter(new NothingSelectedSpinnerAdapter(spinnernationalitystatusArrayAdapter, R.layout.info_spinner_row_nothing_counry, ContactDtlsActivity.this));
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                CountryList selectedItemText = (CountryList) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    Constants.country_ps = selectedItemText.getName();
                    String natiId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});


    }


    /**
     * Request for camera app to open and capture image.
     * @param isFromGallery-if true then launch gallery app else camera app.
     */
    public void startIntent(boolean isFromGallery) {
        if (!isFromGallery) {
            File imageFile = new File(Constants.imageFilePath);
            //Uri imageFileUri = Uri.fromFile(imageFile); // convert path to Uri


            Uri photoURI = FileProvider.getUriForFile(ContactDtlsActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    imageFile);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);   // set the image file name
            startActivityForResult(intent, REQUEST_CODE_CLICK_IMAGE);
        } else if (isFromGallery) {
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CLICK_IMAGE) {
            new ImageCompression().execute(Constants.imageFilePath);
        } else if (requestCode == REQUEST_CODE_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

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


    /**
     * Asynchronos task to reduce an image size without affecting its quality and set in imageview.
     */
    public class ImageCompression extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return CommonUtils.compressImage(strings[0]);
        }

        protected void onPostExecute(String imagePath) {
            stphotoPah = Utils.getBase64FromFile(imagePath);
            if(flag == 1)
            {
                Constants.doc_pr = stphotoPah;
                flag = 0;
            }
            else if(flag == 2)
            {
                Constants.doc_ps = stphotoPah;
                flag = 0;
            }
            flew = new File(imagePath);
            try {
                bmp = getBitmapFromUri(Uri.fromFile(flew));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            if(flag == 5) {
                profilepic.setImageBitmap(BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath()));

                jsonObject1 = new JSONObject();

                try {
                    jsonObject1.put("user_id", userId);
                    jsonObject1.put("emp_image", stphotoPah);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callUpdateimage(jsonObject1);


            }


        }
    }



    private void callUpdateimage(JSONObject jsonobj) {

        flag = 0;
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


        Call<String> call = service.uploadImage(jsonobj.toString());




        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar



                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();

                        try {

                            JSONObject jresData = new JSONObject(jsonresponse);



                            String status = jresData.getString("resultstatus");
                            if (status.equalsIgnoreCase("true")) {
                                String msg = jresData.getString("msg");
                                strProfilepic = jresData.getString("emp_image");

                                sharedPrefernces(strProfilepic);
                            }


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    } else {

                        Snackbar snackbar = Snackbar
                                .make(lin, "No Data Found, Please try again later.", Snackbar.LENGTH_LONG);


                        snackbar.show();
                        // Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }


            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //progressDialog.dismiss();
                // for hiding the ProgressBar


                if (t instanceof IOException) {

                    Snackbar snackbar = Snackbar
                            .make(lin, "Not connected to Internet", Snackbar.LENGTH_LONG);


                    snackbar.show();




                }
                else {

                    Snackbar snackbar = Snackbar
                            .make(lin, "Internet Connection is unavailable, Please try again later.", Snackbar.LENGTH_LONG);


                    snackbar.show();
                    //showAlert("Internet Connection is unavailable, Please try again later.","Message");
                    // todo log to some central bug tracking service
                }



            }


        });





    }


    private void sharedPrefernces(String strprofilephoto) {
        // TODO Auto-generated method stub
        toEdit.putString("USER_PHOTO", strprofilephoto);


        toEdit.commit();
    }




    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }



    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

}



