package org.iiitdharwad.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;


import org.iiitdharwad.navigation.Adapters.RecyclerViewCardViewAdapter;
import org.iiitdharwad.navigation.Adapters.RecyclerViewNotificationViewAdapter;
import org.iiitdharwad.navigation.Adapters.RecyclerViewPersonViewAdapter;
import org.iiitdharwad.navigation.Adapters.ViewPagerCollegeAdapter;
import org.iiitdharwad.navigation.Objects.FDPValue;
import org.iiitdharwad.navigation.schedule_code.ScheduleAdapter;
import org.iiitdharwad.navigation.schedule_code.scheduleDateValue;
import org.iiitdharwad.navigation.schedule_code.scheduleValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkStateReceiver.NetworkStateReceiverListener {

    //Declaring the Variables Required
    LinearLayout main, aboutCollege, topic, resourcePerson, committeeLayout, scheduleLayout, notificationLayout;
    NavigationView navigationView;
    public TextView aboutPage, aboutFDPContent, aboutFDPTitle, aboutCollegeTitle, internetCheck, sponsorTextView, notification_count;
    public int resId;
    public static int arrowDown, arrowUp;
    public static Typeface tf, tf2, tf3;
    public static ImageView notificationBtn, sponsersImage;
    public static Animation shake;
    boolean backIntentional = false, notificationLoaded = false;

    private static final String TAG = "MyActivity";
    public static boolean seen = false;

    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    SwipeRefreshLayout refreshLayoutHome, refreshLayoutNotification;
    WebView webView;

    AppBarLayout appBarLayout;

    SharedPreferences sharedPreferences;
    String contentInCache = null;

    ArrayList<FDPValue> FDPList;
    StringBuilder to_append = new StringBuilder();
    ArrayList<scheduleDateValue> scheduleDateValues;
    String[] seen_notifications;
    ArrayList<FDPValue.resourcePersonValue> resourcePersonValueArrayList, chiefPatronList, patronList, speakerList, convenerList, coConvenerList;
    RecyclerView recyclerView, resourcePersonRecyclerView, committeeRecyclerView, scheduleRecyclerView, notificationRecyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    JsonArrayRequest jsonArrayRequest, jsonArrayRequestPeople ;
    RequestQueue requestQueue, requestQueuePeople ;

    private NetworkStateReceiver networkStateReceiver;
    ScheduleAdapter recyclerViewScheduleViewAdapter;

    ScrollView homePageScroll;

//    DatabaseReference firebaseRefernce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        //Checks for Internet Connectivity
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        //Setting Up the Text View for "No Internet Connection"
        internetCheck = findViewById(R.id.internetCheck);

        //Linking all the Linear Layouts
        setContentView(R.layout.activity_main);
        main = findViewById(R.id.layout_main);
        aboutCollege = findViewById(R.id.layout_about_college);
        topic = findViewById(R.id.layout_topic);
        resourcePerson = findViewById(R.id.layout_resource_person);
        committeeLayout = findViewById(R.id.layout_committee);
        scheduleLayout = findViewById(R.id.layout_schedule);
        notificationLayout = findViewById(R.id.layout_notification);

        //Linking the ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Linking the Drawer and Action Bar
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Setting Up the Navigation View
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        //Recycler View of Topics
        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        //Recycler View of Schedule
        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        scheduleRecyclerView.setLayoutManager(recyclerViewlayoutManager);

        //Recycler View of Speakers
        resourcePersonRecyclerView = findViewById(R.id.personsRecyclerView);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        resourcePersonRecyclerView.setLayoutManager(recyclerViewlayoutManager);

        //Recycler View of Committee
        committeeRecyclerView = findViewById(R.id.committeeRecyclerView);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        committeeRecyclerView.setLayoutManager(recyclerViewlayoutManager);

        //Recycler View of Notifications
        notificationRecyclerView = findViewById(R.id.recyclerViewNotification);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        notificationRecyclerView.setLayoutManager(recyclerViewlayoutManager);
        notification_count = findViewById(R.id.notification_count_view);

        sharedPreferences = getSharedPreferences("notifications", Context.MODE_PRIVATE);


        //View Pager Inflation and setting adapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerCollege);
        ViewPagerCollegeAdapter viewPagerAdapter = new ViewPagerCollegeAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        setUpSlideshow(viewPagerAdapter, viewPager);


        //App Bar Buttons and its animations
        notificationBtn = findViewById(R.id.notification_button);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        //For animation of the scheduling Group View
        arrowDown = getResources().getIdentifier("ic_expand_less", "drawable", getPackageName());
        arrowUp = getResources().getIdentifier("ic_expand_more", "drawable", getPackageName());

        //Initializing the Typeface
        tf = Typeface.createFromAsset(getAssets(), "rale.ttf");
        tf2 = Typeface.createFromAsset(getAssets(), "raleBlackItalic.ttf");
        tf3 = Typeface.createFromAsset(getAssets(), "raleBoldItalic.ttf");
        //Linking the Text Views of Titles and About Content and Setting the Content
        HomeContentDisplay();

        LoadFromDevice();

        //Calling all the Server Functions
        JSON_DATA_WEB_CALL_FOR_ITEMS();
        JSON_DATA_WEB_CALL_FOR_SPEAKERS();
        JSON_DATA_WEB_CALL_FOR_SCHEDULE();
        JSON_DATA_WEB_CALL_FOR_NOTIFICATION();


        //Setting Click Listener on the notification image in app bar
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationBtn.setImageResource(getResources().getIdentifier("ic_notifications", "drawable", getPackageName()));
                layoutToDisplay(6);
                notification_count = findViewById(R.id.notification_count_view);
                notification_count.setVisibility(View.GONE);
            }
        });

        refreshLayoutHome = findViewById(R.id.refreshApp);
        refreshLayoutHome.setRefreshing(true);
        refreshLayoutHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JSON_DATA_WEB_CALL_FOR_ITEMS();
                JSON_DATA_WEB_CALL_FOR_SPEAKERS();
                JSON_DATA_WEB_CALL_FOR_SCHEDULE();
                JSON_DATA_WEB_CALL_FOR_NOTIFICATION();
                Toast.makeText(MainActivity.this, "Refresh Supported in Notification Only", Toast.LENGTH_LONG).show();
                refreshLayoutHome.setRefreshing(false);
                refreshLayoutHome.setEnabled(false);
            }
        });

        refreshLayoutNotification = findViewById(R.id.refreshNotification);
        refreshLayoutNotification.setNestedScrollingEnabled(true);
        refreshLayoutNotification.isNestedScrollingEnabled();
        refreshLayoutNotification.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                JSON_DATA_WEB_CALL_FOR_ITEMS();
