package com.vritti.freshmart.Merchant_MM;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.vritti.freshmart.Merchant_MM.Adapter.MyCategoriesExpandableListAdapter;
import com.vritti.freshmart.Merchant_MM.Model.MerchantCategory;
import com.vritti.freshmart.Merchant_MM.Model.MerchantSelection;
import com.vritti.freshmart.Merchant_MM.Multipart.MerchantElearningActivity;
import com.vritti.freshmart.ProgressHUD;
import com.vritti.freshmart.R;
import com.vritti.freshmart.SplashActivity;
import com.vritti.freshmart.adapters.OpenOrderListAdapter;
import com.vritti.freshmart.adapters.RecentOrderedListAdapter;
import com.vritti.freshmart.adapters.Sliding_Image_Adapter;
import com.vritti.freshmart.adapters.URL_ListAdapter;
import com.vritti.freshmart.adapters.WishListAdapter;
import com.vritti.freshmart.beans.AllCatSubcatItems;
import com.vritti.freshmart.beans.Merchants_against_items;
import com.vritti.freshmart.beans.MyCartBean;
import com.vritti.freshmart.beans.OrderHistoryBean;
import com.vritti.freshmart.classes.Connectiondetector;
import com.vritti.freshmart.classes.DatabaseHandler;
import com.vritti.freshmart.classes.URL_Company_Domain;
import com.vritti.freshmart.customer.MyOrderHistory_Tabactivity;
import com.vritti.freshmart.data.AnyMartData;
import com.vritti.freshmart.data.AnyMartDatabaseConstants;
import com.vritti.freshmart.database.DatabaseHelper;
import com.vritti.freshmart.database.DatabaseHelper_URLStore;
import com.vritti.freshmart.interfaces.CallbackInterface;
import com.vritti.freshmart.utils.NetworkUtils;
import com.vritti.freshmart.utils.StartSession;
import com.vritti.freshmart.utils.Utilities;
import com.vritti.freshmart.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.vritti.freshmart.R.layout.app_update_lay;

