package com.hrms.attendanceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.BuildConfig;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.JobList;
import com.hrms.attendanceapp.getset.TrainningList;
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

public class TrainingDetlsActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE_CLICK_IMAGE = 1002, REQUEST_CODE_GALLERY_IMAGE = 1003;
    private int width;
    private int height;
    private SharedPreferences sh_Pref;
    private SharedPreferences.Editor toEdit;
    private LinearLayout lin;
    private ImageView imgBack;
    private ImageView imgLogo;
    private CircleImageView profilepic;
    private ImageView captureImage;
    private Button btnNext;
    private LinearLayout linEmployee;
    DatePickerDialog picker;
    Calendar cldr;
    private Button btnAdd;
    private Button btnRemove;
    private LinearLayout container;
    ArrayList<EditText> allEds_title;
    ArrayList<TextView> allEds_stDate;
    ArrayList<TextView> allEds_endDate;
    ArrayList<EditText> allEds_desc;
    private String strProfilepic = "";
    private String userId = "";
    private JSONObject jsonObject1;
    private Bitmap bmp = null;
    private String stphotoPah = "";
    private Button btnfromGallyimg;
    private Button btntakephotoimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(TrainingDetlsActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_training_detls);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        Constants.imageFilePath = "";


        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();

        initView();
        changeIconColor(TrainingDetlsActivity.this,R.drawable.calander_icon);

        String strcomLogo= sh_Pref.getString("COM_LOGO", "");
        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        userId = sh_Pref.getString("USER_CODE", "");


        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(TrainingDetlsActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }

        Constants.imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", Constants.imageFilePath);


        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(TrainingDetlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);
            // imgPhoto.setImageResource(R.drawable.noimage);
        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(TrainingDetlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }



        inittab(Constants.trainLists);





    }

    private void inittab(ArrayList<TrainningList> trainiLists) {
        Constants.trainListResult.clear();
        allEds_title = new ArrayList<EditText>();
        allEds_stDate = new ArrayList<TextView>();
        allEds_endDate = new ArrayList<TextView>();
        allEds_desc = new ArrayList<EditText>();

        for(TrainningList tenist_ : trainiLists)
        {

            TextView txt1 = new TextView(this);
            txt1.setText("Title");

            switch (width) {
                case 480:

                    txt1.setTextSize(13);

                    break;
                case 600:

                    txt1.setTextSize(20);

                    break;
                case 720:

                    txt1.setTextSize(15);

                    break;

                case 1080:
                    txt1.setTextSize(16);

                    break;
                case 1200:

                    txt1.setTextSize(18);


                    break;
                default:
                    txt1.setTextSize(15);

                    break;

            }



            txt1.setTextColor(Color.parseColor("#000000"));
            txt1.setGravity(Gravity.LEFT);
            txt1.setPadding(30,10,10,10);

            linEmployee.addView(txt1);


            EditText t1v = new EditText(this);

            LinearLayout.LayoutParams paramsedt = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramsedt.setMargins(0, 0, 0, 15);
            t1v.setLayoutParams(paramsedt);
            t1v.setBackgroundResource(R.drawable.round_shape);


            t1v.setText(tenist_.getName());
            t1v.setSelection(t1v.getText().length());
            t1v.setTag(tenist_.getId());
            allEds_title.add(t1v);

            switch (width) {
                case 480:

                    t1v.setTextSize(13);

                    break;
                case 600:

                    t1v.setTextSize(20);

                    break;
                case 720:

                    t1v.setTextSize(15);

                    break;

                case 1080:
                    t1v.setTextSize(16);

                    break;
                case 1200:

                    t1v.setTextSize(18);


                    break;
                default:
                    t1v.setTextSize(15);

                    break;

            }



            t1v.setTextColor(Color.parseColor("#626060"));
            t1v.setGravity(Gravity.LEFT);
            t1v.setPadding(30,25,10,25);
            linEmployee.addView(t1v);


            TextView txt2 = new TextView(this);
            txt2.setText("Start Date");

            switch (width) {
                case 480:

                    txt2.setTextSize(13);

                    break;
                case 600:

                    txt2.setTextSize(20);

                    break;
                case 720:

                    txt2.setTextSize(15);

                    break;

                case 1080:
                    txt2.setTextSize(16);

                    break;
                case 1200:

                    txt2.setTextSize(18);


                    break;
                default:
                    txt2.setTextSize(15);

                    break;

            }



            txt2.setTextColor(Color.parseColor("#000000"));
            txt2.setGravity(Gravity.LEFT);
            txt2.setPadding(30,10,10,10);

            linEmployee.addView(txt2);



            final TextView t2v = new TextView(this);

            t2v.setText(Utils.parseDateToddMMyyyy(tenist_.getStatdate()));
            allEds_stDate.add(t2v);
            t2v.setBackgroundResource(R.drawable.round_shape);
            LinearLayout.LayoutParams paramt2v = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramt2v.setMargins(0, 0, 0, 15);
            t2v.setLayoutParams(paramt2v);
            t2v.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calander_icon, 0);
            t2v.setCompoundDrawablePadding(10);
            t2v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cldr = Calendar.getInstance();
                    int day3 = cldr.get(Calendar.DAY_OF_MONTH);
                    int month3 = cldr.get(Calendar.MONTH);
                    int year3 = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(TrainingDetlsActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    t2v.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);

                                }
                            }, year3, month3, day3);
                    picker.show();
                }
            });


            switch (width) {
                case 480:

                    t2v.setTextSize(13);

                    break;
                case 600:

                    t2v.setTextSize(20);

                    break;
                case 720:

                    t2v.setTextSize(15);

                    break;

                case 1080:

                    t2v.setTextSize(16);
                    break;
                case 1200:

                    t2v.setTextSize(18);


                    break;
                default:
                    t2v.setTextSize(15);

                    break;

            }



            t2v.setTextColor(Color.parseColor("#626060"));
            t2v.setGravity(Gravity.LEFT);
            t2v.setPadding(30,25,30,25);

            linEmployee.addView(t2v);



            TextView txt3 = new TextView(this);
            txt3.setText("End Date");

            switch (width) {
                case 480:

                    txt3.setTextSize(13);

                    break;
                case 600:

                    txt3.setTextSize(20);

                    break;
                case 720:

                    txt3.setTextSize(15);

                    break;

                case 1080:
                    txt3.setTextSize(16);

                    break;
                case 1200:

                    txt3.setTextSize(18);


                    break;
                default:
                    txt3.setTextSize(15);

                    break;

            }



            txt3.setTextColor(Color.parseColor("#000000"));
            txt3.setGravity(Gravity.LEFT);
            txt3.setPadding(30,10,10,10);

            linEmployee.addView(txt3);




            final TextView t3v = new TextView(this);

            t3v.setText(Utils.parseDateToddMMyyyy(tenist_.getEnddate()));
            allEds_endDate.add(t3v);
            t3v.setBackgroundResource(R.drawable.round_shape);
            LinearLayout.LayoutParams paramt3v = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramt3v.setMargins(0, 0, 0, 15);
            t3v.setLayoutParams(paramt3v);
            t3v.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calander_icon, 0);
            t3v.setCompoundDrawablePadding(10);
            t3v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cldr = Calendar.getInstance();
                    int day3 = cldr.get(Calendar.DAY_OF_MONTH);
                    int month3 = cldr.get(Calendar.MONTH);
                    int year3 = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(TrainingDetlsActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    t3v.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                                }
                            }, year3, month3, day3);
                    picker.show();
                }
            });


            switch (width) {
                case 480:

                    t3v.setTextSize(13);

                    break;
                case 600:

                    t3v.setTextSize(20);

                    break;
                case 720:

                    t3v.setTextSize(15);

                    break;

                case 1080:

                    t3v.setTextSize(16);
                    break;
                case 1200:

                    t3v.setTextSize(18);


                    break;
                default:
                    t3v.setTextSize(15);

                    break;

            }



            t3v.setTextColor(Color.parseColor("#626060"));
            t3v.setGravity(Gravity.LEFT);
            t3v.setPadding(30,25,30,25);
            linEmployee.addView(t3v);

            TextView txt4 = new TextView(this);
            txt4.setText("Description");

            switch (width) {
                case 480:

                    txt4.setTextSize(13);

                    break;
                case 600:

                    txt4.setTextSize(20);

                    break;
                case 720:

                    txt4.setTextSize(15);

                    break;

                case 1080:
                    txt4.setTextSize(16);

                    break;
                case 1200:

                    txt4.setTextSize(18);


                    break;
                default:
                    txt4.setTextSize(15);

                    break;

            }



            txt4.setTextColor(Color.parseColor("#000000"));
            txt4.setGravity(Gravity.LEFT);
            txt4.setPadding(30,10,10,10);

            linEmployee.addView(txt4);


            EditText t4v = new EditText(this);

            LinearLayout.LayoutParams paramst4v = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramst4v.setMargins(0, 0, 0, 10);
            t4v.setLayoutParams(paramst4v);
            t4v.setBackgroundResource(R.drawable.round_shape);


            t4v.setText(tenist_.getDesc());
            t4v.setSelection(t4v.getText().length());
            allEds_desc.add(t4v);

            switch (width) {
                case 480:

                    t4v.setTextSize(13);

                    break;
                case 600:

                    t4v.setTextSize(20);

                    break;
                case 720:

                    t4v.setTextSize(15);

                    break;

                case 1080:
                    t4v.setTextSize(16);

                    break;
                case 1200:

                    t4v.setTextSize(18);


                    break;
                default:
                    t4v.setTextSize(15);

                    break;

            }



            t4v.setTextColor(Color.parseColor("#626060"));
            t4v.setGravity(Gravity.LEFT);
            t4v.setPadding(30,25,10,25);
            // tbrow.addView(t1v);
            linEmployee.addView(t4v);





        }






    }



    public void onAddField(View v) {
        LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView=inflater.inflate(R.layout.row_training, null);
        // Add the new row before the add field button.

        EditText edttitle =  (EditText)rowView.findViewById(R.id.edittext_employee_jobtitle_tr);
        allEds_title.add(edttitle);
        final TextView txtStartdate = (TextView)rowView.findViewById(R.id.textview_employee_startdate_tr);
        txtStartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cldr = Calendar.getInstance();
                int day3 = cldr.get(Calendar.DAY_OF_MONTH);
                int month3 = cldr.get(Calendar.MONTH);
                int year3 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(TrainingDetlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtStartdate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                                allEds_stDate.add(txtStartdate);
                            }
                        }, year3, month3, day3);
                picker.show();
            }
        });




        final TextView txtEnddate = (TextView)rowView.findViewById(R.id.textview_employee_enddate_tr);
        txtEnddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cldr = Calendar.getInstance();
                int day3 = cldr.get(Calendar.DAY_OF_MONTH);
                int month3 = cldr.get(Calendar.MONTH);
                int year3 = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(TrainingDetlsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txtEnddate.setText(Utils.getDate(dayOfMonth) + "-" + Utils.getDate((monthOfYear + 1)) + "-" + year);
                                allEds_endDate.add(txtEnddate);
                            }
                        }, year3, month3, day3);
                picker.show();
            }
        });

        EditText edtdesc =  (EditText)rowView.findViewById(R.id.edittext_employee_jobdesc_tr);
        allEds_desc.add(edtdesc);


    Button buttonRemove = (Button)rowView.findViewById(R.id.button_remove_train);
        buttonRemove.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ((LinearLayout)rowView.getParent()).removeView(rowView);
            }});

        container.addView(rowView);

    }






    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backbutton_4:
                finish();
                break;
            case R.id.captureimage_profile4:
                PopupWindow popupwindow_obj = popupUpImgDisplay();
                popupwindow_obj.showAsDropDown(v, 0, 0);

                break;
            case R.id.next4button:

                for(int i = 0;i<allEds_title.size();i++) {


                    TrainningList trainng1 = new TrainningList();
                    trainng1.setId(String.valueOf(allEds_title.get(i).getTag()));
                    trainng1.setName(allEds_title.get(i).getText().toString());

                    if(allEds_stDate.size()>= i+1)
                        trainng1.setStatdate(allEds_stDate.get(i).getText().toString());

                    if(allEds_endDate.size()>= i+1)
                        trainng1.setStatdate(allEds_endDate.get(i).getText().toString());


                    if(allEds_desc.size()>= i+1)
                        trainng1.setDesc(allEds_desc.get(i).getText().toString());


                    Constants.trainListResult.add(trainng1);

                }

                Constants.trainListResult.size();


                Intent intent = new Intent(TrainingDetlsActivity.this, AdditonalDtlsActivity.class);
               startActivity(intent);
                break;


        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(TrainingDetlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);

        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(TrainingDetlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }


    }







    private void initView() {

        imgBack = (ImageView)findViewById(R.id.backbutton_4);
        imgBack.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_logo_profile4);

        profilepic = (CircleImageView) findViewById(R.id.profile_image4);

        captureImage = (ImageView) findViewById(R.id.captureimage_profile4);
        captureImage.setOnClickListener(this);

        linEmployee = (LinearLayout)findViewById(R.id.lin_employee_train);

        container = (LinearLayout)findViewById(R.id.container_train);



        btnNext = (Button)findViewById(R.id.next4button);
        btnNext.setOnClickListener(this);
    }



    public PopupWindow popupUpImgDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) TrainingDetlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

            Uri photoURI = FileProvider.getUriForFile(TrainingDetlsActivity.this,
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


           ;

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




    //CHANGE ICON COLOR
    private void changeIconColor(Context context , int drawable){
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, drawable);
        assert unwrappedDrawable != null;
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, R.color.colorcal));
    }



}

