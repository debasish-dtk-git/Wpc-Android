package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.BuildConfig;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.utils.CommonUtils;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AdditonalDtlsActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE_CLICK_IMAGE = 1002, REQUEST_CODE_GALLERY_IMAGE = 1003;
    DatePickerDialog picker;
    Calendar cldr;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private String userIdd = "";
    private ImageView imgLogo;
    private CircleImageView profilepic;
    private ImageView captureImage;
    private Button btnNext;
    private Spinner spinnerbloodgp;
    ArrayAdapter<String> spinnerBloodgpArrayAdapter;
    private String str_Bloodgp = "";
    private EditText edtWeight;
    private EditText edtHight;
    private EditText edtidentimarks1;
    private EditText edtidentimarks2;
    private Spinner spinnerPhysiChallnge;
    private ArrayAdapter<String> spinnerdisablelistArrayAdapter;
    private Spinner spinnerCriminaloffence;
    private String strcrimeoffence = "";
    private Spinner spinnerEmgRelasion;
    private ArrayAdapter<String> spinnerrelationshiplistArrayAdapter;
    private EditText edtemgName;
    private EditText edtemgEmail;
    private EditText edtemgContc;
    private EditText edtemgAddress;
    private EditText edtTitle;
    private EditText edtLicnNo;
    private TextView txtStartdate;
    private TextView txtEnddate;
    private String strProfilepic = "";
    private LinearLayout lin;
    private JSONObject jsonObject1;
    private Bitmap bmp = null;
    private String stphotoPah = "";
    private Button btnfromGallyimg;
    private Button btntakephotoimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(AdditonalDtlsActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_additonal_dtls);

        Constants.imageFilePath = "";

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();


        initView();

        userIdd = sh_Pref.getString("USER_CODE", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");
        strProfilepic = sh_Pref.getString("USER_PHOTO", "");

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(AdditonalDtlsActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }

        Constants.imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", Constants.imageFilePath);


        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(AdditonalDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);
            // imgPhoto.setImageResource(R.drawable.noimage);
        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(AdditonalDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }







        if(Constants.str_BloodGp.equalsIgnoreCase(""))
        {

        }
        else{
            //ArrayAdapter myAdap
            spinnerBloodgpArrayAdapter = (ArrayAdapter) spinnerbloodgp.getAdapter(); //cast to an ArrayAdapter

            int bldGroupPosition = spinnerBloodgpArrayAdapter.getPosition(Constants.str_BloodGp);

            //set the default according to value
            spinnerbloodgp.setSelection(bldGroupPosition);

        }

        if(Constants.str_physi_state.equalsIgnoreCase(""))
        {

        }
        else{
            //ArrayAdapter myAdap
            spinnerdisablelistArrayAdapter = (ArrayAdapter) spinnerPhysiChallnge.getAdapter(); //cast to an ArrayAdapter

            int bldGroupPosition = spinnerdisablelistArrayAdapter.getPosition(Constants.str_physi_state);

            //set the default according to value
            spinnerPhysiChallnge.setSelection(bldGroupPosition);

        }





        edtHight.setText(Constants.str_height);
        edtHight.setSelection(edtHight.getText().length());

        edtWeight.setText(Constants.str_weightt);
        edtWeight.setSelection(edtWeight.getText().length());

        edtidentimarks1.setText(Constants.str_idntimark1);
        edtidentimarks1.setSelection(edtidentimarks1.getText().length());

        edtidentimarks2.setText(Constants.str_idntimark2);
        edtidentimarks2.setSelection(edtidentimarks2.getText().length());




        edtemgName.setText(Constants.str_emgName);
        edtemgName.setSelection(edtemgName.getText().length());


        if(Constants.str_emgRelation.equalsIgnoreCase(""))
        {

        }
        else{
            //ArrayAdapter myAdap
            spinnerrelationshiplistArrayAdapter = (ArrayAdapter) spinnerEmgRelasion.getAdapter(); //cast to an ArrayAdapter

            int bldGroupPosition = spinnerrelationshiplistArrayAdapter.getPosition(Constants.str_emgRelation);

            //set the default according to value
            spinnerEmgRelasion.setSelection(bldGroupPosition);

        }





        edtemgEmail.setText(Constants.str_emgEmail);
        edtemgEmail.setSelection(edtemgEmail.getText().length());

        edtemgContc.setText(Constants.str_emgPhone);
        edtemgContc.setSelection(edtemgContc.getText().length());



        edtemgAddress.setText(Constants.str_emgAddress);
        edtemgAddress.setSelection(edtemgAddress.getText().length());





        edtTitle.setText(Constants.str_licenName);
        edtTitle.setSelection(edtTitle.getText().length());

        edtLicnNo.setText(Constants.str_licenNumb);
        edtLicnNo.setSelection(edtLicnNo.getText().length());

        txtStartdate.setText(Utils.parseDateToddMMyyyy(Constants.str_licenstartdate));

        txtEnddate.setText(Utils.parseDateToddMMyyyy(Constants.str_licenenddate));



    }


    @Override
    protected void onResume() {
        super.onResume();

        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(AdditonalDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);

        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(AdditonalDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }


    }



    private void initView() {

        lin = (LinearLayout)findViewById(R.id.act_addit);

        imgBack = (ImageView)findViewById(R.id.backbutton_6);
        imgBack.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_logo_profile6);

        profilepic = (CircleImageView) findViewById(R.id.profile_image6);

        captureImage = (ImageView) findViewById(R.id.captureimage_profile6);
        captureImage.setOnClickListener(this);





        spinnerbloodgp = (Spinner)findViewById(R.id.spinner_employee__blood_grp);

        callBldgp();

        edtWeight = (EditText)findViewById(R.id.edittext_employee_weight);
        edtHight = (EditText)findViewById(R.id.edittext_employee_hight);

        edtidentimarks1 = (EditText)findViewById(R.id.edittext_employee_identification_mark1);
        edtidentimarks2 = (EditText)findViewById(R.id.edittext_employee_identification_mark2);



        spinnerPhysiChallnge = (Spinner)findViewById(R.id.spinner_employee__physi_challange);
        callPhysiChallnge();
        spinnerCriminaloffence = (Spinner)findViewById(R.id.spinner_employee__criminal_offense);
        callCriminaloffen();




        edtemgName = (EditText)findViewById(R.id.edittext_employee_name_emg);



        spinnerEmgRelasion = (Spinner)findViewById(R.id.spinner_employee__relason);
        callEmgRelasion();

        edtemgEmail = (EditText)findViewById(R.id.edittext_employee_email_emg);
        edtemgContc = (EditText)findViewById(R.id.edittext_employee_emgcontact_emg);



       edtemgAddress = (EditText)findViewById(R.id.edittext_employee_adds_emg);




       edtTitle = (EditText)findViewById(R.id.edittext_employee_titleofcerti_license);

       edtLicnNo = (EditText)findViewById(R.id.edittext_employee_license_no);

       txtStartdate = (TextView)findViewById(R.id.textview_employee_startdate_license);
       txtStartdate.setOnClickListener(this);

       txtEnddate = (TextView)findViewById(R.id.textview_employee_enddate_license);
       txtEnddate.setOnClickListener(this);





        btnNext = (Button)findViewById(R.id.next6button);
        btnNext.setOnClickListener(this);





    }




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backbutton_6:
                finish();
                break;
            case R.id.captureimage_profile6:

                PopupWindow popupwindow_obj = popupUpImgDisplay();
                popupwindow_obj.showAsDropDown(v, 0, 0);

                break;
            case R.id.next6button:

               Constants.strWeight_addiDesc = edtWeight.getText().toString().trim();
               Constants.strHight_addiDesc = edtHight.getText().toString().trim();
               Constants.stridMark1_addiDesc = edtidentimarks1.getText().toString().trim();
               Constants.stridMark2_addiDesc = edtidentimarks2.getText().toString().trim();


                Constants.emgName_emgcon = edtemgName.getText().toString().trim();
                Constants.emgEmail_emgcon = edtemgEmail.getText().toString().trim();
                Constants.emgContc_emgcon = edtemgContc.getText().toString().trim();
                Constants.emgAddress_emgcon = edtemgAddress.getText().toString().trim();


                Constants.sttitle_license = edtTitle.getText().toString().trim();
                Constants.stLicnNo_license = edtLicnNo.getText().toString().trim();
                Constants.stStartdate_license = txtStartdate.getText().toString().trim();
                Constants.stEnddate_license = txtEnddate.getText().toString().trim();




                Intent intent = new Intent(AdditonalDtlsActivity.this, ContactDtlsActivity.class);
                startActivity(intent);
                break;

            case R.id.textview_employee_startdate_license:

                cldr = Calendar.getInstance();
                int day2 = cldr.get(Calendar.DAY_OF_MONTH);
                int month2 = cldr.get(Calendar.MONTH);
                int year2 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdditonalDtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtStartdate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year2, month2, day2);
                picker.show();
                break;
            case R.id.textview_employee_enddate_license:

                cldr = Calendar.getInstance();
                int day3 = cldr.get(Calendar.DAY_OF_MONTH);
                int month3 = cldr.get(Calendar.MONTH);
                int year3 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdditonalDtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtEnddate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year3, month3, day3);
                picker.show();
                break;

        }
    }



    private void callBldgp() {

        String[] bloodgps = new String[]{
                "Blood Group",
                "O+",
                "O-",
                "A+",
                "A-",
                "B+",
                "B-",
                "AB+",
                "AB-"
        };

        final List<String> bloodgplist = new ArrayList<>(Arrays.asList(bloodgps));

        // Initializing an ArrayAdapter
        spinnerBloodgpArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, bloodgplist);

        spinnerBloodgpArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerbloodgp.setAdapter(spinnerBloodgpArrayAdapter);
        spinnerbloodgp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.str_Bloodgp_addiDesc = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    private void callPhysiChallnge() {

        String[] disable = new String[]{
                "Disability",
                "No",
                "Yes"

        };

        final List<String> disablelist = new ArrayList<>(Arrays.asList(disable));

        // Initializing an ArrayAdapter
        spinnerdisablelistArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, disablelist);

        spinnerdisablelistArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerPhysiChallnge.setAdapter(spinnerdisablelistArrayAdapter);
        spinnerPhysiChallnge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.strdisablelist_addiDesc = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void callCriminaloffen() {

        String[] disable = new String[]{
                "Criminal Offense",
                "No",
                "Yes"

        };

        final List<String> disablelist = new ArrayList<>(Arrays.asList(disable));

        // Initializing an ArrayAdapter
        spinnerdisablelistArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, disablelist);

        spinnerdisablelistArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerCriminaloffence.setAdapter(spinnerdisablelistArrayAdapter);
        spinnerCriminaloffence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.strcrimeoffence_addiDesc = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void callEmgRelasion() {

        String[] relationship = new String[]{
                "Relationship",
                "Father",
                "Mother",
                "Wife",
                "Relatives",
                "Friends amp; Others"

        };

        final List<String> relationshiplist = new ArrayList<>(Arrays.asList(relationship));

        // Initializing an ArrayAdapter
        spinnerrelationshiplistArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, relationshiplist);

        spinnerrelationshiplistArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerEmgRelasion.setAdapter(spinnerrelationshiplistArrayAdapter);
        spinnerEmgRelasion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.strrelation_emgcon = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }



    public PopupWindow popupUpImgDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) AdditonalDtlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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


    /**
     * Request for camera app to open and capture image.
     * @param isFromGallery-if true then launch gallery app else camera app.
     */
    public void startIntent(boolean isFromGallery) {
        if (!isFromGallery) {
            File imageFile = new File(Constants.imageFilePath);
            //Uri imageFileUri = Uri.fromFile(imageFile); // convert path to Uri

            Uri photoURI = FileProvider.getUriForFile(AdditonalDtlsActivity.this,
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


            File flew = new File(imagePath);
            try {
                bmp = getBitmapFromUri(Uri.fromFile(flew));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }




            profilepic.setImageBitmap(BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath()));

            jsonObject1 = new JSONObject();

            try {
                jsonObject1.put("user_id", userIdd);
                jsonObject1.put("emp_image", stphotoPah);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            callUpdateimage(jsonObject1);
        }
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }




    private void callUpdateimage(JSONObject jsonobj) {


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








}
