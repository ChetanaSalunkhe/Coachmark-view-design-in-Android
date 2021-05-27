package com.vritti.freshmart.Merchant_MM;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.freshmart.ProgressHUD;
import com.vritti.freshmart.R;
import com.vritti.freshmart.SplashActivity;
import com.vritti.freshmart.adapters.MerchCategoryAdapter;
import com.vritti.freshmart.adapters.MerchSubCategoryAdapter;
import com.vritti.freshmart.beans.AllCatSubcatItems;
import com.vritti.freshmart.classes.DatabaseHandler;
import com.vritti.freshmart.data.AnyMartData;
import com.vritti.freshmart.data.AnyMartDatabaseConstants;
import com.vritti.freshmart.database.DatabaseHelper;
import com.vritti.freshmart.interfaces.CallbackInterface;
import com.vritti.freshmart.utils.NetworkUtils;
import com.vritti.freshmart.utils.StartSession;
import com.vritti.freshmart.utils.Utilities;
import com.vritti.freshmart.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.util.ArrayList;

public class CategorySetting_new extends AppCompatActivity {
    Context parent;
    GridView catgrid,subcatgrid;
    LinearLayout laysubcat;
    ImageView refresh;
    Button btnclose,btngotit;
    TextView txtselsubcat;
    LinearLayout coach_mark_master_view;

    DatabaseHandler dbHandler;
    private static DatabaseHelper databaseHelper;
    static ProgressHUD progress;
    public String restoredText, restoredusername, restoredownername, usertype, domainname,CustomerID="",CustVendorMasterId="";
    SharedPreferences sharedpreferences;
    SQLiteDatabase sql_db;

    ArrayList<AllCatSubcatItems> catList;
    ArrayList<AllCatSubcatItems> subcatList;
    ArrayList<AllCatSubcatItems> mainArrayList;

