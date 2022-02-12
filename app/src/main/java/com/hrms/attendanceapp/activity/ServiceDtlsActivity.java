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
import com.hrms.attendanceapp.adapter.NothingSelectedSpinnerAdapter;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.Authority;
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

public class ServiceDtlsActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE_CLICK_IMAGE = 1002, REQUEST_CODE_GALLERY_IMAGE = 1003;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private LinearLayout lin;
    private ImageView imgBack;
    private ImageView imgLogo;
    private CircleImageView profilepic;
    private ImageView captureImage;
    private Spinner spinnerDept;
    ArrayAdapter<String> spinnerdeptstatusArrayAdapter;
    private Spinner spinnerdesig;
    ArrayAdapter<String> spinnerDesigstatusArrayAdapter;
    private Spinner spinnerEmptype;
    ArrayAdapter<String> spinnerTypestatusArrayAdapter;
    private TextView txtDOJ;
    private TextView txtDOConf;
    private TextView txtStartdate;
    private TextView txtEnddate;
    private EditText edtJobloc;
    private Spinner spinnerReportautho;
    private ArrayAdapter<Authority> spinnerAuthAdapter;
    private Spinner spinnerLeaveautho;
    private Button btnNext;
    DatePickerDialog picker;
    Calendar cldr;
    private String empDept = "";
    private String empDesig = "";
    private String empDatofJoin = "";
    private String empType = "";
    private String empDatofConfirm = "";
    private String empstartDate = "";
    private String empendDate = "";
    private String empjobLoc = "";
    private String empreportingAuth = "";
    private String emplvsancAuth = "";
    private String strProfilepic = "";
    private String userId = "";
    private Button btnfromGallyimg;
    private Button btntakephotoimg;
    private String stphotoPah = "";
    private Bitmap bmp = null;
    private String strImagePath = "";
    private JSONObject jsonObject1;
    private String imurl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(ServiceDtlsActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_service_dtls);

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        Constants.imageFilePath = "";

        initView();
        userId = sh_Pref.getString("USER_CODE", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");
        strProfilepic = sh_Pref.getString("USER_PHOTO", "");

        empDept = getIntent().getStringExtra("EMPDEPT");
        empDesig = getIntent().getStringExtra("EMPDESIG");

        empDatofJoin = getIntent().getStringExtra("EMPDATEOFJOIN");
        empType = getIntent().getStringExtra("EMPSTATUS");

        empDatofConfirm = getIntent().getStringExtra("EMPDATECONFIRM");
        empstartDate = getIntent().getStringExtra("EMPSTARTDATE");

        empendDate = getIntent().getStringExtra("EMPENDDATE");

        empjobLoc = getIntent().getStringExtra("EMPJOBLOC");
        empreportingAuth = getIntent().getStringExtra("EMPREPORTAUTH");
        emplvsancAuth = getIntent().getStringExtra("EMPLVSANCAUTH");

        txtDOJ.setText(Utils.parseDateToddMMyyyy(empDatofJoin));
        txtDOConf.setText(Utils.parseDateToddMMyyyy(empDatofConfirm));


        txtStartdate.setText(Utils.parseDateToddMMyyyy(empstartDate));


        txtEnddate.setText(Utils.parseDateToddMMyyyy(empendDate));


        edtJobloc.setText(empjobLoc);
        edtJobloc.setSelection(edtJobloc.getText().length());


        Constants.imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", Constants.imageFilePath);




        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(ServiceDtlsActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }




        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(ServiceDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);
            // imgPhoto.setImageResource(R.drawable.noimage);
        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(ServiceDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);

            //showImage(imurl);


        }

        if(empDept.equalsIgnoreCase(""))
        {

        }
        else {

            //ArrayAdapter myAdap
            spinnerdeptstatusArrayAdapter = (ArrayAdapter) spinnerDept.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerdeptstatusArrayAdapter.getPosition(empDept);

            //set the default according to value
            spinnerDept.setSelection(maritalStatPosition);
        }




        if(!empDesig.equalsIgnoreCase(""))
        {

            //ArrayAdapter myAdap
            spinnerDesigstatusArrayAdapter = (ArrayAdapter) spinnerdesig.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerDesigstatusArrayAdapter.getPosition(empDesig);

            //set the default according to value
            spinnerdesig.setSelection(maritalStatPosition);
        }



        if(!empType.equalsIgnoreCase(""))
        {

            //ArrayAdapter myAdap
            spinnerTypestatusArrayAdapter = (ArrayAdapter) spinnerEmptype.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerTypestatusArrayAdapter.getPosition(empType);

            //set the default according to value
            spinnerEmptype.setSelection(maritalStatPosition);
        }


     for(int i=0; i<Constants.authorityLists.size();i++)
        {
            if(empreportingAuth.equalsIgnoreCase(Constants.authorityLists.get(i).getName()))
            {



                //set the default according to value
                spinnerReportautho.setSelection(i+1);
            }
        }

        for(int i=0; i<Constants.authorityLists.size();i++)
        {
            if(emplvsancAuth.equalsIgnoreCase(Constants.authorityLists.get(i).getName()))
            {



                //set the default according to value
                spinnerLeaveautho.setSelection(i+1);
            }
        }





    }


    @Override
    protected void onResume() {
        super.onResume();

        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(ServiceDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);

        }
        else {


            imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(ServiceDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }


    }




    private void initView() {

        lin = (LinearLayout)findViewById(R.id.act_service);

        imgBack = (ImageView)findViewById(R.id.backbutton_2);
        imgBack.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_logo_profile2);

        profilepic = (CircleImageView) findViewById(R.id.profile_image2);

        captureImage = (ImageView) findViewById(R.id.captureimage_profile2);
        captureImage.setOnClickListener(this);


        spinnerDept = (Spinner)findViewById(R.id.spinner_employee__department);

        callDept();



        spinnerdesig = (Spinner)findViewById(R.id.spinner_employee__designation);

        callDesig();

        spinnerEmptype = (Spinner)findViewById(R.id.spinner_employee__type);

        callType();




        txtDOJ = (TextView)findViewById(R.id.textview_employee_doj);
        txtDOJ.setOnClickListener(this);
        txtDOConf = (TextView)findViewById(R.id.textview_employee_doconfirm);
        txtDOConf.setOnClickListener(this);

        txtStartdate = (TextView)findViewById(R.id.textview_employee_cont_start_date);
        txtStartdate.setOnClickListener(this);

        txtEnddate = (TextView)findViewById(R.id.textview_employee_cont_end_date);
        txtEnddate.setOnClickListener(this);

        edtJobloc = (EditText)findViewById(R.id.edittext_employee_joblocation);

        spinnerReportautho = (Spinner)findViewById(R.id.spinner_employee__report_autho);
        callReportautho();
        spinnerLeaveautho = (Spinner)findViewById(R.id.spinner_employee__leavesanc_auth);
        callLeaveautho();








        btnNext = (Button)findViewById(R.id.next2button);
        btnNext.setOnClickListener(this);





    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backbutton_2:
                finish();
                break;
            case R.id.captureimage_profile2:
                PopupWindow popupwindow_obj = popupUpImgDisplay();
                popupwindow_obj.showAsDropDown(v, 0, 0);

                break;
            case R.id.next2button:

                Constants.strdateojn = txtDOJ.getText().toString().trim();
                Constants.strDOConf = txtDOConf.getText().toString().trim();
                Constants.strStartdateServ = txtStartdate.getText().toString().trim();
                Constants.strEnddateServ = txtEnddate.getText().toString().trim();
                Constants.strJobLocServ = edtJobloc.getText().toString().trim();



                Intent intent = new Intent(ServiceDtlsActivity.this, EducationDtlsActivity.class);
                startActivity(intent);
                break;
            case R.id.textview_employee_doj:
                cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ServiceDtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtDOJ.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year, month, day);
                picker.show();
                break;
            case R.id.textview_employee_doconfirm:

                cldr = Calendar.getInstance();
                int day1 = cldr.get(Calendar.DAY_OF_MONTH);
                int month1 = cldr.get(Calendar.MONTH);
                int year1 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ServiceDtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtDOConf.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year1, month1, day1);
                picker.show();
                break;
            case R.id.textview_employee_cont_start_date:

                cldr = Calendar.getInstance();
                int day2 = cldr.get(Calendar.DAY_OF_MONTH);
                int month2 = cldr.get(Calendar.MONTH);
                int year2 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ServiceDtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtStartdate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year2, month2, day2);
                picker.show();
                break;
            case R.id.textview_employee_cont_end_date:

                 cldr = Calendar.getInstance();
                int day3 = cldr.get(Calendar.DAY_OF_MONTH);
                int month3 = cldr.get(Calendar.MONTH);
                int year3 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ServiceDtlsActivity.this,
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





    private void callDept() {

        String[] deptStatus = new String[]{
                "Department",
                "TRAINING",
                "HUMAN RESOUCE",
                "MARKETING amp; SALES",
                "FINANCE",
                "PURCHASE"


        };

        final List<String> deptstatuslist = new ArrayList<>(Arrays.asList(deptStatus));

        // Initializing an ArrayAdapter
        spinnerdeptstatusArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, deptstatuslist);

        spinnerdeptstatusArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerDept.setAdapter(spinnerdeptstatusArrayAdapter);
        spinnerDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.strDeptstatus = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void callDesig() {

        String[] desigStatus = new String[]{
                "Designation",
                "ASSISTANT DIRECTOR-II",
                "MANAGER"



        };

        final List<String> desigstatuslist = new ArrayList<>(Arrays.asList(desigStatus));

        // Initializing an ArrayAdapter
        spinnerDesigstatusArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, desigstatuslist);

        spinnerDesigstatusArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerdesig.setAdapter(spinnerDesigstatusArrayAdapter);
        spinnerdesig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.strDesigstatus = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void callType() {

        String[] typeStatus = new String[]{
                "Employment Type",
                "REGULAR",
                "FULL TIMER",
                "CONTRACTUAL"



        };

        final List<String> typestatuslist = new ArrayList<>(Arrays.asList(typeStatus));

        // Initializing an ArrayAdapter
        spinnerTypestatusArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, typestatuslist);

        spinnerTypestatusArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerEmptype.setAdapter(spinnerTypestatusArrayAdapter);
        spinnerEmptype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.strEmptype = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void callReportautho() {



        spinnerAuthAdapter = new ArrayAdapter(ServiceDtlsActivity.this,R.layout.simple_spinner_item1, Constants.authorityLists);
        spinnerAuthAdapter.setDropDownViewResource(R.layout.simple_spinner_item1);

        spinnerReportautho.setAdapter(new NothingSelectedSpinnerAdapter(spinnerAuthAdapter, R.layout.info_spinner_row_nothing_selected1, ServiceDtlsActivity.this));
        //simple_spinner_item
        spinnerReportautho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                Authority selectedItemText = (Authority) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    String authname = selectedItemText.getName();
                    String authId = selectedItemText.getId();

                    Constants.reportingAuth = authname;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});


    }



    private void callLeaveautho() {



        spinnerAuthAdapter = new ArrayAdapter(ServiceDtlsActivity.this,R.layout.simple_spinner_item2, Constants.authorityLists);
        spinnerAuthAdapter.setDropDownViewResource(R.layout.simple_spinner_item2);

        spinnerLeaveautho.setAdapter(new NothingSelectedSpinnerAdapter(spinnerAuthAdapter, R.layout.info_spinner_row_nothing_selected2, ServiceDtlsActivity.this));
        //simple_spinner_item
        spinnerLeaveautho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                Authority selectedItemText = (Authority) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    String authname_ = selectedItemText.getName();
                    String authId_ = selectedItemText.getId();

                    Constants.leaveAuth = authname_;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});


    }




    public PopupWindow popupUpImgDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) ServiceDtlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

            Uri photoURI = FileProvider.getUriForFile(ServiceDtlsActivity.this,
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
                jsonObject1.put("user_id", userId);
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