//-------------------------------------Customer Main Activity------------------------------------------//
public class Merchant_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private static int newlistCount = 0;
    private static Context parent;
    public static ArrayList<AllCatSubcatItems> arrayList;
    public static ArrayList<MerchantCategory> arrayList1;
    public static ArrayList<OrderHistoryBean> historyBeanList;
    static ArrayList<OrderHistoryBean> newList = null;
    public static ArrayList<MyCartBean> arrayList_bean;
    private int backpressCount = 0;
    private AllCatSubcatItems bean;
    MerchantSelection merchantCategory;
    MerchantSelection.SubCategory subCategory;
    MerchantSelection.SubCategory.ItemList sub_subCategory;
    private MerchantCategory bean1;
    private static String json;
    private long back_pressed = 0;
    private static DatabaseHelper databaseHelper;
    static ProgressHUD progress;
    NavigationView navigationView;
    public String restoredText, restoredusername, restoredownername, usertype, domainname;
    SharedPreferences sharedpreferences;
    public static String Mobilenumber;
    TextView txtusername, txtmobileno, txtownername,txtappversion;
    ToggleButton account_view_icon_button;
    Button btngotit;
    LinearLayout coach_mark_master_view;
    String message, ataaleliurl;
    ArrayList<URL_Company_Domain> URL_list;
    //ArrayList<String> URL_list;
    private URL_Company_Domain bean_url;
    private DatabaseHelper_URLStore db_URLStore;
    SQLiteDatabase sql_db;
    URL_ListAdapter urlAdapter;
    ListView navheader_accountslist;
    String CustVendorMasterId, CustomerID;
    String res = "";

    //viewpager code
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    int[] imgflag;
    Button btnimage, btnimg, btn_categorySetting;
    final Handler handler = new Handler();
    public Timer swipeTimer;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    public ImageView iv;
    public static final int[] imgthumbIds ={R.drawable.banner_orange,R.drawable.banner_orange};
            /*{R.drawable.banner1,R.drawable.banner2,R.drawable.g6,R.drawable.g4,R.drawable.g2, R.drawable.g3, R.drawable.fruits1,
                    R.drawable.grocery_1, R.drawable.g1, R.drawable.statnry1,R.drawable.clean1,R.drawable.house1,R.drawable.beauty1,
                    R.drawable.beauty2};*/

    Sliding_Image_Adapter mycustmadapter;
    int i = 0;

    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    private ImageSwitcher imageSwitcher;
    private int animationCounter = 1;
    private Handler imageSwitcherHandler;
    TextView txt_showmore, noorder;
    ImageView imageview_goto;
    String cat_name, cat_id, custvendorID, PurDigit;
    WishListAdapter wAdapter;
    RecentOrderedListAdapter RAdapter;
    MyCartBean myCartBean;
    static OpenOrderListAdapter myOrderHistoryAdapter;
    //  static TextView pending_ordrcnt;
    String CopmanyURL;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    Connectiondetector cd;
    private String jimageview_mainon_1, jsonRate;
    private ArrayList<Merchants_against_items> merchants_against_itemsArrayList;
    private Merchants_against_items merchants_against_items;
    public static String today;
    DatabaseHandler dbHandler;
    private static String DateToStr;
    private String DateToString;
    private String DateToString_ack;
    private static int index = 0;
    private Uri imageUri;
    Dialog dialog;

    // ArrayList<MerchantCategory> categoryList1;
    ArrayList<MerchantSelection> categoryList1;
    ArrayList<MerchantSelection> categoryList;
    ArrayList<MerchantSelection.SubCategory> list = new ArrayList<MerchantSelection.SubCategory>();
    ArrayList<MerchantSelection.SubCategory> sub_categoryList;
    ArrayList<MerchantSelection.SubCategory> subcategories;
    ArrayList<MerchantSelection.SubCategory.ItemList> sub_subCategories;
    boolean isFirstViewClick = false;
    boolean isSecondViewClick = false;
    //LinearLayout linear_listview;
    ExpandableListView linear_listview;
    LinearLayout viewpagerlayout;
    ArrayList<MerchantCategory.SubCategory.ItemList> mItemListArray = new ArrayList<MerchantCategory.SubCategory.ItemList>();
    CheckBox check, sub_check;
    TextView tvParent, tvChild;
    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    private MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter;
    String finalJson = "";
    LinearLayout soappr,shipment_invoice,pending_deliveries,transit_shipments,
            catsel,priceList,del_charges,itemcreation,item_report,merchpay_tovsl,merchpay_history,elearning,myallorders;

    Timer timer;
    final long DELAY_MS = 5000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;
    TextView txtupdateversion,btnupdateversion;
    LinearLayout lay_update_app;
    String pathVideo = "";
    VideoView videoView;

    int playstoreflag = 0;
    String diayn="", dicurDate="";
    String TODAYDATE, FLAG_PSTORE;
    String dialogopen = "no";
    static int Year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initialize();

        //callforplayStore();

        if (diayn.equalsIgnoreCase("YesDialog")) {
            Date date = new Date();
            final Calendar c = Calendar.getInstance();

            Year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            String New_TodayDate = day+1 + "-" + (month + 1) + "-" + Year;

            if (New_TodayDate.equalsIgnoreCase(dicurDate)) {
                FLAG_PSTORE = "DONT_SHOW_DIALOG";
            } else {
                FLAG_PSTORE = "SHOW_DIALOG";
            }

            if (FLAG_PSTORE.equalsIgnoreCase("SHOW_DIALOG")) {
                //if date not matched then call
                callforplayStore();
            } else {
                //for forcefully update
                 callforplayStore();
            }
        }else {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Dialog", "YesDialog");
            editor.apply();
        }

        if (databaseHelper.getFamilyDataCount(Merchant_MainActivity.this) > 0) {
            getDataFromDataBase();

        } else {
            //get data of category
            getDataFromServer();

            AnyMartData.LOCAL_NAME=sharedpreferences.getString("local","");

            if (AnyMartData.LOCAL_NAME.equals("")) {
                if (NetworkUtils.isNetworkAvailable(Merchant_MainActivity.this)) {
                    new StartSession(Merchant_MainActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadMerchantData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }

                    });
                }
            }
        }

        setListeners();
    }

    /////////////////////////////////addmerchant database code ///////////////////////////////

    public String get_date(String d) {

        String finalDate;
        if (!(d.equals("") || d == null)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

            Date myDate = null;
            try {
                myDate = dateFormat.parse(d);
                //myDate = readFormat.parse(d);
                System.out.println("..........value of my date after conv" + myDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd yyyy");
            finalDate = timeFormat.format(myDate);

           /* String formattedDate = "";
            if( myDate != null ) {
                finalDate = writeFormat.format( myDate );
            }

            System.out.println(finalDate);*/

        } else {
            finalDate = "";
        }

        return finalDate;
    }

    public static boolean compare_date(String fromdate, String todate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("MMM dd yyyy");

        today = dfDate.format(new Date());
        try {
            if ((dfDate.parse(today).after(dfDate.parse(fromdate)) ||
                    dfDate.parse(today).equals(dfDate.parse(fromdate))) &&
                    (dfDate.parse(today).before(dfDate.parse(todate)) ||
                            dfDate.parse(today).equals(dfDate.parse(todate)))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    private String getFreeItemname(String id) {
        DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase db = db1.getWritableDatabase();
        String que = "Select  ItemName  from "
                + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS +
                " where ItemMasterId='" + id + "'";
        String itemname;
        Cursor c = db.rawQuery(que, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            itemname = c.getString(c.getColumnIndex("ItemName"));
        } else {
            itemname = "";
        }

        return itemname;
    }

  /*  private void getDataFromDataBase_addmerchant() {
        // TODO Auto-generated method stub
        arrayList.clear();

        DatabaseHelper db1 = new DatabaseHelper(parent);
        SQLiteDatabase db = db1.getWritableDatabase();

        Cursor c = db.rawQuery("Select distinct MerchantId,MerchantName from "
                + AnyMartDatabaseConstants.TABLE_MERCHANTS, null);


        if (c.getCount() > 0) {
            c.moveToFirst();
            String id = c.getString(c.getColumnIndex("MerchantId"));
            String M_name = c.getString(c.getColumnIndex("MerchantName"));
            do {


                bean = new Merchants_against_items();
                bean.setMerchant_name(c.getString(c.getColumnIndex("MerchantName")));
                bean.setMerchant_id(c.getString(c.getColumnIndex("MerchantId")));

                Cursor c1 = db.rawQuery("Select MerchantId,MerchantName,qnty,minqnty,offers,price," +
                        "  Product_name,Freeitemqty,Freeitemname,validfrom,validto from "
                        + AnyMartDatabaseConstants.TABLE_MERCHANTS +
                        " where MerchantId='" + id + "'", null);


                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    try {
                        do {
                            bean.setQnty(c1.getInt(c1.getColumnIndex("qnty")));
                            bean.setMinqnty(c1.getInt(c1.getColumnIndex("minqnty")));
                            bean.setOffers(c1.getString(c1.getColumnIndex("offers")));
                            bean.setPrice(c1.getFloat(c1.getColumnIndex("price")));
                            bean.setProduct_name(c1.getString(c1.getColumnIndex("Product_name")));
                            bean.setFreeitemqty(c1.getInt(c1.getColumnIndex("Freeitemqty")));
                            bean.setFreeitemname(c1.getString(c1.getColumnIndex("Freeitemname")));
                            bean.setValidfrom(c1.getString(c1.getColumnIndex("validfrom")));
                            bean.setValidto(c1.getString(c1.getColumnIndex("validto")));

                        } while (c1.moveToNext());

                    } finally {

                    }
                }

                arrayList.add(bean);

            } while (c.moveToNext());

        }

       *//* regMarchantList.setAdapter(new AddRegularMerchantAdapter
                (AddRegularMerchants.this, arrayList, this));*//*
    }*/

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Imageviewer thread
    Thread t = new Thread() {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(3500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            iv.setImageResource(imgthumbIds[i]);
                            overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                            i++;
                            if (i >= imgthumbIds.length) {
                                i = 0;
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private void hideItem() {
        Menu nav_Menu = navigationView.getMenu();
    }

    private void initialize() {
        parent = Merchant_MainActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        iv = (ImageView) findViewById(R.id.imageview_main);
        i = 0;
        t.start();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.home)+"</small>"));

        btn_categorySetting = (Button) findViewById(R.id.btn_categorySetting);
        btn_categorySetting.setVisibility(View.GONE);
        linear_listview = findViewById(R.id.linear_listview);
        viewpagerlayout = findViewById(R.id.viewpagerlayout);
        soappr = findViewById(R.id.soappr);
        shipment_invoice = findViewById(R.id.shipment_invoice);
        pending_deliveries = findViewById(R.id.pending_deliveries);
        transit_shipments = findViewById(R.id.transit_shipments);
        txtupdateversion =  findViewById(R.id.txtupdateversion);
        btnupdateversion =  findViewById(R.id.btnupdateversion);
        txtupdateversion.setVisibility(View.INVISIBLE);
        btnupdateversion.setVisibility(View.INVISIBLE);
        lay_update_app = findViewById(R.id.lay_update_app);
        catsel = findViewById(R.id.catsel);
        priceList = findViewById(R.id.priceList);
        del_charges = findViewById(R.id.del_charges);
        itemcreation = findViewById(R.id.itemcreation);
        item_report = findViewById(R.id.item_report);
        merchpay_tovsl = findViewById(R.id.merchpay_tovsl);
        merchpay_history = findViewById(R.id.merchpay_history);
        elearning = findViewById(R.id.elearning);
        myallorders = findViewById(R.id.myallorders);
        btngotit = findViewById(R.id.btngotit);
        coach_mark_master_view = findViewById(R.id.coach_mark_master_view);

        databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        sql_db = databaseHelper.getWritableDatabase();

        arrayList = new ArrayList<AllCatSubcatItems>();
        arrayList1 = new ArrayList<MerchantCategory>();
        arrayList_bean = new ArrayList<MyCartBean>();
        historyBeanList = new ArrayList<OrderHistoryBean>();
        subcategories = new ArrayList<MerchantSelection.SubCategory>();
        categoryList = new ArrayList<>();
        sub_categoryList = new ArrayList<>();
        sub_subCategories = new ArrayList<MerchantSelection.SubCategory.ItemList>();

        URL_list = new ArrayList<URL_Company_Domain>();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,imgthumbIds);
        viewPager.setAdapter(viewPagerAdapter);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES-1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                if (currentPage >= imgthumbIds.length) {
                    currentPage = 0;
                }else {
                    //play video
                    /*try{
                        pathVideo = "android.resource://" + getPackageName() + "/" + R.raw.baneer_vid1;
                        videoView.setVideoURI(Uri.parse(pathVideo));
                        videoView.start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/

                }
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //  toggle.setHomeAsUpIndicator(R.drawable.hamburger_1);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().setGroupVisible(R.id.group_1_drawrmenu_one, true);
        navigationView.getMenu().setGroupVisible(R.id.group_2_switchvendor, false);
        navigationView.getMenu().findItem(R.id.communicate).setVisible(true);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        ataaleliurl = AnyMartData.URL;

        if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
            txtownername.setVisibility(View.VISIBLE);
            hideItem();
        }

        txtusername = (TextView) header.findViewById(R.id.txtusername);
        txtmobileno = (TextView) header.findViewById(R.id.txtmobileno);
        txtownername = (TextView) header.findViewById(R.id.txtownername);
        txtappversion = navigationView.findViewById(R.id.appversion);

        account_view_icon_button = (ToggleButton) header.findViewById(R.id.account_view_icon_button);
        account_view_icon_button.setVisibility(View.INVISIBLE);
        navheader_accountslist = (ListView) header.findViewById(R.id.list_urlnames);
        navheader_accountslist.setVisibility(View.GONE);

        getUrlListFromDataBase();

        dbHandler = new DatabaseHandler(Merchant_MainActivity.this);

        sharedpreferences = getSharedPreferences(SplashActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        restoredText = sharedpreferences.getString("Mobileno", null);
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
        AnyMartData.instr_merchHome_flag = sharedpreferences.getBoolean("merchHomeInstr", false);
        AnyMartData.LOCAL_NAME = restoredusername;
        playstoreflag = sharedpreferences.getInt("playstoreflag",0);
        diayn = sharedpreferences.getString("Dialog", "YesDialog");
        dicurDate = sharedpreferences.getString("TodaysDate", TODAYDATE);

        merchants_against_itemsArrayList = new ArrayList<Merchants_against_items>();
        categoryList1 = new ArrayList<MerchantSelection>();

        Intent intent = getIntent();
        if (intent.hasExtra("message")) {
            message = intent.getExtras().getString("message");
            if (message != null) {
                if (message.contains("confirmed") || message.contains("rejected")) {
                    databaseHelper.deleteNotification();
                    databaseHelper.deleteNotification_rejected();
                }
            }
        }

        if (restoredText != null) {
            Mobilenumber = restoredText;
            AnyMartData.MOBILE = restoredText;
        }
        if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
            txtmobileno.setVisibility(View.GONE);
        } else if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
            txtusername.setText(restoredusername);
            txtmobileno.setText(restoredText);
            txtownername.setText(restoredownername);
        }

         PackageManager manager = parent.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    parent.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        try {
            txtappversion.setText(getResources().getString(R.string.appversion)+ " "+ version);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(AnyMartData.instr_merchHome_flag == false){
            coach_mark_master_view.setVisibility(View.VISIBLE);
        }else {
            coach_mark_master_view.setVisibility(View.GONE);
        }
    }

    private void getUrlListFromDataBase() {

        // TODO Auto-generated method stub
        URL_list.clear();

       /* databaseHelper = new DatabaseHelper(parent,
                AnyMartDatabaseConstants.DATABASE__NAME_URL);*/

        // sql_db= databaseHelper.getWritableDatabase();

        db_URLStore = new DatabaseHelper_URLStore(parent);
        sql_db = db_URLStore.getWritableDatabase();

        Cursor c1 = sql_db.rawQuery("Select Url from "
                + AnyMartDatabaseConstants.TABLE_URL_COMPANYDOMAIN, null);
        Log.e("cnt", String.valueOf(c1));
        if (c1.getCount() > 0) {
            c1.moveToFirst();
            do {
                String urlname = c1.getString(c1.getColumnIndex("Url"));
            } while (c1.moveToNext());
            //url found
        } else {
            //no url found
        }

        Cursor c = sql_db.rawQuery("Select distinct Url, DBName, CustVendorMasterId,EnvMasterId,PlantMasterId from "
                + AnyMartDatabaseConstants.TABLE_URL_COMPANYDOMAIN, null);
        Log.e("cnt", String.valueOf(c));
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                bean_url = new URL_Company_Domain();
                //bean.setCompanyId(c.getString(c.getColumnIndex("MerchantName")));
                String urlname = c.getString(c.getColumnIndex("Url"));
                String DB_name = c.getString(c.getColumnIndex("DBName"));
                String CustVendorMasterId = c.getString(c.getColumnIndex("CustVendorMasterId"));
                String EnvMasterId = c.getString(c.getColumnIndex("EnvMasterId"));
                String PlantMasterId = c.getString(c.getColumnIndex("PlantMasterId"));

                //AnyMartData.EnvMasterId = EnvMasterId;
                //AnyMartData.PlantMasterId = PlantMasterId;
                String usr = "Select FullName,Mobile from " + AnyMartDatabaseConstants.TABLE_USER + " WHERE UserId='" + CustVendorMasterId + "'";
                Cursor cusr = sql_db.rawQuery(usr, null);
                if (cusr.getCount() > 0) {
                    cusr.moveToFirst();
                    do {
                        String usrname = cusr.getString(cusr.getColumnIndex("FullName"));
                        String mob = cusr.getString(cusr.getColumnIndex("Mobile"));

                        bean_url.setUserName(usrname);
                        bean_url.setMobile(mob);
                    } while (cusr.moveToNext());
                }

                bean_url.setUrlname(c.getString(c.getColumnIndex("Url")));
                bean_url.setDBName(c.getString(c.getColumnIndex("DBName")));
                bean_url.setCustVendorMasterId(c.getString(c.getColumnIndex("CustVendorMasterId")));
                bean_url.setEnvMasterId(c.getString(c.getColumnIndex("EnvMasterId")));
                bean_url.setPlantMasterId(c.getString(c.getColumnIndex("PlantMasterId")));
                URL_list.add(bean_url);
                //URL_list.add(urlname);
            } while (c.moveToNext());
        } else {
            /*bean_url = new URL_Company_Domain();
            bean_url.setCompanyId("No data");
            bean_url.setUrlname("No data");
            URL_list.add(bean_url);*/
        }

        urlAdapter = new URL_ListAdapter(parent, URL_list);
        navheader_accountslist.setAdapter(urlAdapter);
        //navheader_accountslist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setListViewHeightBasedOnItems(navheader_accountslist);

       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,
                android.R.layout.simple_list_item_1, URL_list);
        navheader_accountslist.setAdapter(adapter);*/
    }

    private void getDataFromServer() {

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
        } else {
            Toast.makeText(parent, ""+getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
            // callSnackbar();
        }
    }

    /*public void callSnackbar() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No iternet connection. Please try again later.", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new GetCategoryList().execute();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }*/

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson(String json) {
        Utilities.clearTable(parent,
                AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS);
        arrayList.clear();
        arrayList1.clear();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                //String PricelistId = jsonArray.getJSONObject(i).getString("PricelistId");
                String PricelistId = jsonArray.getJSONObject(i).getString("PricelistHdrId");
                String PricelistRate = jsonArray.getJSONObject(i).getString("PricelistRate");
                String MRP = jsonArray.getJSONObject(i).getString("MRP");

                String storeMRP;
                if (PricelistId.equalsIgnoreCase("")) {
                    storeMRP = MRP;
                } else {

                    if (PricelistRate.equalsIgnoreCase("0") ||
                            PricelistRate.equalsIgnoreCase("0.0")) {
                        storeMRP = MRP;
                    } else {
                        storeMRP = PricelistRate;
                    }
                }

                databaseHelper.addAllCatSubcatItems(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        jsonArray.getJSONObject(i).getString("SubCategoryId"),
                        jsonArray.getJSONObject(i).getString("SubCategoryName"),
                        jsonArray.getJSONObject(i).getString("itemmasterid"),
                        jsonArray.getJSONObject(i).getString("itemnaame"), "http://test1.ekatm.com/menshirts.jpg",
                        storeMRP,
                        jsonArray.getJSONObject(i).getString("custVendorname"),
                        /* jsonArray.getJSONObject(i).getString("TypeFixedPercent")*/"0",
                        jsonArray.getJSONObject(i).getString("validfrom"),
                        jsonArray.getJSONObject(i).getString("validto"),
                        jsonArray.getJSONObject(i).getString("DisRate"),
                        jsonArray.getJSONObject(i).getString("NetRate"),
                        jsonArray.getJSONObject(i).getString("Freeitemid"),
                        jsonArray.getJSONObject(i).getString("Freeitemqty"),
                        jsonArray.getJSONObject(i).getString("Minqty"),
                        jsonArray.getJSONObject(i).getString("Discratepercent"),
                        jsonArray.getJSONObject(i).getString("DiscrateMRP"),
                        jsonArray.getJSONObject(i).getString("PurDigit"),
                        jsonArray.getJSONObject(i).getString("CustVendorMasterId"),
                        jsonArray.getJSONObject(i).getString("PricelistHdrId"),
                        jsonArray.getJSONObject(i).getString("PricelistRate"),
                        "","","","","","",
                        "","","","",AnyMartData.CatImgPath, AnyMartData.SubCatImgPath,"",
                        "","","","","","","",
                        "","","","","","");
            }
            getDataFromDataBase();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getOpenOrderDataFromDatabase() {
        historyBeanList.clear();

        databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase sql = databaseHelper.getWritableDatabase();

        Cursor c = sql.rawQuery(
                "SELECT distinct SOHeaderId,ConsigneeName FROM " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY
                        + " WHERE status NOT IN ('Dispatched', 'Received') ORDER BY DoAck desc ",
                null);
        Log.d("test", "" + c.getCount());

        /*Mobile ='"+ AnyMartData.MOBILE + "'*/

        int ordercnt = 0;
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

                String orderid = c.getString(c
                        .getColumnIndex("SOHeaderId"));
                ordercnt = ordercnt + 1;

                OrderHistoryBean historybean = new OrderHistoryBean();

                historybean.setSOHeaderId(orderid);
                historybean.setConsigneeName(c.getString(c
                        .getColumnIndex("ConsigneeName")));
                SQLiteDatabase sql1 = databaseHelper.getWritableDatabase();
               /* Cursor c1 = sql1.rawQuery(
                        "SELECT distinct SODate , NetAmt, DODisptch, DORcvd, status, DoAck FROM "
                                + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE SOHeaderId ='"
                                + orderid + "' ORDER BY date(DoAck) desc ",
                        null);*/

                Cursor c2 = sql1.rawQuery(
                        "SELECT * FROM "
                                + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE SOHeaderId ='"
                                + orderid + "' ORDER BY date(DoAck) desc ",
                        null);

                Log.d("test", "" + c2.getCount());

                float amt = 0;
                if (c2.getCount() > 0) {
                    c2.moveToFirst();
                    do {
                        //   float amtofitem = c1.getFloat(c1.getColumnIndex("Rate"));
                        String o_date = c2.getString(c2.getColumnIndex("SODate"));
                        //   amt = amt + amtofitem;

                        historybean.setSODate(o_date);
                        // historybean.setRate(Float.parseFloat(c2.getString(c2.getColumnIndex("NetAmt"))));
                        historybean.setDoAck(c2.getString(c2.getColumnIndex("DoAck")));
                        historybean.setStatus(c2.getString(c2.getColumnIndex("status")));

                    } while (c2.moveToNext());
                }
                historyBeanList.add(historybean);

            } while (c.moveToNext());
        } else {

        }
        // myOrderHistoryAdapter = new MyOrderHistoryAdapter(MyOrderHistory.this, arrayList);
        // listview_my_orders_history.setAdapter(myOrderHistoryAdapter);
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        arrayList.clear();
        arrayList1.clear();

        categoryList = new ArrayList<>();
        categoryList1 = new ArrayList<MerchantSelection>();
        sub_subCategories = new ArrayList<MerchantSelection.SubCategory.ItemList>();
        subcategories = new ArrayList<MerchantSelection.SubCategory>();

        sql_db = databaseHelper.getWritableDatabase();

        String que = "Select distinct CategoryId, CategoryName from "
                + AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA;
        Cursor c = sql_db.rawQuery(que, null);
        Log.d("test", "" + c.getCount());
        if (c.getCount() > 1) {
            c.moveToFirst();

            try {
                do {
                    int subcatcount = 0;
                    int itemcount = 0;
                    String cat_name = c.getString(c.getColumnIndex("CategoryName"));
                    String cat_id = c.getString(c.getColumnIndex("CategoryId"));

                    bean = new AllCatSubcatItems();
                    merchantCategory = new MerchantSelection();

                    bean.setCategoryId(cat_id);
                    bean.setCategoryName(cat_name);

                    merchantCategory.setCategoryId(cat_id);
                    merchantCategory.setCategoryName(cat_name);

                    String cq_1= "Select SubCategoryId from "+AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA+" WHERE CategoryId='"+cat_id+"'";
                    Cursor c1=sql_db.rawQuery(cq_1,null);
                    if(c1.getCount()>0){
                        c1.moveToFirst();
                            bean.setSubcatcount(c1.getCount());
                            merchantCategory.setSubCatCnt(c1.getCount());
                    }else {

                    }

                    categoryList1.add(merchantCategory);

                    Cursor cursor = sql_db.rawQuery("Select distinct SubCategoryName,SubCategoryId from "
                            + AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA + "" +
                            " where CategoryName='" + cat_name + "'", null);
                    Log.d("test", "" + cursor.getCount());
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        try {
                            do {
                                subcatcount = cursor.getCount();
                                String subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
                                String subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));

                                subCategory = new MerchantSelection.SubCategory();
                                bean.setSubCategoryName(subcat_name);
                                bean.setSubCategoryId(subcat_id);
                                bean.setSubcatcount(subcatcount);
                                subCategory.setPsubCatId(subcat_id);
                                subCategory.setpSubCatName(subcat_name);
                                subCategory.setCategory_Id(cat_id);
                                subcategories.add(subCategory);

                                /*Cursor cursor1 = sql_db.rawQuery("Select distinct itemmasterid,ItemName,ItemImgPath" +
                                        " from " + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS +
                                        " where SubCategoryId='" + subcat_id + "'", null);

                                Log.d("test", "" + cursor1.getCount());
                                if (cursor1.getCount() > 0) {
                                    cursor1.moveToFirst();
                                    try {
                                        do {

                                            itemcount = cursor1.getCount();
                                            sub_subCategory = new MerchantSelection.SubCategory.ItemList();
                                            bean.setItemMasterId(cursor1.getString(cursor1.getColumnIndex("itemmasterid")));
                                            bean.setItemName(cursor1.getString(cursor1.getColumnIndex("ItemName")));
                                            bean.setItemImgPath(cursor1.getString(cursor1.getColumnIndex("ItemImgPath")));
                                            bean.setItemcount(itemcount);


                                            sub_subCategory.setItemId(cursor1.getString(cursor1.getColumnIndex("itemmasterid")));
                                            sub_subCategory.setItemName(cursor1.getString(cursor1.getColumnIndex("ItemName")));
                                            sub_subCategory.setSubCategory_Id(subcat_id);
                                            *//*sub_subCategory.setItemImgPath(cursor1.getString(cursor1
                                                    .getColumnIndex("ItemImgPath")));
*//*

                                            sub_subCategories.add(sub_subCategory);
                                            //  arrayList.add(bean);

                                        } while (cursor1.moveToNext());
                                    } finally {
                                        cursor1.close();
                                    }
                                }*/
                            }
                            while (cursor.moveToNext());
                   //         cursor.close();

                        } finally {

                        }
                    }
                    arrayList.add(bean);
                    // arrayList1.add(merchantCategory);
                } while (c.moveToNext());
            } finally {

            }
        }
    }

    private void setListeners() {

        btngotit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AnyMartData.instr_merchHome_flag = true;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("merchHomeInstr", AnyMartData.instr_merchHome_flag);
                editor.commit();

                coach_mark_master_view.setVisibility(View.GONE);
            }
        });

        catsel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent1 = new Intent(getApplicationContext(), Category_SelectionActivity.class);
                Intent intent1 = new Intent(getApplicationContext(), CategorySetting_new.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });

        priceList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent1 = new Intent(getApplicationContext(), MerchantSelectedCategoryActivity.class);
                Intent intent1 = new Intent(getApplicationContext(), MySelectedSalesFamily.class);
                startActivity(intent1);
            }
        });

        del_charges.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), DeliveryChargesActivity.class);
                startActivity(intent1);
            }
        });

        itemcreation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ItemCreationActivity.class);
                startActivity(intent1);
            }
        });

        item_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MerchantItemReortActivity.class);
                startActivity(intent1);
            }
        });

        merchpay_tovsl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MerchantPayment.class);
                startActivity(intent1);
            }
        });

        merchpay_history.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),MerchantPaymentHistoryActivity.class);
                startActivity(intent1);
            }
        });

        elearning.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MerchantElearningActivity.class);
                startActivity(intent1);
            }
        });

        myallorders.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MyOrderHistory_Tabactivity.class);
                intent1.putExtra("appName","V");
                startActivity(intent1);
            }
        });

        btnupdateversion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.vritti.freshmart")));

                /*Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.vritti.freshmart"));
                startActivity(intent);*/
            }
        });

        soappr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Merchant_MainActivity.this, SOApproveActivity.class);
                startActivity(intent1);
            }
        });
        shipment_invoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Merchant_MainActivity.this, PackagingInvoiceActivity.class);
                startActivity(intent1);
            }
        });

        pending_deliveries.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Merchant_MainActivity.this, PendingDeliveries.class);
                startActivity(intent1);
            }
        });

        transit_shipments.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Merchant_MainActivity.this, TransitShipmentActivity.class);
                startActivity(intent1);
            }
        });

        account_view_icon_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    //account_view_icon_button.setBackgroundResource(R.drawable.up_triangle);
                    navheader_accountslist.setVisibility(View.VISIBLE);
                    navigationView.getMenu().setGroupVisible(R.id.group_1_drawrmenu_one, false);
                    navigationView.getMenu().setGroupVisible(R.id.group_2_switchvendor, true);
                    navigationView.getMenu().findItem(R.id.communicate).setVisible(false);
                    // Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                } else {
                    //account_view_icon_button.setBackgroundResource(R.drawable.down_triangle);

                    navheader_accountslist.setVisibility(View.GONE);
                    navigationView.getMenu().setGroupVisible(R.id.group_1_drawrmenu_one, true);
                    navigationView.getMenu().setGroupVisible(R.id.group_2_switchvendor, false);
                    navigationView.getMenu().findItem(R.id.communicate).setVisible(true);
                }

                // mAdapter.setUseAccountMode(isChecked);
            }
        });

     /*   listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                AnyMartData.selectedCategoryName =
                        arrayList.get(position).getCategoryName();
                Intent intent = new Intent(parent, MerchantSubCategoryListActivity.class);
                intent.putExtra("CategoryName", arrayList.get(position).getCategoryName());
                intent.putExtra("Category_Id", arrayList.get(position).getCategoryId());
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
              //  finish();
            }
        });*/

        navheader_accountslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url_position_name = URL_list.get(position).getUrlname();
                String db_position = URL_list.get(position).getDBName();
                String db_custmastid = URL_list.get(position).getCustVendorMasterId();
                String envMasterId = URL_list.get(position).getEnvMasterId();
                String plantMasterId = URL_list.get(position).getPlantMasterId();
                restoredusername = URL_list.get(position).getUserName();
                restoredText = URL_list.get(position).getMobile();
                AnyMartData.EnvMasterId = envMasterId;
                AnyMartData.PlantMasterId = plantMasterId;
                CustomerID = db_custmastid;
                AnyMartData.LoginId = "";
                AnyMartData.Password = "";

                if (url_position_name != null || url_position_name != "") {

                    CopmanyURL = "http://" + url_position_name + "/api/OrderBillingAPI/";
                }
                //CompanyURL = "http://"+enterurl.getText().toString()+"/api/OrderBillingAPI/";
                //replace dbname as new dbname
                //replace mainurl name
                //call dbhelper function pass new dbname, urlname
                AnyMartData.CompanyURL = "http://" + url_position_name;
                AnyMartData.MAIN_URL = CopmanyURL;
                AnyMartDatabaseConstants.DATABASE__NAME_URL = db_position;
                restoredownername = url_position_name;
                // AnyMartData.SESSION_ID = null;
                // AnyMartData.HANDLE = null;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CompanyURL", AnyMartData.MAIN_URL);
                editor.putString("companyurlmain", AnyMartData.CompanyURL);
                editor.putString("companyURL_LOGO", url_position_name);
                editor.putString("AppEnvMasterId", AnyMartData.EnvMasterId);
                editor.putString("PlantMasterId", AnyMartData.PlantMasterId);
                editor.putString("DatabaseName", AnyMartDatabaseConstants.DATABASE__NAME_URL);
                editor.putString("CustVendorMasterId", CustomerID);
                editor.putString("username", restoredusername);
                editor.putString("Mobileno", restoredText);
                editor.putString("companyURL_LOGO", restoredownername);
                editor.putString("logopath", "");
                editor.commit();

                txtusername.setText(restoredusername);
                txtmobileno.setText(restoredText);
                txtownername.setText(restoredownername);

                databaseHelper = new DatabaseHelper(Merchant_MainActivity.this, AnyMartDatabaseConstants.DATABASE__NAME_URL);

                Intent i = new Intent(Merchant_MainActivity.this, SplashActivity.class);
                startActivity(i);
                finish();
            }
        });

       /* listview_recent_ordered_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                String itemid = arrayList_bean.get(position).getProduct_id();
                //show dialog box displaying details of it
                //showItemDetails(itemid);
                //getOpenOrderDataFromDatabase();
            }
        });*/

        /*listview_recent_ordered_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View view, final int position, long id) {

                try{
                    RefreshOrderHistory.RefreshOrderHistoryData(Merchant_MainActivity.this);

                    String SOHeaderID = newList.get(position).getSOHeaderId();
                    String status = newList.get(position).getStatus();
                    String statusname = newList.get(position).getStatusname();
                    String TotalAmt = String.valueOf(newList.get(position).getNetAmt());
                    String sono = newList.get(position).getSONo();
                    String address = newList.get(position).getAddress();

                    Intent intent_go = new Intent(parent, OrderDetailsActivity.class);
                    intent_go.putExtra("SOHeaderID", SOHeaderID);
                    intent_go.putExtra("status", status);
                    intent_go.putExtra("statusname",*//*statusname*//*"Created");
                    intent_go.putExtra("TotalAmt",TotalAmt);
                    intent_go.putExtra("OrderNumber",sono);
                    intent_go.putExtra("DelvryAddress",address);
                    startActivity(intent_go);
                    overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

      txt_showmore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                index = historyBeanList.size() + 10;
                // Toast.makeText(parent,"index = "+index,Toast.LENGTH_SHORT ).show();

                if ((AnyMartData.SESSION_ID != null)
                        && (AnyMartData.HANDLE != null)) {
                    new GetPendingOrderHistoryList().execute();
                } else {
                    new StartSession(parent, new CallbackInterface() {

                        @Override
                        public void callMethod() {
                            new GetPendingOrderHistoryList().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            }
        });*/

      /*  startshopping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_goto = new Intent(parent, SubCategoryActivity.class);
                intent_goto.putExtra("CategoryName", cat_name);
                intent_goto.putExtra("Category_Id", cat_id);
                intent_goto.putExtra("CustVendorMasterId", custvendorID);
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                //intent_goto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_goto);
                finish();
            }
        });*/

        btn_categorySetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation_Category();


            }
        });

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

            /*http://localhost:54614/api/OrderBillingAPI/getFamilyDataAnyDukaan?handler=3648139759400686&sessionid=64955413*/
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
            } else if(response_list.contains("sesEndTime")){
                try{
                    progress.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
            }else {
                json = response_list;
                parseJson_FamilyMaster(json);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson_FamilyMaster(String json) {

        // Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS);
        Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA);
        arrayList.clear();
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

            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

            getDataFromDataBase();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Utilities.setCartImage(parent, false);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,imgthumbIds);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        ++backpressCount;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            backpressCount = 0;
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            if (backpressCount == 1) {
                //do whatever you want to do on first click for example:
                Toast.makeText(this, ""+getResources().getString(R.string.prsback), Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
                //super.onBackPressed();
            } else if (backpressCount == 2) {
              /*  if (back_pressed + 1000 > System.currentTimeMillis()) {
                    //do whatever you want to do on the click after the first for example:
                    finish();
                } else {
                    backpressCount = 1;
                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                    back_pressed = System.currentTimeMillis();
                }*/
                finish();
            }

        }

        /*if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit",
                Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_refresh, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);
        menuItem.setVisible(false);
        final MenuItem menurefrehs = menu.findItem(R.id.refresh);
        menurefrehs.setVisible(false);
        /*MenuItemCompat.setActionView(menuItem, update_count);
        RelativeLayout countLayout = (RelativeLayout) MenuItemCompat.getActionView(menuItem);
        countLayout.setVisibility(View.GONE);*/
        // txt_cartcount = (TextView)countLayout.findViewById(R.id.txt_cartcount);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.search:
                break;

            case R.id.refresh:

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
                } else {
                    Toast.makeText(parent, ""+getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    //callSnackbar();
                }
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.category) {
            Intent intent1 = new Intent(getApplicationContext(), Category_SelectionActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
        } else if (id == R.id.priceList) {
            Intent intent1 = new Intent(getApplicationContext(), MerchantSelectedCategoryActivity.class);
            startActivity(intent1);
        }else if (id == R.id.itemCreation) {
            Intent intent1 = new Intent(getApplicationContext(), ItemCreationActivity.class);
            startActivity(intent1);
        }else if (id == R.id.item_report) {
            Intent intent1 = new Intent(getApplicationContext(), MerchantItemReortActivity.class);
            startActivity(intent1);
        }else*/ if (id == R.id.profile) {
            Intent intent1 = new Intent(getApplicationContext(), EditMerchantActivity.class);
            startActivity(intent1);
        }/*else if(id == R.id.del_charges) {
            Intent intent1 = new Intent(getApplicationContext(), DeliveryChargesActivity.class);
            startActivity(intent1);
        }else if(id == R.id.item_merchant_pay) {
            Intent intent1 = new Intent(getApplicationContext(), MerchantPayment.class);
            startActivity(intent1);
        }else if(id == R.id.item_merchant_pay_history) {
            Intent intent1 = new Intent(getApplicationContext(),MerchantPaymentHistoryActivity.class);
            startActivity(intent1);
            *//*Intent intent1 = new Intent(getApplicationContext(),NearBySearch.class);
            startActivity(intent1);*//*
        }*/else if(id == R.id.item_merchant_shop_me) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); /*image/*,*/
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Intent.EXTRA_SUBJECT, "Any Dukaan");

                AnyMartData.ADDRESS= sharedpreferences.getString("address","");
                AnyMartData.PINCODE=sharedpreferences.getString("pincode","");
                AnyMartData.LATITUDE=sharedpreferences.getString("lat","");
                AnyMartData.LONGITUDE=sharedpreferences.getString("lng","");
                AnyMartData.LOCAL_NAME=sharedpreferences.getString("local","");

                String mapaddr = "http://maps.google.com/maps?q=loc:" + AnyMartData.LATITUDE + "," + AnyMartData.LONGITUDE;

                String msg=getResources().getString(R.string.me)+
                        restoredusername+"\n" +
                        AnyMartData.LOCAL_NAME+"\n" +
                        AnyMartData.ADDRESS+","+AnyMartData.PINCODE+"\n" +
                        restoredText+"\n"+mapaddr;
                //  String url1 = "<a href= 'https://play.google.com/store/apps/details?id=com.vritti.freshmart'>https://play.google.com/store/apps/details?id=com.vritti.freshmart</a>";

                i.putExtra(Intent.EXTRA_TEXT, msg );
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(i, "Choose one to Share link!"));



            } catch (Exception e) {
            }
        }
        /*else if (id == R.id.offers) {
        }*/

        else if (id == R.id.nav_share_customer) {

            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); /*image/*,*/
                // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Intent.EXTRA_SUBJECT, "Any Dukaan" /*AnyMartData.MODULE + "App"*/);
                String msg = "\n Let me recommend you Any Dukaan application\n\n";
                String url1 = "<a href= 'https://play.google.com/store/apps/details?id=com.vritti.freshmart'>https://play.google.com/store/apps/details?id=com.vritti.freshmart</a>";
                Uri linkurl = Uri.parse(url1);
                imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + "logo121_anymart");
                i.putExtra(Intent.EXTRA_TEXT, msg + "\n" + Html.fromHtml(url1));
                //i.putExtra(Intent.EXTRA_STREAM, imageUri);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(i, "Choose one to Share link!"));



            } catch (Exception e) {
            }

        }
        else if (id == R.id.language) {
            //language change logic
            //open dialogue box
            openDialogueBox();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //   Toast.makeText(getApplicationContext(), "Search Reult..." + query, Toast.LENGTH_LONG).show();
            mySearch(query);
        }
    }

    protected void mySearch(String query) {

        for (AllCatSubcatItems s : arrayList) {
            if (s.getCategoryName().contains(query)) {
                arrayList.add(s);
            }
        }
    }

    private void shareIt() {
//sharing implementation here
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Any Mart");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "\n Now order products with Any Mart application, " +
                "\n \t click here to visit \n\n https://play.google.com/store/apps/details?id=com.vritti.freshmart");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void showNewPrompt() {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_freq_merchant);
        myDialog.setCancelable(true);

        //myDialog.getWindow().setGravity(Gravity.BOTTOM);
        databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase sql_db = databaseHelper.getWritableDatabase();

        Cursor c_merchantName = sql_db.rawQuery("select MerchantName from " + AnyMartDatabaseConstants.TABLE_MERCHANTS, null);
        Log.d("test", "" + c_merchantName.getCount());

        if (c_merchantName.getCount() > 0) {
            c_merchantName.moveToFirst();
        }

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        quest.setText("Only one merchant is present, No need to add it in Frequent Merchant_MM List");
        Button btnyes = (Button) myDialog
                .findViewById(R.id.btn_ok);
        btnyes.setText("OK");
        btnyes.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
               /* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);/*
                finish();*/
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    public void showItemDetails(String itemid) {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_display_details);
        myDialog.setCancelable(true);

        //myDialog.getWindow().setGravity(Gravity.BOTTOM);
        databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase sql_db = databaseHelper.getWritableDatabase();

        final TextView quest = (TextView) myDialog.findViewById(R.id.textdetails);
        final TextView txtname = (TextView) myDialog.findViewById(R.id.txtname);
        final TextView txtrate = (TextView) myDialog.findViewById(R.id.txtrate);
        final TextView txtqty = (TextView) myDialog.findViewById(R.id.txtqty);
        final TextView txtlineamt = (TextView) myDialog.findViewById(R.id.txtlineamt);
        final TextView txtorderdate = (TextView) myDialog.findViewById(R.id.txtorderdate);
        final TextView txtdeliverydate = (TextView) myDialog.findViewById(R.id.txtdeliverydate);

        String orderID = null;
        Cursor c_recentorder = sql_db.rawQuery("select distinct SOHeaderId from " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " ORDER BY DoAck DESC", null);
        Log.d("test", "" + c_recentorder.getCount());
        if (c_recentorder.getCount() > 0) {
            c_recentorder.moveToFirst();
            orderID = c_recentorder.getString(c_recentorder.getColumnIndex("SOHeaderId"));
        }

        Cursor c_orderdetails = sql_db.rawQuery("select * from " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " where SOHeaderId='" + orderID + "' AND ItemMasterId='" + itemid + "'", null);
        Log.d("test", "" + c_orderdetails.getCount());

        String itemName = null;
        String itemID = null;
        Float Qty = null;
        Float Amount = null;
        Float Rate = null;
        String Merchant_Name = null;
        String Merchant_id = null;
        String DoAck = null;
        String SODate = null;

        if (c_orderdetails.getCount() > 0) {
            c_orderdetails.moveToFirst();
            do {
                itemName = c_orderdetails.getString(c_orderdetails.getColumnIndex("ItemDesc"));
                itemID = c_orderdetails.getString(c_orderdetails.getColumnIndex("ItemMasterId"));
                Qty = Float.valueOf(c_orderdetails.getString(c_orderdetails.getColumnIndex("Qty")));
                Amount = Float.valueOf(c_orderdetails.getString(c_orderdetails.getColumnIndex("LineAmt")));
                Rate = Float.valueOf(c_orderdetails.getString(c_orderdetails.getColumnIndex("Rate")));
                Merchant_Name = c_orderdetails.getString(c_orderdetails.getColumnIndex("merchantname"));
                Merchant_id = c_orderdetails.getString(c_orderdetails.getColumnIndex("merchantid"));
                DoAck = c_orderdetails.getString(c_orderdetails.getColumnIndex("DoAck"));
                SODate = c_orderdetails.getString(c_orderdetails.getColumnIndex("SODate"));

                //SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                SimpleDateFormat Format = new SimpleDateFormat("dd MMM yyyy");//23 Feb 2016 12:16PM
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date d1 = null;
                Date d2_ack = null;
                try {
                    d1 = format.parse(SODate);
                    d2_ack = format.parse(DoAck);
                    //DateToStr = toFormat.format(date);
                    DateToString = Format.format(d1);
                    DateToString_ack = Format.format(d2_ack);
                    // DateToStr = format.format(d1);
                    System.out.println(DateToString);
                    System.out.println(DateToString_ack);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while (c_orderdetails.moveToNext());
        }
        quest.setText("Item Details");
        txtname.setText(itemName);
        txtrate.setText(Rate + " ₹");
        txtqty.setText((String.valueOf(Qty)));
        txtlineamt.setText(Amount + " ₹");
        //txtorderdate.setText(DateToStr);
        txtorderdate.setText(DateToString_ack);
        txtdeliverydate.setText(DateToString);

        Button btnyes = (Button) myDialog
                .findViewById(R.id.btntxtok);
        btnyes.setText("OK");
        btnyes.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    public void showNoDataInWishlist() {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_ok);
        myDialog.setCancelable(true);

        //myDialog.getWindow().setGravity(Gravity.BOTTOM);

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        quest.setText("Sorry! No items in Wishlist Cart");

        final Button SelectDate = (Button) myDialog
                .findViewById(R.id.btn_selectdate_ok);
        SelectDate.setVisibility(View.GONE);

        final Button btnok = (Button) myDialog.findViewById(R.id.copy_btnok);
        btnok.setText("Ok");

        btnok.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public void Navigation_Category() {

        JSONArray jsonArray1 = new JSONArray();
        for (int i = 0; i < categoryList.size(); i++) {

            for (int j = 0; j < categoryList.get(i).getmSubCategoryList().size(); j++) {

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("SubcatId", categoryList.get(i).getmSubCategoryList().get(j).getPsubCatId());//MerchPriceListId
                    //MerchPriceListId
                    if (categoryList.get(i).getmSubCategoryList().get(j).getIsCheck().equalsIgnoreCase("yes")) {
                        jsonObject.put("IsActive", "Y");//MerchPriceListId
                    } else {
                        jsonObject.put("IsActive", "N");//MerchPriceListId
                    }

                    jsonArray1.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("CustVendorMasterId", CustomerID);
            jsonObject1.put("SubCategoryIdArr", jsonArray1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finalJson = jsonObject1.toString();


        if (NetworkUtils.isNetworkAvailable(Merchant_MainActivity.this)) {
            new StartSession(Merchant_MainActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new PostSelectedCategory().execute();
                }

                @Override
                public void callfailMethod(String s) {

                }
            });
        }


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

            try{
                progress = ProgressHUD.show(parent,
                        ""+getResources().getString(R.string.loading), false, true, null);
            }catch (Exception e){
                e.printStackTrace();
            }
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
                Toast.makeText(Merchant_MainActivity.this, ""+getResources().getString(R.string.unablesave),
                        Toast.LENGTH_SHORT).show();
            } else if (responseString.equalsIgnoreCase("true")) {
                Toast.makeText(Merchant_MainActivity.this, ""+getResources().getString(R.string.save_data), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupReferences() {
        btn_categorySetting.setVisibility(View.VISIBLE);

        try{
            progress = ProgressHUD.show(parent,
                    ""+getResources().getString(R.string.loading_cat),
                    false, true, null);
        }catch (Exception e){
            e.printStackTrace();
        }

        viewpagerlayout.setVisibility(View.GONE);
        //lvCategory = findViewById(R.id.lvCategory);
        // categoryList1 = new ArrayList<>();
        // subcategories = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();
        categoryList = new ArrayList<>();
        parentItems.clear();
        childItems.clear();
        categoryList.clear();

        if (categoryList1.size() != 0) {
            for (int i = 0; i < categoryList1.size(); i++) {
                ArrayList<MerchantSelection.SubCategory> subList = new ArrayList<MerchantSelection.SubCategory>();

                for (int j = 0; j < subcategories.size(); j++) {
                    ArrayList<MerchantSelection.SubCategory.ItemList> sub_subList = new ArrayList<MerchantSelection.SubCategory.ItemList>();
                    if (categoryList1.get(i).getCategoryId().equals(subcategories.get(j).getCategory_Id())) {
                        //   subcategories.setIsCheck();
                        subcategories.get(j).setIsCheck(ConstantManager.CHECK_BOX_CHECKED_FALSE);
                        subList.add(subcategories.get(j));
                    }
                }

                categoryList.add(new MerchantSelection(categoryList1.get(i).getCategoryId(), categoryList1.get(i).getCategoryName(),
                        categoryList1.get(i).getCatImgPath(), categoryList1.get(i).getSubCatCnt(),subList));

            }
        }

        Log.d("TAG", "setupReferences: " + categoryList.size());

        for (MerchantSelection data : categoryList) {
//                        Log.i("Item id",item.id);
            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID, data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME, data.getCategoryName());
            mapParent.put(ConstantManager.Parameter.CAT_FLAG, data.getCat_flag());

            int countIsChecked = 0;
            for (MerchantSelection.SubCategory subCategoryItem : data.getmSubCategoryList()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                mapChild.put(ConstantManager.Parameter.SUB_ID, subCategoryItem.getPsubCatId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, subCategoryItem.getpSubCatName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID, subCategoryItem.getCategory_Id());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED, subCategoryItem.getIsCheck());
                mapChild.put(ConstantManager.Parameter.SUBCAT_FLAG, subCategoryItem.getSubCat_flag());

                if (subCategoryItem.getIsCheck().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if (countIsChecked == data.getmSubCategoryList().size()) {

                data.setIsCheck(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                data.setIsCheck(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED, data.getIsCheck());
            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(this, parentItems, childItems,
                false, categoryList);
        linear_listview.setAdapter(myCategoriesExpandableListAdapter);

        try{
            progress.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void openDialogueBox(){
        final BottomSheetDialog btmsheetdialog = new BottomSheetDialog(parent);
        View sheetview = getLayoutInflater().inflate(R.layout.dialogue_select_language, null);
        btmsheetdialog.setContentView(sheetview);
        btmsheetdialog.show();
        btmsheetdialog.setCancelable(true);
        btmsheetdialog.setCanceledOnTouchOutside(false);

        RadioGroup radgrp =  btmsheetdialog.findViewById(R.id.radgrp_lang);
        final RadioButton radbtnhindi =  btmsheetdialog.findViewById(R.id.radbtnhindi);
        final RadioButton radbtnenglish =  btmsheetdialog.findViewById(R.id.radbtnenglish);
        final RadioButton radbtnmarathi =  btmsheetdialog.findViewById(R.id.radbtnmarathi);

        radbtnmarathi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radbtnhindi.setChecked(true);
                    radbtnenglish.setChecked(false);
                    Toast.makeText(parent,""+getResources().getString(R.string.changelanguage),Toast.LENGTH_SHORT).show();
                    Utility.setLocale("mr",Merchant_MainActivity.this);

                    recreate();

                    btmsheetdialog.dismiss();

                }else {
                }
            }
        });

        radbtnhindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radbtnhindi.setChecked(true);
                    radbtnenglish.setChecked(false);
                    Toast.makeText(parent,""+getResources().getString(R.string.changelanguage),Toast.LENGTH_SHORT).show();
                    Utility.setLocale("hi",Merchant_MainActivity.this);

                    recreate();

                    btmsheetdialog.dismiss();

                }else {
                }
            }
        });

        radbtnenglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radbtnenglish.setChecked(true);
                    radbtnhindi.setChecked(false);
                    Toast.makeText(parent,""+getResources().getString(R.string.changelanguage),Toast.LENGTH_SHORT).show();
                    Utility.setLocale("en",Merchant_MainActivity.this);

                    recreate();

                    btmsheetdialog.dismiss();

                }else {
                }
            }
        });

    }

    public class ViewPagerAdapter extends PagerAdapter{
        private Context context;
        private LayoutInflater layoutInflater;
        int images[];
        //private Integer [] images = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3};

        public ViewPagerAdapter(Context context,int images[]) {
            this.context = context;
            this.images = images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.viewpager_item,container, false);

            assert view != null;

            ImageView imageView =  view.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.banner_orange);
            imageView.setVisibility(View.VISIBLE);
            //imageView.setImageResource(images[position]);
            videoView = view.findViewById(R.id.videoview);
            videoView.setVisibility(View.GONE);

            /*VideoView videoHolder = new VideoView(Merchant_MainActivity.this);
            videoHolder.setMediaController(new MediaController(Merchant_MainActivity.this));
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.baneer_vid1);
            pathVideo = "android.resource://" + getPackageName() + "/" + R.raw.baneer_vid1;
            videoView.setVideoURI(Uri.parse(pathVideo));
            videoView.start();*/

            container.addView(view);

            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            /*ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);*/
            container.removeView((RelativeLayout) object);

        }


        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

    }

    private void callforplayStore() {
        String PlayStoreVersion = null;
        String MyAppVersion = null;
        if(NetworkUtils.isNetworkAvailable(Merchant_MainActivity.this)) {
            try {
                MyAppVersion = (getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id="
                        + "com.vritti.freshmart").get();
                String AllStr = doc.text();
                String parts[] = AllStr.split("Current Version");
                String newparts[] = parts[1].split("Requires Android");
                PlayStoreVersion = newparts[0].trim();

                if(!MyAppVersion.equals(PlayStoreVersion)){
                    if(dialogopen.equalsIgnoreCase("no")) {

                        Date date = new Date();
                        final Calendar c = Calendar.getInstance();

                        Year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        day = c.get(Calendar.DAY_OF_MONTH);

                        TODAYDATE = day + "-" + (month + 1) + "-" + Year;

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        //editor.putString("Dialog", "NoDialog");
                        editor.putString("TodaysDate",TODAYDATE);
                        editor.apply();

                        showUpdateDialog(PlayStoreVersion);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

            }catch (NullPointerException e){
                e.printStackTrace();

            }catch (Exception e){
                e.printStackTrace();

            }
        }
    }

    private void showUpdateDialog(String PSVersion) {
        try {
            // txtupdateversion.setVisibility(View.VISIBLE);
            // btnupdateversion.setVisibility(View.VISIBLE);

            /*Animation hrtbeat = AnimationUtils.loadAnimation(this, R.anim.heartbeat);
            btnupdateversion.startAnimation(hrtbeat);*/
           /* String updatemsg = " "+getResources().getString(R.string.new_version)+" " + PSVersion + " " +getResources().getString(R.string.is_on_playstore)+" \n"
                    +getResources().getString(R.string.toaccess);
            txtupdateversion.setText(updatemsg);*/

            sharedpreferences = getSharedPreferences(SplashActivity.MyPREFERENCES,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            final Dialog myDialog = new Dialog(parent);
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myDialog.setContentView(app_update_lay);
            myDialog.setCancelable(false);

            final TextView txtupdateversion = myDialog.findViewById(R.id.txtupdateversion);
            final Button btnupdateversion = myDialog.findViewById(R.id.btnupdateversion);
            final Button btncancel = myDialog.findViewById(R.id.btncancel);

            String updatemsg = " "+getResources().getString(R.string.new_version)+" "
                    +Html.fromHtml("<b>"+PSVersion+"</b>")+" "
                    +getResources().getString(R.string.is_on_playstore)+" \n"
                    +getResources().getString(R.string.toaccess);
            txtupdateversion.setText(updatemsg);

            btnupdateversion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.vritti.freshmart"));
                    startActivity(intent);

                    dialogopen = "no";
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Dialog", "NoDialog");
                    //editor.putString("TodaysDate",TODAYDATE);
                    editor.apply();
                    myDialog.dismiss();
                }
            });

            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogopen = "no";
                    myDialog.dismiss();
                }
            });

            dialogopen = "yes";
            myDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DownloadMerchantData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String url = AnyMartData.CompanyURL + AnyMartData.api_GetMerchantDetails + "?&MerchId="+CustVendorMasterId;
            try {
                res = Utility.OpenConnection_placeorder(url, Merchant_MainActivity.this);
                response = res.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            try {
                if (response != null) {
                    JSONArray jResults = new JSONArray();
                    try {

                        jResults = new JSONArray(response);
                        ContentValues values = new ContentValues();

                        if (jResults.length() > 0) {
                            for (int i = 0; i < jResults.length(); i++) {
                                JSONObject jsonObject = jResults.getJSONObject(i);

                                AnyMartData.LATITUDE = jsonObject.getString("Lattitude");
                                AnyMartData.LONGITUDE = jsonObject.getString("Longitude");
                                AnyMartData.ADDRESS=jsonObject.getString("MerchAddress");
                                AnyMartData.PINCODE = jsonObject.getString("PinCode");
                                AnyMartData.ADDRESS = jsonObject.getString("MerchAddress");
                                AnyMartData.LOCAL_NAME=jsonObject.getString("MerchNameLocalLang");

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("address", AnyMartData.ADDRESS);
                                editor.putString("pincode", AnyMartData.PINCODE);
                                editor.putString("lat", AnyMartData.LATITUDE);
                                editor.putString("lng", AnyMartData.LONGITUDE);
                                editor.putString("local", AnyMartData.LOCAL_NAME);
                                editor.commit();

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