//                JSON_DATA_WEB_CALL_FOR_SPEAKERS();
//                JSON_DATA_WEB_CALL_FOR_SCHEDULE();
                JSON_DATA_WEB_CALL_FOR_NOTIFICATION();
                layoutToDisplay(6);
                refreshLayoutNotification.setRefreshing(false);
            }
        });

        sponsersImage = findViewById(R.id.sponsers_image);
        sponsersImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("https://saankhyalabs.com/"));
//                startActivity(intent);
                startActivity(new Intent(MainActivity.this, descriptionFDP.class));
            }
        });

    }


    //Closing the Drawer if open

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(!navigationView.getMenu().getItem(0).isChecked() || !backIntentional) {
            layoutToDisplay(0);
            Toast.makeText(MainActivity.this, "Press Back Again To EXIT", Toast.LENGTH_LONG).show();
            backIntentional = true;
        }
        else{
            super.onBackPressed();
        }
    }

    //Ignore
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    //Setting the Font and Content of Home Layout
    public void HomeContentDisplay(){
        //Linking the Text Views of Titles and About Content and Setting the Content
        aboutFDPContent = findViewById(R.id.aboutFDPTextView);
        sponsorTextView = findViewById(R.id.sponsorsTextView);
        aboutFDPTitle = findViewById(R.id.fdp_heading);
        aboutFDPContent.setText(R.string.aboutFDPContent);
        TextView fdpHeadingTitle = findViewById(R.id.fdp_heading_title);
        //Applying the Font
        aboutFDPContent.setTypeface(tf);
        sponsorTextView.setTypeface(tf2);
        aboutFDPTitle.setTypeface(tf2);
        fdpHeadingTitle.setTypeface(tf3);
    }

    //Setting the Font and Content of About College Layout
    public void AboutContentDisplay(){
        aboutPage = findViewById(R.id.aboutTextView);
        aboutPage.setText(R.string.about);
        aboutPage.setTypeface(tf);

        aboutCollegeTitle = findViewById(R.id.college_heading);
        aboutCollegeTitle.setTypeface(tf2);
    }

    public void LoadFromDevice(){

        //For Topic Items
        String storedTitleData = sharedPreferences.getString("titleItems", null);
        FDPList = new ArrayList<>();
        if(storedTitleData != null) {
            String[] storedItems = storedTitleData.split("<&>");

            for (int i = 0; i < storedItems.length; i++) {
                FDPValue fdpValue = new FDPValue();
                fdpValue.setFDPName(storedItems[i]);
                FDPList.add(fdpValue);
            }

            System.out.println("Entered here in topics");

            recyclerViewadapter = new RecyclerViewCardViewAdapter(FDPList, this);
            recyclerView.setAdapter(recyclerViewadapter);

        }

        //For Speakers and Resource Persons
        String storedPersonData = sharedPreferences.getString("speakerItems", null);
        if(storedPersonData != null) {
            resourcePersonValueArrayList = new ArrayList<>();
            chiefPatronList = new ArrayList<>();
            patronList = new ArrayList<>();
            speakerList = new ArrayList<>();
            convenerList = new ArrayList<>();
            coConvenerList = new ArrayList<>();


            String[] storedItems = storedPersonData.split("<&>");
            for (int i = 0; i < storedItems.length; i++) {

                FDPValue.resourcePersonValue GetDataAdapter = new FDPValue.resourcePersonValue();
                String[] keys = storedItems[i].split("<,>");

                try {
                    String name = keys[0];
                    String company = keys[1];
                    String category = keys[2];
                    GetDataAdapter.setPersonName(name);
                    GetDataAdapter.setCategory(category);
                    GetDataAdapter.setCompany(company);
                    GetDataAdapter.setHeading(null);

                    String imageId = name.replaceAll("\\s", "");
                    imageId = imageId.toLowerCase();
                    imageId = imageId.replaceAll("\\.", "_");

                    GetDataAdapter.setResId(getResources().getIdentifier(imageId, "drawable", getPackageName()));

                    resourcePersonValueArrayList.add(GetDataAdapter);

                    if (category.equals("chief patron")) {
                        chiefPatronList.add(GetDataAdapter);
                    } else if (category.equals("patron")) {
                        patronList.add(GetDataAdapter);
                    } else if (category.equals("speaker")) {
                        speakerList.add(GetDataAdapter);
                    } else if (category.equals("convener")) {
                        convenerList.add(GetDataAdapter);
                    } else {
                        coConvenerList.add(GetDataAdapter);
                    }

                } catch (Exception e) {

                }
            }

            //Inserting the Items in the Order Needed For the Committee

            ArrayList<FDPValue.resourcePersonValue> committeeList = new ArrayList<>();
            committeeList.add(setHeader("Chief Patron"));
            committeeList.addAll(chiefPatronList);
            committeeList.add(setHeader("Patron"));
            committeeList.addAll(patronList);
            committeeList.add(setHeader("Convener"));
            committeeList.addAll(convenerList);
            committeeList.add(setHeader("Co-Convener"));
            committeeList.addAll(coConvenerList);

            //Dummy Image if Persons image is not found
            resId = getResources().getIdentifier("dummy", "drawable", getPackageName());

            //Setting the Adapter for Speaker View
            RecyclerViewPersonViewAdapter recyclerViewPersonViewAdapter = new RecyclerViewPersonViewAdapter(speakerList, this, resId);
            resourcePersonRecyclerView.setAdapter(recyclerViewPersonViewAdapter);

            //Setting the Adapter for Committee View
            RecyclerViewPersonViewAdapter recyclerViewCommitteeViewAdapter = new RecyclerViewPersonViewAdapter(committeeList, this, resId);
            committeeRecyclerView.setAdapter(recyclerViewCommitteeViewAdapter);
        }

        //For Schedule
        String storedScheduleData = sharedPreferences.getString("scheduleItems", null);
        if(storedScheduleData != null){

            scheduleDateValues = new ArrayList<>();
            String[] storedScheduleItems = storedScheduleData.split("<&>");

            ArrayList<ArrayList<scheduleValue>> days = new ArrayList<>();

            int startDate = 8, noOfDays = 5;
            String restDate = "07-2019";

            for(int i = 0; i < noOfDays; i++){
                days.add(new ArrayList<scheduleValue>());
            }

            for(int i = 0; i<storedScheduleItems.length; i++) {
                try {
                    String[] keys = storedScheduleItems[i].split("<,>");

                    String startTime = keys[0];
                    String endTime = keys[1];
                    String speaker = keys[2];
                    String speakerInfo = keys[3];
                    String topic = keys[4];
                    String date = keys[5];

                    scheduleValue svdata = new scheduleValue(startTime, endTime, speaker, speakerInfo, topic);
                    String[] day = date.split("-");
                    int currDate = Integer.parseInt(day[0]);

                    days.get(currDate - startDate).add(svdata);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ArrayList<scheduleDateValue> finalList = new ArrayList<>();

            for(int i = 0; i < noOfDays; i++){
                String titleDate = (startDate + i) +  "-" + restDate;
                finalList.add(new scheduleDateValue(titleDate, days.get(i)));
            }

            //Setting the Adapter
            recyclerViewScheduleViewAdapter = new ScheduleAdapter(finalList);
            scheduleRecyclerView.setAdapter(recyclerViewScheduleViewAdapter);
        }

        if (!notificationLoaded){
            TextView notification_display = findViewById(R.id.no_notification_display);
            notification_display.setVisibility(View.VISIBLE);
        }

    }


    //Setting a Function to Display the Correct Layout
    public void layoutToDisplay(int itemIndex){

        backIntentional = false;

        //Removing the display completely
        main.setVisibility(View.GONE);
        topic.setVisibility(View.GONE);
        resourcePerson.setVisibility(View.GONE);
        aboutCollege.setVisibility(View.GONE);
        committeeLayout.setVisibility(View.GONE);
        scheduleLayout.setVisibility(View.GONE);
        notificationLayout.setVisibility(View.GONE);

        //Get the Number of items in the navigation View
        int items = navigationView.getMenu().size();
        Log.d(TAG, "layoutToDisplay: Size = " + items);

        //Setting all the checked marks in Navigation View to false
        for (int i = 0; i < items; i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        //Setting the required display
        switch (itemIndex){
            case 0:
                main.setVisibility(View.VISIBLE);
                break;
            case 1:
                aboutCollege.setVisibility(View.VISIBLE);
                break;
            case 2:
                topic.setVisibility(View.VISIBLE);
                break;
            case 3:
                scheduleLayout.setVisibility(View.VISIBLE);
                break;
            case 4:
                resourcePerson.setVisibility(View.VISIBLE);
                break;
            case 5:
                committeeLayout.setVisibility(View.VISIBLE);
                break;
            default:
                notificationLayout.setVisibility(View.VISIBLE);
                refreshLayoutHome.setEnabled(false);
                notificationBtn.setImageResource(getResources().getIdentifier("ic_notifications", "drawable", getPackageName()));
                notification_count = findViewById(R.id.notification_count_view);
                notification_count.setVisibility(View.GONE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                System.out.println("Seen Notifications - " + to_append.toString());
                editor.putString("seen_notifications", to_append.toString());
                editor.commit();
        }

        //Setting Check to the correct item
        navigationView.getMenu().getItem(itemIndex).setChecked(true);


    }

    public void setUpSlideshow(ViewPagerCollegeAdapter viewPagerAdapter, ViewPager viewPager){
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //Setting up the Navigation Menu
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Setting the Layouts to Respective Item
        switch (id){
            case R.id.nav_home:
                //Setting the Display Layout
                layoutToDisplay(0);

                //Setting up the font and content of Home
                HomeContentDisplay();

                //Break to limit the display
                break;
            case R.id.icon_about_college:
                layoutToDisplay(1);
                //Setting up the font of About College
                AboutContentDisplay();

                break;
            case R.id.icon_topics:
                layoutToDisplay(2);
                break;
            case R.id.icon_schedule:
                layoutToDisplay(3);
                break;
            case R.id.icon_resource_person:
                layoutToDisplay(4);
                break;
            case R.id.icon_committee:
                layoutToDisplay(5);
                break;
            default:
                layoutToDisplay(6);
        }

        //closing the Navigation Drawer after displaying the Respective Layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // ITEM ASYNC TASK
    //Setting Connection to the Server to Fetch Data for Items

    private void JSON_DATA_WEB_CALL_FOR_ITEMS(){
        jsonArrayRequest = new JsonArrayRequest("https://iiitworkshop.000webhostapp.com/getFDPData.php",

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Disabling the progress bar


                        //Sending the Response to parse as it will be in JSONArray format
                        JSON_PARSE_DATA_AFTER_WEBCALL_FOR_ITEMS(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Volley Error in Web Call", error);
                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);

    }

    //Setting Function to parse the JSONArray passed for ITEMS

    public void JSON_PARSE_DATA_AFTER_WEBCALL_FOR_ITEMS(JSONArray array){

        StringBuilder itemTitles = new StringBuilder();
        String storedData = sharedPreferences.getString("titleItems", null);

        for(int i = 0; i<array.length(); i++) {

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                String title = json.getString("title");
                itemTitles.append(title).append("<&>");

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }



        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("Unmatched Topics - " + itemTitles.toString());
        editor.putString("titleItems", itemTitles.toString());
        editor.commit();
        LoadFromDevice();

    }

    // COMMITTEE AND SPEAKERS ASYNC TASK
    //Setting Connection to the Server to Fetch Data for Committee and Speakers

    private void JSON_DATA_WEB_CALL_FOR_SPEAKERS(){
        jsonArrayRequestPeople = new JsonArrayRequest("https://iiitworkshop.000webhostapp.com/getResoucePeople.php",

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL_FOR_SPEAKERS(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Volley Error in Web Call for people", error);
                    }
                });

        requestQueuePeople = Volley.newRequestQueue(this);

        requestQueuePeople.add(jsonArrayRequestPeople);
    }

    //Setting Function to parse the JSONArray passed for COMMITTEE AND SPEAKERS

    public void JSON_PARSE_DATA_AFTER_WEBCALL_FOR_SPEAKERS(JSONArray array){

        String storedData = sharedPreferences.getString("speakerItems", null);
        StringBuilder speakerItems = new StringBuilder();

        for (int i = 0; i < array.length(); i++) {

            StringBuilder eachItem = new StringBuilder();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                String name = json.getString("name");
                eachItem.append(name).append("<,>");
                String company = json.getString("company");
                eachItem.append(company).append("<,>");
                String category = json.getString("category");
                eachItem.append(category).append("<,>");

            } catch (Exception e) {

            }
            speakerItems.append(eachItem.toString()).append("<&>");
        }



        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("Unmatched persons - " + speakerItems.toString());
        editor.putString("speakerItems", speakerItems.toString());
        editor.commit();
        LoadFromDevice();

    }

    //Setting the Header in Recycler View
    public FDPValue.resourcePersonValue setHeader(String heading){
        FDPValue.resourcePersonValue justHeading = new FDPValue.resourcePersonValue();
        justHeading.setCategory("header");
        justHeading.setHeading(heading);
        justHeading.setPersonName(null);
        justHeading.setCompany(null);
        justHeading.setResId(0);
        return justHeading;
    }

    // SCHEDULE ASYNC TASK
    //Setting Connection to the Server to Fetch Data for Schedule

    public void JSON_DATA_WEB_CALL_FOR_SCHEDULE(){
        jsonArrayRequestPeople = new JsonArrayRequest("https://iiitworkshop.000webhostapp.com/getSchedule.php",

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSON_PARSE_DATA_AFTER_WEBCALL_FOR_SCHEDULE(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Volley Error in Web Call for Schedule", error);
                    }
                });

        requestQueuePeople = Volley.newRequestQueue(this);

        requestQueuePeople.add(jsonArrayRequestPeople);
    }

    //Setting Function to parse the JSONArray passed for SCHEDULES

    public void JSON_PARSE_DATA_AFTER_WEBCALL_FOR_SCHEDULE(JSONArray array){

        StringBuilder scheduleItems = new StringBuilder();
        String scheduleData = sharedPreferences.getString("scheduleItems", null);

        for(int i = 0; i<array.length(); i++) {

            StringBuilder eachItem = new StringBuilder();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                String startTime = json.getString("start_time");
                eachItem.append(startTime).append("<,>");
                String endTime = json.getString("end_time");
                eachItem.append(endTime).append("<,>");
                String speaker = json.getString("speaker");
                eachItem.append(speaker).append("<,>");
                String speakerInfo = json.getString("speaker_info");
                eachItem.append(speakerInfo).append("<,>");
                String topic = json.getString("topic");
                eachItem.append(topic).append("<,>");
                String date = json.getString("date");
                eachItem.append(date).append("<,>");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            scheduleItems.append(eachItem.toString()).append("<&>");
        }


        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("Unmatched schedule - " + scheduleItems.toString());
        editor.putString("scheduleItems", scheduleItems.toString());
        editor.commit();
        LoadFromDevice();

    }

    // NOTIFICATION ASYNC TASK
    //Setting Connection to the Server to Fetch Data for Notification

    private void JSON_DATA_WEB_CALL_FOR_NOTIFICATION(){
        jsonArrayRequestPeople = new JsonArrayRequest("https://iiitworkshop.000webhostapp.com/getNotifications.php",

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL_FOR_NOTIFICATION(response);
                        refreshLayoutHome.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Volley Error in Web Call for Notification", error);
                    }
                });

        requestQueuePeople = Volley.newRequestQueue(this);

        requestQueuePeople.add(jsonArrayRequestPeople);
    }

    //Setting Function to parse the JSONArray passed for NOTIFICATIONS

    public void JSON_PARSE_DATA_AFTER_WEBCALL_FOR_NOTIFICATION(JSONArray array){

        ArrayList<scheduleValue> getData = new ArrayList<>();
        int notifications_to_see = 0;
        to_append = new StringBuilder();

        if(sharedPreferences.getString("seen_notifications", null) == null){
            seen_notifications = null;
            for (int i = 0; i < array.length(); i++){
                try {
                    to_append.append(array.getJSONObject(i).getString("id")).append(";");
                }
                catch (Exception e){
                    System.out.println("Appending Problem");
                }
            }
            notifications_to_see = array.length();
        }
        else{
            seen_notifications = sharedPreferences.getString("seen_notifications", null).split(";");
            for (int i = 0; i < seen_notifications.length; i++){
                to_append.append(seen_notifications[i]).append(";");
            }
            for (int i = 0; i < array.length(); i++){
                String id_string = new String();
                try {
                    id_string = array.getJSONObject(i).getString("id");
                }
                catch (Exception e){

                }
                System.out.println(id_string);
                if(!Arrays.asList(seen_notifications).contains(id_string)){
                    notifications_to_see++;
                    try {
                        to_append.append(id_string).append(";");
                    }
                    catch (Exception e){
                        System.out.println("In appending notification");
                    }
                }
            }
        }

        for(int i = 0; i<array.length(); i++) {

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                String startTime = json.getString("date");
                String speaker = json.getString("title");
                String topic = json.getString("description");

                System.out.println(startTime + " " + speaker + " " + topic);

                scheduleValue svdata = new scheduleValue(startTime, "", speaker, "", topic);
                getData.add(svdata);
                System.out.println(speaker);

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        //Setting the Adapter
        RecyclerViewNotificationViewAdapter recyclerViewNotificationViewAdapter = new RecyclerViewNotificationViewAdapter(getData, this);
        notificationRecyclerView.setAdapter(recyclerViewNotificationViewAdapter);
        notificationRecyclerView.setVisibility(View.VISIBLE);
        notificationLoaded = true;
        TextView notification_display = findViewById(R.id.no_notification_display);
        notification_display.setVisibility(View.GONE);


        //Checking For Any New Updates And Triggering The Animation
        if(notifications_to_see > 0){
            notificationBtn.setImageResource(getResources().getIdentifier("ic_notifications_alert", "drawable", getPackageName()));
            notificationBtn.startAnimation(shake);
            notification_count = findViewById(R.id.notification_count_view);
            notification_count.setVisibility(View.VISIBLE);
            String count = String.valueOf(notifications_to_see);
            notification_count.setText(count);
        }
        else {
            notificationBtn.setImageResource(getResources().getIdentifier("ic_notifications", "drawable", getPackageName()));
            notification_count = findViewById(R.id.notification_count_view);
            notification_count.setVisibility(View.GONE);
        }

    }

    //Functions to check Internet Connection in the Device

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void networkAvailable() {
        //Removing the Error Message
        internetCheck = findViewById(R.id.internetCheck);
        internetCheck.setVisibility(View.GONE);
    }

    @Override
    public void networkUnavailable() {

        //Displaying the Error Message
        internetCheck = findViewById(R.id.internetCheck);
        Toast.makeText(MainActivity.this, "Network Unavailable", Toast.LENGTH_LONG).show();
        refreshLayoutHome.setRefreshing(false);
        internetCheck.setVisibility(View.VISIBLE);
    }
}
