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
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HistorydtlsActivity extends AppCompatActivity implements View.OnClickListener {

    DatePickerDialog picker;
    Calendar cldr;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private ImageView imgBack;
    private ImageView imgLogo;
    private CircleImageView profilepic;
    private ImageView captureImage;
    private Button btnNext;
    private EditText edtNatinIdNo;
    private EditText edtPlaceIssue;
    private TextView txtIssueDate;
    private TextView txtExpDate;
    private Spinner spinnerNationality;
    private Spinner spinnerContyofbirth;
    private EditText edtDocType;
    private ArrayAdapter<CountryList> spinnernationalitystatusArrayAdapter;
    private EditText edt_passprt_no;
    private Spinner spinnerNationality_paspt;
    private EditText edtplaceofbirth_passpt;
    private EditText edtissud_by_passpt;
    private TextView txtIssueDate_passpt;
    private TextView txtExpDate_passpt;
    private TextView txtreview_date_passpt;
    private Spinner spinnerEligble_status_passpt;
    private ArrayAdapter<String> spinnerstatuslisttArrayAdapter;
    private Spinner spinnerCurrent_passpt;
    private EditText edtRemarks_passpt;
    private EditText edt_brp_no;
    private Spinner spinnerNationality_visa;
    private Spinner spinnercontyresidence_visa;
    private EditText edtissud_by_visa;
    private TextView txtIssueDate_visa;
    private TextView txtExpDate_visa;
    private TextView txtreview_date_visa;
    private Spinner spinnerEligble_status_visa;
    private Spinner spinnerCurrent_visa;
    private EditText edtRemarks_visa;
    private LinearLayout linUploadHisto;
    private Button btnfromGally;
    private Button btntakephoto;
    private final int REQUEST_CODE_CLICK_IMAGE = 1002, REQUEST_CODE_GALLERY_IMAGE = 1003;
    private File flew;
    private String stphotoPah = "";
    private Bitmap bmp = null;
    int flag = 0;
    private LinearLayout linUploadDocpaspt;
    private LinearLayout linUploadDocVisa;
    private LinearLayout lin;
    private String strProfilepic = "";
    private String userId = "";
    private Button btnfromGallyimg;
    private Button btntakephotoimg;
    private JSONObject jsonObject1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setStatusBarGradiant(HistorydtlsActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_historydtls);

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
                    .with(HistorydtlsActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }


        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(HistorydtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);

        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(HistorydtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }











        edtNatinIdNo.setText(Constants.str_National_ID_No);
        edtNatinIdNo.setSelection(edtNatinIdNo.getText().length());

        edtPlaceIssue.setText(Constants.str_placeissue);
        edtPlaceIssue.setSelection(edtPlaceIssue.getText().length());




        txtIssueDate.setText(Utils.parseDateToddMMyyyy(Constants.str_issueDate));
        txtIssueDate.setOnClickListener(this);


        txtExpDate.setText(Utils.parseDateToddMMyyyy(Constants.str_expDate));
        txtExpDate.setOnClickListener(this);




        for(int i=0; i<Constants.contyLists.size();i++)
        {
            if(Constants.str_national.equalsIgnoreCase(Constants.contyLists.get(i).getName()))
            {



                //set the default according to value
                spinnerNationality.setSelection(i+1);
            }
        }


        for(int i=0; i<Constants.contyLists.size();i++)
        {
            if(Constants.str_country_ofbirth.equalsIgnoreCase(Constants.contyLists.get(i).getName()))
            {



                //set the default according to value
                spinnerContyofbirth.setSelection(i+1);
            }
        }









       for(int i = 0; i<Constants.docLists.size();i++) {
           edtDocType.setText(Constants.docLists.get(i).getType_doc());

       }





        edt_passprt_no.setText(Constants.str_passportNo);
        edt_passprt_no.setSelection(edt_passprt_no.getText().length());



        for(int i=0; i<Constants.contyLists.size();i++)
        {
            if(Constants.str_passpt_nation.equalsIgnoreCase(Constants.contyLists.get(i).getName()))
            {



                //set the default according to value
                spinnerNationality_paspt.setSelection(i+1);
            }
        }









        edtplaceofbirth_passpt.setText(Constants.str_placeofbirth);
        edtplaceofbirth_passpt.setSelection(edtplaceofbirth_passpt.getText().length());


        edtissud_by_passpt.setText(Constants.str_issuedBy_paspt);
        edtissud_by_passpt.setSelection(edtissud_by_passpt.getText().length());






        txtIssueDate_passpt.setText(Utils.parseDateToddMMyyyy(Constants.str_issuedDate_paspt));


        txtExpDate_passpt.setText(Utils.parseDateToddMMyyyy(Constants.str_expDate_paspt));

        txtreview_date_passpt.setText(Utils.parseDateToddMMyyyy(Constants.str_reviewDate_paspt));


        if(Constants.str_eligibleStatus_paspt.equalsIgnoreCase(""))
        {


        }
        else {

            //ArrayAdapter myAdap
            spinnerstatuslisttArrayAdapter = (ArrayAdapter) spinnerEligble_status_passpt.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerstatuslisttArrayAdapter.getPosition(Constants.str_eligibleStatus_paspt);

            //set the default according to value
            spinnerEligble_status_passpt.setSelection(maritalStatPosition);
        }


        if(Constants.str_currentPasport.equalsIgnoreCase(""))
        {


        }
        else {

            //ArrayAdapter myAdap
            spinnerstatuslisttArrayAdapter = (ArrayAdapter) spinnerCurrent_passpt.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerstatuslisttArrayAdapter.getPosition(Constants.str_currentPasport);

            //set the default according to value
            spinnerCurrent_passpt.setSelection(maritalStatPosition);
        }



        edtRemarks_passpt.setText(Constants.str_remarksPasport);
        edtRemarks_passpt.setSelection(edtRemarks_passpt.getText().length());




        edt_brp_no.setText(Constants.str_visaNo);
        edt_brp_no.setSelection(edt_brp_no.getText().length());




        for(int i=0; i<Constants.contyLists.size();i++)
        {
            if(Constants.str_visa_nation.equalsIgnoreCase(Constants.contyLists.get(i).getName()))
            {



                //set the default according to value
                spinnerNationality_visa.setSelection(i+1);
            }
        }




        for(int i=0; i<Constants.contyLists.size();i++)
        {
            if(Constants.str_country_residence.equalsIgnoreCase(Constants.contyLists.get(i).getName()))
            {



                //set the default according to value
                spinnercontyresidence_visa.setSelection(i+1);
            }
        }







        edtissud_by_visa.setText(Constants.str_issuedBy_visa);
        edtissud_by_visa.setSelection(edtissud_by_visa.getText().length());

        txtIssueDate_visa.setText(Utils.parseDateToddMMyyyy(Constants.str_issuedDate_visa));

        txtExpDate_visa.setText(Utils.parseDateToddMMyyyy(Constants.str_expDate_visa));

        txtreview_date_visa.setText(Utils.parseDateToddMMyyyy(Constants.str_reviewDate_visa));



        if(Constants.str_eligibleStatus_visa.equalsIgnoreCase(""))
        {


        }
        else {

            //ArrayAdapter myAdap
            spinnerstatuslisttArrayAdapter = (ArrayAdapter) spinnerEligble_status_visa.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerstatuslisttArrayAdapter.getPosition(Constants.str_eligibleStatus_visa);

            //set the default according to value
            spinnerEligble_status_visa.setSelection(maritalStatPosition);
        }





        if(Constants.str_currentVisa.equalsIgnoreCase(""))
        {


        }
        else {

            //ArrayAdapter myAdap
            spinnerstatuslisttArrayAdapter = (ArrayAdapter) spinnerCurrent_visa.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerstatuslisttArrayAdapter.getPosition(Constants.str_currentVisa);

            //set the default according to value
            spinnerCurrent_visa.setSelection(maritalStatPosition);
        }








        edtRemarks_visa.setText(Constants.str_remarksVisa);
        edtRemarks_visa.setSelection(edtRemarks_visa.getText().length());


        Constants.imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", Constants.imageFilePath);


    }


    @Override
    protected void onResume() {
        super.onResume();

        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(HistorydtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);

        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(HistorydtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }


    }







    private void initView() {

        lin = (LinearLayout)findViewById(R.id.act_service);

        imgBack = (ImageView)findViewById(R.id.backbutton_8);
        imgBack.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_logo_profile8);

        profilepic = (CircleImageView) findViewById(R.id.profile_image8);

        captureImage = (ImageView) findViewById(R.id.captureimage_profile8);
        captureImage.setOnClickListener(this);


        edtNatinIdNo = (EditText)findViewById(R.id.edittext_employee_nati_Id_no);
        edtPlaceIssue = (EditText)findViewById(R.id.edittext_employee_plce_issue);

        txtIssueDate = (TextView)findViewById(R.id.textview_employee_issue_date);
        txtIssueDate.setOnClickListener(this);
        txtExpDate = (TextView)findViewById(R.id.textview_employee_exp_date);
        txtExpDate.setOnClickListener(this);

        spinnerNationality = (Spinner)findViewById(R.id.spinner_employee__nation);
        callNationalityNat(spinnerNationality);

        spinnerContyofbirth = (Spinner)findViewById(R.id.spinner_employee__contyofbirth);
        callContyofbirthNat(spinnerContyofbirth);

        edtDocType = (EditText)findViewById(R.id.edittext_employee_type_of_doc);

        linUploadHisto = (LinearLayout)findViewById(R.id.lin_immigration_history);
        linUploadHisto.setOnClickListener(this);


        edt_passprt_no = (EditText)findViewById(R.id.edittext_employee_passprt_no);
        spinnerNationality_paspt = (Spinner)findViewById(R.id.spinner_employee__nation_passport);
        callNationalityPaspt(spinnerNationality_paspt);

        edtplaceofbirth_passpt = (EditText)findViewById(R.id.edittext_employee_placeofbirth_passpt);

        edtissud_by_passpt = (EditText)findViewById(R.id.edittext_employee_issud_by_passpt);

        txtIssueDate_passpt = (TextView)findViewById(R.id.textview_employee_issue_date_passpt);
        txtIssueDate_passpt.setOnClickListener(this);

        txtExpDate_passpt = (TextView)findViewById(R.id.textview_employee_exp_date_passpt);
        txtExpDate_passpt.setOnClickListener(this);

        txtreview_date_passpt = (TextView)findViewById(R.id.textview_employee_review_date_passpt);
        txtreview_date_passpt.setOnClickListener(this);

        spinnerEligble_status_passpt = (Spinner)findViewById(R.id.spinner_employee__eligble_status_passpt);
        callStatusPasspt(spinnerEligble_status_passpt);


        linUploadDocpaspt = (LinearLayout)findViewById(R.id.lin_uploaddoc_paspt);
        linUploadDocpaspt.setOnClickListener(this);

        spinnerCurrent_passpt = (Spinner)findViewById(R.id.spinner_employee_current_passpt);
        callCurrntPaspt1(spinnerCurrent_passpt);

        edtRemarks_passpt = (EditText)findViewById(R.id.edittext_employee_remarks_passpt);




        edt_brp_no = (EditText)findViewById(R.id.edittext_employee_brp_no);

        spinnerNationality_visa = (Spinner)findViewById(R.id.spinner_employee__nation_visa);

        callNationalityVisa(spinnerNationality_visa);


        spinnercontyresidence_visa = (Spinner)findViewById(R.id.spinner_employee__contyresidence_visa);

        callContyofResidence(spinnercontyresidence_visa);


        edtissud_by_visa = (EditText)findViewById(R.id.edittext_employee_issud_by_visa);

        txtIssueDate_visa = (TextView)findViewById(R.id.textview_employee_issue_date_visa);
        txtIssueDate_visa.setOnClickListener(this);

        txtExpDate_visa = (TextView)findViewById(R.id.textview_employee_exp_date_visa);
        txtExpDate_visa.setOnClickListener(this);

        txtreview_date_visa = (TextView)findViewById(R.id.textview_employee_review_date_visa);
        txtreview_date_visa.setOnClickListener(this);




        spinnerEligble_status_visa = (Spinner)findViewById(R.id.spinner_employee__eligble_status_visa);
        callStatusVisa(spinnerEligble_status_visa);


        spinnerCurrent_visa = (Spinner)findViewById(R.id.spinner_employee_current_visa);
        callCurrntPaspt2(spinnerCurrent_visa);
        edtRemarks_visa = (EditText)findViewById(R.id.edittext_employee_remarks_visa);



        linUploadDocVisa = (LinearLayout)findViewById(R.id.lin_uploaddoc_visa);
        linUploadDocVisa.setOnClickListener(this);






        btnNext = (Button)findViewById(R.id.next8button);
        btnNext.setOnClickListener(this);





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbutton_8:
                finish();
                break;
            case R.id.captureimage_profile8:
                 flag = 5;
                PopupWindow popupwindow_obj = popupUpImgDisplay();
                popupwindow_obj.showAsDropDown(v, 0, 0);

                break;
            case R.id.next8button:

                Constants.id_Nat = edtNatinIdNo.getText().toString().trim();
                Constants.placeIssue_Nat = edtPlaceIssue.getText().toString().trim();
                Constants.issueDate_Nat = txtIssueDate.getText().toString().trim();
                Constants.expDate_Nat = txtExpDate.getText().toString().trim();
                Constants.docType_Nat = edtDocType.getText().toString().trim();

                Constants.passprt_no = edt_passprt_no.getText().toString().trim();
                Constants.placeofbirth_passpt = edtplaceofbirth_passpt.getText().toString().trim();
                Constants.issud_by_passpt = edtissud_by_passpt.getText().toString().trim();
                Constants.issueDate_passpt = txtIssueDate_passpt.getText().toString().trim();
                Constants.expDate_passpt = txtExpDate_passpt.getText().toString().trim();
                Constants.review_date_passpt = txtreview_date_passpt.getText().toString().trim();
                Constants.remarks_passpt = edtRemarks_passpt.getText().toString().trim();


                Constants.brp_noVisa = edt_brp_no.getText().toString().trim();
                Constants.issud_by_visa = edtissud_by_visa.getText().toString().trim();
                Constants.issueDate_visa = txtIssueDate_visa.getText().toString().trim();
                Constants.expDate_visa = txtExpDate_visa.getText().toString().trim();
                Constants.review_date_visa = txtreview_date_visa.getText().toString().trim();
                Constants.remarks__visa = edtRemarks_visa.getText().toString().trim();





                Intent intent = new Intent(HistorydtlsActivity.this, DrivingLicenActivity.class);
                startActivity(intent);
                break;

            case R.id.textview_employee_issue_date:

                cldr = Calendar.getInstance();
                int day2 = cldr.get(Calendar.DAY_OF_MONTH);
                int month2 = cldr.get(Calendar.MONTH);
                int year2 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HistorydtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtIssueDate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year2, month2, day2);
                picker.show();
                break;
            case R.id.textview_employee_exp_date:

                cldr = Calendar.getInstance();
                int day3 = cldr.get(Calendar.DAY_OF_MONTH);
                int month3 = cldr.get(Calendar.MONTH);
                int year3 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HistorydtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtExpDate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year3, month3, day3);
                picker.show();
                break;

            case R.id.textview_employee_issue_date_passpt:

                cldr = Calendar.getInstance();
                int day31 = cldr.get(Calendar.DAY_OF_MONTH);
                int month31 = cldr.get(Calendar.MONTH);
                int year31 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HistorydtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtIssueDate_passpt.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year31, month31, day31);
                picker.show();
                break;

            case R.id.textview_employee_exp_date_passpt:

                cldr = Calendar.getInstance();
                int day32 = cldr.get(Calendar.DAY_OF_MONTH);
                int month32 = cldr.get(Calendar.MONTH);
                int year32 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HistorydtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtExpDate_passpt.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year32, month32, day32);
                picker.show();
                break;

            case R.id.textview_employee_review_date_passpt:

                cldr = Calendar.getInstance();
                int day33 = cldr.get(Calendar.DAY_OF_MONTH);
                int month33 = cldr.get(Calendar.MONTH);
                int year33 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HistorydtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtreview_date_passpt.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year33, month33, day33);
                picker.show();
                break;

            case R.id.textview_employee_issue_date_visa:

                cldr = Calendar.getInstance();
                int day311 = cldr.get(Calendar.DAY_OF_MONTH);
                int month311 = cldr.get(Calendar.MONTH);
                int year311 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HistorydtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtIssueDate_visa.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year311, month311, day311);
                picker.show();
                break;

            case R.id.textview_employee_exp_date_visa:

                cldr = Calendar.getInstance();
                int day322 = cldr.get(Calendar.DAY_OF_MONTH);
                int month322 = cldr.get(Calendar.MONTH);
                int year322 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HistorydtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtExpDate_visa.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year322, month322, day322);
                picker.show();
                break;

            case R.id.textview_employee_review_date_visa:

                cldr = Calendar.getInstance();
                int day333 = cldr.get(Calendar.DAY_OF_MONTH);
                int month333 = cldr.get(Calendar.MONTH);
                int year333 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HistorydtlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtreview_date_visa.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                            }
                        }, year333, month333, day333);
                picker.show();
                break;
            case R.id.lin_immigration_history:
                 flag = 1;
                PopupWindow popupwindow_obj3 = popupDisplay();
                popupwindow_obj3.showAsDropDown(v, 0, 0);
                break;
            case R.id.lin_uploaddoc_paspt:
                flag = 2;
                PopupWindow popupwindow_obj1 = popupDisplay();
                popupwindow_obj1.showAsDropDown(v, 0, 0);
                break;
            case R.id.lin_uploaddoc_visa:
                flag = 3;
                PopupWindow popupwindow_obj2 = popupDisplay();
                popupwindow_obj2.showAsDropDown(v, 0, 0);
                break;

        }
    }


    public PopupWindow popupUpImgDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) HistorydtlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        LayoutInflater inflater = (LayoutInflater) HistorydtlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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








    private void callNationalityNat(Spinner spinnerCountry) {


        // Initializing an ArrayAdapter
        spinnernationalitystatusArrayAdapter  = new ArrayAdapter(HistorydtlsActivity.this,R.layout.maritalstatus_item, Constants.contyLists);
        spinnernationalitystatusArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerCountry.setAdapter(new NothingSelectedSpinnerAdapter(spinnernationalitystatusArrayAdapter, R.layout.info_spinner_row_nothing_counry, HistorydtlsActivity.this));

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                CountryList selectedItemText = (CountryList) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    Constants.country_Nat = selectedItemText.getName();
                    String natiId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});




         }


    private void callNationalityPaspt(Spinner spinnerCountry) {


        // Initializing an ArrayAdapter
        spinnernationalitystatusArrayAdapter  = new ArrayAdapter(HistorydtlsActivity.this,R.layout.maritalstatus_item, Constants.contyLists);
        spinnernationalitystatusArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerCountry.setAdapter(new NothingSelectedSpinnerAdapter(spinnernationalitystatusArrayAdapter, R.layout.info_spinner_row_nothing_counry, HistorydtlsActivity.this));

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                CountryList selectedItemText = (CountryList) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    Constants.country_Paspt = selectedItemText.getName();
                    String natiId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});





    }


    private void callNationalityVisa(Spinner spinnerCountry) {

        // Initializing an ArrayAdapter
        spinnernationalitystatusArrayAdapter  = new ArrayAdapter(HistorydtlsActivity.this,R.layout.maritalstatus_item, Constants.contyLists);
        spinnernationalitystatusArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerCountry.setAdapter(new NothingSelectedSpinnerAdapter(spinnernationalitystatusArrayAdapter, R.layout.info_spinner_row_nothing_counry, HistorydtlsActivity.this));

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                CountryList selectedItemText = (CountryList) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    Constants.country_Visa = selectedItemText.getName();
                    String natiId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});












    }













    private void callContyofbirthNat(Spinner spinnerCountry) {


        // Initializing an ArrayAdapter
        spinnernationalitystatusArrayAdapter  = new ArrayAdapter(HistorydtlsActivity.this,R.layout.maritalstatus_item, Constants.contyLists);
        spinnernationalitystatusArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerCountry.setAdapter(new NothingSelectedSpinnerAdapter(spinnernationalitystatusArrayAdapter, R.layout.info_spinner_row_nothing_counry, HistorydtlsActivity.this));

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                CountryList selectedItemText = (CountryList) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    Constants.countryofbirthNat = selectedItemText.getName();
                    String natiId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});






    }












    private void callStatusPasspt(Spinner spinnerEligble_status_passpt) {

        String[] strStatus = new String[]{
                "Eligible Status",
                "No",
                "Yes"

        };

        final List<String> statuslist = new ArrayList<>(Arrays.asList(strStatus));

        // Initializing an ArrayAdapter
        spinnerstatuslisttArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, statuslist);

        spinnerstatuslisttArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerEligble_status_passpt.setAdapter(spinnerstatuslisttArrayAdapter);
        spinnerEligble_status_passpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.statusPasspt = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    private void callStatusVisa(Spinner spinnerEligble_status_passpt) {

        String[] strStatus = new String[]{
                "Eligible Status",
                "No",
                "Yes"

        };

        final List<String> statuslist = new ArrayList<>(Arrays.asList(strStatus));

        // Initializing an ArrayAdapter
        spinnerstatuslisttArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, statuslist);

        spinnerstatuslisttArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerEligble_status_passpt.setAdapter(spinnerstatuslisttArrayAdapter);
        spinnerEligble_status_passpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.statusVisa = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }









    private void callCurrntPaspt1(Spinner spinnerEligble_status_passpt) {

        String[] strStatus = new String[]{
                "Current Passport",
                "No",
                "Yes"

        };

        final List<String> statuslist = new ArrayList<>(Arrays.asList(strStatus));

        // Initializing an ArrayAdapter
        spinnerstatuslisttArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, statuslist);

        spinnerstatuslisttArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerEligble_status_passpt.setAdapter(spinnerstatuslisttArrayAdapter);
        spinnerEligble_status_passpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.currentpas_paspt = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    private void callCurrntPaspt2(Spinner spinnerEligble_status_passpt) {

        String[] strStatus = new String[]{
                "Current Visa",
                "No",
                "Yes"

        };

        final List<String> statuslist = new ArrayList<>(Arrays.asList(strStatus));

        // Initializing an ArrayAdapter
        spinnerstatuslisttArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.genderstatus_item, statuslist);

        spinnerstatuslisttArrayAdapter.setDropDownViewResource(R.layout.genderstatus_item);
        spinnerEligble_status_passpt.setAdapter(spinnerstatuslisttArrayAdapter);
        spinnerEligble_status_passpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.currentpas_pasptVisa = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }








    private void callContyofResidence(Spinner spinnerCountry) {


        // Initializing an ArrayAdapter
        spinnernationalitystatusArrayAdapter  = new ArrayAdapter(HistorydtlsActivity.this,R.layout.maritalstatus_item, Constants.contyLists);
        spinnernationalitystatusArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerCountry.setAdapter(new NothingSelectedSpinnerAdapter(spinnernationalitystatusArrayAdapter, R.layout.info_spinner_row_nothing_counry, HistorydtlsActivity.this));

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                CountryList selectedItemText = (CountryList) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    Constants.contyresiVisa = selectedItemText.getName();
                    String natiId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});








       /* String[] nationalitystatus = new String[]{
                "Country of Residence",
                "Indian",
                "Spanish",



        };

        final List<String> nationalitystatuslist = new ArrayList<>(Arrays.asList(nationalitystatus));

        // Initializing an ArrayAdapter
        spinnernationalitystatusArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.maritalstatus_item, nationalitystatuslist);

        spinnernationalitystatusArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerCountry.setAdapter(spinnernationalitystatusArrayAdapter);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    Constants.contyresiVisa = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }



    /**
     * Request for camera app to open and capture image.
     * @param isFromGallery-if true then launch gallery app else camera app.
     */
    public void startIntent(boolean isFromGallery) {
        if (!isFromGallery) {
            File imageFile = new File(Constants.imageFilePath);
            //Uri imageFileUri = Uri.fromFile(imageFile); // convert path to Uri


            Uri photoURI = FileProvider.getUriForFile(HistorydtlsActivity.this,
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
                Constants.doc_nat = stphotoPah;
                flag = 0;
            }
            else if(flag == 2)
            {
                Constants.doc_paspt = stphotoPah;
                flag = 0;
            }
            else if(flag == 3)
            {
                Constants.doc_visa = stphotoPah;
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