    AllCatSubcatItems bean;
    MerchCategoryAdapter catAdapter;
    MerchSubCategoryAdapter subcatAdapter;
    String res = "";
    private static String json;
    String selCatId="";
    String finalJson = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_setting_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.category_sel)+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        init();

        if(AnyMartData.IsFamilyMasterLoadFirst == "Y"){

            if(databaseHelper.getSelectedFamilyCount(CategorySetting_new.this) >0){
                updateFlagFirstTime();
            }else {

                try{
                if (progress == null) {
                    progress = ProgressHUD.show(parent, ""+
                            parent.getResources().getString(R.string.loading), false, true, null);
                    progress.setCanceledOnTouchOutside(true);
                    progress.setCancelable(true);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

                getFromServer_new();
            }

        }else {
            getDataFromDatabase();
        }

        //test
        /*if (NetworkUtils.isNetworkAvailable(parent)) {
            new StartSession(parent, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetFamilyMasterData().execute();
                }

                @Override
                public void callfailMethod(String s) {

                }
            });
        }*/

        //onCoachMark();

        setListeners();
    }

    public void init(){
        parent = CategorySetting_new.this;

        catgrid=findViewById(R.id.catgrid);
        subcatgrid=findViewById(R.id.subcatgrid);
        laysubcat=findViewById(R.id.laysubcat);
        laysubcat.setVisibility(View.GONE);
        refresh=findViewById(R.id.refresh);
        btnclose=findViewById(R.id.btnclose);
        btngotit=findViewById(R.id.btngotit);
        txtselsubcat=findViewById(R.id.txtselsubcat);
        coach_mark_master_view=findViewById(R.id.coach_mark_master_view);

        dbHandler = new DatabaseHandler(CategorySetting_new.this);

        sharedpreferences = getSharedPreferences(SplashActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        restoredText = sharedpreferences.getString("Mobileno", null);
        //restoredownername = sharedpreferences.getString("OwnerName", null);
        restoredusername = sharedpreferences.getString("username", null);
        usertype = sharedpreferences.getString("usertype", null);
        domainname = sharedpreferences.getString("companyURL_LOGO", null);
        restoredownername = sharedpreferences.getString("companyURL_LOGO", null);
        AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL", null);
        AnyMartData.CompanyURL = sharedpreferences.getString("companyurlmain", null);
        CustVendorMasterId = sharedpreferences.getString("CustVendorMasterId", null);
        CustomerID = sharedpreferences.getString("CustVendorMasterId", null);
        AnyMartDatabaseConstants.DATABASE__NAME_URL = sharedpreferences.getString("DatabaseName", null);
        AnyMartData.LoginId = sharedpreferences.getString("LoginId", null);
        AnyMartData.Password = sharedpreferences.getString("Password", null);
        AnyMartData.SPECIALITY = sharedpreferences.getString("SPECIALITY", "");
        AnyMartData.IsFamilyMasterLoadFirst = sharedpreferences.getString("onRefresh", "Y");
        AnyMartData.instr_catSettiing_flag = sharedpreferences.getBoolean("catSettInstr", false);

        if( AnyMartData.instr_catSettiing_flag == false){
            coach_mark_master_view.setVisibility(View.VISIBLE);
        }else {
            coach_mark_master_view.setVisibility(View.GONE);
        }

        databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        sql_db = databaseHelper.getWritableDatabase();

        catList = new ArrayList<AllCatSubcatItems>();
        subcatList = new ArrayList<AllCatSubcatItems>();
        mainArrayList = new ArrayList<AllCatSubcatItems>();
    }

    public void setListeners(){

        btngotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnyMartData.instr_catSettiing_flag = true;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("catSettInstr", AnyMartData.instr_catSettiing_flag);
                editor.commit();

                coach_mark_master_view.setVisibility(View.GONE);
            }
        });

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laysubcat.setVisibility(View.GONE);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(parent)) {
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetFamilyMasterData().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            }
        });

        /*catgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                laysubcat.setVisibility(View.VISIBLE);
                selCatId = catList.get(position).getCategoryId();

               // getSubCatData();

            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDataFromDatabase(){
        catList.clear();
        subcatList.clear();

        String qry = "Select distinct CategoryId, CategoryName,Cat_flag,CatImgPath from "
                + AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA + " Order by CategoryName ASC";
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            try {
                do{
                    int subcatcount = 0;
                    int itemcount = 0;
                    String cat_name = c.getString(c.getColumnIndex("CategoryName"));
                    String cat_id = c.getString(c.getColumnIndex("CategoryId"));
                    String Cat_flag = c.getString(c.getColumnIndex("Cat_flag"));
                    String CatImgPath = c.getString(c.getColumnIndex("CatImgPath"));

                    try{
                        if(Cat_flag.equalsIgnoreCase("Y")){
                            Cat_flag="Y";
                        }else {
                            Cat_flag = "N";
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Cat_flag = "N";
                    }

                    bean = new AllCatSubcatItems();
                    bean.setCategoryId(cat_id);
                    bean.setCategoryName(cat_name);
                    bean.setCatImgPath(CatImgPath);
                    bean.setCat_flag(Cat_flag);
                    if(Cat_flag.equalsIgnoreCase("Y")){
                        bean.setChecked(true);
                    }else {
                        bean.setChecked(false);
                    }

                    String c_cnt = "Select distinct SubCategoryId from "+AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA+" " +
                            "WHERE CategoryId='"+cat_id+"'";
                    Cursor ccnt=sql_db.rawQuery(c_cnt,null);
                    if(ccnt.getCount()>0){
                        ccnt.moveToFirst();
                        try{
                            subcatcount =ccnt.getCount();
                            bean.setSubcatcount(subcatcount);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    catList.add(bean);

                    String qry_subcat = "Select distinct SubCategoryId,SubCategoryName,SubCat_flag,SubCatImgPath,CatImgPath from "
                            + AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA + "" +
                            " where CategoryId='" + cat_id + "' Order by SubCategoryName ASC";
                    Cursor c_subcat = sql_db.rawQuery(qry_subcat, null);
                    if(c_subcat.getCount()>0){
                        c_subcat.moveToFirst();
                        try{
                            do{
                                subcatcount = c_subcat.getCount();
                                String subcat_name = c_subcat.getString(c_subcat.getColumnIndex("SubCategoryName"));
                                String subcat_id = c_subcat.getString(c_subcat.getColumnIndex("SubCategoryId"));
                                String subcat_flag = c_subcat.getString(c_subcat.getColumnIndex("SubCat_flag"));
                                String SubCatImgPath = c_subcat.getString(c_subcat.getColumnIndex("SubCatImgPath"));
                                CatImgPath = c_subcat.getString(c_subcat.getColumnIndex("CatImgPath"));

                                try{
                                    if(subcat_flag.equalsIgnoreCase("Y")){
                                        subcat_flag="Y";
                                    }else {
                                        subcat_flag = "N";
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();

                                    if(Cat_flag.equalsIgnoreCase("Y")){
                                        subcat_flag = "Y";
                                    }else {
                                        subcat_flag = "N";
                                    }
                                }

                                bean.setSubCategoryName(subcat_name);
                                bean.setSubCategoryId(subcat_id);
                                bean.setSubCatFlag(subcat_flag);
                                bean.setSubCatImgPath(SubCatImgPath);
                                bean.setCatImgPath(CatImgPath);
                                if(subcat_flag.equalsIgnoreCase("Y")){
                                    bean.setChecked(true);
                                }else {
                                    bean.setChecked(false);
                                }

                                subcatList.add(bean);

                            }while (c_subcat.moveToNext());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }while (c.moveToNext());

            }catch (Exception e){
                e.printStackTrace();
            }

            //display cat list to listview
            catAdapter = new MerchCategoryAdapter(CategorySetting_new.this,catList);
            catgrid.setAdapter(catAdapter);
            //setGridViewHeightBasedOnChildren(catgrid,3);

        }
    }

    public void getSubCatData(String catId,String catname){
        laysubcat.setVisibility(View.VISIBLE);
        selCatId = catId;

        txtselsubcat.setText(catname+" - "+getResources().getString(R.string.selectsubcat));

        subcatList.clear();

        String qry_subcat = "Select distinct SubCategoryId,SubCategoryName,SubCat_flag,SubCatImgPath,CatImgPath from "
                + AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA + "" +
                " where CategoryId='" + selCatId + "' Order by SubCategoryName ASC";
        Cursor c_subcat = sql_db.rawQuery(qry_subcat, null);
        if(c_subcat.getCount()>0){
            c_subcat.moveToFirst();
            try{
                do{
                    String subcat_name = c_subcat.getString(c_subcat.getColumnIndex("SubCategoryName"));
                    String subcat_id = c_subcat.getString(c_subcat.getColumnIndex("SubCategoryId"));
                    String subcat_flag = c_subcat.getString(c_subcat.getColumnIndex("SubCat_flag"));
                    String SubCatImgPath = c_subcat.getString(c_subcat.getColumnIndex("SubCatImgPath"));
                    String CatImgPath = c_subcat.getString(c_subcat.getColumnIndex("CatImgPath"));

                    bean = new AllCatSubcatItems();
                    bean.setSubCategoryName(subcat_name);
                    bean.setSubCategoryId(subcat_id);
                    bean.setSubCatFlag(subcat_flag);
                    bean.setSubCatImgPath(SubCatImgPath);
                    bean.setCatImgPath(CatImgPath);

                    subcatList.add(bean);

                }while (c_subcat.moveToNext());
            }catch (Exception e){
                e.printStackTrace();
            }

            subcatAdapter = new MerchSubCategoryAdapter(CategorySetting_new.this,subcatList);
            subcatgrid.setAdapter(subcatAdapter);
        }
    }

    public void updateSubcatData(boolean key, String flag,String catId){
        //set all subcat of selected category checked , unchecked
        //set flag as Y or N as per selection

        SQLiteDatabase sql_db = databaseHelper.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put("Cat_flag",flag);
        values1.put("SubCat_flag",flag);

        sql_db.update(AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA, values1, "CategoryId=?", new String[]{catId});

        //callgetdatafromdatabase

        //send data to server
        PostCategoryToServer(catId);
       // call();
    }

    public void updateSubcatData_fromsubcat(boolean key, String flag,String subcatId){
        //set all subcat of selected category checked , unchecked
        //set flag as Y or N as per selection

        SQLiteDatabase sql_db = databaseHelper.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put("SubCat_flag",flag);

        sql_db.update(AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA, values1, "SubCategoryId=?", new String[]{subcatId});

        postSubCatToServer(subcatId,flag);

    }

    public void call(){
        getDataFromDatabase();
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        try{
            ListAdapter listAdapter = gridView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int items = listAdapter.getCount();
            int rows = 0;

            View listItem = listAdapter.getView(0, null, gridView);
            listItem.measure(0, 0);
            totalHeight = listItem.getMeasuredHeight();

            float x = 1;
            if( items > columns ){
                x = items/columns;
                rows = (int) (x+1);
                totalHeight *= rows;
            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHeight;
            gridView.setLayoutParams(params);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class GetFamilyMasterData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_list = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                progress = ProgressHUD.show(parent,
                        ""+getResources().getString(R.string.loading_cat), false, true, null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER +
                    "?handler=" + AnyMartData.HANDLE +
                    "&sessionid=" + AnyMartData.SESSION_ID+
                    "&MerchId=" + CustVendorMasterId+
                    "&BSegmentid=" + AnyMartData.SPECIALITY;

            String getcatSubItems = url_getCategory_List;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {

                res = Utility.OpenconnectionOrferbilling(url_getCategory_List, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                /*responseString = stringBuff_getItems.toString().replaceAll("^\"|\"$", "");*/
                response_list = responseString.replaceAll("\\\\", "");
                System.out.println("resp =" + response_list);

            } catch (Exception e) {
                response_list = "error";
                e.printStackTrace();
            }
            return response_list;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (response_list.equalsIgnoreCase("Session Expired")) {
                if (NetworkUtils.isNetworkAvailable(parent)) {
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetFamilyMasterData().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            } else if (response_list.equalsIgnoreCase("error")) {
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
            } else {
                json = response_list;
                parseJson_FamilyMaster(json);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson_FamilyMaster(String json) {

        // Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS);
        Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA);
        catList.clear();
        subcatList.clear();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                String catImgPath = "",SubcatImgPath = "";

                catImgPath  =  jsonArray.getJSONObject(i).getString("CatImgPath");
                SubcatImgPath =  jsonArray.getJSONObject(i).getString("SubCatImgPath");

                if(catImgPath.equalsIgnoreCase("") || catImgPath.equalsIgnoreCase(null)){
                    catImgPath = "";
                }else {
                    catImgPath = AnyMartData.CompanyURL+"/images/"+catImgPath;
                }

                if(SubcatImgPath.equalsIgnoreCase("") || SubcatImgPath.equalsIgnoreCase(null)){
                    SubcatImgPath = "";
                }else {
                    SubcatImgPath = AnyMartData.CompanyURL+"/images/"+SubcatImgPath;
                }

                databaseHelper.addFamilyMaster(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        jsonArray.getJSONObject(i).getString("SubCategoryId"),
                        jsonArray.getJSONObject(i).getString("SubCategoryName"),
                        "",catImgPath,SubcatImgPath,"","");
            }

           /* try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }*/

            if(databaseHelper.getSelectedFamilyCount(CategorySetting_new.this) >0){
               updateFlagFirstTime();

                try{
                    progress.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                getFromServer_new();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PostCategoryToServer(String catId){

        try{
            progress = ProgressHUD.show(parent,
                    ""+getResources().getString(R.string.loading), false, true, null);
            progress.setCanceledOnTouchOutside(true);
            progress.setCancelable(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            //create Json
            String SubcatId="",IsActive="";
            JSONArray jsonArray1 = new JSONArray();
            JSONObject jobj;

            String qry = "Select distinct SubCategoryId,SubCat_flag from "+AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA+" WHERE CategoryId='"+catId+"'";
            Cursor c = sql_db.rawQuery(qry,null);
            if(c.getCount()>0){
                c.moveToFirst();
                do{
                    SubcatId = c.getString(c.getColumnIndex("SubCategoryId"));
                    IsActive = c.getString(c.getColumnIndex("SubCat_flag"));

                    jobj = new JSONObject();
                    try {
                        jobj.put("SubcatId", SubcatId);
                        jobj.put("IsActive", IsActive);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jsonArray1.put(jobj);

                }while (c.moveToNext());
            }

            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("CustVendorMasterId", CustomerID);
                jsonObject1.put("SubCategoryIdArr", jsonArray1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            finalJson = jsonObject1.toString();

            if (NetworkUtils.isNetworkAvailable(CategorySetting_new.this)) {
                new StartSession(CategorySetting_new.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new PostSelectedCategory().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();

            try{
                progress.dismiss();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }

        call();
    }

    public void postSubCatToServer(String subcatId,String flag){
        finalJson = "";

        try{
            progress = ProgressHUD.show(parent,
                    ""+getResources().getString(R.string.sending), false, true, null);
            progress.setCanceledOnTouchOutside(true);
            progress.setCancelable(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            //create Json
            String SubcatId="",IsActive="";
            JSONArray jsonArray1 = new JSONArray();
            JSONObject jobj;

            jobj = new JSONObject();
            try {
                jobj.put("SubcatId", subcatId);
                jobj.put("IsActive", flag);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray1.put(jobj);

            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("CustVendorMasterId", CustomerID);
                jsonObject1.put("SubCategoryIdArr", jsonArray1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            finalJson = jsonObject1.toString();

            if (NetworkUtils.isNetworkAvailable(CategorySetting_new.this)) {
                new StartSession(CategorySetting_new.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new PostSelectedCategory().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();

            try{
                progress.dismiss();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }

        call();
    }

    public class PostSelectedCategory extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "";
        String from = "";
        String response_getorderdetails = "";
        Object resp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*try{
                progress = ProgressHUD.show(parent,
                        ""+getResources().getString(R.string.loading), false, true, null);
                progress.setCanceledOnTouchOutside(true);
                progress.setCancelable(true);
            }catch (Exception e){
                e.printStackTrace();
            }*/

        }

        @Override
        protected Void doInBackground(Void... params) {

            String url_itemList = AnyMartData.MAIN_URL + AnyMartData.api_post_selectedCategory;

            try {
                finalJson = finalJson.toString();
                finalJson = finalJson.replaceAll("\\\\", "");
                resp = Utility.OpenPostConnection(url_itemList, finalJson);
                responseString = resp.toString();
                responseString = responseString.replaceAll("\"", "");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

            if (responseString == null || responseString.equalsIgnoreCase("error")) {

            } else if (responseString.equalsIgnoreCase("false") || responseString.equalsIgnoreCase("")) {
                Toast.makeText(CategorySetting_new.this, getResources().getString(R.string.unable_save_data), Toast.LENGTH_SHORT).show();
            } else if (responseString.equalsIgnoreCase("true")) {
                Toast.makeText(CategorySetting_new.this, getResources().getString(R.string.save_data), Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(CategorySetting_new.this,MerchantSelectedCategoryActivity.class));
               // finish();
            }
        }
    }

    /*__________________________________________________Coach Mark Instructional Layout_________________________________________*/

    public void onCoachMark(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.coach_mark);
        dialog.setCanceledOnTouchOutside(true);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.coach_mark_master_view);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class MyTextView extends android.support.v7.widget.AppCompatTextView {

        public MyTextView(Context context) {
            super(context);
            setTypeFace(0);
        }

        public MyTextView(Context context, AttributeSet attrs) {
            super(context, attrs);


            applyCustomFont(context,attrs);
        }

        public MyTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);

            applyCustomFont(context,attrs);
        }

        private void applyCustomFont(Context context, AttributeSet attrs) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CoachmarkArrow,
                    0, 0);
            int mTextPos=0;
            try {
             //   mTextPos = a.getInteger(R.styleable.CoachmarkArrow, 0);
            } finally {
                a.recycle();
            }
            setTypeFace(mTextPos);
        }

        private void setTypeFace(int mTextPos) {
            Typeface myTypeface = null;
            switch (mTextPos){
                case 0:
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
                    break;

                case 1:
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
                    break;
            }
            /*if(myTypeface!=null) {
                Logger.LogDebug("typeface ", String.valueOf(mTextPos));
                setTypeface(myTypeface);
            }*/
        }
    }

    public void getMySelectedCategoriesData(){
        if(databaseHelper.getSelectedFamilyCount(CategorySetting_new.this)>0){
            //then update flag and setcheckboxes
        }else {
            //call api from server and update checkboxes
            getFromServer_new();
        }
    }

    private void getFromServer_new() {

        if (NetworkUtils.isNetworkAvailable(CategorySetting_new.this)) {

            /*try{
                if (progress == null) {
                    progress = ProgressHUD.show(parent, ""+
                            parent.getResources().getString(R.string.loading), false, true, null);
                    progress.setCanceledOnTouchOutside(true);
                    progress.setCancelable(true);
                }
            }catch (Exception e){
                e.printStackTrace();
            }*/

            new StartSession(CategorySetting_new.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetSelectedFamilyMasterData().execute();
                }

                @Override
                public void callfailMethod(String s) {
                }
            });
        }
    }

    public class GetSelectedFamilyMasterData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_list = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*try{
                progress = ProgressHUD.show(parent,
                        ""+getResources().getString(R.string.loading_cat), false, true, null);
            }catch (Exception e){
                e.printStackTrace();
            }*/
        }

        @Override
        protected String doInBackground(String... params) {

            String url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.api_get_selectedCategory +
                    "?MerchId=" + CustomerID+"&Key=SalesFamily"+"&familyid=";

            String getcatSubItems = url_getCategory_List;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {

                res = Utility.OpenconnectionOrferbilling(url_getCategory_List, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                response_list = responseString.replaceAll("\\\\", "");
                System.out.println("resp =" + response_list);

            } catch (Exception e) {
                response_list = "error";
                e.printStackTrace();
            }
            return response_list;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

           /* try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }*/

            if (response_list.equalsIgnoreCase("Session Expired")) {
                 try{
                    progress.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (response_list.equalsIgnoreCase("error")) {
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
                 try{
                  progress.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if(response_list.equalsIgnoreCase("No Data")){
                try{
                    progress.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
                getDataFromDatabase();
            }else {
                json = response_list;
                parseJson_selected(json);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson_selected(String json) {

        Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_MY_SELECTED_FAMILY_MASTERDATA);
        catList.clear();
        subcatList.clear();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                String catImgPath = "",SubcatImgPath = "";

                catImgPath  =  jsonArray.getJSONObject(i).getString("CatImgPath");
                SubcatImgPath =  jsonArray.getJSONObject(i).getString("SubCatImgPath");

                if(catImgPath.equalsIgnoreCase("") || catImgPath.equalsIgnoreCase(null)){
                    catImgPath = "";
                }else {
                    catImgPath = AnyMartData.CompanyURL+"/images/"+catImgPath;
                }

                if(SubcatImgPath.equalsIgnoreCase("") || SubcatImgPath.equalsIgnoreCase(null)){
                    SubcatImgPath = "";
                }else {
                    SubcatImgPath = AnyMartData.CompanyURL+"/images/"+SubcatImgPath;
                }

                databaseHelper.addSelectedFamilyMaster(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        jsonArray.getJSONObject(i).getString("SubCategoryId"),
                        jsonArray.getJSONObject(i).getString("SubCategoryName"),
                        jsonArray.getJSONObject(i).getString("FamilyActive"),
                        catImgPath,SubcatImgPath);
            }

            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

            updateFlagFirstTime();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateFlagFirstTime(){
        String qnew = "Select distinct CategoryId from "+AnyMartDatabaseConstants.TABLE_MY_SELECTED_FAMILY_MASTERDATA;
        Cursor c = sql_db.rawQuery(qnew,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                String CategoryId = c.getString(c.getColumnIndex("CategoryId"));

                String Cat_flag="",SubCategoryId="";

                String flag = "Select distinct SubCategoryId,Cat_flag from "+AnyMartDatabaseConstants.TABLE_MY_SELECTED_FAMILY_MASTERDATA+
                        " WHERE CategoryId='"+CategoryId+"'";
                Cursor cflg = sql_db.rawQuery(flag,null);
                if(cflg.getCount()>0){
                    cflg.moveToFirst();
                    do{
                        Cat_flag = cflg.getString(cflg.getColumnIndex("Cat_flag"));
                        SubCategoryId  = cflg.getString(cflg.getColumnIndex("SubCategoryId"));

                        //update familymasterflags
                        ContentValues values = new ContentValues();
                        values.put("Cat_flag",Cat_flag);
                        values.put("SubCat_flag",Cat_flag);

                        sql_db.update(AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA,values,"SubCategoryId=?",
                                new String[]{SubCategoryId});

                    }while (cflg.moveToNext());
                }

            }while (c.moveToNext());

        }

        AnyMartData.IsFamilyMasterLoadFirst = "N";

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("onRefresh", AnyMartData.IsFamilyMasterLoadFirst);
        editor.commit();

        getDataFromDatabase();
    }

}
