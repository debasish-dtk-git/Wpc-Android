package com.hrms.attendanceapp.constant;

import com.hrms.attendanceapp.getset.Authority;
import com.hrms.attendanceapp.getset.BankList;
import com.hrms.attendanceapp.getset.CountryList;
import com.hrms.attendanceapp.getset.DocList;
import com.hrms.attendanceapp.getset.EducationList;
import com.hrms.attendanceapp.getset.JobList;
import com.hrms.attendanceapp.getset.MonitorList;
import com.hrms.attendanceapp.getset.PayGroup;
import com.hrms.attendanceapp.getset.PayType;
import com.hrms.attendanceapp.getset.TaxCode;
import com.hrms.attendanceapp.getset.TrainningList;

import java.util.ArrayList;

/**
 * Created by Hirak on 12/27/2018.
 */

public class Constants {

    public final static String PERSISTENT_STORE_NAME = "attendenceapp";
    public static String BASE_URL = "https://workpermitcloud.co.uk/hrms/api/";
    public static String img_url = "https://workpermitcloud.co.uk/hrms/";
   // public static String imgUrl = "http://gowin24.net/HRMS/admin/emp_image/";
    public static String imageFilePath = "";//Made it static as need to override the original image with compressed image.



    //http://hrmplus.co.uk/api/loginapi?email=ashim@gmail.com&psw=123456


     public static int StartX=0;
     public static int EndX=0;
     public static int StartY=0;
     public static int EndY=0;


    public static ArrayList<Authority> authorityLists = new ArrayList<Authority>();
    public static ArrayList<JobList> jobLists = new ArrayList<JobList>();
    public static ArrayList<TrainningList> trainLists = new ArrayList<TrainningList>();
    public static ArrayList<EducationList> eduLists = new ArrayList<EducationList>();

    public static ArrayList<PayGroup> gradeLists = new ArrayList<PayGroup>();
    public static ArrayList<PayType> paytypeLists = new ArrayList<PayType>();

    public static ArrayList<TaxCode> taxcodeLists = new ArrayList<TaxCode>();

    public static ArrayList<BankList> bankLists = new ArrayList<BankList>();

   public static ArrayList<CountryList> contyLists = new ArrayList<CountryList>();

    public static ArrayList<DocList> docLists = new ArrayList<DocList>();

   public static ArrayList<MonitorList> monitorList = new ArrayList<MonitorList>();








    public static String str_BloodGp = "";
    public static String str_height = "";
    public static String str_weightt = "";
    public static String str_idntimark1 = "";
    public static String str_idntimark2 = "";
    public static String str_physi_state = "";
    public static String str_criminal = "";


    public static String str_licenName = "";
    public static String str_licenNumb = "";
    public static String str_licenstartdate = "";
    public static String str_licenenddate = "";



    public static String str_emgName = "";
    public static String str_emgRelation = "";
    public static String str_emgEmail = "";
    public static String str_emgPhone = "";
    public static String str_emgAddress = "";


   public static String str_Adds1_pr = "";
   public static String str_Adds2_pr = "";
   public static String str_roadName_pr = "";
   public static String str_city_pr = "";
   public static String str_postcode_pr = "";
   public static String str_country_pr = "";



   public static String str_Adds1_ps = "";
   public static String str_Adds2_ps = "";
   public static String str_roadName_ps = "";
   public static String str_city_ps = "";
   public static String str_postcode_ps = "";
   public static String str_country_ps = "";


    public static String str_National_ID_No = "";
    public static String str_placeissue = "";
    public static String str_issueDate = "";
    public static String str_expDate = "";
    public static String str_national = "";
    public static String str_country_ofbirth = "";
    public static String str_country_residence = "";



    public static String str_passportNo = "";
    public static String str_placeofbirth = "";
    public static String str_passpt_nation = "";
    public static String str_issuedBy_paspt = "";
    public static String str_issuedDate_paspt = "";
    public static String str_expDate_paspt = "";
    public static String str_reviewDate_paspt = "";
    public static String str_eligibleStatus_paspt = "";
    public static String str_currentPasport = "";
    public static String str_remarksPasport = "";



    public static String str_visaNo = "";
    public static String str_visa_nation = "";
    public static String str_issuedBy_visa = "";
    public static String str_issuedDate_visa = "";
    public static String str_expDate_visa = "";
    public static String str_reviewDate_visa = "";
    public static String str_eligibleStatus_visa = "";
    public static String str_currentVisa = "";
    public static String str_remarksVisa = "";


    public static String str_type_of_License = "";
    public static String str_LicenceNo = "";
    public static String str_lisencExperyDate = "";




