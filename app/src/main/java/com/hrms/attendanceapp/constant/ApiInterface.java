package com.hrms.attendanceapp.constant;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by user on 9/2/2019.
 */

public interface ApiInterface {



    @GET("loginapi")
    Call<String> getLogin(@Query("email") String stusername, @Query("psw") String stpasswrd);

    //https://workpermitcloud.co.uk/hrms/api/registrationapi?email=dasankita406@gmail.com&pass=1234&com_name=abc&f_name=ahjj&l_name=mmm&p_no=858394561
    @GET("registrationapi")
    Call<String> getSignUp(@Query("com_name") String strComName, @Query("f_name") String strFName, @Query("l_name") String strLName, @Query("email") String strEmail, @Query("pass") String strPasswd, @Query("p_no") String strPhone);


    @GET("viewemployerprofile")
    Call<String> getViewprofileOrg(@Query("reg") String struserid);




    @GET("viewemployeepprofileapi")
    Call<String> getViewprofile(@Query("user_id") String struserid);


    @GET("holidaymployeeprofileapi")
    Call<String> getViewHoliday(@Query("employee_id") String struserid);


    @GET("leavefileapi")
    Call<String> getLeaveTypelist(@Query("employee_id") String struserid);



    @GET("allholidayprofileapi")
    Call<String> getHolidaylist(@Query("employee_id") String struserid);



    @GET("allnotimployeecountprofileapi")
    Call<String> getViewNoti(@Query("employee_id") String strUserid);



    @GET("timeinmployeeprofileapi")
    Call<String> getTimein(@Query("employee_id") String struserid, @Query("date") String strdate, @Query("time_in") String strtimein, @Query("month") String strmonth, @Query("time_in_location") String strtimeinloc);




    @GET("timeoutmployeeprofileapi")
    Call<String> getTimeOut(@Query("employee_id") String struserid, @Query("date") String strdate, @Query("time_out") String strtimeout, @Query("month") String strmonth, @Query("time_out_location") String strtimeoutloc);


   // http://hrmplus.co.uk/api/dailymployeeprofileapi?employee_id=1471&month=08/2020

    @GET("dailymployeeprofileapi")
    Call<String> getattandencelist(@Query("employee_id") String struserid, @Query("month") String stmonth);

    @GET("getleavegetapi")
    Call<String> getLeaveHand(@Query("employee_id") String strId, @Query("leave_type") String strLeaveId);


    @GET("saveleavegetapi")
    Call<String> getLvapplysubmit(@Query("employee_id") String strId, @Query("employee_name") String strName, @Query("leave_type") String strLeaveId, @Query("leave_inhand") String leavehand, @Query("from_date") String fromdate, @Query("to_date") String todate, @Query("days") String dayNo, @Query("date_of_apply") String currntdate);

    @GET("allleaveappapi")
    Call<String> getViewLevSataus(@Query("employee_id") String strId);

    @GET("leaverequapi")
    Call<String> getLeaveAppver(@Query("employee_id") String strUserid);

    @GET("leaverequapediti")
    Call<String> getLeaveAppverDtl(@Query("employee_id") String strUserid, @Query("id") String strid);



    @GET("allnotimployeeprofileapi")
    Call<String> getViewNotification(@Query("employee_id") String userId);

    @GET("viewnotimployeecountprofileapi")
    Call<String> getViewNotificationdtls(@Query("employee_id") String userId, @Query("not_id") String notiId);



    @POST("editimage")
    Call<String> uploadImage(@Body String json);






    @POST("updateemployee")
    Call<String> getSaveOrder(@Body String json);



    @GET("getallemployeeapi")
    Call<String> getViewEmp(@Query("employee_id") String userId);

    @POST("addnofication")
    Call<String> getSaveNoti(@Body String toString);



    @POST("addnoficationemplyee")
    Call<String> getSaveNotiEmp(@Body String toString);
}
