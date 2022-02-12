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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.BuildConfig;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.DocList;
import com.hrms.attendanceapp.getset.EducationList;
import com.hrms.attendanceapp.getset.JobList;
import com.hrms.attendanceapp.getset.TrainningList;
import com.hrms.attendanceapp.utils.CommonUtils;
import com.hrms.attendanceapp.utils.CustomProgress;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DrivingLicenActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE_CLICK_IMAGE = 1002, REQUEST_CODE_GALLERY_IMAGE = 1003;
    DatePickerDialog picker;
    Calendar cldr;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private ImageView imgLogo;
    private CircleImageView profilepic;
    private ImageView captureImage;
    private Button btnNext;
    private EditText edtTypeOfLisence;
    private EditText edtLicnNo;
    private TextView txtExpryDate;
    private LinearLayout lin;
    private String userId;
    private String strProfilepic;
    private JSONObject jsonObjectimgupl;
    private Bitmap bmp = null;
    private String stphotoPah = "";
    private Button btnfromGallyimg;
    private Button btntakephotoimg;

    JSONObject jsonObject1;
    JSONArray jsonArrayedu;
    private JSONObject jsonObjectpd;
    private JSONArray jsonArrayjob;
    private JSONArray jsonArraytrain;
    private JSONArray jsonArraydoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(DrivingLicenActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_driving_licen);

        Constants.imageFilePath = "";
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();


        initView();

        userId = sh_Pref.getString("USER_CODE", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");
        strProfilepic = sh_Pref.getString("USER_PHOTO", "");

        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(DrivingLicenActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }
        Constants.imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", Constants.imageFilePath);


        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(DrivingLicenActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);
            // imgPhoto.setImageResource(R.drawable.noimage);
        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(DrivingLicenActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }






        edtTypeOfLisence.setText(Constants.str_type_of_License);
        edtTypeOfLisence.setSelection(edtTypeOfLisence.getText().length());

        edtLicnNo.setText(Constants.str_LicenceNo);
        edtLicnNo.setSelection(edtLicnNo.getText().length());

        txtExpryDate.setText(Utils.parseDateToddMMyyyy(Constants.str_lisencExperyDate));


    }

    @Override
    protected void onResume() {
        super.onResume();

        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(DrivingLicenActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);

        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(DrivingLicenActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }


    }







    private void initView() {

        lin = (LinearLayout)findViewById(R.id.act_drivlic);

        imgBack = (ImageView)findViewById(R.id.backbutton_9);
        imgBack.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_logo_profile9);

        profilepic = (CircleImageView) findViewById(R.id.profile_image9);

        captureImage = (ImageView) findViewById(R.id.captureimage_profile9);
        captureImage.setOnClickListener(this);







        edtTypeOfLisence = (EditText)findViewById(R.id.edittext_employee_type_of_licen);
        edtLicnNo = (EditText)findViewById(R.id.edittext_employee_licn_no);

        txtExpryDate = (TextView)findViewById(R.id.textview_employee_expiry_date);
        txtExpryDate.setOnClickListener(this);

        btnNext = (Button)findViewById(R.id.button_sibmit);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backbutton_9:
                finish();
                break;
            case R.id.captureimage_profile9:
                PopupWindow popupwindow_obj = popupUpImgDisplay();
                popupwindow_obj.showAsDropDown(v, 0, 0);

                break;
            case R.id.button_sibmit:

                Constants.typeOfLisenceDriv = edtTypeOfLisence.getText().toString().trim();
                Constants.licnNoDriv = edtLicnNo.getText().toString().trim();
                Constants.expryDateDriv = txtExpryDate.getText().toString().trim();


                jsonObject1 = new JSONObject();
                jsonArrayedu = new JSONArray();
                jsonArrayjob = new JSONArray();
                jsonArraytrain = new JSONArray();
                jsonArraydoc = new JSONArray();


                try {
                    jsonObject1.put("user_id", userId);

                    JSONObject jsonbjecpd = new JSONObject();
                    jsonbjecpd.put("emp_code", Constants.strEmpCode);
                    jsonbjecpd.put("emp_fname",  Constants.strFname);
                    jsonbjecpd.put("emp_mid_name", Constants.strMname);
                    jsonbjecpd.put("emp_lname",  Constants.strLname);
                    jsonbjecpd.put("emp_gender", Constants.strGenderstatus_);
                    jsonbjecpd.put("ni_no",  Constants.strNlno);
                    jsonbjecpd.put("emp_dob", Utils.parseDateToyyyyMMdd(Constants.strDob));
                    jsonbjecpd.put("marital_status", Constants.strMaritalstatus_);
                    jsonbjecpd.put("nationality", Constants.strNationality_);
                    jsonbjecpd.put("emp_ps_email", Constants.strEmailid);
                    jsonbjecpd.put("emp_ps_phone", Constants.strcontact_);
                    jsonbjecpd.put("em_contact", Constants.stremgcontact_);

                    jsonObject1.put("personal_details", jsonbjecpd);





                    JSONObject jsonbjecservdt = new JSONObject();
                    jsonbjecservdt.put("emp_department", Constants.strDeptstatus);
                    jsonbjecservdt.put("emp_designation",  Constants.strDesigstatus);
                    jsonbjecservdt.put("emp_doj", Utils.parseDateToyyyyMMdd(Constants.strdateojn));
                    jsonbjecservdt.put("emp_status", Constants.strEmptype);
                    jsonbjecservdt.put("date_confirm", Utils.parseDateToyyyyMMdd(Constants.strDOConf));
                    jsonbjecservdt.put("start_date",   Utils.parseDateToyyyyMMdd(Constants.strStartdateServ));
                    jsonbjecservdt.put("end_date", Utils.parseDateToyyyyMMdd(Constants.strEnddateServ));
                    jsonbjecservdt.put("job_loc", Constants.strJobLocServ);
                    jsonbjecservdt.put("emp_reporting_auth", Constants.reportingAuth);
                    jsonbjecservdt.put("emp_lv_sanc_auth", Constants.leaveAuth);
                    jsonbjecservdt.put("emp_image", "");


                    jsonObject1.put("service_details", jsonbjecservdt);

                    for (EducationList edu1 : Constants.eduListResult)
                    {



                        JSONObject jsonObjectedu = new JSONObject();
                        jsonObjectedu.put("id", edu1.getId());
                        jsonObjectedu.put("quli", edu1.getQulif());
                        jsonObjectedu.put("dis", edu1.getSubject());
                        jsonObjectedu.put("ins_nmae", edu1.getInsName());
                        jsonObjectedu.put("board", edu1.getBoard());
                        jsonObjectedu.put("year_passing", edu1.getPassyear());
                        jsonObjectedu.put("perce", edu1.getPersentage());
                        jsonObjectedu.put("grade", edu1.getGrade());
                        jsonObjectedu.put("doc", edu1.getDoc1());
                        jsonObjectedu.put("doc2", edu1.getDoc2());



                        jsonArrayedu.put(jsonObjectedu);


                    }

                    jsonObject1.put("educational_details", jsonArrayedu);


                    for(JobList job1 : Constants.jobListResult)
                    {
                        JSONObject jsonObjectjob = new JSONObject();
                        jsonObjectjob.put("job_name", job1.getName());
                        jsonObjectjob.put("job_start_date", Utils.parseDateToyyyyMMdd(job1.getStatdate()));
                        jsonObjectjob.put("job_end_date", Utils.parseDateToyyyyMMdd(job1.getStatdate()));
                        jsonObjectjob.put("des", job1.getDesc());
                        jsonObjectjob.put("exp", job1.getExp());
                        jsonObjectjob.put("emp_id", Constants.empId_job);
                        jsonObjectjob.put("emid", Constants.emId_job);



                        jsonArrayjob.put(jsonObjectjob);
                    }

                    jsonObject1.put("job_details", jsonArrayjob);


                    for(TrainningList trainningList1 : Constants.trainListResult)
                    {
                        JSONObject jsonObjecttrain = new JSONObject();
                        jsonObjecttrain.put("tarin_name", trainningList1.getName());
                        jsonObjecttrain.put("tarin_start_date", Utils.parseDateToyyyyMMdd(trainningList1.getStatdate()));
                        jsonObjecttrain.put("tarin_end_date", Utils.parseDateToyyyyMMdd(trainningList1.getStatdate()));
                        jsonObjecttrain.put("train_des", trainningList1.getDesc());
                        jsonObjecttrain.put("emp_id", Constants.empId_traing);
                        jsonObjecttrain.put("emid", Constants.emId_traing);


                        jsonArraytrain.put(jsonObjecttrain);
                    }

                    jsonObject1.put("training_details", jsonArraytrain);





                    JSONObject jsonbjecad = new JSONObject();
                    jsonbjecad.put("emp_blood_grp", Constants.str_Bloodgp_addiDesc);
                    jsonbjecad.put("emp_weight",  Constants.strWeight_addiDesc);
                    jsonbjecad.put("emp_height", Constants.strHight_addiDesc);
                    jsonbjecad.put("emp_identification_mark_one", Constants.stridMark1_addiDesc);
                    jsonbjecad.put("emp_identification_mark_two", Constants.stridMark2_addiDesc);
                    jsonbjecad.put("emp_physical_status", Constants.strdisablelist_addiDesc);
                    jsonbjecad.put("criminal", Constants.strcrimeoffence_addiDesc);


                    jsonObject1.put("additional_details", jsonbjecad);





                    JSONObject jsonbjecemg = new JSONObject();
                    jsonbjecemg.put("em_name", Constants.str_Bloodgp_addiDesc);
                    jsonbjecemg.put("em_relation",  Constants.strWeight_addiDesc);
                    jsonbjecemg.put("em_email", Constants.strHight_addiDesc);
                    jsonbjecemg.put("em_phone", Constants.stridMark1_addiDesc);
                    jsonbjecemg.put("em_address", Constants.stridMark2_addiDesc);


                    jsonObject1.put("emg_details", jsonbjecemg);




                    JSONObject jsonbjeccer = new JSONObject();
                    jsonbjeccer.put("titleof_license", Constants.sttitle_license);
                    jsonbjeccer.put("cf_license_number",  Constants.stLicnNo_license);
                    jsonbjeccer.put("cf_start_date", Utils.parseDateToyyyyMMdd(Constants.stStartdate_license));
                    jsonbjeccer.put("cf_end_date", Utils.parseDateToyyyyMMdd(Constants.stEnddate_license));

                    jsonObject1.put("cer_membership", jsonbjeccer);




                    JSONObject jsonbjectper = new JSONObject();
                    jsonbjectper.put("emp_pr_address1", Constants.adds1_pr);
                    jsonbjectper.put("emp_pr_address2",  Constants.adds2_pr);
                    jsonbjectper.put("emp_pr_roadname", Constants.roadname_pr);
                    jsonbjectper.put("emp_pr_city", Constants.city_pr);
                    jsonbjectper.put("emp_pr_pincode",  Constants.Zipcode_pr);
                    jsonbjectper.put("emp_pr_country", Constants.country_pr);
                    jsonbjectper.put("pr_add_proof", Constants.doc_pr);

                    jsonObject1.put("permanent_add", jsonbjectper);


                    JSONObject jsonbjectps = new JSONObject();
                    jsonbjectps.put("emp_ps_address1", Constants.adds1_ps);
                    jsonbjectps.put("emp_ps_address2",  Constants.adds2_ps);
                    jsonbjectps.put("emp_ps_roadname", Constants.roadname_ps);
                    jsonbjectps.put("emp_ps_city", Constants.city_ps);
                    jsonbjectps.put("emp_ps_pincode",  Constants.Zipcode_ps);
                    jsonbjectps.put("emp_ps_country", Constants.country_ps);
                    jsonbjectps.put("ps_add_proof", Constants.doc_ps);

                    jsonObject1.put("present_add", jsonbjectps);



                    JSONObject jsonbjectimdt = new JSONObject();
                    jsonbjectimdt.put("nat_id", Constants.id_Nat);
                    jsonbjectimdt.put("place_iss",  Constants.placeIssue_Nat);
                    jsonbjectimdt.put("iss_date", Utils.parseDateToyyyyMMdd(Constants.issueDate_Nat));
                    jsonbjectimdt.put("exp_date", Utils.parseDateToyyyyMMdd(Constants.expDate_Nat));
                    jsonbjectimdt.put("pass_nation",  Constants.country_Nat);
                    jsonbjectimdt.put("country_birth", Constants.countryofbirthNat);

                    for(DocList doclt : Constants.docLists) {
                        JSONObject jsonobj_ = new JSONObject();
                        jsonobj_.put("id", doclt.getId());
                        jsonobj_.put("docu_nat",  Constants.doc_nat);
                        jsonobj_.put("type_doc",  Constants.docType_Nat);


                        jsonArraydoc.put(jsonobj_);
                    }

                    jsonbjectimdt.put("type_of_doc", jsonArraydoc);
                    jsonObject1.put("immigration_det", jsonbjectimdt);




                    JSONObject jsonbjectpaspot = new JSONObject();
                    jsonbjectpaspot.put("pass_doc_no", Constants.passprt_no);
                    jsonbjectpaspot.put("pass_nat",  Constants.country_Paspt);
                    jsonbjectpaspot.put("place_birth", Constants.placeofbirth_passpt);
                    jsonbjectpaspot.put("issue_by", Constants.issud_by_passpt);
                    jsonbjectpaspot.put("pas_iss_date",  Utils.parseDateToyyyyMMdd(Constants.issueDate_passpt));
                    jsonbjectpaspot.put("pass_exp_date", Utils.parseDateToyyyyMMdd(Constants.expDate_passpt));
                    jsonbjectpaspot.put("pass_review_date", Utils.parseDateToyyyyMMdd(Constants.review_date_passpt));
                    jsonbjectpaspot.put("eli_status", Constants.statusPasspt);
                    jsonbjectpaspot.put("cur_pass", Constants.currentpas_paspt);
                    jsonbjectpaspot.put("remarks", Constants.remarks_passpt);
                    jsonbjectpaspot.put("pass_docu",  Constants.doc_paspt);

                    jsonObject1.put("passport_details", jsonbjectpaspot);



                    JSONObject jsonbjectvisa = new JSONObject();
                    jsonbjectvisa.put("visa_doc_no", Constants.brp_noVisa);
                    jsonbjectvisa.put("visa_nat",  Constants.country_Visa);
                    jsonbjectvisa.put("country_residence", Constants.contyresiVisa);
                    jsonbjectvisa.put("visa_issue", Constants.issud_by_visa);
                    jsonbjectvisa.put("visa_issue_date",  Utils.parseDateToyyyyMMdd(Constants.issueDate_visa));
                    jsonbjectvisa.put("visa_exp_date", Utils.parseDateToyyyyMMdd(Constants.expDate_visa));
                    jsonbjectvisa.put("visa_review_date", Utils.parseDateToyyyyMMdd(Constants.review_date_visa));
                    jsonbjectvisa.put("visa_eli_status", Constants.statusVisa);
                    jsonbjectvisa.put("visa_cur", Constants.currentpas_pasptVisa);
                    jsonbjectvisa.put("visa_remarks", Constants.remarks__visa);
                    jsonbjectvisa.put("visa_upload_doc",  Constants.doc_visa);

                    jsonObject1.put("visa_details", jsonbjectvisa);








                    JSONObject jsonbjectdvlic = new JSONObject();
                    jsonbjectdvlic.put("drive_doc", Constants.typeOfLisenceDriv);
                    jsonbjectdvlic.put("licen_num",  Constants.licnNoDriv);
                    jsonbjectdvlic.put("lin_exp_date",  Utils.parseDateToyyyyMMdd(Constants.expryDateDriv));


                    jsonObject1.put("driving_details", jsonbjectdvlic);



                    callSaveprofile(jsonObject1);



                } catch (JSONException e) {
                    e.printStackTrace();
                }



                break;

            case R.id.textview_employee_expiry_date:

                cldr = Calendar.getInstance();
                int day2 = cldr.get(Calendar.DAY_OF_MONTH);
                int month2 = cldr.get(Calendar.MONTH);
                int year2 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(DrivingLicenActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtExpryDate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year2, month2, day2);
                picker.show();
                break;
        }
    }

    public PopupWindow popupUpImgDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) DrivingLicenActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

            Uri photoURI = FileProvider.getUriForFile(DrivingLicenActivity.this,
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

            jsonObjectimgupl = new JSONObject();

            try {
                jsonObjectimgupl.put("user_id", userId);
                jsonObjectimgupl.put("emp_image", stphotoPah);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            callUpdateimage(jsonObjectimgupl);
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


    private void callSaveprofile(JSONObject consumers) {

        final CustomProgress customProgress = CustomProgress.getInstance();

        // now you have the instance of CustomProgres
        // for showing the ProgressBar
        String messag = "Loading. Please wait...";
        customProgress.showProgress(DrivingLicenActivity.this, messag, false);


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


        Call<String> call = service.getSaveOrder(consumers.toString());


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

                            String status = jsonobj.getString("resultstatus");
                            if(status.equalsIgnoreCase("true"))
                            {
                                String mess = jsonobj.getString("msg");

                                Toast.makeText(DrivingLicenActivity.this, mess, Toast.LENGTH_LONG).show();

                                Intent intetn = new Intent(DrivingLicenActivity.this, MainActivity.class);
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

























    private void sharedPrefernces(String strprofilephoto) {
        // TODO Auto-generated method stub
        toEdit.putString("USER_PHOTO", strprofilephoto);


        toEdit.commit();
    }

}
