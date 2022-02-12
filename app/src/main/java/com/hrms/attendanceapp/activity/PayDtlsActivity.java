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
import android.widget.CheckBox;
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
import com.hrms.attendanceapp.getset.BankList;
import com.hrms.attendanceapp.getset.PayGroup;
import com.hrms.attendanceapp.getset.PayType;
import com.hrms.attendanceapp.getset.TaxCode;
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

public class PayDtlsActivity extends AppCompatActivity implements View.OnClickListener {

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
    private Spinner spinnerPay_group;
    private Spinner spinnerAnnualPay;
    private ArrayAdapter<String> spinnerAnnulpayArrayAdapter;
    private String str_AnnualPay = "";
    private Spinner spinnerPayType;
    private TextView txtDailyWages;
    private TextView txtworkHours;
    private TextView txtRates;
    private Spinner spinnerTaxCode;
    private TextView txtRefer;
    private TextView txtpersentage;
    private Spinner spinnerPayMode;
    private ArrayAdapter<String> spinnerpaymodeArrayAdapter;
    private String str_PayMode = "";
    private Spinner spinnerBankName;
    private EditText edtBranchName;
    private EditText edtAccunNo;
    private TextView txtSortCode;
    private Spinner spinnerPayCurrency;
    private ArrayAdapter<String> spinnerpayCurreArrayAdapter;
    private String str_PayCurrency = "";
    private ArrayAdapter<PayGroup> spinnerGradeAdapter;
    private ArrayAdapter<PayType> spinnerPayAdapter;
    private ArrayAdapter<TaxCode> spinnerTaxcAdapter;
    private ArrayAdapter<BankList> spinnerBankAdapter;
    private String stPaygrpname = "";
    private String stPaygrpId = "";
    private String stPayTypename = "";
    private String stPayTypeId = "";
    private String stTaxcodename = "";
    private String stTaxcodeId = "";
    private String stBankname = "";
    private String stBankId = "";
    private String stBranchName = "";
    private String stAccunNo = "";
    private LinearLayout lin;
    private String userId = "";
    private String strProfilepic = "";
    private JSONObject jsonObject1;
    private Bitmap bmp = null;
    private String stphotoPah = "";
    private Button btnfromGallyimg;
    private Button btntakephotoimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(PayDtlsActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay_dtls);

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
                    .with(PayDtlsActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }

        Constants.imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", Constants.imageFilePath);


        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(PayDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);
            // imgPhoto.setImageResource(R.drawable.noimage);
        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(PayDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }





        txtDailyWages.setText(Constants.str_daily);
        txtworkHours.setText(Constants.str_minwork);
        txtRates.setText(Constants.str_minrate);

        txtRefer.setText(Constants.str_taxref);

        txtpersentage.setText(Constants.str_taxperscn);




        edtBranchName.setText(Constants.str_branchId);
        edtBranchName.setSelection(edtBranchName.getText().length());
        edtAccunNo.setText(Constants.str_empAccountNo);
        edtAccunNo.setSelection(edtAccunNo.getText().length());

        txtSortCode.setText(Constants.str_empSortCode);

        if(!Constants.str_currency.equalsIgnoreCase(""))
        {

            //ArrayAdapter myAdap
            spinnerpayCurreArrayAdapter = (ArrayAdapter) spinnerPayCurrency.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerpayCurreArrayAdapter.getPosition(Constants.str_currency);

            //set the default according to value
            spinnerPayCurrency.setSelection(maritalStatPosition);
        }





        for(int i=0; i<Constants.bankLists.size();i++)
        {
            if(Constants.str_bankName.equalsIgnoreCase(Constants.bankLists.get(i).getId()))
            {



                //set the default according to value
                spinnerBankName.setSelection(i+1);
            }
        }


        for(int i=0; i<Constants.taxcodeLists.size();i++)
        {
            if(Constants.str_taxemp.equalsIgnoreCase(Constants.taxcodeLists.get(i).getId()))
            {



                //set the default according to value
                spinnerTaxCode.setSelection(i+1);
            }
        }


        for(int i=0; i<Constants.gradeLists.size();i++)
        {
            if(Constants.str_empGroupname.equalsIgnoreCase(Constants.gradeLists.get(i).getId()))
            {



                //set the default according to value
                spinnerPay_group.setSelection(i+1);
            }
        }

        if(!Constants.str_empPayScale.equalsIgnoreCase(""))
        {

            //ArrayAdapter myAdap
            spinnerAnnulpayArrayAdapter = (ArrayAdapter) spinnerAnnualPay.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerAnnulpayArrayAdapter.getPosition(Constants.str_empPayScale);

            //set the default according to value
            spinnerAnnualPay.setSelection(maritalStatPosition);
        }

