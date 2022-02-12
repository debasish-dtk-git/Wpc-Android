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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hrms.attendanceapp.BuildConfig;
import com.hrms.attendanceapp.R;
import com.hrms.attendanceapp.constant.ApiInterface;
import com.hrms.attendanceapp.constant.Constants;
import com.hrms.attendanceapp.getset.EducationList;
import com.hrms.attendanceapp.getset.TrainningList;
import com.hrms.attendanceapp.utils.CommonUtils;
import com.hrms.attendanceapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EducationDtlsActivity extends AppCompatActivity implements View.OnClickListener{

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
    private LinearLayout container;
    ArrayList<EditText> allEds_qul;
    ArrayList<EditText> allEds_sub;
    ArrayList<EditText> allEds_insnm;
    ArrayList<EditText> allEds_board;
    ArrayList<EditText> allEds_year;
    ArrayList<EditText> allEds_persn;
    ArrayList<EditText> allEds_grade;

    private Button btnfromGally;
    private Button btntakephoto;
    private final int REQUEST_CODE_CLICK_IMAGE = 1002, REQUEST_CODE_GALLERY_IMAGE = 1003;
    private File flew;
    private String stphotoPah = "";
    private Bitmap bmp = null;
    ArrayList<String> doc1;
    ArrayList<String> doc2;
    int flag = 0;
    private Button btnfromGallyimg;
    private Button btntakephotoimg;
    private String strProfilepic = "";
    private JSONObject jsonObject1;
    private String userId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(EducationDtlsActivity.this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_education_dtls);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();


        Constants.imageFilePath = "";

        initView();

        userId = sh_Pref.getString("USER_CODE", "");
        String strcomLogo= sh_Pref.getString("COM_LOGO", "");
        strProfilepic = sh_Pref.getString("USER_PHOTO", "");


        if(!strcomLogo.equalsIgnoreCase("")) {

            String imurl = Constants.img_url + strcomLogo;
            Glide
                    .with(EducationDtlsActivity.this)
                    .load(imurl)
                    .into(imgLogo);


        }




        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(EducationDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);
            // imgPhoto.setImageResource(R.drawable.noimage);
        }
        else {


            String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(EducationDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);

            //showImage(imurl);


        }


        Constants.imageFilePath = CommonUtils.getFilename();

        Log.d("Image Path===", Constants.imageFilePath);


        inittab(Constants.eduLists);





    }

    private void inittab(ArrayList<EducationList> eduLists) {
        Constants.eduListResult.clear();
        allEds_qul = new ArrayList<EditText>();
        allEds_sub = new ArrayList<EditText>();
        allEds_insnm = new ArrayList<EditText>();
        allEds_board = new ArrayList<EditText>();
        allEds_year = new ArrayList<EditText>();
        allEds_persn = new ArrayList<EditText>();
        allEds_grade = new ArrayList<EditText>();
        doc1 = new ArrayList<String>();
        doc2 = new ArrayList<String>();

        for(EducationList edu_ : eduLists)
        {



            TextView txt1 = new TextView(this);
            txt1.setText("Qualification");

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
            // tbrow.addView(t1v);
            linEmployee.addView(txt1);



            EditText t1v = new EditText(this);
            t1v.setHint("Qualification");
            LinearLayout.LayoutParams paramsedt = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramsedt.setMargins(0, 0, 0, 15);
            t1v.setLayoutParams(paramsedt);
            t1v.setBackgroundResource(R.drawable.round_shape);


            t1v.setText(edu_.getQulif());
            t1v.setSelection(t1v.getText().length());
            t1v.setTag(edu_.getId());

            allEds_qul.add(t1v);


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
            txt2.setText("Subject");

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
            // tbrow.addView(t1v);
            linEmployee.addView(txt2);

            EditText t2v = new EditText(this);
            t2v.setHint("Subject");
            LinearLayout.LayoutParams paramst2v = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramst2v.setMargins(0, 0, 0, 15);
            t2v.setLayoutParams(paramst2v);
            t2v.setBackgroundResource(R.drawable.round_shape);


            t2v.setText(edu_.getSubject());
            t2v.setSelection(t2v.getText().length());
            //t2v.setTag(edu_.getId());

            allEds_sub.add(t2v);


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
            t2v.setPadding(30,25,10,25);

            linEmployee.addView(t2v);

            TextView txt3 = new TextView(this);
            txt3.setText("Institution Name");

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

                    txt2.setTextSize(18);


                    break;
                default:
                    txt3.setTextSize(15);

                    break;

            }



            txt3.setTextColor(Color.parseColor("#000000"));
            txt3.setGravity(Gravity.LEFT);
            txt3.setPadding(30,10,10,10);
            // tbrow.addView(t1v);
            linEmployee.addView(txt3);



            EditText t3v = new EditText(this);
            t3v.setHint("Institution Name");
            LinearLayout.LayoutParams paramst3v = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramst3v.setMargins(0, 0, 0, 15);
            t3v.setLayoutParams(paramst2v);
            t3v.setBackgroundResource(R.drawable.round_shape);


            t3v.setText(edu_.getInsName());
            t3v.setSelection(t3v.getText().length());
           // t3v.setTag(edu_.getId());

            allEds_insnm.add(t3v);

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
            t3v.setPadding(30,25,10,25);

            linEmployee.addView(t3v);


            TextView txt4 = new TextView(this);
            txt4.setText("Awarding Body/ University");

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
            t4v.setHint("Awarding Body/ University");
            LinearLayout.LayoutParams paramst4v = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramst4v.setMargins(0, 0, 0, 15);
            t4v.setLayoutParams(paramst4v);
            t4v.setBackgroundResource(R.drawable.round_shape);


            t4v.setText(edu_.getBoard());
            t4v.setSelection(t4v.getText().length());
            //t4v.setTag(edu_.getId());

            allEds_board.add(t4v);

            switch (width) {
                case 480:

                    t4v.setTextSize(13);

                    break;
                case 600:

                    t4v.setTextSize(20);

                    break;
                case 720:

                    t2v.setTextSize(15);

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

            linEmployee.addView(t4v);


            TextView txt5 = new TextView(this);
            txt5.setText("Year of Passing");

            switch (width) {
                case 480:

                    txt5.setTextSize(13);

                    break;
                case 600:

                    txt5.setTextSize(20);

                    break;
                case 720:

                    txt5.setTextSize(15);

                    break;

                case 1080:
                    txt5.setTextSize(16);

                    break;
                case 1200:

                    txt5.setTextSize(18);


                    break;
                default:
                    txt5.setTextSize(15);

                    break;

            }



            txt5.setTextColor(Color.parseColor("#000000"));
            txt5.setGravity(Gravity.LEFT);
            txt5.setPadding(30,10,10,10);
            linEmployee.addView(txt5);



            EditText t5v = new EditText(this);
            t5v.setHint("Year of Passing");
            LinearLayout.LayoutParams paramst5v = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramst5v.setMargins(0, 0, 0, 15);
            t5v.setLayoutParams(paramst5v);
            t5v.setBackgroundResource(R.drawable.round_shape);


            t5v.setText(edu_.getPassyear());
            t5v.setSelection(t2v.getText().length());
            //t5v.setTag(edu_.getId());

            allEds_year.add(t5v);

            switch (width) {
                case 480:

                    t5v.setTextSize(13);

                    break;
                case 600:

                    t5v.setTextSize(20);

                    break;
                case 720:

                    t5v.setTextSize(15);

                    break;

                case 1080:
                    t5v.setTextSize(16);

                    break;
                case 1200:

                    t5v.setTextSize(18);


                    break;
                default:
                    t5v.setTextSize(15);

                    break;

            }



            t5v.setTextColor(Color.parseColor("#626060"));
            t5v.setGravity(Gravity.LEFT);
            t5v.setPadding(30,25,10,25);

            linEmployee.addView(t5v);

            TextView txt6 = new TextView(this);
            txt6.setText("Percentage");

            switch (width) {
                case 480:

                    txt6.setTextSize(13);

                    break;
                case 600:

                    txt6.setTextSize(20);

                    break;
                case 720:

                    txt6.setTextSize(15);

                    break;

                case 1080:
                    txt6.setTextSize(16);

                    break;
                case 1200:

                    txt6.setTextSize(18);


                    break;
                default:
                    txt6.setTextSize(15);

                    break;

            }



            txt6.setTextColor(Color.parseColor("#000000"));
            txt6.setGravity(Gravity.LEFT);
            txt6.setPadding(30,10,10,10);
            linEmployee.addView(txt6);


            EditText t6v = new EditText(this);
            t6v.setHint("Percentage");
            LinearLayout.LayoutParams paramst6v = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramst6v.setMargins(0, 0, 0, 15);
            t6v.setLayoutParams(paramst6v);
            t6v.setBackgroundResource(R.drawable.round_shape);


            t6v.setText(edu_.getPersentage());
            t6v.setSelection(t6v.getText().length());
           // t6v.setTag(edu_.getId());

            allEds_persn.add(t6v);

            switch (width) {
                case 480:

                    t6v.setTextSize(13);

                    break;
                case 600:

                    t6v.setTextSize(20);

                    break;
                case 720:

                    t6v.setTextSize(15);

                    break;

                case 1080:
                    t6v.setTextSize(16);

                    break;
                case 1200:

                    t6v.setTextSize(18);


                    break;
                default:
                    t6v.setTextSize(15);

                    break;

            }



            t6v.setTextColor(Color.parseColor("#626060"));
            t6v.setGravity(Gravity.LEFT);
            t6v.setPadding(30,25,10,25);

            linEmployee.addView(t6v);


            TextView txt7 = new TextView(this);
            txt7.setText("Grade/Division");

            switch (width) {
                case 480:

                    txt7.setTextSize(13);

                    break;
                case 600:

                    txt7.setTextSize(20);

                    break;
                case 720:

                    txt7.setTextSize(15);

                    break;

                case 1080:
                    txt7.setTextSize(16);

                    break;
                case 1200:

                    txt7.setTextSize(18);


                    break;
                default:
                    txt7.setTextSize(15);

                    break;

            }



            txt7.setTextColor(Color.parseColor("#000000"));
            txt7.setGravity(Gravity.LEFT);
            txt7.setPadding(30,10,10,10);
            linEmployee.addView(txt7);


            EditText t7v = new EditText(this);
            t7v.setHint("Grade/Division");
            LinearLayout.LayoutParams paramst7v = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramst7v.setMargins(0, 0, 0, 15);
            t7v.setLayoutParams(paramst7v);
            t7v.setBackgroundResource(R.drawable.round_shape);


            t7v.setText(edu_.getGrade());
            t7v.setSelection(t7v.getText().length());
            //t7v.setTag(edu_.getId());
            allEds_grade.add(t7v);

            switch (width) {
                case 480:

                    t7v.setTextSize(13);

                    break;
                case 600:

                    t7v.setTextSize(20);

                    break;
                case 720:

                    t7v.setTextSize(15);

                    break;

                case 1080:
                    t7v.setTextSize(16);

                    break;
                case 1200:

                    t7v.setTextSize(18);


                    break;
                default:
                    t7v.setTextSize(15);

                    break;

            }



            t7v.setTextColor(Color.parseColor("#626060"));
            t7v.setGravity(Gravity.LEFT);
            t7v.setPadding(30,25,10,25);

            linEmployee.addView(t7v);




            LinearLayout lin1 = new LinearLayout(this);
            lin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 1;
                    PopupWindow popupwindow_obj = popupDisplay();
                    popupwindow_obj.showAsDropDown(v, 0, 0);
                }
            });



            lin1.setOrientation(LinearLayout.HORIZONTAL);
            lin1.setGravity(Gravity.CENTER);

           LinearLayout.LayoutParams paramslin1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
           paramslin1.setMargins(0, 0, 0, 25);
           lin1.setLayoutParams(paramslin1);
           lin1.setBackgroundResource(R.drawable.button_doc_shape);

           TextView txtdoc1 = new TextView(this);

            txtdoc1.setText("Upload Transcript Document");
            LinearLayout.LayoutParams paramt2doc1 = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramt2doc1.setMargins(0, 0, 10, 0);
            txtdoc1.setLayoutParams(paramt2doc1);



            switch (width) {
                case 480:

                    txtdoc1.setTextSize(8);

                    break;
                case 600:

                    txtdoc1.setTextSize(15);

                    break;
                case 720:

                    txtdoc1.setTextSize(10);

                    break;

                case 1080:

                    txtdoc1.setTextSize(12);
                    break;
                case 1200:

                    txtdoc1.setTextSize(13);


                    break;
                default:
                    txtdoc1.setTextSize(10);

                    break;

            }



            txtdoc1.setTextColor(Color.parseColor("#ffffff"));
            txtdoc1.setGravity(Gravity.LEFT);
            txtdoc1.setPadding(30,25,30,25);

            lin1.addView(txtdoc1,0);


           ImageView img = new ImageView(this);

            LinearLayout.LayoutParams paramt2im1 = new LinearLayout.LayoutParams
                    (50, 50);

            paramt2im1.setMargins(0, 0, 0, 0);
            img.setLayoutParams(paramt2im1);
            img.setImageResource(R.drawable.camera_doc);





            lin1.addView(img,1);
            linEmployee.addView(lin1);







            LinearLayout lin2 = new LinearLayout(this);

            lin2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 2;
                    PopupWindow popupwindow_obj = popupDisplay();
                    popupwindow_obj.showAsDropDown(v, 0, 0);
                }
            });







            lin2.setOrientation(LinearLayout.HORIZONTAL);
            lin2.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams paramslin2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramslin2.setMargins(0, 0, 0, 100);
            lin2.setLayoutParams(paramslin2);
            lin2.setBackgroundResource(R.drawable.button_doc_shape);

            TextView txtdoc2 = new TextView(this);

            txtdoc2.setText("Upload Certificate Document");
            LinearLayout.LayoutParams paramt2doc2 = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            paramt2doc2.setMargins(0, 0, 10, 0);
            txtdoc2.setLayoutParams(paramt2doc2);



            switch (width) {
                case 480:

                    txtdoc2.setTextSize(8);

                    break;
                case 600:

                    txtdoc2.setTextSize(15);

                    break;
                case 720:

                    txtdoc2.setTextSize(10);

                    break;

                case 1080:

                    txtdoc2.setTextSize(12);
                    break;
                case 1200:

                    txtdoc2.setTextSize(13);


                    break;
                default:
                    txtdoc2.setTextSize(10);

                    break;

            }



            txtdoc2.setTextColor(Color.parseColor("#ffffff"));
            txtdoc2.setGravity(Gravity.LEFT);
            txtdoc2.setPadding(30,25,30,25);

            lin2.addView(txtdoc2,0);


            ImageView img2 = new ImageView(this);

            LinearLayout.LayoutParams paramt2im2 = new LinearLayout.LayoutParams
                    (50, 50);

            paramt2im2.setMargins(0, 0, 0, 0);
            img2.setLayoutParams(paramt2im2);
            img2.setImageResource(R.drawable.camera_doc);





            lin2.addView(img2,1);
            linEmployee.addView(lin2);







        }






    }



    public void onAddFieldEdu(View v) {
        LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.row_edu, null);
        // Add the new row before the add field button.
       // EducationList edu2 = new EducationList();
        EditText edt_qu =  (EditText)rowView.findViewById(R.id.edittext_qu_edu);
        allEds_qul.add(edt_qu);

        EditText edt_sub =  (EditText)rowView.findViewById(R.id.edittext__sub_edu);
        allEds_sub.add(edt_sub);

        EditText edt_insname =  (EditText)rowView.findViewById(R.id.edittext__insname_edu);
        allEds_insnm.add(edt_insname);

        EditText edt_board =  (EditText)rowView.findViewById(R.id.edittext_board_edu);
        allEds_board.add(edt_board);

        EditText edt_year =  (EditText)rowView.findViewById(R.id.edittext_year_edu);
        allEds_year.add(edt_year);

        EditText edt_persentage =  (EditText)rowView.findViewById(R.id.edittext_qu_edu);
        allEds_persn.add(edt_persentage);

        EditText edt_grade =  (EditText)rowView.findViewById(R.id.edittext_grade_edu);
        allEds_grade.add(edt_grade);

        LinearLayout lin_doc1 =  (LinearLayout)rowView.findViewById(R.id.lin_doc_edu1);
        lin_doc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                PopupWindow popupwindow_obj = popupDisplay();
                popupwindow_obj.showAsDropDown(v, 0, 0);
            }
        });

        LinearLayout lin_doc2 =  (LinearLayout)rowView.findViewById(R.id.lin_doc_edu2);
        lin_doc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                PopupWindow popupwindow_obj = popupDisplay();
                popupwindow_obj.showAsDropDown(v, 0, 0);
            }
        });



        Button buttonRemove = (Button)rowView.findViewById(R.id.button_remove_edu);
        buttonRemove.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ((LinearLayout)rowView.getParent()).removeView(rowView);
            }});

        container.addView(rowView);

    }





    public PopupWindow popupDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) EducationDtlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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



    @Override
    protected void onResume() {
        super.onResume();

        strProfilepic = sh_Pref.getString("USER_PHOTO", "");
        if(strProfilepic.equalsIgnoreCase(""))
        {
            //setting the bitmap from the drawable folder
            Bitmap bitmap = BitmapFactory.decodeResource(EducationDtlsActivity.this.getResources(), R.drawable.profile_pic);

            //set the image to the imageView
            profilepic.setImageBitmap(bitmap);

        }
        else {


           String imurl = Constants.img_url+strProfilepic;
            Glide
                    .with(EducationDtlsActivity.this)
                    .load(imurl)
                    .into(profilepic);




        }


    }





    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backbutton_5:
                finish();
                break;
            case R.id.captureimage_profile5:
                   flag = 5;
                PopupWindow popupwindow_obj = popupUpImgDisplay();
                popupwindow_obj.showAsDropDown(v, 0, 0);

                break;
            case R.id.next5button:
            for(int i = 0;i<allEds_qul.size();i++) {


                EducationList edu1 = new EducationList();
                edu1.setId(String.valueOf(allEds_qul.get(i).getTag()));

                edu1.setQulif(allEds_qul.get(i).getText().toString());
                edu1.setSubject(allEds_sub.get(i).getText().toString());
                edu1.setInsName(allEds_insnm.get(i).getText().toString());
                edu1.setBoard(allEds_board.get(i).getText().toString());
                edu1.setPassyear(allEds_year.get(i).getText().toString());
                edu1.setPersentage(allEds_persn.get(i).getText().toString());
                edu1.setGrade(allEds_grade.get(i).getText().toString());
                if(doc1.size()>= i+1)

                    edu1.setDoc1(doc1.get(i));

                if(doc2.size()>= i+1)
                     edu1.setDoc2(doc2.get(i));

                Constants.eduListResult.add(edu1);

            }


                Constants.eduListResult.size();
                Intent intent = new Intent(EducationDtlsActivity.this, JobDtlsActivity.class);
                startActivity(intent);
                break;
        }
    }




    public PopupWindow popupUpImgDisplay()
    {

        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) EducationDtlsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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





    private void initView() {

        imgBack = (ImageView)findViewById(R.id.backbutton_5);
        imgBack.setOnClickListener(this);

        imgLogo = (ImageView)findViewById(R.id.imageview_logo_profile5);

        profilepic = (CircleImageView) findViewById(R.id.profile_image5);

        captureImage = (ImageView) findViewById(R.id.captureimage_profile5);
        captureImage.setOnClickListener(this);

        linEmployee = (LinearLayout)findViewById(R.id.lin_employee_edu);

        container = (LinearLayout)findViewById(R.id.container_edu);



        btnNext = (Button)findViewById(R.id.next5button);
        btnNext.setOnClickListener(this);
    }



    /**
     * Request for camera app to open and capture image.
     * @param isFromGallery-if true then launch gallery app else camera app.
     */
    public void startIntent(boolean isFromGallery) {
        if (!isFromGallery) {
            File imageFile = new File(Constants.imageFilePath);
            //Uri imageFileUri = Uri.fromFile(imageFile); // convert path to Uri


            Uri photoURI = FileProvider.getUriForFile(EducationDtlsActivity.this,
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
                doc1.add(stphotoPah);


                flag = 0;
            }
            else if(flag == 2)
            {
                doc2.add(stphotoPah);
                flag = 0;
            }


          flew = new File(stphotoPah);
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





}