    public static String str_currency = "";
    public static String str_minwork = "";
    public static String str_minrate = "";
    public static String str_daily = "";
    public static String str_taxemp = "";
    public static String str_taxref = "";
    public static String str_taxperscn = "";
    public static String str_empGroupname = "";
    public static String str_empPayScale = "";
    public static String str_payMode = "";
    public static String str_paymenttype = "";
    public static String str_bankName = "";
    public static String str_branchId = "";
    public static String str_empSortCode = "";
    public static String str_empAccountNo = "";



    public static String stid = "";
    public static String str_da = "";
    public static String str_hra = "";
    public static String str_Con_Allowance = "";
    public static String str_perfomance_Allowance = "";
    public static String str_mothly_Allowance = "";

    public static String str_pf = "";
    public static String str_Esi = "";
    public static String str_Tax_Cess = "";
    public static String str_incomeTax = "";
    public static String str_professionalTax = "";




    public static String strEmpCode = "";
    public static String strFname = "";
    public static String strMname = "";
    public static String strLname = "";
    public static String strNlno = "";
    public static String strDob = "";
    public static String strEmailid = "";
    public static String strGenderstatus_ = "";
    public static String strMaritalstatus_ = "";
    public static String strNationality_ = "";
    public static String strcontact_ = "";
    public static String stremgcontact_ = "";


    public static String strdateojn = "";
    public static String strDOConf = "";
    public static String strStartdateServ = "";
    public static String strEnddateServ = "";
    public static String strJobLocServ = "";
    public static String strDeptstatus = "";
    public static String strDesigstatus = "";
    public static String strEmptype = "";
    public static String reportingAuth = "";
    public static String leaveAuth = "";



 public static ArrayList<EducationList> eduListResult = new ArrayList<EducationList>();

 public static ArrayList<JobList> jobListResult = new ArrayList<JobList>();

 public static ArrayList<TrainningList> trainListResult = new ArrayList<TrainningList>();


 public static String str_Bloodgp_addiDesc = "";
 public static String strWeight_addiDesc = "";
 public static String strHight_addiDesc = "";
 public static String stridMark1_addiDesc = "";
 public static String stridMark2_addiDesc = "";
 public static String strdisablelist_addiDesc = "";
 public static String strcrimeoffence_addiDesc = "";

 public static String emgName_emgcon = "";
 public static String strrelation_emgcon = "";
 public static String emgEmail_emgcon = "";
 public static String emgContc_emgcon = "";
 public static String emgAddress_emgcon = "";

 public static String sttitle_license = "";
 public static String stLicnNo_license = "";
 public static String stStartdate_license = "";
 public static String stEnddate_license = "";


 public static String adds1_pr = "";
 public static String adds2_pr = "";
 public static String roadname_pr = "";
 public static String city_pr = "";
 public static String Zipcode_pr = "";
 public static String country_pr = "";
 public static String doc_pr = "";

 public static String adds1_ps = "";
 public static String adds2_ps = "";
 public static String roadname_ps = "";
 public static String city_ps = "";
 public static String Zipcode_ps = "";
 public static String country_ps = "";
 public static String doc_ps = "";



 public static String id_Nat = "";
 public static String placeIssue_Nat = "";
 public static String issueDate_Nat = "";
 public static String expDate_Nat = "";
 public static String country_Nat = "";
 public static String countryofbirthNat = "";
 public static String docType_Nat = "";
 public static String doc_nat = "";

 public static String passprt_no = "";
 public static String country_Paspt = "";
 public static String placeofbirth_passpt = "";
 public static String issud_by_passpt = "";
 public static String issueDate_passpt = "";
 public static String expDate_passpt = "";
 public static String review_date_passpt = "";
 public static String statusPasspt = "";
 public static String currentpas_paspt = "";
 public static String remarks_passpt = "";
 public static String doc_paspt = "";


 public static String brp_noVisa = "";
 public static String country_Visa = "";
 public static String contyresiVisa = "";
 public static String issud_by_visa = "";
 public static String issueDate_visa = "";
 public static String expDate_visa = "";
 public static String review_date_visa = "";
 public static String statusVisa = "";
 public static String currentpas_pasptVisa = "";
 public static String remarks__visa = "";
 public static String doc_visa = "";


 public static String typeOfLisenceDriv = "";
 public static String licnNoDriv = "";
 public static String expryDateDriv = "";


    public static String empId_job = "";
    public static String emId_job = "";

    public static String empId_traing = "";
    public static String emId_traing = "";
}