        for(int i=0; i<Constants.paytypeLists.size(); i++)
        {
            if(Constants.str_paymenttype.equalsIgnoreCase(Constants.paytypeLists.get(i).getId()))
            {



                //set the default according to value
                spinnerPayType.setSelection(i+1);
            }
        }


        if(!Constants.str_payMode.equalsIgnoreCase(""))
        {

            //ArrayAdapter myAdap
            spinnerpaymodeArrayAdapter = (ArrayAdapter) spinnerPayMode.getAdapter(); //cast to an ArrayAdapter

            int maritalStatPosition = spinnerpaymodeArrayAdapter.getPosition(Constants.str_payMode);

            //set the default according to value
            spinnerPayMode.setSelection(maritalStatPosition);
        }





    }



    @Override
    protected void onResume() {
        super.onResume();

        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(PayDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);

        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(PayDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }


    }

    private void initView() {
        lin = (LinearLayout)findViewById(R.id.act_service);

        imgBack = (ImageView)findViewById(R.id.backbutton_10);
        imgBack.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_logo_profile10);

        profilepic = (CircleImageView) findViewById(R.id.profile_image10);

        captureImage = (ImageView) findViewById(R.id.captureimage_profile10);
        captureImage.setOnClickListener(this);



        spinnerPay_group = (Spinner)findViewById(R.id.spinner_employee__pay_group);

        callPayGroup(spinnerPay_group);

        spinnerAnnualPay = (Spinner)findViewById(R.id.spinner_employee_annual_pay);

        callAnnulpay(spinnerAnnualPay);

        spinnerPayType = (Spinner)findViewById(R.id.spinner_employee_payment_type);

        callPayType(spinnerPayType);



        txtDailyWages = (TextView)findViewById(R.id.textview_employee_daily_wages);

        txtworkHours = (TextView)findViewById(R.id.textview_employee_work_hours);

        txtRates = (TextView)findViewById(R.id.textview_employee_rates);

        spinnerTaxCode = (Spinner)findViewById(R.id.spinner_employee_tax_code);

        callTaxe(spinnerTaxCode);



        txtRefer = (TextView)findViewById(R.id.textview_employee_tax_refer);

        txtpersentage = (TextView)findViewById(R.id.textview_employee_tax_persentage);


        spinnerPayMode = (Spinner)findViewById(R.id.spinner_employee_pay_mode);
        callPayMode(spinnerPayMode);

        spinnerBankName = (Spinner)findViewById(R.id.spinner_employee_bank_name);

        callBank(spinnerBankName);

       edtBranchName = (EditText)findViewById(R.id.edittext_employee_branch_name);
       edtAccunNo = (EditText)findViewById(R.id.edittext_employee_accun_no);

       txtSortCode = (TextView)findViewById(R.id.textview_employee_sort_code);

        spinnerPayCurrency = (Spinner)findViewById(R.id.spinner_employee_pay_currenc);

        callPayCurrency(spinnerPayCurrency);


        btnNext = (Button)findViewById(R.id.next10button);
        btnNext.setOnClickListener(this);



    }

    private void callBank(Spinner spinnerBankName) {

        spinnerBankAdapter = new ArrayAdapter(PayDtlsActivity.this,R.layout.simple_spinner_bank, Constants.bankLists);
        spinnerBankAdapter.setDropDownViewResource(R.layout.simple_spinner_tax);

        spinnerBankName.setAdapter(new NothingSelectedSpinnerAdapter(spinnerBankAdapter, R.layout.info_spinner_row_nothing_bank, PayDtlsActivity.this));

        spinnerBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                BankList selectedItemText = (BankList) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    stBankname = selectedItemText.getName();
                    stBankId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});




    }

    private void callTaxe(Spinner spinnerTaxCode) {

        spinnerTaxcAdapter = new ArrayAdapter(PayDtlsActivity.this,R.layout.simple_spinner_type, Constants.taxcodeLists);
        spinnerTaxcAdapter.setDropDownViewResource(R.layout.simple_spinner_tax);

        spinnerTaxCode.setAdapter(new NothingSelectedSpinnerAdapter(spinnerTaxcAdapter, R.layout.info_spinner_row_nothing_tax, PayDtlsActivity.this));

        spinnerTaxCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                TaxCode selectedItemText = (TaxCode) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    stTaxcodename = selectedItemText.getTaxcode();
                    stTaxcodeId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});


    }

    private void callPayType(Spinner spinnerPayType) {

        spinnerPayAdapter = new ArrayAdapter(PayDtlsActivity.this,R.layout.simple_spinner_type, Constants.paytypeLists);
        spinnerPayAdapter.setDropDownViewResource(R.layout.simple_spinner_grade);

        spinnerPayType.setAdapter(new NothingSelectedSpinnerAdapter(spinnerPayAdapter, R.layout.info_spinner_row_nothing_type, PayDtlsActivity.this));
        //simple_spinner_item
        spinnerPayType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                PayType selectedItemText = (PayType) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    stPayTypename = selectedItemText.getName();
                    stPayTypeId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});


    }

    private void callPayGroup(Spinner spinnerPay_group) {

        spinnerGradeAdapter = new ArrayAdapter(PayDtlsActivity.this,R.layout.simple_spinner_grade, Constants.gradeLists);
        spinnerGradeAdapter.setDropDownViewResource(R.layout.simple_spinner_grade);

        spinnerPay_group.setAdapter(new NothingSelectedSpinnerAdapter(spinnerGradeAdapter, R.layout.info_spinner_row_nothing_grade, PayDtlsActivity.this));
        //simple_spinner_item
        spinnerPay_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                PayGroup selectedItemText = (PayGroup) parent.getItemAtPosition(position);
                if(selectedItemText!=null){
                    stPaygrpname = selectedItemText.getName();
                    stPaygrpId = selectedItemText.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }});



    }

    private void callPayCurrency(Spinner spinnerPayCurrency) {

        String[] payCurr = new String[]{
                "Payment Currency",
                "USD",
                "AUD",
                "EURO",
                "GBP",
                "INR"




        };

        final List<String> payCurrlist = new ArrayList<>(Arrays.asList(payCurr));

        // Initializing an ArrayAdapter
        spinnerpayCurreArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.maritalstatus_item, payCurrlist);

        spinnerpayCurreArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerPayCurrency.setAdapter(spinnerpayCurreArrayAdapter);
        spinnerPayCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    str_PayCurrency = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private void callPayMode(Spinner spinnerPayMode) {


        String[] paymode = new String[]{
                "Payment Mode",
                "Cash",
                "Bank"




        };

        final List<String> paymodelist = new ArrayList<>(Arrays.asList(paymode));

        // Initializing an ArrayAdapter
        spinnerpaymodeArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.maritalstatus_item, paymodelist);

        spinnerpaymodeArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerPayMode.setAdapter(spinnerpaymodeArrayAdapter);
        spinnerPayMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    str_PayMode = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private void callAnnulpay(Spinner spinnerAnnualPay) {

        String[] annulpaystatus = new String[]{
                "Annual Pay",
                "20301",
                "19700",
                "19100",
                "18500",
                "18000",
                "20500"



        };

        final List<String> annulpaylist = new ArrayList<>(Arrays.asList(annulpaystatus));

        // Initializing an ArrayAdapter
        spinnerAnnulpayArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.maritalstatus_item, annulpaylist);

        spinnerAnnulpayArrayAdapter.setDropDownViewResource(R.layout.maritalstatus_item);
        spinnerAnnualPay.setAdapter(spinnerAnnulpayArrayAdapter);
        spinnerAnnualPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else {
                    str_AnnualPay = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backbutton_10:
                finish();
                break;
            case R.id.captureimage_profile10:
                PopupWindow popupwindow_obj = popupUpImgDisplay();
                popupwindow_obj.showAsDropDown(v, 0, 0);

                break;
            case R.id.next10button:

                String stDailyWages = txtDailyWages.getText().toString().trim();
                String stworkHours = txtworkHours.getText().toString().trim();
                String sttRates = txtRates.getText().toString().trim();
                String stTXRefer = txtRefer.getText().toString().trim();
                String stTXpersentage = txtpersentage.getText().toString().trim();

                stBranchName = edtBranchName.getText().toString().trim();
                stAccunNo = edtAccunNo.getText().toString().trim();

                String st_sort_code = txtSortCode.getText().toString().trim();


                Intent intent = new Intent(PayDtlsActivity.this, PayStructureActivity.class);
                intent.putExtra("PAY_GRPID", stPaygrpId);
                intent.putExtra("ANNUL_PAY", str_AnnualPay);
                intent.putExtra("PAY_TYPEID", stPayTypeId);
                intent.putExtra("BASIC_WG", stDailyWages);
                intent.putExtra("WRK_HOUR", stworkHours);
                intent.putExtra("RATES", sttRates);
                intent.putExtra("TAX_CODE", stTaxcodeId);
                intent.putExtra("TAX_REF", stTXRefer);
                intent.putExtra("TAX_PERCN", stTXpersentage);
                intent.putExtra("PAY_MODE", str_PayMode);
                intent.putExtra("BANK_NAME", stBankId);
                intent.putExtra("BRNCH_NAME", stBranchName);
                intent.putExtra("ACC_NO", stAccunNo);
                intent.putExtra("SORT_CODE", st_sort_code);
                intent.putExtra("PAY_CURRNCY", str_PayCurrency);
                startActivity(intent);
                break;


        }
    }




    public PopupWindow popupUpImgDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) PayDtlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

            Uri photoURI = FileProvider.getUriForFile(PayDtlsActivity.this,
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

